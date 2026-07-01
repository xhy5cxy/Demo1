package com.tour.app.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.tour.app.network.ApiClient;
import com.tour.app.network.ApiResponse;
import com.tour.app.network.AuthService;
import com.tour.app.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 网络连接测试工具类
 * 用于诊断网络连接问题
 */
public class NetworkTest {
    private static final String TAG = "NetworkTest";
    
    /**
     * 测试后端连接
     */
    public static void testBackendConnection(Context context) {
        try {
            AuthService authService = ApiClient.getAuthService(context);
            
            // 测试登录接口
            com.tour.app.network.LoginRequest request = new com.tour.app.network.LoginRequest("admin", "123456");
            
            authService.login(request).enqueue(new Callback<ApiResponse<String>>() {
                @Override
                public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                    Log.d(TAG, "Response code: " + response.code());
                    Log.d(TAG, "Response message: " + response.message());
                    
                    if (response.isSuccessful()) {
                        ApiResponse<String> apiResponse = response.body();
                        if (apiResponse != null) {
                            Log.d(TAG, "API response code: " + apiResponse.getCode());
                            Log.d(TAG, "API response message: " + apiResponse.getMessage());
                            
                            if (apiResponse.isSuccess()) {
                                String token = apiResponse.getData();
                                Log.d(TAG, "Token: " + token);
                                Toast.makeText(context, "✅ 网络连接正常！", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, "❌ API返回错误: " + apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(context, "❌ 响应体为空", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "❌ HTTP错误: " + response.code(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                    Log.e(TAG, "网络错误", t);
                    Toast.makeText(context, "❌ 网络错误: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            
        } catch (Exception e) {
            Log.e(TAG, "测试异常", e);
            Toast.makeText(context, "❌ 测试异常: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}