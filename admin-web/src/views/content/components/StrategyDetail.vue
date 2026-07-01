<template>
  <div class="strategy-detail">
    <el-descriptions :column="2" border style="margin-bottom: 20px;">
      <el-descriptions-item label="攻略标题">{{ strategy.title }}</el-descriptions-item>
      <el-descriptions-item label="作者">{{ strategy.author }}</el-descriptions-item>
      <el-descriptions-item label="关联景点">{{ strategy.scenicName }}</el-descriptions-item>
      <el-descriptions-item label="提交时间">{{ strategy.submitTime }}</el-descriptions-item>
      <el-descriptions-item label="审核状态">
        <el-tag :type="getStatusType(strategy.status)">
          {{ getStatusText(strategy.status) }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="拒绝理由" v-if="strategy.status === 'rejected'">
        {{ strategy.rejectReason || '无' }}
      </el-descriptions-item>
    </el-descriptions>
    
    <el-card>
      <template #header>
        <span>攻略内容</span>
      </template>
      <div class="strategy-content" v-html="strategy.content"></div>
    </el-card>
    
    <div class="detail-actions" style="margin-top: 20px; text-align: center;">
      <el-button @click="$emit('close')">关闭</el-button>
    </div>
  </div>
</template>

<script>
import { defineProps } from 'vue'

export default {
  name: 'StrategyDetail',
  props: {
    strategy: {
      type: Object,
      required: true
    }
  },
  emits: ['close'],
  setup(props) {
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
    
    return {
      getStatusType,
      getStatusText
    }
  }
}
</script>

<style lang="scss" scoped>
.strategy-detail {
  .strategy-content {
    min-height: 300px;
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
    
    :deep(img) {
      max-width: 100%;
      height: auto;
      border-radius: 4px;
    }
  }
}
</style>