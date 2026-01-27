package com.newland.ucms.certificate.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newland.ucms.certificate.dto.CertificateTypeDTO;
import com.newland.ucms.certificate.service.CertificateTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 证件类型控制器
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@RestController
@RequestMapping("/api/certificate-type")
public class CertificateTypeController {

  @Autowired
  private CertificateTypeService certificateTypeService;

  /**
   * 分页查询证件类型列表
   *
   * @param page    页码
   * @param pageSize 每页大小
   * @param params   查询参数
   * @return 分页结果
   */
  @GetMapping
  public ResponseEntity<IPage<CertificateTypeDTO>> getCertificateTypeList(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int pageSize,
      @RequestParam(required = false) Map<String, Object> params) {
    IPage<CertificateTypeDTO> result = certificateTypeService.getCertificateTypeList(page, pageSize, params);
    return ResponseEntity.ok(result);
  }

  /**
   * 获取证件类型详情
   *
   * @param id 证件类型ID
   * @return 证件类型详情
   */
  @GetMapping("/{id}")
  public ResponseEntity<CertificateTypeDTO> getCertificateTypeDetail(@PathVariable Long id) {
    CertificateTypeDTO result = certificateTypeService.getCertificateTypeDetail(id);
    return ResponseEntity.ok(result);
  }

  /**
   * 创建证件类型
   *
   * @param certificateTypeDTO 证件类型信息
   * @return 创建后的证件类型信息
   */
  @PostMapping
  public ResponseEntity<CertificateTypeDTO> createCertificateType(
      @RequestBody CertificateTypeDTO certificateTypeDTO) {
    CertificateTypeDTO result = certificateTypeService.createCertificateType(certificateTypeDTO);
    return ResponseEntity.ok(result);
  }

  /**
   * 更新证件类型
   *
   * @param id                    证件类型ID
   * @param certificateTypeDTO 证件类型信息
   * @return 更新后的证件类型信息
   */
  @PutMapping("/{id}")
  public ResponseEntity<CertificateTypeDTO> updateCertificateType(
      @PathVariable Long id,
      @RequestBody CertificateTypeDTO certificateTypeDTO) {
    CertificateTypeDTO result = certificateTypeService.updateCertificateType(id, certificateTypeDTO);
    return ResponseEntity.ok(result);
  }

  /**
   * 删除证件类型
   *
   * @param id 证件类型ID
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCertificateType(@PathVariable Long id) {
    certificateTypeService.deleteCertificateType(id);
    return ResponseEntity.ok().build();
  }
}
