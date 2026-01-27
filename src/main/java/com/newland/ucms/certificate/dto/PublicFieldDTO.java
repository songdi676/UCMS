package com.newland.ucms.certificate.dto;

import lombok.Data;

/**
 * 公共字段DTO
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@Data
public class PublicFieldDTO {

  /**
   * 字段ID
   */
  private Long id;

  /**
   * 字段名称
   */
  private String fieldName;

  /**
   * 英文字段名称
   */
  private String fieldNameEn;
}
