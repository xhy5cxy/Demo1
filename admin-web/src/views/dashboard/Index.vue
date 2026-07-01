<template>
  <div class="dashboard-container">
    <div class="dashboard-header">
      <h2>首页</h2>
      <p>欢迎使用 E-Tour 后台管理系统</p>
    </div>
    
    <!-- 快速导航 -->
    <el-row :gutter="20" class="quick-nav">
      <el-col :span="6">
        <el-card class="nav-card" @click="$router.push('/user')">
          <div class="nav-content">
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="nav-card" @click="$router.push('/content')">
          <div class="nav-content">
            <el-icon><Document /></el-icon>
            <span>内容管理</span>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="nav-card" @click="$router.push('/statistics')">
          <div class="nav-content">
            <el-icon><DataAnalysis /></el-icon>
            <span>数据统计</span>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="nav-card" @click="$router.push('/monitor')">
          <div class="nav-content">
            <el-icon><Monitor /></el-icon>
            <span>系统监控</span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据概览卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon user-icon">
              <el-icon><User /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ stats.totalUsers }}</div>
              <div class="stats-label">总用户数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon content-icon">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ stats.totalContents }}</div>
              <div class="stats-label">总内容数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon today-icon">
              <el-icon><TrendCharts /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ stats.todayActiveUsers }}</div>
              <div class="stats-label">今日活跃用户</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon pending-icon">
              <el-icon><Clock /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ stats.pendingReviews }}</div>
              <div class="stats-label">待审核内容</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>用户增长趋势</span>
          </template>
          <div ref="userChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>内容类型分布</span>
          </template>
          <div ref="contentChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 最近活动 -->
    <el-card class="recent-activity">
      <template #header>
        <span>最近活动</span>
      </template>
      <el-timeline>
        <el-timeline-item
          v-for="activity in recentActivities"
          :key="activity.id"
          :timestamp="activity.time"
          :type="activity.type"
        >
          {{ activity.content }}
        </el-timeline-item>
      </el-timeline>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import * as echarts from 'echarts'

export default {
  name: 'Dashboard',
  setup() {
    const stats = reactive({
      totalUsers: 12543,
      totalContents: 8567,
      todayActiveUsers: 342,
      pendingReviews: 23
    })
    
    const recentActivities = ref([
      {
        id: 1,
        time: '2024-01-15 14:30',
        type: 'primary',
        content: '用户 "旅行达人" 发布了新的景点攻略'
      },
      {
        id: 2,
        time: '2024-01-15 13:45',
        type: 'success',
        content: '管理员审核通过了 5 条用户内容'
      },
      {
        id: 3,
        time: '2024-01-15 11:20',
        type: 'warning',
        content: '系统检测到 3 个疑似违规账号'
      },
      {
        id: 4,
        time: '2024-01-15 09:15',
        type: 'info',
        content: '今日新增用户 28 人'
      }
    ])
    
    const userChart = ref(null)
    const contentChart = ref(null)
    
    const initUserChart = () => {
      if (!userChart.value) return
      
      const chart = echarts.init(userChart.value)
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: [820, 932, 901, 934, 1290, 1330, 1320, 1450, 1520, 1680, 1750, 1890],
          type: 'line',
          smooth: true,
          areaStyle: {}
        }]
      }
      chart.setOption(option)
    }
    
    const initContentChart = () => {
      if (!contentChart.value) return
      
      const chart = echarts.init(contentChart.value)
      const option = {
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [{
          name: '内容类型',
          type: 'pie',
          radius: '50%',
          data: [
            { value: 335, name: '景点介绍' },
            { value: 310, name: '旅游攻略' },
            { value: 234, name: '美食推荐' },
            { value: 135, name: '住宿信息' },
            { value: 1548, name: '用户评论' }
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }]
      }
      chart.setOption(option)
    }
    
    onMounted(() => {
      initUserChart()
      initContentChart()
    })
    
    return {
      stats,
      recentActivities,
      userChart,
      contentChart
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  .dashboard-header {
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
  
  .stats-row {
    margin-bottom: 24px;
    
    .stats-card {
      .stats-content {
        display: flex;
        align-items: center;
        
        .stats-icon {
          width: 60px;
          height: 60px;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          
          .el-icon {
            font-size: 28px;
            color: #fff;
          }
          
          &.user-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }
          
          &.content-icon {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
          }
          
          &.today-icon {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
          }
          
          &.pending-icon {
            background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
          }
        }
        
        .stats-info {
          .stats-value {
            font-size: 28px;
            font-weight: bold;
            color: #303133;
            line-height: 1;
          }
          
          .stats-label {
            margin-top: 8px;
            color: #909399;
            font-size: 14px;
          }
        }
      }
    }
  }
  
  .chart-row {
    margin-bottom: 24px;
  }
  
  .recent-activity {
    :deep(.el-card__header) {
      font-weight: bold;
    }
  }
}
</style>