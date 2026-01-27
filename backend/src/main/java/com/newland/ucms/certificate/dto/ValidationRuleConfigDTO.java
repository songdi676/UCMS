package com.newland.ucms.certificate.dto;

import lombok.Data;

import java.util.List;

/**
 * 校验规则配置DTO
 * 包含字段+逻辑+值
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@Data
public class ValidationRuleConfigDTO {

  /**
   * 规则ID
   */
  private Long ruleId;

  /**
   * 规则名称
   */
  private String ruleName;

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
   * 可用证件类型
   */
  private List<String> allowedCertTypes;

  /**
   * 状态
   */
  private Integer status;

  /**
   * 校验规则明细列表
   */
  private List<ValidationRuleItemDTO> ruleItems;
}
