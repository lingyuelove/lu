package com.luxuryadmin.mapper.fin;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.fin.FinBill;
import com.luxuryadmin.param.fin.ParamBillSearch;
import com.luxuryadmin.vo.fin.VoBillDetailByApp;
import com.luxuryadmin.vo.fin.VoBillPageByApp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * 帐单表
 *
 * @author zhangSai
 * @date   2021/04/23 14:19:52
 */
@Mapper
public interface FinBillMapper extends BaseMapper {

    /**
     * 帐单列表集合显示
     * @param shopId
     * @return
     */
    List<VoBillPageByApp> getBillPageByApp(@Param("shopId")Integer shopId,@Param("state")String state);

    /**
     *  商品总成本价格
     * @param shopId
     * @param attributeCode
     * @return
     */
    BigDecimal getProductPrice( @Param("shopId")Integer shopId,@Param("attributeCode") String attributeCode);

    /**
     * 订单收益的计算(商品售卖（售卖价-成本价）和服务收支（维修价-维修成本）)20:商品销售; 30:维修服务（服务利润）;60:质押商品
     * @param shopId
     * @param
     * @param startTime
     * @param endTime
     * @return
     */
    BigDecimal getMakeMoney(@Param("shopId")Integer shopId, @Param("startTime") String startTime, @Param("endTime") String endTime,@Param("state") String state,@Param("fundType")String fundType,@Param("billId")Integer billId);


    /**
     * 计算价格 10 入库商品；;40薪资;50:其他收支;60:质押商品; 30:维修服务（服务利润）
     * @param shopId
     * @param startTime
     * @param endTime
     * @return
     */
    BigDecimal getMoneyForType(@Param("shopId")Integer shopId, @Param("startTime") String startTime, @Param("endTime") String endTime,@Param("state") String state,@Param("fundType")String fundType,@Param("billId")Integer billId);
}