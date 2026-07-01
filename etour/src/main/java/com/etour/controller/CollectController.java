package com.etour.controller;

import com.etour.common.Result;
import com.etour.entity.Collect;
import com.etour.entity.Strategy;
import com.etour.entity.Spot;
import com.etour.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 收藏控制器
 */
@RestController
@RequestMapping("/api/collects")
@CrossOrigin(origins = "*")
public class CollectController {

    @Autowired
    private CollectService collectService;

    /**
     * 获取用户的收藏记录
     */
    @GetMapping("/user/{userId}")
    public Result<List<Collect>> getUserCollects(@PathVariable Long userId) {
        List<Collect> collects = collectService.getUserCollects(userId);
        return Result.success(collects);
    }

    /**
     * 获取用户收藏的攻略
     */
    @GetMapping("/user/{userId}/strategies")
    public Result<List<Strategy>> getUserFavoriteStrategies(@PathVariable Long userId) {
        List<Strategy> strategies = collectService.getUserFavoriteStrategies(userId);
        return Result.success(strategies);
    }

    /**
     * 获取用户收藏的景点
     */
    @GetMapping("/user/{userId}/spots")
    public Result<List<Spot>> getUserFavoriteSpots(@PathVariable Long userId) {
        List<Spot> spots = collectService.getUserFavoriteSpots(userId);
        return Result.success(spots);
    }

    /**
     * 添加收藏
     */
    @PostMapping
    public Result<Collect> addCollect(@RequestBody Collect collect) {
        Collect result = collectService.addCollect(collect);
        return Result.success(result);
    }

    /**
     * 取消收藏
     */
    @DeleteMapping("/{id}")
    public Result<Void> removeCollect(@PathVariable Long id) {
        boolean success = collectService.removeCollect(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("取消收藏失败");
        }
    }

    /**
     * 根据用户ID和攻略ID取消收藏
     */
    @DeleteMapping("/strategy/{userId}/{strategyId}")
    public Result<Void> removeStrategyCollect(
            @PathVariable Long userId, 
            @PathVariable Long strategyId) {
        boolean success = collectService.removeStrategyCollect(userId, strategyId);
        if (success) {
            return Result.success();
        } else {
            return Result.error("取消收藏失败");
        }
    }

    /**
     * 检查是否已收藏
     */
    @GetMapping("/check/{userId}/{strategyId}")
    public Result<Boolean> checkCollect(
            @PathVariable Long userId, 
            @PathVariable Long strategyId) {
        boolean isCollected = collectService.isCollected(userId, strategyId);
        return Result.success(isCollected);
    }

    /**
     * 获取收藏数量
     */
    @GetMapping("/count/{userId}")
    public Result<Long> getCollectCount(@PathVariable Long userId) {
        Long count = collectService.getCollectCount(userId);
        return Result.success(count);
    }

    /**
     * 收藏攻略
     */
    @PostMapping("/strategy")
    public Result<Void> collectStrategy(@RequestParam Long strategyId, @RequestParam Long userId) {
        boolean success = collectService.collectStrategy(strategyId, userId);
        if (success) {
            return Result.success();
        } else {
            return Result.error("收藏失败");
        }
    }

    /**
     * 收藏景点
     */
    @PostMapping("/spot")
    public Result<Void> collectSpot(@RequestParam Long spotId, @RequestParam Long userId) {
        boolean success = collectService.collectSpot(spotId, userId);
        if (success) {
            return Result.success();
        } else {
            return Result.error("收藏失败");
        }
    }

    /**
     * 根据用户ID和景点ID取消收藏
     */
    @DeleteMapping("/spot/{userId}/{spotId}")
    public Result<Void> removeSpotCollect(
            @PathVariable Long userId, 
            @PathVariable Long spotId) {
        boolean success = collectService.removeSpotCollect(userId, spotId);
        if (success) {
            return Result.success();
        } else {
            return Result.error("取消收藏失败");
        }
    }

    /**
     * 检查景点是否已收藏
     */
    @GetMapping("/check/spot/{userId}/{spotId}")
    public Result<Boolean> checkSpotCollect(
            @PathVariable Long userId, 
            @PathVariable Long spotId) {
        boolean isCollected = collectService.isSpotCollected(userId, spotId);
        return Result.success(isCollected);
    }
}