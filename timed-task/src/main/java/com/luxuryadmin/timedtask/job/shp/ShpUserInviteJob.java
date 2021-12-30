package com.luxuryadmin.timedtask.job.shp;

import com.luxuryadmin.service.shp.ShpUserInviteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

/**
 * @PackgeName: com.luxuryadmin.timedtask.job.shp
 * @ClassName: ShpUserInviteJob
 * @Author: ZhangSai
 * Date: 2021/11/11 21:38
 */
@Slf4j
@Component
@EnableScheduling
public class ShpUserInviteJob  implements SchedulingConfigurer {
    @Autowired
    private ShpUserInviteService shpUserInviteService;
    //15分钟执行一次
    private static final String CRON = "0 0/15 * * * ? ";
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(() -> {
            Long startTime = System.currentTimeMillis();
            shpUserInviteService.addOrUpdateCensus();
            Long endTime = System.currentTimeMillis();
            log.info("更新用户数据的task，耗时：{}ms", endTime-startTime);
        }, triggerContext -> {
            CronTrigger trigger = new CronTrigger(CRON);
            return trigger.nextExecutionTime(triggerContext);
        });
    }
}
