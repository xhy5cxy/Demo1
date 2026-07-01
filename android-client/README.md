# E-Tour 智能旅游攻略APP

## 项目概述

E-Tour是一款基于Android平台的智能旅游攻略应用，集成了AI助手功能，为用户提供个性化的旅游规划、攻略分享、景点推荐等服务。

## 项目特色

- 🎯 **智能AI助手** - 集成ModelScope AI大模型，提供智能旅游建议和对话功能
- 📱 **现代化UI设计** - Material Design风格，卡片式布局，圆角阴影效果
- 🔐 **完整用户系统** - 注册、登录、密码找回、JWT认证管理
- 🗺️ **旅游规划** - 行程创建、景点管理、攻略分享
- 👥 **社区互动** - 瀑布流攻略展示、搜索功能、用户交流
- 🎨 **精美UI组件** - 自定义底部导航、渐变背景、图标资源
- 🔧 **模块化架构** - MVVM + Dagger2依赖注入，代码结构清晰

## 技术架构

### 前端技术栈

- **开发语言**: Java
- **目标平台**: Android (minSdk 31, targetSdk 35)
- **架构模式**: MVVM (Model-View-ViewModel)
- **UI框架**: Material Design Components

### 核心依赖

- **网络请求**: Retrofit2 + OkHttp3
- **图片加载**: Glide
- **响应式编程**: RxJava3
- **依赖注入**: Dagger2
- **导航组件**: Android Navigation
- **数据绑定**: LiveData + ViewModel

## 项目结构

```
app/src/main/java/com/tour/app/
├── MainActivity.java              # 主页面
├── ai/                            # AI助手模块
│   ├── AImainActivity.java         # AI助手主页面
│   ├── service/                   # AI服务
│   └── viewmodel/                 # AI视图模型
├── auth/                          # 认证模块
│   ├── ui/                        # 认证界面
│   └── viewmodel/                 # 认证视图模型
├── common/                        # 公共组件
│   ├── BaseActivity.java          # 基础Activity
│   └── BottomNavigationManager.java # 底部导航管理
├── community/                     # 社区模块
│   ├── adapter/                   # 适配器
│   └── ui/                        # 社区界面
├── me/                            # 个人中心
│   ├── MyselfActivity.java        # 我的页面
│   └── SettingsActivity.java      # 设置页面
├── network/                       # 网络层
│   ├── ApiClient.java             # API客户端
│   ├── AuthService.java           # 认证服务
│   └── StrategyService.java       # 攻略服务
└── utils/                         # 工具类
    ├── AuthManager.java           # 认证管理
    └── JWTUtils.java              # JWT工具
```

## 功能模块

### 1. 用户认证系统

- **登录页面** (`activity_login.xml`) - 用户登录界面，支持用户名/密码验证
- **注册页面** (`activity_register.xml`) - 用户注册界面，包含表单验证
- **忘记密码** (`activity_forgot_password.xml`) - 密码找回功能
- **认证管理** (`AuthManager.java`) - JWT Token管理和自动刷新

### 2. 主功能模块

- **旅行之路** (`activity_main.xml`) - 主页面，展示推荐内容和旅行统计
- **攻略广场** (`activity_community.xml`) - 瀑布流布局攻略展示，支持搜索功能
- **AI助手** (`activity_aimain.xml`) - 集成ModelScope AI的智能对话助手
- **我的页面** (`activity_myself.xml`) - 个人中心，包含用户信息、服务菜单和设置

### 3. 辅助功能

- **创建行程** (`activity_create_trip.xml`) - 旅游行程规划和景点管理
- **创建攻略** (`activity_create_strategy.xml`) - 攻略发布和内容编辑
- **支付页面** (`activity_payment.xml`) - 支付功能集成
- **设置页面** (`activity_settings.xml`) - 应用设置和个性化配置

### 4. 核心组件

- **底部导航** (`BottomNavigationManager.java`) - 自定义底部导航栏管理
- **数据管理** (`StrategyDataManager.java`) - 攻略数据管理和缓存
- **网络请求** (`ApiClient.java`) - Retrofit2网络请求封装
- **依赖注入** (`AppComponent.java`) - Dagger2依赖注入配置

## UI设计特色

### 现代化设计风格

- **渐变背景** - 使用渐变色背景提升视觉体验
- **Material Design** - 遵循Material Design设计规范
- **卡片式布局** - 信息卡片化展示，层次分明
- **圆角设计** - 统一的圆角设计语言

### 交互体验优化

- **底部导航** - 统一的底部导航栏设计
- **状态管理** - 清晰的选中状态指示
- **动画效果** - 流畅的页面切换动画
- **响应式布局** - 适配不同屏幕尺寸

## 核心功能实现

### 底部导航系统

项目实现了可复用的底部导航栏组件，支持：

- 多页面统一导航
- 选中状态管理
- 自动页面跳转
- 高度自定义

### AI助手集成

- 集成AI大模型服务
- 智能对话界面
- 旅游建议生成
- 实时交互体验

### 网络请求封装

- Retrofit2 + OkHttp3网络框架
- 统一的API响应处理
- 错误处理机制
- Token自动管理

## 快速开始

### 环境要求

- Android Studio Arctic Fox 或更高版本
- JDK 17
- Android SDK 31+

### 构建步骤

1. 克隆项目到本地
2. 使用Android Studio打开项目
3. 同步Gradle依赖
4. 连接Android设备或启动模拟器
5. 运行应用

### 配置说明

- 修改`network_security_config.xml`配置网络安全性
- 配置后端API地址
- 设置AI服务密钥

## 开发指南

### 添加新页面

1. 创建新的Activity类继承`BaseActivity`
2. 创建对应的布局文件
3. 在`AndroidManifest.xml`中注册Activity
4. 在底部导航管理器中添加跳转逻辑

### 网络请求示例

```java
// 创建API服务实例
AuthService authService = ApiClient.getClient().create(AuthService.class);

// 发起网络请求
Call<ApiResponse<User>> call = authService.login(loginRequest);
call.enqueue(new Callback<ApiResponse<User>>() {
    @Override
    public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
        // 处理响应
    }
    
    @Override
    public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
        // 处理错误
    }
});
```

### UI组件使用

项目提供了丰富的UI组件，包括：

- 自定义按钮样式
- 输入框验证
- 加载状态指示
- 错误提示组件

## 项目文档

### 相关文档

- [底部导航栏使用说明](README_BottomNavigation.md)
- [API连接测试指南](API连接测试指南.md)
- [ModelScope集成说明](MODELSCOPE_README.md)

### 开发规范

- 遵循Android开发最佳实践
- 使用MVVM架构模式
- 代码注释规范
- 资源命名规范

## 贡献指南

欢迎提交Issue和Pull Request来改进项目。在提交代码前请确保：

1. 代码符合项目编码规范
2. 添加必要的单元测试
3. 更新相关文档
4. 通过代码审查

## 许可证

本项目采用MIT许可证，详情请查看LICENSE文件。

## 联系方式

如有问题或建议，请通过以下方式联系：

- 提交GitHub Issue
- 发送邮件至2718336395\@qq.com

***

