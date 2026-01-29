<template>
  <div class="validation-rule-container">
    <el-card class="main-card">
      <!-- 页面标题 -->
      <template #header>
        <div class="card-header">
          <div class="header-left">
            <el-icon :size="20" color="#3b82f6"><Checked /></el-icon>
            <span class="header-title">校验规则配置</span>
          </div>
          <el-button type="primary" :icon="Plus" @click="handleAdd">新增规则</el-button>
        </div>
      </template>

      <!-- 查询表单 -->
      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="业务类型">
          <el-input v-model="queryForm.businessType" placeholder="请输入业务类型" clearable prefix-icon="Search" />
        </el-form-item>
        <el-form-item label="业务编号">
          <el-input v-model="queryForm.businessCode" placeholder="请输入业务编号" clearable prefix-icon="PriceTag" />
        </el-form-item>
        <el-form-item label="渠道小类">
          <el-input v-model="queryForm.channelSubType" placeholder="请输入渠道小类" clearable prefix-icon="Connection" />
        </el-form-item>
        <el-form-item class="form-actions">
          <el-button type="primary" :icon="Search" @click="handleQuery">查询</el-button>
          <el-button :icon="RefreshLeft" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 字段规则配置区域 -->
      <div class="field-rules-section">
        <div class="section-header" @click="toggleFieldRules">
          <div class="header-left">
            <el-icon :size="18" color="#3b82f6"><Document /></el-icon>
            <span class="section-title">字段规则配置</span>
            <el-tag v-if="fieldRules.length > 0" type="primary" size="small" effect="dark">
              {{ fieldRules.length }} 个字段
            </el-tag>
          </div>
          <el-icon :size="16" :class="{ 'rotate-180': showFieldRules }">
            <ArrowDown v-if="!showFieldRules" />
            <ArrowUp v-else />
          </el-icon>
        </div>
        <el-collapse-transition>
          <div v-show="showFieldRules" class="section-content">
            <div class="section-actions">
              <el-button type="primary" :icon="Plus" @click.stop="handleAddFieldRule">
                新增字段规则
              </el-button>
              <el-button :icon="RefreshLeft" @click.stop="handleRefreshFieldRules">
                刷新
              </el-button>
            </div>
            <FieldRuleTable
              v-if="fieldRules.length > 0"
              :data="fieldRules"
              :certificate-types="certificateTypes"
              @edit-field="handleEditFieldRule"
              @delete-field="handleDeleteFieldRule"
              @update-rules="handleUpdateFieldRules"
            />
            <el-empty v-else description="暂无字段规则，点击上方按钮添加" />
          </div>
        </el-collapse-transition>
      </div>

      <!-- 数据表格 -->
      <el-table :data="tableData" border style="width: 100%" class="rule-table">
        <el-table-column prop="businessType" label="业务类型" width="150" />
        <el-table-column prop="businessCode" label="业务编号" width="180">
          <template #default="{ row }">
            <el-tag type="info" size="small">{{ row.businessCode }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="channelSubType" label="渠道小类" width="120" />
        <el-table-column prop="allowedCertTypes" label="允许的证件类型" min-width="220">
          <template #default="{ row }">
            <div class="cert-tags">
              <el-tag
                v-for="certType in parseAllowedCertTypes(row.allowedCertTypes)"
                :key="certType"
                type="primary"
                size="small"
                effect="plain"
              >
                <el-icon style="margin-right: 4px;"><Document /></el-icon>
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
      width="1000px"
      @close="handleDialogClose"
      class="form-dialog"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="140px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="业务类型" prop="businessType">
              <el-input v-model="form.businessType" placeholder="请输入业务类型" size="large" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="业务编号" prop="businessCode">
              <el-input v-model="form.businessCode" placeholder="请输入业务编号" size="large" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="渠道小类">
          <el-input v-model="form.channelSubType" placeholder="请输入渠道小类" size="large" />
        </el-form-item>
        <el-form-item label="证件类型">
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

    <!-- 字段规则对话框 -->
    <el-dialog
      v-model="showFieldRulesDialog"
      :title="currentEditingRule ? '编辑字段规则' : '新增字段规则'"
      width="600px"
      @close="handleFieldRuleDialogClose"
      class="form-dialog"
    >
      <el-form :model="fieldRuleForm" :rules="fieldRuleFormRules" ref="fieldRuleFormRef" label-width="120px">
        <el-form-item label="字段名称" prop="fieldNameCn">
          <el-input v-model="fieldRuleForm.fieldNameCn" placeholder="请输入字段名称" size="large" />
        </el-form-item>
        <el-form-item label="字段标识" prop="fieldNameEn">
          <el-input v-model="fieldRuleForm.fieldNameEn" placeholder="请输入字段标识（英文）" size="large" />
        </el-form-item>
        <el-form-item label="字段范围" prop="scope">
          <el-radio-group v-model="fieldRuleForm.scope" size="large">
            <el-radio label="共有" border>
              <el-icon color="#10b981"><CircleCheckFilled /></el-icon>
              <span style="margin-left: 8px;">共有</span>
            </el-radio>
            <el-radio label="私有" border>
              <el-icon color="#94a3b8"><CircleCloseFilled /></el-icon>
              <span style="margin-left: 8px;">私有</span>
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="fieldRuleForm.status" size="large">
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
          <el-button @click="showFieldRulesDialog = false" size="large">取消</el-button>
          <el-button type="primary" @click="handleSubmitFieldRule" size="large">
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
  CircleCheckFilled, CircleCloseFilled, Document, Connection, PriceTag, Check, Checked, ArrowDown, ArrowUp
} from '@element-plus/icons-vue'
import {
  getValidationRuleList,
  createValidationRule,
  updateValidationRule,
  deleteValidationRule
} from '@/api/validation-rule'
import { getCertificateTypeList } from '@/api/certificate-type'
import { getFieldRules, createFieldRule, updateFieldRule, deleteFieldRule } from '@/api/field-rule'
import FieldRuleTable from '@/components/FieldRuleTable.vue'

// 查询表单
const queryForm = reactive({
  businessType: '',
  businessCode: '',
  channelSubType: ''
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
const dialogTitle = ref('新增校验规则')
const formRef = ref(null)

// 表单数据
const form = reactive({
  id: null,
  businessType: '',
  businessCode: '',
  channelSubType: '',
  allowedCertTypes: [],
  status: 1
})

// 表单验证规则
const rules = {
  businessType: [{ required: true, message: '请输入业务类型', trigger: 'blur' }],
  businessCode: [{ required: true, message: '请输入业务编号', trigger: 'blur' }],
  allowedCertTypes: [{ required: true, message: '请至少选择一个证件类型', trigger: 'change' }]
}

// 证件类型列表
const certificateTypes = ref([])

// 字段规则
const fieldRules = ref([])
const showFieldRules = ref(true)
const showFieldRulesDialog = ref(false)
const currentEditingRule = ref(null)
const fieldRuleFormRef = ref(null)

// 字段规则表单
const fieldRuleForm = reactive({
  id: null,
  fieldNameEn: '',
  fieldNameCn: '',
  scope: '共有',
  status: 1,
  validationRules: []
})

// 字段规则表单验证规则
const fieldRuleFormRules = {
  fieldNameEn: [{ required: true, message: '请输入字段标识', trigger: 'blur' }],
  fieldNameCn: [{ required: true, message: '请输入字段名称', trigger: 'blur' }],
  scope: [{ required: true, message: '请选择字段范围', trigger: 'change' }]
}

// 应用系统名称映射
const businessChannelMap = {
  'mobile-cloud': '移动云厅',
  'grid': '网格通',
  'convenience-store': '便利店'
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
    const [ruleRes, certTypeRes] = await Promise.all([
      getValidationRuleList({
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
    businessType: '',
    businessCode: '',
    channelSubType: ''
  })
  handleQuery()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增校验规则'
  form.id = null
  form.businessType = ''
  form.businessCode = ''
  form.channelSubType = ''
  form.allowedCertTypes = []
  form.status = 1
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑校验规则'
  form.id = row.id
  form.businessType = row.businessType
  form.businessCode = row.businessCode
  form.channelSubType = row.channelSubType
  form.allowedCertTypes = parseAllowedCertTypes(row.allowedCertTypes)
  form.status = row.status
  dialogVisible.value = true
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该校验规则吗？', '提示', {
    type: 'warning',
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  }).then(async () => {
    try {
      await deleteValidationRule(row.id)
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

// 字段规则对话框关闭
const handleFieldRuleDialogClose = () => {
  fieldRuleFormRef.value?.resetFields()
}

// 提交
const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 将数组转换为竖线分隔的字符串
        const submitData = {
          ...form,
          allowedCertTypes: form.allowedCertTypes.join('|')
        }

        if (form.id) {
          // 更新
          await updateValidationRule(form.id, submitData)
        } else {
          // 创建
          await createValidationRule(submitData)
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

// 加载字段规则
const loadFieldRules = async () => {
  try {
    const res = await getFieldRules({ page: 1, pageSize: 1000 })
    fieldRules.value = Array.isArray(res.records) ? res.records : []
  } catch (error) {
    ElMessage.error('加载字段规则失败')
  }
}

// 展开/收起字段规则
const toggleFieldRules = () => {
  showFieldRules.value = !showFieldRules.value
  if (showFieldRules.value && fieldRules.value.length === 0) {
    loadFieldRules()
  }
}

// 刷新字段规则
const handleRefreshFieldRules = () => {
  loadFieldRules()
}

// 新增字段规则
const handleAddFieldRule = () => {
  showFieldRulesDialog.value = true
  currentEditingRule.value = null
  Object.assign(fieldRuleForm, {
    id: null,
    fieldNameEn: '',
    fieldNameCn: '',
    scope: '共有',
    status: 1,
    validationRules: []
  })
}

// 编辑字段规则
const handleEditFieldRule = (row) => {
  showFieldRulesDialog.value = true
  currentEditingRule.value = row
  Object.assign(fieldRuleForm, {
    id: row.id,
    fieldNameEn: row.fieldNameEn,
    fieldNameCn: row.fieldNameCn,
    scope: row.scope,
    status: row.status,
    validationRules: [...row.validationRules]
  })
}

// 删除字段规则
const handleDeleteFieldRule = (row) => {
  ElMessageBox.confirm('确定要删除该字段规则吗？', '提示', {
    type: 'warning',
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  }).then(async () => {
    try {
      await deleteFieldRule(row.id)
      ElMessage.success('删除成功')
      loadFieldRules()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 更新字段规则
const handleUpdateFieldRules = (fieldId, updatedRules) => {
  const rule = fieldRules.value.find(r => r.fieldId === fieldId)
  if (rule) {
    rule.validationRules = updatedRules
  }
}

// 提交字段规则
const handleSubmitFieldRule = async () => {
  if (!fieldRuleFormRef.value) return

  await fieldRuleFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (fieldRuleForm.id) {
          await updateFieldRule(fieldRuleForm.id, fieldRuleForm)
        } else {
          await createFieldRule(fieldRuleForm)
        }
        ElMessage.success('保存成功')
        showFieldRulesDialog.value = false
        loadFieldRules()
      } catch (error) {
        ElMessage.error('操作失败')
      }
    }
  })
}

// 页面加载时初始化
onMounted(() => {
  loadData()
  loadFieldRules()
})
</script>

<style scoped>
.validation-rule-container {
  max-width: 1920px;
  margin: 0 auto;
}

.field-rules-section {
  margin: 24px 0;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  overflow: hidden;
  background: #ffffff;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  cursor: pointer;
  user-select: none;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.section-header:hover {
  background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #1e293b;
}

.rotate-180 {
  transform: rotate(180deg);
}

.section-content {
  padding: 20px;
  background: #ffffff;
}

.section-actions {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.validation-rule-container {
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

.rule-table {
  margin-bottom: 20px;
  border-radius: 8px;
  overflow: hidden;
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
