package com.etour.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

/**
 * 文件上传配置
 */
@Configuration
public class FileUploadConfig {

    /**
     * 配置文件上传参数
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        
        // 单个文件最大大小：2MB
        factory.setMaxFileSize(DataSize.ofMegabytes(2));
        
        // 总上传数据最大大小：10MB
        factory.setMaxRequestSize(DataSize.ofMegabytes(10));
        
        // 设置临时文件存储路径
        factory.setLocation("d:\\tour\\temp");
        
        return factory.createMultipartConfig();
    }
}