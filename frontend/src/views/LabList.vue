//实验室管理
<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import axios from '../http'
import { ElMessage, ElMessageBox } from 'element-plus'

const tableData = ref<any[]>([])
const dialogVisible = ref(false)
const form = ref({ id: null, labName: '', location: '', description: '' })
const isEdit = ref(false)

// 分页配置
const currentPage = ref(1)
const pageSize = ref(10)

// 搜索关键词
const searchKeyword = ref('')

// 权限判断
const isAdmin = ref(false)

// 过滤 + 分页后的数据
const filteredAndPaginatedData = computed(() => {
  let filtered = tableData.value
  if (searchKeyword.value.trim()) {
    const key = searchKeyword.value.trim().toLowerCase()
    filtered = tableData.value.filter(item => {
      return (
        item.labName?.toLowerCase().includes(key) ||
        item.location?.toLowerCase().includes(key) ||
        item.description?.toLowerCase().includes(key)
      )
    })
  }
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filtered.slice(start, end)
})

// 总条数
const total = computed(() => tableData.value.length)

onMounted(() => {
  // 获取用户角色
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
    const res = await axios.get('/lab/list')
    if (res.data.code === 200) {
      tableData.value = res.data.data
      currentPage.value = 1
    } else {
      ElMessage.error('获取实验室列表失败')
    }
  } catch (err) {
    ElMessage.error('请求失败，请检查后端是否启动')
  }
}

const openAddDialog = () => {
  isEdit.value = false
  form.value = { id: null, labName: '', location: '', description: '' }
  dialogVisible.value = true
}

const openEditDialog = (row: any) => {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

const saveData = async () => {
  try {
    let res
    if (isEdit.value) {
      res = await axios.put('/lab/update', form.value)
    } else {
      res = await axios.post('/lab/add', form.value)
    }
    if (res.data.code === 200) {
      ElMessage.success(isEdit.value ? '修改成功' : '添加成功')
      dialogVisible.value = false
      getList()
    } else {
      ElMessage.error('操作失败')
    }
  } catch (err) {
    ElMessage.error('请求异常，操作失败')
  }
}

const handleDelete = async (id: number) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除该实验室信息吗？删除后无法恢复！',
      '删除确认',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning',
        closeOnClickModal: false,
        closeOnPressEscape: false
      }
    )

    const res = await axios.delete(`/lab/delete/${id}`)
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
      <h2>实验室管理</h2>

      <!-- 按钮 + 搜索框：保持布局，仅admin显示按钮 -->
      <div style="display: flex; justify-content: space-between; align-items: center; margin: 10px 0;">
        <el-button v-if="isAdmin" type="primary" @click="openAddDialog">新增实验室</el-button>
        <!-- 占位元素，保持布局，避免按钮消失后搜索框跑到左边 -->
        <span v-else></span>
        <el-input
          v-model="searchKeyword"
          placeholder="按名称/位置/描述搜索"
          style="width: 260px"
          clearable
        />
      </div>
      
      <!-- 表格绑定过滤分页数据 -->
      <el-table :data="filteredAndPaginatedData" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="labName" label="实验室名称" />
        <el-table-column prop="location" label="位置" />
        <el-table-column prop="description" label="描述" />
        <!-- 仅管理员可见操作列 -->
        <el-table-column v-if="isAdmin" label="操作" width="180">
          <template #default="scope">
            <el-button size="small" @click="openEditDialog(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next, jumper"
        background
        style="margin-top: 15px; text-align: right;"
      />

      <el-dialog v-model="dialogVisible" title="实验室信息">
        <el-form :model="form" label-width="80px">
          <el-form-item label="名称">
            <el-input v-model="form.labName" />
          </el-form-item>
          <el-form-item label="位置">
            <el-input v-model="form.location" />
          </el-form-item>
          <el-form-item label="描述">
            <el-input v-model="form.description" type="textarea" />
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
  background-color: rgba(255, 255, 255, 0.9);
  padding: 20px;
  border-radius: 8px;
  height: 100%;
  box-sizing: border-box;
}
</style>