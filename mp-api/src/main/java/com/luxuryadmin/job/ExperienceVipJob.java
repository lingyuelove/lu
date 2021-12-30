package com.luxuryadmin.job;

import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.context.Cont;
import com.luxuryadmin.service.MpUserService;
import com.luxuryadmin.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Slf4j
@Component
@EnableScheduling
public class ExperienceVipJob implements SchedulingConfigurer {

    @Autowired
    private MpUserService mpUserService;

    @Autowired
    private RedisUtil redisUtil;
    /**
     * 15分钟执行一次
     */
    private static final String FIFTEEN = "0 0/15 * * * ? ";

    /**
     * 体验会员定时器
     *
     * @param scheduledTaskRegistrar
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(() -> {
            Date date = new Date();
            log.info("处理体验会员用户信息(开始):" + date);
            List<String> userNameEncrypt = mpUserService.getUserName(date);
            mpUserService.updatePastUserInfo(date);
            userNameEncrypt.stream().forEach(un -> {
                String username = DESEncrypt.decodeUsername(un);
                String maintainValue = redisUtil.get(Cont.MP_USER + username);
                if (StringUtil.isNotBlank(maintainValue)) {
                    redisUtil.delete(Cont.MP_USER + maintainValue + Cont.END_TIME);
                }
            });
            log.info("处理体验会员用户信息(结束):" + date);
        }, triggerContext -> {
            CronTrigger trigger = new CronTrigger(FIFTEEN);
            return trigger.nextExecutionTime(triggerContext);
        });
    }
}
