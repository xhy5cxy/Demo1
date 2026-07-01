package com.etour.controller;

import com.etour.common.Result;
import com.etour.service.ModelScopeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * AI聊天控制器
 * 提供与前端匹配的AI对话接口
 */
@RestController
@RequestMapping("/ai")
@Tag(name = "AI聊天管理", description = "AI智能对话相关接口")
public class AIChatController {
    
    private static final Logger logger = LoggerFactory.getLogger(AIChatController.class);
    
    @Autowired
    private ModelScopeService modelScopeService;
    
    /**
     * AI对话接口
     * 接收用户消息并返回AI回复
     */
    @PostMapping("/chat")
    @Operation(summary = "AI对话", description = "与AI进行智能对话")
    public Result<String> chat(@RequestBody Map<String, String> request) {
        try {
            String message = request.get("message");
            if (message == null || message.trim().isEmpty()) {
                return Result.error("消息内容不能为空");
            }
            
            logger.info("收到AI对话请求: {}", message);
            
            String reply = modelScopeService.sendMessage(message);
            
            return Result.success(reply);
            
        } catch (Exception e) {
            logger.error("AI对话处理失败", e);
            return Result.error("AI服务暂时不可用，请稍后重试");
        }
    }
    
    /**
     * 测试AI服务连接
     */
    @GetMapping("/test")
    @Operation(summary = "测试AI服务", description = "测试阿里魔塔AI服务连接状态")
    public Result<Map<String, Object>> testAIService() {
        try {
            Map<String, Object> result = new HashMap<>();
            
            // 检查服务可用性
            boolean isAvailable = modelScopeService.isServiceAvailable();
            result.put("available", isAvailable);
            
            if (isAvailable) {
                // 测试连接
                String testResult = modelScopeService.testConnection();
                result.put("testResult", testResult);
                result.put("status", "connected");
            } else {
                result.put("status", "not_configured");
                result.put("message", "阿里魔塔AI服务未配置，请检查API密钥设置");
            }
            
            return Result.success(result);
            
        } catch (Exception e) {
            logger.error("测试AI服务失败", e);
            return Result.error("测试AI服务失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取AI服务状态
     */
    @GetMapping("/status")
    @Operation(summary = "获取AI服务状态", description = "获取阿里魔塔AI服务的当前状态")
    public Result<Map<String, Object>> getAIStatus() {
        try {
            Map<String, Object> status = new HashMap<>();
            
            boolean isAvailable = modelScopeService.isServiceAvailable();
            status.put("available", isAvailable);
            status.put("provider", "阿里魔塔");
            status.put("model", "通义千问");
            status.put("timestamp", System.currentTimeMillis());
            
            if (!isAvailable) {
                status.put("message", "请配置MODELSCOPE_API_KEY环境变量");
            }
            
            return Result.success(status);
            
        } catch (Exception e) {
            logger.error("获取AI服务状态失败", e);
            return Result.error("获取AI服务状态失败");
        }
    }
}