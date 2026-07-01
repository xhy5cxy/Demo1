package com.etour.service;

import com.etour.config.ModelScopeConfig;
import com.etour.dto.ModelScopeRequest;
import com.etour.dto.ModelScopeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * 阿里魔塔（ModelScope）AI服务类
 * 负责与阿里魔塔API进行通信
 */
@Service
public class ModelScopeService {
    
    private static final Logger logger = LoggerFactory.getLogger(ModelScopeService.class);
    
    @Autowired
    private ModelScopeConfig modelScopeConfig;
    
    @Autowired
    private RestTemplate restTemplate;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 发送消息到阿里魔塔AI并获取回复
     */
    public String sendMessage(String userMessage) {
        try {
            // 检查API密钥是否已配置
            if (modelScopeConfig.getApiKey() == null || 
                modelScopeConfig.getApiKey().equals("YOUR_MODELSCOPE_API_KEY")) {
                logger.warn("阿里魔塔API密钥未配置，请设置MODELSCOPE_API_KEY环境变量或在配置文件中配置");
                return "抱歉，AI服务暂不可用，请检查API密钥配置。";
            }
            
            // 构建请求数据
            ModelScopeRequest request = new ModelScopeRequest(userMessage);
            request.setModel(modelScopeConfig.getModel());
            
            // 构建旅行助手专用的提示词
            String travelPrompt = buildTravelPrompt(userMessage);
            request.addSystemMessage(travelPrompt);
            
            // 设置参数
            request.getParameters().setTemperature(modelScopeConfig.getTemperature());
            request.getParameters().setMaxTokens(modelScopeConfig.getMaxTokens());
            
            // 构建HTTP请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.set("Authorization", "Bearer " + modelScopeConfig.getApiKey());
            
            HttpEntity<ModelScopeRequest> entity = new HttpEntity<>(request, headers);
            
            logger.info("发送消息到阿里魔塔AI: {}", userMessage);
            
            // 发送请求
            ResponseEntity<ModelScopeResponse> response = restTemplate.exchange(
                modelScopeConfig.getApiUrl(),
                HttpMethod.POST,
                entity,
                ModelScopeResponse.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String reply = response.getBody().getReply();
                logger.info("阿里魔塔AI回复: {}", reply);
                return reply;
            } else {
                logger.error("阿里魔塔API调用失败，状态码: {}", response.getStatusCode());
                return "抱歉，AI服务暂时无法响应，请稍后重试。";
            }
            
        } catch (Exception e) {
            logger.error("调用阿里魔塔AI服务失败", e);
            return "抱歉，AI服务出现异常，请稍后重试。错误信息: " + e.getMessage();
        }
    }
    
    /**
     * 构建旅行助手专用的提示词
     */
    private String buildTravelPrompt(String userMessage) {
        return "你是一个专业的旅行助手，专门为用户提供旅行相关的建议和帮助。\n" +
               "请根据用户的提问提供专业、实用的旅行建议，包括但不限于：\n" +
               "- 景点推荐和介绍\n" +
               "- 旅行路线规划\n" +
               "- 美食推荐\n" +
               "- 住宿建议\n" +
               "- 交通指南\n" +
               "- 预算规划\n" +
               "- 注意事项\n" +
               "请用友好、专业的语气回答，确保信息准确可靠。\n\n" +
               "用户问题：" + userMessage;
    }
    
    /**
     * 测试阿里魔塔AI连接
     */
    public String testConnection() {
        try {
            String testMessage = "你好，请简单介绍一下你自己";
            return sendMessage(testMessage);
        } catch (Exception e) {
            logger.error("测试阿里魔塔AI连接失败", e);
            return "连接测试失败: " + e.getMessage();
        }
    }
    
    /**
     * 获取AI服务状态
     */
    public boolean isServiceAvailable() {
        return modelScopeConfig.getApiKey() != null && 
               !modelScopeConfig.getApiKey().equals("YOUR_MODELSCOPE_API_KEY");
    }
}