package com.newland.ucms.certificate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域配置
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@Configuration
public class CorsConfig {

  @Bean
  public CorsFilter corsFilter() {
    CorsConfiguration config = new CorsConfiguration();

    // 允许所有域名进行跨域调用
    config.addAllowedOriginPattern("*");

    // 允许所有请求头
    config.addAllowedHeader("*");

    // 允许所有请求方法（GET、POST、PUT、DELETE等）
    config.addAllowedMethod("*");

    // 允许携带凭证（cookies）
    config.setAllowCredentials(true);

    // 暴露响应头
    config.addExposedHeader("*");

    // 预检请求的有效期，单位秒
    config.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return new CorsFilter(source);
  }
}
