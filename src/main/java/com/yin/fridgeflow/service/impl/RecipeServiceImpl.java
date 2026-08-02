package com.yin.fridgeflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yin.fridgeflow.dto.RecipeDto;
import com.yin.fridgeflow.dto.RecipeMatchResultDto;
import com.yin.fridgeflow.entity.FoodItem;
import com.yin.fridgeflow.entity.Recipe;
import com.yin.fridgeflow.entity.RecipeIngredient;
import com.yin.fridgeflow.mapper.FoodItemMapper;
import com.yin.fridgeflow.mapper.RecipeIngredientMapper;
import com.yin.fridgeflow.mapper.RecipeMapper;
import com.yin.fridgeflow.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 食谱服务实现类。
 * <p>实现食谱列表查询与库存匹配。</p>
 *
 * @author yin
 */
@Service
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private RecipeMapper recipeMapper;

    @Autowired
    private RecipeIngredientMapper recipeIngredientMapper;

    @Autowired
    private FoodItemMapper foodItemMapper;

    /**
     * 食谱列表（接口11）。
     * <p>一次性查出所有食谱及其食材关联，按 recipeId 分组计数，
     * 避免逐食谱查询产生 N+1。</p>
     */
    @Override
    public List<RecipeDto> list() {
        List<Recipe> recipes = recipeMapper.selectList(null);

        // 一次性查出所有食谱食材，按 recipeId 分组计数，避免 N+1
        List<RecipeIngredient> all = recipeIngredientMapper.selectList(null);
        java.util.Map<Long, Long> countMap = all.stream()
                .collect(Collectors.groupingBy(RecipeIngredient::getRecipeId, Collectors.counting()));

        return recipes.stream().map(r -> {
            RecipeDto dto = new RecipeDto();
            dto.setId(r.getId());
            dto.setName(r.getName());
            dto.setImageUrl(r.getImageUrl());
            dto.setDescription(r.getDescription());
            dto.setIngredientCount(countMap.getOrDefault(r.getId().longValue(), 0L).intValue());
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 食谱匹配库存（接口12）。
     * <p>步骤：①查食谱（校验存在） ②查食谱所需食材 ③查家庭未删除库存的食材名称集合
     * ④逐项判断是否在库（精确名匹配） ⑤汇总 matchedCount/missingCount。</p>
     */
    @Override
    public RecipeMatchResultDto match(Long recipeId, Long familyId) {
        // 1. 查食谱
        Recipe recipe = recipeMapper.selectById(recipeId);
        if (recipe == null) {
            throw new RuntimeException("食谱不存在");   // → Controller 转 404
        }

        // 2. 查食谱所需食材
        LambdaQueryWrapper<RecipeIngredient> riWrapper = new LambdaQueryWrapper<>();
        riWrapper.eq(RecipeIngredient::getRecipeId, recipeId);
        List<RecipeIngredient> ingredients = recipeIngredientMapper.selectList(riWrapper);

        // 3. 查家庭库存（未删除）食材名称集合，用 Set 便于 O(1) 查询
        LambdaQueryWrapper<FoodItem> foodWrapper = new LambdaQueryWrapper<>();
        foodWrapper.eq(FoodItem::getFamilyId, familyId)
                   .ne(FoodItem::getStatus, 0);
        Set<String> stockNames = foodItemMapper.selectList(foodWrapper).stream()
                .map(FoodItem::getName)
                .collect(Collectors.toSet());

        // 4. 逐项匹配（名称精确匹配；如需模糊匹配可改为 contains）
        List<RecipeMatchResultDto.MatchedIngredient> items = ingredients.stream().map(ri -> {
            RecipeMatchResultDto.MatchedIngredient m = new RecipeMatchResultDto.MatchedIngredient();
            m.setName(ri.getIngredientName());
            m.setQuantity(ri.getQuantity());
            m.setIsOptional(ri.getIsOptional());
            m.setInStock(stockNames.contains(ri.getIngredientName()));
            return m;
        }).collect(Collectors.toList());

        int matched = (int) items.stream().filter(i -> i.isInStock()).count();

        // 5. 组装结果
        RecipeMatchResultDto result = new RecipeMatchResultDto();
        result.setRecipeId(recipe.getId());
        result.setRecipeName(recipe.getName());
        result.setIngredients(items);
        result.setMatchedCount(matched);
        result.setMissingCount(items.size() - matched);
        return result;
    }
}
