package com.tour.app.network;

import com.google.gson.annotations.SerializedName;

/**
 * 修改密码请求模型
 */
public class ChangePasswordRequest {
    @SerializedName("userId")
    private Long userId;
    
    @SerializedName("oldPassword")
    private String oldPassword;
    
    @SerializedName("newPassword")
    private String newPassword;

    public ChangePasswordRequest() {}

    public ChangePasswordRequest(Long userId, String oldPassword, String newPassword) {
        this.userId = userId;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getOldPassword() { return oldPassword; }
    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }
    
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}