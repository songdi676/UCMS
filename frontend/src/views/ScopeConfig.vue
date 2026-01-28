<template>
  <div class="scope-config-container">
    <el-card>
      <!-- 查询表单 -->
      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="应用系统">
          <el-select v-model="queryForm.businessChannel" placeholder="请选择" clearable>
            <el-option label="全部" :value="null" />
            <el-option label="移动云厅" value="mobile-cloud" />
            <el-option label="网格通" value="grid" />
            <el-option label="便利店" value="convenience-store" />
          </el-select>
        </el-form-item>
        <el-form-item label="渠道小类">
          <el-input v-model="queryForm.channelSubType" placeholder="请输入渠道小类" clearable />
        </el-form-item>
        <el-form-item label="地市">
          <el-select v-model="queryForm.city" placeholder="请选择" clearable>
            <el-option label="全部" :value="null" />
            <!-- 这里需要调用外部API获取地市列表 -->
          </el-select>
        </el-form-item>
        <el-form-item label="区县">
          <el-input v-model="queryForm.district" placeholder="请输入区县" clearable />
        </el-form-item>
        <el-form-item label="机构">
          <el-input v-model="queryForm.institution" placeholder="请输入机构" clearable />
        </el-form-item>
        <el-form-item label="证件类型">
          <el-input v-model="queryForm.certTypeCode" placeholder="请输入证件类型代码" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="success" @click="handleAdd">新增配置</el-button>
        </el-form-item>
      </el-form>

      <!-- 数据表格 -->
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="businessChannel" label="应用系统" width="120">
          <template #default="{ row }">
            {{ getBusinessChannelName(row.businessChannel) }}
          </template>
        </el-table-column>
        <el-table-column prop="channelSubType" label="渠道小类" width="120" />
        <el-table-column prop="city" label="地市" width="100" />
        <el-table-column prop="district" label="区县" width="100" />
        <el-table-column prop="institution" label="机构" width="150" />
        <el-table-column prop="allowedCertTypes" label="允许的证件类型" width="200">
          <template #default="{ row }">
            <el-tag
              v-for="certType in parseAllowedCertTypes(row.allowedCertTypes)"
              :key="certType"
              size="small"
              style="margin-right: 5px;"
            >
              {{ getCertTypeName(certType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdBy" label="创建人" width="100" />
        <el-table-column prop="createdAt" label="创建时间" width="180" />
        <el-table-column prop="updatedBy" label="修改人" width="100" />
        <el-table-column prop="updatedAt" label="修改时间" width="180" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button
              type="primary"
              :icon="Edit"
              circle
              size="small"
              @click="handleEdit(row)"
            />
            <el-button
              type="danger"
              :icon="Delete"
              circle
              size="small"
              @click="handleDelete(row)"
            />
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        style="margin-top: 20px; justify-content: flex-end;"
      />
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="800px"
      @close="handleDialogClose"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item label="应用系统">
          <el-select v-model="form.businessChannel" placeholder="请选择应用系统">
            <el-option label="移动云厅" value="mobile-cloud" />
            <el-option label="网格通" value="grid" />
            <el-option label="便利店" value="convenience-store" />
          </el-select>
        </el-form-item>
        <el-form-item label="渠道小类">
          <el-input v-model="form.channelSubType" placeholder="请输入渠道小类" />
        </el-form-item>
        <el-form-item label="地市">
          <el-select v-model="form.city" placeholder="请选择地市" filterable>
            <el-option label="全部" :value="null" />
            <el-option
              v-for="city in cities"
              :key="city.code"
              :label="city.name"
              :value="city.code"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="区县">
          <el-input v-model="form.district" placeholder="请输入区县" />
        </el-form-item>
        <el-form-item label="机构">
          <el-input v-model="form.institution" placeholder="请输入机构" />
        </el-form-item>
        <el-form-item label="证件类型">
          <el-select v-model="form.allowedCertTypes" placeholder="请选择证件类型" multiple>
            <el-option
              v-for="certType in certificateTypes"
              :key="certType.code"
              :label="certType.name"
              :value="certType.code"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Edit, Delete } from '@element-plus/icons-vue'

// 查询表单
const queryForm = reactive({
  businessChannel: null,
  channelSubType: '',
  city: null,
  district: '',
  institution: '',
  certTypeCode: ''
})

// 表格数据
const tableData = ref([])

// 分页
const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

// 对话框
const dialogVisible = ref(false)
const dialogTitle = ref('新增应用范围配置')
const formRef = ref(null)

// 表单数据
const form = reactive({
  id: null,
  businessChannel: null,
  channelSubType: '',
  city: null,
  district: '',
  institution: '',
  allowedCertTypes: [],
  status: 1
})

// 表单验证规则
const rules = {
  businessChannel: [{ required: true, message: '请选择应用系统', trigger: 'change' }]
}

// 地市列表（暂时静态数据，后续需要调用外部API）
const cities = ref([
  { code: '110000', name: '北京市' },
  { code: '120000', name: '天津市' }
])

// 证件类型列表（暂时静态数据，后续需要调用后端API）
const certificateTypes = ref([
  { code: '1', name: '身份证' },
  { code: '3', name: '护照' },
  { code: '14', name: '武装警察身份证' }
])

// 应用系统名称映射
const businessChannelMap = {
  'mobile-cloud': '移动云厅',
  'grid': '网格通',
  'convenience-store': '便利店'
}

// 获取应用系统名称
const getBusinessChannelName = (code) => {
  return businessChannelMap[code] || code
}

// 获取证件类型名称
const getCertTypeName = (code) => {
  const certType = certificateTypes.value.find(t => t.code === code)
  return certType ? certType.name : code
}

// 解析允许的证件类型
const parseAllowedCertTypes = (typesStr) => {
  if (!typesStr) return []
  return typesStr.split(',').map(t => t.trim())
}

// 加载数据
const loadData = async () => {
  try {
    // 暂时使用静态数据
    tableData.value = [
      {
        id: 1,
        businessChannel: 'mobile-cloud',
        channelSubType: '云厅',
        city: '110000',
        district: '',
        institution: '',
        allowedCertTypes: '1,3',
        status: 1,
        createdBy: 'admin',
        updatedBy: 'admin',
        createdAt: '2026-01-27 10:00:00',
        updatedAt: '2026-01-27 10:00:00'
      }
    ]
    pagination.total = 1
  } catch (error) {
    ElMessage.error('加载数据失败')
  }
}

// 查询
const handleQuery = () => {
  pagination.page = 1
  loadData()
}

// 重置
const handleReset = () => {
  Object.assign(queryForm, {
    businessChannel: null,
    channelSubType: '',
    city: null,
    district: '',
    institution: '',
    certTypeCode: ''
  })
  pagination.page = 1
  loadData()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增应用范围配置'
  Object.assign(form, {
    id: null,
    businessChannel: null,
    channelSubType: '',
    city: null,
    district: '',
    institution: '',
    allowedCertTypes: [],
    status: 1
  })
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑应用范围配置'
  Object.assign(form, {
    id: row.id,
    businessChannel: row.businessChannel,
    channelSubType: row.channelSubType,
    city: row.city,
    district: row.district,
    institution: row.institution,
    allowedCertTypes: parseAllowedCertTypes(row.allowedCertTypes),
    status: row.status
  })
  dialogVisible.value = true
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该应用范围配置吗？', '提示', {
    type: 'warning'
  }).then(() => {
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

// 对话框关闭
const handleDialogClose = () => {
  formRef.value?.resetFields()
}

// 提交
const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      ElMessage.success('保存成功')
      dialogVisible.value = false
      loadData()
    }
  })
}

// 分页大小变化
const handleSizeChange = (val) => {
  pagination.pageSize = val
  pagination.page = 1
  loadData()
}

// 当前页变化
const handleCurrentChange = (val) => {
  pagination.page = val
  loadData()
}

// 页面加载时初始化
onMounted(() => {
  loadData()
})
</script>

<style scoped>
.scope-config-container {
  padding: 20px;
}

.query-form {
  margin-bottom: 20px;
}
</style>
