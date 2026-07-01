package com.etour.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.etour.entity.Order;
import com.etour.mapper.OrderMapper;
import com.etour.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Order createOrder(Order order) {
        // 生成订单号
        order.setOrderNo(UUID.randomUUID().toString().replace("-", ""));
        order.setStatus(0); // 待支付
        order.setCreateTime(LocalDateTime.now());
        orderMapper.insert(order);
        return order;
    }

    @Override
    public Order getOrderById(Long id) {
        return orderMapper.selectById(id);
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId)
               .orderByDesc(Order::getCreateTime);
        return orderMapper.selectList(wrapper);
    }

    @Override
    public boolean payOrder(Long orderId, String paymentMethod) {
        Order order = orderMapper.selectById(orderId);
        if (order != null && order.getStatus() == 0) {
            order.setStatus(1); // 已支付
            order.setPayType(paymentMethod.equals("wechat") ? 1 : 2);
            order.setPayTime(LocalDateTime.now());
            return orderMapper.updateById(order) > 0;
        }
        return false;
    }

    @Override
    public boolean cancelOrder(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order != null && order.getStatus() == 0) {
            order.setStatus(4); // 已取消
            order.setUpdateTime(LocalDateTime.now());
            return orderMapper.updateById(order) > 0;
        }
        return false;
    }

    @Override
    public boolean refundOrder(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order != null && order.getStatus() == 1) {
            order.setStatus(3); // 退款中
            order.setUpdateTime(LocalDateTime.now());
            return orderMapper.updateById(order) > 0;
        }
        return false;
    }
}