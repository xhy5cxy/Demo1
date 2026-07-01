package com.etour.controller;

import com.etour.common.Result;
import com.etour.dto.AIRecommendRequest;
import com.etour.entity.Spot;
import com.etour.entity.Strategy;
import com.etour.service.SpotService;
import com.etour.service.StrategyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@Tag(name = "AI智能推荐", description = "AI智能推荐相关接口")
public class AIController {

    @Autowired
    private SpotService spotService;

    @Autowired
    private StrategyService strategyService;

    @GetMapping("/hot")
    @Operation(summary = "获取热门推荐", description = "获取AI推荐的热门景点与攻略")
    public Result<Map<String, Object>> getHotRecommendations() {
        Map<String, Object> data = new HashMap<>();
        data.put("spots", spotService.getHotSpots());
        data.put("strategies", strategyService.getHotStrategies());
        return Result.success(data);
    }

    @PostMapping("/recommend")
    @Operation(summary = "AI智能推荐", description = "根据用户偏好进行AI智能推荐")
    public Result<List<Spot>> getAIRecommendations(@RequestBody AIRecommendRequest request) {
        try {
            List<Spot> spots = spotService.getRecommendationsByPreferences(
                request.getPreferences(),
                request.getBudget(),
                request.getDays()
            );
            return Result.success(spots);
        } catch (Exception e) {
            return Result.error("推荐失败: " + e.getMessage());
        }
    }


}