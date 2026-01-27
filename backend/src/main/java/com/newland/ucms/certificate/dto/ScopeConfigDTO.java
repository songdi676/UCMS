package com.newland.ucms.certificate.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 应用范围配置DTO
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@Data
public class ScopeConfigDTO {

  /**
   * 主键ID
   */
  private Long id;

  /**
   * 应用系统
   */
  private String businessChannel;

  /**
   * 渠道小类
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
   * 允许的证件类型列表
   */
  private String allowedCertTypes;

  /**
   * 状态
   */
  private Integer status;

  /**
   * 创建时间
   */
  private LocalDateTime createdAt;

  /**
   * 更新时间
   */
  private LocalDateTime updatedAt;

  /**
   * 创建人
   */
  private String createdBy;

  /**
   * 修改人
   */
  private String updatedBy;
}
