package com.yin.fridgeflow.service;

import com.yin.fridgeflow.dto.LoginResponse;

/**
 * 认证服务接口。
 *
 * @author yin
 */
public interface AuthService {

    /**
     * 微信登录（接口6）。
     * <p>按 openid 查找用户，不存在则自动注册（昵称默认 "微信用户"+openid末4位），并签发 JWT。</p>
     *
     * @param openid 微信 openid
     * @return 登录响应（token + userInfo）
     */
    LoginResponse login(String openid);
}
