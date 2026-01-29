package com.newland.ucms.certificate.dto;

import lombok.Data;

/**
 * 预置规则DTO
 *
 * @author UCMS Team
 * @since 2026-01-29
 */
@Data
public class PredefinedRuleDTO {

  /**
   * 规则ID
   */
  private Long ruleId;

  /**
   * 证件类型代码
   */
  private String certTypeCode;

  /**
   * 字段ID
   */
  private Long fieldId;

  /**
   * 规则名称
   */
  private String ruleName;

  /**
   * 校验逻辑
   */
  private String validationLogic;

  /**
   * 校验值
   */
  private String validationValue;

  /**
   * 错误信息
   */
  private String errorMessage;

  /**
   * 状态,1-启用,0-禁用
   */
  private Integer status;
}
