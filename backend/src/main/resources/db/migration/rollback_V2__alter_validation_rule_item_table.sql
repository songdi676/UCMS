-- ============================================
-- UCMS 校验规则明细表扩展回滚脚本
-- ============================================

-- 存储过程：安全删除列（如果存在）
DROP PROCEDURE IF EXISTS drop_column_if_exists;

DELIMITER $$
CREATE PROCEDURE drop_column_if_exists(
  IN table_name VARCHAR(255),
  IN column_name VARCHAR(255)
)
BEGIN
  DECLARE column_exists INT;

  SELECT COUNT(*) INTO column_exists
  FROM information_schema.columns
  WHERE table_schema = DATABASE()
    AND table_name = table_name
    AND column_name = column_name;

  IF column_exists > 0 THEN
    SET @sql = CONCAT('ALTER TABLE ', table_name, ' DROP COLUMN ', column_name);
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END IF;
END$$
DELIMITER ;

-- 删除规则来源字段
CALL drop_column_if_exists('UCMS_VALIDATION_RULE_ITEM', 'rule_source');

-- 删除预定义规则ID字段
CALL drop_column_if_exists('UCMS_VALIDATION_RULE_ITEM', 'predefined_rule_id');

-- 删除错误消息字段
CALL drop_column_if_exists('UCMS_VALIDATION_RULE_ITEM', 'error_message');

-- 删除证件类型字段ID
CALL drop_column_if_exists('UCMS_VALIDATION_RULE_ITEM', 'certificate_type_field_id');

-- 清理存储过程
DROP PROCEDURE IF EXISTS drop_column_if_exists;
