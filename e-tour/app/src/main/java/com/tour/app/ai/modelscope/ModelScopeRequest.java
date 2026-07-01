package com.tour.app.ai.modelscope;

import com.google.gson.annotations.SerializedName;

/**
 * ModelScope API请求数据模型
 * 对应DashScope平台的聊天API请求格式
 */
public class ModelScopeRequest {
    
    @SerializedName("model")
    private String model;
    
    @SerializedName("messages")
    private Message[] messages;
    
    @SerializedName("parameters")
    private Parameters parameters;
    
    public ModelScopeRequest() {
        // 默认使用通义千问模型
        this.model = "qwen-turbo";
        this.messages = new Message[0];
        this.parameters = new Parameters();
    }
    
    public ModelScopeRequest(String prompt) {
        this();
        this.messages = new Message[]{new Message("user", prompt)};
    }
    
    // Getters and Setters
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public Message[] getMessages() {
        return messages;
    }
    
    public void setMessages(Message[] messages) {
        this.messages = messages;
    }
    
    public Parameters getParameters() {
        return parameters;
    }
    
    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }
    

    
    /**
     * 消息结构
     */
    public static class Message {
        @SerializedName("role")
        private String role;
        
        @SerializedName("content")
        private String content;
        
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
     * 参数配置
     */
    public static class Parameters {
        @SerializedName("result_format")
        private String resultFormat = "message";
        
        @SerializedName("temperature")
        private double temperature = 0.8;
        
        @SerializedName("top_p")
        private double topP = 0.8;
        
        @SerializedName("max_tokens")
        private int maxTokens = 1500;
        
        public String getResultFormat() {
            return resultFormat;
        }
        
        public void setResultFormat(String resultFormat) {
            this.resultFormat = resultFormat;
        }
        
        public double getTemperature() {
            return temperature;
        }
        
        public void setTemperature(double temperature) {
            this.temperature = temperature;
        }
        
        public double getTopP() {
            return topP;
        }
        
        public void setTopP(double topP) {
            this.topP = topP;
        }
        
        public int getMaxTokens() {
            return maxTokens;
        }
        
        public void setMaxTokens(int maxTokens) {
            this.maxTokens = maxTokens;
        }
    }
}