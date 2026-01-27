<template>
  <el-container class="app-container">
    <el-aside width="200px">
      <el-menu
        :default-active="activeMenu"
        router
        class="el-menu-vertical"
      >
        <el-menu-item index="/certificate-type">
          <el-icon><Document /></el-icon>
          <span>证件类型配置</span>
        </el-menu-item>
        <el-menu-item index="/scope-config">
          <el-icon><Setting /></el-icon>
          <span>应用范围配置</span>
        </el-menu-item>
        <el-menu-item index="/validation-rule">
          <el-icon><Checked /></el-icon>
          <span>校验规则配置</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-main>
      <router-view />
    </el-main>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { Document, Setting, Checked } from '@element-plus/icons-vue'

const route = useRoute()
const activeMenu = computed(() => route.path)
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

#app {
  width: 100%;
  height: 100vh;
}

.app-container {
  height: 100%;
}

.el-aside {
  background-color: #545c64;
}

.el-menu-vertical {
  border-right: none;
  height: 100%;
}

.el-main {
  padding: 0;
  background-color: #f5f7fa;
}
</style>
