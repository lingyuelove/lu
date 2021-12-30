package com.luxuryadmin.mapper.shp;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.shp.ShpShop;
import com.luxuryadmin.entity.shp.ShpShopNumber;
import com.luxuryadmin.entity.shp.ShpUserNumber;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author monkey king
 */
@Mapper
public interface ShpShopNumberMapper extends BaseMapper {

    /**
     * 获取最后一个编号;
     *
     * @return
     */
    Integer getLastShopNumber();

    /**
     * 获取比当前id大的ShpUserNumber
     *
     * @param id
     * @return
     */
    ShpShopNumber getShpShopNumberOverId(int id);

    /**
     * 查找是否存在此编码;如果存在请根据编码状态进行使用;<br/>
     * 编号状态：0:未使用；1:已使用; 2:已弃用;
     *
     * @param shopNumber
     * @return
     */
    ShpShopNumber existsShpShopNumber(String shopNumber);
}