package com.newland.ucms.certificate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newland.ucms.certificate.enums.StatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 校验规则配置表实体类
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@Data
@TableName("UCMS_VALIDATION_RULE_CONFIG")
public class ValidationRuleConfig {

  /**
   * 主键ID
   */
  @TableId(type = IdType.AUTO)
  private Long id;

  /**
   * 业务类型（如"档案补正"等）
   */
  @TableField("business_type")
  private String businessType;

  /**
   * 业务编号
   */
  @TableField("business_code")
  private String businessCode;

  /**
   * 受理渠道
   */
  @TableField("channel_sub_type")
  private String channelSubType;

  /**
   * 可用证件类型（JSON数组）
   */
  @TableField("allowed_cert_types")
  private String allowedCertTypes;

  /**
   * 状态：0-停用, 1-启用
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
   * 修改人
   */
  @TableField("updated_by")
  private String updatedBy;

  /**
   * 逻辑删除标记：0-未删除, 1-已删除
   */
  @TableField("is_deleted")
  private Integer isDeleted;
}
