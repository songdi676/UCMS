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
   * 字段配置ID
   */
  private Long id;

  /**
   * 字段ID（关联UCMS_FIELD_LIBRARY）
   */
  private Long fieldId;

  /**
   * 字段中文名称
   */
  private String fieldNameCn;

  /**
   * 字段英文名称
   */
  private String fieldNameEn;

  /**
   * 字段类型（PUBLIC-公共字段，CUSTOM-自定义字段）
   */
  private String fieldType;

  /**
   * 数据类型（text、number、date等）
   */
  private String fieldDataType;

  /**
   * 字段长度
   */
  private Integer fieldLength;

  /**
   * 是否必填
   */
  private Integer isRequired;

  /**
   * 排序
   */
  private Integer sortOrder;
}
