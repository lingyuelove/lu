package com.luxuryadmin.timedtask.job;

import com.luxuryadmin.timedtask.thread.fin.AutoCreateFinSalaryRunnable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.util.Date;

/**
 * @author monkey king
 * @date 2020-12-12 0:59:40
 */
@Slf4j
@Configuration
@EnableScheduling
public class InitFinSalaryEveryMonthJob implements SchedulingConfigurer {

    @Autowired
    private AutoCreateFinSalaryRunnable autoCreateFinSalaryRunnable;


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        //每次启动时,执行一次初始化薪资,防止时间超过1号,线程没执行而采取的补救措施;
        log.info("=============执行一次薪资初始化=================");
        autoCreateFinSalaryRunnable.run();

        taskRegistrar.addTriggerTask(autoCreateFinSalaryRunnable, triggerContext -> {
            //String cron = redisUtil.get("cron");
            //每个月的1号0点05分开始执行
            String cron = "0 5 0 1 * ? ";
            log.info("生成薪资定时任务，开始：{}, cron {}", new Date(), cron);
            CronTrigger trigger = new CronTrigger(cron);
            return trigger.nextExecutionTime(triggerContext);
        });
    }

}
