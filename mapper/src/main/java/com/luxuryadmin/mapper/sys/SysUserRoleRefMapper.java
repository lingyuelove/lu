package com.luxuryadmin.mapper.sys;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.sys.SysUserRoleRef;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysUserRoleRefMapper  extends BaseMapper {

    int deleteByUserId(Integer id);

    /**
     * 条件查询列表
     * @param userRoleRef
     * @return
     */
    List<SysUserRoleRef> listUserRole(SysUserRoleRef userRoleRef);
}