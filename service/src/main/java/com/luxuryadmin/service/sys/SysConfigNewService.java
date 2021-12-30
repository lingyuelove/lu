package com.luxuryadmin.service.sys;

import com.luxuryadmin.entity.sys.SysConfig;
import com.luxuryadmin.entity.sys.SysConfigNew;
import com.luxuryadmin.enums.sys.EnumSysConfigNew;

import java.util.List;
import java.util.Map;

/**
 * 系统配置表;配置系统的一些初始化信息;
 *
 * @author monkey king
 * @date 2019-12-05 14:53:46
 */
public interface SysConfigNewService {

    /**
     * 根据系统配置枚举值查询value值
     *
     * @param invitePage
     * @return
     */
    String getSysConfigByMasterKeyAndSubKey(EnumSysConfigNew invitePage);

    /**
     * 根据类型来查找key值
     *
     * @param type
     * @return
     */
    List<SysConfigNew> listSysConfigByType(String type);

    /**
     * 初始化配置信息到redis里面
     */
    void initSysConfigToRedis();
}
