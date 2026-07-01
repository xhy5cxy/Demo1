package com.tour.app.utils;

import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * JWT工具类
 * 用于解析JWT token中的用户信息
 */
public class JWTUtils {
    
    private static final String TAG = "JWTUtils";
    
    /**
     * 从JWT token中解析用户ID
     * @param token JWT token
     * @return 用户ID，如果解析失败返回null
     */
    public static Long getUserIdFromToken(String token) {
        try {
            if (token == null || token.isEmpty()) {
                return null;
            }
            
            // JWT token格式：header.payload.signature
            String[] parts = token.split("\\.");
            if (parts.length < 2) {
                Log.e(TAG, "Invalid JWT token format");
                return null;
            }
            
            // 解码payload部分
            String payload = parts[1];
            String decodedPayload = new String(Base64.decode(payload, Base64.URL_SAFE));
            
            // 解析JSON
            JSONObject jsonObject = new JSONObject(decodedPayload);
            
            // 尝试获取用户ID（根据你的JWT结构）
            if (jsonObject.has("userId")) {
                return jsonObject.getLong("userId");
            } else if (jsonObject.has("id")) {
                return jsonObject.getLong("id");
            } else if (jsonObject.has("sub")) {
                String sub = jsonObject.getString("sub");
                try {
                    return Long.parseLong(sub);
                } catch (NumberFormatException e) {
                    Log.e(TAG, "Cannot parse user ID from sub claim: " + sub);
                    return null;
                }
            }
            
            Log.e(TAG, "No user ID found in JWT token");
            return null;
            
        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse JWT token", e);
            return null;
        } catch (Exception e) {
            Log.e(TAG, "Unexpected error parsing JWT token", e);
            return null;
        }
    }
    
    /**
     * 从JWT token中解析用户名
     * @param token JWT token
     * @return 用户名，如果解析失败返回null
     */
    public static String getUsernameFromToken(String token) {
        try {
            if (token == null || token.isEmpty()) {
                return null;
            }
            
            String[] parts = token.split("\\.");
            if (parts.length < 2) {
                Log.e(TAG, "Invalid JWT token format");
                return null;
            }
            
            String payload = parts[1];
            String decodedPayload = new String(Base64.decode(payload, Base64.URL_SAFE));
            
            JSONObject jsonObject = new JSONObject(decodedPayload);
            
            // 尝试获取用户名
            if (jsonObject.has("username")) {
                return jsonObject.getString("username");
            } else if (jsonObject.has("phone")) {
                return jsonObject.getString("phone");
            }
            
            Log.e(TAG, "No username found in JWT token");
            return null;
            
        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse JWT token", e);
            return null;
        } catch (Exception e) {
            Log.e(TAG, "Unexpected error parsing JWT token", e);
            return null;
        }
    }
}