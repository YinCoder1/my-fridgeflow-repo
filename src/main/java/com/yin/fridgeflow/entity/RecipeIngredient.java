package com.yin.fridgeflow.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 食谱-食材关联表实体类（对应数据库表 recipe_ingredient）。
 * <p>记录某食谱所需的一种食材及其数量。食谱匹配库存时，
 * 逐项比对该食材是否在家庭库存（food_item）中。</p>
 *
 * <p><b>是否可选（isOptional 字段）：</b></p>
 * <ul>
 *   <li>0 - 必需（缺货则需采购）</li>
 *   <li>1 - 可选（缺货不影响做菜）</li>
 * </ul>
 *
 * @author yin
 */
@Data
@TableName("recipe_ingredient")
public class RecipeIngredient {

    /** 主键 ID，数据库自增 */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 食谱 ID（对应 recipe.id） */
    @TableField("recipe_id")
    private Long recipeId;

    /** 食材名称（与 food_item.name 比对，判断库存是否充足） */
    @TableField("ingredient_name")
    private String ingredientName;

    /** 数量描述，如 "2个"、"200g" */
    @TableField("quantity")
    private String quantity;

    /** 是否可选：0必需 1可选 */
    @TableField("is_optional")
    private Integer isOptional;
}
