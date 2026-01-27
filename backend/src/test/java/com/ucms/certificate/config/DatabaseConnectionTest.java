package com.ucms.certificate.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.newland.ucms.certificate.entity.CertificateType;
import com.newland.ucms.certificate.entity.FieldLibrary;
import com.newland.ucms.certificate.entity.CertificateTypeField;
import com.newland.ucms.certificate.entity.ValidationRuleConfig;
import com.newland.ucms.certificate.entity.ValidationRuleItem;
import com.newland.ucms.certificate.entity.ScopeConfig;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 数据库连接测试类
 * 用于验证数据库连接成功，可以执行查询
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@SpringBootTest(classes = com.newland.ucms.certificate.CertificateApplication.class)
class DatabaseConnectionTest {

  @Autowired
  private com.baomidou.mybatisplus.core.mapper.BaseMapper<CertificateType> certificateTypeMapper;

  @Autowired
  private com.baomidou.mybatisplus.core.mapper.BaseMapper<FieldLibrary> fieldLibraryMapper;

  @Autowired
  private com.baomidou.mybatisplus.core.mapper.BaseMapper<CertificateTypeField> certificateTypeFieldMapper;

  @Autowired
  private com.baomidou.mybatisplus.core.mapper.BaseMapper<ValidationRuleConfig> validationRuleConfigMapper;

  @Autowired
  private com.baomidou.mybatisplus.core.mapper.BaseMapper<ValidationRuleItem> validationRuleItemMapper;

  @Autowired
  private com.baomidou.mybatisplus.core.mapper.BaseMapper<ScopeConfig> scopeConfigMapper;

  @Test
  void contextLoads() {
    // 测试Spring上下文加载成功
    assertThat(true).isTrue();
  }

  @Test
  void testDatabaseConnection() {
    // 测试数据库连接成功，可以执行查询
    QueryWrapper<CertificateType> queryWrapper = new QueryWrapper<>();
    queryWrapper.select("id", "cert_type_code", "cert_type_name", "status");
    queryWrapper.eq("status", 1);

    CertificateType certificateType = certificateTypeMapper.selectOne(queryWrapper);

    assertThat(certificateType).isNotNull();
    assertThat(certificateType.getCertTypeCode()).isNotNull();
  }

  @Test
  void testFieldLibraryQuery() {
    // 测试字段库查询成功
    long count = fieldLibraryMapper.selectCount(null);

    assertThat(count).isGreaterThan(0);
  }

  @Test
  void testCertificateTypeFieldQuery() {
    // 测试证件类型字段配置查询成功
    QueryWrapper<CertificateTypeField> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("cert_type_code", "1");
    queryWrapper.eq("is_deleted", 0);
    
    long count = certificateTypeFieldMapper.selectCount(queryWrapper);
    
    assertThat(count).isGreaterThan(0);
  }

  @Test
  void testValidationRuleConfigQuery() {
    // 测试校验规则配置查询成功
    QueryWrapper<ValidationRuleConfig> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("status", 1);
    queryWrapper.eq("is_deleted", 0);
    
    long count = validationRuleConfigMapper.selectCount(queryWrapper);
    
    assertThat(count).isGreaterThanOrEqualTo(0);
  }

  @Test
  void testValidationRuleItemQuery() {
    // 测试校验规则明细查询成功
    QueryWrapper<ValidationRuleItem> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("is_deleted", 0);
    
    long count = validationRuleItemMapper.selectCount(queryWrapper);
    
    assertThat(count).isGreaterThanOrEqualTo(0);
  }

  @Test
  void testScopeConfigQuery() {
    // 测试应用范围配置查询成功
    QueryWrapper<ScopeConfig> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("status", 1);
    queryWrapper.eq("is_deleted", 0);
    
    long count = scopeConfigMapper.selectCount(queryWrapper);
    
    assertThat(count).isGreaterThanOrEqualTo(0);
  }
}
