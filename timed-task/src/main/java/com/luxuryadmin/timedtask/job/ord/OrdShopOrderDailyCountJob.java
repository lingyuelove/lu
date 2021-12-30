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
 * 店铺每日订单统计
 *
 * @author nick
 */
@Slf4j
@Component
@EnableScheduling
public class OrdShopOrderDailyCountJob implements SchedulingConfigurer {

    @Autowired
    private OrdOrderService ordOrderService;

    /**
     * 店铺每日订单商品统计
     */
    //private static final String CRON = "0 */1 * * * ?";

    //每日凌晨0点15分执行店铺订单统计任务
    private static final String CRON = "0 15 0 ? * *";

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(() -> {
            Long startTime = System.currentTimeMillis();
            Date yesterDay = DateUtil.addDaysFromOldDate(new Date(),-1).getTime();
//            Date yesterDay = null;
//            try {
//                yesterDay = DateUtil.parse("2020-09-10","yyyy-MM-dd");
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
            ordOrderService.dailyCountShopOrder(yesterDay);
            Long endTime = System.currentTimeMillis();
            log.info("店铺每日订单商品统计的task，耗时：{}ms", endTime-startTime);
        }, triggerContext -> {
            CronTrigger trigger = new CronTrigger(CRON);
            return trigger.nextExecutionTime(triggerContext);
        });
    }
}
