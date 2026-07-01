<template>
  <div class="monitor">
    <div class="page-header">
      <h2>系统监控</h2>
      <p>实时监控系统运行状态，支持阈值告警</p>
    </div>
    
    <!-- 系统状态概览 -->
    <el-row :gutter="20" class="status-overview">
      <el-col :span="6">
        <el-card class="status-card" :class="{ 'status-normal': systemStatus.api === 'normal', 'status-warning': systemStatus.api === 'warning', 'status-error': systemStatus.api === 'error' }">
          <div class="status-content">
            <div class="status-icon">
              <el-icon><Connection /></el-icon>
            </div>
            <div class="status-info">
              <div class="status-title">API服务</div>
              <div class="status-value">{{ systemStatus.api === 'normal' ? '正常' : systemStatus.api === 'warning' ? '警告' : '异常' }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="status-card" :class="{ 'status-normal': systemStatus.cpu === 'normal', 'status-warning': systemStatus.cpu === 'warning', 'status-error': systemStatus.cpu === 'error' }">
          <div class="status-content">
            <div class="status-icon">
              <el-icon><Cpu /></el-icon>
            </div>
            <div class="status-info">
              <div class="status-title">CPU使用率</div>
              <div class="status-value">{{ serverStatus.cpu }}%</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="status-card" :class="{ 'status-normal': systemStatus.memory === 'normal', 'status-warning': systemStatus.memory === 'warning', 'status-error': systemStatus.memory === 'error' }">
          <div class="status-content">
            <div class="status-icon">
              <el-icon><Memory /></el-icon>
            </div>
            <div class="status-info">
              <div class="status-title">内存使用率</div>
              <div class="status-value">{{ serverStatus.memory }}%</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="status-card" :class="{ 'status-normal': systemStatus.disk === 'normal', 'status-warning': systemStatus.disk === 'warning', 'status-error': systemStatus.disk === 'error' }">
          <div class="status-content">
            <div class="status-icon">
              <el-icon><HardDisk /></el-icon>
            </div>
            <div class="status-info">
              <div class="status-title">磁盘使用率</div>
              <div class="status-value">{{ serverStatus.disk }}%</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 监控图表区域 -->
    <el-row :gutter="20" class="chart-area">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>API调用监控</span>
            <el-button type="primary" size="small" @click="refreshApiStats">刷新</el-button>
          </template>
          <div ref="apiChart" style="height: 300px;"></div>
          <div class="api-stats">
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="stat-item">
                  <div class="stat-label">总调用次数</div>
                  <div class="stat-value">{{ apiStats.total }}</div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="stat-item">
                  <div class="stat-label">成功率</div>
                  <div class="stat-value">{{ apiStats.successRate }}%</div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="stat-item">
                  <div class="stat-label">平均响应时间</div>
                  <div class="stat-value">{{ apiStats.avgResponseTime }}ms</div>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>服务器资源监控</span>
            <el-button type="primary" size="small" @click="refreshServerStats">刷新</el-button>
          </template>
          <div ref="serverChart" style="height: 300px;"></div>
          <div class="server-stats">
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="stat-item">
                  <div class="stat-label">CPU使用率</div>
                  <div class="stat-value">{{ serverStatus.cpu }}%</div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="stat-item">
                  <div class="stat-label">内存使用率</div>
                  <div class="stat-value">{{ serverStatus.memory }}%</div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="stat-item">
                  <div class="stat-label">磁盘使用率</div>
                  <div class="stat-value">{{ serverStatus.disk }}%</div>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 错误日志 -->
    <el-card style="margin-top: 20px;">
      <template #header>
        <span>错误日志</span>
        <el-button type="danger" size="small" @click="clearErrorLogs">清空日志</el-button>
      </template>
      <el-table :data="errorLogs" v-loading="loading" style="width: 100%">
        <el-table-column prop="timestamp" label="时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.timestamp) }}
          </template>
        </el-table-column>
        <el-table-column prop="level" label="级别" width="100">
          <template #default="{ row }">
            <el-tag :type="getLogLevelType(row.level)" size="small">
              {{ row.level }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="module" label="模块" width="120" />
        <el-table-column prop="message" label="错误信息" min-width="200" />
        <el-table-column prop="details" label="详细信息" min-width="300" />
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button type="text" @click="viewErrorDetail(row)">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
    
    <!-- 告警设置对话框 -->
    <el-dialog v-model="alarmDialogVisible" title="告警设置" width="600px">
      <el-form :model="alarmForm" label-width="120px">
        <el-form-item label="CPU告警阈值">
          <el-slider v-model="alarmForm.cpuThreshold" :min="50" :max="95" show-stops />
          <span style="margin-left: 10px;">{{ alarmForm.cpuThreshold }}%</span>
        </el-form-item>
        
        <el-form-item label="内存告警阈值">
          <el-slider v-model="alarmForm.memoryThreshold" :min="50" :max="95" show-stops />
          <span style="margin-left: 10px;">{{ alarmForm.memoryThreshold }}%</span>
        </el-form-item>
        
        <el-form-item label="磁盘告警阈值">
          <el-slider v-model="alarmForm.diskThreshold" :min="70" :max="95" show-stops />
          <span style="margin-left: 10px;">{{ alarmForm.diskThreshold }}%</span>
        </el-form-item>
        
        <el-form-item label="API错误率阈值">
          <el-slider v-model="alarmForm.apiErrorThreshold" :min="1" :max="20" show-stops />
          <span style="margin-left: 10px;">{{ alarmForm.apiErrorThreshold }}%</span>
        </el-form-item>
        
        <el-form-item label="告警通知">
          <el-switch v-model="alarmForm.enableNotification" />
          <span style="margin-left: 10px;">启用邮件通知</span>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="alarmDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveAlarmSettings">保存设置</el-button>
      </template>
    </el-dialog>
    
    <!-- 错误详情对话框 -->
    <el-dialog v-model="errorDetailDialogVisible" title="错误详情" width="800px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="错误时间">{{ selectedError.timestamp }}</el-descriptions-item>
        <el-descriptions-item label="错误级别">
          <el-tag :type="getLogLevelType(selectedError.level)">{{ selectedError.level }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="错误模块">{{ selectedError.module }}</el-descriptions-item>
        <el-descriptions-item label="错误信息">{{ selectedError.message }}</el-descriptions-item>
        <el-descriptions-item label="详细信息">
          <pre style="background: #f5f7fa; padding: 10px; border-radius: 4px; overflow: auto;">{{ selectedError.details }}</pre>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted, nextTick, onUnmounted } from 'vue'
import * as echarts from 'echarts'
import { ElMessage } from 'element-plus'

export default {
  name: 'Monitor',
  setup() {
    const loading = ref(false)
    const apiChart = ref(null)
    const serverChart = ref(null)
    const alarmDialogVisible = ref(false)
    const errorDetailDialogVisible = ref(false)
    const currentPage = ref(1)
    const pageSize = ref(10)
    const total = ref(0)
    
    const systemStatus = reactive({
      api: 'normal',
      cpu: 'normal',
      memory: 'normal',
      disk: 'normal'
    })
    
    const serverStatus = reactive({
      cpu: 45.2,
      memory: 68.7,
      disk: 32.1
    })
    
    const apiStats = reactive({
      total: 12543,
      successRate: 98.7,
      avgResponseTime: 156
    })
    
    const alarmForm = reactive({
      cpuThreshold: 80,
      memoryThreshold: 85,
      diskThreshold: 90,
      apiErrorThreshold: 5,
      enableNotification: true
    })
    
    const selectedError = reactive({
      timestamp: '',
      level: '',
      module: '',
      message: '',
      details: ''
    })
    
    const errorLogs = ref([
      {
        timestamp: new Date(Date.now() - 1000 * 60 * 5),
        level: 'ERROR',
        module: 'UserService',
        message: '用户数据查询失败',
        details: '数据库连接超时，请检查数据库服务状态'
      },
      {
        timestamp: new Date(Date.now() - 1000 * 60 * 30),
        level: 'WARNING',
        module: 'FileService',
        message: '文件上传大小超限',
        details: '用户上传文件大小超过限制，文件大小: 15MB, 限制: 10MB'
      },
      {
        timestamp: new Date(Date.now() - 1000 * 60 * 120),
        level: 'ERROR',
        module: 'AuthService',
        message: 'JWT令牌验证失败',
        details: 'Token已过期，请重新登录'
      }
    ])
    
    let timer = null
    
    const initApiChart = () => {
      if (!apiChart.value) return
      
      const chart = echarts.init(apiChart.value)
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['API调用次数', '错误次数']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '15%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['14:00', '14:05', '14:10', '14:15', '14:20', '14:25', '14:30']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: 'API调用次数',
            type: 'line',
            data: [120, 132, 101, 134, 90, 230, 210],
            smooth: true,
            lineStyle: {
              color: '#409EFF'
            }
          },
          {
            name: '错误次数',
            type: 'line',
            data: [2, 1, 0, 3, 1, 0, 2],
            smooth: true,
            lineStyle: {
              color: '#F56C6C'
            }
          }
        ]
      }
      chart.setOption(option)
    }
    
    const initServerChart = () => {
      if (!serverChart.value) return
      
      const chart = echarts.init(serverChart.value)
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['CPU使用率', '内存使用率', '磁盘使用率']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '15%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['14:00', '14:05', '14:10', '14:15', '14:20', '14:25', '14:30']
        },
        yAxis: {
          type: 'value',
          max: 100
        },
        series: [
          {
            name: 'CPU使用率',
            type: 'line',
            data: [45, 52, 38, 61, 55, 48, 42],
            smooth: true,
            lineStyle: {
              color: '#409EFF'
            }
          },
          {
            name: '内存使用率',
            type: 'line',
            data: [68, 72, 65, 75, 70, 68, 67],
            smooth: true,
            lineStyle: {
              color: '#67C23A'
            }
          },
          {
            name: '磁盘使用率',
            type: 'line',
            data: [32, 35, 30, 38, 34, 32, 31],
            smooth: true,
            lineStyle: {
              color: '#E6A23C'
            }
          }
        ]
      }
      chart.setOption(option)
    }
    
    const refreshApiStats = () => {
      // 模拟API调用刷新数据
      apiStats.total += Math.floor(Math.random() * 100)
      apiStats.successRate = Math.max(95, Math.min(99.9, apiStats.successRate + (Math.random() - 0.5)))
      apiStats.avgResponseTime = Math.max(100, Math.min(300, apiStats.avgResponseTime + (Math.random() - 0.5) * 20))
      
      nextTick(() => {
        initApiChart()
      })
      
      ElMessage.success('API统计数据已刷新')
    }
    
    const refreshServerStats = () => {
      // 模拟服务器状态刷新
      serverStatus.cpu = Math.max(10, Math.min(90, serverStatus.cpu + (Math.random() - 0.5) * 10))
      serverStatus.memory = Math.max(50, Math.min(85, serverStatus.memory + (Math.random() - 0.5) * 5))
      serverStatus.disk = Math.max(25, Math.min(40, serverStatus.disk + (Math.random() - 0.5) * 3))
      
      // 更新系统状态
      updateSystemStatus()
      
      nextTick(() => {
        initServerChart()
      })
      
      ElMessage.success('服务器状态已刷新')
    }
    
    const updateSystemStatus = () => {
      // 根据阈值更新系统状态
      systemStatus.cpu = serverStatus.cpu > alarmForm.cpuThreshold ? 'error' : 
                        serverStatus.cpu > alarmForm.cpuThreshold - 10 ? 'warning' : 'normal'
      
      systemStatus.memory = serverStatus.memory > alarmForm.memoryThreshold ? 'error' : 
                           serverStatus.memory > alarmForm.memoryThreshold - 10 ? 'warning' : 'normal'
      
      systemStatus.disk = serverStatus.disk > alarmForm.diskThreshold ? 'error' : 
                         serverStatus.disk > alarmForm.diskThreshold - 10 ? 'warning' : 'normal'
    }
    
    const clearErrorLogs = () => {
      errorLogs.value = []
      ElMessage.success('错误日志已清空')
    }
    
    const viewErrorDetail = (row) => {
      Object.assign(selectedError, row)
      errorDetailDialogVisible.value = true
    }
    
    const saveAlarmSettings = () => {
      ElMessage.success('告警设置已保存')
      alarmDialogVisible.value = false
      updateSystemStatus()
    }
    
    const getLogLevelType = (level) => {
      switch (level) {
        case 'ERROR': return 'danger'
        case 'WARNING': return 'warning'
        case 'INFO': return 'info'
        default: return ''
      }
    }
    
    const formatTime = (timestamp) => {
      return new Date(timestamp).toLocaleString('zh-CN')
    }
    
    const handleSizeChange = (val) => {
      pageSize.value = val
      loadErrorLogs()
    }
    
    const handleCurrentChange = (val) => {
      currentPage.value = val
      loadErrorLogs()
    }
    
    const loadErrorLogs = () => {
      loading.value = true
      // 模拟加载错误日志
      setTimeout(() => {
        loading.value = false
      }, 500)
    }
    
    const startRealTimeMonitoring = () => {
      timer = setInterval(() => {
        refreshServerStats()
        refreshApiStats()
      }, 30000) // 每30秒刷新一次
    }
    
    const stopRealTimeMonitoring = () => {
      if (timer) {
        clearInterval(timer)
        timer = null
      }
    }
    
    onMounted(() => {
      loadErrorLogs()
      nextTick(() => {
        initApiChart()
        initServerChart()
      })
      startRealTimeMonitoring()
    })
    
    onUnmounted(() => {
      stopRealTimeMonitoring()
    })
    
    return {
      loading,
      systemStatus,
      serverStatus,
      apiStats,
      errorLogs,
      alarmForm,
      selectedError,
      alarmDialogVisible,
      errorDetailDialogVisible,
      currentPage,
      pageSize,
      total,
      apiChart,
      serverChart,
      refreshApiStats,
      refreshServerStats,
      clearErrorLogs,
      viewErrorDetail,
      saveAlarmSettings,
      getLogLevelType,
      formatTime,
      handleSizeChange,
      handleCurrentChange
    }
  }
}
</script>

<style lang="scss" scoped>
.monitor {
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
  
  .status-overview {
    margin-bottom: 20px;
    
    .status-card {
      &.status-normal {
        border-left: 4px solid #67C23A;
      }
      
      &.status-warning {
        border-left: 4px solid #E6A23C;
      }
      
      &.status-error {
        border-left: 4px solid #F56C6C;
      }
      
      .status-content {
        display: flex;
        align-items: center;
        
        .status-icon {
          width: 60px;
          height: 60px;
          border-radius: 8px;
          background: #409EFF;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          
          .el-icon {
            font-size: 28px;
            color: #fff;
          }
        }
        
        .status-info {
          .status-title {
            font-size: 14px;
            color: #909399;
            margin-bottom: 8px;
          }
          
          .status-value {
            font-size: 24px;
            font-weight: bold;
            color: #303133;
          }
        }
      }
    }
  }
  
  .chart-area {
    margin-bottom: 20px;
    
    .api-stats, .server-stats {
      margin-top: 20px;
      
      .stat-item {
        text-align: center;
        
        .stat-label {
          font-size: 14px;
          color: #909399;
          margin-bottom: 8px;
        }
        
        .stat-value {
          font-size: 20px;
          font-weight: bold;
          color: #303133;
        }
      }
    }
  }
  
  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
}
</style>