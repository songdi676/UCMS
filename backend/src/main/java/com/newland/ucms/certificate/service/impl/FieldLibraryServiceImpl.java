package com.newland.ucms.certificate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newland.ucms.certificate.dto.FieldLibraryDTO;
import com.newland.ucms.certificate.entity.FieldLibrary;
import com.newland.ucms.certificate.enums.StatusEnum;
import com.newland.ucms.certificate.mapper.FieldLibraryMapper;
import com.newland.ucms.certificate.service.FieldLibraryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 字段库服务实现类
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@Service
public class FieldLibraryServiceImpl extends ServiceImpl<FieldLibraryMapper, FieldLibrary>
    implements FieldLibraryService {

  @Override
  public List<FieldLibraryDTO> getPublicFields() {
    List<FieldLibrary> fields = baseMapper.selectPublicFields();
    return fields.stream()
        .map(this::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<FieldLibraryDTO> getFieldsByType(String fieldType) {
    List<FieldLibrary> fields = baseMapper.selectFieldsByType(fieldType);
    return fields.stream()
        .map(this::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public FieldLibraryDTO createCustomField(FieldLibraryDTO fieldLibraryDTO) {
    // 验证字段类型
    if (!"CUSTOM".equals(fieldLibraryDTO.getFieldType())) {
      throw new IllegalArgumentException("只能创建自定义字段");
    }

    // 验证字段英文名称是否已存在
    FieldLibrary existingField = baseMapper.selectByFieldNameEn(fieldLibraryDTO.getFieldNameEn());
    if (existingField != null) {
      throw new IllegalArgumentException("字段英文名称已存在");
    }

    // 创建字段
    FieldLibrary fieldLibrary = new FieldLibrary();
    BeanUtils.copyProperties(fieldLibraryDTO, fieldLibrary);
    fieldLibrary.setStatus(StatusEnum.ENABLED.getCode());

    baseMapper.insert(fieldLibrary);

    return toDTO(fieldLibrary);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public FieldLibraryDTO updateField(Long id, FieldLibraryDTO fieldLibraryDTO) {
    FieldLibrary fieldLibrary = baseMapper.selectById(id);
    if (fieldLibrary == null) {
      throw new IllegalArgumentException("字段不存在");
    }

    // 更新字段
    BeanUtils.copyProperties(fieldLibraryDTO, fieldLibrary, "id", "createdAt");
    baseMapper.updateById(fieldLibrary);

    return toDTO(fieldLibrary);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteField(Long id) {
    FieldLibrary fieldLibrary = baseMapper.selectById(id);
    if (fieldLibrary == null) {
      throw new IllegalArgumentException("字段不存在");
    }

    // 公共字段不允许删除
    if ("PUBLIC".equals(fieldLibrary.getFieldType())) {
      throw new IllegalArgumentException("公共字段不允许删除");
    }

    // 逻辑删除
    fieldLibrary.setIsDeleted(1);
    baseMapper.updateById(fieldLibrary);
  }

  @Override
  public FieldLibraryDTO toDTO(FieldLibrary fieldLibrary) {
    if (fieldLibrary == null) {
      return null;
    }

    FieldLibraryDTO dto = new FieldLibraryDTO();
    BeanUtils.copyProperties(fieldLibrary, dto);
    return dto;
  }
}
