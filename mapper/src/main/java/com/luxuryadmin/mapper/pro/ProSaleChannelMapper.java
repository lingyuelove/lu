package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.vo.pro.VoProSaleChannel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品销售渠道
 *
 * @author monkey king
 * @date 2020-06-03 22:35:08
 */
@Mapper
public interface ProSaleChannelMapper extends BaseMapper {

    /**
     * 获取店铺自己的销售途径
     *
     * @param shopId
     * @return
     */
    List<VoProSaleChannel> listProSaleChannel(int shopId);

    /**
     * 根据店铺id 初始化销售渠道;从模板中初始化
     *
     * @param shopId
     * @param userId
     * @return
     */
    int initProSaleChannelByShopIdAndUserId(@Param("shopId") int shopId, @Param("userId") int userId);

    /**
     * 删除店铺销售渠道
     *
     * @param shopId
     * @return
     */
    int deleteProSaleChannelByShopId(int shopId);

}
