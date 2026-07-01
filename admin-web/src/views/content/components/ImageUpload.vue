<template>
  <div class="image-upload">
    <!-- 上传按钮 -->
    <el-upload
      v-model:file-list="fileList"
      action="#"
      list-type="picture-card"
      :auto-upload="false"
      :multiple="true"
      :limit="max"
      :on-exceed="handleExceed"
      :on-change="handleChange"
      :on-remove="handleRemove"
      :before-upload="beforeUpload"
    >
      <el-icon><Plus /></el-icon>
      <div class="el-upload__text">
        点击上传<br />
        <span style="color: #909399; font-size: 12px;">最多 {{ max }} 张</span>
      </div>
    </el-upload>
    
    <!-- 批量上传对话框 -->
    <el-dialog v-model="batchDialogVisible" title="批量上传" width="600px">
      <div class="batch-upload">
        <el-upload
          ref="batchUploadRef"
          drag
          action="#"
          :multiple="true"
          :auto-upload="false"
          :file-list="batchFiles"
          :on-change="handleBatchChange"
          :before-upload="beforeBatchUpload"
        >
          <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
          <div class="el-upload__text">
            将文件拖到此处，或<em>点击上传</em>
          </div>
          <template #tip>
            <div class="el-upload__tip">
              支持 jpg、png 格式，单个文件不超过 5MB
            </div>
          </template>
        </el-upload>
        
        <div class="batch-actions" style="margin-top: 20px; text-align: center;">
          <el-button type="primary" @click="handleBatchConfirm">确认上传</el-button>
          <el-button @click="batchDialogVisible = false">取消</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'

export default {
  name: 'ImageUpload',
  props: {
    modelValue: {
      type: Array,
      default: () => []
    },
    max: {
      type: Number,
      default: 10
    }
  },
  emits: ['update:modelValue'],
  setup(props, { emit }) {
    const fileList = ref([])
    const batchDialogVisible = ref(false)
    const batchFiles = ref([])
    const batchUploadRef = ref()
    
    // 同步外部值到fileList
    watch(() => props.modelValue, (newVal) => {
      fileList.value = newVal.map((url, index) => ({
        uid: index,
        name: `image_${index}.jpg`,
        url: url
      }))
    }, { immediate: true })
    
    // 同步fileList到外部
    watch(fileList, (newVal) => {
      const urls = newVal.map(file => file.url || file.raw ? URL.createObjectURL(file.raw) : '').filter(url => url)
      emit('update:modelValue', urls)
    }, { deep: true })
    
    const handleExceed = () => {
      ElMessage.warning(`最多只能上传 ${props.max} 张图片`)
    }
    
    const handleChange = (file, fileList) => {
      // 验证文件类型和大小
      const isImage = file.raw.type.startsWith('image/')
      const isLt5M = file.raw.size / 1024 / 1024 < 5
      
      if (!isImage) {
        ElMessage.error('只能上传图片格式文件!')
        return false
      }
      if (!isLt5M) {
        ElMessage.error('图片大小不能超过 5MB!')
        return false
      }
      
      // 生成预览URL
      if (file.raw) {
        file.url = URL.createObjectURL(file.raw)
      }
    }
    
    const handleRemove = (file, fileList) => {
      // 清理URL对象
      if (file.url && file.url.startsWith('blob:')) {
        URL.revokeObjectURL(file.url)
      }
    }
    
    const beforeUpload = (file) => {
      const isImage = file.type.startsWith('image/')
      const isLt5M = file.size / 1024 / 1024 < 5
      
      if (!isImage) {
        ElMessage.error('只能上传图片格式文件!')
        return false
      }
      if (!isLt5M) {
        ElMessage.error('图片大小不能超过 5MB!')
        return false
      }
      
      return false // 手动上传
    }
    
    const handleBatchChange = (file, fileList) => {
      batchFiles.value = fileList
    }
    
    const beforeBatchUpload = (file) => {
      const isImage = file.type.startsWith('image/')
      const isLt5M = file.size / 1024 / 1024 < 5
      
      if (!isImage) {
        ElMessage.error('只能上传图片格式文件!')
        return false
      }
      if (!isLt5M) {
        ElMessage.error('图片大小不能超过 5MB!')
        return false
      }
      
      return false // 手动上传
    }
    
    const handleBatchConfirm = () => {
      if (batchFiles.value.length === 0) {
        ElMessage.warning('请选择要上传的图片')
        return
      }
      
      // 检查总数限制
      if (fileList.value.length + batchFiles.value.length > props.max) {
        ElMessage.warning(`图片总数不能超过 ${props.max} 张`)
        return
      }
      
      // 添加到文件列表
      batchFiles.value.forEach(file => {
        if (file.raw) {
          fileList.value.push({
            uid: Date.now() + Math.random(),
            name: file.name,
            raw: file.raw,
            url: URL.createObjectURL(file.raw)
          })
        }
      })
      
      batchDialogVisible.value = false
      batchFiles.value = []
      ElMessage.success('批量上传成功')
    }
    
    return {
      fileList,
      batchDialogVisible,
      batchFiles,
      batchUploadRef,
      handleExceed,
      handleChange,
      handleRemove,
      beforeUpload,
      handleBatchChange,
      beforeBatchUpload,
      handleBatchConfirm
    }
  }
}
</script>

<style lang="scss" scoped>
.image-upload {
  :deep(.el-upload-list__item) {
    transition: none !important;
  }
  
  .batch-upload {
    .el-upload-dragger {
      width: 100%;
    }
  }
}
</style>