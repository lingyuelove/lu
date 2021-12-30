package com.luxuryadmin.job;

import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.context.Cont;
import com.luxuryadmin.entity.MpUser;
import com.luxuryadmin.enums.EnumVipType;
import com.luxuryadmin.mapper.shp.ShpUserMapper;
import com.luxuryadmin.service.MpUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Slf4j
@Component
@EnableScheduling
public class OfficialVipJob implements SchedulingConfigurer {
    @Autowired
    private MpUserService mpUserService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ShpUserMapper shpUserMapper;

    /**
     * 每日0点一分执行
     */
    private static final String ZERO_ONE = "0 1 0 ? * *";
    //private static final String ZERO_ONE = "0 0/1 * * * ? ";

    /**
     * 正式会员过期定时器
     *
     * @param scheduledTaskRegistrar
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(() -> {
            Date date = new Date();
            log.info("处理正式会员用户信息(开始):" + date);
            List<MpUser> mpUsers = mpUserService.getVipUserName();
            mpUserService.updateVipUserInfo(date);
            List<String> sdjUsernames = new ArrayList<>();
            List<String> noMpVipUsernama = new ArrayList<>();
            mpUsers.stream().forEach(mu -> {
                if (EnumVipType.SDJ_VIP.getCode().equals(mu.getVipType())) {
                    sdjUsernames.add(mu.getUsername());
                }
                if (mu.getPayEndTime() != null && mu.getPayEndTime().getTime() < date.getTime()) {
                    String usernameDecode = DESEncrypt.decodeUsername(mu.getUsername());
                    redisUtil.delete(Cont.MP_USER + usernameDecode + Cont.PAY_END_TIME);
                    noMpVipUsernama.add(mu.getUsername());
                }
            });
            //之前是小程序会员
            if (noMpVipUsernama.size() > 0) {
                List<String> phones = shpUserMapper.listVipPhone(noMpVipUsernama);
                //是否为奢当家会员
                if (phones != null && phones.size() > 0) {
                    //是奢当家会员
                    List<String> yesSdjVipPhones = new ArrayList<>();
                    for (String phone : phones) {
                        if (noMpVipUsernama.contains(phone)) {
                            yesSdjVipPhones.add(phone);
                            String usernameDecode = DESEncrypt.decodeUsername(phone);
                            redisUtil.set(Cont.MP_USER + usernameDecode + Cont.SDJ_VIP, Cont.IS_SDJ_VIP);
                        }
                    }
                    //将小程序会员转为奢当家会员
                    if (yesSdjVipPhones.size() > 0) {
                        mpUserService.updateSdjVipByMpVipUsername(yesSdjVipPhones);
                    }
                }
            }
            //之前是奢当家会员
            if (sdjUsernames.size() > 0) {
                List<String> phones = shpUserMapper.listVipPhone(sdjUsernames);
                //不是奢当家会员
                List<String> noSdjVipPhones = new ArrayList<>();
                if (phones != null && phones.size() > 0) {
                    for (String phone : phones) {
                        if (!sdjUsernames.contains(phone)) {
                            noSdjVipPhones.add(phone);
                        }
                    }
                } else {
                    noSdjVipPhones.addAll(sdjUsernames);
                }
                if (noSdjVipPhones.size() > 0) {
                    //直接过期.删除redis
                    mpUserService.updateSdjVipPast(noSdjVipPhones);
                    noSdjVipPhones.stream().forEach(ns -> {
                        String usernameDecode = DESEncrypt.decodeUsername(ns);
                        redisUtil.delete(Cont.MP_USER + usernameDecode + Cont.SDJ_VIP);
                    });
                }
            }
            log.info("处理正式会员用户信息(结束):" + new Date());
        }, triggerContext -> {
            CronTrigger trigger = new CronTrigger(ZERO_ONE);
            return trigger.nextExecutionTime(triggerContext);
        });
    }


    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        List<Integer> list1 = new ArrayList<>();
        /*list1.addAll(list);
        System.out.println(list1);*/

        list1 = list;
        System.out.println(list1);
    }
}
