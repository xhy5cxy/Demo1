package com.etour.service;

import com.etour.entity.Collect;
import com.etour.entity.Strategy;
import com.etour.entity.Spot;
import java.util.List;

/**
 * 收藏服务接口
 */
public interface CollectService {
    
    /**
     * 获取用户的收藏列表
     */
    List<Collect> getUserCollects(Long userId);
    
    /**
     * 获取用户收藏的攻略列表
     */
    List<Strategy> getUserFavoriteStrategies(Long userId);
    
    /**
     * 获取用户收藏的景点列表
     */
    List<Spot> getUserFavoriteSpots(Long userId);
    
    /**
     * 添加收藏
     */
    Collect addCollect(Collect collect);
    
    /**
     * 取消收藏
     */
    boolean removeCollect(Long id);
    
    /**
     * 根据用户ID和攻略ID取消收藏
     */
    boolean removeStrategyCollect(Long userId, Long strategyId);
    
    /**
     * 检查是否已收藏
     */
    boolean isCollected(Long userId, Long strategyId);
    
    /**
     * 获取收藏数量
     */
    Long getCollectCount(Long userId);
    
    /**
     * 收藏攻略
     */
    boolean collectStrategy(Long strategyId, Long userId);
    
    /**
     * 收藏景点
     */
    boolean collectSpot(Long spotId, Long userId);
    
    /**
     * 根据用户ID和景点ID取消收藏
     */
    boolean removeSpotCollect(Long userId, Long spotId);
    
    /**
     * 检查景点是否已收藏
     */
    boolean isSpotCollected(Long userId, Long spotId);
}