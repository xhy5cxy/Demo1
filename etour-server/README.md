# E旅游攻略系统后端服务

基于Spring Boot + MyBatis Plus + Redis + JWT的现代化后端服务，为AI智能旅游攻略APP提供完整的RESTful API支持。

## 🚀 技术栈

- **框架**: Spring Boot 2.7.14
- **数据访问**: MyBatis Plus 3.5.3.1
- **数据库**: MySQL 8.0
- **缓存**: Redis
- **安全**: Spring Security + JWT
- **连接池**: Druid
- **工具**: Hutool、Fastjson2

## 📁 项目结构

```
etour/
├── src/main/java/com/etour/
│   ├── ETourApplication.java     # 主启动类
│   ├── config/                   # 配置类
│   │   ├── MyBatisPlusConfig.java
│   │   ├── RedisConfig.java
│   │   ├── CorsConfig.java
│   ├── controller/              # 控制器
│   │   ├── UserController.java
│   ├── service/                   # 服务层
│   │   ├── UserService.java
│   │   └── impl/
│   │       └── UserServiceImpl.java
│   ├── mapper/                    # 数据访问层
│   │   └── UserMapper.java
│   ├── entity/                    # 实体类
│   │   ├── BaseEntity.java
│   │   └── User.java
│   ├── common/                    # 公共类
│   │   ├── Result.java           # 统一响应结果
│   │   ├── GlobalExceptionHandler.java
│   │   └── ServiceException.java
│   └── utils/                     # 工具类
│       └── JwtUtils.java
├── src/main/resources/
│   ├── application.yml           # 主配置文件
│   ├── application-dev.yml       # 开发环境配置
│   ├── application-prod.yml      # 生产环境配置
│   ├── db/schema.sql             # 数据库初始化脚本
│   └── mapper/                   # MyBatis映射文件
├── pom.xml                       # Maven配置文件
└── README.md                     # 项目说明文档
```

## 🛠️ 快速开始

### 1. 环境要求

- JDK 1.8+
- MySQL 8.0+
- Redis 3.2+
- Maven 3.6+

### 2. 数据库配置

```bash
# 创建数据库
mysql -u root -p < src/main/resources/db/schema.sql
```

### 3. 修改配置文件

编辑 `src/main/resources/application-dev.yml` 文件：

```yaml
spring:
  datasource:
    druid:
      url: jdbc:mysql://localhost:3306/etour?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
      username: your_username
      password: your_password
  redis:
    host: localhost
    port: 6379
    password: your_redis_password
```

### 4. 启动项目

```bash
# 开发环境启动
mvn spring-boot:run -Dspring.profiles.active=dev

# 或者使用IDE直接运行 ETourApplication.java
```

项目启动后访问：
- API文档：http://localhost:8888/api/doc.html
- 应用接口：http://localhost:8888/api

## 📋 API接口文档

### 用户相关接口

#### 1. 用户注册
```http
POST /api/user/register
Content-Type: application/json

{
    "username": "testuser",
    "password": "123456",
    "nickname": "测试用户",
    "email": "test@example.com"
}
```

#### 2. 用户登录
```http
POST /api/user/login
Content-Type: application/x-www-form-urlencoded

username=testuser&password=123456
```

#### 3. 获取用户信息
```http
GET /api/user/info?userId=1
Authorization: Bearer your_jwt_token
```

#### 4. 修改密码
```http
POST /api/user/change-password
Content-Type: application/x-www-form-urlencoded
Authorization: Bearer your_jwt_token

userId=1&oldPassword=123456&newPassword=654321
```

## 🔐 安全设计

### JWT认证
- 使用JWT进行用户身份认证
- 令牌有效期：24小时
- 支持令牌刷新机制

### 密码安全
- 使用BCrypt加密算法
- 密码强度校验
- 防止SQL注入和XSS攻击

## 🎯 AI服务集成

系统预留了AI服务接口配置：
- 智能行程生成
- 路线优化
- 智能问答

配置文件：
```yaml
ai:
  service:
    aliyun-recommend-url: https://ai.aliyuncs.com/api/recommend
    gaode-route-url: https://restapi.amap.com/v3/direction/walking
    tencent-nlp-url: https://nlp.tencentcloudapi.com/
```

## 📊 监控和日志

### 日志配置
- 使用Logback日志框架
- 支持文件和控制台输出
- 按天滚动日志文件

### 健康检查
- Spring Boot Actuator
- 数据库连接检查
- Redis连接检查

## 🚀 部署说明

### 开发环境
```bash
mvn clean package -DskipTests
java -jar -Dspring.profiles.active=dev target/etour-backend-1.0.0.jar
```

### 生产环境
```bash
mvn clean package -DskipTests
java -jar -Dspring.profiles.active=prod target/etour-backend-1.0.0.jar
```

### Docker部署
```dockerfile
FROM openjdk:8-jre-slim
COPY target/etour-backend-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]
```

## 📞 技术支持

如有问题请联系开发团队：
- Email: dev@etour.com
- Issues: [提交Issue](https://github.com/your-repo/issues) 

## 📄 许可证

MIT License - 详见 [LICENSE](LICENSE) 文件