package com.newland.ucms.certificate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newland.ucms.certificate.entity.CertificateTypeField;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 证件类型字段配置Mapper接口
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@Mapper
public interface CertificateTypeFieldMapper extends BaseMapper<CertificateTypeField> {

  /**
   * 根据证件类型代码查询字段配置
   *
   * @param certTypeCode 证件类型代码
   * @return 证件类型字段配置列表
   */
  List<CertificateTypeField> findByCertTypeCode(@Param("certTypeCode") String certTypeCode);
}
