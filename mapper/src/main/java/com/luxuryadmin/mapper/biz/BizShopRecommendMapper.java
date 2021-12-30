package com.luxuryadmin.mapper.biz;
import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.param.biz.ParamLeaguerRecommendBySearch;
import com.luxuryadmin.param.biz.ParamRecommendAdminBySearch;
import com.luxuryadmin.vo.biz.VoLeaguerRecommend;
import com.luxuryadmin.vo.biz.VoRecommendAdminList;
import org.apache.ibatis.annotations.Mapper;
import com.luxuryadmin.entity.biz.BizShopRecommend;

import java.util.List;


/**
 *添加友商推荐 dao
 *@author zhangsai
 *@Date 2021-05-27 11:42:44
 */
@Mapper
public interface BizShopRecommendMapper extends BaseMapper {

    /**
     * 友商推荐列表
     * @return
     */
    List<VoLeaguerRecommend> getRecommendLeaguerList(Integer shopId);

    /**
     * 友商列表推荐
     * @return
     */
    List<VoLeaguerRecommend> getByShowRecommend(Integer shopId);

    /**
     * 后端集合显示
     * @param recommendAdminBySearch
     * @return
     */
    List<VoRecommendAdminList> getRecommendAdminPage(ParamRecommendAdminBySearch recommendAdminBySearch);


    BizShopRecommend getByShopId(Integer shopId);

    /**
     * 删除店铺关联删除
     * @param shopId
     */
    void deleteShopRecommendForShopId(Integer shopId);
}
