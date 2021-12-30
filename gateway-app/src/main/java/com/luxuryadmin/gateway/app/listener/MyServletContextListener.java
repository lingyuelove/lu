package com.luxuryadmin.gateway.app.listener;

import com.alibaba.fastjson.JSONObject;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.service.pro.ProCheckService;
import com.luxuryadmin.service.sys.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author monkey king
 * @date 2019-12-17 16:57:26
 */

@Slf4j
@WebListener
public class MyServletContextListener implements ServletContextListener {

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private RedisUtil redisUtil;


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
        log.info("==========项目初始化============");
        initApplicationParams();
        sysConfigService.initDatabase();
        //初始化店铺权限版本
        String key = ConstantRedisKey.SHP_PERM_VERSION;
        if (LocalUtils.isEmptyAndNull(redisUtil.get(key))) {
            redisUtil.set(key, "1");
        }

        //初始化店铺年费会员价格(元)
        String vipFee = ConstantRedisKey.VIP_FEE;
        if (LocalUtils.isEmptyAndNull(redisUtil.get(vipFee))) {
            redisUtil.set(vipFee, "998");
        }

        //支付接口开关,默认关闭
        String paySwitch = ConstantRedisKey.PAY_SWITCH;
        if (LocalUtils.isEmptyAndNull(redisUtil.get(paySwitch))) {
            redisUtil.set(paySwitch, "off");
        }

        //小程序水印图片地址
        String mpCoverImg = ConstantRedisKey.MP_COVER_IMG;
        if (LocalUtils.isEmptyAndNull(redisUtil.get(mpCoverImg))) {
            redisUtil.set(mpCoverImg, "https://file.luxuryadmin.com/default/mpCoverImg.png");
        }

        String smallSize = ConstantRedisKey.IMG_SIZE_SMALL;
        if (LocalUtils.isEmptyAndNull(redisUtil.get(smallSize))) {
            redisUtil.set(smallSize, "?x-oss-process=image/resize,p_20");
        }

        String bigSize = ConstantRedisKey.IMG_SIZE_BIG;
        if (LocalUtils.isEmptyAndNull(redisUtil.get(bigSize))) {
            redisUtil.set(bigSize, "?x-oss-process=image/resize,p_50");
        }

        //付费模块地址
        String payUrl = ConstantRedisKey.PAY_URL;
        if (LocalUtils.isEmptyAndNull(redisUtil.get(payUrl))) {
            //查询数据库,付费模块地址,临时内置,后期改成数据库获取
            List<String> urlList = new ArrayList<>();
            urlList.add("/shop/user/pro/listOnSaleProduct");
            urlList.add("/shop/user/biz/listLeaguerProduct");
            urlList.add("/shop/user/pro/listLockProduct");
            urlList.add("/shop/user/ord/listOrder");
            urlList.add("/shop/user/service/listShpService");
            urlList.add("/shop/user/pro/listProTemp");
            urlList.add("/shop/user/pro/listOwnProductStorehouse");
            urlList.add("/shop/user/pro/listEntrustProductStorehouse");
            urlList.add("/shop/user/pro/listOtherProductStorehouse");
            urlList.add("/shop/user/pro/listProductPawn");
            urlList.add("/shop/user/pro/listAllProductStorehouse");
            //销售分析
            urlList.add("/shop/user/data/saleRank/listSaleRank");
            urlList.add("/shop/user/loadVoEmployee");
            urlList.add("/shop/user/fin/loadSalary");
            //其它记账
            urlList.add("/shop/user/fin/record/listFinShopRecord");
            urlList.add("/shop/user/operatelog/listShpOperateLog");
            //锁单中
            urlList.add("/shop/user/pro/loadLockProduct");
            //盘点
            urlList.add("/shop/user/pro/check/getCheckListForApi");
            //库存预警 商品到期提醒
            urlList.add("/shop/user/pro/expired/getExpiredList");
            //删除列表
            urlList.add("/shop/user/delete/getProductOrOrderForDeletePage");
            redisUtil.set(payUrl, JSONObject.toJSONString(urlList));
        }
        //判断盘点状态地址
        String checkUrl = ConstantRedisKey.CHECK_URL;
        if (LocalUtils.isEmptyAndNull(redisUtil.get(checkUrl))) {
            //查询数据库,判断盘点状态地址,临时内置,后期改成数据库获取
            List<String> urlList = new ArrayList<>();
            //订单
            //初始化开单页面
//            urlList.add("/shop/user/ord/initOrderParam");
            //确认开单
            urlList.add("/shop/user/ord/confirmOrder");
            //快速开单
//            urlList.add("/shop/user/ord/confirmQuickOrder");
            //取消开单，退货退款
            urlList.add("/shop/user/ord/cancelOrder");
            //修改订单信息
            urlList.add("/shop/user/ord/updateOrder");
            //修改订单改结款状态
            urlList.add("/shop/user/ord/updateOrderEntrust");
            //删除订单
            urlList.add("/shop/user/ord/deleteOrder");
            //批量删除订单
            urlList.add("/shop/user/ord/deleteBatchOrder");

            //商品
            //初始化上传商品页面
            urlList.add("/shop/user/pro/initUpload");
            //上传商品
            urlList.add("/shop/user/pro/uploadProduct");
            //根据业务id;更新商品
            urlList.add("/shop/user/pro/updateProduct");
            //赎回质押商品
//            urlList.add("/shop/user/pro/redeemProduct");
            //根据业务id;上架商品;(支持批量;)
//            urlList.add("/shop/user/pro/releaseProduct");
            //根据业务id;下架商品;(支持批量;)
//            urlList.add("/shop/user/pro/backOffProduct");
            //一键全店【全部上架】
            urlList.add("/shop/user/pro/oneKeyReleaseProduct");
            //一键全店【全部下架】
            urlList.add("/shop/user/pro/oneKeyBackOffProduct");
            //根据业务id;删除商品;(支持批量;)
            urlList.add("/shop/user/pro/deleteProduct");
            //根据业务id;把【质押】商品转移到仓库
            urlList.add("/shop/user/pro/movePrivateProToStore");
            //锁定商品(锁单)
            urlList.add("/shop/user/pro/lockProduct");
            //解锁商品(解锁)
            urlList.add("/shop/user/pro/unlockProduct");
            redisUtil.delete(checkUrl);
            redisUtil.set(checkUrl, JSONObject.toJSONString(urlList));
        }
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
    }


}