package com.luxuryadmin.mapper.fin;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.vo.fin.VoSaleTop;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author monkey king
 * @date 2020-01-15 19:12:37
 */
@Mapper
public interface FinSaleTopMapper extends BaseMapper {

    /**
     * 获取销售排行榜;
     *
     * @param shopId
     * @param saleUserId
     * @param startDate
     * @param endDate
     * @param sortKey   排序关键字; saleOrderCount | salePrice | grossProfit
     * @param sort      asc | desc
     * @return
     */
    List<VoSaleTop> listSaleTopByShopId(
            @Param("shopId") int shopId,@Param("saleUserId") Integer saleUserId,
            @Param("startDate") Date startDate, @Param("endDate") Date endDate,
            @Param("sortKey") String sortKey, @Param("sort") String sort);

}
