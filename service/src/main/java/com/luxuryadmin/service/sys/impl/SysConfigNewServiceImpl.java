package com.luxuryadmin.service.sys.impl;

import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.constant.enums.EnumCode;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.common.utils.ThreadUtils;
import com.luxuryadmin.entity.shp.ShpUserShopRef;
import com.luxuryadmin.entity.sys.SysConfig;
import com.luxuryadmin.entity.sys.SysConfigNew;
import com.luxuryadmin.enums.sys.EnumSysConfigNew;
import com.luxuryadmin.mapper.sys.SysConfigMapper;
import com.luxuryadmin.mapper.sys.SysConfigNewMapper;
import com.luxuryadmin.service.ord.OrdTypeService;
import com.luxuryadmin.service.pro.*;
import com.luxuryadmin.service.shp.*;
import com.luxuryadmin.service.sys.SysConfigNewService;
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
public class SysConfigNewServiceImpl implements SysConfigNewService {

    @Resource
    private SysConfigNewMapper sysConfigNewMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public String getSysConfigByMasterKeyAndSubKey(EnumSysConfigNew enumSysConfigNew) {
        return sysConfigNewMapper.selectSysConfigByMasterKeyAndSubKey(enumSysConfigNew.getMasterConfigKey(),
                enumSysConfigNew.getSubConfigKey());
    }

    @Override
    public List<SysConfigNew> listSysConfigByType(String type) {
        return sysConfigNewMapper.listSysConfigByType(type);
    }

    @Override
    public void initSysConfigToRedis() {
        List<SysConfigNew> redisConfig = sysConfigNewMapper.listSysConfigByType("redis");
        //每次重启项目把不自增的配置信息,更新到redis缓存里面,redis的key值, 直接主键和子键拼接
        if (!LocalUtils.isEmptyAndNull(redisConfig)) {
            for (SysConfigNew conf : redisConfig) {
                // 0:不自增; 1:值自增;
                String autoAdd = conf.getAutoAdd();
                if (ConstantCommon.ZERO.equals(autoAdd)) {
                    //主键
                    String masterKey = conf.getMasterConfigKey();
                    //子键
                    String subKey = conf.getSubConfigKey();
                    //redisKey值
                    String key = masterKey + subKey;
                    String value = conf.getConfigValue();
                    redisUtil.set(key, value);
                }
            }
        }
    }
}
