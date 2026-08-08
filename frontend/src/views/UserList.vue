//用户管理
<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import axios from '../http'
import { ElMessage, ElMessageBox } from 'element-plus'

const tableData = ref<any[]>([])
const dialogVisible = ref(false)
const form = ref({ id: null, username: '', password: '', name: '', role: 'student' })
const isEdit = ref(false)

// 分页配置
const currentPage = ref(1)
const pageSize = ref(10)

// 搜索（所有人可用）
const searchKeyword = ref('')

// 权限判断
const isAdmin = ref(false)

// 搜索 + 分页
const paginatedData = computed(() => {
  let data = tableData.value
  const key = searchKeyword.value.trim().toLowerCase()
  if (key) {
    data = data.filter(item =>
      item.username?.toLowerCase().includes(key) ||
      item.name?.toLowerCase().includes(key) ||
      item.role?.toLowerCase().includes(key)
    )
  }
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return data.slice(start, end)
})

const total = computed(() => tableData.value.length)

onMounted(() => {
  const uStr = localStorage.getItem('user')
  if (uStr) {
    try {
      const u = JSON.parse(uStr)
      isAdmin.value = u.role === 'admin'
    } catch (e) {}
  }
  getList()
})

const getList = async () => {
  try {
    const res = await axios.get('/user/list')
    if (res.data.code === 200) {
      tableData.value = res.data.data
      currentPage.value = 1
    }
  } catch (err) {
    ElMessage.error('获取用户列表失败')
  }
}

const openAddDialog = () => {
  isEdit.value = false
  form.value = { id: null, username: '', password: '', name: '', role: 'student' }
  dialogVisible.value = true
}

const openEditDialog = (row: any) => {
  isEdit.value = true
  form.value = { id: row.id, username: row.username, password: '', name: row.name, role: row.role }
  dialogVisible.value = true
}

const saveData = async () => {
  try {
    let res
    if (isEdit.value) {
      const data = { ...form.value }
      if (!data.password) {
         // @ts-ignore
           delete data.password
      }
    
      res = await axios.put('/user/update', data)
    } else {
      res = await axios.post('/user/add', form.value)
    }
    if (res.data.code === 200) {
      ElMessage.success(isEdit.value ? '修改成功' : '添加成功')
      dialogVisible.value = false
      getList()
    } else {
      ElMessage.error(res.data.msg || '操作失败')
    }
  } catch (err) {
    ElMessage.error('请求异常，操作失败')
  }
}

const handleDelete = async (id: number) => {
  try {
    await ElMessageBox.confirm('确定要删除该用户吗？删除后无法恢复！', '删除确认', {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const res = await axios.delete(`/user/delete/${id}`)
    if (res.data.code === 200) {
      ElMessage.success('删除成功')
      getList()
    } else {
      ElMessage.error('删除失败')
    }
  } catch (error) {
    ElMessage.info('已取消删除')
  }
}
</script>

<template>
  <div class="page-container">
    <div class="content-wrapper">
      <h2>用户管理</h2>

      <!-- 布局完全不变：左侧按钮（仅admin），右侧搜索 -->
      <div style="display: flex; justify-content: space-between; align-items: center; margin: 10px 0;">
        <el-button v-if="isAdmin" type="primary" @click="openAddDialog">新增用户</el-button>
        <span v-else style="width: 80px;"></span> <!-- 占位保持布局 -->
        <el-input
          v-model="searchKeyword"
          placeholder="搜索用户名/姓名/角色"
          style="width: 260px"
          clearable
        />
      </div>
      
      <el-table :data="paginatedData" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="name" label="姓名" />
        <el-table-column prop="role" label="角色" />

        <!-- 仅管理员可见操作列 -->
        <el-table-column v-if="isAdmin" label="操作" width="180">
          <template #default="scope">
            <el-button size="small" @click="openEditDialog(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next, jumper"
        background
        style="margin-top: 15px; text-align: right;"
      />

      <el-dialog v-model="dialogVisible" title="用户信息">
        <el-form :model="form" label-width="80px">
          <el-form-item label="用户名">
            <el-input v-model="form.username" />
          </el-form-item>
          <el-form-item label="密码" v-if="!isEdit">
            <el-input v-model="form.password" type="password" placeholder="请输入密码" />
          </el-form-item>
          <el-form-item label="姓名">
            <el-input v-model="form.name" />
          </el-form-item>
          <el-form-item label="角色">
            <el-select v-model="form.role">
              <el-option label="管理员" value="admin" />
              <el-option label="学生" value="student" />
              <el-option label="教师" value="teacher" />
            </el-select>
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveData">确定</el-button>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<style scoped>
.page-container {
  width: 100%;
  height: 100%;
  background-image: url('/images/dome1.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  padding: 20px;
  box-sizing: border-box;
}
.content-wrapper {
  background-color: rgba(255,255,255,0.9);
  padding: 20px;
  border-radius: 8px;
  height: 100%;
}
</style>