package com.etour.service;

import com.etour.entity.Strategy;
import java.util.List;

public interface StrategyService {
    List<Strategy> getCommunityStrategies();
    List<Strategy> getHotStrategies();
    List<Strategy> getUserStrategies(Long userId);
    Strategy getStrategyById(Long id);
    Strategy generateStrategy(String destination, Integer days, Double budget);
    boolean publishStrategy(Strategy strategy);
    boolean updateStrategy(Strategy strategy);
    boolean deleteStrategy(Long id);
    boolean likeStrategy(Long id, Long userId);
    boolean unlikeStrategy(Long id, Long userId);
    boolean collectStrategy(Long id, Long userId);
}