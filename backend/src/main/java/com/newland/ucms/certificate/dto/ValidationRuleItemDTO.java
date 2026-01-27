package com.newland.ucms.certificate.dto;

import lombok.Data;

/**
 * 校验规则明细DTO
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@Data
public class ValidationRuleItemDTO {

  /**
   * 规则明细ID
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
   * 校验逻辑
   */
  private String validationLogic;

  /**
   * 校验值
   */
  private String validationValue;

  /**
   * 排序
   */
  private Integer sortOrder;
}
