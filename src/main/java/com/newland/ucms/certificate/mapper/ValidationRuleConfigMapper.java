package com.newland.ucms.certificate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newland.ucms.certificate.entity.ValidationRuleConfig;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 校验规则配置Mapper接口
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
public interface ValidationRuleConfigMapper extends BaseMapper<ValidationRuleConfig> {

  /**
   * 根据业务代码查询校验规则配置
   *
   * @param businessCode 业务代码
   * @return 校验规则配置
   */
  ValidationRuleConfig findByBusinessCode(@Param("businessCode") String businessCode);

  /**
   * 根据渠道小类查询校验规则配置
   *
   * @param channelSubType 渠道小类
   * @return 校验规则配置
   */
  ValidationRuleConfig findByChannelSubType(@Param("channelSubType") String channelSubType);

  /**
   * 根据业务代码和渠道小类查询校验规则配置
   *
   * @param businessCode 业务代码
   @param channelSubType 渠道小类
   * @return 校验规则配置
   */
  ValidationRuleConfig findByBusinessCodeAndChannelSubType(@Param("businessCode") String businessCode, @Param("channelSubType") String channelSubType);
}
