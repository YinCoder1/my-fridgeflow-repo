package com.yin.fridgeflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yin.fridgeflow.entity.RecipeIngredient;
import org.apache.ibatis.annotations.Mapper;

/**
 * 食谱-食材关联表 DAO（对应 recipe_ingredient 表）。
 * <p>继承 {@link BaseMapper}，自动获得通用 CRUD。</p>
 *
 * @author yin
 */
@Mapper
public interface RecipeIngredientMapper extends BaseMapper<RecipeIngredient> {
}
