package com.etour.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectRequest;
import com.etour.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    private OSS ossClient;
    
    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;
    
    @Value("${aliyun.oss.url-prefix}")
    private String urlPrefix;

    @Override
    public String uploadAvatar(MultipartFile file) {
        return uploadToOSS(file, "avatar/");
    }

    @Override
    public String uploadImage(MultipartFile file) {
        return uploadToOSS(file, "images/");
    }

    private String uploadToOSS(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }

        // 验证文件类型
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        }
        
        if (!".jpg".equals(fileExtension) && !".jpeg".equals(fileExtension) && !".png".equals(fileExtension)) {
            throw new RuntimeException("只支持jpg、jpeg、png格式的图片");
        }

        // 验证文件大小（最大5MB）
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("文件大小不能超过5MB");
        }

        try {
            // 生成文件名：日期 + UUID + 扩展名
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String dateStr = dateFormat.format(new Date());
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String fileName = folder + dateStr + "_" + uuid + fileExtension;
            
            // 上传到OSS
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, file.getInputStream());
            ossClient.putObject(putObjectRequest);
            
            // 返回访问URL
            return urlPrefix + "/" + fileName;
            
        } catch (Exception e) {
            throw new RuntimeException("文件上传到OSS失败: " + e.getMessage(), e);
        }
    }
}