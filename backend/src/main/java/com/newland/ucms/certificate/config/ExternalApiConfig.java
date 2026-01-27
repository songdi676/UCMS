package com.newland.ucms.certificate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 外部API配置类
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@Data
@Component
@ConfigurationProperties(prefix = "external-api")
public class ExternalApiConfig {

  /**
   * 办理通路API地址
   */
  private String businessChannelUrl;

  /**
   * 渠道小类API地址
   */
  private String channelSubTypeUrl;

  /**
   * 地市API地址
   */
  private String cityUrl;

  /**
   * 区县API地址
   */
  private String districtUrl;

  /**
   * 机构API地址
   */
  private String institutionUrl;

  /**
   * 缓存过期时间（分钟）
   */
  private Long cacheTtlMinutes;
}
