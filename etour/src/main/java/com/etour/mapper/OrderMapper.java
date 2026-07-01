package com.etour.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.etour.entity.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单Mapper接口
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}