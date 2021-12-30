package com.luxuryadmin.service.impl;

import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.context.Cont;
import com.luxuryadmin.entity.MpPayOrder;
import com.luxuryadmin.entity.MpUser;
import com.luxuryadmin.enums.*;
import com.luxuryadmin.mapper.MpPayOrderMapper;
import com.luxuryadmin.mapper.MpUserMapper;
import com.luxuryadmin.param.adminconfig.ParamAddVipUser;
import com.luxuryadmin.param.adminconfig.ParamAdminConfig;
import com.luxuryadmin.service.AdminConfigService;
import com.luxuryadmin.service.BasicsService;
import com.luxuryadmin.service.MpUserInviteService;
import com.luxuryadmin.util.DateUtil;
import com.luxuryadmin.vo.adminconfig.VOAdminConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Administrator
 */
@Service
public class AdminConfigServiceImpl implements AdminConfigService {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private MpUserMapper mpUserMapper;

    @Resource
    private MpPayOrderMapper mpPayOrderMapper;

    @Resource
    private BasicsService basicsService;

    @Resource
    private MpUserInviteService mpUserInviteService;


    @Override
    public void updateConfig(ParamAdminConfig param) {
        redisUtil.set(Cont.MP_BAOYANG_CONFIG_EXPERIENCETIME, param.getExperienceTime().toString());
        redisUtil.set(Cont.MP_BAOYANG_CONFIG_AWARDTIME, param.getAwardTime().toString());
        redisUtil.set(Cont.MP_BAOYANG_CONFIG_CLOUDWAREHOUSE, param.getCloudWarehouseNum());
        redisUtil.set(Cont.MP_BAOYANG_CONFIG_SHOPNUM, param.getShopNum());

        redisUtil.set(Cont.MP_BAOYANG_CONFIG_HOMEURL, param.getHomeUrl());
        redisUtil.set(Cont.MP_BAOYANG_CONFIG_APPID, param.getAppId());
    }

    /**
     * 获取配置时间
     */
    @Override
    public VOAdminConfig getConfig() {
        VOAdminConfig voAdminConfig = new VOAdminConfig();
        voAdminConfig.setExperienceTime(Integer.parseInt(redisUtil.get(Cont.MP_BAOYANG_CONFIG_EXPERIENCETIME)));
        voAdminConfig.setAwardTime(Integer.parseInt(redisUtil.get(Cont.MP_BAOYANG_CONFIG_AWARDTIME)));
        voAdminConfig.setCloudWarehouseNum(redisUtil.get(Cont.MP_BAOYANG_CONFIG_CLOUDWAREHOUSE));
        voAdminConfig.setShopNum(redisUtil.get(Cont.MP_BAOYANG_CONFIG_SHOPNUM));

        voAdminConfig.setHomeUrl(redisUtil.get(Cont.MP_BAOYANG_CONFIG_HOMEURL));
        voAdminConfig.setAppId(redisUtil.get(Cont.MP_BAOYANG_CONFIG_APPID));
        return voAdminConfig;
    }

    /**
     * 添加会员用户
     *
     * @param param
     */
    @Override
    public void addVipUser(ParamAddVipUser param) {
        Integer userId = basicsService.getUserId();
        if (userId==null){
            throw new MyException("用户未登录");
        }
        String username = DESEncrypt.encodeUsername(param.getUsername());
        MpUser user = mpUserMapper.getUserInfoByUsername(username);
        Date date = new Date();
        Date todayZero = DateUtil.getTodayZero();
        if (user == null) {
            user=new MpUser();
            user.setUsername(username);
            user.setState(EnumUserState.NORMAL.getCode());
            user.setMasterType(EnumMasterType.BY.name());
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            String experienceTime = redisUtil.get(Cont.MP_BAOYANG_CONFIG_EXPERIENCETIME);
            cal.add(Calendar.DAY_OF_MONTH, +Integer.parseInt(experienceTime));
            Date time = cal.getTime();
            user.setTryStartTime(date);
            user.setTryEndTime(time);
            user.setIsMember(EnumIsMember.YES.getCode());
            user.setMemberState(EnumMemberState.official_vip.getCode());
            user.setVipType(EnumVipType.MP_VIP.getCode());
            user.setPayStartTime(todayZero);
            Calendar endCal = Calendar.getInstance();
            endCal.setTime(todayZero);
            endCal.add(Calendar.YEAR, +1);
            Date payEndTime = endCal.getTime();
            user.setPayEndTime(payEndTime);
            user.setProvince("");
            user.setCity("");
            user.setDistrict("");
            user.setInsertTime(date);
            mpUserMapper.saveObject(user);

        } else {
            user.setIsMember(EnumIsMember.YES.getCode());
            user.setMemberState(EnumMemberState.official_vip.getCode());
            Calendar endCal = Calendar.getInstance();
            if (user.getPayEndTime() != null) {
                //续费会员
                if (user.getPayEndTime().getTime() < date.getTime()) {
                    //已过期会员
                    endCal.setTime(todayZero);
                    user.setPayStartTime(todayZero);
                } else {
                    //在之前会员基础之上加时间
                    endCal.setTime(user.getPayEndTime());
                }
            } else {
                endCal.setTime(todayZero);
                user.setPayStartTime(todayZero);
            }
            endCal.add(Calendar.YEAR, +1);
            Date payEndTime = endCal.getTime();
            String dateStr = com.luxuryadmin.common.utils.DateUtil.formatDaySt(payEndTime);
            try {
                user.setPayEndTime(com.luxuryadmin.common.utils.DateUtil.parse(dateStr));
            } catch (ParseException e) {
                throw new MyException("时间转换异常");
            }
            user.setUpdateTime(date);
            mpUserMapper.updateObject(user);
        }
        String maintainValue = redisUtil.get(Cont.MP_USER + param.getUsername());
        if (StringUtil.isNotBlank(maintainValue)) {
            //删除体验会员日期redis缓存
            redisUtil.delete(Cont.MP_USER + maintainValue + Cont.END_TIME);
        }
        //设置会员过期时间存redis
        redisUtil.set(Cont.MP_USER + param.getUsername() + Cont.PAY_END_TIME, com.luxuryadmin.common.utils.DateUtil.format(user.getPayEndTime()));

        String orderNo = LocalUtils.getTimestamp() + "-" + EnumMasterType.BY.name() + "-" + user.getId();
        MpPayOrder mpPayOrder = new MpPayOrder();
        mpPayOrder.setFkMpUserId(user.getId());
        mpPayOrder.setTransactionId("");
        mpPayOrder.setOrderNo(orderNo);
        mpPayOrder.setTotalMoney(new BigDecimal(EnumMasterType.BY.getMoney()));
        mpPayOrder.setDiscountMoney(new BigDecimal("0"));
        mpPayOrder.setRealMoney(new BigDecimal(param.getRealMoney()));
        mpPayOrder.setVipType(EnumVipType.MP_VIP.getCode());
        mpPayOrder.setOrderType(EnumOrderType.VIP.getCode());
        mpPayOrder.setState(40);
        mpPayOrder.setPayChannel(EnumPayChannel.OTHER.getCode());
        mpPayOrder.setTradeType("其他");
        mpPayOrder.setCreateType("1");
        mpPayOrder.setInsertTime(date);
        mpPayOrder.setUpdateTime(date);
        mpPayOrder.setPayTime(date);
        mpPayOrder.setFinishTime(date);
        mpPayOrder.setInsertAdmin(userId);
        mpPayOrder.setUpdateAdmin(0);
        mpPayOrderMapper.saveObject(mpPayOrder);
        //-----------------处理奖励信息---------------------------------
        mpUserInviteService.setAwardInfo(user.getId());
    }
}
