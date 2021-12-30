package com.luxuryadmin.service.data;

import com.github.pagehelper.Page;
import com.luxuryadmin.param.data.ParamDataSaleRankQuery;
import com.luxuryadmin.vo.data.VoSaleRank;
import com.luxuryadmin.vo.data.VoSaleRankHomePageData;

import java.util.List;

/**
 * @Description: TODO
 * @author: walkingPotato
 * @date: 2020-07-29 18:30
 * @email: dingxin_hz@163.com
 * @version: V1.0
 */
public interface OpDataSaleRankService {

    /**
     * 根据参数查询销售排行榜首页数据
     * @param paramDataSaleRankQuery
     * @return
     */
    VoSaleRankHomePageData loadSaleRankHomePageData(ParamDataSaleRankQuery paramDataSaleRankQuery,Page pag);

    /**
     * 分页查询【销售排行榜】记录
     * @param paramDataSaleRankQuery
     * @return
     */
    List<VoSaleRank> listSaleRank(ParamDataSaleRankQuery paramDataSaleRankQuery, Page page);
}
