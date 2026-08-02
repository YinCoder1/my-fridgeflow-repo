package com.yin.fridgeflow.service;

import com.yin.fridgeflow.dto.CreateFamilyRequest;
import com.yin.fridgeflow.dto.FamilyDto;
import com.yin.fridgeflow.dto.JoinFamilyRequest;

/**
 * 家庭管理服务接口。
 * <p>封装家庭的创建、加入、查询与成员移除逻辑。</p>
 *
 * @author yin
 */
public interface FamilyService {

    /**
     * 创建家庭（接口7）。
     * <p>生成邀请码，创建家庭记录，创建者以管理员身份加入。</p>
     *
     * @param request 创建家庭请求（name、creatorId）
     * @return 家庭信息（含邀请码与成员列表）
     */
    FamilyDto createFamily(CreateFamilyRequest request);

    /**
     * 加入家庭（接口8）。
     * <p>凭邀请码查找家庭，校验是否已加入后，以普通成员身份加入。</p>
     *
     * @param request 加入请求（inviteCode、userId）
     * @return 家庭信息
     * @throws RuntimeException 邀请码无效时抛 "邀请码无效"；已加入时抛 "已加入该家庭"
     */
    FamilyDto joinFamily(JoinFamilyRequest request);

    /**
     * 获取当前用户所在家庭信息（接口9）。
     *
     * @param userId 用户 ID
     * @return 家庭信息（含成员列表）
     * @throws RuntimeException 未加入家庭时抛 "未加入任何家庭"
     */
    FamilyDto getCurrentFamily(Long userId);

    /**
     * 移除家庭成员（接口10）。
     *
     * @param userId   被移除的用户 ID
     * @param familyId 家庭 ID
     * @throws RuntimeException 成员不存在抛 "成员不存在"；被移除者是管理员抛 "不能移除管理员"
     */
    void removeMember(Long userId, Long familyId);
}
