package com.etour.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.etour.entity.Spot;
import com.etour.entity.Strategy;
import com.etour.entity.StrategyTag;
import com.etour.mapper.SpotMapper;
import com.etour.mapper.StrategyMapper;
import com.etour.mapper.StrategyTagMapper;
import com.etour.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class StrategyServiceImpl implements StrategyService {

    @Autowired
    private StrategyMapper strategyMapper;
    
    @Autowired
    private SpotMapper spotMapper;
    
    @Autowired
    private StrategyTagMapper strategyTagMapper;

    @Override
    public List<Strategy> getCommunityStrategies() {
        LambdaQueryWrapper<Strategy> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Strategy::getStatus, 1)
               .orderByDesc(Strategy::getCreateTime);
        return strategyMapper.selectList(wrapper);
    }

    @Override
    public List<Strategy> getHotStrategies() {
        LambdaQueryWrapper<Strategy> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Strategy::getStatus, 1)
               .orderByDesc(Strategy::getLikeCount)
               .last("limit 10");
        return strategyMapper.selectList(wrapper);
    }

    @Override
    public List<Strategy> getUserStrategies(Long userId) {
        LambdaQueryWrapper<Strategy> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Strategy::getUserId, userId)
               .orderByDesc(Strategy::getCreateTime);
        return strategyMapper.selectList(wrapper);
    }

    @Override
    public Strategy getStrategyById(Long id) {
        return strategyMapper.selectById(id);
    }

    @Override
    public Strategy generateStrategy(String destination, Integer days, Double budget) {
        // AI生成攻略逻辑 - 简化版
        Strategy strategy = new Strategy();
        strategy.setTitle(destination + " " + days + "日游攻略");
        strategy.setDestination(destination);
        strategy.setDays(days);
        strategy.setBudget(budget);
        strategy.setStatus(0); // 草稿状态
        strategy.setLikeCount(0);
        strategy.setCollectCount(0);
        strategy.setCreateTime(LocalDateTime.now());
        
        // 根据目的地查找相关景点
        LambdaQueryWrapper<Spot> spotWrapper = new LambdaQueryWrapper<>();
        spotWrapper.like(Spot::getCity, destination)
                    .or()
                    .like(Spot::getName, destination)
                    .orderByDesc(Spot::getRating)
                    .last("limit " + (days * 3)); // 每天3个景点
        
        List<Spot> spots = spotMapper.selectList(spotWrapper);
        
        // 生成攻略内容
        StringBuilder content = new StringBuilder();
        content.append("# ").append(destination).append(" ").append(days).append("日游攻略\n\n");
        content.append("## 行程概览\n");
        content.append("- **目的地**: ").append(destination).append("\n");
        content.append("- **天数**: ").append(days).append("天\n");
        content.append("- **预算**: ¥").append(budget).append("\n\n");
        
        // 按天分配景点
        Random random = new Random();
        for (int day = 1; day <= days; day++) {
            content.append("## 第").append(day).append("天\n");
            
            int spotsPerDay = Math.min(3, spots.size() / days);
            for (int i = 0; i < spotsPerDay && !spots.isEmpty(); i++) {
                Spot spot = spots.remove(random.nextInt(spots.size()));
                content.append("### ").append(spot.getName()).append("\n");
                content.append("- **地址**: ").append(spot.getAddress()).append("\n");
                content.append("- **门票**: ¥").append(spot.getTicketPrice()).append("\n");
                content.append("- **推荐理由**: ").append(spot.getIntro()).append("\n\n");
            }
        }
        
        strategy.setContent(content.toString());
        
        // 保存生成的攻略
        strategyMapper.insert(strategy);
        
        return strategy;
    }

    @Override
    public boolean publishStrategy(Strategy strategy) {
        strategy.setStatus(1);
        strategy.setCreateTime(LocalDateTime.now());
        return strategyMapper.insert(strategy) > 0;
    }

    @Override
    public boolean updateStrategy(Strategy strategy) {
        strategy.setUpdateTime(LocalDateTime.now());
        return strategyMapper.updateById(strategy) > 0;
    }

    @Override
    public boolean deleteStrategy(Long id) {
        return strategyMapper.deleteById(id) > 0;
    }

    @Override
    public boolean likeStrategy(Long id, Long userId) {
        Strategy strategy = strategyMapper.selectById(id);
        if (strategy != null) {
            strategy.setLikeCount(strategy.getLikeCount() + 1);
            return strategyMapper.updateById(strategy) > 0;
        }
        return false;
    }

    @Override
    public boolean unlikeStrategy(Long id, Long userId) {
        Strategy strategy = strategyMapper.selectById(id);
        if (strategy != null && strategy.getLikeCount() > 0) {
            strategy.setLikeCount(strategy.getLikeCount() - 1);
            return strategyMapper.updateById(strategy) > 0;
        }
        return false;
    }

    @Override
    public boolean collectStrategy(Long id, Long userId) {
        Strategy strategy = strategyMapper.selectById(id);
        if (strategy != null) {
            strategy.setCollectCount(strategy.getCollectCount() + 1);
            return strategyMapper.updateById(strategy) > 0;
        }
        return false;
    }
}