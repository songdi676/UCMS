package com.newland.ucms.certificate.dto;

import lombok.Data;

/**
 * 机构DTO
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@Data
public class InstitutionDTO {

  /**
   * 机构代码
   */
  private String code;

  /**
   * 机构名称
   */
  private String name;

  /**
   * 所属地市代码
   */
  private String cityCode;

  /**
   * 所属区县代码
   */
  private String districtCode;
}
