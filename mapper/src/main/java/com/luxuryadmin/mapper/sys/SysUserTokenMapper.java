package com.luxuryadmin.mapper.sys;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.sys.SysUserToken;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserTokenMapper  extends BaseMapper {

    /**
     * 根据token查找SysUserToken
     * @param token {@link SysUserToken}
     */
    SysUserToken getSysUserTokenByToken(String token);
}