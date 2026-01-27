<template>
  <div class="certificate-type-container">
    <el-card>
      <!-- 查询表单 -->
      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="证件类型">
          <el-input v-model="queryForm.certTypeName" placeholder="请输入证件类型" clearable />
        </el-form-item>
        <el-form-item label="证件字段">
          <el-input v-model="queryForm.fieldName" placeholder="请输入证件字段" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable>
            <el-option label="全部" :value="null" />
            <el-option label="启用" :value="1" />
            <el-option label="停用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="创建人">
          <el-input v-model="queryForm.createdBy" placeholder="请输入创建人" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="success" @click="handleAdd">新增</el-button>
        </el-form-item>
      </el-form>

      <!-- 数据表格 -->
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="certTypeName" label="证件类型" width="150" />
        <el-table-column prop="certTypeCode" label="证件编号" width="100" />
        <el-table-column label="必填字段" width="200">
          <template #default="{ row }">
            <el-tag
              v-for="field in getRequiredFields(row)"
              :key="field.id"
              size="small"
              style="margin-right: 5px; margin-bottom: 3px;"
            >
              {{ field.fieldNameCn }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="选填字段" width="200">
          <template #default="{ row }">
            <el-tag
              v-for="field in getOptionalFields(row)"
              :key="field.id"
              type="info"
              size="small"
              style="margin-right: 5px; margin-bottom: 3px;"
            >
              {{ field.fieldNameCn }}
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
        <el-form-item label="证件类型名称" prop="certTypeName">
          <el-input v-model="form.certTypeName" placeholder="请输入证件类型名称" />
        </el-form-item>
        <el-form-item label="证件代码" prop="certTypeCode">
          <el-input v-model="form.certTypeCode" placeholder="请输入证件代码" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 选择公共字段 -->
        <el-form-item label="选择公共字段">
          <div class="public-fields-container">
            <el-tag
              v-for="field in publicFields"
              :key="field.id"
              :type="isFieldSelected(field.id) ? '' : 'info'"
              closable
              @close="removeField(field.id)"
              @click="toggleField(field)"
              style="cursor: pointer; margin-right: 8px; margin-bottom: 8px;"
            >
              {{ field.fieldNameCn }}
            </el-tag>
          </div>
        </el-form-item>

        <!-- 添加自定义字段 -->
        <el-form-item>
          <el-button type="primary" size="small" @click="handleAddCustomField">添加自定义字段</el-button>
        </el-form-item>

        <!-- 字段配置列表 -->
        <el-form-item label="字段配置" prop="fields">
          <el-table :data="form.fields" border style="width: 100%">
            <el-table-column prop="fieldNameCn" label="中文名称" width="150" />
            <el-table-column prop="fieldNameEn" label="英文名称" width="150" />
            <el-table-column label="是否必填" width="100">
              <template #default="{ row }">
                <el-checkbox v-model="row.isRequired" />
              </template>
            </el-table-column>
            <el-table-column label="数据类型" width="120">
              <template #default="{ row }">
                <el-select v-model="row.fieldDataType" placeholder="请选择">
                  <el-option label="文本" value="text" />
                  <el-option label="数字" value="number" />
                  <el-option label="日期" value="date" />
                  <el-option label="日期范围" value="date_range" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column prop="fieldLength" label="字段长度" width="100" />
            <el-table-column label="操作" width="80">
              <template #default="{ $index }">
                <el-button
                  type="danger"
                  :icon="Delete"
                  circle
                  size="small"
                  @click="removeFieldByIndex($index)"
                />
              </template>
            </el-table-column>
          </el-table>
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
import { ElMessage, ElMessageBox } from 'element-plus'
import { Edit, Delete } from '@element-plus/icons-vue'
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
    tableData.value = res.data || []
    pagination.total = res.total || 0
  } catch (error) {
    ElMessage.error('加载数据失败')
  }
}

// 加载公共字段
const loadPublicFields = async () => {
  try {
    const res = await getPublicFields()
    publicFields.value = res.data || []
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
    type: 'warning'
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
  padding: 20px;
}

.query-form {
  margin-bottom: 20px;
}

.public-fields-container {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}
</style>
