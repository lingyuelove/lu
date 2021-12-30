package com.luxuryadmin.service.sys;

import com.luxuryadmin.entity.sys.SysRole;
import com.luxuryadmin.entity.sys.SysUserRoleRef;
import com.luxuryadmin.param.sys.ParamSysRole;
import com.luxuryadmin.param.sys.ParamSysRoleAdd;
import com.luxuryadmin.vo.sys.VoSysRole;

import java.util.List;

/**
 *
 */
public interface SysRoleService {
    /**
     * 查询角色列表 条件：角色编号 角色名称 创建起止时间 状态
     * @param paramSysRole
     * @return
     */
    List<VoSysRole> querySysRoleList(ParamSysRole paramSysRole);

    /**
     * 新增角色
     * @param sysRole
     * @return
     */
    int addSysRole(SysRole sysRole);

    /**
     * 根据id修改角色
     * @param sysRole
     * @return
     */
    int updateSysRole(SysRole sysRole);

    /**
     * 根据id删除角色
     * @param sysRole
     * @return
     */
    int deleteSysRole(SysRole sysRole);

    /**
     * 根据id删除角色
     * @param id
     * @return
     */
    int deleteSysRoleById(Integer id);
    /**
     * 增加角色权限
     * @param paramSysRole
     * @return
     */
    int addSysRolePerm(ParamSysRoleAdd paramSysRole);

    /**
     * 删除角色权限
     * @param roleId
     * @return
     */
    int deleteSysRolePrem(Integer roleId);

    /**
     * 根据条件查询账号角色列表
     * @param userRoleRef
     * @return
     */
    List<SysUserRoleRef> queryUserRole(SysUserRoleRef userRoleRef);

    /**
     * 获取角色详情
     * @return
     */
    SysRole getRoleById(Integer roleId);
}
