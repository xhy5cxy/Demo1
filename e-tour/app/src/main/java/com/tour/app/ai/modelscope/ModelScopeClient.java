package com.tour.app.ai.modelscope;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * ModelScope API客户端
 * 封装阿里ModelScope平台的HTTP API调用
 */
public class ModelScopeClient {
    private static final String TAG = "ModelScopeClient";
    
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    
    private Context context;
    private OkHttpClient client;
    private Gson gson;
    
    public ModelScopeClient(Context context) {
        this.context = context;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        this.gson = new Gson();
    }
    
    /**
     * 获取ModelScope API配置
     * 优先使用系统环境变量 DASHSCOPE_API_KEY，其次使用strings.xml中的配置
     */
    private String getApiKey() {
        // 优先检查系统环境变量
        String envApiKey = System.getenv("DASHSCOPE_API_KEY");
        if (envApiKey != null && !envApiKey.trim().isEmpty()) {
            Log.d(TAG, "使用系统环境变量 DASHSCOPE_API_KEY");
            return envApiKey.trim();
        }
        
        // 其次检查strings.xml中的配置
        try {
            Resources resources = context.getResources();
            String configApiKey = resources.getString(resources.getIdentifier("modelscope_api_key", "string", context.getPackageName()));
            if (!"YOUR_MODELSCOPE_API_KEY".equals(configApiKey) && !configApiKey.trim().isEmpty()) {
                Log.d(TAG, "使用strings.xml中的API-KEY配置");
                return configApiKey;
            }
        } catch (Exception e) {
            Log.e(TAG, "获取strings.xml API-KEY失败", e);
        }
        
        // 如果都未配置，返回默认值
        Log.w(TAG, "未找到有效的API-KEY配置，请设置系统环境变量 DASHSCOPE_API_KEY 或在strings.xml中配置");
        return "YOUR_MODELSCOPE_API_KEY";
    }
    
    /**
     * 获取ModelScope API URL
     */
    private String getApiUrl() {
        try {
            Resources resources = context.getResources();
            return resources.getString(resources.getIdentifier("modelscope_api_url", "string", context.getPackageName()));
        } catch (Exception e) {
            Log.e(TAG, "获取API URL失败", e);
            return "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";
        }
    }
    
    /**
     * 获取ModelScope模型名称
     */
    private String getModelName() {
        try {
            Resources resources = context.getResources();
            return resources.getString(resources.getIdentifier("modelscope_model", "string", context.getPackageName()));
        } catch (Exception e) {
            Log.e(TAG, "获取模型名称失败", e);
            return "qwen-turbo";
        }
    }
    
    /**
     * 调用ModelScope API进行智能对话
     */
    public void callModelScopeAPI(String userMessage, final ModelScopeCallback callback) {
        try {
            // 获取配置参数
            String apiKey = getApiKey();
            String apiUrl = getApiUrl();
            String modelName = getModelName();
            
            // 检查API-KEY是否已配置
            if ("YOUR_MODELSCOPE_API_KEY".equals(apiKey)) {
                callback.onError("请设置系统环境变量 DASHSCOPE_API_KEY 或在strings.xml中配置");
                return;
            }
            
            // 构建请求数据
            ModelScopeRequest request = new ModelScopeRequest(userMessage);
            request.setModel(modelName); // 使用配置的模型名称
            String jsonBody = gson.toJson(request);
            
            // 构建HTTP请求
            Request httpRequest = new Request.Builder()
                    .url(apiUrl)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(jsonBody, JSON))
                    .build();
            
            // 异步执行请求
            client.newCall(httpRequest).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "ModelScope API调用失败", e);
                    callback.onError("网络连接失败: " + e.getMessage());
                }
                
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            String responseBody = response.body().string();
                            Log.d(TAG, "ModelScope API响应: " + responseBody);
                            
                            ModelScopeResponse modelScopeResponse = gson.fromJson(responseBody, ModelScopeResponse.class);
                            if (modelScopeResponse != null) {
                                String reply = modelScopeResponse.getReply();
                                callback.onSuccess(reply);
                            } else {
                                callback.onError("API响应解析失败");
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "响应解析异常", e);
                            callback.onError("响应解析异常: " + e.getMessage());
                        }
                    } else {
                        String errorBody = response.body() != null ? response.body().string() : "未知错误";
                        Log.e(TAG, "API调用失败，状态码: " + response.code() + ", 错误信息: " + errorBody);
                        callback.onError("API调用失败，状态码: " + response.code());
                    }
                }
            });
            
        } catch (Exception e) {
            Log.e(TAG, "构建请求异常", e);
            callback.onError("请求构建失败: " + e.getMessage());
        }
    }
    
    /**
     * 发送消息到ModelScope API（简化接口）
     */
    public void sendMessage(String userMessage, final ModelScopeCallback callback) {
        callModelScopeAPI(userMessage, callback);
    }
    
    /**
     * 生成旅行相关的智能回复
     */
    public void getTravelRecommendation(String userMessage, final ModelScopeCallback callback) {
        // 构建旅行助手专用的提示词
        String travelPrompt = buildTravelPrompt(userMessage);
        callModelScopeAPI(travelPrompt, callback);
    }
    
    /**
     * 构建旅行助手提示词
     */
    private String buildTravelPrompt(String userMessage) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是一个专业的旅行助手，专门帮助用户规划旅行行程、推荐景点、提供旅行建议。");
        prompt.append("请根据用户的问题提供专业、实用的旅行建议。");
        prompt.append("\n\n用户问题：");
        prompt.append(userMessage);
        prompt.append("\n\n请以旅行专家的身份回答：");
        
        return prompt.toString();
    }
    
    /**
     * ModelScope回调接口
     */
    public interface ModelScopeCallback {
        void onSuccess(String response);
        void onError(String error);
    }
    
    /**
     * 设置API-KEY（用于动态配置）
     */
    public static void setApiKey(String apiKey) {
        // 这里可以添加API-KEY的验证逻辑
        Log.d(TAG, "API-KEY已更新");
    }
    
    /**
     * 测试API连接
     */
    public void testConnection(final ModelScopeCallback callback) {
        String testMessage = "你好，请简单介绍一下你自己";
        callModelScopeAPI(testMessage, callback);
    }
}