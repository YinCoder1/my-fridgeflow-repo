package com.yin.fridgeflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yin.fridgeflow.entity.ShoppingItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采购清单表 DAO（对应 shopping_item 表）。
 * <p>继承 {@link BaseMapper}，自动获得通用 CRUD。
 * updateById 会自动携带 version 字段走乐观锁校验。</p>
 *
 * @author yin
 */
@Mapper
public interface ShoppingItemMapper extends BaseMapper<ShoppingItem> {
}
