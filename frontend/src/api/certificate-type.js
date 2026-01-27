import request from '@/utils/request'

/**
 * 获取证件类型列表
 */
export const getCertificateTypeList = (params) => {
  return request({
    url: '/certificate-type',
    method: 'get',
    params
  })
}

/**
 * 获取证件类型详情
 */
export const getCertificateTypeDetail = (id) => {
  return request({
    url: `/certificate-type/${id}`,
    method: 'get'
  })
}

/**
 * 创建证件类型
 */
export const createCertificateType = (data) => {
  return request({
    url: '/certificate-type',
    method: 'post',
    data
  })
}

/**
 * 更新证件类型
 */
export const updateCertificateType = (id, data) => {
  return request({
    url: `/certificate-type/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除证件类型
 */
export const deleteCertificateType = (id) => {
  return request({
    url: `/certificate-type/${id}`,
    method: 'delete'
  })
}

/**
 * 获取公共字段列表
 */
export const getPublicFields = () => {
  return request({
    url: '/field/public',
    method: 'get'
  })
}

/**
 * 创建自定义字段
 */
export const createCustomField = (data) => {
  return request({
    url: '/field/custom',
    method: 'post',
    data
  })
}
