package com.yin.fridgeflow.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 采购清单表实体类
 */
@Data
@TableName("shopping_item")
public class ShoppingItem {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;// 主键id

    @TableField("family_id")
    private Long familyId;// 归属家庭ID

    @TableField("ingredient_name")
    private String ingredientName;// 食材名称

    @TableField("quantity")
    private String quantity;// 数量描述

    @TableField("status")
    private Integer status;// 状态：0待购买 1预占中 2已购买

    @TableField("claimed_by")
    private Long claimedBy;// 认领者用户ID

    @TableField("claim_time")
    private LocalDateTime claimTime;// 认领时间

    @Version
    @TableField("version")
    private Integer version;// 乐观锁版本号
}
