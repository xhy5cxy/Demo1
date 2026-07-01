<template>
  <div class="batch-upload">
    <el-upload
      ref="uploadRef"
      drag
      action="#"
      :multiple="true"
      :auto-upload="false"
      :file-list="fileList"
      :on-change="handleChange"
      :before-upload="beforeUpload"
      :on-remove="handleRemove"
    >
      <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
      <div class="el-upload__text">
        将文件拖到此处，或<em>点击上传</em>
      </div>
      <template #tip>
        <div class="el-upload__tip">
          支持 jpg、png 格式，单个文件不超过 5MB，最多可上传 50 张图片
        </div>
      </template>
    </el-upload>
    
    <!-- 上传进度 -->
    <div v-if="uploading" class="upload-progress">
      <el-progress
        :percentage="uploadProgress"
        :status="uploadStatus"
        :stroke-width="8"
      />
      <div class="progress-info">
        <span>已上传: {{ uploadedCount }}/{{ totalCount }}</span>
        <el-button size="small" @click="handleCancelUpload" v-if="uploading">取消上传</el-button>
      </div>
    </div>
    
    <!-- 上传结果 -->
    <div v-if="uploadResult.length > 0" class="upload-result">
      <h4>上传结果：</h4>
      <el-table :data="uploadResult" size="small" style="width: 100%">
        <el-table-column prop="name" label="文件名" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'success' ? 'success' : 'danger'">
              {{ row.status === 'success' ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="message" label="信息" />
      </el-table>
    </div>
    
    <div class="upload-actions" style="margin-top: 20px; text-align: center;">
      <el-button type="primary" @click="handleUpload" :disabled="fileList.length === 0 || uploading">
        开始上传
      </el-button>
      <el-button @click="handleClear">清空列表</el-button>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

export default {
  name: 'BatchUpload',
  emits: ['success'],
  setup(props, { emit }) {
    const uploadRef = ref()
    const fileList = ref([])
    const uploading = ref(false)
    const uploadProgress = ref(0)
    const uploadStatus = ref('')
    const uploadedCount = ref(0)
    const totalCount = ref(0)
    
    const uploadResult = ref([])
    
    const handleChange = (file, files) => {
      fileList.value = files
    }
    
    const handleRemove = (file, files) => {
      fileList.value = files
    }
    
    const beforeUpload = (file) => {
      const isImage = file.type.startsWith('image/')
      const isLt5M = file.size / 1024 / 1024 < 5
      
      if (!isImage) {
        ElMessage.error(`文件 ${file.name} 不是图片格式!`)
        return false
      }
      if (!isLt5M) {
        ElMessage.error(`文件 ${file.name} 大小超过 5MB!`)
        return false
      }
      
      return false // 手动上传
    }
    
    const handleUpload = async () => {
      if (fileList.value.length === 0) {
        ElMessage.warning('请选择要上传的图片')
        return
      }
      
      uploading.value = true
      uploadProgress.value = 0
      uploadedCount.value = 0
      totalCount.value = fileList.value.length
      uploadResult.value = []
      
      // 模拟上传过程
      for (let i = 0; i < fileList.value.length; i++) {
        const file = fileList.value[i]
        
        try {
          // 模拟上传延迟
          await new Promise(resolve => setTimeout(resolve, 1000))
          
          // 模拟上传成功
          uploadResult.value.push({
            name: file.name,
            status: 'success',
            message: '上传成功'
          })
          
          uploadedCount.value = i + 1
          uploadProgress.value = Math.round(((i + 1) / totalCount.value) * 100)
          
        } catch (error) {
          uploadResult.value.push({
            name: file.name,
            status: 'error',
            message: '上传失败: ' + error.message
          })
        }
      }
      
      uploading.value = false
      uploadStatus.value = 'success'
      
      // 统计结果
      const successCount = uploadResult.value.filter(r => r.status === 'success').length
      if (successCount > 0) {
        ElMessage.success(`成功上传 ${successCount} 张图片`)
        emit('success')
      }
      
      if (successCount < totalCount.value) {
        ElMessage.warning(`有 ${totalCount.value - successCount} 张图片上传失败`)
      }
    }
    
    const handleCancelUpload = () => {
      uploading.value = false
      uploadStatus.value = 'exception'
      ElMessage.info('上传已取消')
    }
    
    const handleClear = () => {
      fileList.value = []
      uploadResult.value = []
      uploadProgress.value = 0
      uploadedCount.value = 0
      totalCount.value = 0
    }
    
    return {
      uploadRef,
      fileList,
      uploading,
      uploadProgress,
      uploadStatus,
      uploadedCount,
      totalCount,
      uploadResult,
      handleChange,
      handleRemove,
      beforeUpload,
      handleUpload,
      handleCancelUpload,
      handleClear
    }
  }
}
</script>

<style lang="scss" scoped>
.batch-upload {
  .upload-progress {
    margin-top: 20px;
    
    .progress-info {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: 10px;
      font-size: 14px;
      color: #606266;
    }
  }
  
  .upload-result {
    margin-top: 20px;
    
    h4 {
      margin: 0 0 10px 0;
      font-size: 16px;
      color: #303133;
    }
  }
  
  .upload-actions {
    .el-button {
      margin: 0 10px;
    }
  }
}
</style>