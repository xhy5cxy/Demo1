<template>
  <div class="scenic-management">
    <!-- 操作按钮 -->
    <div class="action-bar">
      <el-button type="primary" @click="handleAddScenic">
        <el-icon><Plus /></el-icon>
        新增景点
      </el-button>
      <el-button @click="handleBatchUpload">
        <el-icon><Upload /></el-icon>
        批量上传图片
      </el-button>
    </div>
    
    <!-- 景点列表 -->
    <el-card>
      <el-table :data="scenicList" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="name" label="景点名称" min-width="150" />
        <el-table-column prop="location" label="位置" width="120" />
        <el-table-column prop="category" label="分类" width="100">
          <template #default="{ row }">
            <el-tag>{{ row.category }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="imageCount" label="图片数" width="80" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 'published' ? 'success' : 'warning'">
              {{ row.status === 'published' ? '已发布' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" width="150" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 景点编辑对话框 -->
    <el-dialog
      v-model="editDialogVisible"
      :title="isEdit ? '编辑景点' : '新增景点'"
      width="80%"
      top="5vh"
    >
      <scenic-editor
        :scenic="currentScenic"
        :is-edit="isEdit"
        @save="handleSave"
        @cancel="editDialogVisible = false"
      />
    </el-dialog>
    
    <!-- 批量上传对话框 -->
    <el-dialog
      v-model="uploadDialogVisible"
      title="批量上传图片"
      width="600px"
    >
      <batch-upload @success="handleUploadSuccess" />
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import ScenicEditor from './ScenicEditor.vue'
import BatchUpload from './BatchUpload.vue'

export default {
  name: 'ScenicManagement',
  components: {
    ScenicEditor,
    BatchUpload
  },
  setup() {
    const loading = ref(false)
    const editDialogVisible = ref(false)
    const uploadDialogVisible = ref(false)
    const isEdit = ref(false)
    const currentScenic = ref({})
    
    const scenicList = ref([
      {
        id: 1,
        name: '长城',
        location: '北京',
        category: '历史古迹',
        imageCount: 15,
        status: 'published',
        updateTime: '2024-01-15 10:30:22'
      },
      {
        id: 2,
        name: '西湖',
        location: '杭州',
        category: '自然风光',
        imageCount: 23,
        status: 'published',
        updateTime: '2024-01-14 14:20:15'
      },
      {
        id: 3,
        name: '故宫',
        location: '北京',
        category: '历史古迹',
        imageCount: 18,
        status: 'draft',
        updateTime: '2024-01-13 16:45:30'
      }
    ])
    
    const handleAddScenic = () => {
      isEdit.value = false
      currentScenic.value = {
        name: '',
        location: '',
        category: '',
        description: '',
        images: [],
        status: 'draft'
      }
      editDialogVisible.value = true
    }
    
    const handleEdit = (scenic) => {
      isEdit.value = true
      currentScenic.value = { ...scenic }
      editDialogVisible.value = true
    }
    
    const handleDelete = async (scenic) => {
      try {
        await ElMessageBox.confirm(
          `确定要删除景点 "${scenic.name}" 吗？`,
          '删除确认',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        
        // 模拟删除操作
        scenicList.value = scenicList.value.filter(item => item.id !== scenic.id)
        ElMessage.success('删除成功')
      } catch (error) {
        // 用户取消操作
      }
    }
    
    const handleBatchUpload = () => {
      uploadDialogVisible.value = true
    }
    
    const handleSave = (scenicData) => {
      if (isEdit.value) {
        // 更新现有景点
        const index = scenicList.value.findIndex(item => item.id === scenicData.id)
        if (index !== -1) {
          scenicList.value[index] = {
            ...scenicList.value[index],
            ...scenicData,
            updateTime: new Date().toLocaleString('zh-CN')
          }
        }
      } else {
        // 新增景点
        scenicList.value.push({
          id: scenicList.value.length + 1,
          ...scenicData,
          imageCount: scenicData.images?.length || 0,
          updateTime: new Date().toLocaleString('zh-CN')
        })
      }
      
      editDialogVisible.value = false
      ElMessage.success(isEdit.value ? '更新成功' : '新增成功')
    }
    
    const handleUploadSuccess = () => {
      uploadDialogVisible.value = false
      ElMessage.success('图片上传成功')
    }
    
    onMounted(() => {
      // 加载景点列表
      loading.value = true
      setTimeout(() => {
        loading.value = false
      }, 500)
    })
    
    return {
      loading,
      scenicList,
      editDialogVisible,
      uploadDialogVisible,
      isEdit,
      currentScenic,
      handleAddScenic,
      handleEdit,
      handleDelete,
      handleBatchUpload,
      handleSave,
      handleUploadSuccess
    }
  }
}
</script>

<style lang="scss" scoped>
.scenic-management {
  .action-bar {
    margin-bottom: 20px;
    
    .el-button {
      margin-right: 10px;
    }
  }
}
</style>