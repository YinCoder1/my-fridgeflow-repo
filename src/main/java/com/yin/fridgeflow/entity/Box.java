package com.yin.fridgeflow.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 盒子表实体类（对应数据库表 box）。
 * <p>每个保鲜盒对应一个二维码，二维码存储的唯一标识（如 FF-box-0001）即 boxCode。</p>
 *
 * <p><b>状态（status 字段）：</b></p>
 * <ul>
 *   <li>0 - 空闲（未放食材）</li>
 *   <li>1 - 使用中（已放食材）</li>
 * </ul>
 *
 * @author yin
 */
@Data
@TableName("box")
public class Box {

    /** 主键 ID，数据库自增 */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 二维码存储的唯一标识，如 FF-box-0001（扫码接口凭此查找盒子） */
    @TableField("box_code")
    private String boxCode;

    /** 状态：0空闲 1使用中；放入食材时由 0→1，盒子内食材被全部删除后由 1→0 */
    @TableField("status")
    private Integer status;

    /** 创建时间，由 MyMetaObjectHandler 在 INSERT 时自动填充 */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
