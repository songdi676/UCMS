import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/certificate-type'
  },
  {
    path: '/certificate-type',
    name: 'CertificateType',
    component: () => import('@/views/CertificateType.vue'),
    meta: { title: '证件类型配置' }
  },
  {
    path: '/scope-config',
    name: 'ScopeConfig',
    component: () => import('@/views/ScopeConfig.vue'),
    meta: { title: '应用范围配置' }
  },
  {
    path: '/validation-rule',
    name: 'ValidationRule',
    component: () => import('@/views/ValidationRule.vue'),
    meta: { title: '校验规则配置' }
  },
  {
    path: '/predefined-rules',
    name: 'PredefinedRule',
    component: () => import('@/views/PredefinedRule.vue'),
    meta: { title: '预置规则配置' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 设置页面标题
router.beforeEach((to, from, next) => {
  document.title = to.meta.title || '统一证件管理平台'
  next()
})

export default router
