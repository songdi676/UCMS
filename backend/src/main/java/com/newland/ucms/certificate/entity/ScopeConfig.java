package com.newland.ucms.certificate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newland.ucms.certificate.enums.StatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 应用范围配置表实体类
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@Data
@TableName("UCMS_SCOPE_CONFIG")
public class ScopeConfig {

  /**
   * 主键ID
   */
  @TableId(type = IdType.AUTO)
  private Long id;

  /**
   * 应用系统（移动云厅、网格通等，可为空表示全局）
   */
  @TableField("business_channel")
  private String businessChannel;

  /**
   * 渠道小类（可为空）
   */
  @TableField("channel_sub_type")
  private String channelSubType;

  /**
   * 地市（可为空）
   */
  @TableField("city")
  private String city;

  /**
   * 区县（可为空）
   */
  @TableField("district")
  private String district;

  /**
   * 机构（可为空）
   */
  @TableField("institution")
  private String institution;

  /**
   * 允许的证件类型列表（JSON数组，如["1","2","3"]，支持多个，使用|分隔）
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
