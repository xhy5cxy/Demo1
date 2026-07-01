package com.tour.app.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 */
public class Order {
    private Long id;
    private String orderNo;
    private Long userId;
    private BigDecimal amount;
    private Integer payType; // 1-微信，2-支付宝
    private Integer status; // 0-待支付，1-已支付，2-已取消，3-已退款
    private LocalDateTime payTime;
    private LocalDateTime expireTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 关联数据
    private User user;

    public Order() {}

    public Order(Long id, String orderNo, Long userId, BigDecimal amount, Integer payType, 
                Integer status, LocalDateTime payTime, LocalDateTime expireTime) {
        this.id = id;
        this.orderNo = orderNo;
        this.userId = userId;
        this.amount = amount;
        this.payType = payType;
        this.status = status;
        this.payTime = payTime;
        this.expireTime = expireTime;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public Integer getPayType() { return payType; }
    public void setPayType(Integer payType) { this.payType = payType; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public LocalDateTime getPayTime() { return payTime; }
    public void setPayTime(LocalDateTime payTime) { this.payTime = payTime; }

    public LocalDateTime getExpireTime() { return expireTime; }
    public void setExpireTime(LocalDateTime expireTime) { this.expireTime = expireTime; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
