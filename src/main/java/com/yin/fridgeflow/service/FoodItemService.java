package com.yin.fridgeflow.service;

import com.yin.fridgeflow.dto.AddFoodRequest;
import com.yin.fridgeflow.dto.AddFoodResponse;
import com.yin.fridgeflow.dto.FoodItemDto;
import com.yin.fridgeflow.dto.UpdateFoodRequest;

import java.util.List;

/**
 * 食材记录表服务接口。
 * <p>封装食材的增删改查及盒子状态联动逻辑。</p>
 *
 * @author yin
 */
public interface FoodItemService {

    /**
     * 获取食材列表（接口1）。
     * <p>固定查询 family_id=1 的食材；status 不传则排除已删除，传值则按值精确筛选。</p>
     *
     * @param status 状态：0已删除 1正常 2临期；为 null 时返回所有未删除的
     * @return 食材 DTO 列表，按过期日期升序
     */
    List<FoodItemDto> list(Integer status);

    /**
     * 获取盒子信息（接口2扫码）。
     * <p>查 box 表校验盒子存在性，再取该盒下未删除的最新一条食材。</p>
     *
     * @param boxCode 盒子二维码
     * @return 盒子不存在返回 null；盒子空闲（无食材）返回 id 为 null 的空 DTO；否则返回食材 DTO
     */
    FoodItemDto getBoxList(String boxCode);

    /**
     * 向盒子添加食材（接口3）。
     * <p>事务性操作：校验盒子 → 计算过期日期 → 插入食材 → 必要时更新盒子状态为使用中。</p>
     *
     * @param request 添加食材请求（boxCode/name/startDate/expireDays）
     * @return 新创建的食材信息（id、name、expiredDate）
     * @throws RuntimeException 当 boxCode 不存在时抛出 "无效二维码"
     */
    AddFoodResponse addFood(AddFoodRequest request);

    /**
     * 修改食材信息（接口4）。
     * <p>校验食材未被删除后，更新字段并按 startDate+expireDays 重新计算 expiredDate。</p>
     *
     * @param id      食材主键
     * @param request 修改请求（name/startDate/expireDays）
     * @throws RuntimeException 当食材不存在或已删除时抛出 "食材不存在"
     */
    void updateFood(Long id, UpdateFoodRequest request);

    /**
     * 删除食材（接口5，软删除）。
     * <p>事务性操作：置 status=0 后，若该盒下已无其他有效食材，则将盒子状态置为空闲(0)。</p>
     *
     * @param id 食材主键
     * @throws RuntimeException 当食材不存在时抛出 "食材不存在"
     */
    void deleteFood(Long id);
}
