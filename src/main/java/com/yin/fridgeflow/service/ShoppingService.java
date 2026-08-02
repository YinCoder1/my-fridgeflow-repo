package com.yin.fridgeflow.service;

import com.yin.fridgeflow.dto.GenerateShoppingRequest;
import com.yin.fridgeflow.dto.ShoppingItemDto;

import java.util.List;

/**
 * 采购清单服务接口。
 * <p>封装采购清单的生成、查询与认领流转逻辑。</p>
 *
 * <p><b>状态流转：</b>待购买(0) →（认领）→ 预占中(1) →（完成购买）→ 已购买(2)；
 * 预占中可取消认领回到待购买(0)。</p>
 *
 * @author yin
 */
public interface ShoppingService {

    /**
     * 生成采购清单（接口13）。
     * <p>按食谱缺失食材生成采购项，库存已有或采购清单已存在的同名项会跳过，避免重复。</p>
     *
     * @param request 生成请求（recipeId、familyId）
     * @return 生成后该家庭当前待购买/预占中的采购项列表
     * @throws RuntimeException 食谱不存在时抛 "食谱不存在"
     */
    List<ShoppingItemDto> generate(GenerateShoppingRequest request);

    /**
     * 获取采购清单（接口14）。
     *
     * @param familyId 家庭 ID
     * @param status    状态过滤：0待购买 1预占中 2已购买；为 null 时返回全部
     * @return 采购项列表，按状态降序、ID 升序排列
     */
    List<ShoppingItemDto> list(Long familyId, Integer status);

    /**
     * 认领采购项（接口15）。
     * <p>仅 status=0 可认领，置为预占中并记录认领人。携带 version 走乐观锁。</p>
     *
     * @param itemId 采购项 ID
     * @param userId 认领者用户 ID
     * @throws RuntimeException 采购项不存在抛 "采购项不存在"；已被认领或已完成抛 "该采购项已被认领或已完成"
     */
    void claim(Long itemId, Long userId);

    /**
     * 取消认领（接口16）。
     * <p>仅 status=1 可取消，置回待购买并清空认领人。携带 version 走乐观锁。</p>
     *
     * @param itemId 采购项 ID
     * @throws RuntimeException 采购项不存在抛 "采购项不存在"；未被认领抛 "该采购项未被认领"
     */
    void unclaim(Long itemId);

    /**
     * 完成购买（接口17）。
     * <p>仅 status=1 可完成，置为已购买。携带 version 走乐观锁。</p>
     *
     * @param itemId 采购项 ID
     * @throws RuntimeException 采购项不存在抛 "采购项不存在"；未认领抛 "请先认领后再完成购买"
     */
    void complete(Long itemId);
}
