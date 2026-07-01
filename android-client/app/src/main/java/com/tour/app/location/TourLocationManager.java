package com.tour.app.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import androidx.core.app.ActivityCompat;

/**
 * 定位管理器类
 * 用于获取当前位置信息，替代错误的LocationService直接实例化
 */
public class TourLocationManager implements LocationListener {
    
    private static final String TAG = "LocationManager";
    private static final long MIN_TIME_BETWEEN_UPDATES = 10000; // 10秒
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10米
    
    private Context mContext;
    private android.location.LocationManager locationManager;
    private Location currentLocation;
    
    // 定位监听器
    private OnLocationChangeListener locationChangeListener;
    
    public interface OnLocationChangeListener {
        void onLocationChanged(Location location);
        void onLocationError(String error);
    }
    
    public TourLocationManager(Context context) {
        this.mContext = context;
        this.locationManager = (android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Log.d(TAG, "LocationManager created");
    }
    
    /**
     * 开始定位更新
     */
    public void startLocationUpdates() {
        if (!isLocationEnabled()) {
            if (locationChangeListener != null) {
                locationChangeListener.onLocationError("定位服务未开启");
            }
            return;
        }
        
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) 
                != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) 
                != PackageManager.PERMISSION_GRANTED) {
            
            if (locationChangeListener != null) {
                locationChangeListener.onLocationError("定位权限未授予");
            }
            return;
        }
        
        try {
            // 优先使用GPS定位
            if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(
                        android.location.LocationManager.GPS_PROVIDER,
                        MIN_TIME_BETWEEN_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        this);
            }
            
            // 使用网络定位作为备用
            if (locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(
                        android.location.LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BETWEEN_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        this);
            }
            
            // 获取最后一次已知位置
            Location lastKnownLocation = getLastKnownLocation();
            if (lastKnownLocation != null) {
                currentLocation = lastKnownLocation;
                if (locationChangeListener != null) {
                    locationChangeListener.onLocationChanged(lastKnownLocation);
                }
            }
            
            Log.d(TAG, "Location updates started");
            
        } catch (SecurityException e) {
            Log.e(TAG, "SecurityException: " + e.getMessage());
            if (locationChangeListener != null) {
                locationChangeListener.onLocationError("定位权限异常");
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
            if (locationChangeListener != null) {
                locationChangeListener.onLocationError("定位服务异常");
            }
        }
    }
    
    /**
     * 停止定位更新
     */
    public void stopLocationUpdates() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
            Log.d(TAG, "Location updates stopped");
        }
    }
    
    /**
     * 获取最后一次已知位置
     */
    public Location getLastKnownLocation() {
        Location location = null;
        
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) 
                != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) 
                != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        
        try {
            // 优先获取GPS位置
            if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
                location = locationManager.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER);
            }
            
            // 如果GPS位置不可用，获取网络位置
            if (location == null && locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
                location = locationManager.getLastKnownLocation(android.location.LocationManager.NETWORK_PROVIDER);
            }
            
        } catch (SecurityException e) {
            Log.e(TAG, "SecurityException in getLastKnownLocation: " + e.getMessage());
        }
        
        return location;
    }
    
    /**
     * 检查定位服务是否开启
     */
    public boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) ||
               locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER);
    }
    
    /**
     * 获取当前位置
     */
    public Location getCurrentLocation() {
        return currentLocation;
    }
    
    /**
     * 设置位置变化监听器
     */
    public void setOnLocationChangeListener(OnLocationChangeListener listener) {
        this.locationChangeListener = listener;
    }
    
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            currentLocation = location;
            Log.d(TAG, "Location updated: " + location.getLatitude() + ", " + location.getLongitude());
            
            if (locationChangeListener != null) {
                locationChangeListener.onLocationChanged(location);
            }
        }
    }
    
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "Provider " + provider + " status changed: " + status);
    }
    
    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "Provider " + provider + " enabled");
    }
    
    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG, "Provider " + provider + " disabled");
        if (locationChangeListener != null) {
            locationChangeListener.onLocationError("定位服务已关闭");
        }
    }
}