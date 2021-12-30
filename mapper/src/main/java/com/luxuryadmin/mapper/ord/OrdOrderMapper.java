package com.luxuryadmin.mapper.ord;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.ord.OrdOrder;
import com.luxuryadmin.excel.ExVoOrder;
import com.luxuryadmin.param.data.ParamDataSaleRankQuery;
import com.luxuryadmin.param.fin.ParamSalaryDetailQuery;
import com.luxuryadmin.param.ord.ParamOrdForTempSearch;
import com.luxuryadmin.param.ord.ParamOrdOrder;
import com.luxuryadmin.param.ord.ParamOrdTypeUpFopApp;
import com.luxuryadmin.param.ord.ParamOrderQuery;
import com.luxuryadmin.param.pro.ParamProPageDeliver;
import com.luxuryadmin.param.pro.ParamProductOrOrderForDeleteSearch;
import com.luxuryadmin.vo.data.VoSaleRank;
import com.luxuryadmin.vo.data.VoSaleRankHomePageData;
import com.luxuryadmin.vo.fin.VoFinSalaryDetail;
import com.luxuryadmin.vo.ord.VoOrder;
import com.luxuryadmin.vo.ord.VoOrderForTemp;
import com.luxuryadmin.vo.ord.VoOrderLoad;
import com.luxuryadmin.vo.pro.VoOrderUserInfo;
import com.luxuryadmin.vo.pro.VoProDeliverByPage;
import com.luxuryadmin.vo.pro.VoProductOrOrderForDelete;
import com.luxuryadmin.vo.shp.VoEmployee;
import com.luxuryadmin.vo.stat.VoStatSaleProdData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author monkey king
 */
@Mapper
public interface OrdOrderMapper extends BaseMapper {

    /**
     * 加载订单; 根据条件筛选
     *
     * @param orderQuery
     * @return
     */
    List<VoOrderLoad> listOrderByCondition(ParamOrderQuery orderQuery);

    /**
     * 加载订单; 根据条件筛选--excel导出
     *
     * @param orderQuery
     * @return
     */
    List<ExVoOrder> listOrderByConditionExcelExport(ParamOrderQuery orderQuery);

    /**
     * 获得订单的销售总额和数量
     *
     * @param orderQuery
     * @return
     */
    VoSaleRankHomePageData getOrderNumAndPrice(ParamOrderQuery orderQuery);


    /**
     * 根据shopId和流水编号获取订单详情
     *
     * @param shopId
     * @param number
     * @return
     */
    VoOrderLoad getOrderDetailByNumber(@Param("shopId") int shopId, @Param("number") String number);

    /**
     * 条件查询订单
     *
     * @param paramOrdOrder
     * @return
     */
    List<VoOrder> listOrderByPara(ParamOrdOrder paramOrdOrder);

    /**
     * 根据id查询订单详情
     *
     * @param strParseInt
     * @return
     */
    VoOrder getOrdOrderInfo(Integer strParseInt);

    /**
     * 数据统计-销售排行
     *
     * @param paramDataSaleRankQuery
     * @return
     */
    List<VoSaleRank> selectSaleRankListByShopId(ParamDataSaleRankQuery paramDataSaleRankQuery);

    /**
     * 根据店铺ID和订单number获取订单详情
     *
     * @param params
     * @return
     */
    OrdOrder getOrdOrderDetail(Map<String, Object> params);

    /**
     * 根据【售后保障名称】查询被使用的数量
     *
     * @param orderParams
     * @return
     */
    Integer selectAfterSaleGuaranteeCountByName(Map<String, Object> orderParams);


    /**
     * 获取用户的各订单类型的销售总额、销售件数、毛利润总额
     *
     * @param salaryDetailQuery
     * @return
     */
    List<VoFinSalaryDetail> countSaleAndGrossProfitMoney(ParamSalaryDetailQuery salaryDetailQuery);

    /**
     * 获取用户的各订单类型的回收成本总额、回收件数、回收产生的利润
     *
     * @param salaryDetailQuery
     * @return
     */
    List<VoFinSalaryDetail> countRecycleCostAndProfitMoney(ParamSalaryDetailQuery salaryDetailQuery);

    /**
     * @param shopId
     * @param ordTypeName
     * @return
     */
    Integer selectOrdCountByTypeName(@Param("shopId") Integer shopId, @Param("ordTypeName") String ordTypeName);

    /**
     * 获取订单编号
     *
     * @param shopId
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    List<String> listOrderNumber(@Param("shopId") int shopId,
                                 @Param("startDateTime") String startDateTime,
                                 @Param("endDateTime") String endDateTime);


    /**
     * 获取商品id
     *
     * @param shopId
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    List<String> listProductId(@Param("shopId") int shopId,
                               @Param("startDateTime") String startDateTime,
                               @Param("endDateTime") String endDateTime);

    /**
     * 物理删除订单记录
     *
     * @param shopId
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    int deleteOrderByDateTime(@Param("shopId") int shopId,
                              @Param("startDateTime") String startDateTime,
                              @Param("endDateTime") String endDateTime);

    /**
     * 根据订单编号删除订单
     *
     * @param shopId
     * @param orderNumber
     * @return
     */
    int deleteOrderByOrderNumber(@Param("shopId") int shopId, @Param("orderNumber") String orderNumber);

    /**
     * 删除店铺订单(注销时调用)
     *
     * @param shopId
     * @return
     */
    int deleteOrderByShopId(int shopId);

    /**
     * TA回收产生的利润(不一定是本人所销售)
     *
     * @param shopId
     * @param recycleUserId
     * @param proAttr
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    Object countRecycleProfit(
            @Param("shopId") int shopId, @Param("recycleUserId") int recycleUserId,
            @Param("proAttr") String proAttr, @Param("startDateTime") String startDateTime,
            @Param("endDateTime") String endDateTime);

    /**
     * 根据订单编号和店铺id获取实体
     *
     * @param shopId
     * @param ordNumber
     * @return
     */
    OrdOrder getOrdOrderByShopIdAndOrdNumber(@Param("shopId") int shopId, @Param("ordNumber") String ordNumber);

    /**
     * 根据(多个)订单编号和店铺id获取(多个)实体
     *
     * @param shopId
     * @param ordNumbers
     * @return
     */
    List<OrdOrder> listOrdOrderByShopIdAndOrdNumber(
            @Param("shopId") int shopId, @Param("ordNumbers") String ordNumbers);

    /**
     * 根据店铺ID获取总的订单量
     *
     * @param shopId
     * @return
     */
    Integer getTotalOrderNumByShopId(Integer shopId);

    /**
     * 根据统计日期统计订单数
     *
     * @param countDate
     * @return
     */
    Integer countOrderNumByCountDate(Date countDate);

    /**
     * 根据统计日期统计订单销售额
     *
     * @param countDate
     * @return
     */
    BigDecimal countOrderAmountByCountDate(Date countDate);

    /**
     * 根据shopId统计订单销售额
     *
     * @param shopId
     * @return
     */
    BigDecimal countOrderAmountByShopId(Integer shopId);

    /**
     * 销售商品分类数据
     *
     * @return
     */
    List<VoStatSaleProdData> listStatSaleProdClassifyData();

    /**
     * 关联此商品的订单数量
     *
     * @param shopId
     * @param proId
     * @return
     */
    int countOrderByProId(@Param("shopId") int shopId, @Param("proId") int proId);

    /**
     * 临时仓订单page显示
     *
     * @param ordForTempSearch
     * @return
     */
    List<VoOrderForTemp> getOrderForTemp(@Param("ordForTempSearch") ParamOrdForTempSearch ordForTempSearch);

    /**
     * 获取临时仓开单数量
     *
     * @param shopId
     * @param productId
     * @param tempId
     * @return
     */
    Integer getOrderNumByTemp(@Param("shopId") Integer shopId, @Param("productId") Integer productId, @Param("tempId") Integer tempId);

    /**
     * 商品删除列表
     *
     * @param productOrOrderForDeleteSearch
     * @return
     */
    List<VoProductOrOrderForDelete> getOrderForDeleteList(ParamProductOrOrderForDeleteSearch productOrOrderForDeleteSearch);

    /**
     * 订单删除总数量
     *
     * @param productOrOrderForDeleteSearch
     * @return
     */
    Integer getOrderForDeleteCount(ParamProductOrOrderForDeleteSearch productOrOrderForDeleteSearch);

    /**
     * 获取最近一次的开单记录;
     *
     * @param shopId
     * @param userId 如果userId为null,则获取该店铺的最近一次记录
     * @return
     */
    VoOrder getLastOrderInfo(
            @Param("shopId") int shopId, @Param("userId") Integer userId);

    /**
     * 更新该店铺所有此订单类型改为修改之后的
     *
     * @param ordTypeUpFopApp
     */
    void updateOrderType(ParamOrdTypeUpFopApp ordTypeUpFopApp);

    /**
     * 获取已售订单的销售人员;用作与订单筛选;
     *
     * @param shopId
     * @return
     */
    List<VoEmployee> listSaleUserByShopId(int shopId);

    /**
     * 获取还没有初始化年化收益率的订单;只针对已开单(state=20)
     *
     * @return
     */
    List<VoOrderLoad> listNullYearRateOrder();

    /**
     * 批量更新年化收益率
     *
     * @param list
     * @return
     */
    int updateBatchYearRate(List<OrdOrder> list);

    /**
     * 获取发货信息列表
     * @param param
     * @return
     */
    List<VoProDeliverByPage> getProDeliverInfo(ParamProPageDeliver param);

    /**
     * 根据订单号查询订单信息
     *
     * @param orderNumber
     * @return
     */
    OrdOrder getOrderInfoByNumber(@Param("orderNumber") String orderNumber);

    /**
     * 根据店铺id获取开单人员简信息
     *
     * @param shopId
     * @return
     */
    List<VoOrderUserInfo> getFiltrateinfoByShopId(@Param("shopId") Integer shopId);

    /**
     * 根据id获取订单信息
     *
     * @param id
     * @return
     */
    OrdOrder getOrdOrderInfoById(@Param("id") Integer id);

    /**
     * 2.6.6发货列表
     *
     * @param ids
     * @param state
     * @return
     */
    List<VoProDeliverByPage> listProDeliverInfo(@Param("ids") List<Integer> ids,@Param("state") String state);

    /**
     * 获取代发货订单数量
     *
     * @param ids
     * @return
     */
    Integer getOrderSum(@Param("ids") List<Integer> ids);
}