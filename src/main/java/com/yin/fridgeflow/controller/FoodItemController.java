package com.yin.fridgeflow.controller;

import com.yin.fridgeflow.common.Result;
import com.yin.fridgeflow.dto.FoodItemDto;
import com.yin.fridgeflow.service.FoodItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 食材记录表控制器
 */
@RestController
@RequestMapping("/api/food")
@Tag(name="食材记录表接口")
public class FoodItemController {
    @Autowired
    private FoodItemService foodItemService;

    // 获取食材列表
    @GetMapping("/list")
    @Operation(summary = "获取食材列表",description = "根据食材的状态0（已吃完/已删除）1正常，2临期")
    //RequestParam(required = false)的意思是从字符串或者表单拿到数据，然后绑定值，required表示这个参数是可选的，可以不传
    public Result<List<FoodItemDto>> getfoodlist(@RequestParam(required = false) Integer status){
        List<FoodItemDto> list = foodItemService.list(status);
        return Result.success(list);
    }
}
