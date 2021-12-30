package com.luxuryadmin.mapper.sys;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.sys.SysVersion;
import com.luxuryadmin.param.sys.ParamSysVersion;
import com.luxuryadmin.vo.sys.VoSysVersion;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Mapper
public interface SysVersionMapper  extends BaseMapper {

    /**
     * 根据查询条件获取版本更新列表
     * @param paramSysVersion
     * @return
     */
    List<VoSysVersion> selectSysVersionList(ParamSysVersion paramSysVersion);

    /**
     * 根据平台查询数据库里的最新APP版本
     * @param params
     * @return
     */
    VoSysVersion selectSysVersionByPlatform(Map<String,Object> params);
}