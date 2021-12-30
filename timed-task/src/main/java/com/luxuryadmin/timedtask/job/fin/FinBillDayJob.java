package com.luxuryadmin.timedtask.job.fin;

import org.springframework.scheduling.annotation.SchedulingConfigurer;
import com.luxuryadmin.service.fin.FinBillDayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronTrigger;

/**
 * 帐单定时任务
 *
 * @author zhangSai
 * @date   2021/04/23 14:19:52
 */
@Slf4j
@Component
@EnableScheduling
public class FinBillDayJob implements SchedulingConfigurer {

    @Autowired
    private FinBillDayService billDayService;

    //每日凌晨0点10分执行记账单任务
    private static final String CRON = "0 10 0 ? * *";

//    private static final String CRON = "0 07 16 ? * *";
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(() -> {
            Long startTime = System.currentTimeMillis();
            billDayService.addFinBillDay();
            Long endTime = System.currentTimeMillis();
            log.info("账单任务的task，耗时：{}ms", endTime-startTime);
        }, triggerContext -> {
            CronTrigger trigger = new CronTrigger(CRON);
            return trigger.nextExecutionTime(triggerContext);
        });
    }
}
