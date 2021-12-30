package com.luxuryadmin.service.pro.impl;

import com.luxuryadmin.entity.pro.ProShareSeeUserAgency;
import com.luxuryadmin.mapper.pro.ProShareSeeUserAgencyMapper;
import com.luxuryadmin.service.pro.ProShareSeeUserAgencyService;
import com.luxuryadmin.vo.pro.VoProShareSeeUserAgency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author monkey king
 * @date 2021-08-28 17:54:26
 */
@Slf4j
@Service
public class ProShareSeeUserAgencyServiceImpl implements ProShareSeeUserAgencyService {


    @Resource
    private ProShareSeeUserAgencyMapper proShareSeeUserAgencyMapper;

    @Override
    public void saveObject(ProShareSeeUserAgency proShareSeeUserAgency) {
        proShareSeeUserAgencyMapper.saveObject(proShareSeeUserAgency);
    }

    @Override
    public boolean existsWxUserByOpenId(String type, String openId) {
        return proShareSeeUserAgencyMapper.existsWxUserByOpenId(type, openId) > 0;
    }


    @Override
    public boolean existsWxUserByPhone(String type, String phone) {
        return proShareSeeUserAgencyMapper.existsWxUserByPhone(type, phone) > 0;
    }

    @Override
    public List<VoProShareSeeUserAgency> listVoProShareSeeUserAgency(String userId) {
        return proShareSeeUserAgencyMapper.listVoProShareSeeUserAgency(userId);
    }

    @Override
    public ProShareSeeUserAgency getObjectByPhone(String type, String phone) {
        return proShareSeeUserAgencyMapper.getObjectByPhone(type,phone);
    }

    @Override
    public void updateObject(ProShareSeeUserAgency proShareSeeUserAgency) {
        proShareSeeUserAgencyMapper.updateObject(proShareSeeUserAgency);
    }
}
