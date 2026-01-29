import request from '@/utils/request'

/**
 * 获取字段规则列表
 */
export const getFieldRules = (params) => {
  return request({
    url: '/field-rule',
    method: 'get',
    params
  })
}

/**
 * 获取字段规则详情
 */
export const getFieldRuleDetail = (id) => {
  return request({
    url: `/field-rule/${id}`,
    method: 'get'
  })
}

/**
 * 创建字段规则
 */
export const createFieldRule = (data) => {
  return request({
    url: '/field-rule',
    method: 'post',
    data
  })
}

/**
 * 更新字段规则
 */
export const updateFieldRule = (id, data) => {
  return request({
    url: `/field-rule/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除字段规则
 */
export const deleteFieldRule = (id) => {
  return request({
    url: `/field-rule/${id}`,
    method: 'delete'
  })
}
