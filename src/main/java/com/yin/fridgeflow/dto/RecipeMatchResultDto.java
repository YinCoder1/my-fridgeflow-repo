package com.yin.fridgeflow.dto;

import lombok.Data;

import java.util.List;

/**
 * 食谱库存匹配结果（接口12：GET /api/recipes/{id}/match）。
 * <p>逐项比对食谱所需食材是否在家庭库存中，返回匹配明细与汇总数。</p>
 *
 * @author yin
 */
@Data
public class RecipeMatchResultDto {

    /** 食谱 ID */
    private Integer recipeId;

    /** 食谱名称 */
    private String recipeName;

    /** 所需食材的匹配明细列表 */
    private List<MatchedIngredient> ingredients;

    /** 库存中已有的种数 */
    private Integer matchedCount;

    /** 库存中缺失的种数（含可选） */
    private Integer missingCount;

    /**
     * 单个食材的匹配明细。
     */
    @Data
    public static class MatchedIngredient {

        /** 食材名称 */
        private String name;

        /** 数量描述，如 "2个" */
        private String quantity;

        /** 是否可选：0必需 1可选 */
        private Integer isOptional;

        /** 库存中是否有（true=有，false=缺） */
        private boolean inStock;
    }
}
