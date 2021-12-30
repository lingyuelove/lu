package com.luxuryadmin.timedtask.job.op;

import com.luxuryadmin.service.op.OpMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

/**
 * 用户相关的task
 *
 * @author nick
 */
@Slf4j
@Component
@EnableScheduling
public class OpAutoTimerPushJob implements SchedulingConfigurer {

    @Autowired
    private OpMessageService opMessageService;

    /**
     * 一步计算提现风险风度
     */
    private static final String CRON = "0 */1 * * * ?";

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(() -> {
            Long startTime = System.currentTimeMillis();

            opMessageService.autoTimerPush();
            Long endTime = System.currentTimeMillis();
            log.info("消息定时推送的task，耗时：{}ms", endTime-startTime);
        }, triggerContext -> {
            CronTrigger trigger = new CronTrigger(CRON);
            return trigger.nextExecutionTime(triggerContext);
        });
    }
}
