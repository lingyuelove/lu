package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.pro.ProShare;
import com.luxuryadmin.param.pro.ParamShareProductQuery;
import com.luxuryadmin.param.pro.ParamUnionAccess;
import com.luxuryadmin.vo.pro.VoShareList;
import com.luxuryadmin.vo.pro.VoShareProduct;
import com.luxuryadmin.vo.pro.VoShareSearchList;
import com.luxuryadmin.vo.pro.VoUnionAccess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分享商品
 *
 * @author monkey king
 * @date 2020-05-27 15:26:29
 */
@Mapper
public interface ProShareMapper extends BaseMapper {

    /**
     * 根据店铺编号,用户编号和分享批号来获取分享的产品id
     *
     * @param shareProduct
     * @return
     */
    VoShareProduct getProIdsByShareBatch(ParamShareProductQuery shareProduct);

    /**
     * 删除店铺商品分享记录
     *
     * @param shopId
     * @return
     */
    int deleteProShareByShopId(int shopId);

    Integer listProShareByShopId(Integer fkShpShopId);

    /**
     * 获取小程序访客记录
     *
     * @param shopId
     * @return
     */
    List<VoShareList> getShareByList(int shopId);

    /**
     * 获取分享的商品
     *
     * @param shopId
     * @param tempId
     * @return
     */
    List<VoShareSearchList> getShareProductForTempId(@Param("shopId") int shopId, @Param("tempId") Integer tempId);

    /**
     * 更新只删除状态
     *
     * @param shareId
     */
    void updateToDeleteState(int shareId);

    /**
     * 根据分享批号获取详情
     *
     * @param shareBatch
     * @return
     */
    ProShare getShareForShareBatch(String shareBatch);

    /**
     * 获取商家联盟
     *
     * @param paramShareUser
     * @return
     */
    List<VoUnionAccess> listUnionShareUser(ParamUnionAccess paramShareUser);

    /**
     * 获取大于某个日期的访问人次
     * @param paramShareUser
     * @return
     */
    int countAccessUserCount(ParamUnionAccess paramShareUser);

    /**
     * 获取大于某个日期的访问次数
     * @param paramShareUser
     * @return
     */
    int countAccessCount(ParamUnionAccess paramShareUser);
}
