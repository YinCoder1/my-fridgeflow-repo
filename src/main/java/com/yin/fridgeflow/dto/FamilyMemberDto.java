package com.yin.fridgeflow.dto;

import lombok.Data;

/**
 * 家庭成员信息（{@link FamilyDto#members} 的元素）。
 *
 * @author yin
 */
@Data
public class FamilyMemberDto {

    /** 用户 ID */
    private Long userId;

    /** 微信昵称 */
    private String nickname;

    /** 微信头像地址 */
    private String avatarUrl;

    /** 角色：0普通成员 1管理员 */
    private Integer role;
}
