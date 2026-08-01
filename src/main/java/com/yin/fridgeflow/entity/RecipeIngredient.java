package com.yin.fridgeflow.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 食谱食材关联表实体类
 */
@Data
@TableName("recipe_ingredient")//食谱食材关联表
public class RecipeIngredient {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;// 主键id

    @TableField("recipe_id")
    private Long recipeId;// 食谱ID

    @TableField("ingredient_name")
    private String ingredientName;// 食材名称

    @TableField("quantity")
    private String quantity;// 数量描述（如“2个”）

    @TableField("is_optional")
    private Integer isOptional;// 是否可选：0必需 1可选
}
