package com.luxuryadmin.mapper.shp;

import com.luxuryadmin.entity.shp.ShpOrderDailyCount;
import com.luxuryadmin.vo.shp.VoShpInRepoProdDailyCount;
import com.luxuryadmin.vo.shp.VoShpOrderDailyCount;
import com.luxuryadmin.vo.shp.VoShpOrderDailyCountForMonth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface ShpOrderDailyCountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShpOrderDailyCount record);

    int insertSelective(ShpOrderDailyCount record);

    ShpOrderDailyCount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShpOrderDailyCount record);

    int updateByPrimaryKey(ShpOrderDailyCount record);

    int selectCountByCountDate(Date countDate);

    /**
     * 根据店铺ID和订单类型查询入库商品相关统计数据
     * @param shopId
     * @param countDate
     * @param orderType
     * @return
     */
    VoShpOrderDailyCount selectShpOrdDailyCountVoByShopIdAndOrderType(@Param("shopId") Integer shopId,@Param("countDate") Date countDate,
                                                                      @Param("orderType") String orderType);


    /**
     * 根据店铺ID和商品类型查询入库商品相关统计数据
     * @param shopId
     * @param fkProAttributeCode
     * @return
     */
    VoShpInRepoProdDailyCount selectShpInRepoProdDailyCountVoByShopIdAndProdType(@Param("shopId") Integer shopId,
                                 @Param("countDate") Date countDate,@Param("fkProAttributeCode") Integer fkProAttributeCode);

    /**
     * 根据统计日期查询有统计数据的店铺ID列表
     * @param countDate
     */
    List<Integer> queryShpShopIdListByCountDate(Date countDate);

    /**
     * 根据店铺ID和统计日期查询统计信息
     * @param shopId
     * @param countDate
     * @return
     */
    ShpOrderDailyCount selectByShopIdAndCountDate(@Param("shopId") Integer shopId, @Param("countDate") Date countDate);


    /**
     * 根据店铺ID和统计日期查询上个月统计信息
     * @param shopId
     * @param countMonth
     * @param countYear
     * @return
     */
    VoShpOrderDailyCountForMonth selectByShopIdAndCountDateForMonth(@Param("shopId") Integer shopId, @Param("countMonth") Integer countMonth, @Param("countYear") Integer countYear);
}