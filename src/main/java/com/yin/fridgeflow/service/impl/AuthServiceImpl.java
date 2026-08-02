package com.yin.fridgeflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yin.fridgeflow.common.JwtUtil;
import com.yin.fridgeflow.dto.LoginResponse;
import com.yin.fridgeflow.dto.UserInfoDto;
import com.yin.fridgeflow.entity.User;
import com.yin.fridgeflow.mapper.UserMapper;
import com.yin.fridgeflow.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 认证服务实现类。
 * <p>微信登录：按 openid 查找用户，不存在则自动注册，并签发 JWT。</p>
 *
 * @author yin
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 微信登录（接口6）。
     * <p>事务保证：注册用户与（未来可能的）关联记录原子完成。</p>
     * <p>步骤：①按 openid 查用户 ②不存在则注册(昵称默认"微信用户"+openid末4位) ③签发 token ④组装响应。</p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginResponse login(String openid) {
        // 1. 查找用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getOpenid, openid);
        User user = userMapper.selectOne(wrapper);

        // 2. 不存在则注册
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            // 默认昵称：取 openid 末4位
            String tail = openid.length() > 4 ? openid.substring(openid.length() - 4) : openid;
            user.setNickname("微信用户" + tail);
            userMapper.insert(user);   // insert 后 user.id 被回填
        }

        // 3. 签发 token（subject = 用户ID）
        String token = jwtUtil.generateToken(user.getId().longValue());

        // 4. 组装响应
        LoginResponse resp = new LoginResponse();
        resp.setToken(token);

        UserInfoDto userInfo = new UserInfoDto();
        userInfo.setId(user.getId());
        userInfo.setNickname(user.getNickname());
        resp.setUserInfo(userInfo);
        return resp;
    }
}
