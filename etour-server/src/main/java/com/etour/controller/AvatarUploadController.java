package com.etour.controller;

import com.etour.dto.AvatarUploadRequest;
import com.etour.dto.AvatarUploadResponse;
import com.etour.entity.User;
import com.etour.service.AvatarUploadService;
import com.etour.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * 头像上传控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/avatar")
@Tag(name = "头像上传", description = "用户头像上传相关接口")
public class AvatarUploadController {

    @Autowired
    private AvatarUploadService avatarUploadService;

    @Autowired
    private UserService userService;

    /**
     * 上传头像
     */
    @Operation(summary = "上传头像", description = "上传用户头像")
    @PostMapping("/upload")
    public AvatarUploadResponse uploadAvatar(@Valid AvatarUploadRequest request) {
        try {
            // 验证用户是否存在
            User user = userService.getById(request.getUserId());
            if (user == null) {
                return AvatarUploadResponse.error("用户不存在");
            }

            // 上传头像文件
            String avatarUrl = avatarUploadService.uploadAvatar(request.getAvatarFile(), request.getUserId());

            // 更新用户头像URL
            user.setAvatar(avatarUrl);
            boolean updateResult = userService.updateById(user);
            
            if (updateResult) {
                log.info("用户 {} 头像上传成功，URL: {}", request.getUserId(), avatarUrl);
                return AvatarUploadResponse.success("头像上传成功", avatarUrl);
            } else {
                // 如果更新失败，删除已上传的文件
                avatarUploadService.deleteAvatarFile(avatarUrl);
                return AvatarUploadResponse.error("头像更新失败");
            }
        } catch (Exception e) {
            log.error("头像上传失败，用户ID: {}", request.getUserId(), e);
            return AvatarUploadResponse.error("头像上传失败: " + e.getMessage());
        }
    }

    /**
     * 删除头像
     */
    @Operation(summary = "删除头像", description = "删除用户头像")
    @DeleteMapping("/delete/{userId}")
    public AvatarUploadResponse deleteAvatar(@PathVariable Long userId) {
        try {
            // 验证用户是否存在
            User user = userService.getById(userId);
            if (user == null) {
                return AvatarUploadResponse.error("用户不存在");
            }

            // 删除头像文件
            if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                avatarUploadService.deleteAvatarFile(user.getAvatar());
            }

            // 更新用户头像URL为空
            user.setAvatar(null);
            boolean updateResult = userService.updateById(user);
            
            if (updateResult) {
                log.info("用户 {} 头像删除成功", userId);
                return AvatarUploadResponse.success("头像删除成功", null);
            } else {
                return AvatarUploadResponse.error("头像删除失败");
            }
        } catch (Exception e) {
            log.error("头像删除失败，用户ID: {}", userId, e);
            return AvatarUploadResponse.error("头像删除失败: " + e.getMessage());
        }
    }

    /**
     * 获取头像信息
     */
    @Operation(summary = "获取头像信息", description = "获取用户头像信息")
    @GetMapping("/info/{userId}")
    public AvatarUploadResponse getAvatarInfo(@PathVariable Long userId) {
        try {
            // 验证用户是否存在
            User user = userService.getById(userId);
            if (user == null) {
                return AvatarUploadResponse.error("用户不存在");
            }

            String avatarUrl = user.getAvatar();
            if (avatarUrl == null || avatarUrl.isEmpty()) {
                return AvatarUploadResponse.success("用户暂无头像", null);
            }

            return AvatarUploadResponse.success("获取头像信息成功", avatarUrl);
        } catch (Exception e) {
            log.error("获取头像信息失败，用户ID: {}", userId, e);
            return AvatarUploadResponse.error("获取头像信息失败: " + e.getMessage());
        }
    }

    /**
     * 直接上传头像（兼容旧版接口）
     */
    @Operation(summary = "直接上传头像", description = "直接上传头像文件")
    @PostMapping("/upload/direct")
    public AvatarUploadResponse uploadAvatarDirect(@RequestParam("avatarFile") MultipartFile avatarFile,
                                                   @RequestParam("userId") Long userId) {
        AvatarUploadRequest request = new AvatarUploadRequest();
        request.setUserId(userId);
        request.setAvatarFile(avatarFile);
        return uploadAvatar(request);
    }
}