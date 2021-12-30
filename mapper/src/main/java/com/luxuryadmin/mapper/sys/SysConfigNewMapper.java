package com.luxuryadmin.mapper.sys;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.sys.SysConfigNew;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统配置表
 *
 * @author Administrator
 */
@Mapper
public interface SysConfigNewMapper extends BaseMapper {

    /**
     * 根据系统配置枚举值查询value值
     *
     * @param masterConfigKey
     * @param subConfigKey
     * @return
     */
    String selectSysConfigByMasterKeyAndSubKey(@Param("masterConfigKey") String masterConfigKey,
                                               @Param("subConfigKey") String subConfigKey);

    /**
     * 根据类型来查找key值
     *
     * @param type
     * @return
     */
    List<SysConfigNew> listSysConfigByType(String type);
}