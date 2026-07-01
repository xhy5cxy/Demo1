package com.etour.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etour.entity.Spot;
import com.etour.mapper.SpotMapper;
import com.etour.service.SpotService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * 景点服务实现类
 */
@Service
public class SpotServiceImpl extends ServiceImpl<SpotMapper, Spot> implements SpotService {

    @Override
    public IPage<Spot> getSpots(Page<Spot> page, String location, Double minRating) {
        QueryWrapper<Spot> queryWrapper = new QueryWrapper<>();
        
        if (location != null && !location.trim().isEmpty()) {
            queryWrapper.like("city", location);
        }
        
        if (minRating != null && minRating > 0) {
            queryWrapper.ge("rating", minRating);
        }
        
        queryWrapper.orderByDesc("rating");
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Spot> getHotSpots() {
        QueryWrapper<Spot> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("rating", 4.0)
                   .orderByDesc("rating")
                   .last("LIMIT 10");
        return this.list(queryWrapper);
    }

    @Override
    public IPage<Spot> searchSpots(Page<Spot> page, String keyword) {
        QueryWrapper<Spot> queryWrapper = new QueryWrapper<>();
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            queryWrapper.like("name", keyword)
                       .or()
                       .like("intro", keyword)
                       .or()
                       .like("city", keyword);
        }
        
        queryWrapper.orderByDesc("rating");
        return this.page(page, queryWrapper);
    }
    
    @Override
    public List<Spot> getRecommendationsByPreferences(List<String> preferences, Double budget, Integer days) {
        QueryWrapper<Spot> queryWrapper = new QueryWrapper<>();
        
        // 基于偏好和预算进行推荐
        if (preferences != null && !preferences.isEmpty()) {
            // 根据偏好标签进行筛选
            for (String preference : preferences) {
                queryWrapper.like("intro", preference)
                           .or()
                           .like("city", preference);
            }
        }
        
        // 根据预算筛选
        if (budget != null && budget > 0) {
            if (days != null && days > 0) {
                double avgPricePerDay = budget / days;
                queryWrapper.le("ticket_price", avgPricePerDay * 0.3); // 门票占预算的30%
            }
        }
        
        // 优先推荐评分高的景点
        queryWrapper.orderByDesc("rating");
        queryWrapper.last("LIMIT 10");
        
        List<Spot> spots = this.list(queryWrapper);
        
        // 如果推荐结果太少，随机补充一些高评分景点
        if (spots.size() < 5) {
            QueryWrapper<Spot> fallbackQuery = new QueryWrapper<>();
            fallbackQuery.ge("rating", 4.0)
                        .orderByDesc("rating")
                        .last("LIMIT 5");
            
            List<Spot> fallbackSpots = this.list(fallbackQuery);
            for (Spot spot : fallbackSpots) {
                if (!spots.contains(spot)) {
                    spots.add(spot);
                }
                if (spots.size() >= 5) {
                    break;
                }
            }
        }
        
        return spots;
    }
}