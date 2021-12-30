package com.luxuryadmin.service.fin;

import com.luxuryadmin.vo.fin.VoSaleTop;

import java.util.Date;
import java.util.List;

/**
 * 销售排行榜
 *
 * @author monkey king
 * @date 2020-01-15 20:28:20
 */
public interface FinSaleTopService {

    /**
     * 获取销售排行榜;
     *
     * @param shopId
     * @param startDate
     * @param endDate
     * @param sortKey   排序关键字; saleOrderCount | salePrice | grossProfit
     * @param sort      asc | desc
     * @return
     */
    List<VoSaleTop> listSaleTopByShopId(
            int shopId, Date startDate, Date endDate,
            String sortKey, String sort);

    /**
     * 获取排行榜中的具体某一个人
     *
     * @param shopId
     * @param saleUserId 销售人员id; 如果是查询排行榜则填写null, 如果查询具体某个销售人员, 请填写该人员的userId
     * @param startDate
     * @param endDate
     * @return
     */
    List<VoSaleTop> listSaleTopByShopId(
            int shopId, Integer saleUserId, Date startDate, Date endDate);
}
