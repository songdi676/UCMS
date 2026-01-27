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
}
