package com.newland.ucms.certificate.util;

import com.newland.ucms.certificate.dto.CertificateRequest;
import com.newland.ucms.certificate.dto.UnifiedValidationRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 报文解析器工具类
 * 用于将统一验证接口请求报文转换为证件验证请求
 *
 * @author UCMS Team
 * @since 2026-01-29
 */
public class RequestParser {

  /**
   * 解析统一验证请求，转换为证件验证请求
   *
   * @param unifiedRequest 统一验证请求
   * @return 证件验证请求
   */
  public static CertificateRequest parse(UnifiedValidationRequest unifiedRequest) {
    // 构建返回对象
    CertificateRequest certificateRequest = new CertificateRequest();

    // 提取业务类型
    String bizType = unifiedRequest.getXContent().getBizItemList().get(0).getBizType();
    certificateRequest.setBusinessType(bizType);

    // 提取渠道小类（从场景类型编码获取）
    String channelSubType = unifiedRequest.getXContent().getBizItemList().get(0).getSceneTypeCode();
    certificateRequest.setChannelSubType(channelSubType);

    // 提取地市
    String city = unifiedRequest.getXCommonParams().getAcceptInfo().getAcceptCity();
    certificateRequest.setCity(city);

    // 提取机构
    String institution = unifiedRequest.getXCommonParams().getAcceptInfo().getAcceptOrgId();
    certificateRequest.setInstitution(institution);

    // 提取证件事实数据
    Map<String, Object> certificateFactData =
        unifiedRequest.getXContent().getBizItemList().get(0).getObjectInfoList().get(0).getCertificateFactData();

    // 提取证件类型代码
    String certTypeCode = String.valueOf(certificateFactData.get("type"));
    certificateRequest.setCertTypeCode(certTypeCode);

    // 提取字段值（排除type字段）
    Map<String, Object> fieldValues = new HashMap<>();
    for (Map.Entry<String, Object> entry : certificateFactData.entrySet()) {
      if (!"type".equals(entry.getKey())) {
        fieldValues.put(entry.getKey(), entry.getValue());
      }
    }
    certificateRequest.setFieldValues(fieldValues);

    return certificateRequest;
  }
}
