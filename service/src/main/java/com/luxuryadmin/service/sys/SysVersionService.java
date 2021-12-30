package com.luxuryadmin.service.sys;

import com.luxuryadmin.entity.op.OpPlatform;
import com.luxuryadmin.entity.sys.SysVersion;
import com.luxuryadmin.param.sys.ParamSysVersion;
import com.luxuryadmin.vo.sys.VoSysVersion;

import java.util.List;

/**
 * @Description: 系统版本Service
 * @author: walkingPotato
 * @date: 2020-08-14 14:39
 * @email: dingxin_hz@163.com
 * @version: V1.0
 */
public interface SysVersionService {

    /**
     * 查询所有的APP平台
     * @return
     */
    List<OpPlatform> queryAllOpPlatform();

    /**
     * 根据条件查询所有的系统版本记录
     * @param paramSysVersion
     * @return
     */
    List<VoSysVersion> querySysVersionList(ParamSysVersion paramSysVersion);

    /**
     * 增加版本控制
     * @param sysVersion
     * @return
     */
    Integer addSysVersion(SysVersion sysVersion) throws Exception;

    /**
     * 更新版本控制
     * @param sysVersion
     * @return
     */
    int updateSysVersion(SysVersion sysVersion);

    /**
     * 逻辑删除版本控制
     * @param sysVersionDel
     * @return
     */
    int deleteSysVersion(SysVersion sysVersionDel);

    /**
     * 根据平台查询数据库里的最新APP版本
     * @param platform
     * @return
     */
    VoSysVersion querySysVersionByPlatform(String platform);
}
