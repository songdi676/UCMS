package com.newland.ucms.certificate.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newland.ucms.certificate.dto.PredefinedRuleDTO;
import com.newland.ucms.certificate.entity.PredefinedRule;
import com.newland.ucms.certificate.mapper.PredefinedRuleMapper;
import com.newland.ucms.certificate.service.impl.PredefinedRuleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * 预置规则服务测试类
 *
 * @author UCMS Team
 * @since 2026-01-29
 */
@ExtendWith(MockitoExtension.class)
public class PredefinedRuleServiceTest {

  @Mock
  private PredefinedRuleMapper predefinedRuleMapper;

  @InjectMocks
  private PredefinedRuleServiceImpl predefinedRuleService;

  private PredefinedRule predefinedRule;
  private PredefinedRuleDTO predefinedRuleDTO;

  @BeforeEach
  void setUp() {
    // 初始化测试数据
    predefinedRule = new PredefinedRule();
    predefinedRule.setId(1L);
    predefinedRule.setCertTypeCode("ID_CARD");
    predefinedRule.setFieldId(1L);
    predefinedRule.setRuleName("身份证号校验");
    predefinedRule.setValidationLogic("^\\d{17}[0-9Xx]$");
    predefinedRule.setErrorMessage("身份证号格式不正确");
    predefinedRule.setStatus(1);
    predefinedRule.setIsDeleted(0);
    predefinedRule.setCreatedAt(LocalDateTime.now());
    predefinedRule.setUpdatedAt(LocalDateTime.now());

    predefinedRuleDTO = new PredefinedRuleDTO();
    predefinedRuleDTO.setRuleId(1L);
    predefinedRuleDTO.setCertTypeCode("ID_CARD");
    predefinedRuleDTO.setFieldId(1L);
    predefinedRuleDTO.setRuleName("身份证号校验");
    predefinedRuleDTO.setValidationLogic("^\\d{17}[0-9Xx]$");
    predefinedRuleDTO.setErrorMessage("身份证号格式不正确");
    predefinedRuleDTO.setStatus(1);
  }

  /**
   * 测试分页查询预置规则列表 - 正常流程
   */
  @Test
  void testGetPredefinedRuleList_Success() {
    // 准备测试数据
    List<PredefinedRule> ruleList = Arrays.asList(predefinedRule);
    com.baomidou.mybatisplus.extension.plugins.pagination.Page<PredefinedRule> pageResult =
        new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
    pageResult.setRecords(ruleList);
    pageResult.setTotal(1);

    // Mock行为
    when(predefinedRuleMapper.selectPage(any(), any(LambdaQueryWrapper.class))).thenReturn(pageResult);

    // 执行测试
    int page = 1;
    int pageSize = 10;
    Map<String, Object> params = new HashMap<>();
    IPage<PredefinedRuleDTO> result = predefinedRuleService.getPredefinedRuleList(page, pageSize, params);

    // 验证结果
    assertNotNull(result);
    assertEquals(1, result.getTotal());
    assertEquals(1, result.getRecords().size());
    assertEquals("ID_CARD", result.getRecords().get(0).getCertTypeCode());

    // 验证方法调用
    verify(predefinedRuleMapper, times(1)).selectPage(any(), any(LambdaQueryWrapper.class));
  }

  /**
   * 测试分页查询预置规则列表 - 带查询条件
   */
  @Test
  void testGetPredefinedRuleList_WithParams() {
    // 准备测试数据
    List<PredefinedRule> ruleList = Arrays.asList(predefinedRule);
    com.baomidou.mybatisplus.extension.plugins.pagination.Page<PredefinedRule> pageResult =
        new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
    pageResult.setRecords(ruleList);
    pageResult.setTotal(1);

    // Mock行为
    when(predefinedRuleMapper.selectPage(any(), any(LambdaQueryWrapper.class))).thenReturn(pageResult);

    // 执行测试
    int page = 1;
    int pageSize = 10;
    Map<String, Object> params = new HashMap<>();
    params.put("certTypeCode", "ID_CARD");
    params.put("ruleName", "身份证号");
    params.put("status", 1);

    IPage<PredefinedRuleDTO> result = predefinedRuleService.getPredefinedRuleList(page, pageSize, params);

    // 验证结果
    assertNotNull(result);
    assertEquals(1, result.getTotal());

    // 验证方法调用
    verify(predefinedRuleMapper, times(1)).selectPage(any(), any(LambdaQueryWrapper.class));
  }

  /**
   * 测试获取预置规则详情 - 正常流程
   */
  @Test
  void testGetPredefinedRuleDetail_Success() {
    // Mock行为
    when(predefinedRuleMapper.selectById(1L)).thenReturn(predefinedRule);

    // 执行测试
    PredefinedRuleDTO result = predefinedRuleService.getPredefinedRuleDetail(1L);

    // 验证结果
    assertNotNull(result);
    assertEquals(1L, result.getRuleId());
    assertEquals("ID_CARD", result.getCertTypeCode());
    assertEquals("身份证号校验", result.getRuleName());

    // 验证方法调用
    verify(predefinedRuleMapper, times(1)).selectById(1L);
  }

  /**
   * 测试获取预置规则详情 - ID为空
   */
  @Test
  void testGetPredefinedRuleDetail_NullId() {
    // 执行测试并验证异常
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> predefinedRuleService.getPredefinedRuleDetail(null)
    );

    assertEquals("规则ID不能为空", exception.getMessage());
  }

  /**
   * 测试获取预置规则详情 - 规则不存在
   */
  @Test
  void testGetPredefinedRuleDetail_NotFound() {
    // Mock行为
    when(predefinedRuleMapper.selectById(999L)).thenReturn(null);

    // 执行测试并验证异常
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> predefinedRuleService.getPredefinedRuleDetail(999L)
    );

    assertEquals("预置规则不存在", exception.getMessage());
  }

  /**
   * 测试获取预置规则详情 - 规则已删除
   */
  @Test
  void testGetPredefinedRuleDetail_Deleted() {
    // 准备测试数据 - 已删除
    PredefinedRule deletedRule = new PredefinedRule();
    deletedRule.setId(1L);
    deletedRule.setIsDeleted(1);

    // Mock行为
    when(predefinedRuleMapper.selectById(1L)).thenReturn(deletedRule);

    // 执行测试并验证异常
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> predefinedRuleService.getPredefinedRuleDetail(1L)
    );

    assertEquals("预置规则不存在", exception.getMessage());
  }

  /**
   * 测试创建预置规则 - 正常流程
   */
  @Test
  void testCreatePredefinedRule_Success() {
    // 准备测试数据
    PredefinedRuleDTO newRuleDTO = new PredefinedRuleDTO();
    newRuleDTO.setCertTypeCode("ID_CARD");
    newRuleDTO.setFieldId(1L);
    newRuleDTO.setRuleName("身份证号校验");
    newRuleDTO.setValidationLogic("^\\d{17}[0-9Xx]$");
    newRuleDTO.setErrorMessage("身份证号格式不正确");

    // Mock行为
    when(predefinedRuleMapper.insert(any(PredefinedRule.class))).thenAnswer(invocation -> {
      PredefinedRule rule = invocation.getArgument(0);
      rule.setId(1L);
      return 1;
    });

    // 执行测试
    PredefinedRuleDTO result = predefinedRuleService.createPredefinedRule(newRuleDTO);

    // 验证结果
    assertNotNull(result);
    assertEquals(1L, result.getRuleId());
    assertEquals("ID_CARD", result.getCertTypeCode());
    assertEquals("身份证号校验", result.getRuleName());

    // 验证方法调用
    verify(predefinedRuleMapper, times(1)).insert(any(PredefinedRule.class));
  }

  /**
   * 测试创建预置规则 - 证件类型代码为空
   */
  @Test
  void testCreatePredefinedRule_EmptyCertTypeCode() {
    // 准备测试数据
    PredefinedRuleDTO newRuleDTO = new PredefinedRuleDTO();
    newRuleDTO.setCertTypeCode("");
    newRuleDTO.setFieldId(1L);
    newRuleDTO.setRuleName("身份证号校验");
    newRuleDTO.setValidationLogic("^\\d{17}[0-9Xx]$");

    // 执行测试并验证异常
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> predefinedRuleService.createPredefinedRule(newRuleDTO)
    );

    assertEquals("证件类型代码不能为空", exception.getMessage());
  }

  /**
   * 测试创建预置规则 - 字段ID为空
   */
  @Test
  void testCreatePredefinedRule_NullFieldId() {
    // 准备测试数据
    PredefinedRuleDTO newRuleDTO = new PredefinedRuleDTO();
    newRuleDTO.setCertTypeCode("ID_CARD");
    newRuleDTO.setRuleName("身份证号校验");
    newRuleDTO.setValidationLogic("^\\d{17}[0-9Xx]$");

    // 执行测试并验证异常
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> predefinedRuleService.createPredefinedRule(newRuleDTO)
    );

    assertEquals("字段ID不能为空", exception.getMessage());
  }

  /**
   * 测试创建预置规则 - 规则名称为空
   */
  @Test
  void testCreatePredefinedRule_EmptyRuleName() {
    // 准备测试数据
    PredefinedRuleDTO newRuleDTO = new PredefinedRuleDTO();
    newRuleDTO.setCertTypeCode("ID_CARD");
    newRuleDTO.setFieldId(1L);
    newRuleDTO.setValidationLogic("^\\d{17}[0-9Xx]$");

    // 执行测试并验证异常
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> predefinedRuleService.createPredefinedRule(newRuleDTO)
    );

    assertEquals("规则名称不能为空", exception.getMessage());
  }

  /**
   * 测试创建预置规则 - 校验逻辑为空
   */
  @Test
  void testCreatePredefinedRule_EmptyValidationLogic() {
    // 准备测试数据
    PredefinedRuleDTO newRuleDTO = new PredefinedRuleDTO();
    newRuleDTO.setCertTypeCode("ID_CARD");
    newRuleDTO.setFieldId(1L);
    newRuleDTO.setRuleName("身份证号校验");

    // 执行测试并验证异常
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> predefinedRuleService.createPredefinedRule(newRuleDTO)
    );

    assertEquals("校验逻辑不能为空", exception.getMessage());
  }

  /**
   * 测试更新预置规则 - 正常流程
   */
  @Test
  void testUpdatePredefinedRule_Success() {
    // Mock行为
    when(predefinedRuleMapper.selectById(1L)).thenReturn(predefinedRule);
    when(predefinedRuleMapper.updateById(any(PredefinedRule.class))).thenReturn(1);

    // 准备更新数据
    PredefinedRuleDTO updateDTO = new PredefinedRuleDTO();
    updateDTO.setCertTypeCode("ID_CARD");
    updateDTO.setFieldId(1L);
    updateDTO.setRuleName("更新后的规则名称");
    updateDTO.setValidationLogic("^\\d{18}$");
    updateDTO.setErrorMessage("更新后的错误信息");

    // 执行测试
    PredefinedRuleDTO result = predefinedRuleService.updatePredefinedRule(1L, updateDTO);

    // 验证结果
    assertNotNull(result);
    assertEquals(1L, result.getRuleId());
    assertEquals("更新后的规则名称", result.getRuleName());
    assertEquals("^\\d{18}$", result.getValidationLogic());

    // 验证方法调用
    verify(predefinedRuleMapper, times(1)).selectById(1L);
    verify(predefinedRuleMapper, times(1)).updateById(any(PredefinedRule.class));
  }

  /**
   * 测试更新预置规则 - 规则不存在
   */
  @Test
  void testUpdatePredefinedRule_NotFound() {
    // Mock行为
    when(predefinedRuleMapper.selectById(999L)).thenReturn(null);

    // 执行测试并验证异常
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> predefinedRuleService.updatePredefinedRule(999L, predefinedRuleDTO)
    );

    assertEquals("预置规则不存在", exception.getMessage());
  }

  /**
   * 测试更新预置规则 - 证件类型代码为空
   */
  @Test
  void testUpdatePredefinedRule_EmptyCertTypeCode() {
    // Mock行为
    when(predefinedRuleMapper.selectById(1L)).thenReturn(predefinedRule);

    // 准备更新数据
    PredefinedRuleDTO updateDTO = new PredefinedRuleDTO();
    updateDTO.setCertTypeCode("");
    updateDTO.setFieldId(1L);
    updateDTO.setRuleName("规则名称");
    updateDTO.setValidationLogic("^\\d{17}[0-9Xx]$");

    // 执行测试并验证异常
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> predefinedRuleService.updatePredefinedRule(1L, updateDTO)
    );

    assertEquals("证件类型代码不能为空", exception.getMessage());
  }

  /**
   * 测试删除预置规则 - 正常流程
   */
  @Test
  void testDeletePredefinedRule_Success() {
    // Mock行为
    when(predefinedRuleMapper.selectById(1L)).thenReturn(predefinedRule);
    when(predefinedRuleMapper.updateById(any(PredefinedRule.class))).thenReturn(1);

    // 执行测试
    predefinedRuleService.deletePredefinedRule(1L);

    // 验证方法调用
    verify(predefinedRuleMapper, times(1)).selectById(1L);
    verify(predefinedRuleMapper, times(1)).updateById(any(PredefinedRule.class));
  }

  /**
   * 测试删除预置规则 - 规则不存在
   */
  @Test
  void testDeletePredefinedRule_NotFound() {
    // Mock行为
    when(predefinedRuleMapper.selectById(999L)).thenReturn(null);

    // 执行测试并验证异常
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> predefinedRuleService.deletePredefinedRule(999L)
    );

    assertEquals("预置规则不存在", exception.getMessage());
  }

  /**
   * 测试删除预置规则 - 规则已删除
   */
  @Test
  void testDeletePredefinedRule_AlreadyDeleted() {
    // 准备测试数据 - 已删除
    PredefinedRule deletedRule = new PredefinedRule();
    deletedRule.setId(1L);
    deletedRule.setIsDeleted(1);

    // Mock行为
    when(predefinedRuleMapper.selectById(1L)).thenReturn(deletedRule);

    // 执行测试并验证异常
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> predefinedRuleService.deletePredefinedRule(1L)
    );

    assertEquals("预置规则不存在", exception.getMessage());
  }
}
