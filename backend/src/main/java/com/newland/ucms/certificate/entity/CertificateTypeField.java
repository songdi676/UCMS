package com.newland.ucms.certificate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
   * 字段ID（外键UCMS_FIELD_LIBRARY.id）
   */
  @TableField("field_id")
  private Long fieldId;

  /**
   * 字段英文名称（冗余字段，便于查询）
   */
  @TableField("field_name_en")
  private String fieldNameEn;

  /**
   * 字段中文名称（冗余字段，便于查询）
   */
  @TableField("field_name_cn")
  private String fieldNameCn;

  /**
   * 是否必填：0-否, 1-是
   */
  @TableField("is_required")
  private Integer isRequired;

  /**
   * 字段范围：共有、私有
   * 共有：所有证件类型共用该字段的规则(如姓名、性别)
   * 私有：每个证件类型有独立的字段规则(如证件号码)
   */
  @TableField("scope")
  private String scope;

  /**
   * 更新人
   */
  @TableField("updated_by")
  private String updatedBy;

  /**
   * 更新时间
   */
  @TableField("updated_at")
  private LocalDateTime updatedAt;

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
