package com.etour.dto;

import lombok.Data;

/**
 * 头像上传响应DTO
 */
@Data
public class AvatarUploadResponse {
    
    private boolean success;
    private String message;
    private String avatarUrl;

    public AvatarUploadResponse() {}

    public AvatarUploadResponse(boolean success, String message, String avatarUrl) {
        this.success = success;
        this.message = message;
        this.avatarUrl = avatarUrl;
    }

    /**
     * 成功响应
     */
    public static AvatarUploadResponse success(String message, String avatarUrl) {
        return new AvatarUploadResponse(true, message, avatarUrl);
    }

    /**
     * 错误响应
     */
    public static AvatarUploadResponse error(String message) {
        return new AvatarUploadResponse(false, message, null);
    }

    /**
     * 检查是否成功
     */
    public boolean isSuccess() {
        return success;
    }
}