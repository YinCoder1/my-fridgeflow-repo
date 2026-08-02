package com.yin.fridgeflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

/**
 * 食材传输对象（接口1列表/接口2扫码出参）。
 * <p>聚合了 entity 的字段，并补充前端需要的派生字段：daysLeft（剩余天数）、boxCode（盒子二维码）。</p>
 *
 * @author yin
 */
@Data
public class FoodItemDto {

    /** 食材主键 ID */
    private Integer id;

    /** 食材名称 */
    private String name;

    /** 开封/购买日期，格式 yyyy-MM-dd */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    /** 过期日期，格式 yyyy-MM-dd */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiredDate;

    /** 保质期天数 */
    private Integer expireDays;

    /** 状态：0已删除 1正常 2临期 */
    private Integer status;

    /** 还剩几天过期（负数表示已过期天数），由 Service 计算 */
    private Long daysLeft;

    /** 盒子二维码（由 box 表关联查出，前端不传 boxId） */
    private String boxCode;
}
