package com.luxuryadmin.service.pro.impl;

import com.luxuryadmin.entity.pro.ProSource;
import com.luxuryadmin.mapper.pro.ProSourceMapper;
import com.luxuryadmin.service.pro.ProSourceService;
import com.luxuryadmin.vo.pro.VoProSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author monkey king
 * @date 2020-05-27 15:46:48
 */
@Slf4j
@Service
public class ProSourceServiceImpl implements ProSourceService {

    @Resource
    private ProSourceMapper proSourceMapper;

    @Override
    public List<VoProSource> listProSource(int shopId) {
        return proSourceMapper.listProSourceByShopId(shopId);
    }

    @Override
    public int initProSourceByShopIdAndUserId(int shopId, int userId) {
        return proSourceMapper.initProSourceByShopIdAndUserId(shopId, userId);
    }

    @Override
    public void deleteProSourceByShopId(int shopId) {
        proSourceMapper.deleteProSourceByShopId(shopId);
    }
}
