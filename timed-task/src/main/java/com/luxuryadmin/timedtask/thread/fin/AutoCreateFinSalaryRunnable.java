package com.luxuryadmin.timedtask.thread.fin;

import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.service.fin.FinSalaryService;
import com.luxuryadmin.service.shp.ShpShopService;
import com.luxuryadmin.service.shp.ShpUserShopRefService;
import com.luxuryadmin.vo.shp.VoEmployee;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 自动生成薪资(每个月1号0点0分过后, 自动生成该月的薪资记录)<br/>
 * 首先取该店铺上个月所有员工的薪资条作为参考,如果没有,则默认创建所有员工的薪资<br/>
 * 1.查找所有经营正常店铺;店铺状态>=0以上的状态;<br/>
 * 2.生成每一个在职员工的薪资条;每个员工,每个月只有一条工资条<br/>
 * 3.对于上个月已经有生成模板的店铺, 则直接取上个月的薪资条明细;
 *
 * @author monkey king
 * @date 2020-12-10 19:39:15
 */
@Component
@Slf4j
public class AutoCreateFinSalaryRunnable implements Runnable {

    @Autowired
    private ShpUserShopRefService shpUserShopRefService;

    @Autowired
    private FinSalaryService finSalaryService;

    @Autowired
    private ShpShopService shpShopService;

    @Override
    public void run() {
        Date nowDate = new Date();
        long st = System.currentTimeMillis();
        log.info("=====执行生成薪资线程========{}", DateUtil.format(nowDate));
        //1.查找所有经营正常店铺;店铺状态>=0以上的状态;
        List<Integer> shopIdList = shpShopService.queryShpShopIdList();
        if (LocalUtils.isEmptyAndNull(shopIdList)) {
            return;
        }

        Map<String, Date> map = DateUtil.getLastMonthFirstDayAndEndDay();
        //上个月的1号
        String startDate = DateUtil.formatShort(map.get("startDate"));
        //上个月的最后一天;
        String endDate = DateUtil.formatShort(map.get("endDate"));

        for (Integer shopId : shopIdList) {
            //该店铺目前所有在职员工的userId
            List<Integer> allUserId = shpUserShopRefService.listAllUserIdByShopId(shopId, "1");
            //先获取上个月已经创建好薪资的员工,如果存在, 则按照按名单来创建本月员工薪资
            List<Integer> userIdList = finSalaryService.listAlreadyCreateSalaryUserId(shopId, startDate, endDate);
            if (LocalUtils.isEmptyAndNull(userIdList)) {
                //上个月没有创建任何薪资,则本月创建所有员工的薪资
                userIdList = allUserId;
            }
            for (Integer userId : userIdList) {
                //判断该员工现在是否离职, 如果已经离职则不再创建;
                if (allUserId.contains(userId)) {
                    finSalaryService.initFinSalary(shopId, userId, nowDate);
                } else {
                    log.info("该员工已和该店铺脱离关系!shopId: {}, userId: {}", shopId, userId);
                }
            }
        }
        long et = System.currentTimeMillis();

        log.info("=====执行生成薪资线程=====结束===耗时: {}, 共{}个店铺", (et - st), shopIdList.size());
    }

    public static void main(String[] args) {
        try {
            Map<String, Date> map = DateUtil.getLastMonthFirstDayAndEndDay();
            //上个月的1号
            String startDate = DateUtil.formatShort(map.get("startDate"));
            //上个月的最后一天;
            String endDate = DateUtil.formatShort(map.get("endDate"));
            log.info("====startDate:{}, endDate:{}", startDate, endDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
