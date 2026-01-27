package com.newland.ucms.certificate.dto;

import lombok.Data;

/**
 * 证件字段配置DTO
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@Data
public class CertificateTypeFieldDTO {

  /**
   * 字段ID
   */
  private Long id;

  /**
   * 字段名称
   */
  private String fieldName;

  /**
   * 字段显示名称
   */
  private String fieldDisplayName;

  /**
   * 是否必填
   */
  private Integer isRequired;

  /**
   * 字段类型
   */
  private String fieldType;

  /**
   * 字段长度
   */
  private Integer fieldLength;

  /**
   * 排序
   */
  private Integer sortOrder;
}
