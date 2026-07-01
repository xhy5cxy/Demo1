package com.etour.dto;

/**
 * 收藏请求DTO
 */
public class CollectRequest {
    private Long userId;
    private Long strategyId;
    private Long spotId;

    public CollectRequest() {}

    public CollectRequest(Long userId, Long strategyId, Long spotId) {
        this.userId = userId;
        this.strategyId = strategyId;
        this.spotId = spotId;
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
}