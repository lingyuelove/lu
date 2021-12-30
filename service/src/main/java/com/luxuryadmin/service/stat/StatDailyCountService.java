package com.luxuryadmin.service.stat;

import com.luxuryadmin.param.stat.ParamRegDataQuery;
import com.luxuryadmin.vo.stat.VoStatRegData;
import com.luxuryadmin.vo.stat.VoStateDataTotal;
import com.luxuryadmin.vo.stat.VoStateProdSellClassify;
import com.luxuryadmin.vo.stat.VoStateShopRank;

import java.util.Date;
import java.util.List;

/**
 * 运营数据统计Service
 */
public interface StatDailyCountService {

    /**
     * 每日统计运营数据
     * @param countDate
     */
    Integer dailyCountStat(Date countDate);

    /**
     * 根据统计日期获取【顶部汇总数据】
     * @param countDate
     * @return
     */
    VoStateDataTotal loadStatDataTotal(Date countDate);

    /**
     * 获取【店铺排行数据】
     * @param rankField
     * @return
     */
    List<VoStateShopRank> loadStatDataShopRank(String rankField);

    /**
     * 获取【商品销售分类】统计数据
     * @return
     */
    List<VoStateProdSellClassify> loadProdSellClassify(String showField);

    /**
     * 获取【注册】统计数据
     * @param paramRegDataQuery
     * @return
     */
    VoStatRegData loadRegData(ParamRegDataQuery paramRegDataQuery);
}
