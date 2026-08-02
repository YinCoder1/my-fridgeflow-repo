package com.yin.fridgeflow.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 家庭表实体类（对应数据库表 family）。
 * <p>一个家庭包含多个成员（user_family 关联），食材与采购清单均归属家庭。</p>
 * <p>邀请码（inviteCode）用于其他用户加入家庭，格式为 {@code FF-XXXXXX}（6位大写字母数字）。</p>
 *
 * @author yin
 */
@Data
@TableName("family")
public class Family {

    /** 主键 ID，数据库自增 */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 家庭名称，最长 50 */
    @TableField("name")
    private String name;

    /** 邀请码，格式 FF-XXXXXX，创建家庭时随机生成 */
    @TableField("invite_code")
    private String inviteCode;

    /** 创建者用户 ID（对应 user.id），即家庭管理员 */
    @TableField("creator_id")
    private Long creatorId;

    /** 创建时间，由 MyMetaObjectHandler 在 INSERT 时自动填充 */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
