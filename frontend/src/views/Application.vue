<template>
  <div>
    <el-card>
      <template #header>
        <div class="card-header">
          <span>采购申请列表</span>
          <el-button type="primary" @click="showCreateDialog">新建申请</el-button>
        </div>
      </template>
      <el-table :data="applications" border stripe>
        <el-table-column prop="applicant" label="申请人" width="100" />
        <el-table-column prop="department" label="部门" width="100" />
        <el-table-column prop="productName" label="商品名称" />
        <el-table-column prop="quantity" label="数量" width="80" />
        <el-table-column prop="budget" label="预算(元)" width="120">
          <template #default="{ row }">
            ¥{{ row.budget }}
          </template>
        </el-table-column>
        <el-table-column prop="purpose" label="用途" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="350">
          <template #default="{ row }">
            <el-button size="small" @click="$router.push('/quote/' + row.id)">报价管理</el-button>
            <el-button size="small" type="success" @click="recommendSupplier(row)" v-if="row.status === 'PENDING'">推荐供应商</el-button>
            <el-button size="small" type="primary" @click="approve(row)" v-if="row.status === 'PENDING'">通过</el-button>
            <el-button size="small" type="danger" @click="reject(row)" v-if="row.status === 'PENDING'">驳回</el-button>
            <el-button size="small" type="warning" @click="createOrder(row)" v-if="row.status === 'APPROVED'">生成订单</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="createDialogVisible" title="新建采购申请" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="申请人">
          <el-input v-model="form.applicant" />
        </el-form-item>
        <el-form-item label="部门">
          <el-select v-model="form.department" style="width: 100%">
            <el-option label="技术部" value="技术部" />
            <el-option label="市场部" value="市场部" />
            <el-option label="行政部" value="行政部" />
          </el-select>
        </el-form-item>
        <el-form-item label="商品名称">
          <el-input v-model="form.productName" />
        </el-form-item>
        <el-form-item label="数量">
          <el-input-number v-model="form.quantity" :min="1" />
        </el-form-item>
        <el-form-item label="预算(元)">
          <el-input-number v-model="form.budget" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="用途">
          <el-input v-model="form.purpose" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">提交</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="orderDialogVisible" title="生成采购订单" width="500px">
      <el-form :model="orderForm" label-width="100px">
        <el-form-item label="选择供应商">
          <el-select v-model="orderForm.supplierId" style="width: 100%">
            <el-option v-for="s in suppliers" :key="s.id" :label="s.name" :value="s.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="orderDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitOrder">确认生成</el-button>
      </template>
    </el-dialog>

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
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const router = useRouter()
const applications = ref([])
const suppliers = ref([])
const createDialogVisible = ref(false)
const orderDialogVisible = ref(false)
const remarkDialogVisible = ref(false)
const remarkTitle = ref('')
const currentApplication = ref(null)
const actionType = ref('')

const form = ref({
  applicant: '',
  department: '',
  productName: '',
  quantity: 1,
  budget: 0,
  purpose: ''
})

const orderForm = ref({
  supplierId: ''
})

const remarkForm = ref({
  remark: ''
})

const loadApplications = async () => {
  const res = await axios.get('/api/application/list')
  if (res.data.code === 200) {
    applications.value = res.data.data
  }
}

const loadSuppliers = async () => {
  const res = await axios.get('/api/supplier/list')
  if (res.data.code === 200) {
    suppliers.value = res.data.data
  }
}

const showCreateDialog = () => {
  form.value = { applicant: '', department: '', productName: '', quantity: 1, budget: 0, purpose: '' }
  createDialogVisible.value = true
}

const submitForm = async () => {
  const res = await axios.post('/api/application/create', form.value)
  if (res.data.code === 200) {
    ElMessage.success('创建成功')
    createDialogVisible.value = false
    loadApplications()
  } else {
    ElMessage.error(res.data.message)
  }
}

const approve = (row) => {
  currentApplication.value = row
  actionType.value = 'approve'
  remarkTitle.value = '审批通过'
  remarkForm.value.remark = ''
  remarkDialogVisible.value = true
}

const reject = (row) => {
  currentApplication.value = row
  actionType.value = 'reject'
  remarkTitle.value = '审批驳回'
  remarkForm.value.remark = ''
  remarkDialogVisible.value = true
}

const submitRemark = async () => {
  const url = actionType.value === 'approve' 
    ? `/api/application/approve/${currentApplication.value.id}`
    : `/api/application/reject/${currentApplication.value.id}`
  const res = await axios.post(url, remarkForm.value)
  if (res.data.code === 200) {
    ElMessage.success('操作成功')
    remarkDialogVisible.value = false
    loadApplications()
  } else {
    ElMessage.error(res.data.message)
  }
}

const recommendSupplier = async (row) => {
  const res = await axios.get(`/api/application/recommend/${row.id}`)
  if (res.data.code === 200 && res.data.data) {
    const supplier = suppliers.value.find(s => s.id === res.data.data)
    ElMessage.success(`推荐供应商: ${supplier ? supplier.name : res.data.data}`)
  } else {
    ElMessage.info('暂无供应商报价，无法推荐')
  }
}

const createOrder = (row) => {
  currentApplication.value = row
  orderForm.value.supplierId = row.recommendSupplierId || ''
  orderDialogVisible.value = true
}

const submitOrder = async () => {
  const res = await axios.post('/api/order/create', {
    applicationId: currentApplication.value.id,
    supplierId: orderForm.value.supplierId
  })
  if (res.data.code === 200) {
    ElMessage.success('订单生成成功')
    orderDialogVisible.value = false
    loadApplications()
  } else {
    ElMessage.error(res.data.message)
  }
}

const getStatusType = (status) => {
  const map = { PENDING: 'warning', APPROVED: 'success', REJECTED: 'danger', ORDERED: 'primary' }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = { PENDING: '待审批', APPROVED: '已通过', REJECTED: '已驳回', ORDERED: '已下单' }
  return map[status] || status
}

onMounted(() => {
  loadApplications()
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
