<template>
  <div>
    <el-card>
      <template #header>
        <div class="card-header">
          <span>供应商列表</span>
          <el-button type="primary" @click="showCreateDialog">新增供应商</el-button>
        </div>
      </template>
      <el-table :data="suppliers" border stripe>
        <el-table-column prop="name" label="供应商名称" />
        <el-table-column prop="contact" label="联系人" width="120" />
        <el-table-column prop="phone" label="联系电话" width="140" />
        <el-table-column prop="score" label="评分" width="100">
          <template #default="{ row }">
            <el-rate v-model="row.score" disabled show-score text-color="#ff9900" />
          </template>
        </el-table-column>
        <el-table-column prop="winCount" label="中标次数" width="100" />
      </el-table>
    </el-card>

    <el-dialog v-model="createDialogVisible" title="新增供应商" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="供应商名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="联系人">
          <el-input v-model="form.contact" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="评分">
          <el-rate v-model="form.score" show-score text-color="#ff9900" />
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
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const suppliers = ref([])
const createDialogVisible = ref(false)

const form = ref({
  name: '',
  contact: '',
  phone: '',
  score: 80
})

const loadSuppliers = async () => {
  const res = await axios.get('/api/supplier/list')
  if (res.data.code === 200) {
    suppliers.value = res.data.data
  }
}

const showCreateDialog = () => {
  form.value = { name: '', contact: '', phone: '', score: 80 }
  createDialogVisible.value = true
}

const submitForm = async () => {
  const res = await axios.post('/api/supplier/create', form.value)
  if (res.data.code === 200) {
    ElMessage.success('创建成功')
    createDialogVisible.value = false
    loadSuppliers()
  } else {
    ElMessage.error(res.data.message)
  }
}

onMounted(() => {
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
