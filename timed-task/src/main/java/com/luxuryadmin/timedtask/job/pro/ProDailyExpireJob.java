package com.luxuryadmin.timedtask.job.pro;

import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.service.pro.ProProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

/**
 * 质押商品每日过期JOB
 *
 * @author nick
 */
@Slf4j
@Component
@EnableScheduling
public class ProDailyExpireJob implements SchedulingConfigurer {

    @Autowired
    private ProProductService proProductService;

    /**
     * 质押商品每日过期JOB
     */
    //private static final String CRON = "0 */1 * * * ?";

    //每日凌晨0点5分执行店铺订单统计任务
    private static final String CRON = "0 5 0 ? * *";

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        try {
            log.info("========启动时，执行一次扫描到期的质押商品===========");
            proProductService.dailyExpireProd(DateUtil.getStartTimeOfDay(new Date()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        scheduledTaskRegistrar.addTriggerTask(() -> {
            Long startTime = System.currentTimeMillis();
            Date jobDayStart = null;
            try {
                jobDayStart = DateUtil.getStartTimeOfDay(new Date());
            } catch (ParseException e) {
                log.error("" + e);
            }

            proProductService.dailyExpireProd(jobDayStart);
            Long endTime = System.currentTimeMillis();
            log.info("商品每日过期的task，耗时：{}ms", endTime - startTime);
        }, triggerContext -> {
            CronTrigger trigger = new CronTrigger(CRON);
            return trigger.nextExecutionTime(triggerContext);
        });
    }
}
