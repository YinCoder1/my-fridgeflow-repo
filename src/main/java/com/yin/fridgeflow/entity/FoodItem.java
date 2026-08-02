package com.yin.fridgeflow.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 食材记录表实体类（对应数据库表 food_item）。
 * <p>记录家庭中每个食材的存放、保质期与状态信息。</p>
 *
 * <p><b>状态机（status 字段）：</b></p>
 * <ul>
 *   <li>0 - 已删除/已吃完（软删除标记，不再展示）</li>
 *   <li>1 - 正常（在保鲜期内）</li>
 *   <li>2 - 临期（接近过期，由后续定时任务或查询计算得出）</li>
 * </ul>
 *
 * @author yin
 */
@Data
@TableName("food_item")
public class FoodItem {

    /** 主键 ID，数据库自增 */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 关联的盒子 ID（对应 box.id），一个盒子可放多个食材 */
    @TableField("box_id")
    private Long boxId;

    /** 归属家庭 ID（对应 family.id），当前阶段写死为 1 */
    @TableField("family_id")
    private Long familyId;

    /** 食材名称，长度 1~50 */
    @TableField("name")
    private String name;

    /** 开封/购买日期，格式 yyyy-MM-dd */
    @TableField("start_date")
    private LocalDate startDate;

    /** 保质期天数，取值 ≥1 */
    @TableField("expire_days")
    private Integer expireDays;

    /** 过期日期（冗余计算字段，= startDate + expireDays），格式 yyyy-MM-dd */
    @TableField("expired_date")
    private LocalDate expiredDate;

    /** 状态：0已删除 1正常 2临期 */
    @TableField("status")
    private Integer status;

    /** 添加者用户 ID（对应 user.id），当前阶段写死为 1 */
    @TableField("creator_id")
    private Long creatorId;

    /** 创建时间，由 MyMetaObjectHandler 在 INSERT 时自动填充 */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 修改时间，由 MyMetaObjectHandler 在 INSERT/UPDATE 时自动填充 */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
