<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapse ? '64px' : '200px'">
      <div class="logo">
        <span v-if="!isCollapse">E-Tour 后台管理</span>
        <span v-else>ET</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <template v-for="route in routes" :key="route.path">
          <el-menu-item :index="route.path" v-if="!route.children">
            <el-icon><component :is="route.meta.icon" /></el-icon>
            <span>{{ route.meta.title }}</span>
          </el-menu-item>
        </template>
      </el-menu>
    </el-aside>

    <!-- 主内容区 -->
    <el-container>
      <!-- 头部 -->
      <el-header class="layout-header">
        <div class="header-left">
          <el-button @click="toggleCollapse" :icon="isCollapse ? 'Expand' : 'Fold'" circle />
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/dashboard' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentRoute.meta?.title }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-avatar size="small" :src="userAvatar" />
              <span>{{ userName }}</span>
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人信息</el-dropdown-item>
                <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 内容区 -->
      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

export default {
  name: 'Layout',
  setup() {
    const route = useRoute()
    const router = useRouter()
    const isCollapse = ref(false)
    
    const routes = computed(() => {
      return router.options.routes.find(r => r.path === '/').children || []
    })
    
    const activeMenu = computed(() => route.path)
    const currentRoute = computed(() => route)
    
    const userName = ref('管理员')
    const userAvatar = ref('')
    
    const toggleCollapse = () => {
      isCollapse.value = !isCollapse.value
    }
    
    const handleCommand = (command) => {
      if (command === 'logout') {
        localStorage.removeItem('admin_token')
        router.push('/login')
      }
    }
    
    return {
      isCollapse,
      routes,
      activeMenu,
      currentRoute,
      userName,
      userAvatar,
      toggleCollapse,
      handleCommand
    }
  }
}
</script>

<style lang="scss" scoped>
.layout-container {
  height: 100vh;
  
  .el-aside {
    background-color: #304156;
    transition: width 0.3s;
    
    .logo {
      height: 60px;
      line-height: 60px;
      text-align: center;
      color: #fff;
      font-size: 16px;
      font-weight: bold;
      background-color: #2b2f3a;
    }
    
    .el-menu {
      border: none;
      
      .el-menu-item {
        &:hover {
          background-color: #263445 !important;
        }
        
        &.is-active {
          background-color: #1f2d3d !important;
        }
      }
    }
  }
  
  .layout-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    background-color: #fff;
    border-bottom: 1px solid #e6e6e6;
    
    .header-left {
      display: flex;
      align-items: center;
      
      .el-button {
        margin-right: 20px;
      }
    }
    
    .user-info {
      display: flex;
      align-items: center;
      cursor: pointer;
      
      .el-avatar {
        margin-right: 8px;
      }
    }
  }
  
  .layout-main {
    background-color: #f0f2f5;
    padding: 20px;
  }
}
</style>