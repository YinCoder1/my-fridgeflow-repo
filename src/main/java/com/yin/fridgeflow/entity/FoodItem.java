package com.yin.fridgeflow.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 食材记录表实体类
 */
@Data
@TableName("food_item")
public class FoodItem {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;// 主键id

    @TableField("box_id")
    private Long boxId;// 关联的盒子ID

    @TableField("family_id")
    private Long familyId;// 归属家庭ID

    @TableField("name")
    private String name;// 食材名称

    @TableField("start_date")
    private LocalDate startDate;// 开封/购买日期

    @TableField("expire_days")
    private Integer expireDays;// 保质期天数

    @TableField("expired_date")
    private LocalDate expiredDate;// 过期日期（冗余计算字段）

    @TableField("status")
    private Integer status;// 状态：0已删除 1正常 2临期

    @TableField("creator_id")
    private Long creatorId;// 添加者用户ID

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;// 创建时间

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;// 修改时间
}
