package com.yin.fridgeflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 加入家庭请求体（接口8：POST /api/family/join）。
 * <p>凭邀请码加入家庭，以普通成员身份加入。</p>
 *
 * @author yin
 */
@Data
public class JoinFamilyRequest {

    /** 家庭邀请码，格式 FF-XXXXXX */
    @NotBlank(message = "邀请码不能为空")
    private String inviteCode;

    /** 加入者用户 ID（当前阶段无认证，由前端传入；接入 JWT 后改为从 token 提取） */
    @NotNull(message = "userId不能为空")
    private Long userId;
}
