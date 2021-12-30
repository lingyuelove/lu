package com.luxuryadmin.timedtask.job.shp;

import com.luxuryadmin.timedtask.thread.shp.ScanVipExpireShopRunnable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 扫描过期店铺,设置过期状态;<br/>
 * 具体详情查看业务线程;每隔30分钟执行一次;<br/>
 *整点30分钟开始; 如:当前时间是15分,启动执行任务时, 下次执行时间是30分
 * @author qwy
 */
@Slf4j
@Component
@EnableScheduling
public class ScanVipExpireShopJob implements SchedulingConfigurer {

    @Autowired
    private ScanVipExpireShopRunnable scanVipExpireShopRunnable;


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        //每次启动时,执行一次线程;
        log.info("=============执行一次线程=================");
        scanVipExpireShopRunnable.run();

        taskRegistrar.addTriggerTask(scanVipExpireShopRunnable, triggerContext -> {
            //String cron = redisUtil.get("cron");
            //每个月的1号0点05分开始执行
            String cron = "0 0/30 * * * ? ";
            log.info("====扫描过期会员店铺，开始：{}, cron {}", new Date(), cron);
            CronTrigger trigger = new CronTrigger(cron);
            return trigger.nextExecutionTime(triggerContext);
        });
    }
}
