package com.etour.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * 头像上传请求DTO
 */
@Data
public class AvatarUploadRequest {
    
    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    /**
     * 头像文件
     */
    @NotNull(message = "头像文件不能为空")
    private MultipartFile avatarFile;
}