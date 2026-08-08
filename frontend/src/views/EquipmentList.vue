//设备管理
<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import axios from '../http'
import { ElMessage, ElMessageBox } from 'element-plus'

const tableData = ref<any[]>([])
const dialogVisible = ref(false)
const form = ref({ id: null, eqName: '', model: '', labId: null, status: '空闲', buyTime: '' })
const isEdit = ref(false)

// 分页配置
const currentPage = ref(1)
const pageSize = ref(10)

// 搜索关键词（所有人可用）
const searchKeyword = ref('')

// 权限判断
const isAdmin = ref(false)

// 搜索 + 分页
const paginatedData = computed(() => {
  let data = tableData.value
  const key = searchKeyword.value.trim().toLowerCase()
  if (key) {
    data = data.filter(item =>
      item.eqName?.toLowerCase().includes(key) ||
      item.model?.toLowerCase().includes(key) ||
      item.status?.toLowerCase().includes(key) ||
      item.labId?.toString().includes(key)
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
    const res = await axios.get('/equipment/list')
    if (res.data.code === 200) {
      tableData.value = res.data.data
      currentPage.value = 1
    } else {
      ElMessage.error('获取设备列表失败')
    }
  } catch (err) {
    ElMessage.error('请求失败，请检查后端是否启动')
  }
}

const openAddDialog = () => {
  isEdit.value = false
  form.value = { id: null, eqName: '', model: '', labId: null, status: '空闲', buyTime: '' }
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
      res = await axios.put('/equipment/update', form.value)
    } else {
      res = await axios.post('/equipment/add', form.value)
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
      '确定要删除该设备信息吗？删除后无法恢复！',
      '删除确认',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning',
        closeOnClickModal: false,
        closeOnPressEscape: false
      }
    )

    const res = await axios.delete(`/equipment/delete/${id}`)
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
      <h2>设备管理</h2>

      <!-- 布局完全统一：左侧按钮（仅管理员），右侧搜索 -->
      <div style="display: flex; justify-content: space-between; align-items: center; margin: 10px 0;">
        <el-button v-if="isAdmin" type="primary" @click="openAddDialog">新增设备</el-button>
        <span v-else style="width: 80px;"></span> <!-- 占位保持布局不变 -->
        <el-input
          v-model="searchKeyword"
          placeholder="搜索设备名称/型号/状态/实验室ID"
          style="width: 260px"
          clearable
        />
      </div>
      
      <el-table :data="paginatedData" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="eqName" label="设备名称" />
        <el-table-column prop="model" label="型号" />
        <el-table-column prop="labId" label="所属实验室ID" />
        <el-table-column prop="status" label="状态" />
        <el-table-column prop="buyTime" label="购买日期" />

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

      <el-dialog v-model="dialogVisible" title="设备信息">
        <el-form :model="form" label-width="100px">
          <el-form-item label="设备名称">
            <el-input v-model="form.eqName" />
          </el-form-item>
          <el-form-item label="型号">
            <el-input v-model="form.model" />
          </el-form-item>
          <el-form-item label="实验室ID">
            <el-input v-model.number="form.labId" type="number" />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="form.status">
              <el-option label="空闲" value="空闲" />
              <el-option label="借用中" value="借用中" />
              <el-option label="维修中" value="维修中" />
            </el-select>
          </el-form-item>
          <el-form-item label="购买日期">
            <el-date-picker v-model="form.buyTime" type="date" />
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