package com.yin.fridgeflow.controller;

import com.yin.fridgeflow.common.Result;
import com.yin.fridgeflow.dto.AddFoodRequest;
import com.yin.fridgeflow.dto.AddFoodResponse;
import com.yin.fridgeflow.dto.FoodItemDto;
import com.yin.fridgeflow.dto.UpdateFoodRequest;
import com.yin.fridgeflow.service.FoodItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 食材记录表控制器（/api/food）。
 * <p>提供食材的列表查询、添加、修改、删除接口。</p>
 *
 * @author yin
 */
@RestController
@RequestMapping("/api/food")
@Tag(name="食材记录表接口")
public class FoodItemController {

    @Autowired
    private FoodItemService foodItemService;

    /**
     * 接口1：获取食材列表。
     * <p>根据状态筛选；status 不传时返回所有未删除食材。</p>
     *
     * @param status 状态：0已删除 1正常 2临期；可选
     * @return 食材列表，按过期日期升序
     */
    @GetMapping("/list")
    @Operation(summary = "获取食材列表",description = "根据食材的状态0（已吃完/已删除）1正常，2临期")
    public Result<List<FoodItemDto>> getfoodlist(@RequestParam(required = false) Integer status){
        List<FoodItemDto> list = foodItemService.list(status);
        return Result.success(list);
    }

    /**
     * 接口3：向盒子添加食材。
     * <p>请求体校验失败返回 400；boxCode 不存在返回 404。</p>
     *
     * @param request 添加食材请求（boxCode/name/startDate/expireDays）
     * @return 新创建的食材信息（id/name/expiredDate）
     */
    @PostMapping("/add")
    @Operation(summary = "向盒子添加食材")
    public Result<AddFoodResponse> addFood(@Valid @RequestBody AddFoodRequest request){
        try {
            AddFoodResponse resp = foodItemService.addFood(request);
            return Result.success("添加成功", resp);
        } catch (RuntimeException e) {
            if ("无效二维码".equals(e.getMessage())) {
                return Result.error(404, e.getMessage());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 接口4：修改食材信息。
     * <p>按新入参重新计算过期日期；食材不存在或已删除返回 404。</p>
     *
     * @param id      食材主键
     * @param request 修改请求（name/startDate/expireDays）
     * @return message="修改成功"，data=null
     */
    @PutMapping("/{id}")
    @Operation(summary = "修改食材信息")
    public Result<Void> updateFood(@PathVariable Long id, @Valid @RequestBody UpdateFoodRequest request){
        try {
            foodItemService.updateFood(id, request);
            return Result.success("修改成功", null);
        } catch (RuntimeException e) {
            if ("食材不存在".equals(e.getMessage())) {
                return Result.error(404, e.getMessage());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 接口5：删除食材（软删除）。
     * <p>盒子内无其他有效食材时自动释放盒子（置空闲）。</p>
     *
     * @param id 食材主键
     * @return message="删除成功"，data=null
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除食材（软删除，盒子无其他食材时释放盒子）")
    public Result<Void> deleteFood(@PathVariable Long id){
        try {
            foodItemService.deleteFood(id);
            return Result.success("删除成功", null);
        } catch (RuntimeException e) {
            if ("食材不存在".equals(e.getMessage())) {
                return Result.error(404, e.getMessage());
            }
            return Result.fail(e.getMessage());
        }
    }
}
