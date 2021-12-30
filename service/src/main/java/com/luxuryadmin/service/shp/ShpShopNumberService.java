package com.luxuryadmin.service.shp;

import com.luxuryadmin.entity.shp.ShpShopNumber;

import java.util.List;

/**
 * 商铺编号随机号码池;<br/>
 * 商铺编号在此业务中获取;
 *
 * @author monkey king
 * @date 2019-12-19 16:37:38
 */
public interface ShpShopNumberService {

    /**
     * 批量插入新生成未使用的随机用户编号;<br/>
     * 增加号码池容量;
     *
     * @param list
     * @return
     */
    int saveBatchNewShopNumber(List<ShpShopNumber> list);

    /**
     * 生成此号段的所有号码并随机分布;<br/>
     * [startNumber,endNumber]<br/>
     * 例如[1000,9999], 生成的号码包括1000和9999; 总共10000个;<br/>
     *
     * @param startNumber 开始号码;
     * @param endNumber   结束号码;
     * @return
     */
    int generateShopRandomNumber(int startNumber, int endNumber);

    /**
     * 获取最后一个编号;
     *
     * @return
     */
    Integer getLastShopNumber();

    /**
     * 获取比当前id大的ShpUserNumber
     *
     * @return
     */
    ShpShopNumber getShpShopNumberOverId();

    /**
     * 更新用户实体;
     *
     * @param shpShopNumber
     * @return
     */
    int updateShpShopNumber(ShpShopNumber shpShopNumber);

    /**
     * 店铺已使用该店铺编号;更新该记录;
     *
     * @param shopId
     * @param shpShopNumber
     * @return
     */
    int usedShpShopNumber(int shopId, ShpShopNumber shpShopNumber);

    /**
     * 使用该店铺编码
     *
     * @param shopId        使用该店铺编码的店铺id
     * @param newShopNumber 新店铺编码
     */
    void useShopNumber(int shopId, String newShopNumber);

    /**
     * 查找该店铺编码是否被使用过
     * 编号状态：0:未使用；1:已使用; 2:已弃用;
     *
     * @param shopNumber
     * @return
     */
    ShpShopNumber existsShpShopNumber(String shopNumber);

}
