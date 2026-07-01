<template>
  <div class="strategy-review">
    <!-- 筛选条件 -->
    <el-card class="filter-card">
      <el-form :model="filterForm" :inline="true">
        <el-form-item label="审核状态">
          <el-select v-model="filterForm.status" placeholder="请选择状态">
            <el-option label="待审核" value="pending" />
            <el-option label="已通过" value="approved" />
            <el-option label="已拒绝" value="rejected" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="提交时间">
          <el-date-picker
            v-model="filterForm.submitTime"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <!-- 攻略列表 -->
    <el-card>
      <el-table :data="strategyList" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="title" label="标题" min-width="200" />
        <el-table-column prop="author" label="作者" width="120" />
        <el-table-column prop="scenicName" label="关联景点" width="150" />
        <el-table-column prop="submitTime" label="提交时间" width="150" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleReview(row)" v-if="row.status === 'pending'">
              审核
            </el-button>
            <el-button size="small" @click="handleView(row)">查看</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 审核对话框 -->
    <el-dialog
      v-model="reviewDialogVisible"
      title="攻略审核"
      width="70%"
      top="5vh"
    >
      <strategy-review-detail
        :strategy="currentStrategy"
        @approve="handleApprove"
        @reject="handleReject"
        @cancel="reviewDialogVisible = false"
      />
    </el-dialog>
    
    <!-- 查看对话框 -->
    <el-dialog
      v-model="viewDialogVisible"
      :title="currentStrategy.title"
      width="70%"
      top="5vh"
    >
      <strategy-detail :strategy="currentStrategy" />
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import StrategyReviewDetail from './StrategyReviewDetail.vue'
import StrategyDetail from './StrategyDetail.vue'

export default {
  name: 'StrategyReview',
  components: {
    StrategyReviewDetail,
    StrategyDetail
  },
  setup() {
    const loading = ref(false)
    const reviewDialogVisible = ref(false)
    const viewDialogVisible = ref(false)
    const currentStrategy = ref({})
    
    const filterForm = reactive({
      status: 'pending',
      submitTime: []
    })
    
    const strategyList = ref([
      {
        id: 1,
        title: '北京三日游完美攻略',
        author: '旅行达人',
        scenicName: '故宫、长城',
        content: '这是一篇详细的北京三日游攻略...',
        submitTime: '2024-01-15 14:30:22',
        status: 'pending',
        rejectReason: ''
      },
      {
        id: 2,
        title: '杭州西湖美食指南',
        author: '美食探索家',
        scenicName: '西湖',
        content: '西湖周边美食推荐...',
        submitTime: '2024-01-14 10:20:15',
        status: 'approved',
        rejectReason: ''
      },
      {
        id: 3,
        title: '西安古城文化之旅',
        author: '历史爱好者',
        scenicName: '兵马俑、大雁塔',
        content: '西安历史文化深度游...',
        submitTime: '2024-01-13 16:45:30',
        status: 'rejected',
        rejectReason: '内容质量不符合要求'
      }
    ])
    
    const getStatusType = (status) => {
      const types = {
        pending: 'warning',
        approved: 'success',
        rejected: 'danger'
      }
      return types[status] || 'info'
    }
    
    const getStatusText = (status) => {
      const texts = {
        pending: '待审核',
        approved: '已通过',
        rejected: '已拒绝'
      }
      return texts[status] || '未知'
    }
    
    const handleSearch = () => {
      loadStrategyList()
    }
    
    const handleReset = () => {
      filterForm.status = 'pending'
      filterForm.submitTime = []
      loadStrategyList()
    }
    
    const handleReview = (strategy) => {
      currentStrategy.value = strategy
      reviewDialogVisible.value = true
    }
    
    const handleView = (strategy) => {
      currentStrategy.value = strategy
      viewDialogVisible.value = true
    }
    
    const handleDelete = async (strategy) => {
      try {
        await ElMessageBox.confirm(
          `确定要删除攻略 "${strategy.title}" 吗？`,
          '删除确认',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        
        strategyList.value = strategyList.value.filter(item => item.id !== strategy.id)
        ElMessage.success('删除成功')
      } catch (error) {
        // 用户取消操作
      }
    }
    
    const handleApprove = (strategyId) => {
      const strategy = strategyList.value.find(item => item.id === strategyId)
      if (strategy) {
        strategy.status = 'approved'
        strategy.rejectReason = ''
        ElMessage.success('审核通过')
      }
      reviewDialogVisible.value = false
    }
    
    const handleReject = (data) => {
      const strategy = strategyList.value.find(item => item.id === data.id)
      if (strategy) {
        strategy.status = 'rejected'
        strategy.rejectReason = data.reason
        ElMessage.success('已拒绝该攻略')
      }
      reviewDialogVisible.value = false
    }
    
    const loadStrategyList = () => {
      loading.value = true
      // 模拟API调用
      setTimeout(() => {
        loading.value = false
      }, 500)
    }
    
    onMounted(() => {
      loadStrategyList()
    })
    
    return {
      loading,
      filterForm,
      strategyList,
      reviewDialogVisible,
      viewDialogVisible,
      currentStrategy,
      getStatusType,
      getStatusText,
      handleSearch,
      handleReset,
      handleReview,
      handleView,
      handleDelete,
      handleApprove,
      handleReject
    }
  }
}
</script>

<style lang="scss" scoped>
.strategy-review {
  .filter-card {
    margin-bottom: 20px;
    
    :deep(.el-card__body) {
      padding-bottom: 10px;
    }
  }
}
</style>