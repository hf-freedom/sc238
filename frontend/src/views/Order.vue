<template>
  <div>
    <el-card>
      <template #header>
        <div class="card-header">
          <span>订单列表</span>
        </div>
      </template>
      <el-table :data="orders" border stripe>
        <el-table-column prop="supplierName" label="供应商" width="150" />
        <el-table-column prop="productName" label="商品名称" />
        <el-table-column prop="quantity" label="数量" width="80" />
        <el-table-column prop="price" label="单价(元)" width="100">
          <template #default="{ row }">
            ¥{{ row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="总金额(元)" width="120">
          <template #default="{ row }">
            ¥{{ row.totalAmount }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="250">
          <template #default="{ row }">
            <el-button size="small" type="success" @click="confirmDelivery(row)" v-if="row.status === 'PENDING'">确认到货</el-button>
            <el-button size="small" type="primary" @click="acceptOrder(row)" v-if="row.status === 'RECEIVED'">验收通过</el-button>
            <el-button size="small" type="danger" @click="rejectOrder(row)" v-if="row.status === 'RECEIVED'">验收异常</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="remarkDialogVisible" :title="remarkTitle" width="400px">
      <el-form :model="remarkForm" label-width="60px">
        <el-form-item label="备注">
          <el-input v-model="remarkForm.remark" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="remarkDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRemark">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const orders = ref([])
const remarkDialogVisible = ref(false)
const remarkTitle = ref('')
const currentOrder = ref(null)
const actionType = ref('')

const remarkForm = ref({
  remark: ''
})

const loadOrders = async () => {
  const res = await axios.get('/api/order/list')
  if (res.data.code === 200) {
    orders.value = res.data.data
  }
}

const confirmDelivery = async (row) => {
  const res = await axios.post(`/api/order/delivery/${row.id}`)
  if (res.data.code === 200) {
    ElMessage.success('已确认到货')
    loadOrders()
  } else {
    ElMessage.error(res.data.message)
  }
}

const acceptOrder = (row) => {
  currentOrder.value = row
  actionType.value = 'accept'
  remarkTitle.value = '验收通过'
  remarkForm.value.remark = ''
  remarkDialogVisible.value = true
}

const rejectOrder = (row) => {
  currentOrder.value = row
  actionType.value = 'reject'
  remarkTitle.value = '验收异常'
  remarkForm.value.remark = ''
  remarkDialogVisible.value = true
}

const submitRemark = async () => {
  const url = actionType.value === 'accept' 
    ? `/api/order/accept/${currentOrder.value.id}`
    : `/api/order/reject/${currentOrder.value.id}`
  const res = await axios.post(url, remarkForm.value)
  if (res.data.code === 200) {
    ElMessage.success('操作成功')
    remarkDialogVisible.value = false
    loadOrders()
  } else {
    ElMessage.error(res.data.message)
  }
}

const getStatusType = (status) => {
  const map = { PENDING: 'warning', RECEIVED: 'primary', ACCEPTED: 'success', REJECTED: 'danger' }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = { PENDING: '待到货', RECEIVED: '已到货', ACCEPTED: '验收通过', REJECTED: '验收异常' }
  return map[status] || status
}

onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
