package com.newland.ucms.certificate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newland.ucms.certificate.enums.StatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 证件类型主表实体类
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@Data
@TableName("UCMS_CERTIFICATE_TYPE")
public class CertificateType {

  /**
   * 主键ID
   */
  @TableId(type = IdType.AUTO)
  private Long id;

  /**
   * 证件类型代码（1, 2, 3等）
   */
  @TableField("cert_type_code")
  private String certTypeCode;

  /**
   * 证件类型名称（身份证、护照等）
   */
  @TableField("cert_type_name")
  private String certTypeName;

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
