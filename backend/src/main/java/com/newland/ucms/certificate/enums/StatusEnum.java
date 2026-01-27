package com.newland.ucms.certificate.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态枚举
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {

  /**
   * 停用
   */
  DISABLED(0, "停用"),

  /**
   * 启用
   */
  ENABLED(1, "启用");

  /**
   * 状态码
   */
  private final Integer code;

  /**
   * 状态描述
   */
  private final String description;
}
