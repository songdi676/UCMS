-- ============================================
-- UCMS 统一证件验证中台系统 - 初始化数据
-- ============================================

-- 1. 插入字段库数据（公共字段）
INSERT INTO UCMS_FIELD_LIBRARY (field_name_cn, field_name_en, field_type, field_data_type, field_length, creator, description, status) VALUES
('姓名', 'name', 'PUBLIC', 'text', 50, 'SYSTEM', '证件持有人姓名', 1),
('民族', 'nation', 'PUBLIC', 'text', 20, 'SYSTEM', '证件持人民族', 1),
('性别', 'gender', 'PUBLIC', 'text', 2, 'SYSTEM', '证件持有人性别', 1),
('证件号码', 'cert_number', 'PUBLIC', 'text', 50, 'SYSTEM', '证件唯一编号', 1),
('期限起始', 'validity_start', 'PUBLIC', 'date', NULL, 'SYSTEM', '证件有效期起始日期', 1),
('期限终止', 'validity_end', 'PUBLIC', 'date', NULL, 'SYSTEM', '证件有效期终止日期', 1),
('出生日期', 'birth_date', 'PUBLIC', 'date', NULL, 'SYSTEM', '证件持有人出生日期', 1),
('签发机关', 'issuing_authority', 'PUBLIC', 'text', 100, 'SYSTEM', '证件签发机关名称', 1),
('地址', 'address', 'PUBLIC', 'text', 200, 'SYSTEM', '证件持有人地址', 1);

-- 2. 插入15种证件类型配置数据
-- 注：校验规则和字段配置将在后续任务中完善

-- 1. 身份证
INSERT INTO UCMS_CERTIFICATE_TYPE (cert_type_code, cert_type_name, status) VALUES
('1', '身份证', 1);

-- 2. 武装警察身份证
INSERT INTO UCMS_CERTIFICATE_TYPE (cert_type_code, cert_type_name, status) VALUES
('14', '武装警察身份证', 1);

-- 3. 军人身份证
INSERT INTO UCMS_CERTIFICATE_TYPE (cert_type_code, cert_type_name, status) VALUES
('24', '军人文员证', 1);

-- 4. 外国人永久居留身份证
INSERT INTO UCMS_CERTIFICATE_TYPE (cert_type_code, cert_type_name, status) VALUES
('15', '外国人永久居留身份证', 1);

-- 5. 港澳台居民居住证
INSERT INTO UCMS_CERTIFICATE_TYPE (cert_type_code, cert_type_name, status) VALUES
('16', '港澳台居民居住证', 1);

-- 6. 军官证
INSERT INTO UCMS_CERTIFICATE_TYPE (cert_type_code, cert_type_name, status) VALUES
('11', '军官证', 1);

-- 7. 护照
INSERT INTO UCMS_CERTIFICATE_TYPE (cert_type_code, cert_type_name, status) VALUES
('3', '护照', 1);

-- 8. 户口簿
INSERT INTO UCMS_CERTIFICATE_TYPE (cert_type_code, cert_type_name, status) VALUES
('22', '户口簿', 1);

-- 9. 台湾居民来往大陆通行证
INSERT INTO UCMS_CERTIFICATE_TYPE (cert_type_code, cert_type_name, status) VALUES
('13', '台湾居民来往大陆通行证', 1);

-- 10. 澳居民来往内地通行证
INSERT INTO UCMS_CERTIFICATE_TYPE (cert_type_code, cert_type_name, status) VALUES
('33', '澳门居民来往内地通行证', 1);

-- 11. 华侨护照
INSERT INTO UCMS_CERTIFICATE_TYPE (cert_type_code, cert_type_name, status) VALUES
('43', '华侨护照', 1);

-- 12. 临时居民身份证
INSERT INTO UCMS_CERTIFICATE_TYPE (cert_type_code, cert_type_name, status) VALUES
('23', '临时居民身份证', 1);

-- 13. 澳居民来往内地通行证(非中国籍)
INSERT INTO UCMS_CERTIFICATE_TYPE (cert_type_code, cert_type_name, status) VALUES
('20', '澳门居民来往内地通行证(非中国籍)', 1);

-- 3. 插入证件类型字段配置数据
-- 身份证字段配置
INSERT INTO UCMS_CERTIFICATE_TYPE_FIELD (cert_type_code, field_id, field_name_en, field_name_cn, is_required, sort_order) VALUES
('1', 1, 'name', '姓名', 1, 1),
('1', 4, 'cert_number', '证件号码', 1, 2),
('1', 7, 'birth_date', '出生日期', 0, 3),
('1', 5, 'validity_start', '期限起始', 0, 4),
('1', 6, 'validity_end', '期限终止', 0, 5),
('1', 8, 'issuing_authority', '签发机关', 0, 6);

-- 护照字段配置
INSERT INTO UCMS_CERTIFICATE_TYPE_FIELD (cert_type_code, field_id, field_name_en, field_name_cn, is_required, sort_order) VALUES
('3', 1, 'name', '姓名', 1, 1),
('3', 4, 'cert_number', '证件号码', 1, 2),
('3', 7, 'birth_date', '出生日期', 1, 3),
('3', 5, 'validity_start', '期限起始', 1, 4),
('3', 6, 'validity_end', '期限终止', 1, 5),
('3', 8, 'issuing_authority', '签发机关', 0, 6);

-- 军官证字段配置
INSERT INTO UCMS_CERTIFICATE_TYPE_FIELD (cert_type_code, field_id, field_name_en, field_name_cn, is_required, sort_order) VALUES
('11', 1, 'name', '姓名', 1, 1),
('11', 4, 'cert_number', '证件号码', 1, 2),
('11', 7, 'birth_date', '出生日期', 0, 3);

-- 4. 插入应用范围配置数据
-- 云厅应用范围配置（支持身份证、护照）
INSERT INTO UCMS_SCOPE_CONFIG (business_channel, allowed_cert_types, created_by, status) VALUES
('云厅', '1|3|11|13|14|15|16', 'SYSTEM', 1);

-- 网格通应用范围配置（支持身份证）
INSERT INTO UCMS_SCOPE_CONFIG (business_channel, allowed_cert_types, created_by, status) VALUES
('网格通', '1|14|15|16', 'SYSTEM', 1);

-- 便利店应用范围配置（仅支持身份证）
INSERT INTO UCMS_SCOPE_CONFIG (business_channel, allowed_cert_types, created_by, status) VALUES
('便利店', '1', 'SYSTEM', 1);

-- 按渠道小类配置
INSERT INTO UCMS_SCOPE_CONFIG (business_channel, channel_sub_type, allowed_cert_types, created_by, status) VALUES
('云厅', '线上办理', '1|3|11|13', 'SYSTEM', 1),
('云厅', '线下办理', '1|3|11', 'SYSTEM', 1);

-- 按地市配置（杭州市）
INSERT INTO UCMS_SCOPE_CONFIG (city, allowed_cert_types, created_by, status) VALUES
('杭州市', '1|3|11|13|14|15', 'SYSTEM', 1);

-- 按机构配置（杭州电信营业厅）
INSERT INTO UCMS_SCOPE_CONFIG (institution, allowed_cert_types, created_by, status) VALUES
('杭州电信营业厅', '1|3', 'SYSTEM', 1);

-- 全局默认配置（所有字段为空）
INSERT INTO UCMS_SCOPE_CONFIG (allowed_cert_types, created_by, status) VALUES
('1|3|11|13|14|15|16', 'SYSTEM', 1);

-- 5. 插入校验规则配置数据
-- 5.1 插入校验规则配置主表
-- 身份证件号码长度校验
INSERT INTO UCMS_VALIDATION_RULE_CONFIG (business_type, business_code, allowed_cert_types, created_by, status) VALUES
('证件号码校验', 'ID_CARD_LENGTH_CHECK', '1', 'SYSTEM', 1);

-- 护照号码长度校验
INSERT INTO UCMS_VALIDATION_RULE_CONFIG (business_type, business_code, allowed_cert_types, created_by, status) VALUES
('证件号码校验', 'PASSPORT_LENGTH_CHECK', '3', 'SYSTEM', 1);

-- 证件有效期校验
INSERT INTO UCMS_VALIDATION_RULE_CONFIG (business_type, business_code, allowed_cert_types, created_by, status) VALUES
('证件有效期校验', 'VALIDITY_CHECK', '1|3|11', 'SYSTEM', 1);

-- 年龄限制校验
INSERT INTO UCMS_VALIDATION_RULE_CONFIG (business_type, business_code, allowed_cert_types, created_by, status) VALUES
('年龄限制校验', 'AGE_LIMIT_CHECK', '1|3', 'SYSTEM', 1);

-- 5.2 插入校验规则明细
-- 身份证件号码长度和格式校验规则明细
INSERT INTO UCMS_VALIDATION_RULE_ITEM (rule_id, field_name, validation_logic, validation_value, sort_order) VALUES
(1, 'cert_number', 'regex', '^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[\dXx]$', 1);

-- 护照号码长度校验规则明细
INSERT INTO UCMS_VALIDATION_RULE_ITEM (rule_id, field_name, validation_logic, validation_value, sort_order) VALUES
(2, 'cert_number', '>=', '8', 1),
(2, 'cert_number', '<=', '18', 2);

-- 证件有效期校验规则明细
INSERT INTO UCMS_VALIDATION_RULE_ITEM (rule_id, field_name, validation_logic, validation_value, sort_order) VALUES
(3, 'validity_end', '>', 'CURRENT_DATE', 1);

-- 年龄限制校验规则明细（18岁以上）
-- 注：18YEARS_AGO为特殊标记，表示18年前的日期
INSERT INTO UCMS_VALIDATION_RULE_ITEM (rule_id, field_name, validation_logic, validation_value, sort_order) VALUES
(4, 'birth_date', '<=', '2008-01-01', 1);

-- 注：以上数据为测试用示例数据，实际使用时需要根据业务需求调整
-- ============================================
-- UCMS 统一证件验证中台系统 - 基于IBOP证件校验规则生成的预置规则
-- ============================================

-- 4. 插入基于IBOP的校验规则配置数据

-- 4.1 身份证件号码格式校验（规则ID：11，用于补充）
INSERT INTO UCMS_VALIDATION_RULE_CONFIG (business_type, business_code, allowed_cert_types, created_by, status) VALUES
('证件号码格式校验', 'ID_CARD_FORMAT_REGEX', '1', 'SYSTEM', 1);

-- 身份证件号码格式校验规则明细（规则ID：11）
INSERT INTO UCMS_VALIDATION_RULE_ITEM (rule_id, field_name, validation_logic, validation_value, sort_order) VALUES
(11, 'cert_number', 'regex', '^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[0-1])\d{3}[\dXx]$', 1);

-- 4.2 香港身份证校验规则（规则ID：21）
INSERT INTO UCMS_VALIDATION_RULE_CONFIG (business_type, business_code, allowed_cert_types, created_by, status) VALUES
('香港身份证校验', 'HK_ID_CARD_CHECK', '15', 'SYSTEM', 1);

-- 香港身份证校验规则明细
INSERT INTO UCMS_VALIDATION_RULE_ITEM (rule_id, field_name, validation_logic, validation_value, sort_order) VALUES
(21, 'cert_number', 'regex', '^[a-zA-Z]?[a-zA-Z]{1}\d{6}\(\)[a-z0-9A-Z]{1}\)$', 1);

-- 4.3 澳门身份证校验规则（规则ID：22）
INSERT INTO UCMS_VALIDATION_RULE_CONFIG (business_type, business_code, allowed_cert_types, created_by, status) VALUES
('澳门身份证校验', 'MACAU_ID_CARD_CHECK', '16', 'SYSTEM', 1);

-- 澳门身份证校验规则明细（支持新旧两种格式）
INSERT INTO UCMS_VALIDATION_RULE_ITEM (rule_id, field_name, validation_logic, validation_value, sort_order) VALUES
(22, 'cert_number', 'regex', '^\d{1}[\/]\d{6}[\/]\d{1}$|^\d{7}\(\)\d{1}\)$', 1);

-- 4.4 港澳居民来往内地通行证校验规则（规则ID：23）
INSERT INTO UCMS_VALIDATION_RULE_CONFIG (business_type, business_code, allowed_cert_types, created_by, status) VALUES
('港澳居民来往内地通行证校验', 'HK_MACAU_PASS_CHECK', '33', 'SYSTEM', 1);

-- 港澳居民来往内地通行证校验规则明细
INSERT INTO UCMS_VALIDATION_RULE_ITEM (rule_id, field_name, validation_logic, validation_value, sort_order) VALUES
(23, 'cert_number', 'regex', '^(H|M)[0-9]{0,8}$', 1);

-- 4.5 台湾居民来往大陆通行证校验规则（规则ID：24）
INSERT INTO UCMS_VALIDATION_RULE_CONFIG (business_type, business_code, allowed_cert_types, created_by, status) VALUES
('台湾居民来往大陆通行证校验', 'TW_PASS_CHECK', '13', 'SYSTEM', 1);

-- 台湾居民来往大陆通行证校验规则明细
INSERT INTO UCMS_VALIDATION_RULE_ITEM (rule_id, field_name, validation_logic, validation_value, sort_order) VALUES
(24, 'cert_number', 'regex', '^[0-9]{8}$', 1);

-- 4.6 客户姓名校验规则（规则ID：25）
INSERT INTO UCMS_VALIDATION_RULE_CONFIG (business_type, business_code, allowed_cert_types, created_by, status) VALUES
('客户姓名校验', 'CUST_NAME_CHECK', '1|2|3|11|12|13|22|23|33', 'SYSTEM', 1);

-- 客户姓名校验规则明细（身份证、军官证只允许中文和点号；其他证件允许中文、英文、数字和点号）
INSERT INTO UCMS_VALIDATION_RULE_ITEM (rule_id, field_name, validation_logic, validation_value, sort_order) VALUES
(25, 'name', 'regex', '^[\u4e00-\u9fa5\uF900-\uFA2D·\.]*$', 1),
(25, 'name', 'regex', '^[A-Za-z0-9·\.]*$', 2);

-- 4.7 地址校验规则（规则ID：26）
INSERT INTO UCMS_VALIDATION_RULE_CONFIG (business_type, business_code, allowed_cert_types, created_by, status) VALUES
('地址校验', 'ADDRESS_CHECK', '1|2|11|12|13|22|23|33', 'SYSTEM', 1);

-- 地址校验规则明细（身份证、军官证至少8个汉字；其他证件至少6个汉字）
INSERT INTO UCMS_VALIDATION_RULE_ITEM (rule_id, field_name, validation_logic, validation_value, sort_order) VALUES
(26, 'address', '>=', '8', 1),
(26, 'address', '>=', '6', 2);

-- 注：以上规则数据基于IBOP证件校验规则.js文件生成
--     业务类型1,2,3: 仅身份证、军官证、护照要求8个以上汉字的地址
--     业务类型11,12,13,22,23,33: 其他证件要求6个以上汉字的地址
-- 证件类型1,2: 姓名只允许中文字符和点号
--     证件类型3,11,12,13,22,23,33: 姓名允许中文、英文字母、数字、点号
