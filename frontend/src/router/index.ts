import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Main from '../views/Main.vue'
import UserList from '../views/UserList.vue'
import LabList from '../views/LabList.vue'
import EquipmentList from '../views/EquipmentList.vue'
import BorrowList from '../views/BorrowList.vue'
import RepairList from '../views/RepairList.vue'
import Dashboard from '../views/Dashboard.vue' // 首页

const routes = [
  // 👇 一进来 → 登录页（不会跳过）
  { path: '/', redirect: '/login' },
  
  { path: '/login', component: Login },
  { path: '/register', component: Register },
  
  {
    path: '/',
    component: Main,
    children: [
      // 👇 登录后进入的首页
      { path: 'dashboard', component: Dashboard },
      { path: 'user', component: UserList },
      { path: 'lab', component: LabList },
      { path: 'equipment', component: EquipmentList },
      { path: 'borrow', component: BorrowList },
      { path: 'repair', component: RepairList },
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router