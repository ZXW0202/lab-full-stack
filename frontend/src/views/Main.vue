//菜单
<template>
  <div class="main-container">
    <!-- 顶部横向菜单 -->
    <el-menu
      :default-active="activeMenu"
      mode="horizontal"
      class="top-menu"
      @select="handleSelect"
    >
      <el-menu-item index="dashboard" class="logo-item">
        <img src="/images/dome3.png" alt="logo" />
      </el-menu-item>

      <el-menu-item index="dashboard">
        <el-icon><House /></el-icon>
        <span>首页</span>
      </el-menu-item>

      <el-menu-item index="user">
        <el-icon><User /></el-icon>
        <span>用户管理</span>
      </el-menu-item>

      <el-menu-item index="lab">
        <el-icon><OfficeBuilding /></el-icon>
        <span>实验室管理</span>
      </el-menu-item>

      <el-menu-item index="equipment">
        <el-icon><Box /></el-icon>
        <span>设备管理</span>
      </el-menu-item>

      <el-menu-item index="borrow">
        <el-icon><Tickets /></el-icon>
        <span>借用管理</span>
      </el-menu-item>

      <el-menu-item index="repair">
        <el-icon><Tools /></el-icon>
        <span>维修记录</span>
      </el-menu-item>

    </el-menu>

    <!-- 内容区域 -->
    <div class="content-box">
      <router-view />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { User, OfficeBuilding, Box, Tickets, Tools, House } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const activeMenu = ref('dashboard')

onMounted(() => {
  activeMenu.value = route.path.replace('/', '')
})

const handleSelect = (key: string) => {
  router.push(`/${key}`)
}
</script>

<style scoped>
.main-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 顶部菜单：保持 40px 高度不变 */
.top-menu {
  --el-menu-item-height: 40px;
  border-bottom: 1px solid #e5e6eb;
}

/* logo 菜单项：去掉默认内边距 */
.logo-item {
  padding: 0 !important;
}

.logo-item img {
  height: 66px;
  width: auto;
  object-fit: contain;
  display: block;
  margin: auto 0;
}

/* 内容区域 */
.content-box {
  flex: 1;
  padding: 12px;
  background: #f5f7fa;
  overflow: auto;
}
</style>