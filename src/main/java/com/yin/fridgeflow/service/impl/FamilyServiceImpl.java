package com.yin.fridgeflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yin.fridgeflow.dto.CreateFamilyRequest;
import com.yin.fridgeflow.dto.FamilyDto;
import com.yin.fridgeflow.dto.FamilyMemberDto;
import com.yin.fridgeflow.dto.JoinFamilyRequest;
import com.yin.fridgeflow.entity.Family;
import com.yin.fridgeflow.entity.User;
import com.yin.fridgeflow.entity.UserFamily;
import com.yin.fridgeflow.mapper.FamilyMapper;
import com.yin.fridgeflow.mapper.UserFamilyMapper;
import com.yin.fridgeflow.mapper.UserMapper;
import com.yin.fridgeflow.service.FamilyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 家庭管理服务实现类。
 * <p>实现家庭的创建/加入/查询与成员移除，成员列表聚合用户昵称、头像。</p>
 *
 * @author yin
 */
@Service
public class FamilyServiceImpl implements FamilyService {

    @Autowired
    private FamilyMapper familyMapper;

    @Autowired
    private UserFamilyMapper userFamilyMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 创建家庭（接口7）。
     * <p>事务保证：创建家庭记录与创建者加入记录原子完成。</p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public FamilyDto createFamily(CreateFamilyRequest request) {
        // 1. 创建家庭
        Family family = new Family();
        family.setName(request.getName());
        family.setInviteCode(genInviteCode());
        family.setCreatorId(request.getCreatorId());
        familyMapper.insert(family);

        // 2. 创建者加入为管理员
        UserFamily uf = new UserFamily();
        uf.setUserId(request.getCreatorId());
        uf.setFamilyId(family.getId().longValue());
        uf.setRole(1); // 管理员
        userFamilyMapper.insert(uf);

        // 3. 组装返回
        return buildFamilyDto(family);
    }

    /**
     * 加入家庭（接口8）。
     * <p>事务保证：校验与加入原子完成。</p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public FamilyDto joinFamily(JoinFamilyRequest request) {
        // 1. 按邀请码查家庭
        LambdaQueryWrapper<Family> familyWrapper = new LambdaQueryWrapper<>();
        familyWrapper.eq(Family::getInviteCode, request.getInviteCode());
        Family family = familyMapper.selectOne(familyWrapper);
        if (family == null) {
            throw new RuntimeException("邀请码无效");   // → Controller 转 404
        }

        // 2. 校验是否已加入（避免重复加入）
        LambdaQueryWrapper<UserFamily> ufWrapper = new LambdaQueryWrapper<>();
        ufWrapper.eq(UserFamily::getUserId, request.getUserId())
                 .eq(UserFamily::getFamilyId, family.getId());
        Long exist = userFamilyMapper.selectCount(ufWrapper);
        if (exist != null && exist > 0L) {
            throw new RuntimeException("已加入该家庭");
        }

        // 3. 以普通成员加入
        UserFamily uf = new UserFamily();
        uf.setUserId(request.getUserId());
        uf.setFamilyId(family.getId().longValue());
        uf.setRole(0);
        userFamilyMapper.insert(uf);

        return buildFamilyDto(family);
    }

    /**
     * 获取当前用户所在家庭信息（接口9）。
     */
    @Override
    public FamilyDto getCurrentFamily(Long userId) {
        // 1. 查用户所在家庭（当前阶段一个用户只属于一个家庭）
        LambdaQueryWrapper<UserFamily> ufWrapper = new LambdaQueryWrapper<>();
        ufWrapper.eq(UserFamily::getUserId, userId);
        UserFamily uf = userFamilyMapper.selectOne(ufWrapper);
        if (uf == null) {
            throw new RuntimeException("未加入任何家庭");   // → Controller 转 404
        }

        // 2. 查家庭
        Family family = familyMapper.selectById(uf.getFamilyId());
        if (family == null) {
            throw new RuntimeException("家庭不存在");
        }

        return buildFamilyDto(family);
    }

    /**
     * 移除家庭成员（接口10）。
     * <p>事务保证：校验与删除原子完成。</p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeMember(Long userId, Long familyId) {
        LambdaQueryWrapper<UserFamily> ufWrapper = new LambdaQueryWrapper<>();
        ufWrapper.eq(UserFamily::getUserId, userId)
                 .eq(UserFamily::getFamilyId, familyId);
        UserFamily uf = userFamilyMapper.selectOne(ufWrapper);
        if (uf == null) {
            throw new RuntimeException("成员不存在");   // → Controller 转 404
        }
        if (uf.getRole() != null && uf.getRole() == 1) {
            throw new RuntimeException("不能移除管理员");
        }
        userFamilyMapper.deleteById(uf.getId());
    }

    // ===================== 私有辅助 =====================

    /**
     * 组装家庭视图：家庭信息 + 成员列表（含昵称、头像）。
     * <p>成员用户信息一次性 selectByIds 批量查询，避免 N+1。</p>
     *
     * @param family 家庭实体
     * @return 家庭视图（含成员列表）
     */
    private FamilyDto buildFamilyDto(Family family) {
        FamilyDto vo = new FamilyDto();
        vo.setId(family.getId());
        vo.setName(family.getName());
        vo.setInviteCode(family.getInviteCode());
        vo.setCreatorId(family.getCreatorId());

        // 查成员
        LambdaQueryWrapper<UserFamily> ufWrapper = new LambdaQueryWrapper<>();
        ufWrapper.eq(UserFamily::getFamilyId, family.getId());
        List<UserFamily> ufs = userFamilyMapper.selectList(ufWrapper);

        List<Long> userIds = ufs.stream().map(UserFamily::getUserId).collect(Collectors.toList());
        List<FamilyMemberDto> members;
        if (userIds.isEmpty()) {
            members = List.of();
        } else {
            // 批量查用户信息，避免逐个查询产生 N+1
            List<User> users = userMapper.selectByIds(userIds);
            members = ufs.stream().map(it -> {
                FamilyMemberDto m = new FamilyMemberDto();
                m.setUserId(it.getUserId());
                m.setRole(it.getRole());
                users.stream()
                     .filter(u -> u.getId().longValue() == it.getUserId())
                     .findFirst()
                     .ifPresent(u -> {
                         m.setNickname(u.getNickname());
                         m.setAvatarUrl(u.getAvatarUrl());
                     });
                return m;
            }).collect(Collectors.toList());
        }
        vo.setMembers(members);
        return vo;
    }

    /**
     * 生成邀请码：FF- + 6位大写字母数字（UUID 去横线后取前6位转大写）。
     */
    private String genInviteCode() {
        return "FF-" + UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
    }
}
