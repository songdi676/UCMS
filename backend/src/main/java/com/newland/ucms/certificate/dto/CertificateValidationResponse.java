package com.newland.ucms.certificate.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 证件验证响应DTO
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@Data
public class CertificateValidationResponse {

  /**
   * 验证结果：true-成功，false-失败
   */
  private Boolean success;

  /**
   * 错误消息
   */
  private String message;

  /**
   * 错误字段列表（键值对，键为字段名，值为错误信息）
   */
  private Map<String, String> errors;
}
