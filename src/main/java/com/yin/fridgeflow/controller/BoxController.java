package com.yin.fridgeflow.controller;

import com.yin.fridgeflow.common.Result;
import com.yin.fridgeflow.dto.FoodItemDto;
import com.yin.fridgeflow.service.FoodItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 盒子(二维码)控制器（/api/food/box）。
 * <p>扫描盒子二维码，返回盒子状态及最新一条食材信息。</p>
 *
 * @author yin
 */
@RestController
@RequestMapping("/api/food/box")
@Tag(name = "盒子(二维码)接口")
public class BoxController {

    private final FoodItemService foodItemService;

    public BoxController(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }

    /**
     * 接口2：获取盒子（二维码）信息。
     * <p>盒子不存在返回 404；盒子存在但未绑定食材返回 data=null；
     * 盒子绑定食材则返回最新一条食材信息。</p>
     *
     * @param boxCode 盒子二维码
     * @return 食材 DTO（含盒子状态、剩余天数等）
     */
    @GetMapping({"/{boxCode}"})
    @Operation(summary = "扫码获取盒子信息")
    public Result<FoodItemDto> getBoxList(@PathVariable String boxCode){
        FoodItemDto dto = foodItemService.getBoxList(boxCode);
        // 盒子不存在
        if (dto == null) {
            return Result.error(404, "无效二维码");
        }
        // 盒子空闲（未绑定）
        if (dto.getId() == null) {
            return Result.success("盒子未绑定食材", null);
        }
        return Result.success(dto);
    }
}
