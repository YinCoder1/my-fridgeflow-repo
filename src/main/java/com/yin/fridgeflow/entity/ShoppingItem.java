package com.yin.fridgeflow.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 采购清单项实体类（对应数据库表 shopping_item）。
 * <p>记录家庭待采购的食材，可由成员认领后购买。</p>
 *
 * <p><b>状态机（status 字段）：</b></p>
 * <ul>
 *   <li>0 - 待购买（刚生成，尚未被认领）</li>
 *   <li>1 - 预占中（已被某成员认领，正在去买）</li>
 *   <li>2 - 已购买（购买完成）</li>
 * </ul>
 *
 * <p><b>乐观锁：</b>version 字段配合 {@code @Version} 注解与
 * OptimisticLockerInnerInterceptor，保证认领/取消/完成操作的并发安全——
 * 并发更新时仅一方成功，另一方需重试。</p>
 *
 * @author yin
 */
@Data
@TableName("shopping_item")
public class ShoppingItem {

    /** 主键 ID，数据库自增 */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 归属家庭 ID（对应 family.id） */
    @TableField("family_id")
    private Long familyId;

    /** 食材名称 */
    @TableField("ingredient_name")
    private String ingredientName;

    /** 数量描述，如 "2个"、"200g" */
    @TableField("quantity")
    private String quantity;

    /** 状态：0待购买 1预占中 2已购买 */
    @TableField("status")
    private Integer status;

    /** 认领者用户 ID（对应 user.id），未认领时为 null */
    @TableField("claimed_by")
    private Long claimedBy;

    /** 认领时间，未认领时为 null */
    @TableField("claim_time")
    private LocalDateTime claimTime;

    /**
     * 乐观锁版本号，{@link com.baomidou.mybatisplus.annotation.Version}。
     * 每次 update 时 MyBatis-Plus 自动 +1 并校验，更新失败表示发生并发冲突。
     */
    @Version
    @TableField("version")
    private Integer version;
}
