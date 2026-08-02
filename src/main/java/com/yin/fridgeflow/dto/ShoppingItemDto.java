package com.yin.fridgeflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 采购项传输对象（接口13/14 出参）。
 *
 * @author yin
 */
@Data
public class ShoppingItemDto {

    /** 采购项主键 ID */
    private Integer id;

    /** 归属家庭 ID */
    private Long familyId;

    /** 食材名称 */
    private String ingredientName;

    /** 数量描述，如 "2个" */
    private String quantity;

    /** 状态：0待购买 1预占中 2已购买 */
    private Integer status;

    /** 认领者用户 ID（对应 user.id），未认领时为 null */
    private Long claimedBy;

    /** 认领时间，未认领时为 null；格式 yyyy-MM-dd HH:mm:ss */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime claimTime;
}
