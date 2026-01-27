package com.newland.ucms.certificate.dto;

import lombok.Data;

import java.util.List;

/**
 * 证件类型DTO
 * 包含证件类型和字段配置的完整信息
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@Data
public class CertificateTypeDTO {

  /**
   * 证件类型代码
   */
  private String certTypeCode;

  /**
   * 证件类型名称
   */
  private String certTypeName;

  /**
   * 状态
   */
  private Integer status;

  /**
   * 字段配置列表
   */
  private List<CertificateTypeFieldDTO> fields;
}
