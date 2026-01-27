package com.newland.ucms.certificate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 公共字段库表实体类
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@Data
@TableName("UCMS_PUBLIC_FIELD_LIBRARY")
public class PublicFieldLibrary {

  /**
   * 主键ID
   */
  @TableId(type = IdType.AUTO)
  private Long id;

  /**
   * 字段名称（姓名、民族、性别等）
   */
  @TableField("field_name")
  private String fieldName;

  /**
   * 英文字段名称（name、nation、gender等）
   */
  @TableField("field_name_en")
  private String fieldNameEn;

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
