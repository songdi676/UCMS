package com.newland.ucms.certificate.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 字段库DTO
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@Data
public class FieldLibraryDTO {

  /**
   * 字段ID
   */
  private Long id;

  /**
   * 字段中文名称
   */
  private String fieldNameCn;

  /**
   * 字段英文名称
   */
  private String fieldNameEn;

  /**
   * 字段类型：PUBLIC-公共字段，CUSTOM-自定义字段
   */
  private String fieldType;

  /**
   * 数据类型：text、number、date、date_range等
   */
  private String fieldDataType;

  /**
   * 字段长度
   */
  private Integer fieldLength;

  /**
   * 默认值
   */
  private String defaultValue;

  /**
   * 创建人
   */
  private String creator;

  /**
   * 字段描述
   */
  private String description;

  /**
   * 状态：0-停用，1-启用
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
}
