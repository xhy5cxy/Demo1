# ============================================
# E-Tour 项目文件结构说明
# ============================================

## 项目概述

E-Tour 是一个旅游应用系统，包含以下三个主要模块：
- **admin-web** - 前端管理后台（Vue.js）
- **e-tour** - Android移动端应用
- **etour** - 后端服务（Spring Boot）

## 目录结构

```
d:\tour/
├── .env                    # 私人配置文件（不上传）
├── .env.example            # 配置文件模板
├── .gitignore              # Git忽略规则
├── admin-web/              # 前端管理后台
├── docs/                   # 文档目录
├── e-tour/                 # Android移动端应用
└── etour/                  # 后端服务
```

## docs/ - 文档目录

### project/ - 项目概述文档
| 文件 | 说明 |
|------|------|
| E-Tour项目综合说明文档.md | 项目整体说明文档 |

### architecture/ - 架构设计文档
| 文件 | 说明 |
|------|------|
| E-Tour系统架构图.md | 系统整体架构设计 |
| OSS存储架构设计.md | 阿里云OSS存储设计 |
| 数据库ER图设计.md | 数据库实体关系图设计 |

### development/ - 开发指南文档
| 文件 | 说明 |
|------|------|
| 后端服务使用指南.md | 后端服务部署和使用指南 |
| 前后端连接完成总结.md | 前后端对接总结 |
| 中文输入问题诊断与解决方案.md | 中文输入问题处理 |

### api/ - API文档
| 文件 | 说明 |
|------|------|
| ANDROID_API_INTEGRATION.md | Android API集成说明 |
| API_DOCUMENTATION.md | API接口文档 |
| API连接测试指南.md | API连接测试说明 |
| CODE_STRUCTURE.md | 代码结构说明 |
| DEVELOPMENT_GUIDE.md | 开发指南 |
| MODELSCOPE_README.md | ModelScope集成说明 |
| PROJECT_SUMMARY.md | 项目总结 |
| README_BottomNavigation.md | 底部导航说明 |

### features/ - 功能说明文档
| 文件 | 说明 |
|------|------|
| 图片上传功能说明.md | 图片上传功能文档 |
| 高德地图接入指南.md | 高德地图集成指南 |

### troubleshooting/ - 故障排查文档
| 文件 | 说明 |
|------|------|
| 网络错误解决指南.md | 网络问题排查指南 |

### database/ - 数据库相关文件
| 文件 | 说明 |
|------|------|
| etour.sql | 数据库初始化SQL脚本 |
| 数据库ER图-PlantUML.puml | ER图PlantUML代码 |
| 数据库表结构图表.html | 表结构可视化图表 |
| 数据库表结构详细文档.md | 表结构详细说明 |

## admin-web/ - 前端管理后台

```
admin-web/
├── src/
│   ├── api/              # API接口定义
│   ├── components/       # 公共组件
│   ├── layout/           # 布局组件
│   ├── router/           # 路由配置
│   ├── styles/           # 样式文件
│   ├── utils/            # 工具函数
│   └── views/            # 页面组件
├── index.html
├── package.json
└── vite.config.js
```

## e-tour/ - Android移动端应用

```
e-tour/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/tour/app/
│   │   │   │   ├── ai/              # AI功能模块
│   │   │   │   ├── common/          # 公共基类
│   │   │   │   ├── config/          # 配置管理
│   │   │   │   ├── createtrip/      # 创建行程
│   │   │   │   ├── data/            # 数据管理
│   │   │   │   ├── di/              # 依赖注入
│   │   │   │   ├── location/        # 定位服务
│   │   │   │   ├── me/              # 个人中心
│   │   │   │   ├── model/           # 数据模型
│   │   │   │   ├── navigation/      # 导航服务
│   │   │   │   ├── network/         # 网络请求
│   │   │   │   ├── payment/         # 支付模块
│   │   │   │   ├── strategy/        # 攻略模块
│   │   │   │   ├── test/            # 测试模块
│   │   │   │   ├── utils/           # 工具类
│   │   │   │   ├── App.java
│   │   │   │   └── MainActivity.java
│   │   │   └── res/                 # 资源文件
│   │   ├── androidTest/             # 集成测试
│   │   └── test/                    # 单元测试
│   └── build.gradle.kts
├── gradle/                          # Gradle配置
├── image/                           # 图片资源
├── svg/                             # SVG资源
├── build.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
├── my-release-key.keystore          # 签名密钥
├── README.md
└── settings.gradle.kts
```

## etour/ - 后端服务

```
etour/
├── src/main/java/com/etour/
│   ├── common/          # 公共组件
│   ├── config/          # 配置类
│   ├── controller/      # 控制器
│   ├── dto/             # 数据传输对象
│   ├── entity/          # 实体类
│   ├── mapper/          # MyBatis映射器
│   ├── service/         # 业务逻辑层
│   ├── utils/           # 工具类
│   └── ETourApplication.java
├── src/main/resources/
│   ├── mapper/          # MyBatis XML映射文件
│   ├── application.yml  # 应用配置
│   ├── application-dev.yml
│   └── application-prod.yml
├── src/test/            # 测试代码
├── README.md
├── pom.xml
└── start.bat            # 启动脚本
```

## 已删除的文件

以下测试和临时文件已被清理：
- TestOSSConnection.java
- avatar-viewer.html
- check_network.bat
- network_test.html
- simple-avatar-viewer.html
- simple-upload-test.html
- small-test.jpg
- test-*.html (多个测试HTML文件)
- test.jpg, test-image.jpg
- test_admin_password.java/.class
- test_password.java/.class
- test_phone_connection.bat
- init_data.py
- insert_data.py

## 配置文件说明

### 私人配置（不上传GitHub）
- `.env` - 包含敏感信息：API密钥、数据库密码等

### 配置模板（上传GitHub）
- `.env.example` - 配置文件模板，供开发者参考

### 应用配置
- `e-tour/app/src/main/res/values/strings.xml` - Android资源配置
- `etour/src/main/resources/application.yml` - 后端配置

## 开源注意事项

1. **配置文件**：确保 `.env` 文件不上传，使用 `.env.example` 模板
2. **密钥文件**：`my-release-key.keystore` 包含签名密钥，不上传
3. **敏感信息**：所有API密钥、密码等都应存储在 `.env` 文件中
4. **数据库**：SQL脚本存储在 `docs/database/` 目录中，可以上传
