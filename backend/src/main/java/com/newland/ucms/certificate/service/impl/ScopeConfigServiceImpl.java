package com.newland.ucms.certificate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newland.ucms.certificate.dto.ScopeConfigDTO;
import com.newland.ucms.certificate.entity.ScopeConfig;
import com.newland.ucms.certificate.enums.StatusEnum;
import com.newland.ucms.certificate.mapper.ScopeConfigMapper;
import com.newland.ucms.certificate.service.ScopeConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 应用范围配置服务实现类
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@Service
public class ScopeConfigServiceImpl extends ServiceImpl<ScopeConfigMapper, ScopeConfig>
    implements ScopeConfigService {

  @Override
  public IPage<ScopeConfigDTO> getScopeConfigList(int page, int pageSize, Map<String, Object> params) {
    Page<ScopeConfig> pageParam = new Page<>(page, pageSize);

    LambdaQueryWrapper<ScopeConfig> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(ScopeConfig::getIsDeleted, 0);

    // 应用系统查询
    if (params.containsKey("businessChannel") && StringUtils.hasText(params.get("businessChannel").toString())) {
      queryWrapper.like(ScopeConfig::getBusinessChannel, params.get("businessChannel").toString());
    }

    // 状态查询
    if (params.containsKey("status") && params.get("status") != null) {
      queryWrapper.eq(ScopeConfig::getStatus, Integer.parseInt(params.get("status").toString()));
    }

    // 按创建时间倒序
    queryWrapper.orderByDesc(ScopeConfig::getCreatedAt);

    IPage<ScopeConfig> pageResult = baseMapper.selectPage(pageParam, queryWrapper);

    // 转换为DTO
    Page<ScopeConfigDTO> dtoPage = new Page<>(page, pageSize);
    dtoPage.setTotal(pageResult.getTotal());
    dtoPage.setRecords(pageResult.getRecords().stream()
        .map(this::toDTO)
        .collect(java.util.stream.Collectors.toList()));

    return dtoPage;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ScopeConfigDTO createScopeConfig(ScopeConfigDTO scopeConfigDTO) {
    // 验证必填字段
    if (scopeConfigDTO.getAllowedCertTypes() == null || scopeConfigDTO.getAllowedCertTypes().isEmpty()) {
      throw new IllegalArgumentException("请至少选择一个证件类型");
    }

    // 创建应用范围配置
    ScopeConfig scopeConfig = new ScopeConfig();
    BeanUtils.copyProperties(scopeConfigDTO, scopeConfig, "id");
    scopeConfig.setStatus(StatusEnum.ENABLED.getCode());
    scopeConfig.setIsDeleted(0);

    baseMapper.insert(scopeConfig);

    return toDTO(scopeConfig);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ScopeConfigDTO updateScopeConfig(Long id, ScopeConfigDTO scopeConfigDTO) {
    ScopeConfig scopeConfig = baseMapper.selectById(id);
    if (scopeConfig == null || scopeConfig.getIsDeleted() == 1) {
      throw new IllegalArgumentException("应用范围配置不存在");
    }

    // 更新应用范围配置
    BeanUtils.copyProperties(scopeConfigDTO, scopeConfig, "id");
    baseMapper.updateById(scopeConfig);

    return toDTO(scopeConfig);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteScopeConfig(Long id) {
    ScopeConfig scopeConfig = baseMapper.selectById(id);
    if (scopeConfig == null || scopeConfig.getIsDeleted() == 1) {
      throw new IllegalArgumentException("应用范围配置不存在");
    }

    // 逻辑删除
    scopeConfig.setIsDeleted(1);
    baseMapper.updateById(scopeConfig);
  }

  /**
   * 实体转DTO
   *
   * @param scopeConfig 实体
   * @return DTO
   */
  private ScopeConfigDTO toDTO(ScopeConfig scopeConfig) {
    if (scopeConfig == null) {
      return null;
    }

    ScopeConfigDTO dto = new ScopeConfigDTO();
    BeanUtils.copyProperties(scopeConfig, dto);
    dto.setId(scopeConfig.getId());
    return dto;
  }
}
