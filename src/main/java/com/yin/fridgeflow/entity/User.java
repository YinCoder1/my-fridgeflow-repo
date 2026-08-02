package com.yin.fridgeflow.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户表实体类（对应数据库表 user）。
 * <p>微信用户登录后，按 openid 查找；不存在则自动注册（昵称默认 "微信用户"+openid末4位）。</p>
 *
 * @author yin
 */
@Data
@TableName("user")
public class User {

    /** 主键 ID，数据库自增 */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 微信 openid，唯一标识一个微信用户，登录接口凭此查找/注册 */
    @TableField("openid")
    private String openid;

    /** 微信昵称，注册时默认 "微信用户"+openid末4位，后续可由用户修改 */
    @TableField("nickname")
    private String nickname;

    /** 微信头像地址 */
    @TableField("avatar_url")
    private String avatarUrl;

    /** 创建时间，由 MyMetaObjectHandler 在 INSERT 时自动填充 */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
