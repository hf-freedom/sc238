<template>
  <el-container style="height: 100vh">
    <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
      <el-menu :default-active="activeMenu" router style="height: 100%">
        <el-menu-item index="/application">
          <el-icon><ShoppingCart /></el-icon>
          <span>采购申请</span>
        </el-menu-item>
        <el-menu-item index="/supplier">
          <el-icon><OfficeBuilding /></el-icon>
          <span>供应商管理</span>
        </el-menu-item>
        <el-menu-item index="/order">
          <el-icon><Document /></el-icon>
          <span>订单管理</span>
        </el-menu-item>
        <el-menu-item index="/statistics">
          <el-icon><DataLine /></el-icon>
          <span>统计报表</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="text-align: right; font-size: 14px; background: #fff; border-bottom: 1px solid #eee">
        <span style="margin-right: 20px">采购比价系统</span>
      </el-header>
      <el-main style="background-color: #f5f7fa">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { ShoppingCart, OfficeBuilding, Document, DataLine } from '@element-plus/icons-vue'

const route = useRoute()
const activeMenu = computed(() => route.path)
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
</style>
