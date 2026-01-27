package com.newland.ucms.certificate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.newland.ucms.certificate.entity.FieldLibrary;
import com.newland.ucms.certificate.dto.FieldLibraryDTO;

import java.util.List;

/**
 * 字段库服务接口
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
public interface FieldLibraryService extends IService<FieldLibrary> {

  /**
   * 查询公共字段列表
   *
   * @return 公共字段列表
   */
  List<FieldLibraryDTO> getPublicFields();

  /**
   * 根据字段类型查询字段列表
   *
   * @param fieldType 字段类型（PUBLIC/CUSTOM）
   * @return 字段列表
   */
  List<FieldLibraryDTO> getFieldsByType(String fieldType);

  /**
   * 创建自定义字段
   *
   * @param fieldLibraryDTO 字段信息
   * @return 创建后的字段信息
   */
  FieldLibraryDTO createCustomField(FieldLibraryDTO fieldLibraryDTO);

  /**
   * 更新字段
   *
   * @param id 字段ID
   * @param fieldLibraryDTO 字段信息
   * @return 更新后的字段信息
   */
  FieldLibraryDTO updateField(Long id, FieldLibraryDTO fieldLibraryDTO);

  /**
   * 删除字段
   *
   * @param id 字段ID
   */
  void deleteField(Long id);

  /**
   * 实体转DTO
   *
   * @param fieldLibrary 实体
   * @return DTO
   */
  FieldLibraryDTO toDTO(FieldLibrary fieldLibrary);
}
