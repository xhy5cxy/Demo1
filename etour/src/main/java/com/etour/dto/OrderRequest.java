package com.etour.dto;

/**
 * 订单请求DTO
 */
public class OrderRequest {
    private Long userId;
    private Long strategyId;
    private Long spotId;
    private String paymentMethod;
    private Double amount;

    public OrderRequest() {}

    public OrderRequest(Long userId, Long strategyId, Long spotId, String paymentMethod, Double amount) {
        this.userId = userId;
        this.strategyId = strategyId;
        this.spotId = spotId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Long strategyId) {
        this.strategyId = strategyId;
    }

    public Long getSpotId() {
        return spotId;
    }

    public void setSpotId(Long spotId) {
        this.spotId = spotId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}