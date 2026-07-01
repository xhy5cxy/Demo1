package com.tour.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tour.app.network.ApiClient;
import com.tour.app.network.ApiResponse;
import com.tour.app.network.AuthService;
import com.tour.app.network.SpotService;
import com.tour.app.model.User;
import com.tour.app.model.Spot;
import com.tour.app.utils.AuthManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 测试前后端连接的活动
 * 用于验证API连接是否正常
 */
public class TestConnectionActivity extends AppCompatActivity {
    
    private static final String TAG = "TestConnectionActivity";
    
    private TextView tvResult;
    private Button btnTestConnection;
    private Button btnTestLogin;
    private Button btnTestSpots;
    private Button btnGoToMain;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_connection);
        
        initViews();
        initListeners();
    }
    
    private void initViews() {
        try {
            tvResult = findViewById(R.id.tv_result);
            btnTestConnection = findViewById(R.id.btn_test_connection);
            btnTestLogin = findViewById(R.id.btn_test_login);
            btnTestSpots = findViewById(R.id.btn_test_spots);
            btnGoToMain = findViewById(R.id.btn_go_to_main);
            
            // 检查视图是否正确初始化
            if (tvResult == null || btnTestConnection == null || 
                btnTestLogin == null || btnTestSpots == null || btnGoToMain == null) {
                throw new IllegalStateException("关键视图未正确初始化");
            }
        } catch (Exception e) {
            Log.e(TAG, "视图初始化失败", e);
            Toast.makeText(this, "界面初始化失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void initListeners() {
        // 测试连接
        btnTestConnection.setOnClickListener(v -> testConnection());
        
        // 测试登录
        btnTestLogin.setOnClickListener(v -> testLogin());
        
        // 测试获取景点
        btnTestSpots.setOnClickListener(v -> testSpots());
        
        // 跳转到主页面
        btnGoToMain.setOnClickListener(v -> {
            Intent intent = new Intent(TestConnectionActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
    
    /**
     * 测试基础连接
     */
    private void testConnection() {
        if (tvResult == null) {
            Log.e(TAG, "TextView未初始化");
            return;
        }
        
        tvResult.setText("正在测试连接...");
        
        try {
            SpotService spotService = ApiClient.getSpotService(this);
            
            spotService.getSpots(1, 5, null, null).enqueue(new Callback<ApiResponse<List<Spot>>>() {
                @Override
                public void onResponse(Call<ApiResponse<List<Spot>>> call, Response<ApiResponse<List<Spot>>> response) {
                    if (!isFinishing() && !isDestroyed()) {
                        if (response.isSuccessful() && response.body() != null) {
                            ApiResponse<List<Spot>> apiResponse = response.body();
                            if (apiResponse.isSuccess()) {
                                List<Spot> spots = apiResponse.getData();
                                tvResult.setText("连接成功！\n" + 
                                               "状态码: " + response.code() + "\n" +
                                               "获取到景点数量: " + (spots != null ? spots.size() : 0));
                            } else {
                                tvResult.setText("连接失败: " + apiResponse.getMessage());
                            }
                        } else {
                            tvResult.setText("连接失败，状态码: " + response.code());
                        }
                    }
                }
                
                @Override
                public void onFailure(Call<ApiResponse<List<Spot>>> call, Throwable t) {
                    if (!isFinishing() && !isDestroyed()) {
                        String errorMsg = t.getMessage() != null ? t.getMessage() : "未知网络错误";
                        tvResult.setText("连接失败: " + errorMsg);
                        Log.e(TAG, "连接失败", t);
                        Toast.makeText(TestConnectionActivity.this, "网络连接失败，请检查网络设置", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            
        } catch (Exception e) {
            if (!isFinishing() && !isDestroyed()) {
                tvResult.setText("测试连接时发生异常: " + e.getMessage());
                Log.e(TAG, "测试连接异常", e);
                Toast.makeText(this, "测试连接异常: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    /**
     * 测试登录功能
     */
    private void testLogin() {
        if (tvResult == null) {
            Log.e(TAG, "TextView未初始化");
            return;
        }
        
        tvResult.setText("正在测试登录...");
        
        try {
            AuthService authService = ApiClient.getAuthService(this);
            
            // 使用测试账号
            String username = "testuser";
            String password = "password";
            
            authService.login(new com.tour.app.network.LoginRequest(username, password))
                    .enqueue(new Callback<ApiResponse<String>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                            if (!isFinishing() && !isDestroyed()) {
                                if (response.isSuccessful() && response.body() != null) {
                                    ApiResponse<String> apiResponse = response.body();
                                    if (apiResponse.isSuccess()) {
                                        String token = apiResponse.getData();
                                        if (token != null && !token.isEmpty()) {
                                            tvResult.setText("登录成功！\n" + 
                                                           "Token: " + token.substring(0, Math.min(20, token.length())) + "...");
                                            
                                            // 创建用户对象用于AuthManager
                                            User user = new User();
                                            user.setUsername(username);
                                            user.setId(1L); // 默认ID
                                            
                                            // 保存登录信息
                                            AuthManager.getInstance(TestConnectionActivity.this)
                                                    .saveAuthInfo(token, user);
                                        } else {
                                            tvResult.setText("登录失败: token为空");
                                        }
                                    } else {
                                        tvResult.setText("登录失败: " + apiResponse.getMessage());
                                    }
                                } else {
                                    tvResult.setText("登录失败，状态码: " + response.code());
                                }
                            }
                        }
                        
                        @Override
                        public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                            if (!isFinishing() && !isDestroyed()) {
                                String errorMsg = t.getMessage() != null ? t.getMessage() : "未知网络错误";
                                tvResult.setText("登录失败: " + errorMsg);
                                Log.e(TAG, "登录失败", t);
                                Toast.makeText(TestConnectionActivity.this, "登录测试失败，请检查网络", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    
        } catch (Exception e) {
            if (!isFinishing() && !isDestroyed()) {
                tvResult.setText("测试登录时发生异常: " + e.getMessage());
                Log.e(TAG, "测试登录异常", e);
                Toast.makeText(this, "测试登录异常: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    /**
     * 测试获取景点列表
     */
    private void testSpots() {
        if (tvResult == null) {
            Log.e(TAG, "TextView未初始化");
            return;
        }
        
        tvResult.setText("正在获取景点列表...");
        
        try {
            SpotService spotService = ApiClient.getSpotService(this);
            
            spotService.getSpots(1, 10, "北京", 4.0).enqueue(new Callback<ApiResponse<List<Spot>>>() {
                @Override
                public void onResponse(Call<ApiResponse<List<Spot>>> call, Response<ApiResponse<List<Spot>>> response) {
                    if (!isFinishing() && !isDestroyed()) {
                        if (response.isSuccessful() && response.body() != null) {
                            ApiResponse<List<Spot>> apiResponse = response.body();
                            if (apiResponse.isSuccess()) {
                                List<Spot> spots = apiResponse.getData();
                                StringBuilder result = new StringBuilder("获取景点成功！\n");
                                if (spots != null && !spots.isEmpty()) {
                                    result.append("景点数量: ").append(spots.size()).append("\n\n");
                                    for (int i = 0; i < Math.min(3, spots.size()); i++) {
                                        Spot spot = spots.get(i);
                                        if (spot != null) {
                                            result.append(i + 1).append(". ")
                                                  .append(spot.getName() != null ? spot.getName() : "未知景点")
                                                  .append(" (评分: ")
                                                  .append(spot.getRating() != null ? spot.getRating() : "N/A")
                                                  .append(")\n");
                                        }
                                    }
                                } else {
                                    result.append("未找到符合条件的景点");
                                }
                                tvResult.setText(result.toString());
                            } else {
                                String errorMsg = apiResponse.getMessage() != null ? apiResponse.getMessage() : "未知错误";
                                tvResult.setText("获取景点失败: " + errorMsg);
                            }
                        } else {
                            tvResult.setText("获取景点失败，状态码: " + response.code());
                        }
                    }
                }
                
                @Override
                public void onFailure(Call<ApiResponse<List<Spot>>> call, Throwable t) {
                    if (!isFinishing() && !isDestroyed()) {
                        String errorMsg = t.getMessage() != null ? t.getMessage() : "未知网络错误";
                        tvResult.setText("获取景点失败: " + errorMsg);
                        Log.e(TAG, "获取景点失败", t);
                        Toast.makeText(TestConnectionActivity.this, "获取景点失败，请检查网络", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            
        } catch (Exception e) {
            if (!isFinishing() && !isDestroyed()) {
                tvResult.setText("测试获取景点时发生异常: " + e.getMessage());
                Log.e(TAG, "测试获取景点异常", e);
                Toast.makeText(this, "测试获取景点异常: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}