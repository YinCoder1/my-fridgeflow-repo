package com.yin.fridgeflow.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户表实体类
 */
@Data
@TableName("user")//表名
public class User {

    @TableId(value = "id", type = IdType.AUTO)//主键
    private Integer id;// id

    @TableField("openid")
    private String openid;//微信openid

    @TableField("nickname")
    private String nickname;//微信昵称

    @TableField("avatar_url")
    private String avatarUrl;//微信头像地址

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;//创建时间
}
