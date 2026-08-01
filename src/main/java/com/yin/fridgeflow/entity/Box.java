package com.yin.fridgeflow.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 盒子表实体类
 */
@Data
@TableName("box")
public class Box {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;// 主键id

    @TableField("box_code")
    private String boxCode;// 二维码存储的唯一标识，如 FF-box-0001

    @TableField("status")
    private Integer status;// 状态：0空闲 1使用中

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;// 创建时间
}
