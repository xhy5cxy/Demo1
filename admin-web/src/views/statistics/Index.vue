<template>
  <div class="statistics">
    <div class="page-header">
      <h2>数据统计</h2>
      <p>平台数据可视化分析，支持数据导出</p>
    </div>
    
    <!-- 时间范围选择 -->
    <el-card class="filter-card">
      <el-form :model="filterForm" :inline="true">
        <el-form-item label="统计时间">
          <el-date-picker
            v-model="filterForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            @change="handleDateChange"
          />
        </el-form-item>
        
        <el-form-item label="统计类型">
          <el-select v-model="filterForm.type" @change="handleTypeChange">
            <el-option label="用户数据" value="user" />
            <el-option label="内容数据" value="content" />
            <el-option label="活跃度数据" value="activity" />
          </el-select>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleExport">导出数据</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <!-- 数据概览卡片 -->
    <el-row :gutter="20" class="stats-overview">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon user-growth">
              <el-icon><User /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overviewData.userGrowth }}%</div>
              <div class="stat-label">用户增长率</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon content-growth">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overviewData.contentGrowth }}%</div>
              <div class="stat-label">内容增长率</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon active-rate">
              <el-icon><TrendCharts /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overviewData.activeRate }}%</div>
              <div class="stat-label">用户活跃率</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon retention-rate">
              <el-icon><DataLine /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overviewData.retentionRate }}%</div>
              <div class="stat-label">用户留存率</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-area">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>用户增长趋势</span>
          </template>
          <div ref="userChart" style="height: 400px;"></div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>内容生产趋势</span>
          </template>
          <div ref="contentChart" style="height: 400px;"></div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" class="chart-area" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>用户活跃度分布</span>
          </template>
          <div ref="activityChart" style="height: 400px;"></div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>内容类型占比</span>
          </template>
          <div ref="typeChart" style="height: 400px;"></div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 数据表格 -->
    <el-card style="margin-top: 20px;">
      <template #header>
        <span>详细数据</span>
      </template>
      <el-table :data="tableData" v-loading="loading" style="width: 100%">
        <el-table-column prop="date" label="日期" width="120" />
        <el-table-column prop="newUsers" label="新增用户" width="100" />
        <el-table-column prop="totalUsers" label="总用户数" width="100" />
        <el-table-column prop="newContents" label="新增内容" width="100" />
        <el-table-column prop="totalContents" label="总内容数" width="100" />
        <el-table-column prop="activeUsers" label="活跃用户" width="100" />
        <el-table-column prop="retentionRate" label="留存率" width="100">
          <template #default="{ row }">
            {{ row.retentionRate }}%
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { ElMessage } from 'element-plus'

export default {
  name: 'Statistics',
  setup() {
    const loading = ref(false)
    const userChart = ref(null)
    const contentChart = ref(null)
    const activityChart = ref(null)
    const typeChart = ref(null)
    
    const filterForm = reactive({
      dateRange: [
        new Date(Date.now() - 30 * 24 * 60 * 60 * 1000).toISOString().split('T')[0],
        new Date().toISOString().split('T')[0]
      ],
      type: 'user'
    })
    
    const overviewData = reactive({
      userGrowth: 15.8,
      contentGrowth: 23.4,
      activeRate: 68.2,
      retentionRate: 45.6
    })
    
    const tableData = ref([
      { date: '2024-01-15', newUsers: 28, totalUsers: 12543, newContents: 45, totalContents: 8567, activeUsers: 342, retentionRate: 45.6 },
      { date: '2024-01-14', newUsers: 32, totalUsers: 12515, newContents: 38, totalContents: 8522, activeUsers: 356, retentionRate: 46.2 },
      { date: '2024-01-13', newUsers: 25, totalUsers: 12483, newContents: 42, totalContents: 8484, activeUsers: 328, retentionRate: 44.8 },
      { date: '2024-01-12', newUsers: 29, totalUsers: 12458, newContents: 35, totalContents: 8442, activeUsers: 365, retentionRate: 47.1 },
      { date: '2024-01-11', newUsers: 31, totalUsers: 12429, newContents: 40, totalContents: 8407, activeUsers: 338, retentionRate: 45.3 }
    ])
    
    const initUserChart = () => {
      if (!userChart.value) return
      
      const chart = echarts.init(userChart.value)
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['新增用户', '总用户数']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['1月10日', '1月11日', '1月12日', '1月13日', '1月14日', '1月15日']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '新增用户',
            type: 'line',
            stack: 'Total',
            data: [25, 31, 29, 25, 32, 28],
            smooth: true,
            areaStyle: {}
          },
          {
            name: '总用户数',
            type: 'line',
            stack: 'Total',
            data: [12429, 12458, 12483, 12515, 12543, 12571],
            smooth: true
          }
        ]
      }
      chart.setOption(option)
    }
    
    const initContentChart = () => {
      if (!contentChart.value) return
      
      const chart = echarts.init(contentChart.value)
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['新增内容', '总内容数']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['1月10日', '1月11日', '1月12日', '1月13日', '1月14日', '1月15日']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '新增内容',
            type: 'bar',
            data: [38, 40, 35, 42, 38, 45],
            itemStyle: {
              color: '#409EFF'
            }
          },
          {
            name: '总内容数',
            type: 'line',
            data: [8407, 8442, 8484, 8522, 8567, 8612],
            smooth: true,
            lineStyle: {
              color: '#67C23A'
            }
          }
        ]
      }
      chart.setOption(option)
    }
    
    const initActivityChart = () => {
      if (!activityChart.value) return
      
      const chart = echarts.init(activityChart.value)
      const option = {
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [{
          name: '活跃度分布',
          type: 'pie',
          radius: '50%',
          data: [
            { value: 2340, name: '高活跃' },
            { value: 4560, name: '中活跃' },
            { value: 2890, name: '低活跃' },
            { value: 2753, name: '不活跃' }
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
    
    const initTypeChart = () => {
      if (!typeChart.value) return
      
      const chart = echarts.init(typeChart.value)
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['景点介绍', '旅游攻略', '美食推荐', '住宿信息', '用户评论']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value'
        },
        yAxis: {
          type: 'category',
          data: ['景点介绍', '旅游攻略', '美食推荐', '住宿信息', '用户评论']
        },
        series: [{
          name: '内容数量',
          type: 'bar',
          data: [3200, 2800, 1800, 1200, 1500],
          itemStyle: {
            color: function(params) {
              const colorList = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399']
              return colorList[params.dataIndex]
            }
          }
        }]
      }
      chart.setOption(option)
    }
    
    const handleDateChange = () => {
      loadStatistics()
    }
    
    const handleTypeChange = () => {
      loadStatistics()
    }
    
    const handleExport = () => {
      ElMessage.success('数据导出功能开发中...')
    }
    
    const loadStatistics = () => {
      loading.value = true
      
      // 模拟API调用
      setTimeout(() => {
        loading.value = false
        
        // 重新初始化图表
        nextTick(() => {
          initUserChart()
          initContentChart()
          initActivityChart()
          initTypeChart()
        })
      }, 1000)
    }
    
    onMounted(() => {
      loadStatistics()
    })
    
    return {
      loading,
      filterForm,
      overviewData,
      tableData,
      userChart,
      contentChart,
      activityChart,
      typeChart,
      handleDateChange,
      handleTypeChange,
      handleExport
    }
  }
}
</script>

<style lang="scss" scoped>
.statistics {
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
  
  .stats-overview {
    margin-bottom: 20px;
    
    .stat-card {
      .stat-content {
        display: flex;
        align-items: center;
        
        .stat-icon {
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
          
          &.user-growth {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }
          
          &.content-growth {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
          }
          
          &.active-rate {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
          }
          
          &.retention-rate {
            background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
          }
        }
        
        .stat-info {
          .stat-value {
            font-size: 28px;
            font-weight: bold;
            color: #303133;
            line-height: 1;
          }
          
          .stat-label {
            margin-top: 8px;
            color: #909399;
            font-size: 14px;
          }
        }
      }
    }
  }
  
  .chart-area {
    margin-bottom: 20px;
  }
}
</style>