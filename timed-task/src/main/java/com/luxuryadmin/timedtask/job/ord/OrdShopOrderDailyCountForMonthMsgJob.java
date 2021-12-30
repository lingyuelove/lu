package com.luxuryadmin.timedtask.job.ord;

import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.service.ord.OrdOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @PackgeName: com.luxuryadmin.timedtask.job.ord
 * @ClassName: 推送店铺每月订单统计消息JOB
 * @Author: ZhangSai
 * Date: 2021/6/28 16:34
 */
@Slf4j
@Component
@EnableScheduling
public class OrdShopOrderDailyCountForMonthMsgJob  implements SchedulingConfigurer {
    @Autowired
    private OrdOrderService ordOrderService;

    /**
     * 推送店铺每日订单统计消息
     */
    //private static final String CRON = "0 */5 * * * ?";

    //每月1号早上9点十分执行店铺月统计推送统计任务
    private static final String CRON = "0 10 9 1 * ? ";

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(() -> {
            Long startTime = System.currentTimeMillis();
            Date date =new Date();

            ordOrderService.pushDailyCountShopOrderMsgForMonth(date);
            Long endTime = System.currentTimeMillis();
            log.info("推送店铺每月订单统计消息，耗时：{}ms", endTime-startTime);
        }, triggerContext -> {
            CronTrigger trigger = new CronTrigger(CRON);
            return trigger.nextExecutionTime(triggerContext);
        });
    }
}
