<template>
  <div class="validation-rule-container">
    <el-card>
      <!-- 查询表单 -->
      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="业务类型">
          <el-input v-model="queryForm.businessType" placeholder="请输入业务类型" clearable />
        </el-form-item>
        <el-form-item label="业务编号">
          <el-input v-model="queryForm.businessCode" placeholder="请输入业务编号" clearable />
        </el-form-item>
        <el-form-item label="渠道小类">
          <el-input v-model="queryForm.channelSubType" placeholder="请输入渠道小类" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="success" @click="handleAdd">新增规则</el-button>
        </el-form-item>
      </el-form>

      <!-- 数据表格 -->
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="businessType" label="业务类型" width="120" />
        <el-table-column prop="businessCode" label="业务编号" width="120" />
        <el-table-column prop="channelSubType" label="渠道小类" width="120" />
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
      width="900px"
      @close="handleDialogClose"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="140px">
        <el-form-item label="业务类型" prop="businessType">
          <el-input v-model="form.businessType" placeholder="请输入业务类型" />
        </el-form-item>
        <el-form-item label="业务编号" prop="businessCode">
          <el-input v-model="form.businessCode" placeholder="请输入业务编号" />
        </el-form-item>
        <el-form-item label="渠道小类" prop="channelSubType">
          <el-input v-model="form.channelSubType" placeholder="请输入渠道小类" />
        </el-form-item>
        <el-form-item label="允许的证件类型" prop="allowedCertTypes">
          <el-select v-model="form.allowedCertTypes" placeholder="请选择证件类型" multiple>
            <el-option
              v-for="certType in certificateTypes"
              :key="certType.code"
              :label="certType.name"
              :value="certType.code"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="规则明细" prop="ruleItems">
          <div style="max-height: 300px; overflow-y: auto;">
            <div v-for="(item, index) in form.ruleItems" :key="index" style="display: flex; align-items: center; gap: 10px; margin-bottom: 10px;">
              <el-input
                v-model="item.fieldName"
                placeholder="字段名称"
                style="width: 200px;"
              />
              <el-select
                v-model="item.logic"
                placeholder="逻辑"
                style="width: 120px;"
              >
                <el-option label="大于" value=">" />
                <el-option label="小于" value="<" />
                <el-option label="大于等于" value=">=" />
                <el-option label="小于等于" value="<=" />
                <el-option label="等于" value="==" />
                <el-option label="不等于" value="!=" />
                <el-option label="包含" value="contains" />
                <el-option label="正则" value="regex" />
              </el-select>
              <el-input
                v-model="item.value"
                placeholder="比较值"
                style="width: 150px;"
              />
              <el-button
                type="danger"
                :icon="Delete"
                circle
                size="small"
                @click="removeRuleItem(index)"
              />
            </div>
          </div>
          <el-button type="primary" size="small" @click="addRuleItem" style="margin-top: 10px;">添加规则项</el-button>
        </el-form-item>
        <el-form-item label="状态" prop="status">
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
import { ElMessage, ElMessageBox } from 'element-plus'
import { Edit, Delete } from '@element-plus/icons-vue'

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
  ruleItems: [],
  status: 1
})

// 表单验证规则
const rules = {
  businessType: [{ required: true, message: '请输入业务类型', trigger: 'blur' }],
  businessCode: [{ required: true, message: '请输入业务编号', trigger: 'blur' }],
  allowedCertTypes: [{ required: true, message: '请选择至少一个证件类型', trigger: 'change' }]
}

// 证件类型列表（暂时静态数据）
const certificateTypes = ref([
  { code: '1', name: '身份证' },
  { code: '2', name: '护照' },
  { code: '3', name: '军官证' },
  { code: '11', name: '台湾居民来往大陆通行证' },
  { code: '14', name: '武装警察身份证' },
  { code: '15', name: '外国人永久居留证' }
])

// 加载数据
const loadData = async () => {
  try {
    // 暂时使用静态数据
    const mockData = [
      {
        id: 1,
        businessType: '档案补正',
        businessCode: 'ARCHIVE_CORRECTION',
        channelSubType: '云厅',
        allowedCertTypes: '1,2,3',
        status: 1,
        createdBy: 'admin',
        updatedBy: 'admin',
        createdAt: '2026-01-27 10:00:00',
        updatedAt: '2026-01-27 10:00:00'
      },
      {
        id: 2,
        businessType: '新开卡',
        businessCode: 'NEW_CARD_OPENING',
        channelSubType: '便利店',
        allowedCertTypes: '1,3,11',
        status: 1,
        createdBy: 'admin',
        updatedBy: 'admin',
        createdAt: '2026-01-27 10:05:00',
        updatedAt: '2026-01-27 10:05:00'
      }
    ]
    tableData.value = mockData
    pagination.total = mockData.length
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
  pagination.page = 1
  loadData()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增校验规则'
  Object.assign(form, {
    id: null,
    businessType: '',
    businessCode: '',
    channelSubType: '',
    allowedCertTypes: [],
    ruleItems: [
      {
        fieldName: '',
        logic: '>',
        value: '18'
      }
    ],
    status: 1
  })
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑校验规则'
  Object.assign(form, {
    id: row.id,
    businessType: row.businessType,
    businessCode: row.businessCode,
    channelSubType: row.channelSubType,
    allowedCertTypes: parseAllowedCertTypes(row.allowedCertTypes),
    ruleItems: row.ruleItems || [
      {
        fieldName: '',
        logic: '>',
        value: '18'
      }
    ],
    status: row.status
  })
  dialogVisible.value = true
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该校验规则吗？', '提示', {
    type: 'warning'
  }).then(() => {
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

// 添加规则项
const addRuleItem = () => {
  form.ruleItems.push({
    fieldName: '',
    logic: '>',
    value: ''
  })
}

// 移除规则项
const removeRuleItem = (index) => {
  form.ruleItems.splice(index, 1)
}

// 解析允许的证件类型
const parseAllowedCertTypes = (typesStr) => {
  if (!typesStr) return []
  return typesStr.split(',').map(t => t.trim())
}

// 获取证件类型名称
const getCertTypeName = (code) => {
  const certType = certificateTypes.value.find(t => t.code === code)
  return certType ? certType.name : code
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
.validation-rule-container {
  padding: 20px;
}

.query-form {
  margin-bottom: 20px;
}
</style>
