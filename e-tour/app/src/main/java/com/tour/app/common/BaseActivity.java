package com.tour.app.common;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.tour.app.R;

/**
 * 基础Activity类
 * 提供底部导航栏功能，其他Activity可以继承此类
 */
public abstract class BaseActivity extends AppCompatActivity {
    
    protected BottomNavigationManager bottomNavigationManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        
        // 先初始化视图和数据，确保主要内容快速显示
        initViews();
        initData();
        
        // 延迟初始化底部导航栏，减少白屏时间
        getWindow().getDecorView().post(() -> {
            initBottomNavigation();
        });
    }
    
    /**
     * 获取布局ID
     * @return 布局资源ID
     */
    protected abstract int getLayoutId();
    
    /**
     * 初始化视图
     */
    protected abstract void initViews();
    
    /**
     * 初始化数据
     */
    protected abstract void initData();
    
    /**
     * 初始化底部导航栏
     */
    private void initBottomNavigation() {
        try {
            // 首先检查布局中是否包含底部导航栏
            View bottomNavView = findViewById(R.id.bottom_nav);
            if (bottomNavView == null) {
                // 如果没有底部导航栏，直接返回，不初始化管理器
                android.util.Log.d("BaseActivity", "No bottom navigation found in layout, skipping initialization");
                return;
            }
            
            // 使用当前Activity的根布局来查找底部导航栏
            // 直接使用当前Activity的content view，避免复杂的视图查找
            View contentView = findViewById(android.R.id.content);
            if (contentView != null) {
                bottomNavigationManager = new BottomNavigationManager(this, contentView);
                
                // 设置当前选中的导航项
                setCurrentNavigationItem();
            } else {
                // 如果找不到content view，记录错误
                android.util.Log.e("BaseActivity", "Failed to find content view for bottom navigation");
            }
        } catch (Exception e) {
            // 如果底部导航栏初始化失败，不影响主功能
            android.util.Log.e("BaseActivity", "Error initializing bottom navigation", e);
        }
    }
    
    /**
     * 设置当前选中的导航项
     * 子类可以重写此方法来设置不同的选中状态
     */
    protected void setCurrentNavigationItem() {
        // 默认不选中任何项
    }
    
    /**
     * 获取底部导航栏管理器
     * @return BottomNavigationManager实例
     */
    public BottomNavigationManager getBottomNavigationManager() {
        return bottomNavigationManager;
    }
    
    /**
     * 设置底部导航栏可见性
     * @param visible 是否可见
     */
    protected void setBottomNavigationVisible(boolean visible) {
        if (bottomNavigationManager != null) {
            bottomNavigationManager.setBottomNavigationVisible(visible);
        }
    }
}