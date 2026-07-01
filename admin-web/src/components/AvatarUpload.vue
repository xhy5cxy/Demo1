<template>
  <div class="avatar-upload">
    <!-- 头像预览 -->
    <div class="avatar-preview" @click="triggerUpload">
      <el-avatar 
        :size="size" 
        :src="avatarUrl || defaultAvatar" 
        :fit="fit"
        class="avatar-image"
      >
        <template #default v-if="!avatarUrl">
          <el-icon><User /></el-icon>
        </template>
      </el-avatar>
      
      <!-- 上传遮罩 -->
      <div class="upload-overlay" v-if="!disabled">
        <el-icon><Camera /></el-icon>
        <span>点击上传</span>
      </div>
    </div>
    
    <!-- 文件上传输入框 -->
    <input
      ref="fileInput"
      type="file"
      :accept="accept"
      :multiple="false"
      @change="handleFileChange"
      style="display: none"
    />
    
    <!-- 操作按钮 -->
    <div class="upload-actions" v-if="!disabled">
      <el-button 
        size="small" 
        type="primary" 
        @click="triggerUpload"
        :loading="uploading"
      >
        <el-icon><Upload /></el-icon>
        选择图片
      </el-button>
      
      <el-button 
        size="small" 
        type="danger" 
        @click="handleRemove"
        v-if="avatarUrl"
        :disabled="uploading"
      >
        <el-icon><Delete /></el-icon>
        删除头像
      </el-button>
    </div>
    
    <!-- 上传提示 -->
    <div class="upload-tips" v-if="showTips">
      <p>支持 {{ acceptTypes.join('、') }} 格式</p>
      <p>文件大小不超过 {{ maxSizeText }}</p>
    </div>
  </div>
</template>

<script>
import { ref, computed, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { User, Camera, Upload, Delete } from '@element-plus/icons-vue'
import { uploadAvatar, deleteAvatar } from '@/api/user'

export default {
  name: 'AvatarUpload',
  components: {
    User, Camera, Upload, Delete
  },
  props: {
    // 用户ID
    userId: {
      type: [Number, String],
      required: true
    },
    // 当前头像URL
    avatarUrl: {
      type: String,
      default: ''
    },
    // 头像尺寸
    size: {
      type: Number,
      default: 120
    },
    // 图片适应方式
    fit: {
      type: String,
      default: 'cover'
    },
    // 是否禁用
    disabled: {
      type: Boolean,
      default: false
    },
    // 是否显示提示
    showTips: {
      type: Boolean,
      default: true
    },
    // 允许的文件类型
    accept: {
      type: String,
      default: 'image/jpeg,image/jpg,image/png,image/gif'
    },
    // 最大文件大小（字节）
    maxSize: {
      type: Number,
      default: 2 * 1024 * 1024 // 2MB
    }
  },
  emits: ['update:avatarUrl', 'upload-success', 'upload-error', 'remove-success'],
  setup(props, { emit }) {
    const fileInput = ref(null)
    const uploading = ref(false)
    
    // 默认头像
    const defaultAvatar = computed(() => {
      return '/images/default-avatar.png'
    })
    
    // 允许的文件类型文本
    const acceptTypes = computed(() => {
      return props.accept.split(',').map(type => {
        const match = type.match(/image\/(\w+)/)
        return match ? match[1].toUpperCase() : ''
      }).filter(Boolean)
    })
    
    // 最大文件大小文本
    const maxSizeText = computed(() => {
      const sizeMB = props.maxSize / (1024 * 1024)
      return `${sizeMB}MB`
    })
    
    // 触发文件选择
    const triggerUpload = () => {
      if (props.disabled) return
      fileInput.value?.click()
    }
    
    // 处理文件选择
    const handleFileChange = async (event) => {
      const file = event.target.files[0]
      if (!file) return
      
      // 验证文件
      if (!validateFile(file)) {
        event.target.value = '' // 清空输入
        return
      }
      
      // 上传文件
      await uploadFile(file)
      event.target.value = '' // 清空输入
    }
    
    // 验证文件
    const validateFile = (file) => {
      // 检查文件类型
      if (!props.accept.includes(file.type)) {
        ElMessage.error(`不支持的文件类型：${file.type}`)
        return false
      }
      
      // 检查文件大小
      if (file.size > props.maxSize) {
        ElMessage.error(`文件大小不能超过 ${maxSizeText.value}`)
        return false
      }
      
      return true
    }
    
    // 上传文件
    const uploadFile = async (file) => {
      uploading.value = true
      
      try {
        const formData = new FormData()
        formData.append('file', file)  // 注意：后端接口参数名是'file'，不是'avatarFile'
        
        const response = await uploadAvatar(formData)
        
        // 后端返回格式：{ code: 200, data: "头像URL", message: "成功" }
        if (response.code === 200) {
          ElMessage.success('头像上传成功')
          emit('update:avatarUrl', response.data)
          emit('upload-success', {
            avatarUrl: response.data,
            fileSize: file.size,
            fileType: file.type
          })
        } else {
          ElMessage.error(response.message || '上传失败')
          emit('upload-error', response.message)
        }
      } catch (error) {
        console.error('上传失败:', error)
        ElMessage.error('上传失败，请重试')
        emit('upload-error', error.message)
      } finally {
        uploading.value = false
      }
    }
    
    // 删除头像
    const handleRemove = async () => {
      try {
        await ElMessageBox.confirm(
          '确定要删除头像吗？删除后将使用默认头像。',
          '确认删除',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        
        uploading.value = true
        
        try {
          const response = await deleteAvatar(props.userId)
          
          if (response.success) {
            ElMessage.success('头像删除成功')
            emit('update:avatarUrl', '')
            emit('remove-success')
          } else {
            ElMessage.error(response.message || '删除失败')
          }
        } catch (error) {
          console.error('删除失败:', error)
          ElMessage.error('删除失败，请重试')
        } finally {
          uploading.value = false
        }
        
      } catch (cancel) {
        // 用户取消删除
        console.log('用户取消删除头像')
      }
    }
    
    return {
      fileInput,
      uploading,
      defaultAvatar,
      acceptTypes,
      maxSizeText,
      triggerUpload,
      handleFileChange,
      handleRemove
    }
  }
}
</script>

<style lang="scss" scoped>
.avatar-upload {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  
  .avatar-preview {
    position: relative;
    cursor: pointer;
    border-radius: 50%;
    overflow: hidden;
    transition: all 0.3s ease;
    
    &:hover {
      .upload-overlay {
        opacity: 1;
      }
      
      .avatar-image {
        transform: scale(1.05);
      }
    }
    
    .avatar-image {
      transition: transform 0.3s ease;
      border: 2px solid #f0f0f0;
    }
    
    .upload-overlay {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: rgba(0, 0, 0, 0.5);
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      color: white;
      opacity: 0;
      transition: opacity 0.3s ease;
      
      .el-icon {
        font-size: 24px;
        margin-bottom: 8px;
      }
      
      span {
        font-size: 12px;
      }
    }
  }
  
  .upload-actions {
    display: flex;
    gap: 8px;
    
    .el-button {
      display: flex;
      align-items: center;
      gap: 4px;
    }
  }
  
  .upload-tips {
    text-align: center;
    color: #909399;
    font-size: 12px;
    line-height: 1.5;
    
    p {
      margin: 4px 0;
    }
  }
}

// 禁用状态
.avatar-upload.disabled {
  .avatar-preview {
    cursor: not-allowed;
    
    &:hover {
      .upload-overlay {
        opacity: 0;
      }
      
      .avatar-image {
        transform: none;
      }
    }
  }
}
</style>