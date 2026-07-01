# 阿里ModelScope AI接口接入指南

## 概述

本项目已成功集成阿里ModelScope平台的AI接口，支持通过DashScope API调用通义千问等大语言模型，为旅行助手应用提供智能对话功能。

## 已实现的功能

### 1. 数据模型
- **ModelScopeRequest**: API请求数据模型，包含模型配置、输入消息和参数设置
- **ModelScopeResponse**: API响应数据模型，包含AI回复内容和使用量统计

### 2. 服务类
- **ModelScopeClient**: 封装ModelScope API调用，支持异步HTTP请求和错误处理
- **AIServiceManager**: 统一AI服务管理器，支持本地服务和ModelScope服务的动态切换

### 3. 配置管理
- 在`strings.xml`中添加了ModelScope API配置参数
- 支持动态API-KEY配置和模型选择

### 4. 测试工具
- **ModelScopeTestActivity**: 专门的测试界面，支持连接测试和消息发送

## 配置步骤

### 1. 获取API-KEY
1. 访问[阿里云DashScope控制台](https://dashscope.aliyun.com/)
2. 注册账号并开通DashScope服务
3. 在控制台获取API-KEY

### 2. 配置API-KEY
在`app/src/main/res/values/strings.xml`中配置您的API-KEY：

```xml
<string name="modelscope_api_key">您的API-KEY</string>
<string name="modelscope_api_url">https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions</string>
<string name="modelscope_model">qwen-turbo</string>
```

### 3. 可选配置
- **API URL**: 可根据需要修改为其他兼容的API端点
- **模型名称**: 可切换为其他支持的模型（如qwen-plus、qwen-max等）

## 使用方法

### 1. 在代码中使用
```java
// 创建AI服务管理器
AIServiceManager aiManager = new AIServiceManager(context);

// 设置使用ModelScope服务（默认已设置）
aiManager.setAIServiceType(AIServiceManager.AIServiceType.MODEL_SCOPE);

// 发送消息获取AI回复
aiManager.getChatResponse("你好，请推荐北京的景点", new AIServiceManager.AICallback<String>() {
    @Override
    public void onSuccess(String response) {
        // 处理AI回复
    }
    
    @Override
    public void onError(String error) {
        // 处理错误
    }
});
```

### 2. 直接使用ModelScopeClient
```java
ModelScopeClient client = new ModelScopeClient(context);
client.sendMessage("用户消息", new ModelScopeClient.ModelScopeCallback() {
    @Override
    public void onSuccess(String response) {
        // 处理成功响应
    }
    
    @Override
    public void onError(String error) {
        // 处理错误
    }
});
```

## 服务切换

AIServiceManager支持两种AI服务模式：

### 1. ModelScope服务（默认）
- 使用阿里云DashScope API
- 支持通义千问等大模型
- 需要配置API-KEY

### 2. 本地AI服务
- 使用项目原有的AI推荐接口
- 基于关键词匹配和规则回复
- 无需额外配置

切换方法：
```java
// 切换到本地服务
aiManager.setAIServiceType(AIServiceManager.AIServiceType.LOCAL_SERVICE);

// 切换回ModelScope服务
aiManager.setAIServiceType(AIServiceManager.AIServiceType.MODEL_SCOPE);
```

## 错误处理

### 常见错误及解决方案

1. **API-KEY未配置**
   - 错误信息："请先在strings.xml中配置您的ModelScope API-KEY"
   - 解决方案：按照配置步骤设置正确的API-KEY

2. **网络连接失败**
   - 错误信息："网络连接失败"
   - 解决方案：检查网络连接和API端点可达性

3. **API调用失败**
   - 错误信息："API调用失败，状态码: xxx"
   - 解决方案：检查API-KEY权限和配额状态

### 降级机制

当ModelScope服务调用失败时，系统会自动降级到本地AI服务，确保应用功能的可用性。

## 测试方法

### 1. 使用测试Activity
项目已提供专门的测试界面：
- 运行应用
- 启动`ModelScopeTestActivity`
- 进行连接测试和消息发送测试

### 2. 编译测试
```bash
./gradlew.bat assembleDebug
```

## 性能优化建议

1. **连接超时设置**
   - 当前设置为30秒，可根据网络状况调整

2. **请求重试机制**
   - 可考虑添加重试逻辑处理临时网络故障

3. **响应缓存**
   - 对常见问题可添加本地缓存减少API调用

## 费用说明

使用ModelScope API会产生费用，具体计费标准请参考：
- [阿里云DashScope定价](https://help.aliyun.com/zh/dashscope/developer-reference/tongyi-thousand-questions-metering-and-billing)

## 技术支持

- 阿里云官方文档：[DashScope开发指南](https://help.aliyun.com/zh/dashscope/)
- 项目问题反馈：通过GitHub Issues提交

## 版本信息

- **当前版本**: v1.0.0
- **最后更新**: 2024年
- **兼容性**: Android 5.0+ (API 21+)

---

**注意**: 请妥善保管您的API-KEY，避免泄露。建议在正式环境中使用环境变量或安全的配置管理方式。