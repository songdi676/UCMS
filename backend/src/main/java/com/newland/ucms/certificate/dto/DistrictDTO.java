package com.newland.ucms.certificate.dto;

import lombok.Data;

/**
 * 区县DTO
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@Data
public class DistrictDTO {

  /**
   * 区县代码
   */
  private String code;

  /**
   * 区县名称
   */
  private String name;

  /**
   * 所属地市代码
   */
  private String cityCode;
}
