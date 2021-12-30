package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.pro.ProExpiredNotice;
import com.luxuryadmin.param.pro.ParamExpiredProductForMapperSearch;
import com.luxuryadmin.param.pro.ParamExpiredProductSearch;
import com.luxuryadmin.vo.pro.VoExpiredNoticeByList;
import com.luxuryadmin.vo.pro.VoExpiredProductByList;
import com.luxuryadmin.vo.pro.VoProAccessory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商品过期提醒表
 *
 * @author zhangSai
 * @date   2021/04/21 13:56:16
 */
@Mapper
public interface ProExpiredNoticeMapper extends BaseMapper {
    /**
     * 获取商品过期提醒集合显示
     * @param shopId
     * @return
     */
    List<VoExpiredNoticeByList> getExpiredListByShopId(Integer shopId);

    /**
     * 商品过期提醒列表
     * @param expiredProductSearch
     * @return
     */
    List<VoExpiredProductByList> getExpiredProductLists(@Param("expiredProductSearch")ParamExpiredProductForMapperSearch expiredProductSearch);

    ProExpiredNotice getByExpiredNotice(ProExpiredNotice expiredNotice);

    /**
     * 获取总价格和总数量
     * @param expiredProductSearch
     * @return
     */
    Map<String,Object> getExpiredProductTotalMoneyAndCount(@Param("expiredProductSearch")ParamExpiredProductForMapperSearch expiredProductSearch);
}
