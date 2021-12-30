package com.luxuryadmin.service.fin;

import com.luxuryadmin.entity.fin.FinSaleAnalysis;

import java.util.Date;

/**
 * 销售数据分析--业务逻辑层
 *
 * @author monkey king
 * @date 2020-01-15 16:01:16
 */
public interface FinSaleAnalysisService {

    /**
     * 根据店铺id,查询日期<br/>
     * 获取销售统计数据;
     *
     * @param shopId
     * @param startDate
     * @param endDate
     * @return
     */
    FinSaleAnalysis listFinSaleAnalyses(int shopId, Date startDate, Date endDate);
}
