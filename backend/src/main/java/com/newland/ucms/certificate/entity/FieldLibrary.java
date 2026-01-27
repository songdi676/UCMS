package com.newland.ucms.certificate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 字段库表实体类
 * 支持公共字段和自定义字段
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@Data
@TableName("UCMS_FIELD_LIBRARY")
public class FieldLibrary {

  /**
   * 主键ID
   */
  @TableId(type = IdType.AUTO)
  private Long id;

  /**
   * 字段中文名称（如：姓名、性别、民族）
   */
  @TableField("field_name_cn")
  private String fieldNameCn;

  /**
   * 字段英文名称（如：name、gender、nation）
   */
  @TableField("field_name_en")
  private String fieldNameEn;

  /**
   * 字段类型：PUBLIC-公共字段，CUSTOM-自定义字段
   */
  @TableField("field_type")
  private String fieldType;

  /**
   * 数据类型：text、number、date、date_range等
   */
  @TableField("field_data_type")
  private String fieldDataType;

  /**
   * 字段长度
   */
  @TableField("field_length")
  private Integer fieldLength;

  /**
   * 默认值
   */
  @TableField("default_value")
  private String defaultValue;

  /**
   * 创建人（自定义字段需要记录）
   */
  @TableField("creator")
  private String creator;

  /**
   * 字段描述
   */
  @TableField("description")
  private String description;

  /**
   * 状态：0-停用，1-启用
   */
  @TableField("status")
  private Integer status;

  /**
   * 创建时间
   */
  @TableField("created_at")
  private LocalDateTime createdAt;

  /**
   * 更新时间
   */
  @TableField("updated_at")
  private LocalDateTime updatedAt;

  /**
   * 逻辑删除标记：0-未删除，1-已删除
   */
  @TableField("is_deleted")
  private Integer isDeleted;
}
