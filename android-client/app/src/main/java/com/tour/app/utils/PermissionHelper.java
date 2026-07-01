package com.tour.app.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限管理工具类
 * 用于处理动态权限请求
 */
public class PermissionHelper {
    
    // 定位相关权限
    public static final String[] LOCATION_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    
    // 存储相关权限
    public static final String[] STORAGE_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    
    // 网络相关权限
    public static final String[] NETWORK_PERMISSIONS = {
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE
    };
    
    /**
     * 检查权限是否已授予
     */
    public static boolean hasPermissions(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 请求权限
     */
    public static void requestPermissions(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }
    
    /**
     * 检查是否需要显示权限说明
     */
    public static boolean shouldShowRequestPermissionRationale(Activity activity, String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 获取未授予的权限列表
     */
    public static String[] getDeniedPermissions(Context context, String[] permissions) {
        List<String> deniedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permission);
            }
        }
        return deniedPermissions.toArray(new String[0]);
    }
    
    /**
     * 检查定位权限是否已授予
     */
    public static boolean hasLocationPermissions(Context context) {
        return hasPermissions(context, LOCATION_PERMISSIONS);
    }
    
    /**
     * 请求定位权限
     */
    public static void requestLocationPermissions(Activity activity, int requestCode) {
        requestPermissions(activity, LOCATION_PERMISSIONS, requestCode);
    }
    
    /**
     * 检查存储权限是否已授予
     */
    public static boolean hasStoragePermissions(Context context) {
        return hasPermissions(context, STORAGE_PERMISSIONS);
    }
    
    /**
     * 请求存储权限
     */
    public static void requestStoragePermissions(Activity activity, int requestCode) {
        requestPermissions(activity, STORAGE_PERMISSIONS, requestCode);
    }
    
    /**
     * 检查网络权限是否已授予
     */
    public static boolean hasNetworkPermissions(Context context) {
        return hasPermissions(context, NETWORK_PERMISSIONS);
    }
    
    /**
     * 请求网络权限
     */
    public static void requestNetworkPermissions(Activity activity, int requestCode) {
        requestPermissions(activity, NETWORK_PERMISSIONS, requestCode);
    }
    
    /**
     * 检查所有高德地图所需权限是否已授予
     */
    public static boolean hasAmapPermissions(Context context) {
        return hasLocationPermissions(context) && 
               hasStoragePermissions(context) && 
               hasNetworkPermissions(context);
    }
    
    /**
     * 请求所有高德地图所需权限
     */
    public static void requestAmapPermissions(Activity activity, int requestCode) {
        String[] allPermissions = combinePermissions(LOCATION_PERMISSIONS, STORAGE_PERMISSIONS, NETWORK_PERMISSIONS);
        requestPermissions(activity, allPermissions, requestCode);
    }
    
    /**
     * 合并权限数组
     */
    private static String[] combinePermissions(String[]... permissionArrays) {
        List<String> allPermissions = new ArrayList<>();
        for (String[] permissions : permissionArrays) {
            for (String permission : permissions) {
                if (!allPermissions.contains(permission)) {
                    allPermissions.add(permission);
                }
            }
        }
        return allPermissions.toArray(new String[0]);
    }
}