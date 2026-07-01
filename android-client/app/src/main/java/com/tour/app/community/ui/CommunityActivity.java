package com.tour.app.community.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.tour.app.R;
import com.tour.app.common.BaseActivity;
import com.tour.app.community.adapter.StrategyAdapter;
import com.tour.app.data.StrategyDataManager;
import com.tour.app.model.Strategy;
import com.tour.app.model.User;

import java.util.ArrayList;
import java.util.List;

public class CommunityActivity extends BaseActivity {

    private ImageView ivSearch;
    private FrameLayout btnAdd;
    private RecyclerView recyclerView;
    private StrategyAdapter strategyAdapter;
    private List<Strategy> strategyList;
    private LinearLayout searchContainer;
    private LinearLayout searchInputContainer;
    private EditText etSearch;
    private TextView tvCancel;
    private LinearLayout btnCreateStrategy;
    private LinearLayout btnCreateTrip;
    private List<Strategy> originalStrategyList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_community;
    }

    @Override
    protected void initViews() {
        try {
            // 先检查布局是否正常加载
            View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
            if (rootView == null) {
                throw new RuntimeException("页面布局加载失败");
            }
            
            ivSearch = findViewById(R.id.iv_search);
            btnAdd = findViewById(R.id.btn_add);
            recyclerView = findViewById(R.id.recycler_view);
            searchContainer = findViewById(R.id.search_container);
            searchInputContainer = findViewById(R.id.search_input_container);
            etSearch = findViewById(R.id.et_search);
            tvCancel = findViewById(R.id.tv_cancel);
            btnCreateStrategy = findViewById(R.id.btn_create_strategy);
            btnCreateTrip = findViewById(R.id.btn_create_trip);
            
            // 检查关键视图是否成功加载
            if (ivSearch == null || btnAdd == null || recyclerView == null || 
                searchContainer == null || searchInputContainer == null || etSearch == null ||
                tvCancel == null || btnCreateStrategy == null || btnCreateTrip == null) {
                throw new RuntimeException("关键视图加载失败");
            }
            
            // 初始化点击事件
            setupClickListeners();
            
            // 设置底部导航栏选中状态
            setCurrentNavigationItem();
            
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "页面初始化失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void initData() {
        // 初始化数据列表
        strategyList = new ArrayList<>();
        originalStrategyList = new ArrayList<>();
        
        // 从数据管理器加载用户创建的攻略
        StrategyDataManager dataManager = StrategyDataManager.getInstance(this);
        List<Strategy> userStrategies = dataManager.getStrategies();
        
        // 添加用户创建的攻略到列表开头（最新显示在最前面）
        strategyList.addAll(userStrategies);
        
        // 如果用户攻略数量较少，添加默认模拟数据
        if (strategyList.size() < 5) {
            // 添加模拟攻略数据
            User user1 = new User();
            user1.setNickname("旅行达人");
            
            User user2 = new User();
            user2.setNickname("文化探索者");
            
            User user3 = new User();
            user3.setNickname("海岛达人");
            
            User user4 = new User();
            user4.setNickname("美食家");
            
            User user5 = new User();
            user5.setNickname("摄影爱好者");
            
            // 添加不同高度的攻略项，实现瀑布流效果
            strategyList.add(new Strategy("🐼 成都熊猫基地一日游攻略", "看熊猫、吃火锅、逛宽窄巷子全攻略，体验成都慢生活。推荐游玩时间：1天，适合亲子游。", user1, "成都", 856, 1200));
            strategyList.add(new Strategy("🏛️ 西安古城文化之旅攻略", "兵马俑、大雁塔、回民街美食全攻略，感受千年古都魅力。建议游玩：3-4天。", user2, "西安", 1100, 1500));
            strategyList.add(new Strategy("🏖️ 三亚海岛度假计划攻略", "海滩、海鲜、水上活动全攻略，享受阳光沙滩的完美假期。最佳季节：10月-次年4月。", user3, "三亚", 923, 800));
            strategyList.add(new Strategy("🍜 重庆火锅美食地图攻略", "解放碑、洪崖洞、磁器口，寻找最地道的重庆火锅和特色小吃。美食之旅不容错过！", user4, "重庆", 756, 1100));
            strategyList.add(new Strategy("🏔️ 张家界国家森林公园攻略", "天门山、玻璃栈道、袁家界，体验大自然的鬼斧神工。摄影爱好者的天堂。", user5, "张家界", 689, 1400));
            strategyList.add(new Strategy("🏮 苏州园林文化深度游攻略", "拙政园、狮子林、平江路，感受江南水乡的精致与韵味。建议游玩：2天。", user1, "苏州", 542, 900));
            strategyList.add(new Strategy("🏯 北京故宫博物院游览攻略", "太和殿、乾清宫、珍宝馆，探索明清皇家建筑的宏伟壮观。历史文化之旅。", user2, "北京", 1234, 1600));
            strategyList.add(new Strategy("🌊 青岛海滨城市休闲攻略", "栈桥、八大关、啤酒博物馆，享受海滨城市的浪漫与悠闲。啤酒节期间更佳。", user3, "青岛", 876, 1000));
            
            // 添加更多随机攻略数据
            strategyList.add(new Strategy("🌸 杭州西湖春日赏花攻略", "断桥残雪、苏堤春晓、花港观鱼，感受西湖十景的浪漫与诗意。", user4, "杭州", 432, 700));
            strategyList.add(new Strategy("🏜️ 敦煌莫高窟文化探索攻略", "壁画艺术、丝绸之路文化，体验千年佛教艺术的魅力。", user5, "敦煌", 321, 1300));
            strategyList.add(new Strategy("🏞️ 九寨沟自然风光摄影攻略", "五花海、诺日朗瀑布、长海，捕捉大自然的调色板。", user1, "九寨沟", 654, 1100));
            strategyList.add(new Strategy("🏰 上海外滩夜景拍摄攻略", "东方明珠、外白渡桥、陆家嘴金融区，记录魔都的璀璨夜色。", user2, "上海", 789, 950));
        }
        
        // 保存原始数据用于搜索
        originalStrategyList.addAll(strategyList);
        
        // 初始化RecyclerView和瀑布流布局（在数据准备好之后）
        setupRecyclerView();
        
        // 更新适配器数据
        if (strategyAdapter != null) {
            strategyAdapter.notifyDataSetChanged();
        }
    }
    
    private void setupRecyclerView() {
        // 创建瀑布流布局管理器，2列布局
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        
        // 创建适配器
        strategyAdapter = new StrategyAdapter(this, strategyList);
        recyclerView.setAdapter(strategyAdapter);
        
        // 设置RecyclerView的边距和滚动效果
        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(true);
    }

    @Override
    protected void setCurrentNavigationItem() {
        // 设置攻略广场按钮为选中状态
        try {
            if (bottomNavigationManager != null) {
                bottomNavigationManager.setSelectedItem(R.id.btn_explore);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupClickListeners() {
        // 搜索容器点击事件 - 显示搜索输入框
        if (searchContainer != null) {
            searchContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSearchInput();
                }
            });
        }
        
        // 取消按钮点击事件 - 隐藏搜索输入框
        if (tvCancel != null) {
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideSearchInput();
                }
            });
        }
        
        // 搜索输入框文本变化监听
        if (etSearch != null) {
            etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    performSearch(s.toString());
                }
                
                @Override
                public void afterTextChanged(Editable s) {}
            });
            
            // 搜索键盘动作监听
            etSearch.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 隐藏键盘
                    hideKeyboard();
                    return true;
                }
                return false;
            });
        }
        
        // 创建攻略按钮点击事件
        if (btnCreateStrategy != null) {
            btnCreateStrategy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createStrategy();
                }
            });
        }
        
        // 创建行程按钮点击事件
        if (btnCreateTrip != null) {
            btnCreateTrip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createTrip();
                }
            });
        }
        
        // 加号按钮点击事件
        if (btnAdd != null) {
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCreateOptions();
                }
            });
        }
    }

    private void showSearchInput() {
        // 显示搜索输入框，隐藏搜索图标
        searchContainer.setVisibility(View.GONE);
        searchInputContainer.setVisibility(View.VISIBLE);
        
        // 请求焦点并显示键盘
        etSearch.requestFocus();
        showKeyboard();
    }
    
    private void hideSearchInput() {
        // 隐藏搜索输入框，显示搜索图标
        searchInputContainer.setVisibility(View.GONE);
        searchContainer.setVisibility(View.VISIBLE);
        
        // 清空搜索文本并隐藏键盘
        etSearch.setText("");
        hideKeyboard();
        
        // 恢复原始数据
        restoreOriginalData();
    }
    
    private void performSearch(String query) {
        if (query.trim().isEmpty()) {
            // 如果搜索框为空，恢复原始数据
            restoreOriginalData();
            return;
        }
        
        // 执行搜索逻辑
        List<Strategy> filteredList = new ArrayList<>();
        for (Strategy strategy : originalStrategyList) {
            // 搜索标题、内容、目的地、用户名
            if (strategy.getTitle() != null && strategy.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                strategy.getContent() != null && strategy.getContent().toLowerCase().contains(query.toLowerCase()) ||
                strategy.getDestination() != null && strategy.getDestination().toLowerCase().contains(query.toLowerCase()) ||
                (strategy.getAuthor() != null && strategy.getAuthor().getNickname() != null && 
                 strategy.getAuthor().getNickname().toLowerCase().contains(query.toLowerCase()))) {
                filteredList.add(strategy);
            }
        }
        
        // 更新数据并刷新列表
        strategyList.clear();
        strategyList.addAll(filteredList);
        
        if (strategyAdapter != null) {
            strategyAdapter.notifyDataSetChanged();
        }
        
        // 如果没有搜索结果，显示提示
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "没有找到相关攻略", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void restoreOriginalData() {
        strategyList.clear();
        strategyList.addAll(originalStrategyList);
        
        if (strategyAdapter != null) {
            strategyAdapter.notifyDataSetChanged();
        }
    }
    
    private void createStrategy() {
        // 跳转到创建攻略页面
        Intent intent = new Intent(this, com.tour.app.strategy.CreateStrategyActivity.class);
        startActivity(intent);
    }
    
    private void createTrip() {
        // 跳转到创建行程页面
        Intent intent = new Intent(this, com.tour.app.createtrip.CreateTripActivity.class);
        startActivity(intent);
    }
    
    private void showCreateOptions() {
        // 跳转到创建行程页面
        Intent intent = new Intent(this, com.tour.app.createtrip.CreateTripActivity.class);
        startActivity(intent);
    }
    
    private void showKeyboard() {
        // 显示软键盘
        android.view.inputmethod.InputMethodManager imm = 
            (android.view.inputmethod.InputMethodManager) getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(etSearch, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT);
        }
    }
    
    private void hideKeyboard() {
        // 隐藏软键盘
        android.view.inputmethod.InputMethodManager imm = 
            (android.view.inputmethod.InputMethodManager) getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // 页面重新显示时刷新数据，确保新创建的攻略能立即显示
        refreshData();
    }
    
    /**
     * 刷新攻略数据
     */
    private void refreshData() {
        if (strategyList != null && originalStrategyList != null) {
            // 清空当前数据
            strategyList.clear();
            originalStrategyList.clear();
            
            // 重新加载数据
            initData();
            
            // 通知适配器数据已更新
            if (strategyAdapter != null) {
                strategyAdapter.notifyDataSetChanged();
            }
        }
    }
}