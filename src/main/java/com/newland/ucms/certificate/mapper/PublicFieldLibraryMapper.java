package com.newland.ucms.certificate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newland.ucms.certificate.entity.PublicFieldLibrary;
import java.util.List;

/**
 * 公共字段库Mapper接口
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
public interface PublicFieldLibraryMapper extends BaseMapper<PublicFieldLibrary> {

  /**
   * 查询所有公共字段
   *
   * @return 公共字段列表
   */
  List<PublicFieldLibrary> selectList();
}
