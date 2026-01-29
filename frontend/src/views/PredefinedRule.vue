<template>
  <div class="predefined-rule-container">
    <el-card class="main-card">
      <!-- 页面标题 -->
      <template #header>
        <div class="card-header">
          <div class="header-left">
            <el-icon :size="20" color="#6366f1"><DocumentCopy /></el-icon>
            <span class="header-title">预置规则管理</span>
          </div>
          <el-button type="primary" :icon="Plus" @click="handleAdd" class="add-btn">新增规则</el-button>
        </div>
      </template>

      <!-- 查询表单 -->
      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="证件类型">
          <el-select v-model="queryForm.certTypeCode" placeholder="请选择证件类型" clearable prefix-icon="Search">
            <el-option
              v-for="certType in certificateTypes"
              :key="certType.certTypeCode"
              :label="certType.certTypeName"
              :value="certType.certTypeCode"
            >
              <span>{{ certType.certTypeName }}</span>
              <el-icon style="margin-left: 8px;" color="#6366f1"><Document /></el-icon>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="规则名称">
          <el-input v-model="queryForm.ruleName" placeholder="请输入规则名称" clearable prefix-icon="Search" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable prefix-icon="CircleCheckFilled">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item class="form-actions">
          <el-button type="primary" :icon="Search" @click="handleQuery" class="query-btn">查询</el-button>
          <el-button :icon="RefreshLeft" @click="handleReset" class="reset-btn">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 数据表格 -->
      <el-table :data="tableData" border style="width: 100%" class="rule-table">
        <el-table-column prop="id" label="ID" width="80" align="center">
          <template #default="{ row }">
            <el-tag type="info" size="small" effect="plain">{{ row.id }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="certTypeCode" label="证件类型" width="150">
          <template #default="{ row }">
            <div class="cert-type-display">
              <el-icon color="#6366f1"><Document /></el-icon>
              <span>{{ getCertTypeName(row.certTypeCode) }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="fieldId" label="字段ID" width="140" />
        <el-table-column prop="ruleName" label="规则名称" width="180" />
        <el-table-column prop="validationLogic" label="校验逻辑" width="120">
          <template #default="{ row }">
            <el-tag :type="getLogicType(row.validationLogic)" size="small" effect="plain">
              {{ row.validationLogic }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="validationValue" label="校验值" min-width="180">
          <template #default="{ row }">
            <code class="validation-value">{{ row.validationValue }}</code>
          </template>
        </el-table-column>
        <el-table-column prop="errorMessage" label="错误信息" min-width="200" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(row)"
              class="status-switch"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button
              type="primary"
              :icon="Edit"
              size="small"
              @click="handleEdit(row)"
              class="edit-btn"
            >
              编辑
            </el-button>
            <el-button
              type="danger"
              :icon="Delete"
              size="small"
              @click="handleDelete(row)"
              class="delete-btn"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          class="custom-pagination"
        />
      </div>
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
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="证件类型" prop="certTypeCode">
              <el-select v-model="form.certTypeCode" placeholder="请选择证件类型" size="large" style="width: 100%;">
                <el-option
                  v-for="certType in certificateTypes"
                  :key="certType.certTypeCode"
                  :label="certType.certTypeName"
                  :value="certType.certTypeCode"
                >
                  <span>{{ certType.certTypeName }}</span>
                  <el-icon style="margin-left: 8px;" color="#6366f1"><Document /></el-icon>
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="字段ID" prop="fieldId">
              <el-input v-model="form.fieldId" placeholder="请输入字段ID" size="large" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="规则名称" prop="ruleName">
          <el-input v-model="form.ruleName" placeholder="请输入规则名称" size="large" />
        </el-form-item>
        <el-form-item label="校验逻辑" prop="validationLogic">
          <el-select v-model="form.validationLogic" placeholder="请选择校验逻辑" size="large" style="width: 100%;">
            <el-option label="等于" value="equals" />
            <el-option label="不等于" value="not_equals" />
            <el-option label="大于" value="greater_than" />
            <el-option label="小于" value="less_than" />
            <el-option label="包含" value="contains" />
            <el-option label="不包含" value="not_contains" />
            <el-option label="正则匹配" value="regex" />
            <el-option label="长度等于" value="length_equals" />
            <el-option label="长度大于" value="length_greater" />
            <el-option label="长度小于" value="length_less" />
          </el-select>
        </el-form-item>
        <el-form-item label="校验值" prop="validationValue">
          <el-input
            v-model="form.validationValue"
            placeholder="请输入校验值"
            size="large"
            type="textarea"
            :rows="3"
          />
        </el-form-item>
        <el-form-item label="错误信息" prop="errorMessage">
          <el-input
            v-model="form.errorMessage"
            placeholder="请输入错误提示信息"
            size="large"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status" size="large">
            <el-radio :label="1" border>
              <el-icon color="#10b981"><CircleCheckFilled /></el-icon>
              <span style="margin-left: 8px;">启用</span>
            </el-radio>
            <el-radio :label="0" border>
              <el-icon color="#94a3b8"><CircleCloseFilled /></el-icon>
              <span style="margin-left: 8px;">禁用</span>
            </el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false" size="large" class="cancel-btn">取消</el-button>
          <el-button type="primary" @click="handleSubmit" size="large" class="submit-btn">
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
  CircleCheckFilled, CircleCloseFilled, Document,
  DocumentCopy, Check
} from '@element-plus/icons-vue'
import {
  getPredefinedRuleList,
  createPredefinedRule,
  updatePredefinedRule,
  deletePredefinedRule
} from '@/api/predefined-rule'
import { getCertificateTypeList } from '@/api/certificate-type'

// 查询表单
const queryForm = reactive({
  certTypeCode: '',
  ruleName: '',
  status: ''
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
const dialogTitle = ref('新增预置规则')
const formRef = ref(null)

// 表单数据
const form = reactive({
  id: null,
  certTypeCode: '',
  fieldId: '',
  ruleName: '',
  validationLogic: '',
  validationValue: '',
  errorMessage: '',
  status: 1
})

// 表单验证规则
const rules = {
  certTypeCode: [{ required: true, message: '请选择证件类型', trigger: 'change' }],
  fieldId: [{ required: true, message: '请输入字段ID', trigger: 'blur' }],
  ruleName: [{ required: true, message: '请输入规则名称', trigger: 'blur' }],
  validationLogic: [{ required: true, message: '请选择校验逻辑', trigger: 'change' }],
  validationValue: [{ required: true, message: '请输入校验值', trigger: 'blur' }],
  errorMessage: [{ required: true, message: '请输入错误信息', trigger: 'blur' }]
}

// 证件类型列表
const certificateTypes = ref([])

// 获取证件类型名称
const getCertTypeName = (code) => {
  const certType = certificateTypes.value.find(t => t.certTypeCode === code)
  return certType ? certType.certTypeName : code
}

// 获取校验逻辑的标签类型
const getLogicType = (logic) => {
  const typeMap = {
    'equals': '',
    'not_equals': 'warning',
    'greater_than': 'success',
    'less_than': 'danger',
    'contains': 'info',
    'not_contains': 'warning',
    'regex': 'primary',
    'length_equals': '',
    'length_greater': 'success',
    'length_less': 'danger'
  }
  return typeMap[logic] || ''
}

// 加载数据
const loadData = async () => {
  try {
    // 并行加载证件类型列表和预置规则列表
    const [ruleRes, certTypeRes] = await Promise.all([
      getPredefinedRuleList({
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
    tableData.value = ruleRes.records || []
    pagination.total = ruleRes.total || 0

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
    certTypeCode: '',
    ruleName: '',
    status: ''
  })
  handleQuery()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增预置规则'
  form.id = null
  form.certTypeCode = ''
  form.fieldId = ''
  form.ruleName = ''
  form.validationLogic = ''
  form.validationValue = ''
  form.errorMessage = ''
  form.status = 1
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑预置规则'
  form.id = row.id
  form.certTypeCode = row.certTypeCode
  form.fieldId = row.fieldId
  form.ruleName = row.ruleName
  form.validationLogic = row.validationLogic
  form.validationValue = row.validationValue
  form.errorMessage = row.errorMessage
  form.status = row.status
  dialogVisible.value = true
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该预置规则吗？', '提示', {
    type: 'warning',
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  }).then(async () => {
    try {
      await deletePredefinedRule(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 状态变更
const handleStatusChange = async (row) => {
  try {
    await updatePredefinedRule(row.id, {
      ...row,
      status: row.status
    })
    ElMessage.success('状态更新成功')
    loadData()
  } catch (error) {
    ElMessage.error('状态更新失败')
    row.status = row.status === 1 ? 0 : 1
  }
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
        if (form.id) {
          // 更新
          await updatePredefinedRule(form.id, form)
        } else {
          // 创建
          await createPredefinedRule(form)
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
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+SC:wght@400;500;700;900&family=JetBrains+Mono:wght@400;500;600&display=swap');

:root {
  --primary-color: #6366f1;
  --primary-gradient: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
  --success-gradient: linear-gradient(135deg, #10b981 0%, #059669 100%);
  --danger-gradient: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
  --bg-gradient: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  --card-shadow: 0 4px 24px rgba(99, 102, 241, 0.08);
}

.predefined-rule-container {
  max-width: 1920px;
  margin: 0 auto;
  font-family: 'Noto Sans SC', sans-serif;
}

.main-card {
  animation: slideInUp 0.6s cubic-bezier(0.4, 0, 0.2, 1);
  border-radius: 16px;
  overflow: hidden;
  box-shadow: var(--card-shadow);
}

@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
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
  padding: 4px 0;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.header-title {
  font-size: 20px;
  font-weight: 900;
  background: var(--primary-gradient);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  letter-spacing: -0.5px;
}

.add-btn {
  background: var(--primary-gradient);
  border: none;
  padding: 12px 28px;
  font-weight: 700;
  border-radius: 10px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 12px rgba(99, 102, 241, 0.3);
}

.add-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(99, 102, 241, 0.4);
}

.query-form {
  margin-bottom: 24px;
  padding: 24px;
  background: var(--bg-gradient);
  border-radius: 12px;
  border: 1px solid #e2e8f0;
  position: relative;
  overflow: hidden;
}

.query-form::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  background: var(--primary-gradient);
}

.form-actions {
  display: flex;
  gap: 12px;
}

.form-actions .el-form-item__content {
  flex-wrap: nowrap;
}

.query-btn {
  background: var(--primary-gradient);
  border: none;
  padding: 10px 24px;
  font-weight: 600;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.query-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(99, 102, 241, 0.3);
}

.reset-btn {
  border: 2px solid #e2e8f0;
  background: white;
  color: #64748b;
  padding: 10px 24px;
  font-weight: 600;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.reset-btn:hover {
  background: #f1f5f9;
  border-color: #cbd5e1;
}

.rule-table {
  margin-bottom: 24px;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.cert-type-display {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
  color: #334155;
}

.validation-value {
  padding: 4px 8px;
  background: #f1f5f9;
  border-radius: 4px;
  font-family: 'JetBrains Mono', monospace;
  font-size: 12px;
  color: #6366f1;
  font-weight: 500;
}

.status-switch {
  --el-switch-on-color: #10b981;
  --el-switch-off-color: #94a3b8;
}

.edit-btn {
  background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
  border: none;
  border-radius: 6px;
  font-weight: 600;
  transition: all 0.3s ease;
}

.edit-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(99, 102, 241, 0.3);
}

.delete-btn {
  background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
  border: none;
  border-radius: 6px;
  font-weight: 600;
  transition: all 0.3s ease;
}

.delete-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(239, 68, 68, 0.3);
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  padding: 16px 0;
}

.custom-pagination {
  border-radius: 8px;
  overflow: hidden;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.cancel-btn {
  border: 2px solid #e2e8f0;
  background: white;
  color: #64748b;
  padding: 10px 32px;
  font-weight: 600;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.cancel-btn:hover {
  background: #f1f5f9;
  border-color: #cbd5e1;
}

.submit-btn {
  background: var(--primary-gradient);
  border: none;
  padding: 10px 32px;
  font-weight: 700;
  border-radius: 8px;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(99, 102, 241, 0.3);
}

.submit-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 6px 20px rgba(99, 102, 241, 0.4);
}

:deep(.el-form-item__label) {
  font-weight: 600;
  color: #475569;
  font-size: 14px;
}

:deep(.el-input__wrapper) {
  border-radius: 8px;
  transition: all 0.3s ease;
}

:deep(.el-input__wrapper:hover) {
  box-shadow: 0 2px 8px rgba(99, 102, 241, 0.1);
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 2px rgba(99, 102, 241, 0.2);
}

:deep(.el-table) {
  border-radius: 12px;
  overflow: hidden;
}

:deep(.el-table th.el-table__cell) {
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  color: #475569;
  font-weight: 700;
  text-transform: uppercase;
  font-size: 12px;
  letter-spacing: 0.5px;
  padding: 16px 12px;
}

:deep(.el-table td.el-table__cell) {
  padding: 14px 12px;
}

:deep(.el-table tr) {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

:deep(.el-table--enable-row-hover .el-table__body tr:hover > td) {
  background-color: #f8fafc;
}

:deep(.el-radio.is-bordered) {
  border-radius: 8px;
  padding: 12px 20px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 2px solid #e2e8f0;
}

:deep(.el-radio.is-bordered:hover) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(99, 102, 241, 0.2);
  border-color: #6366f1;
}

:deep(.el-radio.is-bordered.is-checked) {
  border-color: #6366f1;
  background: linear-gradient(135deg, rgba(99, 102, 241, 0.05) 0%, rgba(139, 92, 246, 0.05) 100%);
}

:deep(.el-dialog) {
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
}

:deep(.el-dialog__header) {
  padding: 28px 28px 20px;
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  border-bottom: 1px solid #e2e8f0;
}

:deep(.el-dialog__title) {
  font-size: 20px;
  font-weight: 900;
  background: var(--primary-gradient);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

:deep(.el-dialog__body) {
  padding: 28px;
}

:deep(.el-dialog__footer) {
  padding: 20px 28px;
  background: #f8fafc;
  border-top: 1px solid #e2e8f0;
}

:deep(.el-pagination) {
  padding: 8px;
  background: #f8fafc;
  border-radius: 8px;
}

:deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active) {
  background: var(--primary-gradient);
}

:deep(.el-select-dropdown__item.selected) {
  color: #6366f1;
  font-weight: 600;
}

:deep(.el-tag--dark) {
  border: none;
}

:deep(.el-button--primary) {
  background: var(--primary-gradient);
  border: none;
}

:deep(.el-button--danger) {
  background: var(--danger-gradient);
  border: none;
}

:deep(.el-button--success) {
  background: var(--success-gradient);
  border: none;
}
</style>
