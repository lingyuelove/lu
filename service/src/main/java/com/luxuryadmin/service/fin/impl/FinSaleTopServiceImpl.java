package com.luxuryadmin.service.fin.impl;

import com.luxuryadmin.mapper.fin.FinSaleTopMapper;
import com.luxuryadmin.service.fin.FinSaleTopService;
import com.luxuryadmin.vo.fin.VoSaleTop;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author monkey king
 * @date 2020-01-15 20:29:20
 */
@Slf4j
@Service
public class FinSaleTopServiceImpl implements FinSaleTopService {

    @Resource
    private FinSaleTopMapper finSaleTopMapper;

    @Override
    public List<VoSaleTop> listSaleTopByShopId(
            int shopId, Date startDate, Date endDate, String sortKey, String sort) {
        return finSaleTopMapper.listSaleTopByShopId(shopId, null, startDate, endDate, sortKey, sort);
    }

    @Override
    public List<VoSaleTop> listSaleTopByShopId(int shopId, Integer saleUserId, Date startDate, Date endDate) {
        return finSaleTopMapper.listSaleTopByShopId(shopId, saleUserId, startDate, endDate, null, null);
    }
}
