package com.yin.fridgeflow.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户家庭关联表
 */
@Data
@TableName("user_family")//用户家庭关联表
public class UserFamily {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;// id

    @TableField("user_id")
    private Long userId;//用户id

    @TableField("family_id")
    private Long familyId;//家庭id

    @TableField("role")
    private Integer role; // 0普通成员 1管理员

    @TableField(value = "join_time", fill = FieldFill.INSERT)
    private LocalDateTime joinTime;//加入时间
}