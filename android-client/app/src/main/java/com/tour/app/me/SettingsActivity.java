package com.tour.app.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tour.app.R;
import com.tour.app.TestConnectionActivity;
import com.tour.app.auth.ui.AuthTestActivity;
import com.tour.app.test.StrategyTestActivity;

/**
 * 设置页面Activity
 * 包含各种测试功能按钮
 */
public class SettingsActivity extends AppCompatActivity {

    private ImageView ivBack;
    private TextView tvTitle;
    
    // 测试功能按钮
    private LinearLayout btnTestConnection;
    private LinearLayout btnTestLogin;
    private LinearLayout btnTestRegister;
    private LinearLayout btnTestSpots;
    private LinearLayout btnTestNetwork;
    private LinearLayout btnTestStrategy;
    
    // 应用设置按钮
    private LinearLayout btnAbout;
    private LinearLayout btnFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        initViews();
        initListeners();
    }

    /**
     * 初始化视图组件
     */
    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        
        // 测试功能按钮
        btnTestConnection = findViewById(R.id.btn_test_connection);
        btnTestLogin = findViewById(R.id.btn_test_login);
        btnTestRegister = findViewById(R.id.btn_test_register);
        btnTestSpots = findViewById(R.id.btn_test_spots);
        btnTestNetwork = findViewById(R.id.btn_test_network);
        btnTestStrategy = findViewById(R.id.btn_test_strategy);
        
        // 应用设置按钮
        btnAbout = findViewById(R.id.btn_about);
        btnFeedback = findViewById(R.id.btn_feedback);
        
        // 设置标题
        tvTitle.setText("设置");
    }

    /**
     * 初始化点击事件监听器
     */
    private void initListeners() {
        // 返回按钮点击事件
        ivBack.setOnClickListener(v -> finish());
        
        // 测试功能按钮点击事件
        btnTestConnection.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, TestConnectionActivity.class);
            startActivity(intent);
        });
        
        btnTestLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, AuthTestActivity.class);
            startActivity(intent);
        });
        
        btnTestRegister.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, AuthTestActivity.class);
            startActivity(intent);
        });
        
        btnTestSpots.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, TestConnectionActivity.class);
            startActivity(intent);
        });
        
        btnTestNetwork.setOnClickListener(v -> {
            // 网络测试功能 - 可以跳转到网络测试页面或直接执行测试
            showToast("网络测试功能");
        });
        
        btnTestStrategy.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, StrategyTestActivity.class);
            startActivity(intent);
        });
        
        // 应用设置按钮点击事件
        btnAbout.setOnClickListener(v -> {
            showToast("关于应用");
        });
        
        btnFeedback.setOnClickListener(v -> {
            showToast("意见反馈");
        });
    }

    /**
     * 显示Toast消息
     */
    private void showToast(String message) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show();
    }
}