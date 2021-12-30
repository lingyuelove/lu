package com.luxuryadmin.mapper.sys;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.sys.SysConfig;
import com.luxuryadmin.entity.sys.SysConfigNew;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Administrator
 */
@Mapper
public interface SysConfigMapper extends BaseMapper {

    /**
     * 获取系统的配置文件; 获取id等于1的配置文件
     * @return
     */
    SysConfig getSysConfigById1();

}