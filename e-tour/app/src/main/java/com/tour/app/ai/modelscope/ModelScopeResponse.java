package com.tour.app.ai.modelscope;

import com.google.gson.annotations.SerializedName;

/**
 * ModelScope API响应数据模型
 * 对应DashScope平台的聊天API响应格式
 */
public class ModelScopeResponse {
    
    @SerializedName("choices")
    private Choice[] choices;
    
    @SerializedName("usage")
    private Usage usage;
    
    @SerializedName("id")
    private String id;
    
    @SerializedName("model")
    private String model;
    
    // Getters and Setters
    public Choice[] getChoices() {
        return choices;
    }
    
    public void setChoices(Choice[] choices) {
        this.choices = choices;
    }
    
    public Usage getUsage() {
        return usage;
    }
    
    public void setUsage(Usage usage) {
        this.usage = usage;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    /**
     * 获取AI回复内容
     */
    public String getReply() {
        if (choices != null && choices.length > 0) {
            Choice choice = choices[0];
            if (choice != null && choice.getMessage() != null) {
                return choice.getMessage().getContent();
            }
        }
        return "抱歉，我暂时无法回答这个问题。";
    }
    

    
    /**
     * 选择项结构
     */
    public static class Choice {
        @SerializedName("message")
        private Message message;
        
        @SerializedName("finish_reason")
        private String finishReason;
        
        public Message getMessage() {
            return message;
        }
        
        public void setMessage(Message message) {
            this.message = message;
        }
        
        public String getFinishReason() {
            return finishReason;
        }
        
        public void setFinishReason(String finishReason) {
            this.finishReason = finishReason;
        }
    }
    
    /**
     * 消息结构
     */
    public static class Message {
        @SerializedName("role")
        private String role;
        
        @SerializedName("content")
        private String content;
        
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
     * 使用量统计
     */
    public static class Usage {
        @SerializedName("input_tokens")
        private int inputTokens;
        
        @SerializedName("output_tokens")
        private int outputTokens;
        
        @SerializedName("total_tokens")
        private int totalTokens;
        
        public int getInputTokens() {
            return inputTokens;
        }
        
        public void setInputTokens(int inputTokens) {
            this.inputTokens = inputTokens;
        }
        
        public int getOutputTokens() {
            return outputTokens;
        }
        
        public void setOutputTokens(int outputTokens) {
            this.outputTokens = outputTokens;
        }
        
        public int getTotalTokens() {
            return totalTokens;
        }
        
        public void setTotalTokens(int totalTokens) {
            this.totalTokens = totalTokens;
        }
    }
}