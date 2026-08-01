package com.yin.fridgeflow.service;

import com.yin.fridgeflow.dto.FoodItemDto;

import java.util.List;

/**
 * 食材记录表服务接口
 */
public interface FoodItemService {

    List<FoodItemDto> list(Integer status);
}
