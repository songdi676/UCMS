package com.newland.ucms.certificate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newland.ucms.certificate.entity.ValidationRuleItem;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 校验规则明细Mapper接口
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
public interface ValidationRuleItemMapper extends BaseMapper<ValidationRuleItem> {

  /**
   * 根据规则ID查询规则明细
   *
   * @param ruleId 规则ID
   * @return 规则明细列表
   */
  List<ValidationRuleItem> findByRuleId(@Param("ruleId") Long ruleId);
}
