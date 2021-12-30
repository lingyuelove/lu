package com.luxuryadmin.service.pro.impl;

import com.luxuryadmin.mapper.pro.ProAttributeMapper;
import com.luxuryadmin.service.pro.ProAttributeService;
import com.luxuryadmin.vo.pro.VoProAttribute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author monkey king
 * @date 2019-12-24 21:02:18
 */
@Slf4j
@Service
public class ProAttributeServiceImpl implements ProAttributeService {

    @Resource
    private ProAttributeMapper proAttributeMapper;

    @Override
    public List<VoProAttribute> listProAttributeByShopId(int shopId) {
        return proAttributeMapper.listProAttributeByShopId(shopId);
    }

    @Override
    public int initProAttributeByShopIdAndUserId(int shopId, int userId) {
        return proAttributeMapper.initProAttributeByShopIdAndUserId(shopId, userId);
    }

    @Override
    public void deleteProAttributeByShopId(int shopId) {
        proAttributeMapper.deleteProAttributeByShopId(shopId);
    }
}
