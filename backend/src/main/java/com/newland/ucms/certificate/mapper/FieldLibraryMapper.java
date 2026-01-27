package com.newland.ucms.certificate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newland.ucms.certificate.entity.FieldLibrary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 字段库表Mapper接口
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@Mapper
public interface FieldLibraryMapper extends BaseMapper<FieldLibrary> {

  /**
   * 查询公共字段列表
   *
   * @return 公共字段列表
   */
  @Select("SELECT * FROM UCMS_FIELD_LIBRARY WHERE field_type = 'PUBLIC' AND status = 1 AND is_deleted = 0 ORDER BY id")
  List<FieldLibrary> selectPublicFields();

  /**
   * 根据字段类型查询字段列表
   *
   * @param fieldType 字段类型（PUBLIC/CUSTOM）
   * @return 字段列表
   */
  @Select("SELECT * FROM UCMS_FIELD_LIBRARY WHERE field_type = #{fieldType} AND status = 1 AND is_deleted = 0 ORDER BY id")
  List<FieldLibrary> selectFieldsByType(@Param("fieldType") String fieldType);

  /**
   * 根据字段英文名称查询字段
   *
   * @param fieldNameEn 字段英文名称
   * @return 字段信息
   */
  @Select("SELECT * FROM UCMS_FIELD_LIBRARY WHERE field_name_en = #{fieldNameEn} AND is_deleted = 0 LIMIT 1")
  FieldLibrary selectByFieldNameEn(@Param("fieldNameEn") String fieldNameEn);
}
