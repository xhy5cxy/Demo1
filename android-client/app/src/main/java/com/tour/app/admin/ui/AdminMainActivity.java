package com.tour.app.admin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.tour.app.R;
import com.tour.app.admin.adapter.AdminMenuAdapter;
import com.tour.app.admin.model.AdminMenuItem;
import java.util.ArrayList;
import java.util.List;

public class AdminMainActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private AdminMenuAdapter adapter;
    private Button btnLogout;
    private TextView tvWelcome;
    private Button btnMapTest;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        
        initViews();
        setupRecyclerView();
        setupListeners();
    }
    
    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view_admin_menu);
        btnLogout = findViewById(R.id.btn_logout);
        tvWelcome = findViewById(R.id.tv_welcome);
        btnMapTest = findViewById(R.id.btn_map_test);
        
        tvWelcome.setText("欢迎，管理员");
    }
    
    private void setupRecyclerView() {
        List<AdminMenuItem> menuItems = createMenuItems();
        adapter = new AdminMenuAdapter(menuItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        
        adapter.setOnItemClickListener(new AdminMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AdminMenuItem item) {
                handleMenuClick(item);
            }
        });
    }
    
    private List<AdminMenuItem> createMenuItems() {
        List<AdminMenuItem> items = new ArrayList<>();
        
        // 用户管理
        items.add(new AdminMenuItem(
            R.drawable.ic_user_management,
            "用户管理",
            "查看用户数据、处理投诉、权限管理、行为分析",
            "UserManagementActivity"
        ));
        
        // 内容管理
        items.add(new AdminMenuItem(
            R.drawable.ic_content_management,
            "内容管理",
            "审核攻略评论、处理举报、推荐优质内容",
            "ContentManagementActivity"
        ));
        
        // 系统监控
        items.add(new AdminMenuItem(
            R.drawable.ic_system_monitor,
            "系统监控",
            "性能监控、数据分析、AI服务管理、系统配置",
            "SystemMonitorActivity"
        ));
        
        // 数据统计
        items.add(new AdminMenuItem(
            R.drawable.ic_data_statistics,
            "数据统计",
            "系统使用数据统计和分析",
            "DataStatisticsActivity"
        ));
        
        // 地图测试
        items.add(new AdminMenuItem(
            R.drawable.ic_explore,
            "地图测试",
            "测试导航地图功能",
            "NavigationActivity"
        ));
        
        return items;
    }
    
    private void handleMenuClick(AdminMenuItem item) {
        // 根据菜单项处理点击事件
        switch (item.getTitle()) {
            case "用户管理":
                Toast.makeText(this, "进入用户管理页面", Toast.LENGTH_SHORT).show();
                // Intent intent = new Intent(this, UserManagementActivity.class);
                // startActivity(intent);
                break;
            case "内容管理":
                Toast.makeText(this, "进入内容管理页面", Toast.LENGTH_SHORT).show();
                break;
            case "系统监控":
                Toast.makeText(this, "进入系统监控页面", Toast.LENGTH_SHORT).show();
                break;
            case "数据统计":
                Toast.makeText(this, "进入数据统计页面", Toast.LENGTH_SHORT).show();
                break;
            case "地图测试":
                Toast.makeText(this, "正在启动导航地图...", Toast.LENGTH_SHORT).show();
                // 启动导航Activity
                Intent intent = new Intent(this, com.tour.app.navigation.NavigationActivity.class);
                startActivity(intent);
                break;
        }
    }
    
    private void setupListeners() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 退出登录，返回主页面
                Intent intent = new Intent(AdminMainActivity.this, com.tour.app.MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        
        btnMapTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动导航地图测试
                Toast.makeText(AdminMainActivity.this, "正在启动导航地图测试...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminMainActivity.this, com.tour.app.navigation.NavigationActivity.class);
                startActivity(intent);
            }
        });
    }
}