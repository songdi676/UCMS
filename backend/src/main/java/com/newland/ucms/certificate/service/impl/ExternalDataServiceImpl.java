package com.newland.ucms.certificate.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newland.ucms.certificate.config.ExternalApiConfig;
import com.newland.ucms.certificate.dto.BusinessChannelDTO;
import com.newland.ucms.certificate.dto.ChannelSubTypeDTO;
import com.newland.ucms.certificate.dto.CityDTO;
import com.newland.ucms.certificate.dto.DistrictDTO;
import com.newland.ucms.certificate.dto.InstitutionDTO;
import com.newland.ucms.certificate.service.ExternalDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

/**
 * 外部数据集成服务实现
 *
 * @author UCMS Team
 * @since 2026-01-27
 */
@Slf4j
@Service
public class ExternalDataServiceImpl implements ExternalDataService {

  @Autowired
  private ExternalApiConfig externalApiConfig;

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  @Cacheable(value = "businessChannels", key = "'all'")
  public List<BusinessChannelDTO> getBusinessChannels() {
    log.info("调用外部API获取办理通路列表，URL：{}", externalApiConfig.getBusinessChannelUrl());
    try {
      String response = restTemplate.getForObject(externalApiConfig.getBusinessChannelUrl(), String.class);
      List<BusinessChannelDTO> channels = objectMapper.readValue(response,
          new TypeReference<List<BusinessChannelDTO>>() {});
      log.info("成功获取办理通路列表，数量：{}", channels.size());
      return channels;
    } catch (Exception e) {
      log.error("获取办理通路列表失败", e);
      return Collections.emptyList();
    }
  }

  @Override
  @Cacheable(value = "channelSubTypes", key = "'all'")
  public List<ChannelSubTypeDTO> getChannelSubTypes() {
    log.info("调用外部API获取渠道小类列表，URL：{}", externalApiConfig.getChannelSubTypeUrl());
    try {
      String response = restTemplate.getForObject(externalApiConfig.getChannelSubTypeUrl(), String.class);
      List<ChannelSubTypeDTO> subTypes = objectMapper.readValue(response,
          new TypeReference<List<ChannelSubTypeDTO>>() {});
      log.info("成功获取渠道小类列表，数量：{}", subTypes.size());
      return subTypes;
    } catch (Exception e) {
      log.error("获取渠道小类列表失败", e);
      return Collections.emptyList();
    }
  }

  @Override
  @Cacheable(value = "cities", key = "'all'")
  public List<CityDTO> getCities() {
    log.info("调用外部API获取地市列表，URL：{}", externalApiConfig.getCityUrl());
    try {
      String response = restTemplate.getForObject(externalApiConfig.getCityUrl(), String.class);
      List<CityDTO> cities = objectMapper.readValue(response, new TypeReference<List<CityDTO>>() {});
      log.info("成功获取地市列表，数量：{}", cities.size());
      return cities;
    } catch (Exception e) {
      log.error("获取地市列表失败", e);
      return Collections.emptyList();
    }
  }

  @Override
  @Cacheable(value = "districts", key = "#cityCode")
  public List<DistrictDTO> getDistricts(String cityCode) {
    log.info("调用外部API获取区县列表，地市代码：{}，URL：{}", cityCode, externalApiConfig.getDistrictUrl());
    try {
      String response = restTemplate.getForObject(
          externalApiConfig.getDistrictUrl() + "?cityCode=" + cityCode,
          String.class);
      List<DistrictDTO> districts = objectMapper.readValue(response,
          new TypeReference<List<DistrictDTO>>() {});
      log.info("成功获取区县列表，数量：{}", districts.size());
      return districts;
    } catch (Exception e) {
      log.error("获取区县列表失败", e);
      return Collections.emptyList();
    }
  }

  @Override
  @Cacheable(value = "institutions", key = "#cityCode + '_' + #districtCode")
  public List<InstitutionDTO> getInstitutions(String cityCode, String districtCode) {
    log.info("调用外部API获取机构列表，地市：{}，区县：{}，URL：{}",
        cityCode, districtCode, externalApiConfig.getInstitutionUrl());
    try {
      String response = restTemplate.getForObject(
          externalApiConfig.getInstitutionUrl() + "?cityCode=" + cityCode + "&districtCode=" + districtCode,
          String.class);
      List<InstitutionDTO> institutions = objectMapper.readValue(response,
          new TypeReference<List<InstitutionDTO>>() {});
      log.info("成功获取机构列表，数量：{}", institutions.size());
      return institutions;
    } catch (Exception e) {
      log.error("获取机构列表失败", e);
      return Collections.emptyList();
    }
  }

  @Override
  @CacheEvict(value = {"businessChannels", "channelSubTypes", "cities", "districts", "institutions"},
              allEntries = true)
  public void clearCache() {
    log.info("清除所有外部数据缓存");
  }
}
