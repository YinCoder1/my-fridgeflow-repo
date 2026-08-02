package com.yin.fridgeflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * 添加食材请求体（接口3：POST /api/food/add）。
 * <p>校验规则：boxCode/name 非空，name 长度 1~50，startDate 非空，expireDays ≥1。</p>
 *
 * @author yin
 */
@Data
public class AddFoodRequest {

    /** 盒子二维码，必须存在于 box 表 */
    @NotBlank(message = "盒子码不能为空")
    private String boxCode;

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
