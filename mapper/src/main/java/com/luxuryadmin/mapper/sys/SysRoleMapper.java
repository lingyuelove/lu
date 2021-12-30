package com.luxuryadmin.mapper.sys;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.param.sys.ParamSysRole;
import com.luxuryadmin.vo.sys.VoSysRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysRoleMapper  extends BaseMapper {

    List<VoSysRole> listSysRole(ParamSysRole paramSysRole);
}