package com.newland.ucms.certificate.dto;

import lombok.Data;

import java.util.Map;

/**
 * 证件验证请求DTO
 * 包含业务类型、证件类型、字段值等
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@Data
public class CertificateRequest {

  /**
   * 业务类型
   */
  private String businessType;

  /**
   * 业务编号
   */
  private String businessCode;

  /**
   * 受理渠道
   */
  private String channelSubType;

  /**
   * 地市
   */
  private String city;

  /**
   * 区县
   */
  private String district;

  /**
   * 机构
   */
  private String institution;

  /**
   * 证件类型代码
   */
  private String certTypeCode;

  /**
   * 证件字段值（键值对）
   */
  private Map<String, Object> fieldValues;
}
