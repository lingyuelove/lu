package com.luxuryadmin.service.pro.impl;

import com.luxuryadmin.mapper.pro.ProSaleChannelMapper;
import com.luxuryadmin.service.pro.ProSaleChannelService;
import com.luxuryadmin.vo.pro.VoProSaleChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品销售渠道
 *
 * @author monkey king
 * @date 2020-06-03 22:42:38
 */
@Slf4j
@Service
public class ProSaleChannelServiceImpl implements ProSaleChannelService {

    @Resource
    private ProSaleChannelMapper proSaleChannelMapper;

    @Override
    public List<VoProSaleChannel> listProSaleChannel(int shopId) {
        return proSaleChannelMapper.listProSaleChannel(shopId);
    }

    @Override
    public int initProSaleChannelByShopIdAndUserId(int shopId, int userId) {
        return proSaleChannelMapper.initProSaleChannelByShopIdAndUserId(shopId, userId);
    }

    @Override
    public void deleteProSaleChannelByShopId(int shopId) {
        proSaleChannelMapper.deleteProSaleChannelByShopId(shopId);
    }
}
