package com.newland.ucms.certificate.controller;

import com.newland.ucms.certificate.dto.CertificateRequest;
import com.newland.ucms.certificate.dto.CertificateValidationResponse;
import com.newland.ucms.certificate.dto.UnifiedValidationRequest;
import com.newland.ucms.certificate.service.ValidationService;
import com.newland.ucms.certificate.util.RequestParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统一验证接口控制器
 *
 * @author UCMS Team
 * @since 2026-01-28
 */
@RestController
@RequestMapping("/api/validation")
public class ValidationController {

  @Autowired
  private ValidationService validationService;

  /**
   * 统一验证接口
   * 验证证件信息的合法性
   *
   * @param unifiedRequest 统一验证请求（业务受理平台调用）
   * @return 验证结果
   */
  @PostMapping("/certificate")
  public ResponseEntity<CertificateValidationResponse> validateCertificate(@RequestBody UnifiedValidationRequest unifiedRequest) {
    // 解析统一请求，转换为证件验证请求
    CertificateRequest certificateRequest = RequestParser.parse(unifiedRequest);

    // 调用验证服务
    CertificateValidationResponse response = validationService.validateCertificate(certificateRequest);
    return ResponseEntity.ok(response);
  }
}
