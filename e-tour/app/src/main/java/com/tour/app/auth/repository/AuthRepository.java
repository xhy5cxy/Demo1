package com.tour.app.auth.repository;

import com.tour.app.network.AuthService;
import com.tour.app.network.LoginRequest;
import com.tour.app.network.RegisterRequest;
import com.tour.app.network.ChangePasswordRequest;
import com.tour.app.network.ApiResponse;
import com.tour.app.model.User;
import com.tour.app.network.ApiClient;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.Retrofit;
import android.content.Context;
import java.util.concurrent.TimeUnit;

public class AuthRepository {
    private final AuthService service;
    
    public AuthRepository(Retrofit retrofit) {
        if (retrofit != null) {
            this.service = retrofit.create(AuthService.class);
        } else {
            // 默认使用ApiClient获取服务
            this.service = null;
        }
    }
    
    public AuthRepository(Context context) {
        this.service = ApiClient.getAuthService(context);
    }
    
    public AuthRepository(AuthService service) {
        this.service = service;
    }
    
    /**
     * 用户登录
     */
    public Call<ApiResponse<String>> login(String username, String password) {
        if (service == null) {
            throw new IllegalStateException("AuthService未初始化，请确保使用Context构造函数");
        }
        LoginRequest request = new LoginRequest(username, password);
        return service.login(request);
    }
    
    /**
     * 用户注册
     */
    public Call<ApiResponse<String>> register(String username, String password, 
                                          String phone, String email, String nickname) {
        if (service == null) {
            throw new IllegalStateException("AuthService未初始化，请确保使用Context构造函数");
        }
        RegisterRequest request = new RegisterRequest(username, password, phone, email, nickname);
        return service.register(request);
    }
    
    /**
     * 修改密码
     */
    public Call<ApiResponse<String>> changePassword(Long userId, String oldPassword, String newPassword) {
        if (service == null) {
            throw new IllegalStateException("AuthService未初始化，请确保使用Context构造函数");
        }
        ChangePasswordRequest request = new ChangePasswordRequest(userId, oldPassword, newPassword);
        return service.changePassword(request);
    }
    
    /**
     * 获取用户信息
     */
    public Call<ApiResponse<User>> getUserById(Long id) {
        if (service == null) {
            throw new IllegalStateException("AuthService未初始化，请确保使用Context构造函数");
        }
        return service.getUserById(id);
    }
    
    /**
     * 模拟登录（用于测试）
     */
    public Single<Boolean> simulateLogin(String username, String password) {
        // 模拟登录逻辑
        return Single.just(true).delay(1, TimeUnit.SECONDS);
    }
}