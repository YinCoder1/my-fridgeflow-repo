package com.yin.fridgeflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yin.fridgeflow.dto.GenerateShoppingRequest;
import com.yin.fridgeflow.dto.ShoppingItemDto;
import com.yin.fridgeflow.entity.FoodItem;
import com.yin.fridgeflow.entity.Recipe;
import com.yin.fridgeflow.entity.RecipeIngredient;
import com.yin.fridgeflow.entity.ShoppingItem;
import com.yin.fridgeflow.mapper.FoodItemMapper;
import com.yin.fridgeflow.mapper.RecipeIngredientMapper;
import com.yin.fridgeflow.mapper.RecipeMapper;
import com.yin.fridgeflow.mapper.ShoppingItemMapper;
import com.yin.fridgeflow.service.ShoppingService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 采购清单服务实现类。
 * <p>实现采购清单的生成、查询与认领流转。
 * 认领/取消/完成操作携带 version 字段走乐观锁，保证并发安全。</p>
 *
 * @author yin
 */
@Service
public class ShoppingServiceImpl implements ShoppingService {

    @Autowired
    private ShoppingItemMapper shoppingItemMapper;

    @Autowired
    private RecipeMapper recipeMapper;

    @Autowired
    private RecipeIngredientMapper recipeIngredientMapper;

    @Autowired
    private FoodItemMapper foodItemMapper;

    /**
     * 生成采购清单（接口13）。
     * <p>事务保证：批量插入与查询原子完成。</p>
     * <p>步骤：①校验食谱 ②查食谱所需食材 ③查家庭库存名称集合 ④查采购清单中待购买/预占中的同名项集合
     * ⑤库存已有或采购清单已存在的同名项跳过，避免重复生成 ⑥返回该家庭当前采购项。</p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ShoppingItemDto> generate(GenerateShoppingRequest request) {
        // 1. 校验食谱存在
        Recipe recipe = recipeMapper.selectById(request.getRecipeId());
        if (recipe == null) {
            throw new RuntimeException("食谱不存在");   // → Controller 转 404
        }

        // 2. 查食谱所需食材
        LambdaQueryWrapper<RecipeIngredient> riWrapper = new LambdaQueryWrapper<>();
        riWrapper.eq(RecipeIngredient::getRecipeId, request.getRecipeId());
        List<RecipeIngredient> ingredients = recipeIngredientMapper.selectList(riWrapper);

        // 3. 查家庭库存名称集合（未删除）
        LambdaQueryWrapper<FoodItem> foodWrapper = new LambdaQueryWrapper<>();
        foodWrapper.eq(FoodItem::getFamilyId, request.getFamilyId())
                   .ne(FoodItem::getStatus, 0);
        Set<String> stockNames = foodItemMapper.selectList(foodWrapper).stream()
                .map(FoodItem::getName)
                .collect(Collectors.toSet());

        // 4. 查采购清单中已存在的待购买/预占中项名称（避免重复生成）
        LambdaQueryWrapper<ShoppingItem> existWrapper = new LambdaQueryWrapper<>();
        existWrapper.eq(ShoppingItem::getFamilyId, request.getFamilyId())
                    .in(ShoppingItem::getStatus, 0, 1);
        Set<String> pendingNames = shoppingItemMapper.selectList(existWrapper).stream()
                .map(ShoppingItem::getIngredientName)
                .collect(Collectors.toSet());

        // 5. 为缺失食材创建采购项
        for (RecipeIngredient ri : ingredients) {
            String name = ri.getIngredientName();
            if (stockNames.contains(name) || pendingNames.contains(name)) {
                continue; // 库存已有 或 已在采购清单中 → 跳过
            }
            ShoppingItem item = new ShoppingItem();
            item.setFamilyId(request.getFamilyId());
            item.setIngredientName(name);
            item.setQuantity(ri.getQuantity());
            item.setStatus(0); // 待购买
            shoppingItemMapper.insert(item);
            pendingNames.add(name); // 防止同食谱内重复食材重复插入
        }

        // 6. 返回该家庭当前待购买/预占中的采购项
        return list(request.getFamilyId(), null);
    }

    /**
     * 获取采购清单（接口14）。
     * <p>按状态降序、ID 升序排列，便于前端优先展示待处理项。</p>
     */
    @Override
    public List<ShoppingItemDto> list(Long familyId, Integer status) {
        LambdaQueryWrapper<ShoppingItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingItem::getFamilyId, familyId);
        if (status != null) {
            wrapper.eq(ShoppingItem::getStatus, status);
        }
        wrapper.orderByDesc(ShoppingItem::getStatus).orderByAsc(ShoppingItem::getId);
        List<ShoppingItem> items = shoppingItemMapper.selectList(wrapper);

        // entity → dto（同名字段自动拷贝）
        return items.stream().map(item -> {
            ShoppingItemDto dto = new ShoppingItemDto();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 认领采购项（接口15）。
     * <p>仅 status=0 可认领；置为预占中(1)并记录认领人、时间。
     * updateById 携带 version 字段，由乐观锁插件保证并发安全。</p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void claim(Long itemId, Long userId) {
        ShoppingItem item = shoppingItemMapper.selectById(itemId);
        if (item == null) {
            throw new RuntimeException("采购项不存在");   // → Controller 转 404
        }
        if (item.getStatus() == null || item.getStatus() != 0) {
            throw new RuntimeException("该采购项已被认领或已完成");
        }
        item.setStatus(1);
        item.setClaimedBy(userId);
        item.setClaimTime(LocalDateTime.now());
        shoppingItemMapper.updateById(item);   // 携带 version 走乐观锁
    }

    /**
     * 取消认领（接口16）。
     * <p>仅 status=1 可取消；置回待购买(0)并清空认领人、时间。</p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unclaim(Long itemId) {
        ShoppingItem item = shoppingItemMapper.selectById(itemId);
        if (item == null) {
            throw new RuntimeException("采购项不存在");   // → Controller 转 404
        }
        if (item.getStatus() == null || item.getStatus() != 1) {
            throw new RuntimeException("该采购项未被认领");
        }
        item.setStatus(0);
        item.setClaimedBy(null);
        item.setClaimTime(null);
        shoppingItemMapper.updateById(item);
    }

    /**
     * 完成购买（接口17）。
     * <p>仅 status=1 可完成；置为已购买(2)。</p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void complete(Long itemId) {
        ShoppingItem item = shoppingItemMapper.selectById(itemId);
        if (item == null) {
            throw new RuntimeException("采购项不存在");   // → Controller 转 404
        }
        if (item.getStatus() == null || item.getStatus() != 1) {
            throw new RuntimeException("请先认领后再完成购买");
        }
        item.setStatus(2);
        shoppingItemMapper.updateById(item);
    }
}
