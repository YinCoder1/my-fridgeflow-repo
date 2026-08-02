package com.yin.fridgeflow.controller;

import com.yin.fridgeflow.common.Result;
import com.yin.fridgeflow.dto.LoginRequest;
import com.yin.fridgeflow.dto.LoginResponse;
import com.yin.fridgeflow.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证控制器（/api/auth）。
 * <p>提供微信登录接口。</p>
 *
 * @author yin
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "认证接口")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 接口6：用户登录。
     * <p>凭 openid 登录，不存在则自动注册，返回 token 与用户信息。</p>
     *
     * @param request 登录请求（openid）
     * @return 登录响应（token + userInfo）
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录（按 openid）")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse resp = authService.login(request.getOpenid());
        return Result.success(resp);
    }
}
