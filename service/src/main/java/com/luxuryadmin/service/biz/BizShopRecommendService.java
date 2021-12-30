package com.luxuryadmin.service.biz;

import com.luxuryadmin.param.biz.ParamLeaguerRecommendBySearch;
import com.luxuryadmin.param.biz.ParamRecommendAdminBySearch;
import com.luxuryadmin.param.biz.ParamShopRecommendAdd;
import com.luxuryadmin.vo.biz.VoLeaguerRecommend;
import com.luxuryadmin.vo.biz.VoLeaguerRecommendPage;
import com.luxuryadmin.vo.biz.VoRecommendAdminPage;

import java.util.List;

/**
 *添加友商推荐 service
 *@author zhangsai
 *@Date 2021-05-27 11:42:44
 */
public interface BizShopRecommendService {

    /**
     * 友商推荐列表
     * @param leaguerRecommendBySearch
     * @return
     */
    VoLeaguerRecommendPage getRecommendLeaguerList(ParamLeaguerRecommendBySearch leaguerRecommendBySearch);

    List<VoLeaguerRecommend> getByShowRecommend( Integer shopId);

    /**
     * 集合显示
     * @param recommendAdminBySearch
     * @return
     */
    VoRecommendAdminPage getRecommendAdminPage(ParamRecommendAdminBySearch recommendAdminBySearch);

    /**
     * 新增
     * @param shopRecommendAdd
     */
    void addShopRecommend(ParamShopRecommendAdd shopRecommendAdd);

    /**
     * 删除
     * @param id
     */
    void deleteShopRecommend(Integer id);

}
