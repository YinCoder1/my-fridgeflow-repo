package com.yin.fridgeflow.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
/**
 * 家庭表实体类
 */
@Data
@TableName("family")//表名
public class Family {

    @TableId(value = "id", type = IdType.AUTO)//主键
    private Integer id;// id

    @TableField("name")
    private String name;//家庭名称

    @TableField("invite_code")
    private String inviteCode;//邀请码

    @TableField("creator_id")
    private Long creatorId;//创建者id

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;//创建时间
}
