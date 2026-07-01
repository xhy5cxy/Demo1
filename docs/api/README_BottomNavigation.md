# 底部导航栏使用说明

## 概述
本项目创建了一个可复用的底部导航栏组件，基于MainActivity的底部导航栏设计，可以在多个Activity中使用。底部导航栏包含4个按钮：旅行、探索、AI助手、我的。

## 文件结构

### 布局文件
- `layout_bottom_navigation.xml` - 底部导航栏布局
- `activity_with_bottom_nav.xml` - 使用底部导航栏的Activity布局示例

### Java类
- `BottomNavigationManager.java` - 底部导航栏管理类
- `BaseActivity.java` - 基础Activity类，提供底部导航栏功能
- `SampleActivity.java` - 使用示例

## 使用方法

### 方法1：继承BaseActivity（推荐）

```java
public class YourActivity extends BaseActivity {
    
    @Override
    protected int getLayoutId() {
        return R.layout.your_activity_layout;
    }
    
    @Override
    protected void initViews() {
        // 初始化视图
    }
    
    @Override
    protected void initData() {
        // 初始化数据
    }
    
    @Override
    protected void setCurrentNavigationItem() {
        // 设置当前选中的导航项
        if (bottomNavigationManager != null) {
            bottomNavigationManager.setSelectedItem(R.id.btn_travel);
        }
    }
}
```

### 方法2：手动使用BottomNavigationManager

```java
public class YourActivity extends AppCompatActivity {
    
    private BottomNavigationManager bottomNavigationManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.your_activity_layout);
        
        // 初始化底部导航栏
        View rootView = findViewById(android.R.id.content);
        bottomNavigationManager = new BottomNavigationManager(this, rootView);
        
        // 设置选中状态
        bottomNavigationManager.setSelectedItem(R.id.btn_explore);
    }
}
```

## 布局文件要求

你的Activity布局文件需要包含底部导航栏的布局：

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout 
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <!-- 主要内容区域 -->
    <LinearLayout
        android:id="@+id/content_container"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toTopOf="@id/bottom_nav_container"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent">
        
        <!-- 你的内容 -->
        
    </LinearLayout>

    <!-- 底部导航栏 -->
    <include
        android:id="@+id/bottom_nav_container"
        layout="@layout/layout_bottom_navigation"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"/>

</androidx.constraintlayout.widget.ConstraintLayout>
```

## 功能特性

### 自动导航
- 点击旅行按钮：跳转到MainActivity
- 点击探索按钮：跳转到CommunityActivity
- 点击AI助手按钮：跳转到AImainActivity
- 点击我的按钮：预留个人中心页面

### 选中状态管理
- 支持设置当前选中的导航项
- 选中状态会高亮显示（蓝色）
- 未选中状态为灰色

### 可见性控制
- 可以控制底部导航栏的显示/隐藏

## API说明

### BottomNavigationManager类

```java
// 设置选中项
public void setSelectedItem(int selectedItem)

// 设置底部导航栏可见性
public void setBottomNavigationVisible(boolean visible)

// 获取底部导航栏高度
public int getBottomNavigationHeight()

// 获取底部导航栏视图
public View getBottomNavigationView()
```

### BaseActivity类

```java
// 获取底部导航栏管理器
public BottomNavigationManager getBottomNavigationManager()

// 设置底部导航栏可见性
protected void setBottomNavigationVisible(boolean visible)
```

## 导航项ID

- `R.id.btn_travel` - 旅行按钮
- `R.id.btn_explore` - 探索按钮
- `R.id.btn_ai` - AI助手按钮
- `R.id.btn_me` - 我的按钮

## 注意事项

1. 确保在AndroidManifest.xml中注册了所有相关的Activity
2. 底部导航栏使用了Material Design组件，确保添加了相关依赖
3. 选中状态通过文字颜色变化来显示（蓝色为选中，灰色为未选中）
4. 每个按钮都有对应的图标和文字

## 扩展功能

如果需要添加新的导航项或修改跳转逻辑，可以：

1. 修改`BottomNavigationManager.java`中的`initListeners()`方法
2. 在布局文件中添加新的按钮
3. 更新颜色资源文件
4. 添加新的Activity跳转逻辑

## 示例

MainActivity已经修改为继承BaseActivity，并设置旅行按钮为选中状态。其他Activity可以参考SampleActivity的实现方式。 