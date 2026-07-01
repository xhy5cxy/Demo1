# E-Tour 开发指南

## 项目架构说明

### MVVM架构模式
本项目采用标准的MVVM（Model-View-ViewModel）架构模式：

```
View (Activity/Fragment) ←→ ViewModel ←→ Model (Repository/Service)
```

**各层职责：**
- **View层**: 负责UI展示和用户交互，不包含业务逻辑
- **ViewModel层**: 处理业务逻辑，管理UI状态，与Model层通信
- **Model层**: 数据源管理，包括网络请求、数据库操作等

### 依赖注入框架
项目使用Dagger2进行依赖注入，提高代码的可测试性和可维护性。

## 核心组件详解

### 1. 底部导航系统

#### 实现原理
底部导航通过`BottomNavigationManager`类统一管理：

```java
public class BottomNavigationManager {
    private Activity activity;
    private BottomNavigationView bottomNavigationView;
    
    // 初始化导航栏
    public void init(Activity activity, BottomNavigationView navView) {
        this.activity = activity;
        this.bottomNavigationView = navView;
        setupNavigation();
    }
    
    // 设置导航监听
    private void setupNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // 跳转到首页
                    break;
                case R.id.navigation_community:
                    // 跳转到社区
                    break;
                // ... 其他导航项
            }
            return true;
        });
    }
}
```

#### 使用方式
1. **继承BaseActivity**: 自动获得底部导航功能
2. **手动管理**: 在Activity中手动初始化`BottomNavigationManager`

### 2. 网络请求封装

#### API客户端配置
```java
public class ApiClient {
    private static Retrofit retrofit = null;
    
    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            
            // 添加认证拦截器
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    
                    // 添加认证头
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", "Bearer " + AuthManager.getToken());
                    
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
            
            // 添加日志拦截器
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                httpClient.addInterceptor(logging);
            }
            
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }
}
```

#### 统一响应处理
```java
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;
    
    // 响应状态判断
    public boolean isSuccess() {
        return code == 200;
    }
}
```

### 3. UI组件规范

#### 颜色定义
在`colors.xml`中定义统一的颜色方案：
```xml
<resources>
    <color name="primary_color">#6200EE</color>
    <color name="primary_dark">#3700B3</color>
    <color name="accent_color">#03DAC5</color>
    <color name="text_primary">#212121</color>
    <color name="text_secondary">#757575</color>
</resources>
```

#### 布局规范
1. **使用ConstraintLayout**作为根布局
2. **统一边距**: 使用`16dp`作为标准边距
3. **圆角设计**: 统一使用`12dp`圆角
4. **阴影效果**: 使用`elevation`属性添加阴影

### 4. 底部导航栏实现

```java
public class BottomNavigationManager {
    private Activity activity;
    private BottomNavigationView bottomNavigationView;
    
    public BottomNavigationManager(Activity activity, BottomNavigationView bottomNavigationView) {
        this.activity = activity;
        this.bottomNavigationView = bottomNavigationView;
        setupNavigation();
    }
    
    private void setupNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(activity, MainActivity.class));
                    return true;
                case R.id.navigation_community:
                    startActivity(new Intent(activity, CommunityActivity.class));
                    return true;
                case R.id.navigation_ai:
                    startActivity(new Intent(activity, AIMainActivity.class));
                    return true;
                case R.id.navigation_myself:
                    startActivity(new Intent(activity, MyselfActivity.class));
                    return true;
            }
            return false;
        });
    }
}
```

#### 瀑布流布局实现（社区页面）
```java
public class CommunityActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private StrategyAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        
        // 初始化RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        
        // 设置瀑布流布局（2列）
        StaggeredGridLayoutManager layoutManager = 
            new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        
        // 设置适配器
        adapter = new StrategyAdapter(this, getStrategyData());
        recyclerView.setAdapter(adapter);
        
        // 设置点击事件
        setupClickListeners();
    }
    
    private List<Strategy> getStrategyData() {
        // 从StrategyDataManager获取数据
        return StrategyDataManager.getInstance().getStrategies();
    }
}
```

#### AI助手集成（ModelScope）
```java
public class AIService {
    private static final String MODEL_NAME = "qwen/Qwen2.5-1.5B-Instruct";
    
    public static void sendMessage(String message, AICallback callback) {
        // 初始化ModelScope客户端
        ModelScopeClient client = new ModelScopeClient.Builder()
                .model(MODEL_NAME)
                .build();
        
        // 构建对话请求
        ChatRequest request = new ChatRequest.Builder()
                .message(message)
                .maxTokens(500)
                .temperature(0.7)
                .build();
        
        // 异步执行请求
        client.chatAsync(request, new ChatCallback() {
            @Override
            public void onSuccess(ChatResponse response) {
                callback.onSuccess(response.getText());
            }
            
            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }
}
```

## 功能模块开发指南

### 1. 添加新功能模块

#### 步骤1: 创建包结构
```
app/src/main/java/com/tour/app/
└── newmodule/
    ├── ui/
    │   ├── NewModuleActivity.java
    │   └── fragments/
    ├── viewmodel/
    │   └── NewModuleViewModel.java
    └── model/
        └── NewModuleRepository.java
```

#### 步骤2: 创建Activity
```java
public class NewModuleActivity extends BaseActivity {
    private NewModuleViewModel viewModel;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_module);
        
        // 初始化ViewModel
        viewModel = new ViewModelProvider(this).get(NewModuleViewModel.class);
        
        setupUI();
        observeViewModel();
    }
    
    private void setupUI() {
        // 设置UI组件
    }
    
    private void observeViewModel() {
        // 观察ViewModel数据变化
        viewModel.getData().observe(this, data -> {
            // 更新UI
        });
    }
}
```

#### 步骤3: 创建布局文件
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    
    <!-- 使用Material Design组件 -->
    <com.google.android.material.card.MaterialCardView
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        android:layout_margin="16dp"
        app:cardCornerRadius="12dp"
        app:cardElevation="4dp">
        
        <!-- 卡片内容 -->
        
    </com.google.android.material.card.MaterialCardView>
    
</androidx.constraintlayout.widget.ConstraintLayout>
```

### 2. 网络请求开发

#### 创建API服务接口
```java
public interface NewModuleService {
    @GET("api/newmodule/data")
    Call<ApiResponse<List<NewData>>> getNewData();
    
    @POST("api/newmodule/create")
    Call<ApiResponse<NewData>> createNewData(@Body NewDataRequest request);
}
```

#### 在ViewModel中处理网络请求
```java
public class NewModuleViewModel extends BaseViewModel {
    private final NewModuleRepository repository;
    private final MutableLiveData<List<NewData>> data = new MutableLiveData<>();
    
    public NewModuleViewModel() {
        this.repository = new NewModuleRepository();
    }
    
    public void loadData() {
        repository.getNewData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.isSuccess()) {
                                data.setValue(response.getData());
                            } else {
                                // 处理错误
                            }
                        },
                        error -> {
                            // 处理网络错误
                        }
                );
    }
}
```

## 测试指南

### 单元测试
```java
@RunWith(MockitoJUnitRunner.class)
public class NewModuleViewModelTest {
    
    @Mock
    private NewModuleRepository repository;
    
    private NewModuleViewModel viewModel;
    
    @Before
    public void setup() {
        viewModel = new NewModuleViewModel(repository);
    }
    
    @Test
    public void testLoadData_Success() {
        // 模拟成功响应
        when(repository.getNewData()).thenReturn(Single.just(
                new ApiResponse<>(200, "成功", Arrays.asList(new NewData()))
        ));
        
        // 执行测试
        viewModel.loadData();
        
        // 验证结果
        // ...
    }
}
```

### UI测试
```java
@RunWith(AndroidJUnit4.class)
public class NewModuleActivityTest {
    
    @Rule
    public ActivityScenarioRule<NewModuleActivity> activityRule =
            new ActivityScenarioRule<>(NewModuleActivity.class);
    
    @Test
    public void testUIElementsDisplayed() {
        onView(withId(R.id.title)).check(matches(isDisplayed()));
        onView(withId(R.id.button)).check(matches(isDisplayed()));
    }
}
```

## 性能优化建议

### 1. 内存优化
- 使用`WeakReference`避免内存泄漏
- 及时取消网络请求和订阅
- 优化图片加载，使用合适的图片尺寸

### 2. 网络优化
- 实现请求缓存机制
- 使用连接池复用HTTP连接
- 压缩请求数据

### 3. UI优化
- 使用`RecyclerView`替代`ListView`
- 实现视图复用
- 避免过度绘制

## 常见问题解决

### 1. 网络连接问题
- 检查网络权限配置
- 验证API地址是否正确
- 检查SSL证书配置

### 2. 依赖冲突解决
- 使用`./gradlew app:dependencies`查看依赖树
- 排除冲突的依赖版本
- 使用`resolutionStrategy`统一版本

### 3. 布局问题
- 使用`ConstraintLayout`避免嵌套过深
- 验证布局在不同屏幕尺寸的显示效果
- 使用`tools:`命名空间进行预览

## 代码审查要点

### 1. 架构规范
- [ ] 是否遵循MVVM架构
- [ ] View层是否不包含业务逻辑
- [ ] 是否使用依赖注入

### 2. 代码质量
- [ ] 命名规范是否统一
- [ ] 是否有适当的注释
- [ ] 错误处理是否完善

### 3. 性能考虑
- [ ] 是否有内存泄漏风险
- [ ] 网络请求是否合理
- [ ] UI渲染是否高效

---

**最后更新**: 2024年12月  
**版本**: 1.0.0