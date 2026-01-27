package com.ucms.certificate.service;

import com.newland.ucms.certificate.dto.CertificateRequest;
import com.newland.ucms.certificate.dto.CertificateValidationResponse;
import com.newland.ucms.certificate.entity.CertificateType;
import com.newland.ucms.certificate.entity.CertificateTypeField;
import com.newland.ucms.certificate.entity.ScopeConfig;
import com.newland.ucms.certificate.entity.ValidationRuleConfig;
import com.newland.ucms.certificate.entity.ValidationRuleItem;
import com.newland.ucms.certificate.enums.StatusEnum;
import com.newland.ucms.certificate.mapper.CertificateTypeFieldMapper;
import com.newland.ucms.certificate.mapper.CertificateTypeMapper;
import com.newland.ucms.certificate.mapper.ScopeConfigMapper;
import com.newland.ucms.certificate.mapper.ValidationRuleConfigMapper;
import com.newland.ucms.certificate.mapper.ValidationRuleItemMapper;
import com.newland.ucms.certificate.service.impl.ValidationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * 统一验证服务测试类
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@ExtendWith(MockitoExtension.class)
class ValidationServiceTest {

  @Mock
  private ScopeConfigMapper scopeConfigMapper;

  @Mock
  private CertificateTypeMapper certificateTypeMapper;

  @Mock
  private CertificateTypeFieldMapper certificateTypeFieldMapper;

  @Mock
  private ValidationRuleConfigMapper validationRuleConfigMapper;

  @Mock
  private ValidationRuleItemMapper validationRuleItemMapper;

  @Mock
  private com.newland.ucms.certificate.mapper.FieldLibraryMapper fieldLibraryMapper;

  @InjectMocks
  private ValidationServiceImpl validationService;

  @Test
  void testValidateCertificate_Success() {
    // 准备测试数据
    CertificateRequest request = new CertificateRequest();
    request.setBusinessCode("TEST001");
    request.setChannelSubType("云厅");
    request.setCertTypeCode("1");
    Map<String, Object> fieldValues = new HashMap<>();
    fieldValues.put("name", "张三");
    fieldValues.put("age", "25");
    request.setFieldValues(fieldValues);

    // Mock ScopeConfig
    ScopeConfig scopeConfig = new ScopeConfig();
    scopeConfig.setId(1L);
    scopeConfig.setAllowedCertTypes("1,2,3");
    scopeConfig.setStatus(StatusEnum.ENABLED.getCode());
    scopeConfig.setIsDeleted(0);
    scopeConfig.setCreatedAt(LocalDateTime.now());

    when(scopeConfigMapper.selectList(any())).thenReturn(Arrays.asList(scopeConfig));

    // Mock CertificateType
    CertificateType certificateType = new CertificateType();
    certificateType.setId(1L);
    certificateType.setCertTypeCode("1");
    certificateType.setCertTypeName("身份证");
    certificateType.setStatus(StatusEnum.ENABLED.getCode());
    certificateType.setIsDeleted(0);
    certificateType.setCreatedAt(LocalDateTime.now());

    when(certificateTypeMapper.selectOne(any())).thenReturn(certificateType);

    // Mock CertificateTypeField
    CertificateTypeField field1 = new CertificateTypeField();
    field1.setId(1L);
    field1.setCertTypeCode("1");
    field1.setFieldNameEn("name");
    field1.setFieldNameCn("姓名");
    field1.setIsRequired(1);
    field1.setSortOrder(1);
    field1.setIsDeleted(0);
    field1.setCreatedAt(LocalDateTime.now());

    CertificateTypeField field2 = new CertificateTypeField();
    field2.setId(2L);
    field2.setCertTypeCode("1");
    field2.setFieldNameEn("age");
    field2.setFieldNameCn("年龄");
    field2.setIsRequired(0);
    field2.setSortOrder(2);
    field2.setIsDeleted(0);
    field2.setCreatedAt(LocalDateTime.now());

    when(certificateTypeFieldMapper.selectList(any())).thenReturn(Arrays.asList(field1, field2));

    // Mock ValidationRuleConfig（返回null，表示没有规则配置）
    when(validationRuleConfigMapper.selectOne(any())).thenReturn(null);

    // Mock FieldLibrary
    com.newland.ucms.certificate.entity.FieldLibrary fieldLibrary = new com.newland.ucms.certificate.entity.FieldLibrary();
    fieldLibrary.setId(1L);
    fieldLibrary.setFieldNameCn("姓名");
    fieldLibrary.setFieldNameEn("name");
    fieldLibrary.setFieldDataType("text");
    fieldLibrary.setStatus(StatusEnum.ENABLED.getCode());
    fieldLibrary.setIsDeleted(0);
    fieldLibrary.setCreatedAt(LocalDateTime.now());

    // 设置fieldId
    field1.setFieldId(1L);

    when(fieldLibraryMapper.selectById(1L)).thenReturn(fieldLibrary);

    // 执行测试
    CertificateValidationResponse response = validationService.validateCertificate(request);

    // 验证结果
    assertThat(response).isNotNull();
    assertThat(response.getSuccess()).isTrue();
    assertThat(response.getErrors()).isEmpty();
  }

  @Test
  void testValidateCertificate_RequiredFieldEmpty() {
    // 准备测试数据
    CertificateRequest request = new CertificateRequest();
    request.setBusinessCode("TEST001");
    request.setChannelSubType("云厅");
    request.setCertTypeCode("1");
    Map<String, Object> fieldValues = new HashMap<>();
    // 不设置必填字段name
    fieldValues.put("age", "25");
    request.setFieldValues(fieldValues);

    // Mock ScopeConfig
    ScopeConfig scopeConfig = new ScopeConfig();
    scopeConfig.setId(1L);
    scopeConfig.setAllowedCertTypes("1,2,3");
    scopeConfig.setStatus(StatusEnum.ENABLED.getCode());
    scopeConfig.setIsDeleted(0);
    scopeConfig.setCreatedAt(LocalDateTime.now());

    when(scopeConfigMapper.selectList(any())).thenReturn(Arrays.asList(scopeConfig));

    // Mock CertificateType
    CertificateType certificateType = new CertificateType();
    certificateType.setId(1L);
    certificateType.setCertTypeCode("1");
    certificateType.setCertTypeName("身份证");
    certificateType.setStatus(StatusEnum.ENABLED.getCode());
    certificateType.setIsDeleted(0);
    certificateType.setCreatedAt(LocalDateTime.now());

    when(certificateTypeMapper.selectOne(any())).thenReturn(certificateType);

    // Mock CertificateTypeField
    CertificateTypeField field1 = new CertificateTypeField();
    field1.setId(1L);
    field1.setCertTypeCode("1");
    field1.setFieldNameEn("name");
    field1.setFieldNameCn("姓名");
    field1.setIsRequired(1);
    field1.setSortOrder(1);
    field1.setIsDeleted(0);
    field1.setCreatedAt(LocalDateTime.now());

    when(certificateTypeFieldMapper.selectList(any())).thenReturn(Arrays.asList(field1));

    // Mock FieldLibrary
    com.newland.ucms.certificate.entity.FieldLibrary fieldLibrary = new com.newland.ucms.certificate.entity.FieldLibrary();
    fieldLibrary.setId(1L);
    fieldLibrary.setFieldNameCn("姓名");
    fieldLibrary.setFieldNameEn("name");
    fieldLibrary.setFieldDataType("text");
    fieldLibrary.setStatus(StatusEnum.ENABLED.getCode());
    fieldLibrary.setIsDeleted(0);
    fieldLibrary.setCreatedAt(LocalDateTime.now());

    // Mock ValidationRuleConfig（返回null，表示没有规则配置）
    when(validationRuleConfigMapper.selectOne(any())).thenReturn(null);

    // 执行测试
    CertificateValidationResponse response = validationService.validateCertificate(request);

    // 验证结果
    assertThat(response).isNotNull();
    assertThat(response.getSuccess()).isFalse();
    assertThat(response.getErrors()).isNotEmpty();
    assertThat(response.getErrors()).containsKey("name");
    assertThat(response.getErrors().get("name")).contains("不能为空");
  }

  @Test
  void testValidateCertificate_TypeValidationRule() {
    // 准备测试数据
    CertificateRequest request = new CertificateRequest();
    request.setBusinessCode("TEST001");
    request.setChannelSubType("云厅");
    request.setCertTypeCode("1");
    Map<String, Object> fieldValues = new HashMap<>();
    fieldValues.put("name", "张三");
    fieldValues.put("age", "15"); // 年龄小于18，应该触发验证规则
    request.setFieldValues(fieldValues);

    // Mock ScopeConfig
    ScopeConfig scopeConfig = new ScopeConfig();
    scopeConfig.setId(1L);
    scopeConfig.setAllowedCertTypes("1,2,3");
    scopeConfig.setStatus(StatusEnum.ENABLED.getCode());
    scopeConfig.setIsDeleted(0);
    scopeConfig.setCreatedAt(LocalDateTime.now());

    when(scopeConfigMapper.selectList(any())).thenReturn(Arrays.asList(scopeConfig));

    // Mock CertificateType
    CertificateType certificateType = new CertificateType();
    certificateType.setId(1L);
    certificateType.setCertTypeCode("1");
    certificateType.setCertTypeName("身份证");
    certificateType.setStatus(StatusEnum.ENABLED.getCode());
    certificateType.setIsDeleted(0);
    certificateType.setCreatedAt(LocalDateTime.now());

    when(certificateTypeMapper.selectOne(any())).thenReturn(certificateType);

    // Mock CertificateTypeField
    CertificateTypeField field1 = new CertificateTypeField();
    field1.setId(1L);
    field1.setCertTypeCode("1");
    field1.setFieldNameEn("name");
    field1.setFieldNameCn("姓名");
    field1.setIsRequired(1);
    field1.setSortOrder(1);
    field1.setIsDeleted(0);
    field1.setCreatedAt(LocalDateTime.now());

    CertificateTypeField field2 = new CertificateTypeField();
    field2.setId(2L);
    field2.setCertTypeCode("1");
    field2.setFieldNameEn("age");
    field2.setFieldNameCn("年龄");
    field2.setIsRequired(0);
    field2.setSortOrder(2);
    field2.setIsDeleted(0);
    field2.setCreatedAt(LocalDateTime.now());

    when(certificateTypeFieldMapper.selectList(any())).thenReturn(Arrays.asList(field1, field2));

    // Mock ValidationRuleConfig
    ValidationRuleConfig ruleConfig = new ValidationRuleConfig();
    ruleConfig.setId(1L);
    ruleConfig.setBusinessCode("TEST001");
    ruleConfig.setStatus(StatusEnum.ENABLED.getCode());
    ruleConfig.setIsDeleted(0);
    ruleConfig.setCreatedAt(LocalDateTime.now());

    when(validationRuleConfigMapper.selectOne(any())).thenReturn(ruleConfig);

    // Mock ValidationRuleItem
    ValidationRuleItem ruleItem = new ValidationRuleItem();
    ruleItem.setId(1L);
    ruleItem.setRuleId(1L);
    ruleItem.setFieldName("age");
    ruleItem.setValidationLogic(">=");
    ruleItem.setValidationValue("18");
    ruleItem.setSortOrder(1);
    ruleItem.setIsDeleted(0);
    ruleItem.setCreatedAt(LocalDateTime.now());

    when(validationRuleItemMapper.selectList(any())).thenReturn(Arrays.asList(ruleItem));

    // Mock FieldLibrary
    com.newland.ucms.certificate.entity.FieldLibrary fieldLibrary1 = new com.newland.ucms.certificate.entity.FieldLibrary();
    fieldLibrary1.setId(1L);
    fieldLibrary1.setFieldNameCn("姓名");
    fieldLibrary1.setFieldNameEn("name");
    fieldLibrary1.setFieldDataType("text");
    fieldLibrary1.setStatus(StatusEnum.ENABLED.getCode());
    fieldLibrary1.setIsDeleted(0);
    fieldLibrary1.setCreatedAt(LocalDateTime.now());

    com.newland.ucms.certificate.entity.FieldLibrary fieldLibrary2 = new com.newland.ucms.certificate.entity.FieldLibrary();
    fieldLibrary2.setId(2L);
    fieldLibrary2.setFieldNameCn("年龄");
    fieldLibrary2.setFieldNameEn("age");
    fieldLibrary2.setFieldDataType("number");
    fieldLibrary2.setStatus(StatusEnum.ENABLED.getCode());
    fieldLibrary2.setIsDeleted(0);
    fieldLibrary2.setCreatedAt(LocalDateTime.now());

    // 设置fieldId
    field1.setFieldId(1L);
    field2.setFieldId(2L);

    when(fieldLibraryMapper.selectById(1L)).thenReturn(fieldLibrary1);
    when(fieldLibraryMapper.selectById(2L)).thenReturn(fieldLibrary2);

    // 执行测试
    CertificateValidationResponse response = validationService.validateCertificate(request);

    // 验证结果
    assertThat(response).isNotNull();
    assertThat(response.getSuccess()).isFalse();
    assertThat(response.getErrors()).isNotEmpty();
    assertThat(response.getErrors()).containsKey("age");
  }

  @Test
  void testValidateCertificate_NoScopeConfig() {
    // 准备测试数据
    CertificateRequest request = new CertificateRequest();
    request.setBusinessCode("TEST001");
    request.setChannelSubType("云厅");

    // Mock ScopeConfig（返回空列表）
    when(scopeConfigMapper.selectList(any())).thenReturn(Arrays.asList());

    // 执行测试
    CertificateValidationResponse response = validationService.validateCertificate(request);

    // 验证结果
    assertThat(response).isNotNull();
    assertThat(response.getSuccess()).isFalse();
    assertThat(response.getMessage()).contains("未找到匹配的应用范围配置");
  }

  @Test
  void testValidateCertificate_CertTypeNotAllowed() {
    // 准备测试数据
    CertificateRequest request = new CertificateRequest();
    request.setBusinessCode("TEST001");
    request.setChannelSubType("云厅");
    request.setCertTypeCode("99"); // 不在允许的证件类型中
    Map<String, Object> fieldValues = new HashMap<>();
    request.setFieldValues(fieldValues);

    // Mock ScopeConfig
    ScopeConfig scopeConfig = new ScopeConfig();
    scopeConfig.setId(1L);
    scopeConfig.setAllowedCertTypes("1,2,3");
    scopeConfig.setStatus(StatusEnum.ENABLED.getCode());
    scopeConfig.setIsDeleted(0);
    scopeConfig.setCreatedAt(LocalDateTime.now());

    when(scopeConfigMapper.selectList(any())).thenReturn(Arrays.asList(scopeConfig));

    // 执行测试
    CertificateValidationResponse response = validationService.validateCertificate(request);

    // 验证结果
    assertThat(response).isNotNull();
    assertThat(response.getSuccess()).isFalse();
    assertThat(response.getMessage()).contains("证件类型不在允许的范围内");
  }

  @Test
  void testValidateCertificate_CertTypeNotExists() {
    // 准备测试数据
    CertificateRequest request = new CertificateRequest();
    request.setBusinessCode("TEST001");
    request.setChannelSubType("云厅");
    request.setCertTypeCode("1");
    Map<String, Object> fieldValues = new HashMap<>();
    request.setFieldValues(fieldValues);

    // Mock ScopeConfig
    ScopeConfig scopeConfig = new ScopeConfig();
    scopeConfig.setId(1L);
    scopeConfig.setAllowedCertTypes("1,2,3");
    scopeConfig.setStatus(StatusEnum.ENABLED.getCode());
    scopeConfig.setIsDeleted(0);
    scopeConfig.setCreatedAt(LocalDateTime.now());

    when(scopeConfigMapper.selectList(any())).thenReturn(Arrays.asList(scopeConfig));

    // Mock CertificateType（返回null）
    when(certificateTypeMapper.selectOne(any())).thenReturn(null);

    // 执行测试
    CertificateValidationResponse response = validationService.validateCertificate(request);

    // 验证结果
    assertThat(response).isNotNull();
    assertThat(response.getSuccess()).isFalse();
    assertThat(response.getMessage()).contains("证件类型配置不存在");
  }
}
