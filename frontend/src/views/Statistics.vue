<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-label">采购总金额</div>
            <div class="stat-value">¥{{ Number(statistics.totalPurchaseAmount || 0).toLocaleString() }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-label">预算占用金额</div>
            <div class="stat-value">¥{{ Number(statistics.totalBudgetUsed || 0).toLocaleString() }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-label">采购申请数</div>
            <div class="stat-value">{{ statistics.totalApplications || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-label">异常到货率</div>
            <div class="stat-value danger">{{ Number(statistics.abnormalRate || 0).toFixed(2) }}%</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card style="margin-top: 20px">
      <template #header>
        <span>供应商中标统计</span>
      </template>
      <el-table :data="statistics.supplierWinStats || []" border stripe>
        <el-table-column prop="supplierName" label="供应商名称" />
        <el-table-column prop="winCount" label="中标次数" width="120">
          <template #default="{ row }">
            <el-tag type="success">{{ row.winCount }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="score" label="供应商评分" width="150">
          <template #default="{ row }">
            <el-progress :percentage="row.score" :stroke-width="12" />
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card style="margin-top: 20px">
      <template #header>
        <span>部门预算情况</span>
      </template>
      <el-table :data="budgets" border stripe>
        <el-table-column prop="department" label="部门" width="120" />
        <el-table-column prop="totalBudget" label="总预算(元)" width="150">
          <template #default="{ row }">
            ¥{{ Number(row.totalBudget || 0).toLocaleString() }}
          </template>
        </el-table-column>
        <el-table-column prop="usedBudget" label="已用预算(元)" width="150">
          <template #default="{ row }">
            ¥{{ Number(row.usedBudget || 0).toLocaleString() }}
          </template>
        </el-table-column>
        <el-table-column label="预算使用率" width="200">
          <template #default="{ row }">
            <el-progress 
              :percentage="Math.round(Number(row.usedBudget || 0) * 100 / Number(row.totalBudget || 1))" 
              :stroke-width="12"
              :color="getBudgetColor(row)"
            />
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const statistics = ref({})
const budgets = ref([])

const loadStatistics = async () => {
  const res = await axios.get('/api/statistics/summary')
  if (res.data.code === 200) {
    statistics.value = res.data.data
  }
}

const loadBudgets = async () => {
  const res = await axios.get('/api/budget/list')
  if (res.data.code === 200) {
    budgets.value = res.data.data
  }
}

const getBudgetColor = (row) => {
  const rate = Number(row.usedBudget || 0) * 100 / Number(row.totalBudget || 1)
  if (rate >= 90) return '#f56c6c'
  if (rate >= 70) return '#e6a23c'
  return '#67c23a'
}

onMounted(() => {
  loadStatistics()
  loadBudgets()
})
</script>

<style scoped>
.stat-item {
  text-align: center;
  padding: 10px 0;
}
.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}
.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}
.stat-value.danger {
  color: #f56c6c;
}
</style>
