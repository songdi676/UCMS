<template>
  <div class="scope-config-container">
    <el-card class="main-card">
      <!-- 页面标题 -->
      <template #header>
        <div class="card-header">
          <div class="header-left">
            <el-icon :size="20" color="#3b82f6"><Setting /></el-icon>
            <span class="header-title">应用范围配置</span>
          </div>
          <el-button type="primary" :icon="Plus" @click="handleAdd">新增配置</el-button>
        </div>
      </template>

      <!-- 查询表单 -->
      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="应用系统">
          <el-select v-model="queryForm.businessChannel" placeholder="请选择应用系统" clearable style="width: 180px;">
            <el-option label="全部" :value="null" />
            <el-option label="移动云厅" value="mobile-cloud">
              <span>移动云厅</span>
              <el-icon color="#3b82f6"><Monitor /></el-icon>
            </el-option>
            <el-option label="网格通" value="grid">
              <span>网格通</span>
              <el-icon color="#10b981"><Grid /></el-icon>
            </el-option>
            <el-option label="便利店" value="convenience-store">
              <span>便利店</span>
              <el-icon color="#f59e0b"><Shop /></el-icon>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="渠道小类">
          <el-input v-model="queryForm.channelSubType" placeholder="请输入渠道小类" clearable prefix-icon="Search" />
        </el-form-item>
        <el-form-item label="地市">
          <el-select v-model="queryForm.city" placeholder="请选择地市" clearable style="width: 150px;">
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
          <el-input v-model="queryForm.district" placeholder="请输入区县" clearable prefix-icon="Location" />
        </el-form-item>
        <el-form-item label="机构">
          <el-input v-model="queryForm.institution" placeholder="请输入机构" clearable prefix-icon="OfficeBuilding" />
        </el-form-item>
        <el-form-item label="证件类型">
          <el-input v-model="queryForm.certTypeCode" placeholder="请输入证件类型代码" clearable prefix-icon="Document" />
        </el-form-item>
        <el-form-item class="form-actions">
          <el-button type="primary" :icon="Search" @click="handleQuery">查询</el-button>
          <el-button :icon="RefreshLeft" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 数据表格 -->
      <el-table :data="tableData" border style="width: 100%" class="scope-table">
        <el-table-column prop="businessChannel" label="应用系统" width="140">
          <template #default="{ row }">
            <div class="channel-cell">
              <el-icon :size="16" color="#3b82f6"><Monitor /></el-icon>
              <span>{{ getBusinessChannelName(row.businessChannel) }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="channelSubType" label="渠道小类" width="120" />
        <el-table-column prop="city" label="地市" width="120" />
        <el-table-column prop="district" label="区县" width="120" />
        <el-table-column prop="institution" label="机构" width="150" />
        <el-table-column prop="allowedCertTypes" label="证件类型" min-width="220">
          <template #default="{ row }">
            <div class="cert-tags">
              <el-tag
                v-for="certType in parseAllowedCertTypes(row.allowedCertTypes)"
                :key="certType"
                type="primary"
                size="small"
                effect="plain"
              >
                {{ getCertTypeName(certType) }}
              </el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'" effect="dark" size="large">
              <el-icon v-if="row.status === 1"><CircleCheckFilled /></el-icon>
              <el-icon v-else><CircleCloseFilled /></el-icon>
              <span style="margin-left: 4px;">{{ row.status === 1 ? '启用' : '停用' }}</span>
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdBy" label="创建人" width="100" />
        <el-table-column prop="createdAt" label="创建时间" width="180" />
        <el-table-column prop="updatedBy" label="修改人" width="100" />
        <el-table-column prop="updatedAt" label="修改时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button
              type="primary"
              :icon="Edit"
              size="small"
              @click="handleEdit(row)"
            >
              编辑
            </el-button>
            <el-button
              type="danger"
              :icon="Delete"
              size="small"
              @click="handleDelete(row)"
            >
              删除
            </el-button>
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
      />
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="800px"
      @close="handleDialogClose"
      class="form-dialog"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="140px">
        <el-form-item label="应用系统" prop="businessChannel">
          <el-select v-model="form.businessChannel" placeholder="请选择应用系统" size="large" style="width: 100%;">
            <el-option label="移动云厅" value="mobile-cloud">
              <span>移动云厅</span>
              <el-icon color="#3b82f6"><Monitor /></el-icon>
            </el-option>
            <el-option label="网格通" value="grid">
              <span>网格通</span>
              <el-icon color="#10b981"><Grid /></el-icon>
            </el-option>
            <el-option label="便利店" value="convenience-store">
              <span>便利店</span>
              <el-icon color="#f59e0b"><Shop /></el-icon>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="渠道小类">
          <el-input v-model="form.channelSubType" placeholder="请输入渠道小类" size="large" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="地市">
              <el-select v-model="form.city" placeholder="请选择地市" filterable size="large" style="width: 100%;">
                <el-option label="全部" :value="null" />
                <el-option
                  v-for="city in cities"
                  :key="city.code"
                  :label="city.name"
                  :value="city.code"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="区县">
              <el-input v-model="form.district" placeholder="请输入区县" size="large" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="机构">
          <el-input v-model="form.institution" placeholder="请输入机构" size="large" />
        </el-form-item>
        <el-form-item label="证件类型" prop="allowedCertTypes">
          <el-select v-model="form.allowedCertTypes" placeholder="请选择证件类型" multiple size="large" style="width: 100%;">
            <el-option
              v-for="certType in certificateTypes"
              :key="certType.certTypeCode"
              :label="certType.certTypeName"
              :value="certType.certTypeCode"
            >
              <span>{{ certType.certTypeName }}</span>
              <el-icon style="margin-left: 8px;" color="#3b82f6"><Document /></el-icon>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status" size="large">
            <el-radio :label="1" border>
              <el-icon color="#10b981"><CircleCheckFilled /></el-icon>
              <span style="margin-left: 8px;">启用</span>
            </el-radio>
            <el-radio :label="0" border>
              <el-icon color="#94a3b8"><CircleCloseFilled /></el-icon>
              <span style="margin-left: 8px;">停用</span>
            </el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false" size="large">取消</el-button>
          <el-button type="primary" @click="handleSubmit" size="large">
            <el-icon><Check /></el-icon>
            <span style="margin-left: 4px;">保存</span>
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Plus, Edit, Delete, Search, RefreshLeft,
  Monitor, Grid, Shop, CircleCheckFilled, CircleCloseFilled, Document, Check
} from '@element-plus/icons-vue'
import {
  getScopeConfigList,
  createScopeConfig,
  updateScopeConfig,
  deleteScopeConfig
} from '@/api/scope-config'
import { getCertificateTypeList } from '@/api/certificate-type'

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
  businessChannel: [{ required: true, message: '请选择应用系统', trigger: 'change' }],
  allowedCertTypes: [{ required: true, message: '请至少选择一个证件类型', trigger: 'change' }]
}

// 地市列表
const cities = ref([
  { code: '110000', name: '北京市' },
  { code: '120000', name: '天津市' }
])

// 证件类型列表
const certificateTypes = ref([])

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
  const certType = certificateTypes.value.find(t => t.certTypeCode === code)
  return certType ? certType.certTypeName : code
}

// 解析允许的证件类型
const parseAllowedCertTypes = (typesStr) => {
  if (!typesStr) return []
  return typesStr.split('|').map(t => t.trim())
}

// 加载数据
const loadData = async () => {
  try {
    // 并行加载证件类型列表
    const [scopeRes, certTypeRes] = await Promise.all([
      getScopeConfigList({
        page: pagination.page,
        pageSize: pagination.pageSize,
        ...queryForm
      }),
      getCertificateTypeList({
        page: 1,
        pageSize: 1000
      })
    ])
    
    // 后端返回MyBatis-Plus的IPage结构: {records: [], total: 0, ...}
    tableData.value = scopeRes.records || []
    pagination.total = scopeRes.total || 0
    
    // 加载证件类型列表
    certificateTypes.value = Array.isArray(certTypeRes.records) ? certTypeRes.records : []
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
    type: 'warning',
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  }).then(async () => {
    try {
      await deleteScopeConfig(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
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
      try {
        // 将数组转换为竖线分隔的字符串（使用|分隔符）
        const submitData = {
          ...form,
          allowedCertTypes: form.allowedCertTypes.join('|')
        }

        if (form.id) {
          // 更新
          await updateScopeConfig(form.id, submitData)
        } else {
          // 创建
          await createScopeConfig(submitData)
        }
        ElMessage.success('保存成功')
        dialogVisible.value = false
        loadData()
      } catch (error) {
        ElMessage.error('操作失败')
      }
    }
  })
}

// 分页大小变化
const handleSizeChange = (val) => {
  pagination.pageSize = val
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
  max-width: 1920px;
  margin: 0 auto;
}

.main-card {
  animation: slideIn 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.header-title {
  font-size: 18px;
  font-weight: 600;
  color: #1e293b;
}

.query-form {
  margin-bottom: 24px;
  padding: 20px;
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  border-radius: 12px;
  border: 1px solid #e2e8f0;
}

.form-actions {
  display: flex;
  gap: 12px;
}

.form-actions .el-form-item__content {
  flex-wrap: nowrap;
}

.scope-table {
  margin-bottom: 20px;
  border-radius: 8px;
  overflow: hidden;
}

.channel-cell {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
}

.cert-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.cert-tags .el-tag {
  font-weight: 500;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border-radius: 6px;
}

.cert-tags .el-tag:hover {
  transform: translateY(-2px) scale(1.02);
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.2);
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #475569;
}

:deep(.el-form-item__content) {
  border-radius: 8px;
}

:deep(.el-input__inner) {
  border-radius: 8px;
}

:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-table th.el-table__cell) {
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  color: #475569;
  font-weight: 600;
  text-transform: uppercase;
  font-size: 12px;
  letter-spacing: 0.5px;
}

:deep(.el-table tr) {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

:deep(.el-table--enable-row-hover .el-table__body tr:hover > td) {
  background-color: #f1f5f9;
}

:deep(.el-tag--dark.el-tag--success) {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  border: none;
}

:deep(.el-radio.is-bordered) {
  border-radius: 8px;
  padding: 12px 20px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

:deep(.el-radio.is-bordered:hover) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.2);
}

:deep(.el-dialog) {
  border-radius: 16px;
  overflow: hidden;
}

:deep(.el-dialog__header) {
  padding: 24px 24px 16px;
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  border-bottom: 1px solid #e2e8f0;
}

:deep(.el-dialog__body) {
  padding: 24px;
}

:deep(.el-dialog__footer) {
  padding: 16px 24px;
  background: #f8fafc;
  border-top: 1px solid #e2e8f0;
}
</style>
