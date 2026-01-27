package com.newland.ucms.certificate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import com.newland.ucms.certificate.service.ValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统一验证服务实现
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@Slf4j
@Service
public class ValidationServiceImpl implements ValidationService {

  @Autowired
  private ScopeConfigMapper scopeConfigMapper;

  @Autowired
  private CertificateTypeMapper certificateTypeMapper;

  @Autowired
  private CertificateTypeFieldMapper certificateTypeFieldMapper;

  @Autowired
  private ValidationRuleConfigMapper validationRuleConfigMapper;

  @Autowired
  private ValidationRuleItemMapper validationRuleItemMapper;

  @Autowired
  private com.newland.ucms.certificate.mapper.FieldLibraryMapper fieldLibraryMapper;

  @Override
  public CertificateValidationResponse validateCertificate(CertificateRequest request) {
    log.info("开始验证证件信息，请求参数：{}", request);

    CertificateValidationResponse response = new CertificateValidationResponse();
    response.setSuccess(true);
    response.setErrors(new HashMap<>());

    try {
      // 步骤1：根据业务信息匹配应用范围配置
      ScopeConfig scopeConfig = findMatchingScopeConfig(request);
      if (scopeConfig == null) {
        log.warn("未找到匹配的应用范围配置，请求参数：{}", request);
        response.setSuccess(false);
        response.setMessage("未找到匹配的应用范围配置");
        return response;
      }

      // 步骤2：获取允许的证件类型
      String allowedCertTypes = scopeConfig.getAllowedCertTypes();
      if (!StringUtils.hasText(allowedCertTypes)) {
        log.warn("应用范围配置中未配置允许的证件类型");
        response.setSuccess(false);
        response.setMessage("未配置允许的证件类型");
        return response;
      }

      // 步骤3：验证证件类型是否在允许的范围内
      if (StringUtils.hasText(request.getCertTypeCode())) {
        if (!allowedCertTypes.contains(request.getCertTypeCode())) {
          log.warn("证件类型{}不在允许的范围内：{}", request.getCertTypeCode(), allowedCertTypes);
          response.setSuccess(false);
          response.setMessage("证件类型不在允许的范围内");
          return response;
        }
      }

      // 如果没有指定证件类型，使用第一个允许的证件类型
      String certTypeCode = request.getCertTypeCode();
      if (!StringUtils.hasText(certTypeCode)) {
        certTypeCode = allowedCertTypes.split("[|,]")[0].trim();
        log.info("未指定证件类型，使用默认证件类型：{}", certTypeCode);
      }

      // 步骤4：查询证件类型配置
      CertificateType certificateType = certificateTypeMapper.selectOne(
          new QueryWrapper<CertificateType>()
              .eq("cert_type_code", certTypeCode)
              .eq("status", StatusEnum.ENABLED.getCode())
      );

      if (certificateType == null) {
        log.warn("证件类型配置不存在：{}", certTypeCode);
        response.setSuccess(false);
        response.setMessage("证件类型配置不存在");
        return response;
      }

      // 步骤5：查询证件类型字段配置
      List<CertificateTypeField> fields = certificateTypeFieldMapper.selectList(
          new QueryWrapper<CertificateTypeField>()
              .eq("cert_type_code", certTypeCode)
              .eq("is_deleted", 0)
              .orderByAsc("sort_order")
      );

      if (CollectionUtils.isEmpty(fields)) {
        log.warn("证件类型{}未配置字段", certTypeCode);
        response.setSuccess(false);
        response.setMessage("证件类型未配置字段");
        return response;
      }

      // 步骤6：验证必填字段
      validateRequiredFields(request, fields, response);

      // 步骤7：查询验证规则配置
      ValidationRuleConfig ruleConfig = validationRuleConfigMapper.selectOne(
          new QueryWrapper<ValidationRuleConfig>()
              .eq("business_code", request.getBusinessCode())
              .eq("status", StatusEnum.ENABLED.getCode())
              .eq("is_deleted", 0)
      );

      if (ruleConfig != null) {
        // 步骤8：查询验证规则明细
        List<ValidationRuleItem> ruleItems = validationRuleItemMapper.selectList(
            new QueryWrapper<ValidationRuleItem>()
                .eq("rule_id", ruleConfig.getId())
                .eq("is_deleted", 0)
                .orderByAsc("sort_order")
        );

        // 步骤9：根据验证规则验证字段值
        validateFieldValues(request, ruleItems, response);
      }

      // 步骤10：验证字段数据类型
      validateFieldDataTypes(request, fields, response);

      // 检查是否有错误
      if (!response.getErrors().isEmpty()) {
        response.setSuccess(false);
        response.setMessage("证件验证失败");
      }

      log.info("证件验证完成，结果：{}", response.getSuccess() ? "成功" : "失败");
      return response;

    } catch (Exception e) {
      log.error("证件验证过程中发生异常", e);
      response.setSuccess(false);
      response.setMessage("证件验证过程中发生异常：" + e.getMessage());
      return response;
    }
  }

  /**
   * 根据业务信息匹配应用范围配置
   * 匹配规则：优先级从高到低依次匹配
   * 1. 全部匹配（渠道小类 + 地市 + 区县 + 机构）
   * 2. 渠道小类 + 地市 + 区县
   * 3. 渠道小类 + 地市
   * 4. 渠道小类
   * 5. 全局配置（所有字段为空）
   */
  private ScopeConfig findMatchingScopeConfig(CertificateRequest request) {
    // 先尝试精确匹配
    QueryWrapper<ScopeConfig> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("status", StatusEnum.ENABLED.getCode());
    queryWrapper.eq("is_deleted", 0);

    // 添加渠道小类匹配条件
    if (StringUtils.hasText(request.getChannelSubType())) {
      queryWrapper.eq("channel_sub_type", request.getChannelSubType());
    } else {
      queryWrapper.isNull("channel_sub_type");
    }

    // 添加地市匹配条件
    if (StringUtils.hasText(request.getCity())) {
      queryWrapper.eq("city", request.getCity());
    } else {
      queryWrapper.isNull("city");
    }

    // 添加区县匹配条件
    if (StringUtils.hasText(request.getDistrict())) {
      queryWrapper.eq("district", request.getDistrict());
    } else {
      queryWrapper.isNull("district");
    }

    // 添加机构匹配条件
    if (StringUtils.hasText(request.getInstitution())) {
      queryWrapper.eq("institution", request.getInstitution());
    } else {
      queryWrapper.isNull("institution");
    }

    List<ScopeConfig> exactMatches = scopeConfigMapper.selectList(queryWrapper);
    if (!CollectionUtils.isEmpty(exactMatches)) {
      return exactMatches.get(0);
    }

    // 如果没有精确匹配，则使用全局配置
    QueryWrapper<ScopeConfig> globalQuery = new QueryWrapper<>();
    globalQuery.eq("status", StatusEnum.ENABLED.getCode());
    globalQuery.eq("is_deleted", 0);
    globalQuery.isNull("business_channel");
    globalQuery.isNull("channel_sub_type");
    globalQuery.isNull("city");
    globalQuery.isNull("district");
    globalQuery.isNull("institution");

    List<ScopeConfig> globalMatches = scopeConfigMapper.selectList(globalQuery);
    return CollectionUtils.isEmpty(globalMatches) ? null : globalMatches.get(0);
  }

  /**
   * 验证必填字段
   */
  private void validateRequiredFields(CertificateRequest request,
                                   List<CertificateTypeField> fields,
                                   CertificateValidationResponse response) {
    for (CertificateTypeField field : fields) {
      if (field.getIsRequired() == 1) {
        Object value = request.getFieldValues() != null
            ? request.getFieldValues().get(field.getFieldNameEn())
            : null;

        if (value == null || value.toString().trim().isEmpty()) {
          String fieldName = field.getFieldNameCn();
          log.warn("必填字段{}为空", fieldName);
          response.getErrors().put(field.getFieldNameEn(), fieldName + "不能为空");
        }
      }
    }
  }

  /**
   * 根据验证规则验证字段值
   */
  private void validateFieldValues(CertificateRequest request,
                                List<ValidationRuleItem> ruleItems,
                                CertificateValidationResponse response) {
    Map<String, Object> fieldValues = request.getFieldValues();
    if (fieldValues == null || fieldValues.isEmpty()) {
      return;
    }

    for (ValidationRuleItem rule : ruleItems) {
      String fieldName = rule.getFieldName();
      String logic = rule.getValidationLogic();
      String value = rule.getValidationValue();

      Object fieldValue = fieldValues.get(fieldName);
      if (fieldValue == null || fieldValue.toString().trim().isEmpty()) {
        continue; // 字段为空，跳过验证（必填字段已在前面验证）
      }

      String fieldValueStr = fieldValue.toString().trim();

      switch (logic) {
        case ">":
          if (!isNumeric(fieldValueStr) || Double.parseDouble(fieldValueStr) <= Double.parseDouble(value)) {
            response.getErrors().put(fieldName, fieldName + "必须大于" + value);
          }
          break;

        case "<":
          if (!isNumeric(fieldValueStr) || Double.parseDouble(fieldValueStr) >= Double.parseDouble(value)) {
            response.getErrors().put(fieldName, fieldName + "必须小于" + value);
          }
          break;

        case ">=":
          if (!isNumeric(fieldValueStr) || Double.parseDouble(fieldValueStr) < Double.parseDouble(value)) {
            response.getErrors().put(fieldName, fieldName + "必须大于等于" + value);
          }
          break;

        case "<=":
          if (!isNumeric(fieldValueStr) || Double.parseDouble(fieldValueStr) > Double.parseDouble(value)) {
            response.getErrors().put(fieldName, fieldName + "必须小于等于" + value);
          }
          break;

        case "==":
          if (!fieldValueStr.equals(value)) {
            response.getErrors().put(fieldName, fieldName + "必须等于" + value);
          }
          break;

        case "!=":
          if (fieldValueStr.equals(value)) {
            response.getErrors().put(fieldName, fieldName + "不能等于" + value);
          }
          break;

        case "contains":
          if (!fieldValueStr.contains(value)) {
            response.getErrors().put(fieldName, fieldName + "必须包含" + value);
          }
          break;

        case "regex":
          if (!fieldValueStr.matches(value)) {
            response.getErrors().put(fieldName, fieldName + "格式不正确");
          }
          break;

        default:
          log.warn("未知的验证逻辑：{}", logic);
      }
    }
  }

  /**
   * 验证字段数据类型
   */
  private void validateFieldDataTypes(CertificateRequest request,
                                     List<CertificateTypeField> fields,
                                     CertificateValidationResponse response) {
    Map<String, Object> fieldValues = request.getFieldValues();
    if (fieldValues == null || fieldValues.isEmpty()) {
      return;
    }

    for (CertificateTypeField field : fields) {
      String fieldName = field.getFieldNameEn();
      Object fieldValue = fieldValues.get(fieldName);

      if (fieldValue == null || fieldValue.toString().trim().isEmpty()) {
        continue;
      }

      // 从字段库中获取字段数据类型
      com.newland.ucms.certificate.entity.FieldLibrary fieldLibrary = null;
      if (field.getFieldId() != null) {
        fieldLibrary = fieldLibraryMapper.selectById(field.getFieldId());
      }
      String fieldType = fieldLibrary != null ? fieldLibrary.getFieldDataType() : "text";
      String fieldValueStr = fieldValue.toString().trim();

      try {
        switch (fieldType) {
          case "number":
            if (!isNumeric(fieldValueStr)) {
              response.getErrors().put(fieldName, fieldName + "必须是数字");
            }
            break;

          case "text":
            // 文本类型不需要特殊验证
            break;

          case "date":
            LocalDate.parse(fieldValueStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            break;

          case "date_range":
            // 日期范围格式：YYYY-MM-DD 至 YYYY-MM-DD
            String[] dateParts = fieldValueStr.split(" 至 ");
            if (dateParts.length != 2) {
              response.getErrors().put(fieldName, fieldName + "格式不正确，应为：YYYY-MM-DD 至 YYYY-MM-DD");
            } else {
              LocalDate startDate = LocalDate.parse(dateParts[0].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
              LocalDate endDate = LocalDate.parse(dateParts[1].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
              if (startDate.isAfter(endDate)) {
                response.getErrors().put(fieldName, fieldName + "开始日期不能晚于结束日期");
              }
            }
            break;

          default:
            log.warn("未知的字段数据类型：{}", fieldType);
        }
      } catch (DateTimeParseException e) {
        response.getErrors().put(fieldName, fieldName + "日期格式不正确");
      }
    }
  }

  /**
   * 判断字符串是否为数字
   */
  private boolean isNumeric(String str) {
    if (str == null || str.isEmpty()) {
      return false;
    }
    try {
      Double.parseDouble(str);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
