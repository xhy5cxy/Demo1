package com.tour.app.ai.service;

import android.content.Context;
import android.util.Log;

import com.tour.app.ai.model.AIRecommendRequest;
import com.tour.app.ai.model.AIRecommendResponse;
import com.tour.app.ai.model.ChatRequest;
import com.tour.app.model.Spot;
import com.tour.app.network.ApiClient;
import com.tour.app.network.AIService;
import com.tour.app.network.ApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.tour.app.ai.modelscope.ModelScopeClient;
import com.tour.app.ai.modelscope.ModelScopeRequest;
import com.tour.app.ai.modelscope.ModelScopeResponse;

/**
 * AI服务管理器
 * 负责处理AI接口的调用和响应处理
 */
public class AIServiceManager {
    private static final String TAG = "AIServiceManager";
    
    private AIService aiService;
    private ModelScopeClient modelScopeClient;
    private Context context;
    
    // AI服务类型枚举
    public enum AIServiceType {
        LOCAL_SERVICE,    // 本地AI服务
        MODEL_SCOPE       // 阿里ModelScope服务
    }
    
    // 当前使用的AI服务类型（默认为ModelScope）
    private AIServiceType currentServiceType = AIServiceType.MODEL_SCOPE;
    
    public AIServiceManager(Context context) {
        this.context = context;
        this.aiService = ApiClient.getClient(context).create(AIService.class);
        this.modelScopeClient = new ModelScopeClient(context);
    }
    
    /**
     * 设置AI服务类型
     */
    public void setAIServiceType(AIServiceType serviceType) {
        this.currentServiceType = serviceType;
        Log.d(TAG, "AI服务类型已设置为: " + serviceType);
    }
    
    /**
     * 获取当前AI服务类型
     */
    public AIServiceType getCurrentServiceType() {
        return currentServiceType;
    }
    
    /**
     * 获取热门推荐
     */
    public void getHotRecommendations(final AICallback<AIRecommendResponse> callback) {
        Call<ApiResponse<AIRecommendResponse>> call = aiService.getHotRecommendations();
        
        call.enqueue(new Callback<ApiResponse<AIRecommendResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AIRecommendResponse>> call, Response<ApiResponse<AIRecommendResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<AIRecommendResponse> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError("API错误: " + apiResponse.getMessage());
                    }
                } else {
                    callback.onError("网络请求失败: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<AIRecommendResponse>> call, Throwable t) {
                Log.e(TAG, "获取热门推荐失败", t);
                callback.onError("网络连接失败: " + t.getMessage());
            }
        });
    }
    
    /**
     * AI智能推荐
     */
    public void getAIRecommendations(AIRecommendRequest request, final AICallback<AIRecommendResponse> callback) {
        Call<ApiResponse<AIRecommendResponse>> call = aiService.getAIRecommendations(request);
        
        call.enqueue(new Callback<ApiResponse<AIRecommendResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AIRecommendResponse>> call, Response<ApiResponse<AIRecommendResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<AIRecommendResponse> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError("AI推荐失败: " + apiResponse.getMessage());
                    }
                } else {
                    callback.onError("网络请求失败: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<AIRecommendResponse>> call, Throwable t) {
                Log.e(TAG, "AI智能推荐失败", t);
                callback.onError("网络连接失败: " + t.getMessage());
            }
        });
    }
    
    /**
     * 智能对话回复（基于用户消息生成AI回复）
     */
    public void getChatResponse(String userMessage, final AICallback<String> callback) {
        // 根据当前服务类型选择不同的AI接口
        if (currentServiceType == AIServiceType.MODEL_SCOPE) {
            // 使用阿里ModelScope服务
            getChatResponseFromModelScope(userMessage, callback);
        } else {
            // 使用本地AI服务（后端API）
            getChatResponseFromBackendService(userMessage, callback);
        }
    }
    
    /**
     * 使用阿里ModelScope服务获取聊天回复
     */
    private void getChatResponseFromModelScope(String userMessage, final AICallback<String> callback) {
        // 直接使用用户消息，不添加额外提示词，让AI自由对话
        modelScopeClient.sendMessage(userMessage, new ModelScopeClient.ModelScopeCallback() {
            @Override
            public void onSuccess(String response) {
                callback.onSuccess(response);
            }
            
            @Override
            public void onError(String error) {
                Log.e(TAG, "ModelScope API调用失败: " + error);
                // 如果ModelScope服务失败，降级到后端服务
                getChatResponseFromBackendService(userMessage, callback);
            }
        });
    }
    
    /**
     * 使用后端AI服务获取聊天回复
     */
    private void getChatResponseFromBackendService(String userMessage, final AICallback<String> callback) {
        // 创建聊天请求
        ChatRequest chatRequest = new ChatRequest(userMessage);
        
        // 调用后端AI聊天接口
        Call<ApiResponse<String>> call = aiService.getChatResponse(chatRequest);
        
        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<String> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        Log.e(TAG, "后端AI服务API错误: " + apiResponse.getMessage());
                        // 如果后端服务失败，使用简单回复
                        String simpleResponse = "我收到了您的消息：" + userMessage + "。目前AI服务暂时不可用，请稍后重试。";
                        callback.onSuccess(simpleResponse);
                    }
                } else {
                    Log.e(TAG, "后端AI服务网络请求失败: " + response.code());
                    // 如果网络请求失败，使用简单回复
                    String simpleResponse = "我收到了您的消息：" + userMessage + "。网络连接出现问题，请检查网络后重试。";
                    callback.onSuccess(simpleResponse);
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                Log.e(TAG, "后端AI服务网络连接失败", t);
                // 如果网络连接失败，使用简单回复
                String simpleResponse = "我收到了您的消息：" + userMessage + "。网络连接失败，请稍后重试。";
                callback.onSuccess(simpleResponse);
            }
        });
    }
    
    /**
     * 生成默认回复（当所有AI服务都不可用时使用）
     */
    private String generateDefaultResponse(String userMessage) {
        // 简单的关键词匹配回复
        if (userMessage.contains("行程") || userMessage.contains("规划") || userMessage.contains("旅游")) {
            return "我可以帮您规划旅行行程！请告诉我您的目的地、出行时间、预算和兴趣偏好，我将为您定制专属行程。";
        } else if (userMessage.contains("酒店") || userMessage.contains("住宿")) {
            return "我可以为您推荐合适的酒店！请告诉我您的预算范围、入住时间和位置偏好。";
        } else if (userMessage.contains("美食") || userMessage.contains("餐厅")) {
            return "我可以推荐当地特色美食！请告诉我您喜欢的美食类型和预算范围。";
        } else if (userMessage.contains("天气") || userMessage.contains("气候")) {
            return "我可以提供天气信息！请告诉我您要查询的目的地和时间。";
        } else {
            return "感谢您的提问！我是您的旅行助手，可以帮您规划行程、推荐景点、预订服务等。请告诉我您的具体需求，比如：\\n\\n• 想去哪里旅游？\\n• 计划玩几天？\\n• 预算大概多少？\\n• 有什么特别的兴趣偏好？";
        }
    }
    
    /**
     * AI回调接口
     */
    public interface AICallback<T> {
        void onSuccess(T data);
        void onError(String error);
    }
}