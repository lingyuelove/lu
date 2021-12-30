package com.luxuryadmin.mapper.biz;
import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.param.biz.ParamShopSeeSearch;
import com.luxuryadmin.param.biz.ParamShopSeeSearchByCount;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import com.luxuryadmin.entity.biz.BizShopSee;
import org.apache.ibatis.annotations.Param;


/**
 *店铺查看次数表 dao
 *@author zhangsai
 *@Date 2021-05-27 11:42:44
 */
@Mapper
public interface BizShopSeeMapper  extends BaseMapper {

    /**
     * 搜索每日店铺查看次数表
     * @param shopSeeSearch
     * @return
     */
    BizShopSee getBySearch( @Param("shopSeeSearch")ParamShopSeeSearch shopSeeSearch);

    Integer getShopSeeCount(@Param("shopSeeSearchByCount")ParamShopSeeSearchByCount shopSeeSearchByCount);

    void deleteByShopId(Integer shopId);
}
