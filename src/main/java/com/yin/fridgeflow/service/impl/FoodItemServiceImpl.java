package com.yin.fridgeflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yin.fridgeflow.dto.FoodItemDto;
import com.yin.fridgeflow.entity.FoodItem;
import com.yin.fridgeflow.mapper.FoodItemMapper;
import com.yin.fridgeflow.service.FoodItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 食材记录表服务实现类
 */
@Service
public class FoodItemServiceImpl implements FoodItemService {
    @Autowired
    private FoodItemMapper foodItemMapper;

    @Override
    public List<FoodItemDto> list(Integer status) {
        // 构造查询条件
        LambdaQueryWrapper<FoodItem> wrapper = new LambdaQueryWrapper<>();

        // 第一阶段：写死 family_id = 1
        wrapper.eq(FoodItem::getFamilyId, 1L);

        // status 筛选：不传则排除已删除，传了就按传入值筛选
        if (status != null) {
            wrapper.eq(FoodItem::getStatus, status);
        } else {
            wrapper.ne(FoodItem::getStatus, 0);   // 排除已删除
        }

        // 按过期日期升序（最早过期的排前面）
        wrapper.orderByAsc(FoodItem::getExpiredDate);

        List<FoodItem> foodItems = foodItemMapper.selectList(wrapper);

        // 转换为 DTO
        return foodItems.stream().map(item -> {
            FoodItemDto dto = new FoodItemDto();
            dto.setId(item.getId());
            dto.setName(item.getName());
            dto.setExpiredDate(item.getExpiredDate());
            dto.setStatus(item.getStatus());
            // 计算剩余天数
            long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), item.getExpiredDate());
            dto.setDaysLeft(daysLeft);
            return dto;
        }).collect(Collectors.toList());
    }
}
