package com.yin.fridgeflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yin.fridgeflow.entity.Recipe;
import org.apache.ibatis.annotations.Mapper;

/**
 * 食谱表 DAO（对应 recipe 表）。
 * <p>继承 {@link BaseMapper}，自动获得通用 CRUD。</p>
 *
 * @author yin
 */
@Mapper
public interface RecipeMapper extends BaseMapper<Recipe> {
}
