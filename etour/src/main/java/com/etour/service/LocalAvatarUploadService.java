package com.etour.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 本地头像上传服务
 */
@Slf4j
@Service
public class LocalAvatarUploadService {

    // 允许的文件类型
    private static final List<String> ALLOWED_FILE_TYPES = Arrays.asList("jpg", "jpeg", "png", "gif");
    
    // 最大文件大小：2MB
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;
    
    // 头像存储路径
    @Value("${file.upload.path:uploads/avatar}")
    private String avatarUploadPath;
    
    // 头像访问路径前缀
    @Value("${file.access.prefix:/uploads/avatar}")
    private String avatarAccessPrefix;

    /**
     * 上传头像文件
     */
    public String uploadAvatar(MultipartFile file, Long userId) throws IOException {
        // 1. 验证文件
        validateFile(file);
        
        // 2. 生成唯一文件名
        String originalFileName = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFileName);
        String uniqueFileName = generateUniqueFileName(userId, fileExtension);
        
        // 3. 创建存储目录
        createUploadDirectory();
        
        // 4. 保存文件
        String filePath = avatarUploadPath + File.separator + uniqueFileName;
        File destFile = new File(filePath);
        file.transferTo(destFile);
        
        // 5. 生成访问URL
        String avatarUrl = avatarAccessPrefix + "/" + uniqueFileName;
        
        log.info("头像上传成功，用户ID: {}, 文件路径: {}", userId, avatarUrl);
        return avatarUrl;
    }

    /**
     * 验证文件
     */
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("上传文件不能为空");
        }
        
        // 检查文件大小
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("文件大小不能超过2MB");
        }
        
        // 检查文件类型
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            throw new RuntimeException("文件名不能为空");
        }
        
        String fileExtension = getFileExtension(originalFileName).toLowerCase();
        if (!ALLOWED_FILE_TYPES.contains(fileExtension)) {
            throw new RuntimeException("不支持的文件类型，仅支持: " + ALLOWED_FILE_TYPES);
        }
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 生成唯一文件名
     */
    private String generateUniqueFileName(Long userId, String fileExtension) {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomStr = UUID.randomUUID().toString().substring(0, 8);
        return String.format("%s_%s_%s.%s", userId, dateStr, randomStr, fileExtension);
    }

    /**
     * 创建上传目录
     */
    private void createUploadDirectory() throws IOException {
        Path uploadPath = Paths.get(avatarUploadPath);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            log.info("创建头像上传目录: {}", uploadPath.toAbsolutePath());
        }
    }

    /**
     * 删除头像文件
     */
    public boolean deleteAvatarFile(String avatarUrl) {
        if (avatarUrl == null || avatarUrl.isEmpty()) {
            return true;
        }
        
        try {
            // 从URL中提取文件名
            String fileName = avatarUrl.replace(avatarAccessPrefix + "/", "");
            String filePath = avatarUploadPath + File.separator + fileName;
            
            File oldFile = new File(filePath);
            if (oldFile.exists() && oldFile.isFile()) {
                boolean deleted = oldFile.delete();
                if (deleted) {
                    log.info("删除头像文件成功: {}", filePath);
                } else {
                    log.warn("删除头像文件失败: {}", filePath);
                }
                return deleted;
            }
            return true;
        } catch (Exception e) {
            log.error("删除头像文件异常: {}", avatarUrl, e);
            return false;
        }
    }
}