package com.luxuryadmin.service.pro.impl;

import com.luxuryadmin.mapper.pro.ProQualityMapper;
import com.luxuryadmin.service.shp.ProQualityService;
import com.luxuryadmin.vo.pro.VoProQuality;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author monkey king
 * @date 2020-05-27 18:03:26
 */
@Slf4j
@Service
public class ProQualityServiceImpl implements ProQualityService {

    @Resource
    private ProQualityMapper proQualityMapper;

    @Override
    public List<VoProQuality> listProQuality() {
        return proQualityMapper.listVoProQuality();
    }
}
