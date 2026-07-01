package com.tour.app.utils;

/**
 * 高德地图API配置类
 * 用于管理高德地图相关的配置参数
 */
public class AmapConfig {
    
    // 高德地图API密钥（从AndroidManifest.xml中配置）
    // 实际密钥在strings.xml中定义：amap_api_key
    public static final String API_KEY_CONFIGURED = "configured_in_manifest";
    
    // 高德地图包名
    public static final String AMAP_PACKAGE_NAME = "com.autonavi.minimap";
    
    // 高德地图应用商店链接
    public static final String AMAP_MARKET_URL = "market://details?id=com.autonavi.minimap";
    public static final String AMAP_WEB_URL = "https://app.mi.com/details?id=com.autonavi.minimap";
    
    // 导航相关配置
    public static class Navigation {
        // 默认目的地（天安门）
        public static final double DEFAULT_DEST_LAT = 39.908823;
        public static final double DEFAULT_DEST_LNG = 116.397470;
        public static final String DEFAULT_DEST_NAME = "天安门";
        
        // 导航类型：0-驾车，1-公交，2-步行，3-骑行
        public static final int NAV_TYPE_DRIVING = 0;
        public static final int NAV_TYPE_BUS = 1;
        public static final int NAV_TYPE_WALK = 2;
        public static final int NAV_TYPE_BIKE = 3;
        
        // 导航URI模板
        public static final String NAV_URI_TEMPLATE = "amapuri://route/plan/?dlat=%s&dlon=%s&dname=%s&dev=0&t=%d";
    }
    
    // 路径规划相关配置
    public static class Route {
        // 路径规划URL模板
        public static final String ROUTE_API_URL = "https://restapi.amap.com/v3/direction/driving";
        
        // 路径规划参数
        public static final String ORIGIN = "origin";
        public static final String DESTINATION = "destination";
        public static final String KEY = "key";
        public static final String OUTPUT = "json";
    }
    
    // 地理编码相关配置
    public static class Geocode {
        // 地理编码API URL
        public static final String GEOCODE_API_URL = "https://restapi.amap.com/v3/geocode/geo";
        
        // 逆地理编码API URL
        public static final String REVERSE_GEOCODE_API_URL = "https://restapi.amap.com/v3/geocode/regeo";
    }
    
    // 错误码定义
    public static class ErrorCode {
        public static final int SUCCESS = 1000;
        public static final int INVALID_API_KEY = 10001;
        public static final int SERVICE_NOT_AVAILABLE = 10002;
        public static final int DAILY_QUOTA_EXCEEDED = 10003;
        public static final int ACCESS_TOO_FREQUENT = 10004;
        public static final int INVALID_USER_IP = 10005;
        public static final int INVALID_USER_DOMAIN = 10006;
        public static final int INVALID_USER_SIGNATURE = 10007;
        public static final int INVALID_USER_SCODE = 10008;
        public static final int INVALID_USER_KEY = 10009;
        public static final int IP_QUERY_QUOTA_EXCEEDED = 10010;
        public static final int NOT_SUPPORT_HTTPS = 10011;
        public static final int INSUFFICIENT_PRIVILEGES = 10012;
        public static final int USER_KEY_PLATFORM_MISMATCH = 10013;
        public static final int IP_QUERY_TOO_FREQUENT = 10014;
        public static final int INVALID_PARAMS = 30001;
        public static final int MISSING_REQUIRED_PARAMS = 30002;
        public static final int ILLEGAL_REQUEST = 30003;
        public static final int UNKNOWN_ERROR = 30004;
        public static final int INVALID_SERVICE = 40001;
        public static final int SERVICE_RESPONSE_ERROR = 40002;
        public static final int QUERY_FAILED = 40003;
    }
    
    /**
     * 获取高德地图导航URI
     * @param destLat 目的地纬度
     * @param destLng 目的地经度
     * @param destName 目的地名称
     * @param navType 导航类型
     * @return 导航URI
     */
    public static String getNavigationUri(double destLat, double destLng, String destName, int navType) {
        return String.format(Navigation.NAV_URI_TEMPLATE, destLat, destLng, destName, navType);
    }
    
    /**
     * 获取默认导航URI（驾车到天安门）
     * @return 默认导航URI
     */
    public static String getDefaultNavigationUri() {
        return getNavigationUri(Navigation.DEFAULT_DEST_LAT, Navigation.DEFAULT_DEST_LNG, Navigation.DEFAULT_DEST_NAME, Navigation.NAV_TYPE_DRIVING);
    }
}