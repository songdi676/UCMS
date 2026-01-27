package com.newland.ucms.certificate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newland.ucms.certificate.entity.ScopeConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 应用范围配置Mapper接口
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@Mapper
public interface ScopeConfigMapper extends BaseMapper<ScopeConfig> {

  /**
   * 根据应用系统查询应用范围配置
   *
   * @param businessChannel 应用系统
   * @return 应用范围配置列表
   */
  ScopeConfig findByBusinessChannel(@Param("businessChannel") String businessChannel);

  /**
   * 根据渠道小类查询应用范围配置
   *
   * @param channelSubType 渠道小类
   * @return 应用范围配置列表
   */
  ScopeConfig findByChannelSubType(@Param("channelSubType") String channelSubType);

  /**
   * 根据地市查询应用范围配置
   *
   * @param city 地市
   @return 应用范围配置列表
   */
  ScopeConfig findByCity(@Param("city") String city);

  /**
   * 根据区县查询应用范围配置
   *
   * @param district 区县
   * @return 应用范围配置列表
   */
  ScopeConfig findByDistrict(@Param("district") String district);

  /**
   * 根据机构查询应用范围配置
   *
   * @param institution 机构
   * @return 应用范围配置列表
   */
  ScopeConfig findByInstitution(@Param("institution") String institution);

  /**
   * 查询匹配的应用范围配置
   * 根据业务通路、渠道小类、地市、区县、机构信息，按照优先级规则（最末级优先）返回匹配的配置
   *
   * @param businessChannel 应用系统（可为空）
   * @param channelSubType 渠道小类（可为空）
   * @param city 地市（可为空）
   * @param district 区县（可为空）
   * @param institution 机构（可为空）
   * @return 匹配的应用范围配置
   */
  ScopeConfig findMatchingScopeConfig(@Param("businessChannel") String businessChannel,
                                  @Param("channelSubType") String channelSubType,
                                  @Param("city") String city,
                                  @Param("district") String district,
                                  @Param("institution") String institution);
}
