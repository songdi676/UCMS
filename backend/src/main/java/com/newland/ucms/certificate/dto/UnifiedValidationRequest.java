package com.newland.ucms.certificate.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 统一证件验证请求DTO
 * 映射业务受理平台调用的统一验证接口请求报文
 *
 * @author UCMS Team
 * @since 2026-01-29
 */
@Data
public class UnifiedValidationRequest {

  /**
   * 业务编码
   */
  private String xBizCode;

  /**
   * 业务描述
   */
  private String xBizDesc;

  /**
   * 业务中心
   */
  private String xBizCenter;

  /**
   * 公共参数
   */
  private XCommonParams xCommonParams;

  /**
   * 业务内容
   */
  private XContent xContent;

  /**
   * 公共参数DTO
   */
  @Data
  public static class XCommonParams {

    /**
     * 受理流水号
     */
    private Long acceptId;

    /**
     * 交易流水号
     */
    private String txId;

    /**
     * 请求时间
     */
    private String requestTime;

    /**
     * 验证码
     */
    private String verifyCode;

    /**
     * 认证类型
     */
    private String identifyType;

    /**
     * 请求来源
     */
    private String requestSource;

    /**
     * 认证标识
     */
    private String identifyId;

    /**
     * 系统功能ID
     */
    private String sysfuncId;

    /**
     * 系统功能名称
     */
    private String sysfuncName;

    /**
     * 操作员会话ID
     */
    private String operatorSessionId;

    /**
     * 业务会话ID
     */
    private String businessSessionId;

    /**
     * 受理信息
     */
    private AcceptInfo acceptInfo;

    /**
     * 操作员信息
     */
    private OperatorInfo operatorInfo;

    /**
     * 认证信息
     */
    private Identity identity;
  }

  /**
   * 受理信息DTO
   */
  @Data
  public static class AcceptInfo {

    /**
     * 受理省份
     */
    private String acceptProvince;

    /**
     * 受理地市
     */
    private String acceptCity;

    /**
     * 受理机构ID
     */
    private String acceptOrgId;

    /**
     * 受理机构名称
     */
    private String acceptOrgName;
  }

  /**
   * 操作员信息DTO
   */
  @Data
  public static class OperatorInfo {

    /**
     * 操作员ID
     */
    private String operatorId;

    /**
     * 操作员地市
     */
    private String operatorCity;

    /**
     * 操作员区县
     */
    private String operatorCounty;

    /**
     * 操作员级别
     */
    private String operatorLevel;

    /**
     * 操作员姓名
     */
    private String operatorName;

    /**
     * 操作员归属区域
     */
    private String operatorHomeArea;

    /**
     * 操作员归属区域名称
     */
    private String operatorHomeAreaName;

    /**
     * 门户ID
     */
    private String portalid;
  }

  /**
   * 认证信息DTO
   */
  @Data
  public static class Identity {

    /**
     * 流程编码
     */
    private String processCode;
  }

  /**
   * 业务内容DTO
   */
  @Data
  public static class XContent {

    /**
     * 检查结果
     */
    private CheckResult checkResult;

    /**
     * 业务项目列表
     */
    private List<BizItem> bizItemList;
  }

  /**
   * 检查结果DTO
   */
  @Data
  public static class CheckResult {

    /**
     * 错误编码
     */
    private String errCode;

    /**
     * 检查结果明细列表
     */
    private List<Object> checkResultDetailList;
  }

  /**
   * 业务项目DTO
   */
  @Data
  public static class BizItem {

    /**
     * 受理对象类型列表
     */
    private List<ObjectInfo> objectInfoList;

    /**
     * 场景类型编码
     */
    private String sceneTypeCode;

    /**
     * 业务大类
     */
    private String bizCategory;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 项目分组
     */
    private String itemGrp;

    /**
     * 项目ID
     */
    private String itemId;

    /**
     * 完整性
     */
    private Integer integrity;

    /**
     * 项目类型编码
     */
    private Integer itemTypeCode;

    /**
     * 项目编码
     */
    private String itemCode;

    /**
     * 项目名称
     */
    private String itemName;

    /**
     * 父项目ID
     */
    private String parentItemId;

    /**
     * 动作
     */
    private String action;

    /**
     * 数量
     */
    private Integer num;

    /**
     * 支付类型
     */
    private Integer paymentType;

    /**
     * 数据来源
     */
    private Integer dataSource;

    /**
     * 关系列表
     */
    private List<RelaItem> relaList;

    /**
     * 套餐项目信息
     */
    private OfferItemInfo offerItemInfo;
  }

  /**
   * 对象信息DTO
   */
  @Data
  public static class ObjectInfo {

    /**
     * 受理对象类型
     */
    private Integer acceptObjectType;

    /**
     * 地区编码
     */
    private Integer regionCode;

    /**
     * 区县编码
     */
    private Integer countyCode;

    /**
     * 对象类型
     */
    private Integer objType;

    /**
     * 对象ID
     */
    private String objId;

    /**
     * 对象号码
     */
    private String objNumber;

    /**
     * 客户类型
     */
    private Integer customerType;

    /**
     * 客户ID
     */
    private String customerId;

    /**
     * 证件事实数据（动态字段，Map类型）
     * 包含证件类型、证件号码、姓名等字段
     * 示例: {"type":"身份证","age":18,"cert_number":"44152319851109XXX"}
     */
    private Map<String, Object> certificateFactData;
  }

  /**
   * 关系项DTO
   */
  @Data
  public static class RelaItem {
    // 关系项具体字段待定义
  }

  /**
   * 套餐项目信息DTO
   */
  @Data
  public static class OfferItemInfo {

    /**
     * 套餐实例ID
     */
    private String offeringInstId;

    /**
     * 订购ID
     */
    private String subsId;

    /**
     * 套餐ID
     */
    private String offeringId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 生效类型
     */
    private Integer effType;

    /**
     * 生效日期
     */
    private String effDate;

    /**
     * 失效日期
     */
    private String expDate;

    /**
     * 父套餐ID
     */
    private String pOfferingId;

    /**
     * 父套餐实例ID
     */
    private String pOfferingInstId;

    /**
     * 价格ID
     */
    private String prcId;

    /**
     * 销售ID
     */
    private String salesId;

    /**
     * 销售角色类型
     */
    private String salesRoleType;

    /**
     * 项目属性列表
     */
    private List<ItemAttrList> itemAttrList;

    /**
     * 产品列表
     */
    private List<ProductItemInfo> productList;
  }

  /**
   * 产品项目信息DTO
   */
  @Data
  public static class ProductItemInfo {

    /**
     * 受理对象类型列表
     */
    private List<ObjectInfo> objectInfoList;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 项目ID
     */
    private String itemId;

    /**
     * 项目类型编码
     */
    private Integer itemTypeCode;

    /**
     * 项目名称
     */
    private String itemName;

    /**
     * 父项目ID
     */
    private String parentItemId;

    /**
     * 数量
     */
    private Integer num;

    /**
     * 关系列表
     */
    private List<RelaItem> relaList;

    /**
     * 产品项目信息
     */
    private ProductItemInfoDetail productItemInfo;
  }

  /**
   * 产品项目信息详情DTO
   */
  @Data
  public static class ProductItemInfoDetail {

    /**
     * 订购ID
     */
    private String subsId;

    /**
     * 产品ID
     */
    private String productId;

    /**
     * 生效日期
     */
    private String effDate;

    /**
     * 失效日期
     */
    private String expDate;

    /**
     * 服务列表
     */
    private List<ServiceList> serviceList;

    /**
     * 项目属性列表
     */
    private List<ItemAttrList> itemAttrList;
  }

  /**
   * 服务列表DTO
   */
  @Data
  public static class ServiceList {

    /**
     * 受理对象类型列表
     */
    private List<ObjectInfo> objectInfoList;

    /**
     * 项目ID
     */
    private String itemId;

    /**
     * 关系列表
     */
    private List<RelaItem> relaList;

    /**
     * 服务项目信息
     */
    private SvrItemInfo svrItemInfo;
  }

  /**
   * 服务项目信息DTO
   */
  @Data
  public static class SvrItemInfo {

    /**
     * 服务ID
     */
    private String serviceId;

    /**
     * 生效日期
     */
    private String effDate;

    /**
     * 失效日期
     */
    private String expDate;

    /**
     * 项目属性列表
     */
    private List<ItemAttrList> itemAttrList;
  }

  /**
   * 项目属性列表DTO
   */
  @Data
  public static class ItemAttrList {
    // 项目属性具体字段待定义
  }
}
