<template>
  <div class="content-management">
    <div class="page-header">
      <h2>内容管理</h2>
      <p>管理景点信息、用户攻略，支持富文本编辑和批量操作</p>
    </div>
    
    <!-- 标签页切换 -->
    <el-tabs v-model="activeTab" type="card">
      <el-tab-pane label="景点管理" name="scenic">
        <scenic-management />
      </el-tab-pane>
      <el-tab-pane label="攻略审核" name="strategy">
        <strategy-review />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { ref } from 'vue'
import ScenicManagement from './components/ScenicManagement.vue'
import StrategyReview from './components/StrategyReview.vue'

export default {
  name: 'ContentManagement',
  components: {
    ScenicManagement,
    StrategyReview
  },
  setup() {
    const activeTab = ref('scenic')
    
    return {
      activeTab
    }
  }
}
</script>

<style lang="scss" scoped>
.content-management {
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
}
</style>