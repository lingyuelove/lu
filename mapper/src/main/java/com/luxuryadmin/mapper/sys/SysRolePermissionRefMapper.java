package com.luxuryadmin.mapper.sys;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.sys.SysRolePermissionRef;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysRolePermissionRefMapper  extends BaseMapper {
    int deleteByRoleId(Integer id);

    /**
     * 根据条件查询角色权限
     * @param sysRolePermissionRef
     * @return
     */
    List<SysRolePermissionRef> listPermByRole(SysRolePermissionRef sysRolePermissionRef);

    /**
     * 删除权限id
     * @param permissionId
     * @return
     */
    int deleteRoleByPermissionId(Integer permissionId);

    void saveList(List<SysRolePermissionRef> list);
}