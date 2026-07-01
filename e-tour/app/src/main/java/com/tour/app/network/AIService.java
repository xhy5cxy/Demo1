package com.tour.app.network;

import com.tour.app.ai.model.AIRecommendRequest;
import com.tour.app.ai.model.AIRecommendResponse;
import com.tour.app.ai.model.ChatRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * AI智能推荐和聊天相关API接口
 * 匹配后端接口格式
 */
public interface AIService {
    
    /**
     * 获取热门推荐
     */
    @GET("ai/hot")
    Call<ApiResponse<AIRecommendResponse>> getHotRecommendations();
    
    /**
     * AI智能推荐
     */
    @POST("ai/recommend")
    Call<ApiResponse<AIRecommendResponse>> getAIRecommendations(@Body AIRecommendRequest request);
    
    /**
     * AI智能对话
     */
    @POST("ai/chat")
    Call<ApiResponse<String>> getChatResponse(@Body ChatRequest request);
    
    /**
     * 测试AI服务连接
     */
    @GET("ai/test")
    Call<ApiResponse<String>> testAIService();
    
    /**
     * 获取AI服务状态
     */
    @GET("ai/status")
    Call<ApiResponse<String>> getAIStatus();
}