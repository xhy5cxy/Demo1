package com.tour.app.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * 高德地图导航工具类
 * 提供高德地图导航相关的功能封装
 */
public class AmapNavigationHelper {
    
    private static final String TAG = "AmapNavigationHelper";
    
    /**
     * 检查高德地图是否已安装
     * @param context 上下文
     * @return 是否已安装
     */
    public static boolean isAmapInstalled(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("amapuri://"));
            intent.setPackage(AmapConfig.AMAP_PACKAGE_NAME);
            return intent.resolveActivity(context.getPackageManager()) != null;
        } catch (Exception e) {
            android.util.Log.e(TAG, "检查高德地图安装状态失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 启动高德地图导航
     * @param context 上下文
     * @param destLat 目的地纬度
     * @param destLng 目的地经度
     * @param destName 目的地名称
     * @param navType 导航类型
     * @return 是否成功启动
     */
    public static boolean startNavigation(Context context, double destLat, double destLng, 
                                        String destName, int navType) {
        try {
            // 检查高德地图是否安装
            if (!isAmapInstalled(context)) {
                android.util.Log.e(TAG, "高德地图未安装");
                Toast.makeText(context, "请先安装高德地图应用", Toast.LENGTH_LONG).show();
                return false;
            }
            
            // 检查定位权限
            if (!PermissionHelper.hasLocationPermissions(context)) {
                android.util.Log.d(TAG, "需要定位权限，请先授予权限");
                Toast.makeText(context, "需要定位权限才能使用导航功能", Toast.LENGTH_LONG).show();
                return false;
            }
            
            // 构建导航URI
            String navigationUri = AmapConfig.getNavigationUri(destLat, destLng, destName, navType);
            android.util.Log.d(TAG, "导航URI: " + navigationUri);
            
            // 创建导航Intent
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(navigationUri));
            intent.setPackage(AmapConfig.AMAP_PACKAGE_NAME);
            
            // 添加启动标志
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            
            // 启动导航
            context.startActivity(intent);
            
            android.util.Log.d(TAG, "高德地图导航启动成功");
            Toast.makeText(context, "正在启动高德地图导航...", Toast.LENGTH_SHORT).show();
            return true;
            
        } catch (Exception e) {
            android.util.Log.e(TAG, "启动高德地图导航失败: " + e.getMessage());
            Toast.makeText(context, "导航功能暂不可用，请稍后重试", Toast.LENGTH_LONG).show();
            return false;
        }
    }
    
    /**
     * 启动默认导航（驾车到天安门）
     * @param context 上下文
     * @return 是否成功启动
     */
    public static boolean startDefaultNavigation(Context context) {
        return startNavigation(context, 
            AmapConfig.Navigation.DEFAULT_DEST_LAT,
            AmapConfig.Navigation.DEFAULT_DEST_LNG,
            AmapConfig.Navigation.DEFAULT_DEST_NAME,
            AmapConfig.Navigation.NAV_TYPE_DRIVING);
    }
    
    /**
     * 跳转到应用商店安装高德地图
     * @param context 上下文
     */
    public static void installAmap(Context context) {
        try {
            // 尝试跳转到应用商店
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AmapConfig.AMAP_MARKET_URL));
            marketIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            
            if (marketIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(marketIntent);
                Toast.makeText(context, "正在跳转到应用商店安装高德地图", Toast.LENGTH_SHORT).show();
            } else {
                // 应用商店不可用，跳转到网页版
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AmapConfig.AMAP_WEB_URL));
                webIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(webIntent);
                Toast.makeText(context, "正在跳转到网页安装高德地图", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            android.util.Log.e(TAG, "跳转到应用商店失败: " + e.getMessage());
            Toast.makeText(context, "无法跳转到应用商店，请手动安装高德地图", Toast.LENGTH_LONG).show();
        }
    }
    
    /**
     * 获取导航类型的中文名称
     * @param navType 导航类型
     * @return 中文名称
     */
    public static String getNavigationTypeName(int navType) {
        switch (navType) {
            case AmapConfig.Navigation.NAV_TYPE_DRIVING:
                return "驾车";
            case AmapConfig.Navigation.NAV_TYPE_BUS:
                return "公交";
            case AmapConfig.Navigation.NAV_TYPE_WALK:
                return "步行";
            case AmapConfig.Navigation.NAV_TYPE_BIKE:
                return "骑行";
            default:
                return "驾车";
        }
    }
    
    /**
     * 验证坐标是否有效
     * @param lat 纬度
     * @param lng 经度
     * @return 是否有效
     */
    public static boolean isValidCoordinate(double lat, double lng) {
        return lat >= -90 && lat <= 90 && lng >= -180 && lng <= 180;
    }
    
    /**
     * 获取当前位置（模拟实现，实际应用中需要调用定位服务）
     * @param context 上下文
     * @return 当前位置的经纬度数组 [纬度, 经度]，如果获取失败返回null
     */
    public static double[] getCurrentLocation(Context context) {
        try {
            // 这里应该调用实际的定位服务获取当前位置
            // 目前返回一个模拟位置（北京天安门附近）
            return new double[]{39.909604, 116.397228};
        } catch (Exception e) {
            android.util.Log.e(TAG, "获取当前位置失败: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 计算两点之间的距离（粗略计算）
     * @param lat1 起点纬度
     * @param lng1 起点经度
     * @param lat2 终点纬度
     * @param lng2 终点经度
     * @return 距离（公里）
     */
    public static double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        // 使用简单的球面距离公式（粗略计算）
        double earthRadius = 6371; // 地球半径（公里）
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
}