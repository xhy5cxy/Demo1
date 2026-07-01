package com.etour.service;

import com.etour.entity.Order;
import java.util.List;

public interface OrderService {
    Order createOrder(Order order);
    Order getOrderById(Long id);
    List<Order> getUserOrders(Long userId);
    boolean payOrder(Long orderId, String paymentMethod);
    boolean cancelOrder(Long orderId);
    boolean refundOrder(Long orderId);
}