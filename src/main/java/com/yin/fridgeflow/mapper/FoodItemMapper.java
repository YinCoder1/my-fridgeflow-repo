package com.yin.fridgeflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yin.fridgeflow.entity.FoodItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 食材记录表 DAO（对应 food_item 表）。
 * <p>继承 MyBatis-Plus {@link BaseMapper}，自动获得 insert/updateById/deleteById/selectById/selectList/selectCount 等方法，
 * 无需手写 XML。</p>
 *
 * @author yin
 */
@Mapper
public interface FoodItemMapper extends BaseMapper<FoodItem> {
}
