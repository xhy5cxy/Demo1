package com.etour.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传服务接口
 */
public interface FileUploadService {

    /**
     * 上传图片
     * @param file 图片文件
     * @return 图片URL
     */
    String uploadImage(MultipartFile file);

    /**
     * 上传头像
     * @param file 头像文件
     * @return 头像URL
     */
    String uploadAvatar(MultipartFile file);
}