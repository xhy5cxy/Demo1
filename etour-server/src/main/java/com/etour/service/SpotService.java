package com.etour.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.etour.entity.Spot;
import java.util.List;

/**
 * 景点服务接口
 */
public interface SpotService extends IService<Spot> {
    
    /**
     * 获取景点列表（分页）
     */
    IPage<Spot> getSpots(Page<Spot> page, String location, Double minRating);
    
    /**
     * 获取热门景点
     */
    List<Spot> getHotSpots();
    
    /**
     * 搜索景点
     */
    IPage<Spot> searchSpots(Page<Spot> page, String keyword);
    
    /**
     * 根据偏好推荐景点
     */
    List<Spot> getRecommendationsByPreferences(List<String> preferences, Double budget, Integer days);
}