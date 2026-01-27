package com.newland.ucms.certificate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 证件类型字段配置表实体类
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@Data
@TableName("UCMS_CERTIFICATE_TYPE_FIELD")
public class CertificateTypeField {

  /**
   * 主键ID
   */
  @TableId(type = IdType.AUTO)
  private Long id;

  /**
   * 证件类型代码（外键UCMS_CERTIFICATE_TYPE.cert_type_code）
   */
  @TableField("cert_type_code")
  private String certTypeCode;

  /**
   * 字段名称（name、cert_number等）
   */
  @TableField("field_name")
  private String fieldName;

  /**
   * 字段显示名称（姓名、证件号码等）
   */
  @TableField("field_display_name")
  private String fieldDisplayName;

  /**
   * 是否必填：0-否, 1-是
   */
  @TableField("is_required")
  private Integer isRequired;

  /**
   * 字段类型（text/number/date等）
   */
  @TableField("field_type")
  private String fieldType;

  /**
   * 字段长度
   */
  @TableField("field_length")
  private Integer fieldLength;

  /**
   * 排序
   */
  @TableField("sort_order")
  private Integer sortOrder;

  /**
   * 创建时间
   */
  @TableField("created_at")
  private LocalDateTime createdAt;

  /**
   * 逻辑删除标记：0-未删除, 1-已删除
   */
  @TableField("is_deleted")
  private Integer isDeleted;
}
