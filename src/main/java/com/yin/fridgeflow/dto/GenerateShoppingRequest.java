package com.yin.fridgeflow.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 生成采购清单请求体（接口13：POST /api/shopping/generate）。
 * <p>根据指定食谱的缺失食材生成采购项（库存已有或采购清单已存在的同名项会跳过）。</p>
 *
 * @author yin
 */
@Data
public class GenerateShoppingRequest {

    /** 食谱 ID（对应 recipe.id） */
    @NotNull(message = "recipeId不能为空")
    private Long recipeId;

    /** 家庭 ID（对应 family.id） */
    @NotNull(message = "familyId不能为空")
    private Long familyId;
}
