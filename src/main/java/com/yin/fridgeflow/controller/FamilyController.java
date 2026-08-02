package com.yin.fridgeflow.controller;

import com.yin.fridgeflow.common.Result;
import com.yin.fridgeflow.dto.CreateFamilyRequest;
import com.yin.fridgeflow.dto.FamilyDto;
import com.yin.fridgeflow.dto.JoinFamilyRequest;
import com.yin.fridgeflow.service.FamilyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 家庭管理控制器（/api/family）。
 * <p>提供家庭的创建、加入、查询与成员移除接口。</p>
 *
 * @author yin
 */
@RestController
@RequestMapping("/api/family")
@Tag(name = "家庭管理接口")
public class FamilyController {

    @Autowired
    private FamilyService familyService;

    /**
     * 接口7：创建家庭。
     * <p>创建者以管理员身份加入，返回含邀请码的家庭信息。</p>
     *
     * @param request 创建家庭请求（name、creatorId）
     * @return 家庭信息（含邀请码与成员列表）
     */
    @PostMapping
    @Operation(summary = "创建家庭")
    public Result<FamilyDto> createFamily(@Valid @RequestBody CreateFamilyRequest request) {
        try {
            return Result.success("创建成功", familyService.createFamily(request));
        } catch (RuntimeException e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 接口8：加入家庭。
     * <p>凭邀请码加入，以普通成员身份加入；邀请码无效返回 404。</p>
     *
     * @param request 加入请求（inviteCode、userId）
     * @return 家庭信息
     */
    @PostMapping("/join")
    @Operation(summary = "加入家庭（凭邀请码）")
    public Result<FamilyDto> joinFamily(@Valid @RequestBody JoinFamilyRequest request) {
        try {
            return Result.success("加入成功", familyService.joinFamily(request));
        } catch (RuntimeException e) {
            if ("邀请码无效".equals(e.getMessage())) {
                return Result.error(404, e.getMessage());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 接口9：获取当前用户所在家庭信息。
     * <p>未加入任何家庭返回 404。</p>
     *
     * @param userId 用户 ID
     * @return 家庭信息（含成员列表）
     */
    @GetMapping("/current")
    @Operation(summary = "获取当前用户所在家庭信息")
    public Result<FamilyDto> getCurrentFamily(@RequestParam Long userId) {
        try {
            return Result.success(familyService.getCurrentFamily(userId));
        } catch (RuntimeException e) {
            if ("未加入任何家庭".equals(e.getMessage())) {
                return Result.error(404, e.getMessage());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 接口10：移除家庭成员。
     * <p>成员不存在返回 404；管理员不可移除。</p>
     *
     * @param userId   被移除的用户 ID
     * @param familyId 家庭 ID
     * @return message="移除成功"，data=null
     */
    @DeleteMapping("/members/{userId}")
    @Operation(summary = "移除家庭成员")
    public Result<Void> removeMember(@PathVariable Long userId, @RequestParam Long familyId) {
        try {
            familyService.removeMember(userId, familyId);
            return Result.success("移除成功", null);
        } catch (RuntimeException e) {
            if ("成员不存在".equals(e.getMessage())) {
                return Result.error(404, e.getMessage());
            }
            return Result.fail(e.getMessage());
        }
    }
}
