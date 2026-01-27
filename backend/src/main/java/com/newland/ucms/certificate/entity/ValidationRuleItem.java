package com.newland.ucms.certificate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 校验规则明细表实体类
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@Data
@TableName("UCMS_VALIDATION_RULE_ITEM")
public class ValidationRuleItem {

  /**
   * 主键ID
   */
  @TableId(type = IdType.AUTO)
  private Long id;

  /**
   * 规则ID（外键UCMS_VALIDATION_RULE_CONFIG.id）
   */
  @TableField("rule_id")
  private Long ruleId;

  /**
   * 字段名称（关联证件类型字段）
   */
  @TableField("field_name")
  private String fieldName;

  /**
   * 校验逻辑（>、<、>=等）
   */
  @TableField("validation_logic")
  private String validationLogic;

  /**
   * 校验值（如18表示age>18）
   */
  @TableField("validation_value")
  private String validationValue;

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
