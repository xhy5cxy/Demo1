package com.etour.controller;

import com.etour.common.Result;
import com.etour.entity.Strategy;
import com.etour.service.StrategyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/strategies")
@Tag(name = "攻略管理", description = "旅游攻略相关接口")
public class StrategyController {

    @Autowired
    private StrategyService strategyService;

    @GetMapping("/community")
    @Operation(summary = "获取社区攻略", description = "获取所有公开的攻略")
    public Result<List<Strategy>> getCommunityStrategies() {
        List<Strategy> strategies = strategyService.getCommunityStrategies();
        return Result.success(strategies);
    }

    @GetMapping("/hot")
    @Operation(summary = "获取热门攻略", description = "获取点赞数最多的攻略")
    public Result<List<Strategy>> getHotStrategies() {
        List<Strategy> strategies = strategyService.getHotStrategies();
        return Result.success(strategies);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户攻略", description = "获取指定用户的攻略")
    public Result<List<Strategy>> getUserStrategies(@PathVariable Long userId) {
        List<Strategy> strategies = strategyService.getUserStrategies(userId);
        return Result.success(strategies);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取攻略详情", description = "获取指定攻略的详细信息")
    public Result<Strategy> getStrategyDetail(@PathVariable Long id) {
        Strategy strategy = strategyService.getStrategyById(id);
        return strategy != null ? Result.success(strategy) : Result.error("攻略不存在");
    }

    @PostMapping("/generate")
    @Operation(summary = "AI生成攻略", description = "根据目的地、天数、预算生成攻略")
    public Result<Strategy> generateStrategy(
            @RequestParam String destination,
            @RequestParam(defaultValue = "3") Integer days,
            @RequestParam(defaultValue = "1000") Double budget) {
        try {
            Strategy strategy = strategyService.generateStrategy(destination, days, budget);
            return Result.success(strategy);
        } catch (Exception e) {
            return Result.error("生成攻略失败: " + e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "发布攻略", description = "发布新的攻略")
    public Result<Void> publishStrategy(@RequestBody Strategy strategy) {
        boolean success = strategyService.publishStrategy(strategy);
        return success ? Result.success() : Result.error("发布失败");
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新攻略", description = "更新已有攻略")
    public Result<Void> updateStrategy(@PathVariable Long id, @RequestBody Strategy strategy) {
        strategy.setId(id);
        boolean success = strategyService.updateStrategy(strategy);
        return success ? Result.success() : Result.error("更新失败");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除攻略", description = "删除指定攻略")
    public Result<Void> deleteStrategy(@PathVariable Long id) {
        boolean success = strategyService.deleteStrategy(id);
        return success ? Result.success() : Result.error("删除失败");
    }

    @PostMapping("/{id}/like")
    @Operation(summary = "点赞攻略", description = "为攻略点赞")
    public Result<Void> likeStrategy(@PathVariable Long id, @RequestParam Long userId) {
        boolean success = strategyService.likeStrategy(id, userId);
        return success ? Result.success() : Result.error("点赞失败");
    }

    @DeleteMapping("/{id}/like")
    @Operation(summary = "取消点赞攻略", description = "取消攻略点赞")
    public Result<Void> unlikeStrategy(@PathVariable Long id, @RequestParam Long userId) {
        boolean success = strategyService.unlikeStrategy(id, userId);
        return success ? Result.success() : Result.error("取消点赞失败");
    }

    @PostMapping("/{id}/collect")
    @Operation(summary = "收藏攻略", description = "收藏攻略")
    public Result<Void> collectStrategy(@PathVariable Long id, @RequestParam Long userId) {
        boolean success = strategyService.collectStrategy(id, userId);
        return success ? Result.success() : Result.error("收藏失败");
    }
}