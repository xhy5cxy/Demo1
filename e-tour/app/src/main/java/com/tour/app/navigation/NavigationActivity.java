package com.tour.app.navigation;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.tour.app.R;
import com.tour.app.common.BaseActivity;
import com.tour.app.location.TourLocationManager;
import com.tour.app.utils.PermissionHelper;

/**
 * 导航功能Activity
 * 提供从当前位置导航到目的地的完整功能
 */
public class NavigationActivity extends BaseActivity {
    
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    
    private EditText etDestination;
    private RadioGroup rgMode;
    private Button btnNavigate, btnGetCurrentLocation;
    private TextView tvCurrentLocation;
    private LinearLayout llLocationInfo;
    
    private TourLocationManager locationManager;
    private Location currentLocation;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 初始化定位管理器
        locationManager = new TourLocationManager(this);
        
        // 设置位置变化监听器
        locationManager.setOnLocationChangeListener(new TourLocationManager.OnLocationChangeListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = location;
                updateLocationInfo();
            }
            
            @Override
            public void onLocationError(String error) {
                runOnUiThread(() -> {
                    tvCurrentLocation.setText("定位失败: " + error);
                    Toast.makeText(NavigationActivity.this, error, Toast.LENGTH_SHORT).show();
                });
            }
        });
        
        // 检查权限并开始定位
        checkPermissionsAndStartLocation();
        
        // 设置监听器
        setupListeners();
        
        // 设置热门目的地快捷按钮
        setupQuickDestinations();
    }
    
    @Override
    protected int getLayoutId() {
        return R.layout.activity_navigation;
    }
    
    @Override
    protected void initViews() {
        etDestination = findViewById(R.id.et_destination);
        rgMode = findViewById(R.id.rg_mode);
        btnNavigate = findViewById(R.id.btn_start_navigate);
        btnGetCurrentLocation = findViewById(R.id.btn_get_current_location);
        tvCurrentLocation = findViewById(R.id.tv_current_location);
        llLocationInfo = findViewById(R.id.ll_location_info);
        
        // 初始化底部导航栏视图
        initBottomNavigation();
    }
    
    @Override
    protected void initData() {
        // 初始化导航相关数据
        // 可以在这里加载默认目的地或用户历史记录等
    }
    
    @Override
    protected void setCurrentNavigationItem() {
        // 导航页面是独立功能，不设置任何底部导航项为选中状态
        // 保持底部导航栏的默认状态
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (PermissionHelper.hasLocationPermissions(this)) {
            try {
                locationManager.startLocationUpdates();
            } catch (SecurityException e) {
                // 处理权限异常
                Toast.makeText(this, "定位权限异常，请检查权限设置", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.stopLocationUpdates();
    }
    
    private void setupListeners() {
        btnNavigate.setOnClickListener(v -> startNavigation());
        btnGetCurrentLocation.setOnClickListener(v -> getCurrentLocation());
        
        // 设置热门目的地快捷按钮
        setupQuickDestinations();
        
        // 设置底部导航栏监听器
        setupBottomNavigationListeners();
    }
    
    private void setupQuickDestinations() {
        // 热门景点快捷导航
        String[] popularDestinations = {
                "天安门", "故宫", "长城", "颐和园", "鸟巢", "水立方"
        };
        
        LinearLayout quickDestinationsLayout = findViewById(R.id.ll_quick_destinations);
        if (quickDestinationsLayout != null) {
            for (String destination : popularDestinations) {
                Button button = new Button(this);
                button.setText(destination);
                button.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                button.setPadding(16, 8, 16, 8);
                button.setOnClickListener(v -> {
                    etDestination.setText(destination);
                    startNavigation();
                });
                quickDestinationsLayout.addView(button);
            }
        }
    }
    
    private void checkPermissionsAndStartLocation() {
        if (!PermissionHelper.hasLocationPermissions(this)) {
            PermissionHelper.requestLocationPermissions(this, LOCATION_PERMISSION_REQUEST_CODE);
            llLocationInfo.setVisibility(View.GONE);
        } else {
            startLocationService();
        }
    }
    
    private void startLocationService() {
        locationManager.startLocationUpdates();
        llLocationInfo.setVisibility(View.VISIBLE);
    }
    
    private void getCurrentLocation() {
        if (currentLocation != null) {
            updateLocationInfo();
        } else {
            Toast.makeText(this, "正在获取当前位置...", Toast.LENGTH_SHORT).show();
            locationManager.startLocationUpdates();
        }
    }
    
    private void updateLocationInfo() {
        if (currentLocation != null) {
            String locationInfo = String.format("当前位置: %.6f, %.6f\n精度: %.1f米",
                    currentLocation.getLatitude(),
                    currentLocation.getLongitude(),
                    currentLocation.getAccuracy());
            tvCurrentLocation.setText(locationInfo);
        }
    }
    

    
    private void startNavigation() {
        String destination = etDestination.getText().toString().trim();
        if (destination.isEmpty()) {
            Toast.makeText(this, "请输入目的地", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // 获取导航模式
        int selectedMode = getSelectedMode();
        
        // 如果有当前位置，使用当前位置作为起点
        if (currentLocation != null) {
            AmapNavigationHelper.smartNavigate(this, destination, 
                    currentLocation.getLatitude(), currentLocation.getLongitude(), selectedMode);
        } else {
            // 没有当前位置，使用默认导航
            AmapNavigationHelper.navigateTo(this, destination);
        }
    }
    
    private int getSelectedMode() {
        int selectedId = rgMode.getCheckedRadioButtonId();
        
        if (selectedId == R.id.rb_drive) {
            return 0; // 驾车
        } else if (selectedId == R.id.rb_bus) {
            return 1; // 公交
        } else if (selectedId == R.id.rb_walk) {
            return 2; // 步行
        } else if (selectedId == R.id.rb_bike) {
            return 3; // 骑行
        }
        
        return 0; // 默认驾车
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, 
                                          @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            
            if (allGranted) {
                startLocationService();
                Toast.makeText(this, "定位权限已授予", Toast.LENGTH_SHORT).show();
            } else {
                llLocationInfo.setVisibility(View.GONE);
                Toast.makeText(this, "定位权限被拒绝，部分功能受限", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    /**
     * 从其他Activity启动导航
     */
    public static void startNavigation(AppCompatActivity activity, String destination) {
        Intent intent = new Intent(activity, NavigationActivity.class);
        if (destination != null) {
            intent.putExtra("destination", destination);
        }
        activity.startActivity(intent);
    }
    
    /**
     * 初始化底部导航栏
     */
    private void initBottomNavigation() {
        // 底部导航栏视图已经在布局文件中定义
        // 这里可以添加额外的初始化逻辑，如设置默认选中状态等
    }
    
    /**
     * 设置底部导航栏点击事件
     */
    private void setupBottomNavigationListeners() {
        // 旅行之路按钮
        findViewById(R.id.btn_travel).setOnClickListener(v -> navigateToMainActivity());
        
        // 攻略广场按钮
        findViewById(R.id.btn_explore).setOnClickListener(v -> navigateToCommunityActivity());
        
        // 中间加号按钮
        findViewById(R.id.btn_add).setOnClickListener(v -> showAddMenu());
        
        // AI助手按钮
        findViewById(R.id.btn_ai).setOnClickListener(v -> navigateToAIActivity());
        
        // 我的按钮
        findViewById(R.id.btn_me).setOnClickListener(v -> navigateToMyselfActivity());
    }
    
    /**
     * 跳转到主页面
     */
    private void navigateToMainActivity() {
        Intent intent = new Intent(this, com.tour.app.MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
    
    /**
     * 跳转到攻略广场页面
     */
    private void navigateToCommunityActivity() {
        // 这里需要根据实际项目中的攻略广场Activity类名进行修改
        Toast.makeText(this, "攻略广场功能开发中", Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(this, com.tour.app.CommunityActivity.class);
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // startActivity(intent);
        // finish();
    }
    
    /**
     * 跳转到AI助手页面
     */
    private void navigateToAIActivity() {
        // 这里需要根据实际项目中的AI助手Activity类名进行修改
        Toast.makeText(this, "AI助手功能开发中", Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(this, com.tour.app.AIActivity.class);
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // startActivity(intent);
        // finish();
    }
    
    /**
     * 跳转到我的页面
     */
    private void navigateToMyselfActivity() {
        // 这里需要根据实际项目中的我的页面Activity类名进行修改
        Toast.makeText(this, "我的页面功能开发中", Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(this, com.tour.app.MyselfActivity.class);
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // startActivity(intent);
        // finish();
    }
    
    /**
     * 显示添加菜单
     */
    private void showAddMenu() {
        Toast.makeText(this, "添加功能开发中", Toast.LENGTH_SHORT).show();
        // 这里可以添加弹出菜单或对话框的逻辑
    }
}