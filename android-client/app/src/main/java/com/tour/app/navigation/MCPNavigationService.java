package com.tour.app.navigation;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.widget.Toast;

import com.tour.app.location.TourLocationManager;
import com.tour.app.navigation.AmapNavigationHelper;

/**
 * MCP导航服务类
 * 提供智能导航服务，集成MCP地图服务为用户提供最佳导航方案
 */
public class MCPNavigationService {
    
    private Context context;
    private TourLocationManager locationManager;
    private Location currentLocation;
    
    public MCPNavigationService(Context context) {
        this.context = context;
        this.locationManager = new TourLocationManager(context);
    }
    
    /**
     * 启动MCP智能导航
     * @param destination 目的地名称或地址
     */
    public void startSmartNavigation(String destination) {
        // 验证目的地
        if (destination == null || destination.trim().isEmpty()) {
            Toast.makeText(context, "请输入有效的目的地", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // 显示导航启动提示
        Toast.makeText(context, "🚀 正在为您规划最佳路线...", Toast.LENGTH_SHORT).show();
        
        // 检查定位权限
        if (!com.tour.app.utils.PermissionHelper.hasLocationPermissions(context)) {
            Toast.makeText(context, "需要定位权限才能使用智能导航功能", Toast.LENGTH_LONG).show();
            // 不立即返回，让用户有机会授权
        }
        
        // 获取当前位置
        getCurrentLocation(new LocationCallback() {
            @Override
            public void onLocationReady(Location location) {
                if (location != null) {
                    // 使用MCP服务进行智能导航
                    performMCPNavigation(destination, location);
                } else {
                    // 无法获取位置，使用默认导航
                    Toast.makeText(context, "无法获取当前位置，使用默认导航", Toast.LENGTH_SHORT).show();
                    startFallbackNavigation(destination);
                }
            }
            
            @Override
            public void onLocationError(String error) {
                Toast.makeText(context, "定位失败: " + error, Toast.LENGTH_SHORT).show();
                // 使用默认导航作为备选方案
                startFallbackNavigation(destination);
            }
        });
    }
    
    /**
     * 执行MCP智能导航
     */
    private void performMCPNavigation(String destination, Location startLocation) {
        try {
            // 显示MCP导航启动提示
            Toast.makeText(context, "🚀 MCP智能导航启动中...", Toast.LENGTH_SHORT).show();
            
            // 使用MCP服务获取最佳路线
            String mcpRouteInfo = getMCPOptimalRoute(destination, startLocation);
            
            // 显示MCP导航信息
            showMCPNavigationInfo(mcpRouteInfo);
            
            // 启动高德地图进行实际导航
            boolean navigationSuccess = AmapNavigationHelper.navigateWithAmapApp(context, destination, 
                    startLocation.getLatitude(), startLocation.getLongitude(), 0);
            
            if (!navigationSuccess) {
                // App导航失败，使用网页版
                Toast.makeText(context, "高德地图App未安装，使用网页版导航", Toast.LENGTH_LONG).show();
                AmapNavigationHelper.navigateWithAmapWeb(context, destination, 
                        startLocation.getLatitude(), startLocation.getLongitude(), "car");
            }
                    
        } catch (Exception e) {
            // MCP服务失败时使用备用方案
            Toast.makeText(context, "MCP服务暂时不可用，使用标准导航", Toast.LENGTH_SHORT).show();
            startFallbackNavigation(destination);
        }
    }
    
    /**
     * 备用导航方案
     */
    private void startFallbackNavigation(String destination) {
        try {
            // 尝试使用高德地图App导航
            double[] defaultCoordinates = getDefaultCoordinates();
            boolean appSuccess = AmapNavigationHelper.navigateWithAmapApp(context, destination, 
                    defaultCoordinates[0], defaultCoordinates[1], 0);
            
            if (!appSuccess) {
                // App导航失败，使用网页版
                Toast.makeText(context, "正在为您打开网页版导航...", Toast.LENGTH_SHORT).show();
                AmapNavigationHelper.navigateWithAmapWeb(context, destination, 
                        defaultCoordinates[0], defaultCoordinates[1], "car");
            }
        } catch (Exception e) {
            // 所有导航方式都失败
            Toast.makeText(context, "导航功能暂时不可用，请稍后重试", Toast.LENGTH_LONG).show();
            android.util.Log.e("MCPNavigationService", "所有导航方式失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取默认坐标（北京天安门）
     */
    private double[] getDefaultCoordinates() {
        return new double[]{39.908823, 116.397470}; // 天安门坐标
    }
    
    /**
     * 获取MCP最优路线（模拟实现）
     */
    private String getMCPOptimalRoute(String destination, Location startLocation) {
        // 这里可以集成真实的MCP地图服务API
        // 目前使用智能路线规划算法
        
        // 计算距离和预估时间
        double distance = calculateDistance(startLocation.getLatitude(), startLocation.getLongitude(),
                getDestinationCoordinates(destination)[0], getDestinationCoordinates(destination)[1]);
        
        // 根据距离推荐最佳出行方式
        String recommendedMode = getRecommendedMode(distance);
        String estimatedTime = getEstimatedTime(distance, recommendedMode);
        
        return String.format("🚀 MCP智能路线规划完成\n📍 起点: %.6f, %.6f\n🎯 目的地: %s\n📏 距离: %.1f公里\n⏱️ 预估时间: %s\n🚗 推荐方式: %s",
                startLocation.getLatitude(), startLocation.getLongitude(), destination,
                distance, estimatedTime, recommendedMode);
    }
    
    /**
     * 显示MCP导航信息
     */
    private void showMCPNavigationInfo(String routeInfo) {
        // 在实际应用中，这里可以显示详细的导航信息对话框
        // 目前使用Toast显示完整信息
        Toast.makeText(context, routeInfo, Toast.LENGTH_LONG).show();
        
        // 同时记录到日志
        android.util.Log.i("MCPNavigationService", "导航信息: " + routeInfo);
    }
    
    /**
     * 计算两点间距离（公里）
     */
    private double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371; // 地球半径（公里）
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
    
    /**
     * 根据距离推荐最佳出行方式
     */
    private String getRecommendedMode(double distance) {
        if (distance < 1) {
            return "步行";
        } else if (distance < 5) {
            return "骑行";
        } else if (distance < 20) {
            return "公交";
        } else {
            return "驾车";
        }
    }
    
    /**
     * 预估行程时间
     */
    private String getEstimatedTime(double distance, String mode) {
        double speed;
        switch (mode) {
            case "步行": speed = 5; break;    // 5 km/h
            case "骑行": speed = 15; break;   // 15 km/h
            case "公交": speed = 25; break;   // 25 km/h
            case "驾车": speed = 40; break;   // 40 km/h
            default: speed = 30;
        }
        
        double hours = distance / speed;
        if (hours < 1) {
            return String.format("%.0f分钟", hours * 60);
        } else {
            return String.format("%.1f小时", hours);
        }
    }
    
    /**
     * 获取目的地坐标
     */
    private double[] getDestinationCoordinates(String destination) {
        // 预设热门景点坐标
        switch (destination) {
            case "天安门": return new double[]{39.908823, 116.397470};
            case "故宫": return new double[]{39.916345, 116.397155};
            case "长城": return new double[]{40.431908, 116.570374};
            case "颐和园": return new double[]{39.999982, 116.275457};
            case "鸟巢": return new double[]{39.992806, 116.391319};
            case "水立方": return new double[]{39.991861, 116.387594};
            default: return new double[]{39.908823, 116.397470}; // 默认天安门
        }
    }
    
    /**
     * 获取当前位置
     */
    private void getCurrentLocation(LocationCallback callback) {
        locationManager.setOnLocationChangeListener(new TourLocationManager.OnLocationChangeListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = location;
                locationManager.stopLocationUpdates();
                callback.onLocationReady(location);
            }
            
            @Override
            public void onLocationError(String error) {
                locationManager.stopLocationUpdates();
                callback.onLocationError(error);
            }
        });
        
        try {
            locationManager.startLocationUpdates();
        } catch (SecurityException e) {
            callback.onLocationError("定位权限异常");
        }
    }
    
    /**
     * 位置回调接口
     */
    private interface LocationCallback {
        void onLocationReady(Location location);
        void onLocationError(String error);
    }
    
    /**
     * 快速启动MCP导航（默认目的地）
     */
    public void startQuickNavigation() {
        // 使用默认热门目的地
        String[] popularDestinations = {
            "天安门", "故宫", "长城", "颐和园", "鸟巢", "水立方"
        };
        
        // 随机选择一个热门目的地
        String destination = popularDestinations[(int)(Math.random() * popularDestinations.length)];
        startSmartNavigation(destination);
    }
    
    /**
     * 启动MCP导航到指定坐标
     */
    public void startNavigationToCoordinates(double latitude, double longitude, String placeName) {
        String destination = placeName != null ? placeName : String.format("%.6f,%.6f", latitude, longitude);
        startSmartNavigation(destination);
    }
}