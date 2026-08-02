package com.yin.fridgeflow.service;

import com.yin.fridgeflow.dto.RecipeDto;
import com.yin.fridgeflow.dto.RecipeMatchResultDto;

import java.util.List;

/**
 * 食谱服务接口。
 *
 * @author yin
 */
public interface RecipeService {

    /**
     * 食谱列表（接口11）。
     *
     * @return 食谱列表，每项含所需食材种数
     */
    List<RecipeDto> list();

    /**
     * 食谱匹配库存（接口12）。
     * <p>逐项比对食谱所需食材是否在指定家庭的未删除库存中。</p>
     *
     * @param recipeId 食谱 ID
     * @param familyId 家庭 ID
     * @return 匹配结果（明细列表 + matchedCount/missingCount）
     * @throws RuntimeException 食谱不存在时抛 "食谱不存在"
     */
    RecipeMatchResultDto match(Long recipeId, Long familyId);
}
