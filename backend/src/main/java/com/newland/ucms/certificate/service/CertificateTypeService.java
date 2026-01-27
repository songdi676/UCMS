package com.newland.ucms.certificate.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.newland.ucms.certificate.dto.CertificateTypeDTO;
import com.newland.ucms.certificate.entity.CertificateType;

import java.util.Map;

/**
 * 证件类型服务接口
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
public interface CertificateTypeService extends IService<CertificateType> {

  /**
   * 分页查询证件类型列表
   *
   * @param page      页码
   * @param pageSize  每页大小
   * @param params    查询参数
   * @return 分页结果
   */
  IPage<CertificateTypeDTO> getCertificateTypeList(int page, int pageSize, Map<String, Object> params);

  /**
   * 获取证件类型详情
   *
   * @param id 证件类型ID
   * @return 证件类型详情
   */
  CertificateTypeDTO getCertificateTypeDetail(Long id);

  /**
   * 创建证件类型
   *
   * @param certificateTypeDTO 证件类型信息
   * @return 创建后的证件类型信息
   */
  CertificateTypeDTO createCertificateType(CertificateTypeDTO certificateTypeDTO);

  /**
   * 更新证件类型
   *
   * @param id                    证件类型ID
   * @param certificateTypeDTO 证件类型信息
   * @return 更新后的证件类型信息
   */
  CertificateTypeDTO updateCertificateType(Long id, CertificateTypeDTO certificateTypeDTO);

  /**
   * 删除证件类型
   *
   * @param id 证件类型ID
   */
  void deleteCertificateType(Long id);

  /**
   * 实体转DTO
   *
   * @param certificateType 实体
   * @return DTO
   */
  CertificateTypeDTO toDTO(CertificateType certificateType);
}
