<template>
  <div class="field-rule-table">
    <el-table
      :data="displayData"
      border
      stripe
      style="width: 100%"
      class="rule-table"
      :row-key="getRowKey"
      @expand-change="handleExpandChange"
    >
      <el-table-column type="expand">
        <template #default="{ row }">
          <div class="expand-content">
            <div v-for="(rule, index) in row.validationRules" :key="index" class="rule-item">
              <div class="rule-header">
                <el-tag :type="getRuleTypeColor(rule.ruleType)" size="small">
                  {{ getRuleTypeName(rule.ruleType) }}
                </el-tag>
                <span class="rule-logic">{{ rule.validationLogic }}</span>
                <span class="rule-value">{{ rule.validationValue }}</span>
              </div>
              <div class="rule-actions">
                <el-button
                  type="primary"
                  :icon="Edit"
                  size="small"
                  link
                  @click="handleEditRule(row, rule, index)"
                >
                  编辑规则
                </el-button>
                <el-button
                  type="danger"
                  :icon="Delete"
                  size="small"
                  link
                  @click="handleDeleteRule(row, index)"
                >
                  删除规则
                </el-button>
              </div>
            </div>
            <div class="add-rule-btn">
              <el-button
                type="primary"
                :icon="Plus"
                size="small"
                @click="handleAddRule(row)"
              >
                添加规则
              </el-button>
            </div>
          </div>
        </template>
      </el-table-column>

      <el-table-column prop="fieldNameCn" label="字段名称" width="150">
        <template #default="{ row }">
          <div class="field-name">
            <el-icon :size="16" color="#3b82f6"><Document /></el-icon>
            <span>{{ row.fieldNameCn }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column prop="fieldNameEn" label="字段标识" width="180">
        <template #default="{ row }">
          <el-tag type="info" size="small" effect="plain">{{ row.fieldNameEn }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="scope" label="字段范围" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="row.scope === '共有' ? 'success' : 'warning'" effect="dark" size="large">
            <el-icon v-if="row.scope === '共有'"><CircleCheckFilled /></el-icon>
            <el-icon v-else><Lock /></el-icon>
            <span style="margin-left: 4px;">{{ row.scope }}</span>
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="ruleCount" label="规则数量" width="100" align="center">
        <template #default="{ row }">
          <el-badge :value="row.validationRules.length" :max="99" class="badge">
            <el-icon :size="20" color="#64748b"><List /></el-icon>
          </el-badge>
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

      <el-table-column prop="certTypeNames" label="适用证件类型" min-width="200">
        <template #default="{ row }">
          <div class="cert-tags">
            <el-tag
              v-for="certType in row.certTypes"
              :key="certType.certTypeCode"
              type="primary"
              size="small"
              effect="plain"
            >
              <el-icon style="margin-right: 4px;"><Document /></el-icon>
              {{ certType.certTypeName }}
            </el-tag>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button
            type="primary"
            :icon="Edit"
            size="small"
            @click="handleEditField(row)"
          >
            编辑字段
          </el-button>
          <el-button
            type="danger"
            :icon="Delete"
            size="small"
            @click="handleDeleteField(row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 规则编辑对话框 -->
    <el-dialog
      v-model="ruleDialogVisible"
      :title="ruleDialogTitle"
      width="600px"
      @close="handleRuleDialogClose"
      class="rule-dialog"
    >
      <el-form :model="ruleForm" :rules="ruleFormRules" ref="ruleFormRef" label-width="120px">
        <el-form-item label="规则类型" prop="ruleType">
          <el-radio-group v-model="ruleForm.ruleType" @change="handleRuleTypeChange">
            <el-radio label="predefined" border>
              <el-icon><Collection /></el-icon>
              <span style="margin-left: 8px;">预置规则</span>
            </el-radio>
            <el-radio label="custom" border>
              <el-icon><EditPen /></el-icon>
              <span style="margin-left: 8px;">自定义规则</span>
            </el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item
          v-if="ruleForm.ruleType === 'predefined'"
          label="选择预置规则"
          prop="predefinedRuleId"
        >
          <el-select
            v-model="ruleForm.predefinedRuleId"
            placeholder="请选择预置规则"
            size="large"
            style="width: 100%"
            filterable
            @change="handlePredefinedRuleChange"
          >
            <el-option
              v-for="predefinedRule in predefinedRules"
              :key="predefinedRule.ruleId"
              :label="predefinedRule.ruleName"
              :value="predefinedRule.ruleId"
            >
              <span>{{ predefinedRule.ruleName }}</span>
              <el-icon style="margin-left: 8px;" color="#3b82f6"><Collection /></el-icon>
            </el-option>
          </el-select>
        </el-form-item>

        <template v-if="ruleForm.ruleType === 'custom'">
          <el-form-item label="校验逻辑" prop="validationLogic">
            <el-input
              v-model="ruleForm.validationLogic"
              placeholder="例如: equals, regex, min, max, required等"
              size="large"
            />
          </el-form-item>
          <el-form-item label="校验值" prop="validationValue">
            <el-input
              v-model="ruleForm.validationValue"
              placeholder="例如: ^\d{18}$, 18, true等"
              size="large"
            />
          </el-form-item>
          <el-form-item label="错误提示" prop="errorMessage">
            <el-input
              v-model="ruleForm.errorMessage"
              placeholder="校验失败时的提示信息"
              size="large"
            />
          </el-form-item>
        </template>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="ruleDialogVisible = false" size="large">取消</el-button>
          <el-button type="primary" @click="handleSaveRule" size="large">
            <el-icon><Check /></el-icon>
            <span style="margin-left: 4px;">保存</span>
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Edit, Delete, Plus, Document, List, CircleCheckFilled, CircleCloseFilled,
  Lock, Collection, EditPen, Check
} from '@element-plus/icons-vue'
import { getPredefinedRuleList } from '@/api/predefined-rule'

// Props
const props = defineProps({
  data: {
    type: Array,
    default: () => []
  },
  certificateTypes: {
    type: Array,
    default: () => []
  }
})

// Emits
const emit = defineEmits(['edit-field', 'delete-field', 'update-rules'])

// 预置规则列表
const predefinedRules = ref([])

// 规则对话框
const ruleDialogVisible = ref(false)
const ruleDialogTitle = ref('编辑规则')
const ruleFormRef = ref(null)
const currentField = ref(null)
const currentRuleIndex = ref(-1)

// 规则表单
const ruleForm = reactive({
  ruleType: 'predefined',
  predefinedRuleId: null,
  validationLogic: '',
  validationValue: '',
  errorMessage: ''
})

// 规则表单验证规则
const ruleFormRules = {
  ruleType: [{ required: true, message: '请选择规则类型', trigger: 'change' }],
  predefinedRuleId: [
    {
      validator: (rule, value, callback) => {
        if (ruleForm.ruleType === 'predefined' && !value) {
          callback(new Error('请选择预置规则'))
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ],
  validationLogic: [
    {
      validator: (rule, value, callback) => {
        if (ruleForm.ruleType === 'custom' && !value) {
          callback(new Error('请输入校验逻辑'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  validationValue: [
    {
      validator: (rule, value, callback) => {
        if (ruleForm.ruleType === 'custom' && !value) {
          callback(new Error('请输入校验值'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 处理显示数据(按scope去重)
const displayData = computed(() => {
  const result = []
  const sharedFieldMap = new Map()

  props.data.forEach(item => {
    if (item.scope === '共有') {
      // 共有字段只显示一次
      const key = item.fieldNameEn
      if (!sharedFieldMap.has(key)) {
        sharedFieldMap.set(key, { ...item, certTypes: [] })
      }
      // 合并证件类型
      if (item.certTypeCode) {
        const certType = props.certificateTypes.find(ct => ct.certTypeCode === item.certTypeCode)
        if (certType) {
          sharedFieldMap.get(key).certTypes.push(certType)
        }
      }
    } else {
      // 私有字段全部显示
      const certType = props.certificateTypes.find(ct => ct.certTypeCode === item.certTypeCode)
      result.push({
        ...item,
        certTypes: certType ? [certType] : []
      })
    }
  })

  // 添加共有字段
  sharedFieldMap.forEach((value) => {
    result.push(value)
  })

  return result
})

// 获取行key
const getRowKey = (row) => {
  if (row.scope === '共有') {
    return `shared_${row.fieldNameEn}`
  }
  return `private_${row.fieldId}_${row.certTypeCode}`
}

// 获取规则类型名称
const getRuleTypeName = (type) => {
  const typeMap = {
    predefined: '预置规则',
    custom: '自定义规则'
  }
  return typeMap[type] || type
}

// 获取规则类型颜色
const getRuleTypeColor = (type) => {
  const colorMap = {
    predefined: 'success',
    custom: 'warning'
  }
  return colorMap[type] || 'info'
}

// 加载预置规则
const loadPredefinedRules = async () => {
  try {
    const res = await getPredefinedRuleList({ page: 1, pageSize: 1000 })
    predefinedRules.value = Array.isArray(res.records) ? res.records : []
  } catch (error) {
    ElMessage.error('加载预置规则失败')
  }
}

// 规则类型变化
const handleRuleTypeChange = () => {
  // 清空对应字段
  if (ruleForm.ruleType === 'predefined') {
    ruleForm.validationLogic = ''
    ruleForm.validationValue = ''
    ruleForm.errorMessage = ''
  } else {
    ruleForm.predefinedRuleId = null
  }
}

// 预置规则变化
const handlePredefinedRuleChange = (ruleId) => {
  const rule = predefinedRules.value.find(r => r.ruleId === ruleId)
  if (rule) {
    ruleForm.validationLogic = rule.validationLogic
    ruleForm.validationValue = rule.validationValue
    ruleForm.errorMessage = rule.errorMessage
  }
}

// 添加规则
const handleAddRule = (row) => {
  currentField.value = row
  currentRuleIndex.value = -1
  ruleDialogTitle.value = '添加规则'
  ruleForm.ruleType = 'predefined'
  ruleForm.predefinedRuleId = null
  ruleForm.validationLogic = ''
  ruleForm.validationValue = ''
  ruleForm.errorMessage = ''
  ruleDialogVisible.value = true
}

// 编辑规则
const handleEditRule = (row, rule, index) => {
  currentField.value = row
  currentRuleIndex.value = index
  ruleDialogTitle.value = '编辑规则'
  ruleForm.ruleType = rule.ruleType || 'custom'
  if (rule.ruleType === 'predefined') {
    ruleForm.predefinedRuleId = rule.predefinedRuleId
  } else {
    ruleForm.validationLogic = rule.validationLogic
    ruleForm.validationValue = rule.validationValue
    ruleForm.errorMessage = rule.errorMessage
  }
  ruleDialogVisible.value = true
}

// 删除规则
const handleDeleteRule = (row, index) => {
  ElMessageBox.confirm('确定要删除该规则吗？', '提示', {
    type: 'warning',
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  }).then(() => {
    const updatedRules = [...row.validationRules]
    updatedRules.splice(index, 1)
    emit('update-rules', row.fieldId, updatedRules)
    ElMessage.success('删除成功')
  }).catch(() => {})
}

// 保存规则
const handleSaveRule = async () => {
  if (!ruleFormRef.value) return

  await ruleFormRef.value.validate((valid) => {
    if (valid) {
      const updatedRules = [...currentField.value.validationRules]
      const newRule = {
        ruleType: ruleForm.ruleType,
        validationLogic: ruleForm.validationLogic,
        validationValue: ruleForm.validationValue,
        errorMessage: ruleForm.errorMessage
      }

      if (ruleForm.ruleType === 'predefined') {
        newRule.predefinedRuleId = ruleForm.predefinedRuleId
      }

      if (currentRuleIndex.value === -1) {
        // 添加新规则
        updatedRules.push(newRule)
      } else {
        // 更新现有规则
        updatedRules[currentRuleIndex.value] = newRule
      }

      emit('update-rules', currentField.value.fieldId, updatedRules)
      ruleDialogVisible.value = false
      ElMessage.success('保存成功')
    }
  })
}

// 编辑字段
const handleEditField = (row) => {
  emit('edit-field', row)
}

// 删除字段
const handleDeleteField = (row) => {
  emit('delete-field', row)
}

// 规则对话框关闭
const handleRuleDialogClose = () => {
  ruleFormRef.value?.resetFields()
}

// 展开变化
const handleExpandChange = (row, expandedRows) => {
  // 可以在这里加载更多数据
}

// 初始化
loadPredefinedRules()
</script>

<style scoped>
.field-rule-table {
  width: 100%;
}

.rule-table {
  border-radius: 8px;
  overflow: hidden;
}

.field-name {
  display: flex;
  align-items: center;
  gap: 8px;
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

.expand-content {
  padding: 20px;
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
}

.rule-item {
  padding: 16px;
  background: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  margin-bottom: 12px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.rule-item:hover {
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.1);
  transform: translateY(-2px);
}

.rule-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.rule-logic {
  flex: 1;
  font-weight: 600;
  color: #1e293b;
}

.rule-value {
  font-family: 'Courier New', monospace;
  padding: 4px 12px;
  background: #f1f5f9;
  border-radius: 4px;
  color: #3b82f6;
  font-size: 14px;
}

.rule-actions {
  display: flex;
  gap: 8px;
  padding-top: 8px;
  border-top: 1px solid #e2e8f0;
}

.add-rule-btn {
  display: flex;
  justify-content: center;
  margin-top: 16px;
}

.badge :deep(.el-badge__content) {
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
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

:deep(.el-table__expand-icon) {
  color: #3b82f6;
}

:deep(.el-tag--dark.el-tag--success) {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  border: none;
}

:deep(.el-tag--dark.el-tag--warning) {
  background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
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
