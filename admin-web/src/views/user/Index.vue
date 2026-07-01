<template>
  <div class="user-management">
    <div class="page-header">
      <h2>用户管理</h2>
      <p>管理平台用户账号，支持筛选和批量操作</p>
    </div>
    
    <!-- 筛选条件 -->
    <el-card class="filter-card">
      <el-form :model="filterForm" ref="filterFormRef" :inline="true">
        <el-form-item label="注册时间">
          <el-date-picker
            v-model="filterForm.registerTime"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        
        <el-form-item label="活跃度">
          <el-select v-model="filterForm.activityLevel" placeholder="请选择活跃度">
            <el-option label="高活跃" value="high" />
            <el-option label="中活跃" value="medium" />
            <el-option label="低活跃" value="low" />
            <el-option label="不活跃" value="inactive" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="账号状态">
          <el-select v-model="filterForm.status" placeholder="请选择状态">
            <el-option label="正常" value="normal" />
            <el-option label="违规" value="violation" />
            <el-option label="封禁" value="banned" />
          </el-select>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <!-- 操作按钮 -->
    <div class="action-bar">
      <el-button type="primary" @click="handleExport">导出数据</el-button>
      <el-button type="danger" @click="handleBatchBan" :disabled="selectedUsers.length === 0">
        批量封禁
      </el-button>
      <el-button @click="handleBatchUnban" :disabled="selectedUsers.length === 0">
        批量解封
      </el-button>
    </div>
    
    <!-- 用户列表 -->
    <el-card>
      <el-table
        :data="userList"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="用户ID" width="80" />
        <!-- 添加头像列 -->
        <el-table-column label="头像" width="100">
          <template #default="{ row }">
            <el-avatar 
              :size="40" 
              :src="row.avatar" 
              fit="cover"
              @click="handleAvatarClick(row)"
              style="cursor: pointer;"
            >
              <template #default v-if="!row.avatar">
                <el-icon><User /></el-icon>
              </template>
            </el-avatar>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="nickname" label="昵称" width="120" />
        <el-table-column prop="email" label="邮箱" min-width="150" />
        <el-table-column prop="registerTime" label="注册时间" width="150" />
        <el-table-column prop="lastLogin" label="最后登录" width="150" />
        <el-table-column prop="activityLevel" label="活跃度" width="100">
          <template #default="{ row }">
            <el-tag :type="getActivityType(row.activityLevel)">
              {{ getActivityText(row.activityLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="contentCount" label="内容数" width="80" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 'normal' ? 'success' : 'danger'">
              {{ row.status === 'normal' ? '正常' : row.status === 'violation' ? '违规' : '封禁' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleView(row)">查看</el-button>
            <el-button size="small" @click="handleUploadAvatar(row)">上传头像</el-button>
            <el-button
              size="small"
              :type="row.status === 'banned' ? 'success' : 'danger'"
              @click="handleToggleStatus(row)"
            >
              {{ row.status === 'banned' ? '解封' : '封禁' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
    
    <!-- 用户详情对话框 -->
    <el-dialog
      v-model="detailVisible"
      title="用户详情"
      width="600px"
    >
      <user-detail :user="currentUser" v-if="detailVisible" />
    </el-dialog>
    
    <!-- 头像上传对话框 -->
    <el-dialog
      v-model="avatarUploadVisible"
      :title="'上传头像 - ' + currentUser.nickname"
      width="500px"
    >
      <avatar-upload
        :userId="currentUser.id"
        :avatar-url="currentUser.avatar"
        @update:avatar-url="handleAvatarUpdate"
        @upload-success="handleUploadSuccess"
        @upload-error="handleUploadError"
        @remove-success="handleRemoveSuccess"
      />
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { User } from '@element-plus/icons-vue'
import UserDetail from './components/UserDetail.vue'
import AvatarUpload from '@/components/AvatarUpload.vue'

export default {
  name: 'UserManagement',
  components: {
    UserDetail,
    AvatarUpload,
    User
  },
  setup() {
    const filterFormRef = ref()
    const loading = ref(false)
    const detailVisible = ref(false)
    const avatarUploadVisible = ref(false)
    const selectedUsers = ref([])
    const currentUser = ref({})
    
    const filterForm = reactive({
      registerTime: [],
      activityLevel: '',
      status: ''
    })
    
    const pagination = reactive({
      current: 1,
      size: 10,
      total: 0
    })
    
    const userList = ref([
      {
        id: 10001,
        username: 'user001',
        nickname: '旅行达人',
        email: 'user001@example.com',
        avatar: '',
        registerTime: '2024-01-10 14:30:22',
        lastLogin: '2024-01-15 09:15:33',
        activityLevel: 'high',
        contentCount: 45,
        status: 'normal'
      },
      {
        id: 10002,
        username: 'user002',
        nickname: '美食探索家',
        email: 'user002@example.com',
        avatar: '',
        registerTime: '2024-01-08 10:20:15',
        lastLogin: '2024-01-14 16:45:22',
        activityLevel: 'medium',
        contentCount: 23,
        status: 'normal'
      },
      {
        id: 10003,
        username: 'user003',
        nickname: '风景摄影师',
        email: 'user003@example.com',
        avatar: '',
        registerTime: '2024-01-05 08:45:30',
        lastLogin: '2024-01-12 11:30:18',
        activityLevel: 'low',
        contentCount: 12,
        status: 'violation'
      },
      {
        id: 10004,
        username: 'user004',
        nickname: '冒险家',
        email: 'user004@example.com',
        avatar: '',
        registerTime: '2024-01-03 16:20:45',
        lastLogin: '2024-01-10 14:22:11',
        activityLevel: 'inactive',
        contentCount: 5,
        status: 'banned'
      }
    ])
    
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
    
    // 点击头像
    const handleAvatarClick = (user) => {
      if (user.avatar) {
        // 如果有头像，可以放大预览
        ElMessage.info(`查看用户 ${user.nickname} 的头像`)
      }
    }
    
    // 上传头像
    const handleUploadAvatar = (user) => {
      currentUser.value = user
      avatarUploadVisible.value = true
    }
    
    // 头像更新
    const handleAvatarUpdate = (newAvatarUrl) => {
      const userIndex = userList.value.findIndex(user => user.id === currentUser.value.id)
      if (userIndex !== -1) {
        userList.value[userIndex].avatar = newAvatarUrl
        currentUser.value.avatar = newAvatarUrl
      }
    }
    
    // 上传成功
    const handleUploadSuccess = (data) => {
      ElMessage.success(`头像上传成功，文件大小：${(data.fileSize / 1024).toFixed(2)}KB`)
      avatarUploadVisible.value = false
    }
    
    // 上传失败
    const handleUploadError = (error) => {
      console.error('头像上传失败:', error)
    }
    
    // 删除成功
    const handleRemoveSuccess = () => {
      ElMessage.success('头像删除成功')
      avatarUploadVisible.value = false
    }
    
    const handleSearch = () => {
      pagination.current = 1
      loadUserList()
    }
    
    const handleReset = () => {
      filterFormRef.value?.resetFields()
      pagination.current = 1
      loadUserList()
    }
    
    const handleSelectionChange = (selection) => {
      selectedUsers.value = selection
    }
    
    const handleView = (user) => {
      currentUser.value = user
      detailVisible.value = true
    }
    
    const handleToggleStatus = async (user) => {
      const action = user.status === 'banned' ? '解封' : '封禁'
      try {
        await ElMessageBox.confirm(
          `确定要${action}用户 "${user.nickname}" 吗？`,
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        
        // 模拟API调用
        user.status = user.status === 'banned' ? 'normal' : 'banned'
        ElMessage.success(`${action}成功`)
      } catch (error) {
        // 用户取消操作
      }
    }
    
    const handleBatchBan = async () => {
      try {
        await ElMessageBox.confirm(
          `确定要封禁选中的 ${selectedUsers.value.length} 个用户吗？`,
          '批量操作',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        
        selectedUsers.value.forEach(user => {
          user.status = 'banned'
        })
        ElMessage.success('批量封禁成功')
        selectedUsers.value = []
      } catch (error) {
        // 用户取消操作
      }
    }
    
    const handleBatchUnban = async () => {
      try {
        await ElMessageBox.confirm(
          `确定要解封选中的 ${selectedUsers.value.length} 个用户吗？`,
          '批量操作',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        
        selectedUsers.value.forEach(user => {
          user.status = 'normal'
        })
        ElMessage.success('批量解封成功')
        selectedUsers.value = []
      } catch (error) {
        // 用户取消操作
      }
    }
    
    const handleExport = () => {
      ElMessage.success('导出功能开发中...')
    }
    
    const handleSizeChange = (size) => {
      pagination.size = size
      loadUserList()
    }
    
    const handleCurrentChange = (current) => {
      pagination.current = current
      loadUserList()
    }
    
    const loadUserList = () => {
      loading.value = true
      // 模拟API调用
      setTimeout(() => {
        pagination.total = userList.value.length
        loading.value = false
      }, 500)
    }
    
    onMounted(() => {
      loadUserList()
    })
    
    return {
      filterFormRef,
      filterForm,
      userList,
      loading,
      pagination,
      detailVisible,
      avatarUploadVisible,
      currentUser,
      selectedUsers,
      getActivityType,
      getActivityText,
      handleAvatarClick,
      handleUploadAvatar,
      handleAvatarUpdate,
      handleUploadSuccess,
      handleUploadError,
      handleRemoveSuccess,
      handleSearch,
      handleReset,
      handleSelectionChange,
      handleView,
      handleToggleStatus,
      handleBatchBan,
      handleBatchUnban,
      handleExport,
      handleSizeChange,
      handleCurrentChange
    }
  }
}
</script>

<style lang="scss" scoped>
.user-management {
  .page-header {
    margin-bottom: 24px;
    
    h2 {
      margin: 0 0 8px 0;
      font-size: 24px;
      color: #303133;
    }
    
    p {
      margin: 0;
      color: #909399;
      font-size: 14px;
    }
  }
  
  .filter-card {
    margin-bottom: 20px;
    
    :deep(.el-card__body) {
      padding-bottom: 10px;
    }
  }
  
  .action-bar {
    margin-bottom: 20px;
    
    .el-button {
      margin-right: 10px;
    }
  }
  
  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
}
</style>