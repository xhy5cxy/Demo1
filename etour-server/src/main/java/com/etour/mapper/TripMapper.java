package com.etour.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.etour.entity.Trip;
import org.apache.ibatis.annotations.Mapper;

/**
 * 行程Mapper接口
 */
@Mapper
public interface TripMapper extends BaseMapper<Trip> {
}