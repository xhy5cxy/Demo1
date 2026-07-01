package com.tour.app.network;

import com.tour.app.model.User;
import com.tour.app.network.ApiResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 认证相关API接口
 * 匹配后端Spring Boot接口
 */
public interface AuthService {
    
    /**
     * 用户登录
     */
    @POST("user/login")
    Call<ApiResponse<String>> login(@Body LoginRequest request);

    /**
     * 用户注册
     */
    @POST("user/register")
    Call<ApiResponse<String>> register(@Body RegisterRequest request);

    /**
     * 获取用户信息
     */
    @GET("user/info")
    Call<ApiResponse<User>> getUserById(@Path("id") Long userId);
    
    /**
     * 修改密码
     */
    @POST("user/change-password")
    Call<ApiResponse<String>> changePassword(@Body ChangePasswordRequest request);
}