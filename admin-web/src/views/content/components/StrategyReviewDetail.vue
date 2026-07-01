<template>
  <div class="strategy-review-detail">
    <el-descriptions :column="2" border style="margin-bottom: 20px;">
      <el-descriptions-item label="攻略标题">{{ strategy.title }}</el-descriptions-item>
      <el-descriptions-item label="作者">{{ strategy.author }}</el-descriptions-item>
      <el-descriptions-item label="关联景点">{{ strategy.scenicName }}</el-descriptions-item>
      <el-descriptions-item label="提交时间">{{ strategy.submitTime }}</el-descriptions-item>
    </el-descriptions>
    
    <el-card>
      <template #header>
        <span>攻略内容</span>
      </template>
      <div class="strategy-content" v-html="strategy.content"></div>
    </el-card>
    
    <el-card style="margin-top: 20px;">
      <template #header>
        <span>审核操作</span>
      </template>
      <el-form ref="reviewFormRef" :model="reviewForm" :rules="reviewRules">
        <el-form-item label="审核意见" prop="reviewComment">
          <el-input
            v-model="reviewForm.reviewComment"
            type="textarea"
            :rows="3"
            placeholder="请输入审核意见（可选）"
          />
        </el-form-item>
        
        <el-form-item label="拒绝理由" prop="rejectReason" v-if="showRejectReason">
          <el-input
            v-model="reviewForm.rejectReason"
            type="textarea"
            :rows="3"
            placeholder="请填写拒绝理由"
          />
        </el-form-item>
        
        <div class="review-actions">
          <el-button type="success" @click="handleApprove">通过审核</el-button>
          <el-button type="danger" @click="showRejectReason = true" v-if="!showRejectReason">
            拒绝
          </el-button>
          <el-button type="danger" @click="handleReject" v-else>确认拒绝</el-button>
          <el-button @click="$emit('cancel')">取消</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, watch } from 'vue'
import { ElMessage } from 'element-plus'

export default {
  name: 'StrategyReviewDetail',
  props: {
    strategy: {
      type: Object,
      required: true
    }
  },
  emits: ['approve', 'reject', 'cancel'],
  setup(props, { emit }) {
    const reviewFormRef = ref()
    const showRejectReason = ref(false)
    
    const reviewForm = reactive({
      reviewComment: '',
      rejectReason: ''
    })
    
    const reviewRules = {
      rejectReason: [
        { required: true, message: '请填写拒绝理由', trigger: 'blur' }
      ]
    }
    
    const handleApprove = async () => {
      try {
        emit('approve', props.strategy.id)
        ElMessage.success('审核通过')
      } catch (error) {
        ElMessage.error('操作失败')
      }
    }
    
    const handleReject = async () => {
      if (!reviewFormRef.value) return
      
      const valid = await reviewFormRef.value.validate()
      if (!valid) return
      
      try {
        emit('reject', {
          id: props.strategy.id,
          reason: reviewForm.rejectReason
        })
        ElMessage.success('已拒绝该攻略')
      } catch (error) {
        ElMessage.error('操作失败')
      }
    }
    
    // 重置表单状态
    watch(() => props.strategy, () => {
      showRejectReason.value = false
      reviewForm.reviewComment = ''
      reviewForm.rejectReason = ''
    }, { immediate: true })
    
    return {
      reviewFormRef,
      reviewForm,
      reviewRules,
      showRejectReason,
      handleApprove,
      handleReject
    }
  }
}
</script>

<style lang="scss" scoped>
.strategy-review-detail {
  .strategy-content {
    min-height: 200px;
    line-height: 1.6;
    
    :deep(*) {
      margin-bottom: 16px;
    }
    
    :deep(h1), :deep(h2), :deep(h3) {
      margin-top: 24px;
      margin-bottom: 16px;
    }
    
    :deep(ul), :deep(ol) {
      padding-left: 24px;
    }
    
    :deep(blockquote) {
      border-left: 4px solid #409EFF;
      padding-left: 16px;
      margin-left: 0;
      background-color: #f5f7fa;
      padding: 16px;
    }
  }
  
  .review-actions {
    text-align: center;
    margin-top: 20px;
    
    .el-button {
      margin: 0 10px;
    }
  }
}
</style>