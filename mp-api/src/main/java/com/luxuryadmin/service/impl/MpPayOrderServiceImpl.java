package com.luxuryadmin.service.impl;

import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.MpPayOrder;
import com.luxuryadmin.entity.MpUser;
import com.luxuryadmin.enums.EnumMasterType;
import com.luxuryadmin.enums.EnumOrderType;
import com.luxuryadmin.enums.EnumUserState;
import com.luxuryadmin.enums.EnumVipType;
import com.luxuryadmin.mapper.MpPayOrderMapper;
import com.luxuryadmin.mapper.MpUserMapper;
import com.luxuryadmin.service.BasicsService;
import com.luxuryadmin.service.MpPayOrderService;
import com.luxuryadmin.util.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 支付订单 serverImpl
 *
 * @author zhangsai
 * @Date 2021-11-24 13:56:01
 */
@Service
public class MpPayOrderServiceImpl implements MpPayOrderService {


    /**
     * 注入dao
     */
    @Resource
    private MpPayOrderMapper mpPayOrderMapper;

    @Resource
    private BasicsService basicsService;

    @Resource
    private MpUserMapper mpUserMapper;

    /**
     * 创建鲍杨小程序购买会员订单
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createOrder() {
        //用户信息
        Integer userId = basicsService.getUserId();
        if (userId == null) {
            throw new MyException("用户未登录");
        }
        MpUser mpUser = mpUserMapper.getObjectById(userId);
        if (mpUser == null || "1".equals(mpUser.getDel())) {
            throw new MyException("用户不存在");
        }
        if (EnumUserState.FORBIDDEN.getCode().equals(mpUser.getState())) {
            throw new MyException("用户被禁用");
        }

        //订单信息
        String orderNo = LocalUtils.getTimestamp() + "-" + EnumMasterType.BY.name() + "-" + userId;
        MpPayOrder mpPayOrder = new MpPayOrder();
        mpPayOrder.setFkMpUserId(userId);
        mpPayOrder.setOrderNo(orderNo);
        mpPayOrder.setTotalMoney(new BigDecimal(EnumMasterType.BY.getMoney()));
        mpPayOrder.setDiscountMoney(new BigDecimal("0"));
        mpPayOrder.setRealMoney(new BigDecimal("0"));
        mpPayOrder.setVipType(EnumVipType.MP_VIP.getCode());
        mpPayOrder.setOrderType(EnumOrderType.VIP.getCode());
        mpPayOrder.setState(10);
        mpPayOrder.setCreateType("0");
        mpPayOrder.setInsertTime(new Date());
        mpPayOrderMapper.saveObject(mpPayOrder);
        return orderNo;
    }

    /**
     * 根据订单编号获取用户订单信息
     *
     * @param out_trade_no
     * @return
     */
    @Override
    public MpPayOrder getOrderInfoByOrderNo(String out_trade_no) {
        MpPayOrder orderInfoByOrderNo = mpPayOrderMapper.getOrderInfoByOrderNo(out_trade_no, null);
        return orderInfoByOrderNo;
    }

    /**
     * 修改订单信息
     *
     * @param mpPayOrder
     */
    @Override
    public void updateOrderInfo(MpPayOrder mpPayOrder) {
        mpPayOrderMapper.updateObject(mpPayOrder);
    }
}
