package com.luxuryadmin.mapper.stat;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.stat.StatShop;
import com.luxuryadmin.vo.stat.VoStateShopRank;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StatShopMapper extends BaseMapper {

    /**
     * 根据店铺ID查询统计数据
     * @param shopId
     * @return
     */
    StatShop selectStatShopByShopId(Integer shopId);

    /**
     *
     * @param rankFieldDb
     * @return
     */
    List<VoStateShopRank> selectStatDataShopRank(String rankFieldDb);
}