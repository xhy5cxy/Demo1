package com.tour.app;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.tour.app.common.BaseActivity;
import com.tour.app.auth.ui.LoginActivity;
import com.tour.app.utils.AmapNavigationHelper;

public class MainActivity extends BaseActivity {

    private LinearLayout btnTravel, btnExplore, btnAdd, btnAi, btnMe;
    private TextView tvTravel, tvExplore, tvAi, tvMe;
    // 高德地图导航按钮
    private android.widget.Button btnAmapNavigation;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        // 立即初始化主要视图
        try {
            // 初始化底部导航按钮
            btnTravel = findViewById(R.id.btn_travel);
            btnExplore = findViewById(R.id.btn_explore);
            btnAdd = findViewById(R.id.btn_add);
            btnAi = findViewById(R.id.btn_ai);
            btnMe = findViewById(R.id.btn_me);
            
            tvTravel = findViewById(R.id.tv_travel);
            tvExplore = findViewById(R.id.tv_explore);
            tvAi = findViewById(R.id.tv_ai);
            tvMe = findViewById(R.id.tv_me);
            
            // 初始化新的智能导航按钮
            android.widget.Button smartNavButton = findViewById(R.id.btn_smart_navigation);
            
            if (smartNavButton != null) {
                // 调试日志：按钮初始化成功
                android.util.Log.d("MainActivity", "智能导航按钮初始化成功 - ID: " + R.id.btn_smart_navigation);
                
                // 测试按钮是否可见和可点击
                android.util.Log.d("MainActivity", "按钮可见性: " + smartNavButton.getVisibility());
                android.util.Log.d("MainActivity", "按钮可点击: " + smartNavButton.isClickable());
                android.util.Log.d("MainActivity", "按钮启用状态: " + smartNavButton.isEnabled());
                
                // 设置简单的点击监听器进行测试
                smartNavButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 调试日志：按钮被点击
                        android.util.Log.d("MainActivity", "行程导航按钮被点击");
                        
                        // 显示点击反馈
                        v.setPressed(true);
                        v.postDelayed(() -> v.setPressed(false), 100);
                        
                        // 显示简单的Toast提示
                        android.widget.Toast.makeText(MainActivity.this, "🚀 智能导航按钮被点击！", android.widget.Toast.LENGTH_LONG).show();
                        
                        // 测试导航服务
                        try {
                            android.util.Log.d("MainActivity", "启动MCP导航服务");
                            com.tour.app.navigation.MCPNavigationService mcpService = new com.tour.app.navigation.MCPNavigationService(MainActivity.this);
                            mcpService.startQuickNavigation();
                        } catch (Exception e) {
                            android.util.Log.e("MainActivity", "MCP导航服务启动失败: " + e.getMessage());
                            android.widget.Toast.makeText(MainActivity.this, "导航服务启动失败: " + e.getMessage(), android.widget.Toast.LENGTH_LONG).show();
                        }
                    }
                });
                
                // 确保按钮可点击状态
                smartNavButton.setEnabled(true);
                smartNavButton.setClickable(true);
                smartNavButton.setLongClickable(true);
                android.util.Log.d("MainActivity", "按钮设置为可点击状态");
            } else {
                // 调试日志：按钮初始化失败
                android.util.Log.e("MainActivity", "行程导航按钮初始化失败，按钮为null");
                android.widget.Toast.makeText(this, "导航按钮初始化失败", android.widget.Toast.LENGTH_LONG).show();
            }
            
            // 只设置加号按钮的点击监听器，其他按钮由BottomNavigationManager处理
            if (btnAdd != null) btnAdd.setOnClickListener(this::onAddClick);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initData() {
        // 检查登录状态（延迟检查，确保按钮能正常工作）
        getWindow().getDecorView().post(() -> {
            if (!isLoggedIn()) {
                redirectToLogin();
                return;
            }
            // 初始化数据
        });
    }

    @Override
    protected void setCurrentNavigationItem() {
        // 设置旅行按钮为选中状态
        if (bottomNavigationManager != null) {
            bottomNavigationManager.setSelectedItem(R.id.btn_travel);
        }
    }

    // 导航按钮点击事件由BottomNavigationManager统一处理
    // 这里不再需要重复处理

    private void onAddClick(View view) {
        // 直接跳转到创建行程页面
        Intent intent = new Intent(this, com.tour.app.createtrip.CreateTripActivity.class);
        startActivity(intent);
    }
    

    
    private boolean isLoggedIn() {
        return com.tour.app.utils.AuthManager.getInstance(this).isLoggedIn();
    }
    
    private void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    

    
    /**
     * 权限请求结果处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, 
                                          @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == 1001) { // 定位权限请求
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            
            if (allGranted) {
                // 权限已授予，启动MCP智能导航服务
                android.widget.Toast.makeText(this, "定位权限已授予，启动MCP智能导航", android.widget.Toast.LENGTH_SHORT).show();
                try {
                    com.tour.app.navigation.MCPNavigationService mcpService = new com.tour.app.navigation.MCPNavigationService(this);
                    mcpService.startQuickNavigation();
                } catch (Exception e) {
                    android.widget.Toast.makeText(this, "MCP导航服务启动失败", android.widget.Toast.LENGTH_SHORT).show();
                }
            } else {
                // 权限被拒绝
                android.widget.Toast.makeText(this, "定位权限被拒绝，无法使用MCP导航功能", android.widget.Toast.LENGTH_SHORT).show();
            }
        }
    }
}