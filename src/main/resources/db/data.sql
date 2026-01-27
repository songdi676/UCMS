-- ============================================
-- UCMS 统一证件验证中台系统 - 初始化数据
-- ============================================

-- 1. 插入公共字段库数据
INSERT INTO UCMS_PUBLIC_FIELD_LIBRARY (field_name, field_name_en) VALUES
('姓名', 'name'),
('民族', 'nation'),
('性别', 'gender'),
('证件号码', 'cert_number'),
('期限起始', 'validity_start'),
('期限终止', 'validity_end'),
('出生日期', 'birth_date'),
('签发机关', 'issuing_authority'),
('地址', 'address');

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

-- 注：证件字段配置（必填字段）将在Task 3中完善
-- 目前仅插入证件类型主表数据，字段配置和校验规则后续补充
