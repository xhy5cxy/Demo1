package com.tour.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.tour.app.model.User;
import com.google.gson.Gson;

/**
 * 认证管理工具类
 * 用于管理用户登录状态和token
 */
public class AuthManager {
    private static final String PREF_NAME = "auth_prefs";
    private static final String KEY_TOKEN = "auth_token";
    private static final String KEY_USER = "current_user";
    
    private static AuthManager instance;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    
    private String authToken;
    private User currentUser;
    
    private AuthManager(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        loadFromPrefs();
    }
    
    public static synchronized AuthManager getInstance(Context context) {
        if (instance == null) {
            instance = new AuthManager(context);
        }
        return instance;
    }
    
    /**
     * 保存登录信息
     */
    public void saveAuthInfo(String token, User user) {
        this.authToken = token;
        this.currentUser = user;
        
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_USER, gson.toJson(user));
        editor.apply();
    }
    
    /**
     * 清除登录信息
     */
    public void clearAuthInfo() {
        authToken = null;
        currentUser = null;
        
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_TOKEN);
        editor.remove(KEY_USER);
        editor.apply();
    }
    
    /**
     * 从本地加载登录信息
     */
    private void loadFromPrefs() {
        authToken = sharedPreferences.getString(KEY_TOKEN, null);
        String userJson = sharedPreferences.getString(KEY_USER, null);
        if (userJson != null) {
            currentUser = gson.fromJson(userJson, User.class);
        }
    }
    
    /**
     * 是否已登录
     */
    public boolean isLoggedIn() {
        return authToken != null && currentUser != null;
    }
    
    /**
     * 获取认证token
     */
    public String getAuthToken() {
        return authToken;
    }
    
    /**
     * 获取当前用户
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * 更新用户信息
     */
    public void updateUser(User user) {
        this.currentUser = user;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER, gson.toJson(user));
        editor.apply();
    }
}