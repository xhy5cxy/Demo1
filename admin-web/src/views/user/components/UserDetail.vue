<template>
  <div class="user-detail">
    <!-- 头像和基本信息 -->
    <div class="user-basic-info">
      <div class="avatar-section">
        <el-avatar 
          :size="80" 
          :src="user.avatar" 
          fit="cover"
          class="user-avatar"
        >
          <template #default v-if="!user.avatar">
            <el-icon size="30"><User /></el-icon>
          </template>
        </el-avatar>
        <div class="avatar-actions" v-if="user.avatar">
          <el-button size="small" @click="viewAvatar">查看大图</el-button>
        </div>
      </div>
      <div class="basic-info">
        <h3>{{ user.nickname || user.username }}</h3>
        <p class="user-id">用户ID: {{ user.id }}</p>
        <p class="user-status">
          <el-tag :type="user.status === 'normal' ? 'success' : 'danger'">
            {{ user.status === 'normal' ? '正常' : user.status === 'violation' ? '违规' : '封禁' }}
          </el-tag>
        </p>
      </div>
    </div>
    
    <!-- 详细信息 -->
    <el-descriptions title="用户信息" :column="2" border>
      <el-descriptions-item label="用户名">{{ user.username }}</el-descriptions-item>
      <el-descriptions-item label="昵称">{{ user.nickname }}</el-descriptions-item>
      <el-descriptions-item label="邮箱">{{ user.email }}</el-descriptions-item>
      <el-descriptions-item label="注册时间">{{ user.registerTime }}</el-descriptions-item>
      <el-descriptions-item label="最后登录">{{ user.lastLogin }}</el-descriptions-item>
      <el-descriptions-item label="活跃度">
        <el-tag :type="getActivityType(user.activityLevel)">
          {{ getActivityText(user.activityLevel) }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="内容数量">{{ user.contentCount }}</el-descriptions-item>
      <el-descriptions-item label="头像状态">
        <el-tag :type="user.avatar ? 'success' : 'info'">
          {{ user.avatar ? '已设置' : '未设置' }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="头像URL" :span="2">
        <div class="avatar-url">
          <span class="url-text" :title="user.avatar || '暂无头像'">
            {{ user.avatar || '暂无头像' }}
          </span>
          <el-button 
            v-if="user.avatar" 
            size="small" 
            type="primary" 
            link
            @click="copyAvatarUrl"
          >
            复制链接
          </el-button>
        </div>
      </el-descriptions-item>
    </el-descriptions>
    
    <!-- 头像预览对话框 -->
    <el-dialog
      v-model="avatarPreviewVisible"
      title="头像预览"
      width="400px"
      align-center
    >
      <div class="avatar-preview-dialog">
        <img :src="user.avatar" alt="用户头像" class="preview-image" />
        <div class="preview-actions">
          <el-button type="primary" @click="downloadAvatar">下载头像</el-button>
          <el-button @click="avatarPreviewVisible = false">关闭</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { User } from '@element-plus/icons-vue'

export default {
  name: 'UserDetail',
  components: {
    User
  },
  props: {
    user: {
      type: Object,
      required: true
    }
  },
  setup(props) {
    const avatarPreviewVisible = ref(false)
    
    const getActivityType = (level) => {
      const types = {
        high: 'success',
        medium: 'warning',
        low: 'info',
        inactive: 'danger'
      }
      return types[level] || 'info'
    }
    
    const getActivityText = (level) => {
      const texts = {
        high: '高活跃',
        medium: '中活跃',
        low: '低活跃',
        inactive: '不活跃'
      }
      return texts[level] || '未知'
    }
    
    // 查看头像大图
    const viewAvatar = () => {
      if (props.user.avatar) {
        avatarPreviewVisible.value = true
      } else {
        ElMessage.info('该用户暂无头像')
      }
    }
    
    // 复制头像URL
    const copyAvatarUrl = async () => {
      if (!props.user.avatar) {
        ElMessage.info('该用户暂无头像')
        return
      }
      
      try {
        await navigator.clipboard.writeText(props.user.avatar)
        ElMessage.success('头像链接已复制到剪贴板')
      } catch (error) {
        console.error('复制失败:', error)
        ElMessage.error('复制失败，请手动复制')
      }
    }
    
    // 下载头像
    const downloadAvatar = () => {
      if (!props.user.avatar) return
      
      const link = document.createElement('a')
      link.href = props.user.avatar
      link.download = `avatar_${props.user.id}_${Date.now()}.${props.user.avatar.split('.').pop()}`
      link.click()
    }
    
    return {
      avatarPreviewVisible,
      getActivityType,
      getActivityText,
      viewAvatar,
      copyAvatarUrl,
      downloadAvatar
    }
  }
}
</script>

<style lang="scss" scoped>
.user-detail {
  .user-basic-info {
    display: flex;
    align-items: center;
    margin-bottom: 24px;
    padding: 16px;
    background: #f5f7fa;
    border-radius: 8px;
    
    .avatar-section {
      margin-right: 16px;
      text-align: center;
      
      .user-avatar {
        border: 3px solid #fff;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
      }
      
      .avatar-actions {
        margin-top: 8px;
      }
    }
    
    .basic-info {
      h3 {
        margin: 0 0 8px 0;
        font-size: 20px;
        color: #303133;
      }
      
      .user-id {
        margin: 0 0 8px 0;
        color: #606266;
        font-size: 14px;
      }
      
      .user-status {
        margin: 0;
      }
    }
  }
  
  .avatar-url {
    display: flex;
    align-items: center;
    justify-content: space-between;
    
    .url-text {
      flex: 1;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      color: #606266;
    }
  }
  
  .avatar-preview-dialog {
    text-align: center;
    
    .preview-image {
      max-width: 100%;
      max-height: 300px;
      border-radius: 8px;
      margin-bottom: 16px;
    }
    
    .preview-actions {
      display: flex;
      justify-content: center;
      gap: 12px;
    }
  }
}

:deep(.el-descriptions) {
  margin-top: 16px;
  
  .el-descriptions__title {
    font-size: 16px;
    font-weight: 600;
    color: #303133;
  }
  
  .el-descriptions__label {
    font-weight: 500;
    color: #606266;
  }
  
  .el-descriptions__content {
    color: #303133;
  }
}
</style>