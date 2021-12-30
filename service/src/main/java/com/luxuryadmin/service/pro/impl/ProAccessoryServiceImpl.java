package com.luxuryadmin.service.pro.impl;

import com.luxuryadmin.mapper.pro.ProAccessoryMapper;
import com.luxuryadmin.service.pro.ProAccessoryService;
import com.luxuryadmin.vo.pro.VoProAccessory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author monkey king
 * @date 2020-05-28 11:02:38
 */
@Slf4j
@Service
public class ProAccessoryServiceImpl implements ProAccessoryService {

    @Resource
    private ProAccessoryMapper proAccessoryMapper;

    @Override
    public List<VoProAccessory> listProAccessoryByShopId(int shopId) {
        return proAccessoryMapper.listProAccessoryByShopId(shopId);
    }

    @Override
    public int deleteProAccessoryById(int id) {
        return proAccessoryMapper.deleteObject(id);
    }

    @Override
    public int initProAccessoryByShopIdAndUserId(int shopId, int userId) {
        return proAccessoryMapper.initProAccessoryByShopIdAndUserId(shopId, userId);
    }

    @Override
    public void deleteProAccessoryByShopId(int shopId) {
        proAccessoryMapper.deleteProAccessoryByShopId(shopId);
    }
}
