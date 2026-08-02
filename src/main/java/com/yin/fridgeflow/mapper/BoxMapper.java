package com.yin.fridgeflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yin.fridgeflow.entity.Box;
import org.apache.ibatis.annotations.Mapper;

/**
 * 盒子表 DAO（对应 box 表）。
 * <p>继承 {@link BaseMapper}，自动获得通用 CRUD。</p>
 *
 * @author yin
 */
@Mapper
public interface BoxMapper extends BaseMapper<Box> {
}
