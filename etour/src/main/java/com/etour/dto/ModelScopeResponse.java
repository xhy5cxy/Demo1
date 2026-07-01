package com.etour.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 阿里魔塔（ModelScope）API响应数据模型
 * 与前端Android应用保持一致
 */
public class ModelScopeResponse {
    
    /**
     * 请求ID
     */
    @JsonProperty("request_id")
    private String requestId;
    
    /**
     * 输出结果
     */
    private Output output;
    
    /**
     * 使用量统计
     */
    private Usage usage;
    
    /**
     * 获取AI回复内容
     */
    public String getReply() {
        if (output != null && output.getChoices() != null && output.getChoices().length > 0) {
            Choice choice = output.getChoices()[0];
            if (choice != null && choice.getMessage() != null) {
                return choice.getMessage().getContent();
            }
        }
        return "抱歉，我暂时无法回答这个问题。";
    }
    
    // Getter和Setter方法
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Output getOutput() {
        return output;
    }

    public void setOutput(Output output) {
        this.output = output;
    }

    public Usage getUsage() {
        return usage;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }
    
    /**
     * 输出内部类
     */
    public static class Output {
        private Choice[] choices;
        
        public Choice[] getChoices() {
            return choices;
        }
        
        public void setChoices(Choice[] choices) {
            this.choices = choices;
        }
    }
    
    /**
     * 选择项内部类
     */
    public static class Choice {
        private Message message;
        
        public Message getMessage() {
            return message;
        }
        
        public void setMessage(Message message) {
            this.message = message;
        }
    }
    
    /**
     * 消息内部类
     */
    public static class Message {
        private String role;
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
     * 使用量统计内部类
     */
    public static class Usage {
        @JsonProperty("input_tokens")
        private int inputTokens;
        
        @JsonProperty("output_tokens")
        private int outputTokens;
        
        @JsonProperty("total_tokens")
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