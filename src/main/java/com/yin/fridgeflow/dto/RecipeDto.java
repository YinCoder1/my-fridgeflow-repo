package com.yin.fridgeflow.dto;

import lombok.Data;

/**
 * 食谱列表项（接口11：GET /api/recipes）。
 * <p>除基本信息外，附带所需食材种数 ingredientCount，便于前端展示。</p>
 *
 * @author yin
 */
@Data
public class RecipeDto {

    /** 食谱 ID */
    private Integer id;

    /** 食谱名称 */
    private String name;

    /** 封面图片地址 */
    private String imageUrl;

    /** 简介/做法描述 */
    private String description;

    /** 该食谱所需食材种数（由 recipe_ingredient 表聚合得出） */
    private Integer ingredientCount;
}
