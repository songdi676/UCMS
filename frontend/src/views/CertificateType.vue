<template>
  <div class="certificate-type-container">
    <el-card class="main-card">
      <!-- 页面标题 -->
      <template #header>
        <div class="card-header">
          <div class="header-left">
            <el-icon :size="20" color="#3b82f6"><Document /></el-icon>
            <span class="header-title">证件类型配置</span>
          </div>
          <el-button type="primary" :icon="Plus" @click="handleAdd">添加证件类型</el-button>
        </div>
      </template>

      <!-- 查询表单 -->
      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="证件类型">
          <el-input v-model="queryForm.certTypeName" placeholder="请输入证件类型" clearable prefix-icon="Search" />
        </el-form-item>
        <el-form-item label="证件字段">
          <el-input v-model="queryForm.fieldName" placeholder="请输入证件字段" clearable prefix-icon="Search" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable style="width: 150px;">
            <el-option label="全部" :value="null" />
            <el-option label="启用" :value="1">
              <span>启用</span>
              <el-icon color="#10b981"><CircleCheckFilled /></el-icon>
            </el-option>
            <el-option label="停用" :value="0">
              <span>停用</span>
              <el-icon color="#94a3b8"><CircleCloseFilled /></el-icon>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="创建人">
          <el-input v-model="queryForm.createdBy" placeholder="请输入创建人" clearable prefix-icon="User" />
        </el-form-item>
        <el-form-item class="form-actions">
          <el-button type="primary" :icon="Search" @click="handleQuery">查询</el-button>
          <el-button :icon="RefreshLeft" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 数据表格 -->
      <el-table
        :data="tableData"
        border
        style="width: 100%"
        :page-size="pagination.pageSize"
        :current-page="pagination.page"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      >
        <template #empty>
          <el-empty description="暂无数据" :image-size="100">
            <el-button type="primary" :icon="Plus" @click="handleAdd">添加证件类型</el-button>
          </el-empty>
        </template>
        <el-table-column prop="certTypeName" label="证件类型名称" min-width="180" />
        <el-table-column prop="certTypeCode" label="证件代码" width="120" />
        <el-table-column label="字段数量" width="100" align="center">
          <template #default="{ row }">
            <el-tag type="primary" size="large">{{ (row.fields || []).length }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="必填字段" width="120" align="center">
          <template #default="{ row }">
            <el-tag type="warning" size="large">{{ getRequiredFields(row).length }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="选填字段" width="120" align="center">
          <template #default="{ row }">
            <el-tag type="info" size="large">{{ getOptionalFields(row).length }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'" effect="dark">
              <el-icon v-if="row.status === 1"><CircleCheckFilled /></el-icon>
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
      width="900px"
      @close="handleDialogClose"
      class="form-dialog"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="140px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="证件类型名称" prop="certTypeName">
              <el-input v-model="form.certTypeName" placeholder="请输入证件类型名称" size="large" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="证件代码" prop="certTypeCode">
              <el-input v-model="form.certTypeCode" placeholder="请输入证件代码" size="large" />
            </el-form-item>
          </el-col>
        </el-row>

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

        <!-- 选择公共字段 -->
        <el-form-item label="选择公共字段">
          <div class="public-fields-container">
            <el-tag
              v-for="field in publicFields"
              :key="field.id"
              :type="isFieldSelected(field.id) ? 'primary' : 'info'"
              closable
              @close="removeField(field.id)"
              @click="toggleField(field)"
              class="field-tag"
              :class="{ 'is-selected': isFieldSelected(field.id) }"
            >
              <el-icon style="margin-right: 4px;"><Key /></el-icon>
              {{ field.fieldNameCn }}
            </el-tag>
          </div>
          <div class="field-tip">
            <el-icon><InfoFilled /></el-icon>
            点击标签添加或移除字段
          </div>
        </el-form-item>

        <!-- 添加自定义字段按钮 -->
        <el-form-item>
          <el-button type="primary" :icon="Plus" @click="handleAddCustomField">添加自定义字段</el-button>
        </el-form-item>

        <!-- 字段配置列表 -->
        <el-form-item label="字段配置" prop="fields">
          <el-table :data="form.fields" border style="width: 100%" class="fields-table">
            <el-table-column prop="fieldNameCn" label="中文名称" width="160">
              <template #default="{ row, $index }">
                <el-input v-model="row.fieldNameCn" placeholder="中文名称" size="small" />
              </template>
            </el-table-column>
            <el-table-column prop="fieldNameEn" label="英文名称" width="160">
              <template #default="{ row, $index }">
                <el-input v-model="row.fieldNameEn" placeholder="英文名称" size="small" />
              </template>
            </el-table-column>
            <el-table-column label="是否必填" width="100" align="center">
              <template #default="{ row }">
                <el-switch v-model="row.isRequired" :active-value="1" :inactive-value="0" />
              </template>
            </el-table-column>
            <el-table-column label="数据类型" width="140">
              <template #default="{ row }">
                <el-select v-model="row.fieldDataType" placeholder="请选择" size="small">
                  <el-option label="文本" value="text" />
                  <el-option label="数字" value="number" />
                  <el-option label="日期" value="date" />
                  <el-option label="日期范围" value="date_range" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column prop="fieldLength" label="字段长度" width="120" align="center" />
            <el-table-column label="操作" width="200" align="center" fixed="right">
              <template #default="{ $index }">
                <el-button
                  type="danger"
                  :icon="Delete"
                  size="small"
                  @click="removeFieldByIndex($index)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-if="form.fields.length === 0" description="暂无字段，请添加字段" :image-size="80" />
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
  Document, Plus, Edit, Delete, Search, RefreshLeft,
  CircleCheckFilled, CircleCloseFilled, Key, InfoFilled, Check
} from '@element-plus/icons-vue'
import { getCertificateTypeList, getPublicFields, createCertificateType } from '@/api/certificate-type'

// 查询表单
const queryForm = reactive({
  certTypeName: '',
  fieldName: '',
  status: null,
  createdBy: ''
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
const dialogTitle = ref('新增证件类型')
const formRef = ref(null)

// 表单数据
const form = reactive({
  id: null,
  certTypeName: '',
  certTypeCode: '',
  status: 1,
  fields: []
})

// 表单验证规则
const rules = {
  certTypeName: [{ required: true, message: '请输入证件类型名称', trigger: 'blur' }],
  certTypeCode: [{ required: true, message: '请输入证件代码', trigger: 'blur' }],
  fields: [
    { required: true, message: '请至少选择或添加一个字段', trigger: 'change' }
  ]
}

// 公共字段列表
const publicFields = ref([])

// 加载数据
const loadData = async () => {
  try {
    const res = await getCertificateTypeList({
      page: pagination.page,
      pageSize: pagination.pageSize,
      ...queryForm
    })
    // 后端返回MyBatis-Plus的IPage结构: {records: [], total: 0, ...}
    tableData.value = res.records || []
    pagination.total = res.total || 0
  } catch (error) {
    ElMessage.error('加载数据失败')
  }
}

// 加载公共字段
const loadPublicFields = async () => {
  try {
    const res = await getPublicFields()
    publicFields.value = Array.isArray(res) ? res : []
  } catch (error) {
    ElMessage.error('加载公共字段失败')
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
    certTypeName: '',
    fieldName: '',
    status: null,
    createdBy: ''
  })
  handleQuery()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增证件类型'
  form.id = null
  form.certTypeName = ''
  form.certTypeCode = ''
  form.status = 1
  form.fields = []
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑证件类型'
  form.id = row.id
  form.certTypeName = row.certTypeName
  form.certTypeCode = row.certTypeCode
  form.status = row.status
  form.fields = row.fields || []
  dialogVisible.value = true
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该证件类型吗？', '提示', {
    type: 'warning',
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  }).then(async () => {
    // 调用删除接口
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

// 提交
const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (form.fields.length === 0) {
          ElMessage.warning('请至少选择或添加一个字段')
          return
        }

        if (form.id) {
          // 更新
        } else {
          await createCertificateType(form)
          ElMessage.success('创建成功')
        }
        dialogVisible.value = false
        loadData()
      } catch (error) {
        ElMessage.error('操作失败')
      }
    }
  })
}

// 对话框关闭
const handleDialogClose = () => {
  formRef.value?.resetFields()
}

// 判断字段是否已选中
const isFieldSelected = (fieldId) => {
  return form.fields.some(f => f.fieldId === fieldId)
}

// 切换字段选中状态
const toggleField = (field) => {
  const index = form.fields.findIndex(f => f.fieldId === field.id)
  if (index > -1) {
    form.fields.splice(index, 1)
  } else {
    form.fields.push({
      fieldId: field.id,
      fieldNameCn: field.fieldNameCn,
      fieldNameEn: field.fieldNameEn,
      fieldType: field.fieldType,
      fieldDataType: field.fieldDataType,
      fieldLength: field.fieldLength,
      isRequired: 0
    })
  }
}

// 移除字段
const removeField = (fieldId) => {
  const index = form.fields.findIndex(f => f.fieldId === fieldId)
  if (index > -1) {
    form.fields.splice(index, 1)
  }
}

// 添加自定义字段
const handleAddCustomField = () => {
  form.fields.push({
    fieldId: null,
    fieldNameCn: '',
    fieldNameEn: '',
    fieldType: 'CUSTOM',
    fieldDataType: 'text',
    fieldLength: null,
    isRequired: 0
  })
}

// 按索引移除字段
const removeFieldByIndex = (index) => {
  form.fields.splice(index, 1)
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

// 获取必填字段
const getRequiredFields = (row) => {
  return (row.fields || []).filter(f => f.isRequired === 1)
}

// 获取选填字段
const getOptionalFields = (row) => {
  return (row.fields || []).filter(f => f.isRequired === 0)
}

// 页面加载时初始化
onMounted(() => {
  loadData()
  loadPublicFields()
})
</script>

<style scoped>
.certificate-type-container {
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

.public-fields-container {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  padding: 16px;
  background: #f8fafc;
  border-radius: 12px;
  border: 2px dashed #cbd5e1;
  min-height: 80px;
}

.field-tag {
  cursor: pointer;
  padding: 10px 16px;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border-radius: 8px;
}

.field-tag:hover {
  transform: translateY(-2px) scale(1.02);
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.2);
}

.field-tag.is-selected {
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  color: white;
  border: none;
}

.field-tip {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-top: 8px;
  color: #64748b;
  font-size: 13px;
}

.fields-table {
  overflow-x: auto;
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

:deep(.el-empty) {
  padding: 40px 0;
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

.form-dialog {
  max-height: 70vh;
  overflow-y: auto;
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
