//主页
<script setup lang="ts">

import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from '../http'
import { ElMessage } from 'element-plus'


const router = useRouter()
const route = useRoute()
const userInfo = ref(JSON.parse(localStorage.getItem('user') || '{}'))
const currentTime = ref('')


const profileInfo = ref({
  id: '',
  username: '',
  name: '',
  role: '',
  password: ''
})

const pwdDialogVisible = ref(false)
const pwdForm = ref({ oldPwd: '', newPwd: '', confirmPwd: '' })

// 系统统计数据（首页卡片展示）
const stats = ref({
  labCount: 0,          // 实验室总数
  equipmentCount: 0,    // 设备总数
  equipmentRepair: 0,   // 待维修设备数
  borrowPending: 0,     // 待处理借用数
  userCount: 0          // 用户总数
})

// ==========================================
// 从后端获取系统统计数据
// ==========================================
const getStats = async () => {
  try {
    // 请求后端首页统计接口
    const res = await axios.get('/dashboard/stats')
    console.log("接口返回:", res.data)
    // 判断后端返回状态码 200 成功
    if (res.data.code === '200') {
      // 将数据赋值给页面统计对象
      stats.value = res.data.data
    }
  } catch (err) {
    console.error('获取统计数据失败', err)
  }
}

// ==========================================
// 页面加载完成后执行
// ==========================================
onMounted(() => {
  // 更新当前时间
  const updateTime = () => {
    const now = new Date()
    currentTime.value = now.toLocaleString('zh-CN', {
      year: 'numeric', month: '2-digit', day: '2-digit',
      hour: '2-digit', minute: '2-digit', second: '2-digit'
    })
  }
  // 立即执行一次
  updateTime()
  // 每秒刷新时间
  setInterval(updateTime, 1000)

  // 读取本地存储的用户信息并展示
  const u = localStorage.getItem('user')
  if (u) {
    const parsed = JSON.parse(u)
    userInfo.value = parsed
    profileInfo.value = parsed
  }

  // 加载系统统计数据
  getStats()
})

// ==========================================
// 监听路由：回到首页时自动刷新统计数据
// ==========================================
watch(
  () => route.path,
  (path) => {
    if (path === '/dashboard') {
      getStats()
    }
  }
)

// ==========================================
// 页面快捷跳转方法
// ==========================================
const goTo = (path: string) => {
  router.push(path)
}

// ==========================================
// 退出登录方法
// ==========================================
const logout = () => {
  // 清除本地用户信息
  localStorage.removeItem('user')
  ElMessage.success('退出成功')
  // 延迟1秒跳转到登录页
  setTimeout(() => (window.location.href = '/'), 1000)
}

// ==========================================
// 打开修改密码弹窗
// ==========================================
const openPwdDialog = () => {
  pwdForm.value = { oldPwd: '', newPwd: '', confirmPwd: '' }
  pwdDialogVisible.value = true
}

// ==========================================
// 提交修改密码
// ==========================================
const submitChangePwd = async () => {
  const { oldPwd, newPwd, confirmPwd } = pwdForm.value
  // 非空判断
  if (!oldPwd || !newPwd || !confirmPwd) return ElMessage.warning('请填写完整信息')
  // 两次密码一致判断
  if (newPwd !== confirmPwd) return ElMessage.error('两次密码不一致')

  try {
    // 请求后端修改密码接口
    const res = await axios.post('/user/changePwd', {
      id: profileInfo.value.id, oldPwd, newPwd
    })
    // 修改成功
    if (res.data.code === '200') {
      ElMessage.success('修改成功，请重新登录')
      localStorage.removeItem('user')
      setTimeout(() => window.location.href = '/', 1000)
    } else {
      ElMessage.error(res.data.msg || '旧密码错误')
    }
  } catch (err) {
    ElMessage.error('请求失败')
  }
}
</script>

<template>
  <div class="dashboard-container">
    <div class="content-wrapper">

      <!-- 顶部欢迎区域 -->
      <div class="welcome-card">
        <h1>👋 欢迎回来，{{ userInfo.name || userInfo.username }}！</h1>
        <p class="time">当前时间：{{ currentTime }}</p>
      </div>

      <!-- 系统数据统计卡片 -->
      <div class="stat-grid">
        <div class="stat-card">
          <div class="left">
            <div class="num">{{ stats.labCount }}</div>
            <div class="label">实验室总数</div>
          </div>
          <div class="icon">🏫</div>
        </div>
        <div class="stat-card">
          <div class="left">
            <div class="num">{{ stats.equipmentCount }}</div>
            <div class="label">设备总数</div>
          </div>
          <div class="icon">🔧</div>
        </div>
        <div class="stat-card">
          <div class="left">
            <div class="num">{{ stats.borrowPending }}</div>
            <div class="label">待处理借用</div>
          </div>
          <div class="icon">📦</div>
        </div>
        <div class="stat-card">
          <div class="left">
            <div class="num">{{ stats.equipmentRepair }}</div>
            <div class="label">待维修设备</div>
          </div>
          <div class="icon">🛠️</div>
        </div>
      </div>

      <!-- 功能快捷入口卡片 -->
      <div class="card-grid">
        <div class="card" @click="goTo('/user')">
          <div class="icon">👥</div>
          <div class="text">
            <h3>用户管理</h3>
            <p>系统共有 {{ stats.userCount }} 位用户</p>
          </div>
        </div>
        <div class="card" @click="goTo('/lab')">
          <div class="icon">🏫</div>
          <div class="text">
            <h3>实验室管理</h3>
            <p>统一管理实验室信息</p>
          </div>
        </div>
        <div class="card" @click="goTo('/equipment')">
          <div class="icon">🔧</div>
          <div class="text">
            <h3>设备管理</h3>
            <p>{{ stats.equipmentRepair }} 台设备待维修</p>
          </div>
        </div>
        <div class="card" @click="goTo('/borrow')">
          <div class="icon">📦</div>
          <div class="text">
            <h3>借用管理</h3>
            <p>{{ stats.borrowPending }} 条待处理申请</p>
          </div>
        </div>
        <div class="card" @click="goTo('/repair')">
          <div class="icon">🛠️</div>
          <div class="text">
            <h3>维修记录</h3>
            <p>设备维修全程跟踪</p>
          </div>
        </div>
      </div>

      <!-- 个人信息展示卡片 -->
      <div class="profile-card">
        <div class="avatar-wrap">
          <el-avatar src="/images/dome2.jpg" size="100" />
        </div>
        <div class="profile-info">
          <div class="info-row">
            <span class="label">姓名</span>
            <span class="val">{{ profileInfo.name || '未设置' }}</span>
          </div>
          <div class="info-row">
            <span class="label">账号</span>
            <span class="val">{{ profileInfo.username || '未获取' }}</span>
          </div>
          <div class="info-row">
            <span class="label">角色</span>
            <span class="val">
              {{ profileInfo.role === 'admin' ? '管理员' : 
                 profileInfo.role === 'teacher' ? '教师' : '学生' }}
            </span>
          </div>
        </div>
        <div class="profile-actions">
          <el-button type="primary" @click="openPwdDialog">修改密码</el-button>
          <el-button type="danger" plain @click="logout">退出登录</el-button>
        </div>
      </div>
    </div>

    <!-- 修改密码弹窗 -->
    <el-dialog v-model="pwdDialogVisible" title="修改密码" width="400px">
      <el-form :model="pwdForm" label-width="100px">
        <el-form-item label="旧密码"><el-input v-model="pwdForm.oldPwd" type="password" /></el-form-item>
        <el-form-item label="新密码"><el-input v-model="pwdForm.newPwd" type="password" /></el-form-item>
        <el-form-item label="确认密码"><el-input v-model="pwdForm.confirmPwd" type="password" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="pwdDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitChangePwd">确认修改</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
/* 页面整体背景样式 */
.dashboard-container {
  background: url('/images/dome1.png') no-repeat center center;
  background-size: cover;
  background-attachment: fixed;
  overflow: visible;
  height: auto;
}

/* 内容区域半透明背景 */
.content-wrapper {
  padding: 30px;
  background: rgba(255, 255, 255, 0.55);
  height: auto;
}

/* 卡片公共样式：圆角、阴影、过渡动画 */
.welcome-card,
.stat-card,
.card,
.profile-card {
  background: rgba(255, 255, 255, 0.88);
  border-radius: 14px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  transition: all 0.3s ease;
}

/* 欢迎卡片样式 */
.welcome-card {
  padding: 28px 32px;
  margin-bottom: 24px;
}
.welcome-card h1 {
  font-size: 24px;
  color: #2c3e50;
  margin: 0 0 10px;
}
.time {
  color: #7f8c8d;
  font-size: 14px;
}

/* 统计卡片网格布局 */
.stat-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 18px;
  margin-bottom: 24px;
}
.stat-card {
  padding: 22px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.stat-card .num {
  font-size: 28px;
  font-weight: bold;
  color: #2c3e50;
  line-height: 1.2;
}
.stat-card .label {
  font-size: 14px;
  color: #7f8c8d;
  margin-top: 4px;
}
.stat-card .icon {
  font-size: 40px;
  opacity: 0.8;
}

/* 功能卡片网格布局 */
.card-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 18px;
  margin-bottom: 30px;
}
.card {
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  cursor: pointer;
}
/* 鼠标悬浮动画 */
.card:hover {
  transform: translateY(-4px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}
.icon {
  font-size: 36px;
}
.text h3 {
  margin: 0 0 4px;
  font-size: 16px;
  color: #2c3e50;
}
.text p {
  margin: 0;
  font-size: 13px;
  color: #7f8c8d;
}

/* 个人信息卡片样式 */
.profile-card {
  padding: 28px 32px;
  display: flex;
  align-items: center;
  gap: 40px;
  flex-wrap: wrap;
  justify-content: center;
}
.avatar-wrap {
  flex-shrink: 0;
}
.avatar-wrap img {
  border: 3px solid #409eff;
  border-radius: 50%;
}
.profile-info {
  flex: 1;
  min-width: 280px;
}
.info-row {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
  font-size: 15px;
}
.label {
  width: 70px;
  font-weight: 500;
  color: #34495e;
}
.val {
  color: #2c3e50;
}
.profile-actions {
  display: flex;
  gap: 12px;
  flex-shrink: 0;
}
</style>