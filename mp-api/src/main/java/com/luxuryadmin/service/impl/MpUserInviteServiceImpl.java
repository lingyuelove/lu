package com.luxuryadmin.service.impl;

import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.context.Cont;
import com.luxuryadmin.entity.MpAddVipTime;
import com.luxuryadmin.entity.MpUser;
import com.luxuryadmin.entity.MpUserInvite;
import com.luxuryadmin.enums.EnumIsMember;
import com.luxuryadmin.enums.EnumMemberState;
import com.luxuryadmin.enums.EnumVipType;
import com.luxuryadmin.mapper.MpAddVipTimeMapper;
import com.luxuryadmin.mapper.MpUserInviteMapper;
import com.luxuryadmin.mapper.MpUserMapper;
import com.luxuryadmin.param.invite.ParamUserInviteList;
import com.luxuryadmin.service.BasicsService;
import com.luxuryadmin.service.MpUserInviteService;
import com.luxuryadmin.util.DateUtil;
import com.luxuryadmin.vo.invite.VOUserInviteList;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * 邀请记录 serverImpl
 *
 * @author zhangsai
 * @Date 2021-11-24 13:56:01
 */
@Log4j2
@Service
public class MpUserInviteServiceImpl implements MpUserInviteService {


    /**
     * 注入dao
     */
    @Resource
    private MpUserInviteMapper mpUserInviteMapper;

    @Resource
    private BasicsService basicsService;

    @Resource
    private MpAddVipTimeMapper mpAddVipTimeMapper;

    @Resource
    private MpUserMapper mpUserMapper;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 获取邀请用户列表
     *
     * @param param
     * @return
     */
    @Override
    public List<VOUserInviteList> listUserInvite(ParamUserInviteList param) {
        Integer userId = basicsService.getUserId();
        MpUser mpUser = mpUserMapper.getObjectById(userId);
        if (mpUser != null && EnumVipType.MP_VIP.getCode().equals(mpUser.getVipType()) && EnumIsMember.YES.getCode().equals(mpUser.getIsMember())) {
            param.setUserId(userId);
            List<VOUserInviteList> vo = mpUserInviteMapper.listUserInvite(param);
            return vo;
        }
        return null;
    }


    /**
     * 支付成功后修改邀请记录和奖励信息
     *
     * @param userId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserInviteAndAddDayInfo(Integer userId) {
        Date date = new Date();
        //处理用户信息
        MpUser payUserInfo = mpUserMapper.getObjectById(userId);
        payUserInfo.setIsMember(EnumIsMember.YES.getCode());
        payUserInfo.setMemberState(EnumMemberState.official_vip.getCode());
        Calendar endCal = Calendar.getInstance();
        //获取当天0点
        Date todayZero = DateUtil.getTodayZero();
        if (payUserInfo.getPayEndTime() != null) {
            //续费会员
            if (payUserInfo.getPayEndTime().getTime() < date.getTime()) {
                //已过期会员
                endCal.setTime(todayZero);
                payUserInfo.setPayStartTime(todayZero);
            } else {
                //在之前会员基础之上加时间
                endCal.setTime(payUserInfo.getPayEndTime());
            }
        } else {
            endCal.setTime(todayZero);
            payUserInfo.setPayStartTime(todayZero);
        }
        endCal.add(Calendar.YEAR, +1);
        Date payEndTime = endCal.getTime();
        String dateStr = com.luxuryadmin.common.utils.DateUtil.formatDaySt(payEndTime);
        try {
            payUserInfo.setPayEndTime(com.luxuryadmin.common.utils.DateUtil.parse(dateStr));
        } catch (ParseException e) {
            throw new MyException("时间转换异常");
        }
        payUserInfo.setUpdateTime(date);
        payUserInfo.setVipType(EnumVipType.MP_VIP.getCode());
        mpUserMapper.updateObject(payUserInfo);

        String username = DESEncrypt.decodeUsername(payUserInfo.getUsername());
        String maintainValue = redisUtil.get(Cont.MP_USER + username);
        if (StringUtil.isNotBlank(maintainValue)) {
            //删除体验会员日期redis缓存
            redisUtil.delete(Cont.MP_USER + maintainValue + Cont.END_TIME);
        }
        //设置会员过期时间存redis
        redisUtil.set(Cont.MP_USER + username + Cont.PAY_END_TIME, com.luxuryadmin.common.utils.DateUtil.format(payEndTime));

        //-----------------------处理奖励及业务逻辑------------------------------
        setAwardInfo(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setAwardInfo(Integer userId) {
        MpUserInvite userInviteByBeUserId = mpUserInviteMapper.getUserInviteByBeUserId(userId);
        if (userInviteByBeUserId == null) {
            //没有邀请信息
            return;
        }
        if ("20".equals(userInviteByBeUserId.getRewardState())) {
            //奖励已发放
            return;
        }
        //查询用户信息（奖励人的用户信息）
        MpUser mpUser = mpUserMapper.getObjectById(userInviteByBeUserId.getFkInviteMpUserId());
        if (mpUser == null || "1".equals(mpUser.getDel())) {
            //用户被删除
            return;
        }
        String experienceTime = redisUtil.get(Cont.MP_BAOYANG_CONFIG_AWARDTIME);
        //处理邀请表信息
        userInviteByBeUserId.setRewardState("20");
        userInviteByBeUserId.setUpdateTime(new Date());
        mpUserInviteMapper.updateObject(userInviteByBeUserId);
        //处理奖励表信息
        MpAddVipTime mpAddVipTime = new MpAddVipTime();
        mpAddVipTime.setFkMpUserId(mpUser.getId());
        mpAddVipTime.setFkBeMpUserId(userId);
        mpAddVipTime.setStartTime(mpUser.getPayEndTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(mpUser.getPayEndTime());
        cal.add(Calendar.DAY_OF_MONTH, +Integer.parseInt(experienceTime));
        Date time = cal.getTime();
        mpAddVipTime.setFinalEndTime(time);
        mpAddVipTime.setAddDay(Integer.parseInt(experienceTime));
        mpAddVipTime.setInsertTime(new Date());
        mpAddVipTimeMapper.saveObject(mpAddVipTime);

        //处理奖励时间
        Calendar cd = Calendar.getInstance();
        cd.setTime(mpUser.getPayEndTime());
        cd.add(Calendar.DATE, Integer.parseInt(experienceTime));
        Date awaryPayEndTime = cd.getTime();
        mpUser.setPayEndTime(awaryPayEndTime);
        mpUserMapper.updateObject(mpUser);
    }
}
