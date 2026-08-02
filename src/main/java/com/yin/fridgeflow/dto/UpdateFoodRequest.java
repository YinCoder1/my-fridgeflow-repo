package com.yin.fridgeflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * 修改食材请求体（接口4：PUT /api/food/{id}）。
 * <p>校验规则与 {@link AddFoodRequest} 一致（不含 boxCode，盒子不可改）。
 * 过期日期由后端按 startDate + expireDays 重新计算，客户端无需传。</p>
 *
 * @author yin
 */
@Data
public class UpdateFoodRequest {

    /** 食材名称，长度 1~50 */
    @NotBlank(message = "食材名不能为空")
    @Size(min = 1, max = 50, message = "食材名长度1~50")
    private String name;

    /** 开封/购买日期，格式 yyyy-MM-dd */
    @NotNull(message = "开封日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    /** 保质期天数，取值 ≥1 */
    @NotNull(message = "保质期不能为空")
    @Min(value = 1, message = "保质期至少1天")
    private Integer expireDays;
}
