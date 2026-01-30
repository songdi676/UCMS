package com.newland.ucms.certificate;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 统一证件验证中台系统主应用类
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@SpringBootApplication
@EnableCaching
@MapperScan("com.newland.ucms.certificate.mapper")
public class CertificateApplication {

  public static void main(String[] args) {
    SpringApplication.run(CertificateApplication.class, args);
  }

}
