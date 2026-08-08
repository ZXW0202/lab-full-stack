//注册页面
<script setup lang="ts">
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import axios from '../http'
import { ElMessage } from 'element-plus'

const router = useRouter()
const form = reactive({
  username: '',
  password: '',
  name: ''
})

// 表单校验规则
const rules = reactive({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ]
})

const onSubmit = async () => {
  try {
    const res = await axios.post('/user/add', form)
    if (res.data.code === 200) {
      ElMessage.success('注册成功，请登录')
      router.push('/')
    } else {
      ElMessage.error(res.data.msg || '注册失败')
    }
  } catch (err) {
    ElMessage.error('注册失败，请检查是否正确填写用户名，密码或姓名')
  }
}
</script>

<template>
  <div class="register-page">
    <div class="register-box">
      <h2>用户注册</h2>
      <!-- 这里加上 :rules="rules" -->
      <el-form :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit" style="width: 100%">注册</el-button>
        </el-form-item>
        <el-form-item>
          <el-button @click="router.push('/')" style="width: 100%">已有账号？去登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.register-page {
  width: 100vw;
  height: 100vh;
  background-image: url('/images/dome1.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  display: flex;
  justify-content: center;
  align-items: center;
}

.register-box {
  width: 380px;
  padding: 40px 30px;
  background-color: rgba(255, 255, 255, 0.92);
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.1);
}

h2 {
  text-align: center;
  margin-bottom: 30px;
  font-weight: 600;
}
</style>