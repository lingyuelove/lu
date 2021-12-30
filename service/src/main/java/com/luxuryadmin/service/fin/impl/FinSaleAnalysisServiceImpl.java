package com.luxuryadmin.service.fin.impl;

import com.luxuryadmin.entity.fin.FinSaleAnalysis;
import com.luxuryadmin.mapper.fin.FinSaleAnalysisMapper;
import com.luxuryadmin.service.fin.FinSaleAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author monkey king
 * @date 2020-01-15 16:03:33
 */
@Slf4j
@Service
public class FinSaleAnalysisServiceImpl implements FinSaleAnalysisService {

    @Resource
    private FinSaleAnalysisMapper finSaleAnalysisMapper;

    @Override
    public FinSaleAnalysis listFinSaleAnalyses(int shopId, Date startDate, Date endDate) {
        return finSaleAnalysisMapper.listFinSaleAnalyses(shopId, startDate, endDate);
    }
}
