package com.luxuryadmin.service.sys.impl;

import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.constant.enums.EnumCode;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.common.utils.ThreadUtils;
import com.luxuryadmin.entity.biz.BizShopUnion;
import com.luxuryadmin.entity.shp.ShpShop;
import com.luxuryadmin.entity.shp.ShpUserShopRef;
import com.luxuryadmin.entity.sys.SysConfig;
import com.luxuryadmin.mapper.biz.BizShopUnionMapper;
import com.luxuryadmin.mapper.sys.SysConfigMapper;
import com.luxuryadmin.service.biz.BizShopUnionService;
import com.luxuryadmin.service.ord.OrdTypeService;
import com.luxuryadmin.service.pro.*;
import com.luxuryadmin.service.shp.*;
import com.luxuryadmin.service.sys.SysConfigService;
import com.luxuryadmin.service.sys.SysEnumService;
import com.luxuryadmin.vo.shp.VoUserShopBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author monkey king
 * @date 2019-12-10 16:34:26
 */
@Slf4j
@Service
public class SysConfigServiceImpl implements SysConfigService {
    @Resource
    private SysConfigMapper sysConfigMapper;

    @Resource
    private ShpShopService shpShopService;

    @Autowired
    private SysEnumService sysEnumService;

    @Autowired
    private ShpUserNumberService shpUserNumberService;

    @Autowired
    private ShpShopNumberService shpShopNumberService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ProClassifyService proClassifyService;

    @Autowired
    private ProAttributeService proAttributeService;

    @Autowired
    private OrdTypeService ordTypeService;

    @Autowired
    private ShpUserTypeService shpUserTypeService;

    @Autowired
    private ProSourceService proSourceService;

    @Autowired
    private ProAccessoryService proAccessoryService;

    @Autowired
    private ProSaleChannelService proSaleChannelService;

    @Autowired
    private ShpUserPermissionTplService shpUserPermissionTplService;

    @Autowired
    private ShpUserShopRefService shpUserShopRefService;

    @Resource
    private BizShopUnionService bizShopUnionService;


    @Override
    public SysConfig getSysConfigById1() {
        SysConfig sysConfig = sysConfigMapper.getSysConfigById1();
        //如果不存在则重新初始化
        if (LocalUtils.isEmptyAndNull(sysConfig)) {
            log.info("=========初始化SysConfig==========");
            sysConfig = createSysConfig();
        }
        return sysConfig;
    }

    private SysConfig createSysConfig() {
        SysConfig sysConfig = new SysConfig();
        sysConfig.setId(1);
        sysConfig.setParamAesSwitch("0");
        sysConfig.setParamSignSwitch("0");
        sysConfig.setInsertTime(new Date());
        sysConfig.setIpSmsLimit(1);
        sysConfig.setSmsLimit(1);
        sysConfig.setInsertAdmin(0);
        sysConfig.setVersions(1);
        sysConfig.setDel("0");
        sysConfig.setRemark(ConstantCommon.AUTO_REMARK);
        try {
            int rows = sysConfigMapper.saveObject(sysConfig);
            if (rows > 0) {
                return sysConfig;
            }
        } catch (Exception e) {
            log.error("=============系统初始化配置表失败==============" + e.getMessage(), e);
        }
        throw new MyException(EnumCode.ERROR_SYSTEM_INIT);
    }

    @Override
    public Map initSysConfig() {
        HashMap<String, String> hashMap;
        try {
            SysConfig sysConfig = getSysConfigById1();
            String isAesValue = sysConfig.getParamAesSwitch();
            String isSignValue = sysConfig.getParamSignSwitch();
            String smsLimit = sysConfig.getSmsLimit() + "";
            String ipSmsLimit = sysConfig.getIpSmsLimit() + "";
            redisUtil.set(ConstantRedisKey.IS_AES_KEY, isAesValue);
            redisUtil.set(ConstantRedisKey.IS_SIGN_KEY, isSignValue);
            redisUtil.set(ConstantRedisKey.IP_SMS_LIMIT, ipSmsLimit);
            redisUtil.set(ConstantRedisKey.SMS_LIMIT, smsLimit);
            //redisUtil.set(ConstantRedisKey.UPGRADE_VERSION, "2.4.0");
            //redisUtil.set(ConstantRedisKey.UPGRADE_MSG, "当前不是最新版本，为了更好体验，请关注【奢当家】微信公众号，或去App Store下载最新版！");
            //redisUtil.set(ConstantRedisKey.UPGRADE_URL, "https://apps.apple.com/cn/app/id1526802429");
            hashMap = new HashMap<>(16);
            hashMap.put("is_aes", isAesValue);
            hashMap.put("is_sign", isSignValue);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MyException(EnumCode.ERROR_SYSTEM_INIT);
        }
        return hashMap;
    }

    @Override
    public void resetAllTpl() {
        sysEnumService.resetSysEnumByType(null);
    }

    @Override
    public void initDatabase() {
        log.info("============初始化数据库=========项目环境: " + ConstantCommon.springProfilesActive);
        initSysConfig();
        sysEnumService.initSysEnumByType(null);
        initShopNumberAndUserNumber();
        bizShopUnionService.refreshBizShopUnionForRedis();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void restartBossShpUserShopRef(int shopId, int userId) {

        try {
            ShpUserShopRef userShopRef = shpUserShopRefService.getShpUserShopRefByUserId(shopId, userId);
            if (!LocalUtils.isEmptyAndNull(userShopRef)) {
                userShopRef.setFkShpUserTypeId(-9);
                userShopRef.setUpdateTime(new Date());
                userShopRef.setUpdateAdmin(0);
                userShopRef.setRemark("系统自动关联");
                shpUserShopRefService.updateUserShopRef(userShopRef);
            }
        } catch (Exception e) {
            log.info("=====重置boss的店铺关系: shopId:{};userId{}", shopId, userId);
            log.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

    }

    @Override
    public void initShopBaseData() {
        List<VoUserShopBase> shopList = shpShopService.listAllShopIdAndUserId();
        for (VoUserShopBase shop : shopList) {
            int userId = shop.getUserId();
            int shopId = shop.getShopId();
            try {
                //初始化店铺分类
                proClassifyService.initProClassifyByShopIdAndUserId(shopId, userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                //初始化店铺属性
                proAttributeService.initProAttributeByShopIdAndUserId(shopId, userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                //初始化订单类型
                ordTypeService.initOrdTypeByShopIdAndUserId(shopId, userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                //初始化商品来源
                proSourceService.initProSourceByShopIdAndUserId(shopId, userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                //初始化附件表
                proAccessoryService.initProAccessoryByShopIdAndUserId(shopId, userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                //初始化店铺权限模板(系统自动创建)
                shpUserPermissionTplService.initShopSystemPerm(shopId, userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                //初始化销售渠道
                proSaleChannelService.initProSaleChannelByShopIdAndUserId(shopId, userId);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public void initVipExpire() {
        List<ShpShop> shopList = shpShopService.listShpShopForInitVip();
        try {
            if (!LocalUtils.isEmptyAndNull(shopList)) {

                for (ShpShop shop : shopList) {
                    String isMember = shop.getIsMember();
                    shop.setMemberState(1);
                    if ("yes".equalsIgnoreCase(isMember)) {
                        //会员;
                        shop.setMemberState(2);
                    }
                    Date tryEndTime = shop.getTryEndTime();
                    Date payEndTime = shop.getPayEndTime();

                    //是否会员
                    String isMemberKey = ConstantRedisKey.getShopMemberRedisKeyByShopId(shop.getId());
                    redisUtil.set(isMemberKey, isMember);

                    //会员状态
                    String memberStateKey = ConstantRedisKey.getMemberStateRedisKeyByShopId(shop.getId());
                    redisUtil.set(memberStateKey, shop.getMemberState().toString());

                    //会员过期时间
                    Date vipExpire = LocalUtils.isEmptyAndNull(payEndTime) ? tryEndTime : payEndTime;
                    String vipExpireKey = ConstantRedisKey.getVipExpireRedisKeyByShopId(shop.getId());
                    redisUtil.set(vipExpireKey, DateUtil.formatShort(vipExpire));

                    shpShopService.updateShpShop(shop);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    /**
     * 如果没有用户编号池,则初始化;启用后台线程进行初始化;
     */
    private void initShopNumberAndUserNumber() {
        ThreadUtils threadUtils = ThreadUtils.getInstance();
        if (ConstantCommon.DEV.equals(ConstantCommon.springProfilesActive)) {
            //开发环境;
            threadUtils.setMonitor(false);
            threadUtils.monitorThread();
        }

        threadUtils.executorService.execute(() -> {
            Integer lastUserNumber = shpUserNumberService.getLastUserNumber();
            Integer lastShopNumber = shpShopNumberService.getLastShopNumber();
            //数据库编号池里还没有数据
            if (null == lastUserNumber) {
                shpUserNumberService.generateUserRandomNumber(10000, 99999);
                redisUtil.delete(ConstantRedisKey.SHP_USER_NUMBER_ID);
            }

            if (null == lastShopNumber) {
                shpShopNumberService.generateShopRandomNumber(10000, 99999);
                redisUtil.delete(ConstantRedisKey.SHP_SHOP_NUMBER_ID);
            }
        });
    }

}
