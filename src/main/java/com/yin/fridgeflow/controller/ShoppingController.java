package com.yin.fridgeflow.controller;

import com.yin.fridgeflow.common.Result;
import com.yin.fridgeflow.dto.GenerateShoppingRequest;
import com.yin.fridgeflow.dto.ShoppingItemDto;
import com.yin.fridgeflow.service.ShoppingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 采购清单控制器（/api/shopping）。
 * <p>提供采购清单的生成、查询与认领流转接口。</p>
 *
 * @author yin
 */
@RestController
@RequestMapping("/api/shopping")
@Tag(name = "采购清单接口")
public class ShoppingController {

    @Autowired
    private ShoppingService shoppingService;

    /**
     * 接口13：根据食谱缺失食材生成采购清单。
     * <p>食谱不存在返回 404；库存已有或采购清单已存在的同名项会跳过。</p>
     *
     * @param request 生成请求（recipeId、familyId）
     * @return 生成后该家庭当前待购买/预占中的采购项列表
     */
    @PostMapping("/generate")
    @Operation(summary = "根据食谱缺失食材生成采购清单")
    public Result<List<ShoppingItemDto>> generate(@Valid @RequestBody GenerateShoppingRequest request) {
        try {
            List<ShoppingItemDto> list = shoppingService.generate(request);
            return Result.success("生成成功", list);
        } catch (RuntimeException e) {
            if ("食谱不存在".equals(e.getMessage())) {
                return Result.error(404, e.getMessage());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 接口14：获取采购清单。
     *
     * @param familyId 家庭 ID
     * @param status    状态过滤：0待购买 1预占中 2已购买；可选
     * @return 采购项列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取采购清单")
    public Result<List<ShoppingItemDto>> list(@RequestParam Long familyId,
                                              @RequestParam(required = false) Integer status) {
        return Result.success(shoppingService.list(familyId, status));
    }

    /**
     * 接口15：认领采购项。
     * <p>采购项不存在返回 404；仅待购买项可认领。</p>
     *
     * @param itemId 采购项 ID
     * @param userId 认领者用户 ID
     * @return message="认领成功"，data=null
     */
    @PutMapping("/claim/{itemId}")
    @Operation(summary = "认领采购项")
    public Result<Void> claim(@PathVariable Long itemId, @RequestParam Long userId) {
        try {
            shoppingService.claim(itemId, userId);
            return Result.success("认领成功", null);
        } catch (RuntimeException e) {
            if ("采购项不存在".equals(e.getMessage())) {
                return Result.error(404, e.getMessage());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 接口16：取消认领。
     * <p>采购项不存在返回 404；仅预占中项可取消。</p>
     *
     * @param itemId 采购项 ID
     * @return message="取消认领成功"，data=null
     */
    @PutMapping("/unclaim/{itemId}")
    @Operation(summary = "取消认领")
    public Result<Void> unclaim(@PathVariable Long itemId) {
        try {
            shoppingService.unclaim(itemId);
            return Result.success("取消认领成功", null);
        } catch (RuntimeException e) {
            if ("采购项不存在".equals(e.getMessage())) {
                return Result.error(404, e.getMessage());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 接口17：完成购买。
     * <p>采购项不存在返回 404；需先认领后才能完成。</p>
     *
     * @param itemId 采购项 ID
     * @return message="完成购买"，data=null
     */
    @PutMapping("/complete/{itemId}")
    @Operation(summary = "完成购买")
    public Result<Void> complete(@PathVariable Long itemId) {
        try {
            shoppingService.complete(itemId);
            return Result.success("完成购买", null);
        } catch (RuntimeException e) {
            if ("采购项不存在".equals(e.getMessage())) {
                return Result.error(404, e.getMessage());
            }
            return Result.fail(e.getMessage());
        }
    }
}
