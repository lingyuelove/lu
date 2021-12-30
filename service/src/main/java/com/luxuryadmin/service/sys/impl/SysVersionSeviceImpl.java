package com.luxuryadmin.service.sys.impl;

import com.luxuryadmin.entity.op.OpPlatform;
import com.luxuryadmin.entity.sys.SysVersion;
import com.luxuryadmin.mapper.op.OpPlatformMapper;
import com.luxuryadmin.mapper.sys.SysVersionMapper;
import com.luxuryadmin.param.sys.ParamSysVersion;
import com.luxuryadmin.service.sys.SysVersionService;
import com.luxuryadmin.vo.sys.VoSysVersion;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @author: walkingPotato
 * @date: 2020-08-14 14:41
 * @email: dingxin_hz@163.com
 * @version: V1.0
 */
@Service
public class SysVersionSeviceImpl implements SysVersionService {

    @Resource
    private SysVersionMapper sysVersionMapper;

    @Resource
    private OpPlatformMapper opPlatformMapper;

    @Override
    public List<OpPlatform> queryAllOpPlatform() {
        return opPlatformMapper.selectAllOpPlatform();
    }

    @Override
    public List<VoSysVersion> querySysVersionList(ParamSysVersion paramSysVersion) {
        return sysVersionMapper.selectSysVersionList(paramSysVersion);
    }

    @Override
    public Integer addSysVersion(SysVersion sysVersion) throws Exception{
        return sysVersionMapper.saveObject(sysVersion);
    }

    /**
     * 更新版本控制
     *
     * @param sysVersion
     * @return
     */
    @Override
    public int updateSysVersion(SysVersion sysVersion) {
        sysVersion.setUpdateTime(new Date());
        return sysVersionMapper.updateObject(sysVersion);
    }

    /**
     * 逻辑删除版本控制
     *
     * @param sysVersion
     * @return
     */
    @Override
    public int deleteSysVersion(SysVersion sysVersion) {
        sysVersion.setUpdateTime(new Date());
        return sysVersionMapper.updateObject(sysVersion);
    }

    @Override
    public VoSysVersion querySysVersionByPlatform(String platform) {
        Map<String,Object> params = new HashMap<>();
        params.put("platform",platform);
        return sysVersionMapper.selectSysVersionByPlatform(params);
    }
}
