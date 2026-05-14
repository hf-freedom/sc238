<template>
  <div>
    <el-card v-if="recommendation.bestSupplierName" style="margin-bottom: 20px" shadow="hover">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center">
          <span style="font-weight: bold; color: #67c23a">🏆 系统推荐供应商</span>
          <el-tag type="success" size="large">综合评分: {{ Number(recommendation.bestTotalScore).toFixed(2) }}</el-tag>
        </div>
      </template>
      <div style="text-align: center; padding: 20px">
        <h2 style="color: #67c23a; margin-bottom: 10px">{{ recommendation.bestSupplierName }}</h2>
        <p style="color: #909399">基于价格(50%)、交货期(30%)、供应商评分(20%)综合计算推荐</p>
      </div>
    </el-card>

    <el-card>
      <template #header>
        <div class="card-header">
          <span>报价管理 - {{ applicationId }}</span>
          <el-button @click="$router.back()">返回</el-button>
          <div>
            <el-button type="success" @click="calculateRecommendation" :disabled="quotes.length === 0">
              计算推荐供应商
            </el-button>
            <el-button type="primary" @click="showCreateDialog">新增报价</el-button>
          </div>
        </div>
      </template>

      <el-alert v-if="quotes.length === 0" title="暂无报价数据，请先添加供应商报价" type="info" :closable="false" style="margin-bottom: 20px" />

      <el-table :data="quotesWithScore" border stripe>
        <el-table-column type="index" label="排名" width="80">
          <template #default="{ $index }">
            <el-tag v-if="$index === 0" type="success" size="small">第1名</el-tag>
            <span v-else>第{{ $index + 1 }}名</span>
          </template>
        </el-table-column>
        <el-table-column prop="supplierName" label="供应商" min-width="120" />
        <el-table-column prop="price" label="单价(元)" width="120">
          <template #default="{ row }">
            ¥{{ Number(row.price).toLocaleString() }}
          </template>
        </el-table-column>
        <el-table-column prop="deliveryDays" label="交货天数" width="100">
          <template #default="{ row }">
            <el-tag :type="row.deliveryDays <= 5 ? 'success' : row.deliveryDays <= 10 ? 'warning' : 'danger'" size="small">
              {{ row.deliveryDays }}天
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="价格评分" width="100">
          <template #default="{ row }">
            <el-progress :percentage="Number(row.priceScore || 0).toFixed(0)" :stroke-width="10" color="#409eff" />
          </template>
        </el-table-column>
        <el-table-column label="交期评分" width="100">
          <template #default="{ row }">
            <el-progress :percentage="Number(row.deliveryScore || 0).toFixed(0)" :stroke-width="10" color="#67c23a" />
          </template>
        </el-table-column>
        <el-table-column label="供应商评分" width="100">
          <template #default="{ row }">
            <el-progress :percentage="Number(row.supplierScore || 0).toFixed(0)" :stroke-width="10" color="#e6a23c" />
          </template>
        </el-table-column>
        <el-table-column label="综合评分" width="120">
          <template #default="{ row }">
            <el-tag :type="row.totalScore >= 80 ? 'success' : row.totalScore >= 60 ? 'warning' : 'info'" size="large">
              {{ Number(row.totalScore || 0).toFixed(2) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="120" />
        <el-table-column prop="createTime" label="报价时间" width="180" />
      </el-table>

      <el-divider content-position="left">比价规则说明</el-divider>
      <el-descriptions :column="3" border size="small">
        <el-descriptions-item label="价格权重">50%</el-descriptions-item>
        <el-descriptions-item label="交期权重">30%</el-descriptions-item>
        <el-descriptions-item label="供应商评分权重">20%</el-descriptions-item>
        <el-descriptions-item label="价格评分">
          价格越低评分越高，公式: max(0, 100 - 价格/1000)
        </el-descriptions-item>
        <el-descriptions-item label="交期评分">
          交货越快评分越高，公式: max(0, 100 - 天数*5)
        </el-descriptions-item>
        <el-descriptions-item label="综合评分">
          价格*50% + 交期*30% + 供应商*20%
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-dialog v-model="createDialogVisible" title="新增报价" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="供应商">
          <el-select v-model="form.supplierId" style="width: 100%">
            <el-option v-for="s in suppliers" :key="s.id" :label="s.name" :value="s.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="单价(元)">
          <el-input-number v-model="form.price" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="交货天数">
          <el-input-number v-model="form.deliveryDays" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const route = useRoute()
const applicationId = route.params.applicationId
const quotes = ref([])
const suppliers = ref([])
const createDialogVisible = ref(false)
const recommendation = ref({})

const form = ref({
  supplierId: '',
  price: 0,
  deliveryDays: 1,
  remark: ''
})

const quotesWithScore = computed(() => {
  if (!recommendation.value.supplierScores || recommendation.value.supplierScores.length === 0) {
    return quotes.value
  }
  return quotes.value.map(q => {
    const score = recommendation.value.supplierScores.find(s => s.supplierId === q.supplierId)
    return score ? { ...q, ...score } : q
  }).sort((a, b) => (b.totalScore || 0) - (a.totalScore || 0))
})

const loadQuotes = async () => {
  const res = await axios.get(`/api/quote/list/${applicationId}`)
  if (res.data.code === 200) {
    quotes.value = res.data.data
  }
}

const loadSuppliers = async () => {
  const res = await axios.get('/api/supplier/list')
  if (res.data.code === 200) {
    suppliers.value = res.data.data
  }
}

const calculateRecommendation = async () => {
  const res = await axios.get(`/api/application/recommend/detail/${applicationId}`)
  if (res.data.code === 200) {
    recommendation.value = res.data.data
    if (res.data.data.bestSupplierName) {
      ElMessage.success(`已计算推荐供应商: ${res.data.data.bestSupplierName}`)
    } else {
      ElMessage.info('报价数量不足，无法计算推荐')
    }
  }
}

const showCreateDialog = () => {
  form.value = { supplierId: '', price: 0, deliveryDays: 1, remark: '' }
  createDialogVisible.value = true
}

const submitForm = async () => {
  const data = { ...form.value, applicationId }
  const res = await axios.post('/api/quote/create', data)
  if (res.data.code === 200) {
    ElMessage.success('报价成功')
    createDialogVisible.value = false
    loadQuotes()
    if (recommendation.value.bestSupplierName) {
      await calculateRecommendation()
    }
  } else {
    ElMessage.error(res.data.message)
  }
}

onMounted(() => {
  loadQuotes()
  loadSuppliers()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
