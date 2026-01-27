package com.newland.ucms.certificate.service;

import com.newland.ucms.certificate.dto.BusinessChannelDTO;
import com.newland.ucms.certificate.dto.ChannelSubTypeDTO;
import com.newland.ucms.certificate.dto.CityDTO;
import com.newland.ucms.certificate.dto.DistrictDTO;
import com.newland.ucms.certificate.dto.InstitutionDTO;

import java.util.List;

/**
 * 外部数据集成服务接口
 * 用于从外部API获取办理通路、渠道小类、地市、区县、机构等数据
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
public interface ExternalDataService {

  /**
   * 获取办理通路列表
   *
   * @return 办理通路列表
   */
  List<BusinessChannelDTO> getBusinessChannels();

  /**
   * 获取渠道小类列表
   *
   * @return 渠道小类列表
   */
  List<ChannelSubTypeDTO> getChannelSubTypes();

  /**
   * 获取地市列表
   *
   * @return 地市列表
   */
  List<CityDTO> getCities();

  /**
   * 根据地市代码获取区县列表
   *
   * @param cityCode 地市代码
   * @return 区县列表
   */
  List<DistrictDTO> getDistricts(String cityCode);

  /**
   * 根据地市和区县获取机构列表
   *
   * @param cityCode    地市代码
   * @param districtCode 区县代码
   * @return 机构列表
   */
  List<InstitutionDTO> getInstitutions(String cityCode, String districtCode);

  /**
   * 清除缓存
   */
  void clearCache();
}
