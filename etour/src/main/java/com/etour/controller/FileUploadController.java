package com.etour.controller;

import com.etour.common.Result;
import com.etour.service.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传控制器
 */
@Slf4j
@RestController
@RequestMapping("/upload")
@Tag(name = "文件上传", description = "文件上传相关接口")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/image")
    @Operation(summary = "上传图片", description = "上传图片到阿里云OSS")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            log.info("收到图片上传请求，文件名: {}, 大小: {} bytes", file.getOriginalFilename(), file.getSize());
            
            if (file.isEmpty()) {
                log.error("上传文件为空");
                return Result.error("上传文件不能为空");
            }
            
            String url = fileUploadService.uploadImage(file);
            log.info("图片上传成功，URL: {}", url);
            return Result.success(url);
        } catch (Exception e) {
            log.error("图片上传失败: {}", e.getMessage(), e);
            return Result.error("图片上传失败: " + e.getMessage());
        }
    }

    @PostMapping("/avatar")
    @Operation(summary = "上传头像", description = "上传头像到阿里云OSS")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            log.info("收到头像上传请求，文件名: {}, 大小: {} bytes", file.getOriginalFilename(), file.getSize());
            
            if (file.isEmpty()) {
                log.error("上传文件为空");
                return Result.error("上传文件不能为空");
            }
            
            // 使用阿里云OSS服务上传头像
            String url = fileUploadService.uploadAvatar(file);
            log.info("头像上传成功，URL: {}", url);
            return Result.success(url);
        } catch (Exception e) {
            log.error("头像上传失败: {}", e.getMessage(), e);
            return Result.error("头像上传失败: " + e.getMessage());
        }
    }
}