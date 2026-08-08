//登录
<script setup lang="ts">
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import axios from '../http'
import { ElMessage } from 'element-plus'

const router = useRouter()
const form = reactive({
  username: '',
  password: ''
})

// 校验规则
const rules = reactive({
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
})

// 登录提交
const onSubmit = async () => {
  try {
    const res = await axios.post('/user/login', form)
    if (res.data.code === 200) {
      // ✅ 保存用户信息到本地（必须叫 user）
      localStorage.setItem('user', JSON.stringify(res.data.data))
      ElMessage.success('登录成功')
      router.push('/dashboard')
    } else {
      ElMessage.error(res.data.msg || '登录失败')
    }
  } catch (err) {
    ElMessage.error('登录失败')
  }
}
</script>

<template>
  <div class="login-page">
    <div class="login-box">
      <h2>实验室管理系统登录</h2>
      <el-form :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit" style="width:100%">登录</el-button>
        </el-form-item>
        <el-form-item>
          <el-button @click="router.push('/register')" style="width:100%">注册账号</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  width: 100vw;
  height: 100vh;
  background-image: url('/images/dome1.png');
  background-size: cover;
  display: flex;
  justify-content: center;
  align-items: center;
}
.login-box {
  width: 380px;
  padding: 40px 30px;
  background: rgba(255,255,255,0.92);
  border-radius: 12px;
}
h2 {
  text-align: center;
  margin-bottom: 30px;
}
</style>