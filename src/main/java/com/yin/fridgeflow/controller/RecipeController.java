package com.yin.fridgeflow.controller;

import com.yin.fridgeflow.common.Result;
import com.yin.fridgeflow.dto.RecipeDto;
import com.yin.fridgeflow.dto.RecipeMatchResultDto;
import com.yin.fridgeflow.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 食谱控制器（/api/recipes）。
 * <p>提供食谱列表与库存匹配接口。</p>
 *
 * @author yin
 */
@RestController
@RequestMapping("/api/recipes")
@Tag(name = "食谱接口")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    /**
     * 接口11：食谱列表。
     * <p>每项含所需食材种数。</p>
     *
     * @return 食谱列表
     */
    @GetMapping
    @Operation(summary = "食谱列表")
    public Result<List<RecipeDto>> list() {
        return Result.success(recipeService.list());
    }

    /**
     * 接口12：食谱匹配库存。
     * <p>食谱不存在返回 404。</p>
     *
     * @param id       食谱 ID
     * @param familyId 家庭 ID，默认 1
     * @return 匹配结果（明细 + matchedCount/missingCount）
     */
    @GetMapping("/{id}/match")
    @Operation(summary = "食谱匹配库存")
    public Result<RecipeMatchResultDto> match(@PathVariable Long id,
                                              @RequestParam(defaultValue = "1") Long familyId) {
        try {
            return Result.success(recipeService.match(id, familyId));
        } catch (RuntimeException e) {
            if ("食谱不存在".equals(e.getMessage())) {
                return Result.error(404, e.getMessage());
            }
            return Result.fail(e.getMessage());
        }
    }
}
