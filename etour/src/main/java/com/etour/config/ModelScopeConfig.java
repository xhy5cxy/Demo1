package com.etour.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里魔塔（ModelScope）AI配置类
 * 用于读取配置文件中的ModelScope相关参数
 */
@Configuration
@ConfigurationProperties(prefix = "ai.modelscope")
public class ModelScopeConfig {
    
    /**
     * API密钥
     */
    private String apiKey;
    
    /**
     * API接口地址
     */
    private String apiUrl;
    
    /**
     * 模型名称
     */
    private String model;
    
    /**
     * 请求超时时间（毫秒）
     */
    private int timeout;
    
    /**
     * 最大令牌数
     */
    private int maxTokens;
    
    /**
     * 温度参数
     */
    private double temperature;

    // Getter和Setter方法
    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}