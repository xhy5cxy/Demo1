package com.tour.app.network;

import com.google.gson.annotations.SerializedName;

/**
 * 统一API响应格式
 * 匹配后端Result类格式
 */
public class ApiResponse<T> {
    @SerializedName("code")
    private Integer code;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("data")
    private T data;
    
    @SerializedName("timestamp")
    private Long timestamp;

    public ApiResponse() {}

    public ApiResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    // 成功判断
    public boolean isSuccess() {
        return code != null && code == 200;
    }

    // Getters and Setters
    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
}
