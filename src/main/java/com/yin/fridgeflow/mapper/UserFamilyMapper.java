package com.yin.fridgeflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yin.fridgeflow.entity.UserFamily;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户-家庭关联表 DAO（对应 user_family 表）。
 * <p>继承 {@link BaseMapper}，自动获得通用 CRUD。</p>
 *
 * @author yin
 */
@Mapper
public interface UserFamilyMapper extends BaseMapper<UserFamily> {
}
