package com.tour.app;

import android.app.Application;
import com.tour.app.di.AppComponent;
import com.tour.app.di.DaggerAppComponent;

/**
 * 自定义Application类，用于初始化Dagger依赖注入框架
 * 在应用启动时创建AppComponent实例，提供全局依赖注入功能
 */
public class App extends Application {
    
    private AppComponent appComponent;
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        // 初始化Dagger组件，创建依赖注入容器
        appComponent = DaggerAppComponent.create();
    }
    
    /**
     * 获取AppComponent实例，供应用其他部分使用依赖注入功能
     * @return AppComponent依赖注入组件实例
     */
    public AppComponent getAppComponent() {
        return appComponent;
    }
} 