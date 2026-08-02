package com.yin.fridgeflow.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录请求体（接口6：POST /api/auth/login）。
 * <p>微信登录：凭 openid 查找用户，不存在则自动注册。</p>
 *
 * @author yin
 */
@Data
public class LoginRequest {

    /** 微信 openid，非空 */
    @NotBlank(message = "openid不能为空")
    private String openid;
}
