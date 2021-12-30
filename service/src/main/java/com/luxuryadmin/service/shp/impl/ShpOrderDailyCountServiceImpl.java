package com.luxuryadmin.service.shp.impl;

import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.entity.shp.ShpOrderDailyCount;
import com.luxuryadmin.enums.pro.EnumProAttribute;
import com.luxuryadmin.mapper.op.OpMessageMapper;
import com.luxuryadmin.mapper.shp.ShpOrderDailyCountMapper;
import com.luxuryadmin.param.pro.ParamProductQuery;
import com.luxuryadmin.service.op.OpPushService;
import com.luxuryadmin.service.pro.ProProductService;
import com.luxuryadmin.service.shp.ShpOrderDailyCountService;
import com.luxuryadmin.service.shp.ShpServiceService;
import com.luxuryadmin.vo.shp.VoShpInRepoProdDailyCount;
import com.luxuryadmin.vo.shp.VoShpOrderDailyCount;
import com.luxuryadmin.vo.shp.VoShpOrderDailyCountForMonth;
import com.luxuryadmin.vo.shp.VoShpServiceSettleCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Classname ShpOrderDailyCountServiceImpl
 * @Description TODO
 * @Date 2020/9/14 17:20
 * @Created by Administrator
 */
@Service
public class ShpOrderDailyCountServiceImpl implements ShpOrderDailyCountService {

    @Resource
    private ShpOrderDailyCountMapper shpOrderDailyCountMapper;

    @Resource
    private ProProductService proProductService;

    @Autowired
    private OpPushService opPushService;

    @Resource
    private OpMessageMapper opMessageMapper;

    @Autowired
    private ShpServiceService shpServiceService;

    /**
     * 查询统计日期生成的店铺订单统计数据数量
     *
     * @param countDate
     * @return
     */
    @Override
    public Integer getCountByCountDate(Date countDate) {
        return shpOrderDailyCountMapper.selectCountByCountDate(countDate);
    }

    @Override
    public Integer generateShopOrderDailyCount(Integer shopId,Date countDate) {
        if(null == shopId){
            throw new MyException("生成店铺订单商品统计信息异常，店铺ID为空");
        }

        ShpOrderDailyCount  dailyCount = new ShpOrderDailyCount();
        /**
         * 基础属性
         */
        //统计日期
        dailyCount.setCountDate(countDate);
        //统计月份
        dailyCount.setCountMonth(DateUtil.getMonth(countDate));
        //统计年份
        dailyCount.setCountYear(DateUtil.getYear(countDate));
        //统计店铺
        dailyCount.setFkShpShopId(shopId);

        /**
         * 正常订单
         */
        VoShpOrderDailyCount voShpOrderDailyCountNormal = shpOrderDailyCountMapper.selectShpOrdDailyCountVoByShopIdAndOrderType(shopId,countDate,"normal");
        voShpOrderDailyCountNormal = null == voShpOrderDailyCountNormal ? new VoShpOrderDailyCount() : voShpOrderDailyCountNormal;
        //已售出订单总量
        dailyCount.setOrdNum(voShpOrderDailyCountNormal.getOrderNum());
        //已售出订单总额
        dailyCount.setOrdAmount(voShpOrderDailyCountNormal.getOrderAmount());
        //订单总净收益
        dailyCount.setOrdNetProfit(voShpOrderDailyCountNormal.getOrderNetProfit());

        /**
         * 退货订单
         */
        VoShpOrderDailyCount voShpOrderDailyCountCancel = shpOrderDailyCountMapper.selectShpOrdDailyCountVoByShopIdAndOrderType(shopId,countDate,"cancel");
        voShpOrderDailyCountCancel = null == voShpOrderDailyCountCancel ? new VoShpOrderDailyCount() : voShpOrderDailyCountCancel;
        dailyCount.setOrdCancelNum(voShpOrderDailyCountCancel.getOrderNum());
        dailyCount.setOrdCancelAmount(voShpOrderDailyCountCancel.getOrderAmount());

        /**
         * 入库商品
         */
        //自有商品
        VoShpInRepoProdDailyCount voShpInRepoProdDailyCountSelf = shpOrderDailyCountMapper.
                selectShpInRepoProdDailyCountVoByShopIdAndProdType(shopId,countDate,EnumProAttribute.OWN.getCode());
        voShpInRepoProdDailyCountSelf = null == voShpInRepoProdDailyCountSelf ? new VoShpInRepoProdDailyCount() : voShpInRepoProdDailyCountSelf;
        //自有商品数量
        dailyCount.setInRepoNumSelf(voShpInRepoProdDailyCountSelf.getInRepoNum());
        dailyCount.setInRepoAmountSelf(voShpInRepoProdDailyCountSelf.getInRepoAmount());

        //寄售商品
        VoShpInRepoProdDailyCount voShpInRepoProdDailyCountConsignment = shpOrderDailyCountMapper.
                selectShpInRepoProdDailyCountVoByShopIdAndProdType(shopId,countDate,EnumProAttribute.ENTRUST.getCode());
        voShpInRepoProdDailyCountConsignment = null == voShpInRepoProdDailyCountConsignment ? new VoShpInRepoProdDailyCount() : voShpInRepoProdDailyCountConsignment;
        dailyCount.setInRepoNumConsignment(voShpInRepoProdDailyCountConsignment.getInRepoNum());
        dailyCount.setInRepoAmountConsignment(voShpInRepoProdDailyCountConsignment.getInRepoAmount());

        //质押商品
        VoShpInRepoProdDailyCount voShpInRepoProdDailyCountPledge = shpOrderDailyCountMapper.
                selectShpInRepoProdDailyCountVoByShopIdAndProdType(shopId, countDate,EnumProAttribute.PAWN.getCode());
        voShpInRepoProdDailyCountPledge = null == voShpInRepoProdDailyCountPledge ? new VoShpInRepoProdDailyCount() : voShpInRepoProdDailyCountPledge;
        dailyCount.setInRepoNumPledge(voShpInRepoProdDailyCountPledge.getInRepoNum());
        dailyCount.setInRepoAmountPledge(voShpInRepoProdDailyCountPledge.getInRepoAmount());

        //其它商品
        VoShpInRepoProdDailyCount voShpInRepoProdDailyCountOther = shpOrderDailyCountMapper.
                selectShpInRepoProdDailyCountVoByShopIdAndProdType(shopId, countDate,EnumProAttribute.OTHER.getCode());
        voShpInRepoProdDailyCountOther = null == voShpInRepoProdDailyCountOther ? new VoShpInRepoProdDailyCount() : voShpInRepoProdDailyCountOther;
        dailyCount.setInRepoNumOther(voShpInRepoProdDailyCountOther.getInRepoNum());
        dailyCount.setInRepoAmountOther(voShpInRepoProdDailyCountOther.getInRepoAmount());

        //所有商品
        VoShpInRepoProdDailyCount voShpInRepoProdDailyCountAll = shpOrderDailyCountMapper.
                selectShpInRepoProdDailyCountVoByShopIdAndProdType(shopId, countDate,null);
        voShpInRepoProdDailyCountAll = null == voShpInRepoProdDailyCountAll ? new VoShpInRepoProdDailyCount() : voShpInRepoProdDailyCountAll;
        dailyCount.setInRepoNumTotal(voShpInRepoProdDailyCountAll.getInRepoNum());
        dailyCount.setInRepoAmountTotal(voShpInRepoProdDailyCountAll.getInRepoAmount());

        //上架商品数量
        //统计今日上传,今日上架
        String today = DateUtil.formatShort(countDate);
        ParamProductQuery queryParam = new ParamProductQuery();
        queryParam.setShopId(shopId);
        queryParam.setTodayDate(today);
        //只统计今天的上传上架数据, 需要把查询时间设置为空,把状态设置为all;才不会因为状态和查询时间而改变查询结果;
        queryParam.setUploadStDateTime(null);
        queryParam.setUploadEtDateTime(null);
        queryParam.setStateCode("all");
        Integer onshelvesProdNum = proProductService.countTodayOnRelease(queryParam);
        onshelvesProdNum = null == onshelvesProdNum ? 0 : onshelvesProdNum;
        dailyCount.setOnshelvesProdNum(onshelvesProdNum);

        /**
         * 维修保养相关字段
         */
        //结算相关参数
        VoShpServiceSettleCount voShpServiceSettleCount = shpServiceService.countTodaySettle(queryParam);
        voShpServiceSettleCount = null == voShpServiceSettleCount ? new VoShpServiceSettleCount() : voShpServiceSettleCount;
        dailyCount.setServiceSettleNum(voShpServiceSettleCount.getServiceSettleNum());
        dailyCount.setServiceSettleSellAmount(voShpServiceSettleCount.getServiceSettleSellAmount());
        dailyCount.setServiceSettleCostAmount(voShpServiceSettleCount.getServiceSettleCostAmount());
        dailyCount.setServiceSettleProfitAmount(voShpServiceSettleCount.getServiceSettleProfitAmount());

        /**
         * 时间字段
         */
        dailyCount.setInsertTime(new Date());
        dailyCount.setUpdateTime(new Date());

        return shpOrderDailyCountMapper.insertSelective(dailyCount);
    }

    @Override
    public List<Integer> queryShpShopIdListByCountDate(Date countDate) {
        return shpOrderDailyCountMapper.queryShpShopIdListByCountDate(countDate);
    }

    @Override
    public void pushDailyCountShopOrderMsg(Integer shopId, Date countDate) {
        if(null == shopId){
            throw new MyException("推送店铺订单商品统计信息异常，店铺ID为空");
        }

        ShpOrderDailyCount shpOrderDailyCount = shpOrderDailyCountMapper.selectByShopIdAndCountDate(shopId,countDate);
        if(null == shpOrderDailyCount){
            throw new MyException("没有查询到对应店铺ID的【店铺订单商品统计信息】shopId="+shopId);
        }

        opPushService.pushShpOrderDailyCount(shopId,shpOrderDailyCount);

    }

    @Override
    public void pushDailyCountShopOrderMsgForMonth(Integer shopId, Date countDate) {
        if(null == shopId){
            throw new MyException("推送店铺订单商品统计信息异常，店铺ID为空");
        }
        Calendar calendar = Calendar.getInstance();
        // 设置为当前时间
        calendar.setTime(countDate);
        // 设置为上一个月
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        Integer month = DateUtil.getMonth(calendar.getTime());
        Integer year =DateUtil.getYear(calendar.getTime());
        VoShpOrderDailyCountForMonth orderDailyCountForMonth = shpOrderDailyCountMapper.selectByShopIdAndCountDateForMonth(shopId,month,year);
        if(null == orderDailyCountForMonth){
            throw new MyException("没有查询到对应店铺ID的【店铺订单商品统计信息】shopId="+shopId);
        }
        opPushService.pushShpOrderDailyCountForMonth(shopId,orderDailyCountForMonth);
    }

    @Override
    public Integer getMsgCountByCountDate(String msgType,String msgSubType,Date countDate) {
        return opMessageMapper.selectMsgCountByCountDate(msgType,msgSubType,countDate);
    }
}
