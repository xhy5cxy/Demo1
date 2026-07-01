package com.etour.dto;

/**
 * 用户信息请求DTO
 */
public class UserInfoRequest {
    private Long userId;

    public UserInfoRequest() {}

    public UserInfoRequest(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}