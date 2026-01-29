-- ============================================
-- UCMS 证件类型字段配置表扩展回滚脚本
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

-- 删除字段作用域字段
CALL drop_column_if_exists('UCMS_CERTIFICATE_TYPE_FIELD', 'scope');

-- 清理存储过程
DROP PROCEDURE IF EXISTS drop_column_if_exists;
