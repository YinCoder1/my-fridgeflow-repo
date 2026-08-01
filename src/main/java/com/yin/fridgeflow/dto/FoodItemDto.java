package com.yin.fridgeflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FoodItemDto {
    private Integer id;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiredDate;
    private Integer status;
    private Long daysLeft;          // 还剩几天过期（负数表示已过期天数）
}
