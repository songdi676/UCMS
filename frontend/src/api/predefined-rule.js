import request from '@/utils/request'

/**
 * 获取预置规则列表
 */
export const getPredefinedRuleList = (params) => {
  return request({
    url: '/predefined-rules',
    method: 'get',
    params
  })
}

/**
 * 获取预置规则详情
 */
export const getPredefinedRuleDetail = (id) => {
  return request({
    url: `/predefined-rules/${id}`,
    method: 'get'
  })
}

/**
 * 创建预置规则
 */
export const createPredefinedRule = (data) => {
  return request({
    url: '/predefined-rules',
    method: 'post',
    data
  })
}

/**
 * 更新预置规则
 */
export const updatePredefinedRule = (id, data) => {
  return request({
    url: `/predefined-rules/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除预置规则
 */
export const deletePredefinedRule = (id) => {
  return request({
    url: `/predefined-rules/${id}`,
    method: 'delete'
  })
}
