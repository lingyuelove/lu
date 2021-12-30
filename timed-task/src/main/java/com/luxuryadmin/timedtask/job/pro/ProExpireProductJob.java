package com.luxuryadmin.timedtask.job.pro;

import com.luxuryadmin.service.pro.ProExpiredNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

/**
 * 每日早上九点10分执行记定时发送商品到期提醒任务
 *
 * @author zhangSai
 * @date 2021/04/23 14:19:52
 */
@Slf4j
@Component
@EnableScheduling
public class ProExpireProductJob implements SchedulingConfigurer {

    @Autowired
    private ProExpiredNoticeService expiredNoticeService;
    //每日早上九点10分执行记定时发送商品到期提醒任务
    private static final String CRON = "0 10 09 ? * *";

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(() -> {
            Long startTime = System.currentTimeMillis();
            expiredNoticeService.sendMessageForExpiredProduct();
            Long endTime = System.currentTimeMillis();
            log.info("发送商品到期提醒的task，耗时：{}ms", endTime - startTime);
        }, triggerContext -> {
            CronTrigger trigger = new CronTrigger(CRON);
            return trigger.nextExecutionTime(triggerContext);
        });

    }
}
