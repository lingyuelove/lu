package com.luxuryadmin.service.pro.impl;

import com.luxuryadmin.entity.pro.ProClassify;
import com.luxuryadmin.mapper.pro.ProClassifyMapper;
import com.luxuryadmin.mapper.pro.ProClassifySubMapper;
import com.luxuryadmin.mapper.sys.SysEnumMapper;
import com.luxuryadmin.param.pro.ParamProClassifySubQuery;
import com.luxuryadmin.service.pro.ProClassifyService;
import com.luxuryadmin.vo.pro.VoProClassify;
import com.luxuryadmin.vo.pro.VoProClassifySub;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author monkey king
 * @date 2019-12-18 20:34:34
 */
@Slf4j
@Service
public class ProClassifyServiceImpl implements ProClassifyService {

    @Resource
    private ProClassifyMapper proClassifyMapper;

    @Resource
    private SysEnumMapper sysEnumMapper;

    @Override
    public List<VoProClassify> listProClassifyByState(int shopId, String state) {
        return proClassifyMapper.listProClassifyByState(shopId,state);
    }

    @Override
    public List<VoProClassify> listSysProClassifyByState(String state) {
        return sysEnumMapper.listSysProClassifyByState(state);
    }

    @Override
    public int initProClassifyByShopIdAndUserId(int shopId, int userId) {
        return proClassifyMapper.initProClassifyByShopIdAndUserId(shopId, userId);
    }

    @Override
    public List<VoProClassify> listLeaguerProClassifyByState(int leaguerShopId) {
        return proClassifyMapper.listLeaguerProClassify(leaguerShopId);
    }

    @Override
    public int updateProClassify(List<ProClassify> list, int shopId) {
        return proClassifyMapper.updateProClassify(list, shopId);
    }

    @Override
    public int deleteProClassifyByShopId(int shopId) {
        return proClassifyMapper.deleteProClassifyByShopId(shopId);
    }



}
