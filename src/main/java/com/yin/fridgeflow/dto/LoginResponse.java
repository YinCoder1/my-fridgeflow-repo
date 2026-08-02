package com.yin.fridgeflow.dto;

import lombok.Data;

/**
 * 登录响应体（接口6）。
 * <p>返回签发的 JWT token 与用户基本信息。</p>
 *
 * @author yin
 */
@Data
public class LoginResponse {

    /** 签发的 JWT，后续需鉴权的接口放 Authorization 头携带 */
    private String token;

    /** 用户基本信息 */
    private UserInfoDto userInfo;
}
