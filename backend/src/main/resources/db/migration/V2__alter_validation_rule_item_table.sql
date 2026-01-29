-- ============================================
-- UCMS 校验规则明细表扩展
-- ============================================

-- 1. 添加规则来源字段 (0-自定义, 1-预定义)
ALTER TABLE UCMS_VALIDATION_RULE_ITEM
  ADD COLUMN rule_source TINYINT NOT NULL DEFAULT 0 COMMENT '规则来源: 0-自定义, 1-预定义'
  AFTER validation_value;

-- 2. 添加预定义规则ID字段
ALTER TABLE UCMS_VALIDATION_RULE_ITEM
  ADD COLUMN predefined_rule_id BIGINT COMMENT '预定义规则ID (预定义规则时使用)'
  AFTER rule_source;

-- 3. 添加错误消息字段
ALTER TABLE UCMS_VALIDATION_RULE_ITEM
  ADD COLUMN error_message VARCHAR(200) COMMENT '错误消息 (自定义规则时使用)'
  AFTER predefined_rule_id;

-- 4. 添加证件类型字段ID
ALTER TABLE UCMS_VALIDATION_RULE_ITEM
  ADD COLUMN certificate_type_field_id BIGINT COMMENT '证件类型字段ID (关联UCMS_CERTIFICATE_TYPE_FIELD表)'
  AFTER error_message;
