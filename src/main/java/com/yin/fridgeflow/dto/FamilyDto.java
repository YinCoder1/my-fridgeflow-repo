package com.yin.fridgeflow.dto;

import lombok.Data;

import java.util.List;

/**
 * 家庭信息（接口7/8/9 返回），聚合成员列表。
 *
 * @author yin
 */
@Data
public class FamilyDto {

    /** 家庭 ID */
    private Integer id;

    /** 家庭名称 */
    private String name;

    /** 邀请码，格式 FF-XXXXXX */
    private String inviteCode;

    /** 创建者用户 ID（管理员） */
    private Long creatorId;

    /** 家庭成员列表 */
    private List<FamilyMemberDto> members;
}
