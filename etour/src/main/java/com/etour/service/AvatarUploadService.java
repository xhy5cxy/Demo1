package com.etour.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 头像上传服务类
 */
@Slf4j
@Service
public class AvatarUploadService {

    // 头像存储目录
    private static final String AVATAR_UPLOAD_DIR = "uploads/avatars/";
    
    // 允许的文件类型
    private static final String[] ALLOWED_EXTENSIONS = {"jpg", "jpeg", "png", "gif"};
    
    // 最大文件大小（2MB）
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;

    /**
     * 上传头像文件
     */
    public String uploadAvatar(MultipartFile file, Long userId) throws IOException {
        // 验证文件
        validateFile(file);
        
        // 创建存储目录
        createUploadDirectory();
        
        // 生成唯一文件名
        String fileName = generateUniqueFileName(file.getOriginalFilename());
        
        // 构建完整文件路径
        String filePath = AVATAR_UPLOAD_DIR + fileName;
        
        // 保存文件
        File dest = new File(filePath);
        file.transferTo(dest);
        
        // 生成访问URL
        String avatarUrl = "/" + filePath;
        
        log.info("头像文件上传成功，用户ID: {}, 文件路径: {}", userId, filePath);
        return avatarUrl;
    }

    /**
     * 删除头像文件
     */
    public boolean deleteAvatarFile(String avatarUrl) {
        try {
            if (avatarUrl == null || avatarUrl.isEmpty()) {
                return true;
            }
            
            // 从URL中提取文件路径
            String filePath = avatarUrl.startsWith("/") ? avatarUrl.substring(1) : avatarUrl;
            
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                boolean deleted = file.delete();
                if (deleted) {
                    log.info("头像文件删除成功: {}", filePath);
                } else {
                    log.warn("头像文件删除失败: {}", filePath);
                }
                return deleted;
            }
            
            log.warn("头像文件不存在: {}", filePath);
            return true;
        } catch (Exception e) {
            log.error("删除头像文件失败: {}", avatarUrl, e);
            return false;
        }
    }

    /**
     * 验证文件
     */
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }
        
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("文件大小不能超过2MB");
        }
        
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("文件名不能为空");
        }
        
        String extension = getFileExtension(originalFilename).toLowerCase();
        boolean validExtension = false;
        for (String allowedExt : ALLOWED_EXTENSIONS) {
            if (allowedExt.equals(extension)) {
                validExtension = true;
                break;
            }
        }
        
        if (!validExtension) {
            throw new IllegalArgumentException("不支持的文件类型，仅支持: " + String.join(", ", ALLOWED_EXTENSIONS));
        }
    }

    /**
     * 创建上传目录
     */
    private void createUploadDirectory() throws IOException {
        Path uploadPath = Paths.get(AVATAR_UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            log.info("创建头像上传目录: {}", uploadPath.toAbsolutePath());
        }
    }

    /**
     * 生成唯一文件名
     */
    private String generateUniqueFileName(String originalFilename) {
        String extension = getFileExtension(originalFilename);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return String.format("avatar_%s_%s.%s", timestamp, uuid, extension);
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            return filename.substring(dotIndex + 1);
        }
        throw new IllegalArgumentException("无法获取文件扩展名: " + filename);
    }

    /**
     * 获取头像文件信息
     */
    public File getAvatarFile(String avatarUrl) {
        if (avatarUrl == null || avatarUrl.isEmpty()) {
            return null;
        }
        
        String filePath = avatarUrl.startsWith("/") ? avatarUrl.substring(1) : avatarUrl;
        File file = new File(filePath);
        return file.exists() && file.isFile() ? file : null;
    }
}