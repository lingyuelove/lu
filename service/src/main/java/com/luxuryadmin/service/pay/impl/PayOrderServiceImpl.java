package com.luxuryadmin.service.pay.impl;

import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.entity.pay.PayOrder;
import com.luxuryadmin.entity.shp.ShpShop;
import com.luxuryadmin.mapper.pay.PayOrderMapper;
import com.luxuryadmin.param.mem.ParamMemShopOrder;
import com.luxuryadmin.param.pay.ParamAddPayOrderForAdmin;
import com.luxuryadmin.service.pay.PayOrderService;
import com.luxuryadmin.service.shp.ShpShopService;
import com.luxuryadmin.vo.mem.VoMemShop;
import com.luxuryadmin.vo.mem.VoMemShopOrder;
import com.luxuryadmin.vo.pay.VoPayOrderShopForAdmin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * 支付订单
 *
 * @author monkey king
 * @date 2021-03-21 18:15:09
 */
@Slf4j
@Service
public class PayOrderServiceImpl implements PayOrderService {

    @Resource
    PayOrderMapper payOrderMapper;

    @Autowired
    protected RedisUtil redisUtil;

    @Autowired
    private ShpShopService shpShopService;

    @Autowired
    protected ServicesUtil servicesUtil;
    @Override
    public PayOrder createNoPayOrder(int shopId, int userId, String goodsName, String orderType,
                                     Long totalMoney, Long discountMoney, Long realMoney, String payChannel,
                                     String payPlatform, String tradeType, String createType) {
        //订单号
        String orderNo = LocalUtils.getTimestamp() + "-" + shopId + userId;
        PayOrder payOrder = new PayOrder();
        payOrder.setFkShpShopId(shopId);
        payOrder.setFkShpUserId(userId);
        payOrder.setGoodsName(goodsName);

        payOrder.setOrderNo(orderNo);
        payOrder.setTotalMoney(totalMoney);
        payOrder.setDiscountMoney(discountMoney);
        payOrder.setRealMoney(realMoney);
        payOrder.setOrderType(orderType);
        payOrder.setState(10);
        payOrder.setPayChannel(payChannel);
        payOrder.setPayPlatform(payPlatform);
        payOrder.setTradeType(tradeType);
        payOrder.setCreateType(createType);
        payOrder.setInsertTime(new Date());
        payOrder.setUpdateTime(payOrder.getInsertTime());
        payOrder.setInsertAdmin(userId);
        int id = payOrderMapper.saveObject(payOrder);
        payOrder.setId(id);
        return payOrder;
    }

    @Override
    public Integer saveOrUpdatePayOrder(PayOrder payOrder) {
        if (payOrder == null) {
            String msg = "创建订单失败,订单信息为空";
            log.error(msg);
            throw new MyException(msg);
        }
        Integer id = payOrder.getId();
        if (null != id) {
            payOrderMapper.updateObject(payOrder);
        } else {
            id = payOrderMapper.saveObject(payOrder);
        }
        return id;
    }

    @Override
    public PayOrder getPayOrderByOrderNo(int userId, String orderNo) {
        return payOrderMapper.getPayOrderByOrderNo(userId, orderNo);
    }

    @Override
    public PayOrder getPayOrderByTransactionId(int userId, String transactionId) {
        return payOrderMapper.getPayOrderByTransactionId(userId, transactionId);
    }

    @Override
    public List<VoMemShopOrder> pageVoMemShopOrder(ParamMemShopOrder paramMemShopOrder) {
        List<VoMemShopOrder> voMemShopOrderList = payOrderMapper.pageVoMemShopOrder(paramMemShopOrder);
        return voMemShopOrderList;
    }

    @Override
    public BaseResult addPayOrderForAdmin(ParamAddPayOrderForAdmin paramAddPayOrderForAdmin) {
        ShpShop shpShop= shpShopService.updateShpShopMember(paramAddPayOrderForAdmin.getFkShpShopId(),paramAddPayOrderForAdmin.getPayMonth());
        if (LocalUtils.isEmptyAndNull(shpShop)){
            BaseResult.defaultErrorWithMsg("店铺不存在!");
        }
        PayOrder payOrder = new PayOrder();
        BeanUtils.copyProperties(paramAddPayOrderForAdmin,payOrder);
        payOrder.setOrderType("vip");
        payOrder.setState(40);
        payOrder.setPayChannel("other");
        payOrder.setPayPlatform("admin");
        payOrder.setTradeType("线下支付");
        payOrder.setCreateType("1");
        payOrder.setInsertTime(new Date());
        payOrder.setPayTime(new Date());
        payOrder.setFinishTime(new Date());
        payOrder.setFkShpUserId(shpShop.getFkShpUserId());
        payOrder.setGoodsName(shpShop.getName()+"年费会员");
        payOrder.setRemark(paramAddPayOrderForAdmin.getPayMonth().toString());
        String orderNo = LocalUtils.getTimestamp() + "-" + paramAddPayOrderForAdmin.getFkShpShopId() + "-1";
        payOrder.setOrderNo(orderNo);
        int id = payOrderMapper.saveObject(payOrder);

        return LocalUtils.getBaseResult(payOrder);
    }

    @Override
    public List<VoPayOrderShopForAdmin> getVoPayOrderShopForAdmin(Integer fkShpShopId) {
        return payOrderMapper.getVoPayOrderShopForAdmin(fkShpShopId);
    }

}
