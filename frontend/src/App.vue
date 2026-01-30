<template>
  <el-container class="app-container">
    <!-- 顶部导航栏 -->
    <el-header class="app-header">
      <div class="header-left">
        <div class="logo">
          <el-icon :size="24" color="#60a5fa"><DocumentChecked /></el-icon>
          <span class="logo-text">统一证件管理</span>
        </div>
      </div>
      <div class="header-right">
        <el-badge :value="3" class="notification-badge">
          <el-button :icon="Bell" circle />
        </el-badge>
        <el-dropdown>
          <div class="user-info">
            <el-avatar :size="36" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
            <span class="username">管理员</span>
            <el-icon><ArrowDown /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>个人中心</el-dropdown-item>
              <el-dropdown-item>系统设置</el-dropdown-item>
              <el-dropdown-item divided>退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>

    <el-container class="main-container">
      <!-- 侧边栏 -->
      <el-aside :width="sidebarWidth" class="app-aside">
        <el-menu
          :default-active="activeMenu"
          router
          class="el-menu-vertical"
          :collapse-transition="false"
        >
          <el-menu-item index="/certificate-type" class="menu-item">
            <el-icon><Document /></el-icon>
            <span>证件类型配置</span>
          </el-menu-item>
          <el-menu-item index="/scope-config" class="menu-item">
            <el-icon><Setting /></el-icon>
            <span>应用范围配置</span>
          </el-menu-item>
          <el-menu-item index="/validation-rule" class="menu-item">
            <el-icon><Checked /></el-icon>
            <span>校验规则配置</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

       <!-- 主内容区 -->
       <el-main class="app-main">
         <div class="main-content">
           <div class="breadcrumb-wrapper">
             <el-breadcrumb separator="/">
               <el-breadcrumb-item to="/">首页</el-breadcrumb-item>
               <el-breadcrumb-item>{{ currentPageTitle }}</el-breadcrumb-item>
             </el-breadcrumb>
           </div>
           <router-view v-slot="{ Component }">
             <keep-alive>
               <component :is="Component" />
             </keep-alive>
           </router-view>
         </div>
       </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed, ref, onMounted, onUnmounted } from 'vue'
import { useRoute } from 'vue-router'
import { Document, Setting, Checked, Bell, ArrowDown, DocumentChecked } from '@element-plus/icons-vue'

const route = useRoute()
const activeMenu = computed(() => route.path)

// 当前页面标题
const currentPageTitle = computed(() => {
  return route.meta.title || '首页'
})

// 侧边栏宽度
const sidebarWidth = ref('240px')

// 检测屏幕尺寸
const checkScreenSize = () => {
  if (window.innerWidth >= 1920) {
    sidebarWidth.value = '280px'
  } else if (window.innerWidth >= 1440) {
    sidebarWidth.value = '260px'
  } else {
    sidebarWidth.value = '240px'
  }
}

onMounted(() => {
  checkScreenSize()
  window.addEventListener('resize', checkScreenSize)
})

onUnmounted(() => {
  window.removeEventListener('resize', checkScreenSize)
})
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, 'Noto Sans', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

#app {
  width: 100%;
  height: 100vh;
}

.app-container {
  height: 100%;
  overflow: hidden;
}

/* 顶部导航栏 */
.app-header {
  background: linear-gradient(135deg, #1e40af 0%, #3b82f6 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  box-shadow: 0 4px 12px rgba(30, 64, 175, 0.2);
  height: 60px;
  z-index: 1000;
  position: relative;
}

.header-left {
  display: flex;
  align-items: center;
  min-width: 280px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-text {
  font-size: 20px;
  font-weight: 700;
  letter-spacing: 0.5px;
  background: linear-gradient(90deg, #ffffff 0%, #bfdbfe 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
  min-width: 280px;
  justify-content: flex-end;
}

.notification-badge {
  margin-right: 8px;
}

.notification-badge .el-badge__content {
  background-color: #ef4444;
  border: 2px solid #3b82f6;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 8px 16px;
  border-radius: 8px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  background-color: rgba(255, 255, 255, 0.1);
}

.user-info:hover {
  background-color: rgba(255, 255, 255, 0.2);
  transform: translateY(-1px);
}

.username {
  font-size: 14px;
  font-weight: 500;
  color: white;
}

/* 主容器 */
.main-container {
  height: calc(100vh - 60px);
  overflow: hidden;
}

/* 侧边栏 */
.app-aside {
  background: linear-gradient(180deg, #0f172a 0%, #1e293b 100%);
  overflow-y: auto;
  overflow-x: hidden;
  box-shadow: 4px 0 12px rgba(0, 0, 0, 0.1);
  transition: width 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.el-menu-vertical {
  border-right: none;
  height: 100%;
  background-color: transparent;
  padding: 16px 12px;
}

.el-menu-vertical .el-menu-item {
  margin-bottom: 8px;
  border-radius: 8px;
  color: #94a3b8;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.el-menu-vertical .el-menu-item::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px;
  background: linear-gradient(180deg, #60a5fa 0%, #3b82f6 100%);
  transform: scaleY(0);
  transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.el-menu-vertical .el-menu-item.is-active {
  background: linear-gradient(135deg, #1e40af 0%, #3b82f6 100%);
  color: white;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
}

.el-menu-vertical .el-menu-item.is-active::before {
  transform: scaleY(1);
}

.el-menu-vertical .el-menu-item:hover {
  background-color: rgba(59, 130, 246, 0.1);
  color: #ffffff;
  transform: translateX(4px);
}

.el-menu-vertical .el-menu-item.is-active:hover {
  transform: translateX(0);
}

.el-menu-vertical .el-icon {
  margin-right: 10px;
  font-size: 18px;
  transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.el-menu-vertical .el-menu-item:hover .el-icon {
  transform: scale(1.1);
}

/* 主内容区 */
.app-main {
  background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%);
  padding: 24px;
  overflow-y: auto;
  overflow-x: hidden;
}

.main-content {
  max-width: 1920px;
  margin: 0 auto;
  animation: fadeIn 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

.breadcrumb-wrapper {
  margin-bottom: 20px;
  padding: 12px 20px;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.breadcrumb-wrapper .el-breadcrumb {
  --el-breadcrumb-text-color: #64748b;
  --el-breadcrumb-item-active-color: #3b82f6;
  font-size: 14px;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 滚动条样式 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f5f9;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 4px;
  transition: background 0.3s;
}

::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}

/* Element Plus 组件样式优化 */
.el-card {
  border-radius: 12px;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06);
  border: none;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.el-card:hover {
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
}

.el-button {
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.el-button--primary {
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  border: none;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
}

.el-button--primary:hover {
  background: linear-gradient(135deg, #2563eb 0%, #1d4ed8 100%);
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(59, 130, 246, 0.4);
}

.el-button--success {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  border: none;
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.3);
}

.el-button--success:hover {
  background: linear-gradient(135deg, #059669 0%, #047857 100%);
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(16, 185, 129, 0.4);
}

.el-input__wrapper,
.el-select .el-input__wrapper,
.el-date-editor .el-input__wrapper {
  border-radius: 8px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
}

.el-input__wrapper:hover,
.el-select .el-input__wrapper:hover,
.el-date-editor .el-input__wrapper:hover {
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.1);
}

.el-input__wrapper.is-focus,
.el-select .el-input__wrapper.is-focus,
.el-date-editor .el-input__wrapper.is-focus {
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.2), 0 2px 4px 0 rgba(0, 0, 0, 0.1);
}

.el-table {
  border-radius: 8px;
  overflow: hidden;
}

.el-table th.el-table__cell {
  background-color: #f8fafc;
  color: #475569;
  font-weight: 600;
  text-transform: uppercase;
  font-size: 12px;
  letter-spacing: 0.5px;
}

.el-table tr {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.el-table--enable-row-hover .el-table__body tr:hover > td {
  background-color: #f1f5f9;
}

.el-tag {
  border-radius: 6px;
  font-weight: 500;
  border: none;
}

.el-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.el-pagination button,
.el-pager li {
  border-radius: 6px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.el-pager li.is-active {
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  color: white;
}
</style>
