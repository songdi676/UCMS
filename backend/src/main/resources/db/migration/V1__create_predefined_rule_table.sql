-- ============================================
-- UCMS 预置规则表
-- ============================================

CREATE TABLE IF NOT EXISTS UCMS_PREDEFINED_RULE (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
  cert_type_code VARCHAR(10) NOT NULL COMMENT '证件类型代码',
  field_id BIGINT NOT NULL COMMENT '字段ID,关联UCMS_FIELD_LIBRARY.id',
  rule_name VARCHAR(100) NOT NULL COMMENT '规则名称',
  validation_logic VARCHAR(50) NOT NULL COMMENT '校验逻辑',
  validation_value VARCHAR(200) COMMENT '校验值',
  error_message VARCHAR(200) COMMENT '错误信息',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态,1-启用,0-禁用',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  created_by VARCHAR(50) COMMENT '创建人',
  updated_by VARCHAR(50) COMMENT '更新人',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '删除标识,1-已删除,0-未删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预置规则表';

-- 索引
CREATE INDEX IF NOT EXISTS idx_cert_type_code ON UCMS_PREDEFINED_RULE(cert_type_code);
CREATE INDEX IF NOT EXISTS idx_field_id ON UCMS_PREDEFINED_RULE(field_id);
CREATE INDEX IF NOT EXISTS idx_status ON UCMS_PREDEFINED_RULE(status);
CREATE INDEX IF NOT EXISTS idx_is_deleted ON UCMS_PREDEFINED_RULE(is_deleted);
