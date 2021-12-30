package com.luxuryadmin.timedtask.job.shp;

import com.luxuryadmin.service.shp.ShpShareTotalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @PackgeName: com.luxuryadmin.timedtask.job.shp
 * @ClassName: ShopMemberAddHourJob
 * @Author: ZhangSai
 * Date: 2021/6/22 15:53
 */
@Slf4j
@Component
@EnableScheduling
public class ShopMemberAddHourJob implements SchedulingConfigurer {

    @Autowired
    private ShpShareTotalService shpShareTotalService;
    //每日凌晨0点15分执行会员分享添加时长任务
    private static final String CRON = "0 15 0 ? * *";
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(() -> {
            Long startTime = System.currentTimeMillis();
            shpShareTotalService.updateShopMemberAddHour();
            Long endTime = System.currentTimeMillis();
            log.info("添加分享时间的task，耗时：{}ms", endTime-startTime);
        }, triggerContext -> {
            CronTrigger trigger = new CronTrigger(CRON);
            return trigger.nextExecutionTime(triggerContext);
        });
    }
}
