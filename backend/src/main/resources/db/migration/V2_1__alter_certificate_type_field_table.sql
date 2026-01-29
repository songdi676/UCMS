-- ============================================
-- UCMS 证件类型字段配置表扩展
-- ============================================

-- 添加字段作用域字段 ('私有'或'公有')
ALTER TABLE UCMS_CERTIFICATE_TYPE_FIELD
  ADD COLUMN scope VARCHAR(10) NOT NULL DEFAULT '私有' COMMENT '字段作用域: 私有-仅当前证件类型可用, 公有-可被其他证件类型引用'
  AFTER is_required;
