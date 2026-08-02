package com.yin.fridgeflow.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户-家庭关联表实体类（对应数据库表 user_family）。
 * <p>多对多关联：一个用户可加入家庭（当前阶段限制每个用户只属于一个家庭），
 * 一个家庭包含多个成员。</p>
 *
 * <p><b>角色（role 字段）：</b></p>
 * <ul>
 *   <li>0 - 普通成员</li>
 *   <li>1 - 管理员（家庭创建者，不可被移除）</li>
 * </ul>
 *
 * @author yin
 */
@Data
@TableName("user_family")
public class UserFamily {

    /** 主键 ID，数据库自增 */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 用户 ID（对应 user.id） */
    @TableField("user_id")
    private Long userId;

    /** 家庭 ID（对应 family.id） */
    @TableField("family_id")
    private Long familyId;

    /** 角色：0普通成员 1管理员（管理员不可被移除） */
    @TableField("role")
    private Integer role;

    /** 加入时间，由 MyMetaObjectHandler 在 INSERT 时自动填充 */
    @TableField(value = "join_time", fill = FieldFill.INSERT)
    private LocalDateTime joinTime;
}
