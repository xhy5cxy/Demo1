package com.tour.app.ai.model;

/**
 * AI聊天请求数据模型
 * 用于向后端发送聊天消息
 */
public class ChatRequest {
    
    /**
     * 用户消息内容
     */
    private String message;
    
    /**
     * 默认构造函数
     */
    public ChatRequest() {
    }
    
    /**
     * 带消息内容的构造函数
     * @param message 用户消息
     */
    public ChatRequest(String message) {
        this.message = message;
    }
    
    /**
     * 获取消息内容
     * @return 消息内容
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * 设置消息内容
     * @param message 消息内容
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
        return "ChatRequest{" +
                "message='" + message + '\'' +
                '}';
    }
}