package com.newland.ucms.certificate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newland.ucms.certificate.dto.PredefinedRuleDTO;
import com.newland.ucms.certificate.entity.PredefinedRule;
import com.newland.ucms.certificate.enums.StatusEnum;
import com.newland.ucms.certificate.mapper.PredefinedRuleMapper;
import com.newland.ucms.certificate.service.PredefinedRuleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 预置规则服务实现类
 *
 * @author UCMS Team
 * @since 2026-01-29
 */
@Service
public class PredefinedRuleServiceImpl extends ServiceImpl<PredefinedRuleMapper, PredefinedRule>
    implements PredefinedRuleService {

  @Override
  public IPage<PredefinedRuleDTO> getPredefinedRuleList(int page, int pageSize, Map<String, Object> params) {
    Page<PredefinedRule> pageParam = new Page<>(page, pageSize);

    LambdaQueryWrapper<PredefinedRule> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(PredefinedRule::getIsDeleted, 0);

    // 证件类型代码查询
    if (params.containsKey("certTypeCode") && StringUtils.hasText(params.get("certTypeCode").toString())) {
      queryWrapper.like(PredefinedRule::getCertTypeCode, params.get("certTypeCode").toString());
    }

    // 规则名称查询
    if (params.containsKey("ruleName") && StringUtils.hasText(params.get("ruleName").toString())) {
      queryWrapper.like(PredefinedRule::getRuleName, params.get("ruleName").toString());
    }

    // 状态查询
    if (params.containsKey("status") && params.get("status") != null) {
      queryWrapper.eq(PredefinedRule::getStatus, Integer.parseInt(params.get("status").toString()));
    }

    // 按创建时间倒序
    queryWrapper.orderByDesc(PredefinedRule::getCreatedAt);

    IPage<PredefinedRule> pageResult = baseMapper.selectPage(pageParam, queryWrapper);

    // 转换为DTO
    Page<PredefinedRuleDTO> dtoPage = new Page<>(page, pageSize);
    dtoPage.setTotal(pageResult.getTotal());
    dtoPage.setRecords(pageResult.getRecords().stream()
        .map(this::toDTO)
        .collect(java.util.stream.Collectors.toList()));

    return dtoPage;
  }

  @Override
  public PredefinedRuleDTO getPredefinedRuleDetail(Long id) {
    if (id == null) {
      throw new IllegalArgumentException("规则ID不能为空");
    }

    PredefinedRule predefinedRule = baseMapper.selectById(id);
    if (predefinedRule == null || predefinedRule.getIsDeleted() == 1) {
      throw new IllegalArgumentException("预置规则不存在");
    }

    return toDTO(predefinedRule);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public PredefinedRuleDTO createPredefinedRule(PredefinedRuleDTO ruleDTO) {
    // 验证必填字段
    if (!StringUtils.hasText(ruleDTO.getCertTypeCode())) {
      throw new IllegalArgumentException("证件类型代码不能为空");
    }
    if (ruleDTO.getFieldId() == null) {
      throw new IllegalArgumentException("字段ID不能为空");
    }
    if (!StringUtils.hasText(ruleDTO.getRuleName())) {
      throw new IllegalArgumentException("规则名称不能为空");
    }
    if (!StringUtils.hasText(ruleDTO.getValidationLogic())) {
      throw new IllegalArgumentException("校验逻辑不能为空");
    }

    // 创建预置规则
    PredefinedRule predefinedRule = new PredefinedRule();
    BeanUtils.copyProperties(ruleDTO, predefinedRule, "ruleId");

    predefinedRule.setStatus(StatusEnum.ENABLED.getCode());
    predefinedRule.setIsDeleted(0);
    predefinedRule.setCreatedAt(LocalDateTime.now());
    predefinedRule.setUpdatedAt(LocalDateTime.now());

    baseMapper.insert(predefinedRule);

    return toDTO(predefinedRule);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public PredefinedRuleDTO updatePredefinedRule(Long id, PredefinedRuleDTO ruleDTO) {
    PredefinedRule predefinedRule = baseMapper.selectById(id);
    if (predefinedRule == null || predefinedRule.getIsDeleted() == 1) {
      throw new IllegalArgumentException("预置规则不存在");
    }

    // 验证必填字段
    if (!StringUtils.hasText(ruleDTO.getCertTypeCode())) {
      throw new IllegalArgumentException("证件类型代码不能为空");
    }
    if (ruleDTO.getFieldId() == null) {
      throw new IllegalArgumentException("字段ID不能为空");
    }
    if (!StringUtils.hasText(ruleDTO.getRuleName())) {
      throw new IllegalArgumentException("规则名称不能为空");
    }
    if (!StringUtils.hasText(ruleDTO.getValidationLogic())) {
      throw new IllegalArgumentException("校验逻辑不能为空");
    }

    // 更新预置规则
    BeanUtils.copyProperties(ruleDTO, predefinedRule, "ruleId");
    predefinedRule.setUpdatedAt(LocalDateTime.now());

    baseMapper.updateById(predefinedRule);

    return toDTO(predefinedRule);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deletePredefinedRule(Long id) {
    PredefinedRule predefinedRule = baseMapper.selectById(id);
    if (predefinedRule == null || predefinedRule.getIsDeleted() == 1) {
      throw new IllegalArgumentException("预置规则不存在");
    }

    // 逻辑删除
    predefinedRule.setIsDeleted(1);
    predefinedRule.setUpdatedAt(LocalDateTime.now());
    baseMapper.updateById(predefinedRule);
  }

  /**
   * 实体转DTO
   *
   * @param predefinedRule 实体
   * @return DTO
   */
  private PredefinedRuleDTO toDTO(PredefinedRule predefinedRule) {
    if (predefinedRule == null) {
      return null;
    }

    PredefinedRuleDTO dto = new PredefinedRuleDTO();
    BeanUtils.copyProperties(predefinedRule, dto);
    dto.setRuleId(predefinedRule.getId());

    return dto;
  }
}
