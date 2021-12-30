package com.luxuryadmin.mapper.sys;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.sys.SysUser;
import com.luxuryadmin.param.sys.ParamSysUser;
import com.luxuryadmin.vo.sys.VoSysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper {

    List<VoSysUser> listSysUser(ParamSysUser paramSysUser);

    SysUser getByName(String username);

    /**
     * 获取运营帐号
     *
     * @return
     */
    List<VoSysUser> loadOpUser();

}