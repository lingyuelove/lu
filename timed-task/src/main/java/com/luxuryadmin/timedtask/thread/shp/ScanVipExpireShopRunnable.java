package com.luxuryadmin.timedtask.thread.shp;

import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.entity.shp.ShpShop;
import com.luxuryadmin.service.shp.ShpShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 扫描过期店铺,设置过期状态
 *
 * @author qwy
 */
@Slf4j
@Component
public class ScanVipExpireShopRunnable implements Runnable {

    @Autowired
    private ShpShopService shpShopService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void run() {

        int i = 1;
        for (; ; ) {
            String expireTime = DateUtil.format(new Date());
            PageHelper.startPage(1, 50);
            List<ShpShop> listShop = shpShopService.listVipExpireShop(expireTime);
            if (LocalUtils.isEmptyAndNull(listShop)) {
                log.info("====执行【扫描过期会员店铺】线程====结束=====时间参数：{},当前页:{}", expireTime, i);
                break;
            }
            log.info("====执行【扫描过期会员店铺】线程，时间参数：{},当前页:{}，执行数量：{}", expireTime, i, listShop.size());
            for (ShpShop shpShop : listShop) {
                int myShopId = shpShop.getId();
                shpShop.setIsMember("no");
                shpShop.setMemberState(0);
                shpShop.setUpdateTime(new Date());
                shpShopService.updateShpShop(shpShop);
                //是否会员; no:非会员 | yes:会员
                String isMemberKey = ConstantRedisKey.getShopMemberRedisKeyByShopId(myShopId);
                redisUtil.set(isMemberKey, "no");

                //会员状态: 0:非会员(会员已过期); 1:体验会员;2:正式会员;3:靓号会员
                String memberStateKey = ConstantRedisKey.getMemberStateRedisKeyByShopId(myShopId);
                redisUtil.set(memberStateKey, "0");
            }
            i++;
        }

    }
}
