package com.etour.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.etour.entity.Spot;
import org.apache.ibatis.annotations.Mapper;

/**
 * 景点Mapper接口
 */
@Mapper
public interface SpotMapper extends BaseMapper<Spot> {
}