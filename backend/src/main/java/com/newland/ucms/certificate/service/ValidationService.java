package com.newland.ucms.certificate.service;

import com.newland.ucms.certificate.dto.CertificateRequest;
import com.newland.ucms.certificate.dto.CertificateValidationResponse;

/**
 * 统一验证服务接口
 * 用于验证证件信息的合法性
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
public interface ValidationService {

  /**
   * 验证证件信息
   *
   * @param request 证件验证请求
   * @return 验证结果
   */
  CertificateValidationResponse validateCertificate(CertificateRequest request);

  /**
   * 获取去重后的证件类型字段列表
   * 按照scope字段去重:
   * - scope="共有"(多证件共有的字段): 只保留一个字段,所有证件类型共用
   * - scope="私有"(证件私有的字段): 保留所有字段,每个证件类型独立
   *
   * @param certTypeCodes 证件类型代码列表
   * @return 去重后的字段列表
   */
  java.util.List<com.newland.ucms.certificate.entity.CertificateTypeField> getDeduplicatedFields(java.util.List<String> certTypeCodes);

  /**
   * 应用预置规则
   * 查询预置规则并转换为ValidationRuleItem格式
   *
   * @param certTypeCode 证件类型代码
   * @param fields 字段列表
   * @return 预置规则列表
   */
  java.util.List<com.newland.ucms.certificate.entity.ValidationRuleItem> applyPredefinedRules(String certTypeCode, java.util.List<com.newland.ucms.certificate.entity.CertificateTypeField> fields);
}
