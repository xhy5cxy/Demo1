package com.tour.app.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tour.app.model.Strategy;
import com.tour.app.model.User;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 攻略数据管理器
 * 负责用户创建攻略的本地存储和读取
 */
public class StrategyDataManager {
    
    private static final String TAG = "StrategyDataManager";
    private static final String PREF_NAME = "strategy_data";
    private static final String KEY_STRATEGIES = "user_strategies";
    
    private static StrategyDataManager instance;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    
    private StrategyDataManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }
    
    public static synchronized StrategyDataManager getInstance(Context context) {
        if (instance == null) {
            instance = new StrategyDataManager(context);
        }
        return instance;
    }
    
    /**
     * 保存用户创建的攻略
     */
    public void saveStrategy(Strategy strategy) {
        try {
            // 获取现有的攻略列表
            List<Strategy> strategies = getStrategies();
            
            // 设置创建时间
            strategy.setCreateTime(LocalDateTime.now());
            strategy.setUpdateTime(LocalDateTime.now());
            
            // 设置默认状态为已通过（1）
            if (strategy.getStatus() == null) {
                strategy.setStatus(1);
            }
            
            // 设置默认的浏览量和点赞数
            if (strategy.getViewCount() == null) {
                strategy.setViewCount(0);
            }
            if (strategy.getLikeCount() == null) {
                strategy.setLikeCount(0);
            }
            if (strategy.getCollectCount() == null) {
                strategy.setCollectCount(0);
            }
            
            // 添加到列表开头（最新攻略显示在最前面）
            strategies.add(0, strategy);
            
            // 保存到SharedPreferences
            String strategiesJson = gson.toJson(strategies);
            sharedPreferences.edit().putString(KEY_STRATEGIES, strategiesJson).apply();
            
            Log.d(TAG, "攻略保存成功: " + strategy.getTitle());
        } catch (Exception e) {
            Log.e(TAG, "保存攻略失败", e);
        }
    }
    
    /**
     * 获取所有用户创建的攻略
     */
    public List<Strategy> getStrategies() {
        try {
            String strategiesJson = sharedPreferences.getString(KEY_STRATEGIES, "[]");
            Type listType = new TypeToken<List<Strategy>>(){}.getType();
            List<Strategy> strategies = gson.fromJson(strategiesJson, listType);
            
            if (strategies == null) {
                strategies = new ArrayList<>();
            }
            
            return strategies;
        } catch (Exception e) {
            Log.e(TAG, "获取攻略列表失败", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 清空所有攻略数据（用于测试）
     */
    public void clearAllStrategies() {
        sharedPreferences.edit().remove(KEY_STRATEGIES).apply();
        Log.d(TAG, "已清空所有攻略数据");
    }
    
    /**
     * 获取攻略数量
     */
    public int getStrategyCount() {
        return getStrategies().size();
    }
}