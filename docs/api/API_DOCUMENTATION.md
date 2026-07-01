# E-Tour API 文档

## 概述

本文档详细说明E-Tour项目的API接口设计、数据模型和使用方法。项目采用RESTful API设计风格，使用JSON格式进行数据交换。

## 基础信息

### 请求基础
- **Base URL**: `https://api.e-tour.com/v1`
- **认证方式**: Bearer Token (JWT)
- **内容类型**: `application/json`
- **字符编码**: UTF-8

### 统一响应格式
```json
{
    "code": 200,
    "message": "成功",
    "data": {}
}
```

**响应码说明:**
- `200`: 成功
- `400`: 请求参数错误
- `401`: 未授权
- `403`: 禁止访问
- `404`: 资源不存在
- `500`: 服务器内部错误

## 认证模块

### 用户注册

**接口:** `POST /auth/register`

**请求参数:**
```json
{
    "username": "string",
    "email": "string",
    "password": "string",
    "phone": "string"
}
```

**响应数据:**
```json
{
    "userId": "string",
    "username": "string",
    "email": "string",
    "createdAt": "2024-01-01T00:00:00Z",
    "avatar": "https://example.com/avatars/default.png",
    "level": 1,
    "experience": 0
}
```

### 用户登录

**接口:** `POST /auth/login`

**请求参数:**
```json
{
    "username": "string",
    "password": "string"
}
```

**响应数据:**
```json
{
    "token": "string",
    "user": {
        "userId": "string",
        "username": "string",
        "email": "string",
        "avatar": "string"
    }
}
```

### 修改密码

**接口:** `POST /auth/change-password`

**请求参数:**
```json
{
    "oldPassword": "string",
    "newPassword": "string"
}
```

## 用户模块

### 获取用户信息

**接口:** `GET /users/{userId}`

**响应数据:**
```json
{
    "userId": "string",
    "username": "string",
    "email": "string",
    "phone": "string",
    "avatar": "string",
    "bio": "string",
    "createdAt": "2024-01-01T00:00:00Z",
    "stats": {
        "tripCount": 10,
        "strategyCount": 5,
        "followerCount": 100,
        "followingCount": 50
    }
}
```

### 更新用户信息

**接口:** `PUT /users/{userId}`

**请求参数:**
```json
{
    "username": "string",
    "email": "string",
    "phone": "string",
    "avatar": "string",
    "bio": "string"
}
```

## 景点模块

### 获取景点列表

**接口:** `GET /spots`

**查询参数:**
- `page`: 页码 (默认: 1)
- `size`: 每页数量 (默认: 20)
- `city`: 城市筛选
- `category`: 分类筛选
- `keyword`: 关键词搜索

**响应数据:**
```json
{
    "spots": [
        {
            "spotId": "string",
            "name": "string",
            "city": "string",
            "category": "string",
            "image": "string",
            "rating": 4.5,
            "reviewCount": 100,
            "price": 50.0,
            "tags": ["历史", "文化", "拍照"]
        }
    ],
    "total": 100,
    "page": 1,
    "size": 20
}
```

### 获取景点详情

**接口:** `GET /spots/{spotId}`

**响应数据:**
```json
{
    "spotId": "string",
    "name": "string",
    "description": "string",
    "address": "string",
    "city": "string",
    "category": "string",
    "images": ["string"],
    "rating": 4.5,
    "reviewCount": 100,
    "price": 50.0,
    "openingHours": "09:00-17:00",
    "contact": "string",
    "tags": ["历史", "文化", "拍照"],
    "coordinates": {
        "latitude": 39.9042,
        "longitude": 116.4074
    }
}
```

## 攻略模块

### 获取攻略列表

**接口:** `GET /strategies`

**查询参数:**
- `page`: 页码 (默认: 1)
- `size`: 每页数量 (默认: 20)
- `city`: 城市筛选
- `tag`: 标签筛选
- `sort`: 排序方式 (hot, new, popular)

**响应数据:**
```json
{
    "strategies": [
        {
            "strategyId": "string",
            "title": "string",
            "author": {
                "userId": "string",
                "username": "string",
                "avatar": "string"
            },
            "city": "string",
            "coverImage": "string",
            "summary": "string",
            "tags": ["美食", "摄影", "亲子"],
            "likeCount": 100,
            "commentCount": 50,
            "createdAt": "2024-01-01T00:00:00Z",
            "readCount": 1000
        }
    ],
    "total": 100,
    "page": 1,
    "size": 20
}
```

### 创建攻略

**接口:** `POST /strategies`

**请求参数:**
```json
{
    "title": "string",
    "content": "string",
    "city": "string",
    "coverImage": "string",
    "tags": ["string"],
    "spots": ["string"]
}
```

### 获取攻略详情

**接口:** `GET /strategies/{strategyId}`

**响应数据:**
```json
{
    "strategyId": "string",
    "title": "string",
    "content": "string",
    "author": {
        "userId": "string",
        "username": "string",
        "avatar": "string"
    },
    "city": "string",
    "coverImage": "string",
    "tags": ["美食", "摄影", "亲子"],
    "spots": [
        {
            "spotId": "string",
            "name": "string",
            "image": "string"
        }
    ],
    "likeCount": 100,
    "commentCount": 50,
    "createdAt": "2024-01-01T00:00:00Z",
    "readCount": 1000
}
```

## 行程模块

### 创建行程

**接口:** `POST /trips`

**请求参数:**
```json
{
    "title": "string",
    "description": "string",
    "startDate": "2024-01-01",
    "endDate": "2024-01-07",
    "city": "string",
    "spots": [
        {
            "spotId": "string",
            "visitDate": "2024-01-01",
            "notes": "string"
        }
    ]
}
```

### 获取用户行程列表

**接口:** `GET /users/{userId}/trips`

**响应数据:**
```json
{
    "trips": [
        {
            "tripId": "string",
            "title": "string",
            "city": "string",
            "startDate": "2024-01-01",
            "endDate": "2024-01-07",
            "spotCount": 5,
            "createdAt": "2024-01-01T00:00:00Z"
        }
    ],
    "total": 10
}
```

## 社区模块

### 点赞攻略

**接口:** `POST /strategies/{strategyId}/like`

**响应数据:**
```json
{
    "likeCount": 101,
    "isLiked": true
}
```

### 取消点赞

**接口:** `DELETE /strategies/{strategyId}/like`

### 发表评论

**接口:** `POST /strategies/{strategyId}/comments`

**请求参数:**
```json
{
    "content": "string",
    "parentId": "string"
}
```

**响应数据:**
```json
{
    "commentId": "string",
    "content": "string",
    "author": {
        "userId": "string",
        "username": "string",
        "avatar": "string"
    },
    "createdAt": "2024-01-01T00:00:00Z"
}
```

## AI助手模块

### AI对话

**接口:** `POST /ai/chat`

**请求参数:**
```json
{
    "message": "string",
    "context": "string",
    "type": "travel_advice"
}
```

**响应数据:**
```json
{
    "response": "string",
    "suggestions": ["string"],
    "relatedSpots": ["string"]
}
```

### 智能推荐

**接口:** `POST /ai/recommendations`

**请求参数:**
```json
{
    "preferences": {
        "interests": ["美食", "历史"],
        "budget": "medium",
        "duration": 3,
        "companion": "family"
    },
    "location": "北京"
}
```

## 数据模型

### User 用户模型
```java
public class User {
    private String userId;
    private String username;
    private String email;
    private String phone;
    private String avatar;
    private String bio;
    private Date createdAt;
    private UserStats stats;
}

public class UserStats {
    private int tripCount;
    private int strategyCount;
    private int followerCount;
    private int followingCount;
}
```

### Spot 景点模型
```java
public class Spot {
    private String spotId;
    private String name;
    private String description;
    private String address;
    private String city;
    private String category;
    private List<String> images;
    private double rating;
    private int reviewCount;
    private double price;
    private String openingHours;
    private String contact;
    private List<String> tags;
    private Coordinates coordinates;
}

public class Coordinates {
    private double latitude;
    private double longitude;
}
```

### Strategy 攻略模型
```java
public class Strategy {
    private String strategyId;
    private String title;
    private String content;
    private User author;
    private String city;
    private String coverImage;
    private List<String> tags;
    private List<StrategySpot> spots;
    private int likeCount;
    private int commentCount;
    private Date createdAt;
    private int readCount;
}

public class StrategySpot {
    private String spotId;
    private String name;
    private String image;
}
```

## 错误处理

### 常见错误码

| 错误码 | 说明 | 解决方案 |
|--------|------|----------|
| 1001 | 用户名已存在 | 更换用户名 |
| 1002 | 邮箱已注册 | 更换邮箱或找回密码 |
| 1003 | 密码强度不足 | 使用更复杂的密码 |
| 2001 | 景点不存在 | 检查景点ID |
| 2002 | 城市不存在 | 检查城市名称 |
| 3001 | 攻略不存在 | 检查攻略ID |
| 3002 | 无权限修改 | 检查用户权限 |
| 4001 | Token过期 | 重新登录 |
| 4002 | Token无效 | 检查Token格式 |

### 错误响应示例
```json
{
    "code": 1001,
    "message": "用户名已存在",
    "data": null
}
```

## 客户端实现示例

### 网络请求封装
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
                    Request request = original.newBuilder()
                            .header("Authorization", "Bearer " + AuthManager.getToken())
                            .build();
                    return chain.proceed(request);
                }
            });
            
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

### API服务接口
```java
public interface AuthService {
    @POST("auth/login")
    Call<ApiResponse<LoginResponse>> login(@Body LoginRequest request);
    
    @POST("auth/register")
    Call<ApiResponse<RegisterResponse>> register(@Body RegisterRequest request);
}

public interface SpotService {
    @GET("spots")
    Call<ApiResponse<SpotListResponse>> getSpots(@Query("page") int page, 
                                                 @Query("size") int size,
                                                 @Query("city") String city);
}
```

## 安全规范

### 1. 认证安全
- 使用HTTPS加密传输
- JWT Token有效期设置为24小时
- 实现Token自动刷新机制

### 2. 数据安全
- 敏感信息加密存储
- 密码使用bcrypt加密
- 实现请求频率限制

### 3. 权限控制
- 基于角色的访问控制
- 数据所有权验证
- 敏感操作二次验证

---

**最后更新**: 2024年12月  
**版本**: 1.0.0