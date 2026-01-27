package com.newland.ucms.certificate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newland.ucms.certificate.dto.CertificateTypeDTO;
import com.newland.ucms.certificate.dto.CertificateTypeFieldDTO;
import com.newland.ucms.certificate.entity.CertificateType;
import com.newland.ucms.certificate.entity.CertificateTypeField;
import com.newland.ucms.certificate.entity.FieldLibrary;
import com.newland.ucms.certificate.enums.StatusEnum;
import com.newland.ucms.certificate.mapper.CertificateTypeFieldMapper;
import com.newland.ucms.certificate.mapper.CertificateTypeMapper;
import com.newland.ucms.certificate.mapper.FieldLibraryMapper;
import com.newland.ucms.certificate.service.CertificateTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 证件类型服务实现类
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@Service
public class CertificateTypeServiceImpl extends ServiceImpl<CertificateTypeMapper, CertificateType>
    implements CertificateTypeService {

  @Autowired
  private CertificateTypeFieldMapper certificateTypeFieldMapper;

  @Autowired
  private FieldLibraryMapper fieldLibraryMapper;

  @Override
  public IPage<CertificateTypeDTO> getCertificateTypeList(int page, int pageSize, Map<String, Object> params) {
    Page<CertificateType> pageParam = new Page<>(page, pageSize);

    LambdaQueryWrapper<CertificateType> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(CertificateType::getIsDeleted, 0);

    // 证件类型名称模糊查询
    if (params.containsKey("certTypeName") && StringUtils.hasText(params.get("certTypeName").toString())) {
      queryWrapper.like(CertificateType::getCertTypeName, params.get("certTypeName").toString());
    }

    // 状态查询
    if (params.containsKey("status") && params.get("status") != null) {
      queryWrapper.eq(CertificateType::getStatus, Integer.parseInt(params.get("status").toString()));
    }

    // 创建人模糊查询
    if (params.containsKey("createdBy") && StringUtils.hasText(params.get("createdBy").toString())) {
      queryWrapper.like(CertificateType::getCreatedBy, params.get("createdBy").toString());
    }

    // 按创建时间倒序
    queryWrapper.orderByDesc(CertificateType::getCreatedAt);

    IPage<CertificateType> pageResult = baseMapper.selectPage(pageParam, queryWrapper);

    // 转换为DTO并填充字段信息
    Page<CertificateTypeDTO> dtoPage = new Page<>(page, pageSize);
    dtoPage.setTotal(pageResult.getTotal());
    dtoPage.setRecords(pageResult.getRecords().stream()
        .map(this::toDTOWithFields)
        .collect(Collectors.toList()));

    return dtoPage;
  }

  @Override
  public CertificateTypeDTO getCertificateTypeDetail(Long id) {
    CertificateType certificateType = baseMapper.selectById(id);
    if (certificateType == null || certificateType.getIsDeleted() == 1) {
      throw new IllegalArgumentException("证件类型不存在");
    }
    return toDTOWithFields(certificateType);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public CertificateTypeDTO createCertificateType(CertificateTypeDTO certificateTypeDTO) {
    // 验证证件类型代码是否已存在
    LambdaQueryWrapper<CertificateType> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(CertificateType::getCertTypeCode, certificateTypeDTO.getCertTypeCode());
    queryWrapper.eq(CertificateType::getIsDeleted, 0);
    CertificateType existing = baseMapper.selectOne(queryWrapper);
    if (existing != null) {
      throw new IllegalArgumentException("证件类型代码已存在");
    }

    // 验证字段配置
    if (certificateTypeDTO.getFields() == null || certificateTypeDTO.getFields().isEmpty()) {
      throw new IllegalArgumentException("请至少配置一个字段");
    }

    // 创建证件类型
    CertificateType certificateType = new CertificateType();
    BeanUtils.copyProperties(certificateTypeDTO, certificateType, "id", "fields");
    certificateType.setStatus(StatusEnum.ENABLED.getCode());
    certificateType.setIsDeleted(0);

    baseMapper.insert(certificateType);

    // 创建字段配置
    if (certificateTypeDTO.getFields() != null && !certificateTypeDTO.getFields().isEmpty()) {
      createCertificateTypeFields(certificateType.getId(), certificateTypeDTO.getCertTypeCode(),
          certificateTypeDTO.getFields());
    }

    return getCertificateTypeDetail(certificateType.getId());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public CertificateTypeDTO updateCertificateType(Long id, CertificateTypeDTO certificateTypeDTO) {
    CertificateType certificateType = baseMapper.selectById(id);
    if (certificateType == null || certificateType.getIsDeleted() == 1) {
      throw new IllegalArgumentException("证件类型不存在");
    }

    // 更新证件类型
    BeanUtils.copyProperties(certificateTypeDTO, certificateType, "id", "fields");
    baseMapper.updateById(certificateType);

    // 删除原有字段配置
    LambdaQueryWrapper<CertificateTypeField> deleteWrapper = new LambdaQueryWrapper<>();
    deleteWrapper.eq(CertificateTypeField::getCertTypeCode, certificateType.getCertTypeCode());
    certificateTypeFieldMapper.delete(deleteWrapper);

    // 创建新的字段配置
    if (certificateTypeDTO.getFields() != null && !certificateTypeDTO.getFields().isEmpty()) {
      createCertificateTypeFields(id, certificateType.getCertTypeCode(), certificateTypeDTO.getFields());
    }

    return getCertificateTypeDetail(id);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteCertificateType(Long id) {
    CertificateType certificateType = baseMapper.selectById(id);
    if (certificateType == null || certificateType.getIsDeleted() == 1) {
      throw new IllegalArgumentException("证件类型不存在");
    }

    // 逻辑删除证件类型
    certificateType.setIsDeleted(1);
    baseMapper.updateById(certificateType);

    // 逻辑删除字段配置
    LambdaQueryWrapper<CertificateTypeField> deleteWrapper = new LambdaQueryWrapper<>();
    deleteWrapper.eq(CertificateTypeField::getCertTypeCode, certificateType.getCertTypeCode());
    List<CertificateTypeField> fields = certificateTypeFieldMapper.selectList(deleteWrapper);
    fields.forEach(field -> {
      field.setIsDeleted(1);
      certificateTypeFieldMapper.updateById(field);
    });
  }

  @Override
  public CertificateTypeDTO toDTO(CertificateType certificateType) {
    if (certificateType == null) {
      return null;
    }

    CertificateTypeDTO dto = new CertificateTypeDTO();
    BeanUtils.copyProperties(certificateType, dto);
    return dto;
  }

  /**
   * 实体转DTO并填充字段信息
   *
   * @param certificateType 实体
   * @return DTO
   */
  private CertificateTypeDTO toDTOWithFields(CertificateType certificateType) {
    CertificateTypeDTO dto = toDTO(certificateType);

    // 查询字段配置
    LambdaQueryWrapper<CertificateTypeField> fieldWrapper = new LambdaQueryWrapper<>();
    fieldWrapper.eq(CertificateTypeField::getCertTypeCode, certificateType.getCertTypeCode());
    fieldWrapper.eq(CertificateTypeField::getIsDeleted, 0);
    fieldWrapper.orderByAsc(CertificateTypeField::getSortOrder);
    List<CertificateTypeField> fields = certificateTypeFieldMapper.selectList(fieldWrapper);

    // 转换为DTO
    List<CertificateTypeFieldDTO> fieldDTOs = new ArrayList<>();
    for (CertificateTypeField field : fields) {
      CertificateTypeFieldDTO fieldDTO = new CertificateTypeFieldDTO();
      fieldDTO.setId(field.getId());
      fieldDTO.setFieldId(field.getFieldId());
      fieldDTO.setFieldNameCn(field.getFieldNameCn());
      fieldDTO.setFieldNameEn(field.getFieldNameEn());
      fieldDTO.setIsRequired(field.getIsRequired());
      fieldDTO.setSortOrder(field.getSortOrder());

      // 查询字段类型和数据类型
      FieldLibrary fieldLibrary = fieldLibraryMapper.selectById(field.getFieldId());
      if (fieldLibrary != null) {
        fieldDTO.setFieldType(fieldLibrary.getFieldType());
        fieldDTO.setFieldDataType(fieldLibrary.getFieldDataType());
        fieldDTO.setFieldLength(fieldLibrary.getFieldLength());
      }

      fieldDTOs.add(fieldDTO);
    }

    dto.setFields(fieldDTOs);
    return dto;
  }

  /**
   * 创建证件类型字段配置
   *
   * @param certTypeId     证件类型ID
   * @param certTypeCode   证件类型代码
   * @param fields         字段配置列表
   */
  private void createCertificateTypeFields(Long certTypeId, String certTypeCode,
      List<CertificateTypeFieldDTO> fields) {
    for (CertificateTypeFieldDTO fieldDTO : fields) {
      // 如果是自定义字段，先创建字段库记录
      if ("CUSTOM".equals(fieldDTO.getFieldType()) && fieldDTO.getFieldId() == null) {
        // 创建自定义字段逻辑（这里简化处理）
        // 实际应该在创建证件类型前先创建自定义字段
      }

      CertificateTypeField field = new CertificateTypeField();
      field.setCertTypeCode(certTypeCode);
      field.setFieldId(fieldDTO.getFieldId());
      field.setFieldNameEn(fieldDTO.getFieldNameEn());
      field.setFieldNameCn(fieldDTO.getFieldNameCn());
      field.setIsRequired(fieldDTO.getIsRequired());
      field.setSortOrder(fieldDTO.getSortOrder());
      field.setIsDeleted(0);

      certificateTypeFieldMapper.insert(field);
    }
  }
}
