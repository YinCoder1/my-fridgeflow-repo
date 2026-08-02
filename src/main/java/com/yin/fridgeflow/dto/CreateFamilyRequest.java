package com.yin.fridgeflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 创建家庭请求体（接口7：POST /api/family）。
 * <p>创建者将以管理员身份加入该家庭。</p>
 *
 * @author yin
 */
@Data
public class CreateFamilyRequest {

    /** 家庭名称，最长 50 */
    @NotBlank(message = "家庭名称不能为空")
    @Size(max = 50, message = "家庭名称最长50")
    private String name;

    /** 创建者用户 ID（当前阶段无认证，由前端传入；接入 JWT 后改为从 token 提取） */
    @NotNull(message = "creatorId不能为空")
    private Long creatorId;
}
