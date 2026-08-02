package com.yin.fridgeflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yin.fridgeflow.dto.AddFoodRequest;
import com.yin.fridgeflow.dto.AddFoodResponse;
import com.yin.fridgeflow.dto.FoodItemDto;
import com.yin.fridgeflow.dto.UpdateFoodRequest;
import com.yin.fridgeflow.entity.Box;
import com.yin.fridgeflow.entity.FoodItem;
import com.yin.fridgeflow.mapper.BoxMapper;
import com.yin.fridgeflow.mapper.FoodItemMapper;
import com.yin.fridgeflow.service.FoodItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 食材记录表服务实现类。
 * <p>实现食材的增删改查及盒子状态联动（放入食材→盒子变使用中，清空食材→盒子变空闲）。</p>
 *
 * @author yin
 */
@Service
public class FoodItemServiceImpl implements FoodItemService {

    @Autowired
    private FoodItemMapper foodItemMapper;

    @Autowired
    private BoxMapper boxMapper;

    /**
     * 获取食材列表（接口1）。
     * <p>固定 family_id=1；status 为 null 时排除已删除项，否则按值精确筛选；
     * 结果按过期日期升序，便于前端优先展示临期食材。</p>
     */
    @Override
    public List<FoodItemDto> list(Integer status) {
        // 构造查询条件
        LambdaQueryWrapper<FoodItem> wrapper = new LambdaQueryWrapper<>();

        // 第一阶段：写死 family_id = 1
        wrapper.eq(FoodItem::getFamilyId, 1L);

        // status 筛选：不传则排除已删除，传了就按传入值筛选
        if (status != null) {
            wrapper.eq(FoodItem::getStatus, status);
        } else {
            wrapper.ne(FoodItem::getStatus, 0);   // 排除已删除
        }

        // 按过期日期升序（最早过期的排前面）
        wrapper.orderByAsc(FoodItem::getExpiredDate);

        List<FoodItem> foodItems = foodItemMapper.selectList(wrapper);

        // 转换为 DTO（并计算派生字段 daysLeft）
        return foodItems.stream().map(item -> {
            FoodItemDto dto = new FoodItemDto();
            dto.setId(item.getId());
            dto.setName(item.getName());
            dto.setStartDate(item.getStartDate());
            dto.setExpiredDate(item.getExpiredDate());
            dto.setExpireDays(item.getExpireDays());
            dto.setStatus(item.getStatus());
            // 计算剩余天数（负数表示已过期天数）
            dto.setDaysLeft(ChronoUnit.DAYS.between(LocalDate.now(), item.getExpiredDate()));
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 获取盒子信息（接口2扫码）。
     * <p>先校验盒子存在性，再取该盒下未删除的最新一条食材（按 id 倒序）。</p>
     */
    @Override
    public FoodItemDto getBoxList(String boxCode) {
        // 1. 查 box 表，校验盒子是否存在
        LambdaQueryWrapper<Box> boxWrapper = new LambdaQueryWrapper<>();
        boxWrapper.eq(Box::getBoxCode, boxCode);
        Box box = boxMapper.selectOne(boxWrapper);
        if (box == null) {
            return null;   // 盒子不存在 → Controller 转 404
        }

        // 2. 查该盒子下未删除的最新一条食材（一个盒子可放多个，这里取最近添加的一条）
        LambdaQueryWrapper<FoodItem> foodWrapper = new LambdaQueryWrapper<>();
        foodWrapper.eq(FoodItem::getBoxId, box.getId())
                   .ne(FoodItem::getStatus, 0)
                   .orderByDesc(FoodItem::getId)
                   .last("LIMIT 1");
        FoodItem foodItem = foodItemMapper.selectOne(foodWrapper);

        // 3. 封装 DTO（盒子未绑定食材时返回仅带 boxCode 的空 DTO，id 为 null）
        FoodItemDto dto = new FoodItemDto();
        dto.setBoxCode(box.getBoxCode());
        if (foodItem == null) {
            return dto;   // 盒子空闲 → Controller 返回 "盒子未绑定食材"
        }

        BeanUtils.copyProperties(foodItem, dto);   // 同名字段自动拷贝
        dto.setBoxCode(box.getBoxCode());          // entity 无 boxCode，单独补
        dto.setDaysLeft(ChronoUnit.DAYS.between(LocalDate.now(), foodItem.getExpiredDate()));
        return dto;
    }

    /**
     * 向盒子添加食材（接口3）。
     * <p>事务保证：插入食材与更新盒子状态要么都成功要么都回滚。</p>
     * <p>业务步骤：①校验 boxCode 存在 ②算过期日期 ③插食材(family_id/creator_id 写死 1) ④盒子空闲则置使用中 ⑤组装响应。</p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddFoodResponse addFood(AddFoodRequest request) {
        // 1. 校验 boxCode 存在
        LambdaQueryWrapper<Box> boxWrapper = new LambdaQueryWrapper<>();
        boxWrapper.eq(Box::getBoxCode, request.getBoxCode());
        Box box = boxMapper.selectOne(boxWrapper);
        if (box == null) {
            throw new RuntimeException("无效二维码");   // → Controller 转 404
        }

        // 2. 计算过期日期 = startDate + expireDays
        LocalDate expiredDate = request.getStartDate().plusDays(request.getExpireDays());

        // 3. 创建 FoodItem 记录，family_id 和 creator_id 暂写死为 1
        FoodItem foodItem = new FoodItem();
        foodItem.setBoxId(box.getId().longValue());
        foodItem.setFamilyId(1L);
        foodItem.setCreatorId(1L);
        foodItem.setName(request.getName());
        foodItem.setStartDate(request.getStartDate());
        foodItem.setExpireDays(request.getExpireDays());
        foodItem.setExpiredDate(expiredDate);
        foodItem.setStatus(1); // 1 正常
        foodItemMapper.insert(foodItem);   // insert 后 foodItem.id 被回填

        // 4. 盒子当前 status==0 时，更新为 1（使用中）
        if (box.getStatus() != null && box.getStatus() == 0) {
            box.setStatus(1);
            boxMapper.updateById(box);
        }

        // 5. 返回新创建的食材信息（按契约只回 id/name/expiredDate）
        AddFoodResponse resp = new AddFoodResponse();
        resp.setId(foodItem.getId());
        resp.setName(foodItem.getName());
        resp.setExpiredDate(foodItem.getExpiredDate());
        return resp;
    }

    /**
     * 修改食材信息（接口4）。
     * <p>校验未被删除后更新字段，并按新入参重新计算 expiredDate。</p>
     */
    @Override
    public void updateFood(Long id, UpdateFoodRequest request) {
        // 1. 根据 id 查询
        FoodItem foodItem = foodItemMapper.selectById(id);
        if (foodItem == null || foodItem.getStatus() == null || foodItem.getStatus() == 0) {
            throw new RuntimeException("食材不存在");   // → Controller 转 404
        }

        // 2. 更新字段，重新计算 expiredDate
        foodItem.setName(request.getName());
        foodItem.setStartDate(request.getStartDate());
        foodItem.setExpireDays(request.getExpireDays());
        foodItem.setExpiredDate(request.getStartDate().plusDays(request.getExpireDays()));

        foodItemMapper.updateById(foodItem);   // updateTime 由 MetaObjectHandler 自动填充
    }

    /**
     * 删除食材（接口5，软删除）。
     * <p>事务保证：软删食材与释放盒子原子完成。</p>
     * <p>业务步骤：①软删(置 status=0) ②查该盒下是否还有其他有效食材 ③若无则释放盒子(置 status=0)。</p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFood(Long id) {
        // 1. 查询食材
        FoodItem foodItem = foodItemMapper.selectById(id);
        if (foodItem == null) {
            throw new RuntimeException("食材不存在");   // → Controller 转 404
        }

        // 2. 软删除（标记 status=0，保留记录便于追溯）
        foodItem.setStatus(0);
        foodItemMapper.updateById(foodItem);

        // 3. 查该盒子下是否还有其他有效食材（status != 0，已含当前被软删项会被排除）
        LambdaQueryWrapper<FoodItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FoodItem::getBoxId, foodItem.getBoxId())
               .ne(FoodItem::getStatus, 0);
        Long count = foodItemMapper.selectCount(wrapper);

        // 4. 没有其他有效食材 → 释放盒子（置空闲）
        if (count == null || count == 0L) {
            Box box = boxMapper.selectById(foodItem.getBoxId());
            if (box != null) {
                box.setStatus(0);
                boxMapper.updateById(box);
            }
        }
    }
}
