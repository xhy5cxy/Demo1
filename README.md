# 🌍 E-Tour - 智慧旅游应用系统

> 一款基于Android的智慧旅游应用，集成AI智能规划、高德地图导航、攻略分享等功能

<br />

## ✨ 功能特性

### 🤖 AI智能功能

- **智能行程规划** - 基于AI的个性化旅游路线推荐
- **自然语言交互** - 支持语音和文字查询
- **智能问答** - 提供旅游相关信息咨询

### 🗺️ 地图导航

- **高德地图集成** - 精准的地图展示和导航服务
- **实时定位** - 获取用户当前位置
- **路线规划** - 多种出行方式的路线推荐

### 📝 攻略分享

- **攻略创建** - 支持图文并茂的攻略发布
- **封面上传** - 为攻略添加精美封面图片
- **攻略浏览** - 浏览其他用户的旅游攻略

### 👤 用户服务

- **用户注册登录** - 安全的身份验证系统
- **个人中心** - 管理个人信息和收藏
- **行程管理** - 创建和管理旅游行程

## 🛠️ 技术栈

### 前端 (Android)

| 技术          | 版本   | 说明      |
| ----------- | ---- | ------- |
| Java        | 17   | 开发语言    |
| Android SDK | 34   | 目标SDK版本 |
| Retrofit    | 2.9  | 网络请求框架  |
| OkHttp      | 4.10 | HTTP客户端 |
| Gson        | 2.10 | JSON解析  |
| 高德地图SDK     | 9.5  | 地图服务    |

### 后端 (Spring Boot)

| 技术              | 版本   | 说明    |
| --------------- | ---- | ----- |
| Spring Boot     | 3.2  | 应用框架  |
| Spring Security | 6.2  | 安全框架  |
| MyBatis Plus    | 3.5  | ORM框架 |
| MySQL           | 8.0+ | 数据库   |
| Redis           | 7.0+ | 缓存    |
| JWT             | -    | 身份认证  |
| 阿里云OSS          | -    | 图片存储  |

### 前端管理后台 (Vue.js)

| 技术           | 版本  | 说明      |
| ------------ | --- | ------- |
| Vue.js       | 3.3 | 前端框架    |
| Vite         | 5.0 | 构建工具    |
| Element Plus | 2.4 | UI组件库   |
| Axios        | 1.6 | HTTP客户端 |

## 📁 项目结构

```
E-Tour/
├── admin-web/          # 前端管理后台 (Vue.js)
├── android-client/     # Android移动端应用
├── docs/               # 项目文档
│   ├── project/        # 项目概述
│   ├── architecture/   # 架构设计
│   ├── development/    # 开发指南
│   ├── api/            # API文档
│   ├── features/       # 功能说明
│   ├── troubleshooting/# 故障排查
│   └── database/       # 数据库文件
└── etour-server/       # 后端服务 (Spring Boot)
```

## 🚀 快速开始

### 环境要求

- **JDK**: 17+
- **Android Studio**: Flamingo+
- **MySQL**: 8.0+
- **Redis**: 7.0+
- **Node.js**: 18+

### 1. 配置环境

```bash
# 复制配置文件模板
cp .env.example .env

# 编辑配置文件，填入实际的配置值
vim .env
```

### 2. 数据库配置

```sql
-- 创建数据库
CREATE DATABASE etour CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 导入初始化数据
source docs/database/etour.sql;
```

### 3. 运行后端服务

```bash
cd etour-server

# 使用Maven构建
mvn clean package -DskipTests

# 运行服务
java -jar target/etour-1.0.0.jar

# 或者使用启动脚本
start.bat
```

后端服务默认运行在: <http://localhost:8888/api>

### 4. 运行前端管理后台

```bash
cd admin-web

# 安装依赖
npm install

# 开发模式运行
npm run dev

# 构建生产版本
npm run build
```

前端管理后台默认运行在: <http://localhost:5173>

### 5. 运行Android应用

1. 使用Android Studio打开 `e-tour` 目录
2. 配置SDK路径
3. 连接Android设备或启动模拟器
4. 运行应用

## 🔧 配置说明

### .env 文件配置项

| 配置项                           | 说明                 | 默认值                               |
| ----------------------------- | ------------------ | --------------------------------- |
| SERVER\_PORT                  | 后端服务端口             | 8888                              |
| DB\_URL                       | 数据库连接URL           | jdbc:mysql://localhost:3306/etour |
| DB\_USERNAME                  | 数据库用户名             | root                              |
| DB\_PASSWORD                  | 数据库密码              | -                                 |
| REDIS\_HOST                   | Redis主机            | localhost                         |
| REDIS\_PORT                   | Redis端口            | 6379                              |
| JWT\_SECRET                   | JWT密钥              | -                                 |
| ANDROID\_AMAP\_API\_KEY       | 高德地图API Key        | -                                 |
| ANDROID\_MODELSCOPE\_API\_KEY | ModelScope API Key | -                                 |

### 获取API Key

1. **高德地图API Key**: [申请地址](https://lbs.amap.com/)
2. **ModelScope API Key**: [申请地址](https://modelscope.cn/)
3. **阿里云OSS**: [申请地址](https://oss.console.aliyun.com/)

## 🔒 安全说明

### 私人配置保护

本项目使用 `.env` 文件管理敏感配置，确保：

- ✅ `.env` 文件已添加到 `.gitignore`
- ✅ 不会被上传到版本控制
- ✅ 提供 `.env.example` 模板供开发者参考

### 敏感文件清单

以下文件包含敏感信息，不会被上传：

| 文件                               | 敏感内容               |
| -------------------------------- | ------------------ |
| `.env`                           | API密钥、数据库密码、JWT密钥等 |
| `e-tour/my-release-key.keystore` | Android签名密钥        |
| `e-tour/local.properties`        | SDK路径等本地配置         |

## 📖 API文档

### 接口文档地址

- **Swagger UI**: <http://localhost:8888/api/doc.html>
- **OpenAPI**: <http://localhost:8888/api/v3/api-docs>

### 主要API模块

| 模块   | 路径            | 说明           |
| ---- | ------------- | ------------ |
| 用户认证 | `/auth`       | 登录、注册、验证码    |
| 用户管理 | `/users`      | 用户信息、头像上传    |
| 景点管理 | `/spots`      | 景点信息、搜索      |
| 攻略管理 | `/strategies` | 攻略创建、浏览、图片上传 |
| 行程管理 | `/trips`      | 行程创建、管理      |

## 🤝 贡献指南

### 贡献流程

1. Fork 本仓库
2. 创建功能分支 (`git checkout -b feature/xxx`)
3. 提交代码 (`git commit -m 'feat: add xxx'`)
4. 推送到分支 (`git push origin feature/xxx`)
5. 创建 Pull Request

### 代码规范

- **Java**: 遵循 Google Java Style Guide
- **Vue**: 遵循 Vue 官方风格指南
- **Commit Message**: 遵循 Conventional Commits

## 📄 许可证

本项目采用 MIT 许可证，详见 [LICENSE](LICENSE) 文件。

## 📮 联系方式

- **问题反馈**:2718336395\@qq.com
- **开发者**: 小徐呀

***

⭐ 如果这个项目对你有帮助，请给个Star！
