package com.etour.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.etour.entity.Collect;
import com.etour.entity.Strategy;
import com.etour.entity.Spot;
import com.etour.mapper.CollectMapper;
import com.etour.mapper.StrategyMapper;
import com.etour.mapper.SpotMapper;
import com.etour.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

/**
 * 收藏服务实现类
 */
@Service
public class CollectServiceImpl implements CollectService {

    @Autowired
    private CollectMapper collectMapper;

    @Autowired
    private StrategyMapper strategyMapper;

    @Autowired
    private SpotMapper spotMapper;

    @Override
    public List<Collect> getUserCollects(Long userId) {
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, userId)
               .orderByDesc(Collect::getCreateTime);
        List<Collect> collects = collectMapper.selectList(wrapper);
        
        // 兼容旧的strategyId字段
        collects.forEach(collect -> {
            if (collect.getType() != null && collect.getType() == 1) {
                collect.setStrategyId(collect.getTargetId());
            }
        });
        
        return collects;
    }

    @Override
    public List<Strategy> getUserFavoriteStrategies(Long userId) {
        // 获取用户的收藏记录（类型为1-攻略）
        LambdaQueryWrapper<Collect> collectWrapper = new LambdaQueryWrapper<>();
        collectWrapper.eq(Collect::getUserId, userId)
                     .eq(Collect::getType, 1) // 攻略类型
                     .orderByDesc(Collect::getCreateTime);
        List<Collect> collects = collectMapper.selectList(collectWrapper);
        
        if (collects.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 获取收藏的攻略ID列表
        List<Long> strategyIds = collects.stream()
                .map(Collect::getTargetId)
                .collect(Collectors.toList());
        
        // 获取攻略详情
        LambdaQueryWrapper<Strategy> strategyWrapper = new LambdaQueryWrapper<>();
        strategyWrapper.in(Strategy::getId, strategyIds)
                      .eq(Strategy::getStatus, 1); // 只获取已发布的攻略
        List<Strategy> strategies = strategyMapper.selectList(strategyWrapper);
        
        // 为攻略添加收藏ID，方便前端取消收藏
        strategies.forEach(strategy -> {
            collects.stream()
                   .filter(collect -> collect.getTargetId().equals(strategy.getId()))
                   .findFirst()
                   .ifPresent(collect -> strategy.setCollectId(collect.getId()));
        });
        
        return strategies;
    }

    @Override
    public List<Spot> getUserFavoriteSpots(Long userId) {
        // 获取用户的收藏记录（类型为2-景点）
        LambdaQueryWrapper<Collect> collectWrapper = new LambdaQueryWrapper<>();
        collectWrapper.eq(Collect::getUserId, userId)
                     .eq(Collect::getType, 2) // 景点类型
                     .orderByDesc(Collect::getCreateTime);
        List<Collect> collects = collectMapper.selectList(collectWrapper);
        
        if (collects.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 获取收藏的景点ID列表
        List<Long> spotIds = collects.stream()
                .map(Collect::getTargetId)
                .collect(Collectors.toList());
        
        // 获取景点详情
        LambdaQueryWrapper<Spot> spotWrapper = new LambdaQueryWrapper<>();
        spotWrapper.in(Spot::getId, spotIds);
        List<Spot> spots = spotMapper.selectList(spotWrapper);
        
        // 注意：Spot实体不支持收藏ID字段，前端需要通过其他方式处理取消收藏
        
        return spots;
    }

    @Override
    public Collect addCollect(Collect collect) {
        // 兼容旧的strategyId字段
        if (collect.getStrategyId() != null && collect.getTargetId() == null) {
            collect.setTargetId(collect.getStrategyId());
            collect.setType(1); // 默认为攻略类型
        }
        
        // 检查是否已收藏
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, collect.getUserId())
               .eq(Collect::getType, collect.getType())
               .eq(Collect::getTargetId, collect.getTargetId());
        
        Collect existing = collectMapper.selectOne(wrapper);
        if (existing != null) {
            return existing; // 已收藏，返回现有记录
        }
        
        // 设置创建时间
        collect.setCreateTime(LocalDateTime.now());
        collectMapper.insert(collect);
        
        // 更新对应的收藏数量
        if (collect.getType() == 1) {
            // 攻略收藏
            updateStrategyCollectCount(collect.getTargetId(), 1);
        }
        
        return collect;
    }

    @Override
    public boolean removeCollect(Long id) {
        Collect collect = collectMapper.selectById(id);
        if (collect != null) {
            int result = collectMapper.deleteById(id);
            if (result > 0) {
                // 更新对应的收藏数量
                if (collect.getType() == 1) {
                    // 攻略收藏
                    updateStrategyCollectCount(collect.getTargetId(), -1);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeStrategyCollect(Long userId, Long strategyId) {
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, userId)
               .eq(Collect::getType, 1) // 攻略类型
               .eq(Collect::getTargetId, strategyId);
        
        Collect collect = collectMapper.selectOne(wrapper);
        if (collect != null) {
            return removeCollect(collect.getId());
        }
        return false;
    }

    @Override
    public boolean isCollected(Long userId, Long strategyId) {
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, userId)
               .eq(Collect::getType, 1) // 攻略类型
               .eq(Collect::getTargetId, strategyId);
        return collectMapper.selectCount(wrapper) > 0;
    }

    @Override
    public Long getCollectCount(Long userId) {
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, userId);
        return collectMapper.selectCount(wrapper);
    }

    @Override
    public boolean collectStrategy(Long strategyId, Long userId) {
        Collect collect = new Collect();
        collect.setUserId(userId);
        collect.setTargetId(strategyId);
        collect.setType(1); // 攻略类型
        Collect result = addCollect(collect);
        return result != null;
    }

    /**
     * 更新攻略的收藏数量
     */
    private void updateStrategyCollectCount(Long strategyId, int delta) {
        Strategy strategy = strategyMapper.selectById(strategyId);
        if (strategy != null) {
            strategy.setCollectCount(strategy.getCollectCount() + delta);
            strategyMapper.updateById(strategy);
        }
    }

    @Override
    public boolean collectSpot(Long spotId, Long userId) {
        Collect collect = new Collect();
        collect.setUserId(userId);
        collect.setTargetId(spotId);
        collect.setType(2); // 景点类型
        Collect result = addCollect(collect);
        return result != null;
    }

    @Override
    public boolean removeSpotCollect(Long userId, Long spotId) {
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, userId)
               .eq(Collect::getType, 2) // 景点类型
               .eq(Collect::getTargetId, spotId);
        
        Collect collect = collectMapper.selectOne(wrapper);
        if (collect != null) {
            return removeCollect(collect.getId());
        }
        return false;
    }

    @Override
    public boolean isSpotCollected(Long userId, Long spotId) {
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, userId)
               .eq(Collect::getType, 2) // 景点类型
               .eq(Collect::getTargetId, spotId);
        return collectMapper.selectCount(wrapper) > 0;
    }
}