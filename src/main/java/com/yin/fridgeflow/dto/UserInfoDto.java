package com.yin.fridgeflow.dto;

import lombok.Data;

/**
 * 用户基本信息（登录响应中嵌套的用户信息）。
 *
 * @author yin
 */
@Data
public class UserInfoDto {

    /** 用户 ID */
    private Integer id;

    /** 微信昵称 */
    private String nickname;
}
