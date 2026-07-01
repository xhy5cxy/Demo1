package com.tour.app.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import com.tour.app.R;
import com.tour.app.community.ui.CommunityActivity;
import com.tour.app.data.StrategyDataManager;
import com.tour.app.model.Strategy;
import com.tour.app.model.User;
import com.tour.app.strategy.CreateStrategyActivity;

import java.util.List;

/**
 * 攻略功能测试Activity
 * 用于测试用户创建攻略并在攻略广场显示的功能
 */
public class StrategyTestActivity extends Activity {
    
    private StrategyDataManager dataManager;
    private TextView testResultView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strategy_test);
        
        dataManager = StrategyDataManager.getInstance(this);
        testResultView = findViewById(R.id.test_result_view);
        
        setupTestButtons();
        updateTestResult();
    }
    
    private void setupTestButtons() {
        Button btnCreateTestStrategy = findViewById(R.id.btn_create_test_strategy);
        Button btnViewStrategies = findViewById(R.id.btn_view_strategies);
        Button btnClearData = findViewById(R.id.btn_clear_data);
        Button btnGoToCommunity = findViewById(R.id.btn_go_to_community);
        
        btnCreateTestStrategy.setOnClickListener(v -> createTestStrategy());
        btnViewStrategies.setOnClickListener(v -> viewStrategies());
        btnClearData.setOnClickListener(v -> clearData());
        btnGoToCommunity.setOnClickListener(v -> goToCommunity());
    }
    
    /**
     * 创建测试攻略
     */
    private void createTestStrategy() {
        try {
            // 创建测试用户
            User testUser = new User();
            testUser.setId(1001L);
            testUser.setUsername("测试用户");
            testUser.setAvatar("https://example.com/avatar.jpg");
            
            // 创建测试攻略
            Strategy testStrategy = new Strategy();
            testStrategy.setTitle("测试攻略 - " + System.currentTimeMillis());
            testStrategy.setContent("这是一个自动生成的测试攻略内容，用于验证攻略创建和显示功能。");
            testStrategy.setDestination("测试目的地");
            testStrategy.setCoverImage("https://example.com/cover.jpg");
            testStrategy.setUserId(testUser.getId());
            testStrategy.setAuthor(testUser);
            
            // 设置默认数据
            testStrategy.setViewCount(0);
            testStrategy.setLikeCount(0);
            testStrategy.setStatus(1); // 已通过状态
            testStrategy.setCreateTime(java.time.LocalDateTime.now());
            testStrategy.setUpdateTime(java.time.LocalDateTime.now());
            
            // 保存攻略
            dataManager.saveStrategy(testStrategy);
            
            testResultView.setText("✅ 测试攻略创建成功！\n" +
                    "标题: " + testStrategy.getTitle() + "\n" +
                    "当前攻略总数: " + dataManager.getStrategyCount());
            
        } catch (Exception e) {
            testResultView.setText("❌ 创建测试攻略失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 查看当前存储的攻略
     */
    private void viewStrategies() {
        try {
            List<Strategy> strategies = dataManager.getStrategies();
            StringBuilder result = new StringBuilder();
            result.append("📋 当前存储的攻略列表:\n\n");
            
            if (strategies.isEmpty()) {
                result.append("暂无攻略数据");
            } else {
                for (int i = 0; i < strategies.size(); i++) {
                    Strategy strategy = strategies.get(i);
                    result.append("攻略 ").append(i + 1).append(":\n");
                    result.append("  标题: ").append(strategy.getTitle()).append("\n");
                    result.append("  作者: ").append(strategy.getAuthor() != null ? strategy.getAuthor().getUsername() : "未知").append("\n");
                    result.append("  状态: ").append(getStatusText(strategy.getStatus())).append("\n");
                    result.append("  点赞: ").append(strategy.getLikeCount()).append("\n");
                    result.append("  浏览: ").append(strategy.getViewCount()).append("\n\n");
                }
            }
            
            testResultView.setText(result.toString());
            
        } catch (Exception e) {
            testResultView.setText("❌ 查看攻略列表失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 清空攻略数据
     */
    private void clearData() {
        try {
            dataManager.clearAllStrategies();
            testResultView.setText("🗑️ 攻略数据已清空");
        } catch (Exception e) {
            testResultView.setText("❌ 清空数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 跳转到攻略广场
     */
    private void goToCommunity() {
        try {
            Intent intent = new Intent(this, CommunityActivity.class);
            startActivity(intent);
            testResultView.setText("🚀 正在跳转到攻略广场...");
        } catch (Exception e) {
            testResultView.setText("❌ 跳转失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 更新测试结果显示
     */
    private void updateTestResult() {
        int count = dataManager.getStrategyCount();
        testResultView.setText("📊 当前攻略总数: " + count);
    }
    
    /**
     * 获取状态文本
     */
    private String getStatusText(int status) {
        switch (status) {
            case 0: return "待审核";
            case 1: return "已通过";
            case 2: return "已驳回";
            default: return "未知";
        }
    }
}