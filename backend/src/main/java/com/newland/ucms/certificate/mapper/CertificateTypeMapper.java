package com.newland.ucms.certificate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newland.ucms.certificate.entity.CertificateType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 证件类型Mapper接口
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@Mapper
public interface CertificateTypeMapper extends BaseMapper<CertificateType> {

  /**
   * 根据证件类型代码查询证件类型
   *
   * @param certTypeCode 证件类型代码
   * @return 证件类型实体
   */
  CertificateType selectByCertTypeCode(@Param("certTypeCode") String certTypeCode);

  /**
   * 根据状态查询所有证件类型
   *
   * @param status 状态：0-停用, 1-启用
   * @return 证件类型列表
   */
  List<CertificateType> findAllByStatus(@Param("status") Integer status);
}
