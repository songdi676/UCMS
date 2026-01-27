package com.newland.ucms.certificate.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.newland.ucms.certificate.dto.ScopeConfigDTO;
import com.newland.ucms.certificate.entity.ScopeConfig;

import java.util.Map;

/**
 * 应用范围配置服务接口
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
public interface ScopeConfigService extends IService<ScopeConfig> {

  /**
   * 分页查询应用范围配置列表
   *
   * @param page      页码
   * @param pageSize  每页大小
   * @param params    查询参数
   * @return 分页结果
   */
  IPage<ScopeConfigDTO> getScopeConfigList(int page, int pageSize, Map<String, Object> params);

  /**
   * 创建应用范围配置
   *
   * @param scopeConfigDTO 应用范围配置信息
   * @return 创建后的配置信息
   */
  ScopeConfigDTO createScopeConfig(ScopeConfigDTO scopeConfigDTO);

  /**
   * 更新应用范围配置
   *
   * @param id                  配置ID
   * @param scopeConfigDTO 应用范围配置信息
   * @return 更新后的配置信息
   */
  ScopeConfigDTO updateScopeConfig(Long id, ScopeConfigDTO scopeConfigDTO);

  /**
   * 删除应用范围配置
   *
   * @param id 配置ID
   */
  void deleteScopeConfig(Long id);
}
