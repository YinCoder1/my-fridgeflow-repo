package com.yin.fridgeflow.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 食谱表实体类
 */
@Data
@TableName("recipe")//食谱表
public class Recipe {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;// 主键id

    @TableField("name")
    private String name;// 食谱名称

    @TableField("image_url")
    private String imageUrl;// 图片地址

    @TableField("description")
    private String description;// 简介

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;// 创建时间
}
