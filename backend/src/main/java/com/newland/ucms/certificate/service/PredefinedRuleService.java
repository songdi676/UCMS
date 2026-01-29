package com.newland.ucms.certificate.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.newland.ucms.certificate.dto.PredefinedRuleDTO;
import com.newland.ucms.certificate.entity.PredefinedRule;

import java.util.Map;

/**
 * 预置规则服务接口
 *
 * @author UCMS Team
 * @since 2026-01-29
 */
public interface PredefinedRuleService extends IService<PredefinedRule> {

  /**
   * 分页查询预置规则列表
   *
   * @param page      页码
   * @param pageSize  每页大小
   * @param params    查询参数
   * @return 分页结果
   */
  IPage<PredefinedRuleDTO> getPredefinedRuleList(int page, int pageSize, Map<String, Object> params);

  /**
   * 获取预置规则详情
   *
   * @param id 规则ID
   * @return 预置规则详情
   */
  PredefinedRuleDTO getPredefinedRuleDetail(Long id);

  /**
   * 创建预置规则
   *
   * @param ruleDTO 预置规则信息
   * @return 创建后的规则信息
   */
  PredefinedRuleDTO createPredefinedRule(PredefinedRuleDTO ruleDTO);

  /**
   * 更新预置规则
   *
   * @param id      规则ID
   * @param ruleDTO 预置规则信息
   * @return 更新后的规则信息
   */
  PredefinedRuleDTO updatePredefinedRule(Long id, PredefinedRuleDTO ruleDTO);

  /**
   * 删除预置规则
   *
   * @param id 规则ID
   */
  void deletePredefinedRule(Long id);
}
