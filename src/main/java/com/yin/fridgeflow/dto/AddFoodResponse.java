package com.yin.fridgeflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

/**
 * 添加食材响应体（接口3：POST /api/food/add）。
 * <p>只回 id / name / expiredDate 三个字段，与接口契约一致。</p>
 *
 * @author yin
 */
@Data
public class AddFoodResponse {

    /** 新创建的食材主键 ID */
    private Integer id;

    /** 食材名称 */
    private String name;

    /** 过期日期（= startDate + expireDays），格式 yyyy-MM-dd */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiredDate;
}
