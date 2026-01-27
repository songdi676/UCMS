-- ============================================
-- UCMS 统一证件验证中台系统 - 数据库表结构
-- ============================================

-- 1. 证件类型主表
CREATE TABLE IF NOT EXISTS UCMS_CERTIFICATE_TYPE (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
  cert_type_code VARCHAR(10) NOT NULL UNIQUE COMMENT '证件类型代码（1, 2, 3等）',
  cert_type_name VARCHAR(50) NOT NULL COMMENT '证件类型名称（身份证、护照等）',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-停用, 1-启用',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  created_by VARCHAR(50) COMMENT '创建人',
  updated_by VARCHAR(50) COMMENT '修改人',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0-未删除, 1-已删除'
) COMMENT='证件类型主表';

-- 1.1 证件类型主表索引
CREATE INDEX idx_cert_type_code ON UCMS_CERTIFICATE_TYPE(cert_type_code);
CREATE INDEX idx_status ON UCMS_CERTIFICATE_TYPE(status);
CREATE INDEX idx_is_deleted ON UCMS_CERTIFICATE_TYPE(is_deleted);

-- 2. 字段库表（支持公共字段和自定义字段）
CREATE TABLE IF NOT EXISTS UCMS_FIELD_LIBRARY (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
  field_name_cn VARCHAR(50) NOT NULL COMMENT '字段中文名称（姓名、性别、民族等）',
  field_name_en VARCHAR(50) NOT NULL COMMENT '字段英文名称（name、gender、nation等）',
  field_type VARCHAR(20) NOT NULL COMMENT '字段类型：PUBLIC-公共字段，CUSTOM-自定义字段',
  field_data_type VARCHAR(20) NOT NULL COMMENT '数据类型：text、number、date、date_range等',
  field_length INT COMMENT '字段长度',
  default_value VARCHAR(200) COMMENT '默认值',
  creator VARCHAR(50) COMMENT '创建人（自定义字段需要记录）',
  description VARCHAR(200) COMMENT '字段描述',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-停用，1-启用',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0-未删除, 1-已删除'
) COMMENT='字段库表';

-- 2.1 字段库表索引
CREATE INDEX idx_field_name_cn ON UCMS_FIELD_LIBRARY(field_name_cn);
CREATE INDEX idx_field_name_en ON UCMS_FIELD_LIBRARY(field_name_en);
CREATE INDEX idx_field_type ON UCMS_FIELD_LIBRARY(field_type);
CREATE INDEX idx_status ON UCMS_FIELD_LIBRARY(status);
CREATE INDEX idx_is_deleted ON UCMS_FIELD_LIBRARY(is_deleted);

-- 3. 证件类型字段配置表
CREATE TABLE IF NOT EXISTS UCMS_CERTIFICATE_TYPE_FIELD (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
  cert_type_code VARCHAR(10) NOT NULL COMMENT '证件类型代码（外键UCMS_CERTIFICATE_TYPE.cert_type_code）',
  field_id BIGINT COMMENT '字段ID（外键UCMS_FIELD_LIBRARY.id）',
  field_name_en VARCHAR(50) COMMENT '字段英文名称（冗余字段，便于查询）',
  field_name_cn VARCHAR(50) COMMENT '字段中文名称（冗余字段，便于查询）',
  is_required TINYINT NOT NULL DEFAULT 0 COMMENT '是否必填：0-否, 1-是',
  sort_order INT DEFAULT 0 COMMENT '排序',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  updated_by VARCHAR(50) COMMENT '更新人',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0-未删除, 1-已删除'
) COMMENT='证件类型字段配置表';

-- 3.1 证件类型字段配置表索引
CREATE INDEX idx_cert_type_code ON UCMS_CERTIFICATE_TYPE_FIELD(cert_type_code);
CREATE INDEX idx_field_id ON UCMS_CERTIFICATE_TYPE_FIELD(field_id);
CREATE INDEX idx_is_deleted ON UCMS_CERTIFICATE_TYPE_FIELD(is_deleted);

-- 4. 校验规则配置表
CREATE TABLE IF NOT EXISTS UCMS_VALIDATION_RULE_CONFIG (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
  business_type VARCHAR(100) COMMENT '业务类型（如"档案补正"等）',
  business_code VARCHAR(50) NOT NULL COMMENT '业务编号',
  channel_sub_type VARCHAR(50) COMMENT '受理渠道',
  allowed_cert_types TEXT COMMENT '可用证件类型（JSON数组）',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-停用, 1-启用',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  created_by VARCHAR(50) COMMENT '创建人',
  updated_by VARCHAR(50) COMMENT '修改人',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0-未删除, 1-已删除'
) COMMENT='校验规则配置表';

-- 4.1 校验规则配置表索引
CREATE INDEX idx_business_code ON UCMS_VALIDATION_RULE_CONFIG(business_code);
CREATE INDEX idx_channel_sub_type ON UCMS_VALIDATION_RULE_CONFIG(channel_sub_type);
CREATE INDEX idx_status ON UCMS_VALIDATION_RULE_CONFIG(status);
CREATE INDEX idx_is_deleted ON UCMS_VALIDATION_RULE_CONFIG(is_deleted);

-- 5. 校验规则明细表
CREATE TABLE IF NOT EXISTS UCMS_VALIDATION_RULE_ITEM (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
  rule_id BIGINT NOT NULL COMMENT '规则ID（外键UCMS_VALIDATION_RULE_CONFIG.id）',
  field_name VARCHAR(50) NOT NULL COMMENT '字段名称（关联证件类型字段）',
  validation_logic VARCHAR(50) NOT NULL COMMENT '校验逻辑（>、<、>=等）',
  validation_value VARCHAR(100) COMMENT '校验值（如18表示age>18）',
  sort_order INT DEFAULT 0 COMMENT '排序',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0-未删除, 1-已删除'
) COMMENT='校验规则明细表';

-- 5.1 校验规则明细表索引
CREATE INDEX idx_rule_id ON UCMS_VALIDATION_RULE_ITEM(rule_id);
CREATE INDEX idx_field_name ON UCMS_VALIDATION_RULE_ITEM(field_name);
CREATE INDEX idx_is_deleted ON UCMS_VALIDATION_RULE_ITEM(is_deleted);

-- 6. 应用范围配置表
CREATE TABLE IF NOT EXISTS UCMS_SCOPE_CONFIG (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
  business_channel VARCHAR(50) COMMENT '应用系统（移动云厅、网格通等，可为空表示全局）',
  channel_sub_type VARCHAR(50) COMMENT '渠道小类（可为空）',
  city VARCHAR(50) COMMENT '地市（可为空）',
  district VARCHAR(50) COMMENT '区县（可为空）',
  institution VARCHAR(100) COMMENT '机构（可为空）',
  allowed_cert_types TEXT NOT NULL COMMENT '允许的证件类型列表（JSON数组，如["1","2","3"]，支持多个，使用|分隔）',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-停用, 1-启用',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  created_by VARCHAR(50) COMMENT '创建人',
  updated_by VARCHAR(50) COMMENT '修改人',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0-未删除, 1-已删除'
) COMMENT='应用范围配置表';

-- 6.1 应用范围配置表索引
CREATE INDEX idx_business_channel ON UCMS_SCOPE_CONFIG(business_channel);
CREATE INDEX idx_channel_sub_type ON UCMS_SCOPE_CONFIG(channel_sub_type);
CREATE INDEX idx_city ON UCMS_SCOPE_CONFIG(city);
CREATE INDEX idx_district ON UCMS_SCOPE_CONFIG(district);
CREATE INDEX idx_institution ON UCMS_SCOPE_CONFIG(institution);
CREATE INDEX idx_status ON UCMS_SCOPE_CONFIG(status);
CREATE INDEX idx_is_deleted ON UCMS_SCOPE_CONFIG(is_deleted);
