package com.tour.app.navigation;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * 高德地图导航助手类
 * 提供从当前位置导航到目的地的功能
 */
public class AmapNavigationHelper {
    
    private static final String AMAP_PACKAGE_NAME = "com.autonavi.minimap";
    private static final String AMAP_URI_SCHEME = "amapuri";
    
    /**
     * 检查是否安装了高德地图App
     */
    public static boolean isAmapInstalled(Context context) {
        try {
            context.getPackageManager().getPackageInfo(AMAP_PACKAGE_NAME, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    
    /**
     * 使用高德地图App进行导航
     * @param context 上下文
     * @param destination 目的地名称
     * @param dlat 目的地纬度
     * @param dlon 目的地经度
     * @param mode 导航模式：0-驾车，1-公交，2-步行，3-骑行
     * @return 是否成功启动导航
     */
    public static boolean navigateWithAmapApp(Context context, String destination, 
                                             double dlat, double dlon, int mode) {
        if (!isAmapInstalled(context)) {
            Toast.makeText(context, "请先安装高德地图App", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        try {
            String modeStr = getModeString(mode);
            String uriString = String.format("amapuri://route/plan/?dlat=%s&dlon=%s&dname=%s&dev=0&t=%s",
                    dlat, dlon, Uri.encode(destination), mode);
            
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uriString));
            intent.setPackage(AMAP_PACKAGE_NAME);
            
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * 使用高德地图网页版进行导航
     * @param context 上下文
     * @param destination 目的地名称
     * @param dlat 目的地纬度
     * @param dlon 目的地经度
     * @param mode 导航模式：car-驾车，bus-公交，walk-步行，ride-骑行
     */
    public static void navigateWithAmapWeb(Context context, String destination, 
                                          double dlat, double dlon, String mode) {
        try {
            String url = String.format("https://uri.amap.com/navigation?to=%s&to=%s,%s&mode=%s&src=etour",
                    Uri.encode(destination), dlat, dlon, mode);
            
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {
                // 如果没有浏览器，打开默认浏览器
                intent.setPackage(null);
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "导航失败，请检查网络连接", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * 智能导航：优先使用App，失败时使用网页版
     * @param context 上下文
     * @param destination 目的地名称
     * @param dlat 目的地纬度
     * @param dlon 目的地经度
     * @param mode 导航模式
     */
    public static void smartNavigate(Context context, String destination, 
                                    double dlat, double dlon, int mode) {
        // 优先使用高德地图App
        if (navigateWithAmapApp(context, destination, dlat, dlon, mode)) {
            return;
        }
        
        // App导航失败，使用网页版
        String webMode = getWebModeString(mode);
        navigateWithAmapWeb(context, destination, dlat, dlon, webMode);
    }
    
    /**
     * 简化的导航方法（只传目的地名称，使用默认坐标）
     * @param context 上下文
     * @param destination 目的地名称
     */
    public static void navigateTo(Context context, String destination) {
        // 这里可以预设一些热门景点的坐标
        double[] coordinates = getDestinationCoordinates(destination);
        if (coordinates != null) {
            smartNavigate(context, destination, coordinates[0], coordinates[1], 0); // 默认驾车模式
        } else {
            // 如果不知道具体坐标，使用名称搜索
            navigateWithAmapWeb(context, destination, 0, 0, "car");
        }
    }
    
    /**
     * 获取导航模式字符串
     */
    private static String getModeString(int mode) {
        switch (mode) {
            case 0: return "驾车";
            case 1: return "公交";
            case 2: return "步行";
            case 3: return "骑行";
            default: return "驾车";
        }
    }
    
    /**
     * 获取网页版导航模式字符串
     */
    private static String getWebModeString(int mode) {
        switch (mode) {
            case 0: return "car";
            case 1: return "bus";
            case 2: return "walk";
            case 3: return "ride";
            default: return "car";
        }
    }
    
    /**
     * 获取热门景点的坐标（可以扩展这个列表）
     */
    private static double[] getDestinationCoordinates(String destination) {
        if (TextUtils.isEmpty(destination)) {
            return null;
        }
        
        // 预设一些热门景点的坐标
        switch (destination) {
            case "天安门":
                return new double[]{39.908823, 116.397470};
            case "故宫":
                return new double[]{39.916345, 116.397155};
            case "长城":
                return new double[]{40.431908, 116.570374};
            case "颐和园":
                return new double[]{39.999982, 116.275457};
            case "鸟巢":
                return new double[]{39.992806, 116.391319};
            case "水立方":
                return new double[]{39.991861, 116.387594};
            default:
                return null;
        }
    }
}