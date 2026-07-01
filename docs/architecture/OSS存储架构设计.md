# E-Tour系统阿里云OSS存储架构设计

## 1. 存储目录结构设计

### 1.1 基础目录结构
```
e-tour/
├── avatars/                 # 用户头像
│   ├── {YYYYMMDD}/          # 按日期分目录
│   └── temp/               # 临时头像
├── images/                  # 通用图片
│   ├── spot/               # 景点图片
│   ├── strategy/           # 攻略图片
│   ├── trip/               # 行程图片
│   └── temp/               # 临时图片
├── documents/              # 文档文件
│   ├── strategy/           # 攻略文档
│   └── user/               # 用户文档
├── audio/                   # 音频文件
│   └── strategy/           # 攻略音频
└── backup/                  # 备份文件
    └── {YYYYMMDD}/         # 按日期备份
```

### 1.2 文件命名规范
- **头像文件**: `avatars/{YYYYMMDD}/{user_id}_{timestamp}_{random}.{ext}`
- **景点图片**: `images/spot/{spot_id}_{sequence}_{timestamp}.{ext}`
- **攻略图片**: `images/strategy/{strategy_id}_{sequence}_{timestamp}.{ext}`
- **行程图片**: `images/trip/{trip_id}_{sequence}_{timestamp}.{ext}`

### 1.3 文件大小限制
| 文件类型 | 最大大小 | 支持格式 |
|---------|---------|---------|
| 头像图片 | 2MB | JPG, PNG, WebP |
| 普通图片 | 5MB | JPG, PNG, WebP, GIF |
| 文档文件 | 20MB | PDF, DOC, DOCX, TXT |
| 音频文件 | 10MB | MP3, WAV |

## 2. 后端架构优化

### 2.1 配置文件优化
```yaml
aliyun:
  oss:
    endpoint: ${OSS_ENDPOINT:https://oss-cn-hangzhou.aliyuncs.com}
    access-key-id: ${OSS_ACCESS_KEY_ID}
    access-key-secret: ${OSS_ACCESS_KEY_SECRET}
    bucket-name: ${OSS_BUCKET_NAME:e-tour}
    url-prefix: ${OSS_URL_PREFIX:https://e-tour.oss-cn-hangzhou.aliyuncs.com/}
    
    # 存储策略
    policy:
      image-max-size: 5242880      # 5MB
      avatar-max-size: 2097152     # 2MB
      document-max-size: 20971520  # 20MB
      audio-max-size: 10485760     # 10MB
      
    # 缓存策略
      cache-control: max-age=2592000  # 30天缓存
      content-disposition: inline
```

### 2.2 服务层扩展
新增文件类型支持：
- 图片上传服务（已存在）
- 文档上传服务
- 音频上传服务
- 文件管理服务（删除、查询）

### 2.3 安全策略
- 文件类型白名单验证
- 文件大小限制
- 病毒扫描集成
- 访问权限控制

## 3. 前端组件设计

### 3.1 统一上传组件
```vue
<template>
  <FileUpload
    :type="type"           // 文件类型：image/avatar/document/audio
    :max-size="maxSize"    // 最大文件大小
    :multiple="multiple"   // 是否多选
    @success="onSuccess"   // 上传成功回调
    @error="onError"       // 上传失败回调
  />
</template>
```

### 3.2 预览组件
- 图片预览（缩放、旋转）
- 文档预览（PDF、Word）
- 音频播放器

## 4. 性能优化策略

### 4.1 图片优化
- 自动压缩（WebP格式）
- 缩略图生成
- 懒加载实现

### 4.2 缓存策略
- CDN加速配置
- 浏览器缓存优化
- 服务端缓存

## 5. 监控与日志

### 5.1 监控指标
- 上传成功率
- 平均上传时间
- 存储空间使用率
- 流量监控

### 5.2 日志记录
- 文件上传日志
- 错误日志
- 访问日志

## 6. 备份与恢复

### 6.1 备份策略
- 每日增量备份
- 每周全量备份
- 异地备份

### 6.2 恢复流程
- 文件恢复接口
- 批量恢复工具
- 数据一致性验证

## 7. 安全考虑

### 7.1 数据安全
- 文件加密存储
- 访问权限控制
- 数据脱敏

### 7.2 网络安全
- HTTPS强制使用
- 防盗链配置
- IP白名单

## 8. 成本控制

### 8.1 存储成本
- 生命周期管理
- 冷热数据分离
- 压缩优化

### 8.2 流量成本
- CDN优化
- 缓存策略
- 压缩传输