package com.etour.controller;

import com.etour.common.Result;
import com.etour.entity.Order;
import com.etour.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
@Tag(name = "支付管理", description = "订单支付相关接口")
public class PaymentController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    @Operation(summary = "创建订单", description = "创建支付订单")
    public Result<Order> createOrder(@RequestBody Order order) {
        try {
            Order createdOrder = orderService.createOrder(order);
            return Result.success(createdOrder);
        } catch (Exception e) {
            return Result.error("创建订单失败: " + e.getMessage());
        }
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "获取订单详情", description = "获取指定订单的详细信息")
    public Result<Order> getOrderDetail(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        return order != null ? Result.success(order) : Result.error("订单不存在");
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户订单", description = "获取指定用户的所有订单")
    public Result<List<Order>> getUserOrders(@PathVariable Long userId) {
        List<Order> orders = orderService.getUserOrders(userId);
        return Result.success(orders);
    }

    @PostMapping("/pay")
    @Operation(summary = "支付订单", description = "支付指定订单")
    public Result<Void> payOrder(@RequestParam Long orderId, @RequestParam String paymentMethod) {
        try {
            boolean success = orderService.payOrder(orderId, paymentMethod);
            return success ? Result.success() : Result.error("支付失败");
        } catch (Exception e) {
            return Result.error("支付失败: " + e.getMessage());
        }
    }

    @PostMapping("/cancel/{orderId}")
    @Operation(summary = "取消订单", description = "取消指定订单")
    public Result<Void> cancelOrder(@PathVariable Long orderId) {
        boolean success = orderService.cancelOrder(orderId);
        return success ? Result.success() : Result.error("取消失败");
    }

    @PostMapping("/refund/{orderId}")
    @Operation(summary = "申请退款", description = "申请订单退款")
    public Result<Void> refundOrder(@PathVariable Long orderId) {
        try {
            boolean success = orderService.refundOrder(orderId);
            return success ? Result.success() : Result.error("退款失败");
        } catch (Exception e) {
            return Result.error("退款失败: " + e.getMessage());
        }
    }
}