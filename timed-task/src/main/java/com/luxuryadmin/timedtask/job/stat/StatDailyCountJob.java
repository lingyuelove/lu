package com.luxuryadmin.timedtask.job.stat;

import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.service.stat.StatDailyCountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 运营统计数据生成JOB
 *
 * @author nick
 */
@Slf4j
@Component
@EnableScheduling
public class StatDailyCountJob implements SchedulingConfigurer {

    @Autowired
    private StatDailyCountService statDailyCountService;

    /**
     * 运营统计数据生成JOB
     */
    //private static final String CRON = "0 */1 * * * ?";

    //每日凌晨0点15分执行店铺订单统计任务
    private static final String CRON = "0 */15 * * * ?";

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(() -> {
            Long startTime = System.currentTimeMillis();
            //考虑00:00分执行的任务，仍需要再统计一次前一天最后【任务间隔周期】时间内的数据
            //所以要减1分钟
            Date countDate = DateUtil.addMinuteFromOldDate(new Date(),-1).getTime();
            //statDailyCountService.dailyCountStat(countDate);
            Long endTime = System.currentTimeMillis();
            log.info("运营统计数据生成JOB，耗时：{}ms", endTime-startTime);
        }, triggerContext -> {
            CronTrigger trigger = new CronTrigger(CRON);
            return trigger.nextExecutionTime(triggerContext);
        });
    }
}
