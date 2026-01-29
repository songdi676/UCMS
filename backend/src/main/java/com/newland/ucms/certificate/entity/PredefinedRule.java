package com.newland.ucms.certificate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 预置规则表实体类
 *
 * @author UCMS Team
 * @since 2026-01-29
 */
@Data
@TableName("UCMS_PREDEFINED_RULE")
public class PredefinedRule {

  /**
   * 主键ID
   */
  @TableId(type = IdType.AUTO)
  private Long id;

  /**
   * 证件类型代码
   */
  @TableField("cert_type_code")
  private String certTypeCode;

  /**
   * 字段ID,关联UCMS_FIELD_LIBRARY.id
   */
  @TableField("field_id")
  private Long fieldId;

  /**
   * 规则名称
   */
  @TableField("rule_name")
  private String ruleName;

  /**
   * 校验逻辑
   */
  @TableField("validation_logic")
  private String validationLogic;

  /**
   * 校验值
   */
  @TableField("validation_value")
  private String validationValue;

  /**
   * 错误信息
   */
  @TableField("error_message")
  private String errorMessage;

  /**
   * 状态,1-启用,0-禁用
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
   * 创建人
   */
  @TableField("created_by")
  private String createdBy;

  /**
   * 更新人
   */
  @TableField("updated_by")
  private String updatedBy;

  /**
   * 删除标识,1-已删除,0-未删除
   */
  @TableField("is_deleted")
  private Integer isDeleted;
}
