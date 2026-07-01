# e-tour安卓前端与后端API连接测试指南

## 概述
本指南将帮助您测试e-tour安卓前端与Spring Boot后端的连接是否成功。

## 后端配置

### 1. 启动后端服务
```bash
cd d:\tour\etour
mvn spring-boot:run
```

### 2. 验证后端是否正常运行
- 访问API文档: http://localhost:8888/api/doc.html
- 访问API根路径: http://localhost:8888/api/

## 安卓前端配置

### 1. 基础配置
- 网络基础URL: `http://10.0.2.2:8888/api/`
- 使用10.0.2.2作为Android模拟器访问本机后端的地址

### 2. 网络权限
- 已在AndroidManifest.xml中配置INTERNET权限
- 已启用明文流量传输(android:usesCleartextTraffic="true")
- 已配置网络安全策略

### 3. 测试活动
已创建`TestConnectionActivity`用于测试API连接，包含以下功能：

#### 测试按钮
- **测试基础连接**: 测试与后端的连通性
- **测试登录功能**: 使用测试账号进行登录
- **测试获取景点**: 获取景点列表数据
- **跳转到主页面**: 进入主应用界面

## 测试步骤

### 1. 启动后端
确保后端服务在8888端口正常运行。

### 2. 运行安卓应用
在Android Studio中运行e-tour应用，将自动启动`TestConnectionActivity`。

### 3. 执行测试
按顺序点击测试按钮验证连接：

1. **测试基础连接** - 验证API可达性
2. **测试登录功能** - 验证用户认证
3. **测试获取景点** - 验证数据获取

### 4. 预期结果
- 所有测试都应返回成功状态
- 能够获取到景点列表数据
- 用户登录功能正常工作

## 常见问题排查

### 1. 连接失败
- 检查后端是否运行在8888端口
- 检查防火墙设置
- 确认网络配置正确

### 2. 认证失败
- 检查用户名密码是否正确
- 验证token是否正确传递

### 3. 数据获取失败
- 检查数据库连接
- 验证API路径是否正确

## API接口映射

| 前端服务 | 后端接口 | 说明 |
|---------|----------|------|
| AuthService | /users/login | 用户登录 |
| AuthService | /users/register | 用户注册 |
| SpotService | /spots | 获取景点列表 |
| SpotService | /spots/{id} | 获取景点详情 |
| TripService | /trips | 获取行程列表 |
| StrategyService | /strategies | 获取攻略列表 |

## 技术支持
如遇到连接问题，请检查：
1. 后端服务状态
2. 网络配置
3. API路径匹配
4. 数据格式兼容性