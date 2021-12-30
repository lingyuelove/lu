package com.luxuryadmin.mapper.fin;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.fin.FinSaleAnalysis;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author monkey king
 * @date 2020-01-14 20:26:40
 */
@Mapper
public interface FinSaleAnalysisMapper extends BaseMapper {

    /**
     * 根据店铺id,查询日期<br/>
     * 获取销售统计数据;
     *
     * @param shopId
     * @param startDate
     * @param endDate
     * @return
     */
    FinSaleAnalysis listFinSaleAnalyses(
            @Param("shopId") int shopId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
