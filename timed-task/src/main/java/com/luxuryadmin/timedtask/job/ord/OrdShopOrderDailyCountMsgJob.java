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
 * 推送店铺每日订单统计消息JOB
 *
 * @author nick
 */
@Slf4j
@Component
@EnableScheduling
public class OrdShopOrderDailyCountMsgJob implements SchedulingConfigurer {

    @Autowired
    private OrdOrderService ordOrderService;

    /**
     * 推送店铺每日订单统计消息
     */
    //private static final String CRON = "0 */5 * * * ?";

    //每日早上9点执行店铺日志推送统计任务
    private static final String CRON = "0 0 9 ? * *";

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(() -> {
            Long startTime = System.currentTimeMillis();
            Date yesterDay = DateUtil.addDaysFromOldDate(new Date(),-1).getTime();
//            Date yesterDay = null;
//            try {
//                yesterDay =
//                .parse("2020-09-10","yyyy-MM-dd");
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
            ordOrderService.pushDailyCountShopOrderMsg(yesterDay);
            Long endTime = System.currentTimeMillis();
            log.info("推送店铺每日订单统计消息，耗时：{}ms", endTime-startTime);
        }, triggerContext -> {
            CronTrigger trigger = new CronTrigger(CRON);
            return trigger.nextExecutionTime(triggerContext);
        });
    }
}
