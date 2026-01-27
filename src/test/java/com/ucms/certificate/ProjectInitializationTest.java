package com.ucms.certificate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 项目初始化测试类
 * 用于验证Spring Boot应用启动成功，数据库连接成功
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@SpringBootTest(classes = com.newland.ucms.certificate.CertificateApplication.class)
class ProjectInitializationTest {

  @Test
  void contextLoads() {
    // 测试Spring上下文加载成功
    assertThat(true).isTrue();
  }

}
