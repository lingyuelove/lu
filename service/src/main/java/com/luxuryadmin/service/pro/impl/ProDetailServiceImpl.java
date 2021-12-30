package com.luxuryadmin.service.pro.impl;

import com.luxuryadmin.entity.pro.ProDetail;
import com.luxuryadmin.mapper.pro.ProDetailMapper;
import com.luxuryadmin.service.pro.ProDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author monkey king
 * @date 2019-12-24 19:55:56
 */
@Slf4j
@Service
public class ProDetailServiceImpl implements ProDetailService {

    @Resource
    private ProDetailMapper proDetailMapper;

    @Override
    public int saveProDetail(ProDetail proDetail) {
        return proDetailMapper.saveObject(proDetail);
    }

    @Override
    public Integer getProDetailIdByShopIdAndBizId(int shopId, String bizId) {
        return proDetailMapper.getProDetailIdByShopIdAndBizId(shopId, bizId);
    }

    @Override
    public int updateProDetail(ProDetail proDetail) {
        return proDetailMapper.updateObject(proDetail);
    }

    @Override
    public String getUniqueCodeByShopIdBizId(int shopId, String bizId) {
        return proDetailMapper.getUniqueCodeByShopIdBizId(shopId, bizId);
    }

    @Override
    public int updateUniqueCodeByProId(int proId, String uniqueCode) {
        return proDetailMapper.updateUniqueCodeByProId(proId, uniqueCode);
    }

    @Override
    public void deleteProDetailByProIds(String ids) {
        proDetailMapper.deleteProDetailByProIds(ids);
    }

    @Override
    public ProDetail getProDetailByProId(int proId) {
        return  proDetailMapper.getProDetailByProId(proId);
    }

}
