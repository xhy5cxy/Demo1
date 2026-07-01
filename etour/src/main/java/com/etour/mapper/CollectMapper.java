package com.etour.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.etour.entity.Collect;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 收藏Mapper接口
 */
@Mapper
public interface CollectMapper extends BaseMapper<Collect> {
    
    /**
     * 根据用户ID和攻略ID查询收藏记录
     */
    Collect selectByUserIdAndStrategyId(@Param("userId") Long userId, @Param("strategyId") Long strategyId);
    
    /**
     * 根据用户ID查询收藏列表
     */
    List<Collect> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 根据攻略ID查询收藏数量
     */
    Long selectCountByStrategyId(@Param("strategyId") Long strategyId);
}