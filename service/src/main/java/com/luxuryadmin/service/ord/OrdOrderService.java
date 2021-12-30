package com.luxuryadmin.service.ord;

import com.luxuryadmin.common.base.BasicParam;
import com.luxuryadmin.entity.ord.OrdAddress;
import com.luxuryadmin.entity.ord.OrdOrder;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.excel.ExVoOrder;
import com.luxuryadmin.param.fin.ParamSalaryDetailQuery;
import com.luxuryadmin.param.ord.*;
import com.luxuryadmin.param.pro.ParamProPageDeliver;
import com.luxuryadmin.vo.data.VoSaleRankHomePageData;
import com.luxuryadmin.vo.fin.VoSalaryOrdType;
import com.luxuryadmin.vo.ord.*;
import com.luxuryadmin.vo.pro.VoOrderUserInfo;
import com.luxuryadmin.vo.pro.VoProDeliverByPage;
import com.luxuryadmin.vo.shp.VoEmployee;
import io.swagger.models.auth.In;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 订单模块业务逻辑
 *
 * @author monkey king
 * @date 2019-12-25 21:44:25
 */
public interface OrdOrderService {


    /**
     * 加载订单; 根据条件筛选
     *
     * @param orderQuery
     * @return
     */
    List<VoOrderLoad> listOrderByCondition(ParamOrderQuery orderQuery);

    /**
     * 加载订单; 根据条件筛选-excel导出
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
     * 添加OrdOrder
     *
     * @param ordOrder
     * @return
     */
    int saveOrdOrder(OrdOrder ordOrder);

    /**
     * 确认开单; 更新商品;添加开单记录
     *
     * @param pro
     * @param order
     * @param address
     * @return 订单编号
     */
    String confirmOrder(ProProduct pro, OrdOrder order, OrdAddress address);

    /**
     * 根据shopId和流水编号获取订单详情
     *
     * @param shopId
     * @param number
     * @return
     */
    VoOrderLoad getOrderDetailByNumber(int shopId, String number);

    /**
     * 条件查询订单列表
     *
     * @param paramOrdOrder
     * @return
     */
    List<VoOrder> queryOrdOrderList(ParamOrdOrder paramOrdOrder);

    /**
     * 根据id查询订单
     *
     * @param id
     * @return
     */
    VoOrder getOrdOrderInfo(String id);

    /**
     * 取消订单。退货退款
     *
     * @param shopId
     * @param ordOrder
     * @param cancelReason
     * @param cancelPerson
     */
    void cancelOrder(Integer shopId, OrdOrder ordOrder, String cancelReason, Integer cancelPerson, BasicParam basicParam);

    /**
     * 更新订单
     *
     * @param ordOrderUpdate
     * @return
     */
    Integer updateOrder(OrdOrder ordOrderUpdate, String receiveAddress, VoOrderLoad voOrderLoad);

    Integer updateOrderEntrust(ParamOrderUploadEntrust orderUploadEntrust);

    /**
     * 根据订单ID查询订单详情
     *
     * @param shopId
     * @param orderBizId
     * @return
     */
    OrdOrder getOrdOrderDetail(int shopId, String orderBizId);

    /**
     * 根据统计日期插入店铺的每日订单商品统计信息
     *
     * @param countDate
     */
    void dailyCountShopOrder(Date countDate);

    /**
     * 推送店铺的每日订单消息统计信息
     *
     * @param countDate
     */
    void pushDailyCountShopOrderMsg(Date countDate);


    /**
     * 推送店铺的每月订单消息统计信息
     *
     * @param countDate
     */
    void pushDailyCountShopOrderMsgForMonth(Date countDate);

    /**
     * 保存订单修改记录
     *
     * @param shopId         店铺ID
     * @param userId         用户ID
     * @param ordOrderUpdate 更新的订单记录
     * @param ordOrderOld    更改前的订单记录
     */
    Integer saveOrdOrderModifyRecord(int shopId, int userId, OrdOrder ordOrderUpdate, VoOrderLoad ordOrderOld, String receiveAddressOld, String receiveAddressNew);

    /**
     * 根据订单number获取订单修改记录
     *
     * @param orderBizId
     * @return
     */
    List<VoOrderModifyRecord> listOrderModifyRecordById(Integer shopId, String orderBizId, boolean hasSeeSalePricePerm);

    /**
     * 获取用户的各订单类型的销售总额、销售件数、毛利润总额
     *
     * @param salaryDetailQuery
     * @return
     */
    List<List<VoSalaryOrdType>> countSaleAndGrossProfitMoney(ParamSalaryDetailQuery salaryDetailQuery);

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
    Object countRecycleProfit(int shopId, int recycleUserId, String proAttr, String startDateTime, String endDateTime);


    /**
     * 物理删除订单数据, 包括订单凭证和分享凭证记录
     *
     * @param shopId
     * @param startDateTime
     * @param endDateTime
     */
    void deleteOrderAndReceipt(int shopId, String startDateTime, String endDateTime);

    /**
     * 删除订单
     *
     * @param shopId
     * @param userId
     * @param orderNumber
     * @param request
     */
    void deleteOrder(int shopId, int userId, String orderNumber, HttpServletRequest request);

    /**
     * 删除订单 假删
     *
     * @param delete
     * @param request
     */
    void deleteOrderForFalse(ParamOrderDelete delete, HttpServletRequest request);

    /**
     * 删除订单 由于要做公价查询 所以删除改为假删
     *
     * @param delete
     */
    void deleteOrderForDel(ParamOrderDelete delete);

    /**
     * 删除订单
     *
     * @param shopId
     * @param orderNumberArray
     * @return
     */
    void deleteBatchOrder(int shopId, String[] orderNumberArray);

    /**
     * 删除订单 多个删除 假删 不添加日志
     *
     * @param delete
     * @param request
     */
    void deleteBatchOrderForFalse(ParamOrderDelete delete, HttpServletRequest request);

    /**
     * 删除订单,注销店铺时调用<br/>
     * 包括和订单相关联的数据
     *
     * @param shopId
     * @return
     */
    void deleteOrderByShopId(int shopId);

    /**
     * 新增, 修改, 退单; 只要订单有变动, 都需要调用此方法;刷新薪资
     *
     * @param ordId
     */
    //void refreshSalaryByOrderId(Integer ordId);

    /**
     * 即时更新薪资
     *
     * @param shopId      店铺Id
     * @param userId      需要更新薪资的用户
     * @param localUserId 当前登录用户id
     * @param startDate   订单开始时间
     */
    //void refreshSalaryForUser(int shopId, int userId, int localUserId, Date startDate);

    /**
     * 获取开单数量
     *
     * @param shopId
     * @return
     */
    Integer getTotalOrderNumByShopId(Integer shopId);

    /**
     * 临时仓订单page显示
     *
     * @param ordForTempSearch
     * @return
     */
    VoOrderForTempPage getOrderForTemp(ParamOrdForTempSearch ordForTempSearch);

    /**
     * 获取临时仓开单数量
     *
     * @param shopId
     * @return
     */
    Integer getOrderNumByTemp(Integer shopId, Integer productId, Integer tempId);


    /**
     * 获取最近一次的开单记录;
     *
     * @param shopId
     * @param userId 如果userId为null,则获取该店铺的最近一次记录
     * @return
     */
    VoOrder getLastOrderInfo(int shopId, Integer userId);

    /**
     * 获取已售订单的销售人员;用作与订单筛选;
     *
     * @param shopId
     * @return
     */
    List<VoEmployee> listSaleUserByShopId(int shopId);


    /**
     * 初始化年化收益率
     */
    void initYearRate();


    /**
     * 获取发货信息列表
     *
     * @param param
     * @return
     */
    List<VoProDeliverByPage> getProDeliverInfo(ParamProPageDeliver param);

    /**
     * 添加到发货列表
     *
     * @param ordOrder
     */
    void savProDeliver(OrdOrder ordOrder);

    /**
     * 根据订单号查询订单信息
     *
     * @param orderNumber
     * @return
     */
    OrdOrder getOrderInfoByNumber(String orderNumber);

    /**
     * 根据店铺id获取开单人员简信息
     *
     * @param shopId
     * @return
     */
    List<VoOrderUserInfo> getFiltrateinfoByShopId(Integer shopId);

    /**
     * 根据id获取订单信息
     *
     * @param fkOrdOrderId
     * @return
     */
    OrdOrder getOrdOrderInfoById(Integer fkOrdOrderId);

    /**
     * 2.6.6发货列表
     *
     * @param ids
     * @param state
     * @return
     */
    List<VoProDeliverByPage> listProDeliverInfo(List<Integer> ids, String state);

    /**
     * 获取代发货订单数量
     *
     * @param ids
     * @return
     */
    Integer getOrderSum(List<Integer> ids);
}
