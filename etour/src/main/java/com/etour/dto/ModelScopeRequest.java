package com.etour.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 阿里魔塔（ModelScope）API请求数据模型
 * 与前端Android应用保持一致
 */
public class ModelScopeRequest {
    
    /**
     * 模型名称
     */
    private String model;
    
    /**
     * 消息列表
     */
    private List<Message> messages;
    
    /**
     * 参数设置
     */
    private Parameters parameters;
    
    /**
     * 构造函数
     */
    public ModelScopeRequest() {
        this.messages = new ArrayList<>();
        this.parameters = new Parameters();
    }
    
    /**
     * 构造函数（带用户消息）
     */
    public ModelScopeRequest(String userMessage) {
        this();
        addUserMessage(userMessage);
    }
    
    /**
     * 添加用户消息
     */
    public void addUserMessage(String content) {
        this.messages.add(new Message("user", content));
    }
    
    /**
     * 添加系统消息
     */
    public void addSystemMessage(String content) {
        this.messages.add(new Message("system", content));
    }
    
    // Getter和Setter方法
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }
    
    /**
     * 消息内部类
     */
    public static class Message {
        private String role;
        private String content;
        
        public Message() {}
        
        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
        
        public String getRole() {
            return role;
        }
        
        public void setRole(String role) {
            this.role = role;
        }
        
        public String getContent() {
            return content;
        }
        
        public void setContent(String content) {
            this.content = content;
        }
    }
    
    /**
     * 参数内部类
     */
    public static class Parameters {
        private double temperature = 0.7;
        private int maxTokens = 2000;
        private double topP = 0.8;
        
        public double getTemperature() {
            return temperature;
        }
        
        public void setTemperature(double temperature) {
            this.temperature = temperature;
        }
        
        public int getMaxTokens() {
            return maxTokens;
        }
        
        public void setMaxTokens(int maxTokens) {
            this.maxTokens = maxTokens;
        }
        
        public double getTopP() {
            return topP;
        }
        
        public void setTopP(double topP) {
            this.topP = topP;
        }
    }
}