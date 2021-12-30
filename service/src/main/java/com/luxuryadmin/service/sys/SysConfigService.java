package com.luxuryadmin.service.sys;

import com.luxuryadmin.entity.sys.SysConfig;
import com.luxuryadmin.enums.sys.EnumSysConfigNew;

import java.util.Map;

/**
 * 系统配置表;配置系统的一些初始化信息;
 *
 * @author monkey king
 * @date 2019-12-05 14:53:46
 */
public interface SysConfigService {
    /**
     * 获取系统的配置文件; 获取id等于1的配置文件
     *
     * @return
     */
    SysConfig getSysConfigById1();


    /**
     * 获取开关的值
     *
     * @return
     */
    Map initSysConfig();

    /**
     * 重置所有模板数据
     */
    void resetAllTpl();

    /**
     * 初始化数据库;加载默认模板;
     */
    void initDatabase();

    /**
     * 重置boss的店铺关系
     *
     * @param shopId 店铺id
     * @param userId 用户id
     */
    void restartBossShpUserShopRef(int shopId, int userId);

    /**
     * 初始化店铺基础数据;<br/>
     * 商品分类;商品来源;订单类型;销售渠道等等
     */
    void initShopBaseData();

    void initVipExpire();

}
