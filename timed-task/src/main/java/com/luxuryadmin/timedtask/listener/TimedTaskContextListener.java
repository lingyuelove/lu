package com.luxuryadmin.timedtask.listener;

import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.common.utils.ThreadUtils;
import com.luxuryadmin.service.sys.SysConfigService;
import com.luxuryadmin.timedtask.thread.fin.AutoCreateFinSalaryRunnable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author monkey king
 * @date 2019-12-17 16:57:26
 */

@Slf4j
@WebListener
public class TimedTaskContextListener implements ServletContextListener {

    /**
     * 运行环境
     */
    @Value("${spring.profiles.active}")
    private String springProfilesActive;

    /**
     * 阿里云oss域名
     */
    @Value("${oss.domain}")
    private String ossDomain;

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        log.info("==========timedTask项目初始化============{}", DateUtil.format(new Date()));
        initApplicationParams();
        //ThreadUtils.getInstance().executorService.schedule(autoCreateFinSalaryRunnable, 10000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
        log.info("====项目摧毁===: contextDestroyed");
    }

    private void initApplicationParams() {
        log.info("====================初始化ConstantCommon非常量参数================");
        ConstantCommon.springProfilesActive = springProfilesActive;
        ConstantCommon.ossDomain = ossDomain;
        log.info("==================项目环境: " + ConstantCommon.springProfilesActive);
    }


}