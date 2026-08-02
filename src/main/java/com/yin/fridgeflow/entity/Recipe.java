package com.yin.fridgeflow.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 食谱表实体类（对应数据库表 recipe）。
 * <p>记录一道菜的基本信息，其所需食材列表存储在 recipe_ingredient 关联表。</p>
 *
 * @author yin
 */
@Data
@TableName("recipe")
public class Recipe {

    /** 主键 ID，数据库自增 */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 食谱名称 */
    @TableField("name")
    private String name;

    /** 食谱封面图片地址 */
    @TableField("image_url")
    private String imageUrl;

    /** 食谱简介/做法描述 */
    @TableField("description")
    private String description;

    /** 创建时间，由 MyMetaObjectHandler 在 INSERT 时自动填充 */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
