package com.etour.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.etour.entity.Spot;
import com.etour.service.SpotService;
import com.etour.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 景点控制器
 * 提供景点相关的API接口
 */
@RestController
@RequestMapping("/spots")
public class SpotController {

    @Autowired
    private SpotService spotService;

    /**
     * 获取景点列表（分页）
     */
    @GetMapping
    public Result<IPage<Spot>> getSpots(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Double minRating) {
        
        Page<Spot> pageParam = new Page<>(page, size);
        IPage<Spot> spotPage = spotService.getSpots(pageParam, location, minRating);
        return Result.success(spotPage);
    }

    /**
     * 根据ID获取景点详情
     */
    @GetMapping("/{id}")
    public Result<Spot> getSpotById(@PathVariable Long id) {
        Spot spot = spotService.getById(id);
        if (spot != null) {
            return Result.success(spot);
        } else {
            return Result.error("景点不存在");
        }
    }

    /**
     * 获取热门景点
     */
    @GetMapping("/hot")
    public Result<List<Spot>> getHotSpots() {
        List<Spot> hotSpots = spotService.getHotSpots();
        return Result.success(hotSpots);
    }

    /**
     * 搜索景点
     */
    @GetMapping("/search")
    public Result<IPage<Spot>> searchSpots(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam String keyword) {
        
        Page<Spot> pageParam = new Page<>(page, size);
        IPage<Spot> spotPage = spotService.searchSpots(pageParam, keyword);
        return Result.success(spotPage);
    }
}