package com.tour.app.common;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tour.app.MainActivity;
import com.tour.app.R;
import com.tour.app.ai.AImainActivity;
import com.tour.app.community.ui.CommunityActivity;
import com.tour.app.me.MyselfActivity;



/**
 * 底部导航栏管理类
 * 提供统一的底部导航功能，可在多个Activity中复用
 */
public class BottomNavigationManager {
    
    private Activity activity;
    private LinearLayout btnTravel, btnExplore, btnAi, btnMe;
    private ImageView ivTravel, ivExplore, ivAi, ivMe;
    private TextView tvTravel, tvExplore, tvAi, tvMe;
    private View bottomNavView;
    
    public BottomNavigationManager(Activity activity, View rootView) {
        this.activity = activity;
        initViews(rootView);
        initListeners();
    }
    
    private void initViews(View rootView) {
        try {
            // 查找底部导航栏的各个组件
            bottomNavView = rootView.findViewById(R.id.bottom_nav);
            
            // 检查底部导航栏是否存在
            if (bottomNavView == null) {
                return; // 如果没有底部导航栏，直接返回
            }
            
            btnTravel = rootView.findViewById(R.id.btn_travel);
            btnExplore = rootView.findViewById(R.id.btn_explore);
            btnAi = rootView.findViewById(R.id.btn_ai);
            btnMe = rootView.findViewById(R.id.btn_me);
            
            ivTravel = rootView.findViewById(R.id.iv_travel);
            ivExplore = rootView.findViewById(R.id.iv_explore);
            ivAi = rootView.findViewById(R.id.iv_ai);
            ivMe = rootView.findViewById(R.id.iv_me);
            
            tvTravel = rootView.findViewById(R.id.tv_travel);
            tvExplore = rootView.findViewById(R.id.tv_explore);
            tvAi = rootView.findViewById(R.id.tv_ai);
            tvMe = rootView.findViewById(R.id.tv_me);
        } catch (Exception e) {
            // 捕获可能的空指针异常
            e.printStackTrace();
        }
    }
    
    private void initListeners() {
        // 检查底部导航栏是否存在
        if (bottomNavView == null) {
            return; // 如果没有底部导航栏，直接返回
        }
        
        // 旅行按钮
        if (btnTravel != null) {
            btnTravel.setOnClickListener(v -> {
                if (!(activity instanceof MainActivity)) {
                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.startActivity(intent);
                }
            });
        }
        
        // 探索按钮
        if (btnExplore != null) {
            btnExplore.setOnClickListener(v -> {
                if (!(activity instanceof CommunityActivity)) {
                    Intent intent = new Intent(activity, CommunityActivity.class);
                    activity.startActivity(intent);
                }
            });
        }
        
        // AI助手按钮
        if (btnAi != null) {
            btnAi.setOnClickListener(v -> {
                if (!(activity instanceof AImainActivity)) {
                    Intent intent = new Intent(activity, AImainActivity.class);
                    activity.startActivity(intent);
                }
            });
        }
        
        // 我的按钮
        if (btnMe != null) {
            btnMe.setOnClickListener(v -> {
//                 这里可以跳转到我的页面
                 Intent intent = new Intent(activity, MyselfActivity.class);
                 activity.startActivity(intent);
            });
        }
    }
    
    /**
     * 设置当前选中的导航项
     * @param selectedItem 选中的导航项ID
     */
    public void setSelectedItem(int selectedItem) {
        // 检查底部导航栏是否存在
        if (bottomNavView == null) {
            return; // 如果没有底部导航栏，直接返回
        }
        
        // 重置所有按钮状态
        resetAllButtons();
        
        // 设置选中状态
        if (selectedItem == R.id.btn_travel) {
            setButtonSelected(ivTravel, tvTravel, true);
        } else if (selectedItem == R.id.btn_explore) {
            setButtonSelected(ivExplore, tvExplore, true);
        } else if (selectedItem == R.id.btn_ai) {
            setButtonSelected(ivAi, tvAi, true);
        } else if (selectedItem == R.id.btn_me) {
            setButtonSelected(ivMe, tvMe, true);
        }
    }
    
    /**
     * 重置所有按钮状态
     */
    private void resetAllButtons() {
        setButtonSelected(ivTravel, tvTravel, false);
        setButtonSelected(ivExplore, tvExplore, false);
        setButtonSelected(ivAi, tvAi, false);
        setButtonSelected(ivMe, tvMe, false);
    }
    
    /**
     * 设置按钮选中状态
     */
    private void setButtonSelected(ImageView imageView, TextView textView, boolean selected) {
        // 添加空值检查
        if (textView == null || imageView == null) {
            return;
        }
        
        if (selected) {
            textView.setTextColor(activity.getResources().getColor(android.R.color.holo_blue_bright));
            // 设置图标选中状态 - 使用蓝色滤镜
            imageView.setColorFilter(activity.getResources().getColor(android.R.color.holo_blue_bright));
        } else {
            textView.setTextColor(activity.getResources().getColor(android.R.color.darker_gray));
            // 设置图标未选中状态 - 使用灰色滤镜
            imageView.setColorFilter(activity.getResources().getColor(android.R.color.darker_gray));
        }
    }
    
    /**
     * 获取底部导航栏的高度
     * @return 底部导航栏高度
     */
    public int getBottomNavigationHeight() {
        if (bottomNavView == null) {
            return 0;
        }
        return bottomNavView.getHeight();
    }
    
    /**
     * 设置底部导航栏的可见性
     * @param visible 是否可见
     */
    public void setBottomNavigationVisible(boolean visible) {
        if (bottomNavView == null) {
            return;
        }
        bottomNavView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
    
    /**
     * 获取底部导航栏视图
     * @return 底部导航栏视图
     */
    public View getBottomNavigationView() {
        return bottomNavView;
    }
}