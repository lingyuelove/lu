package com.luxuryadmin.service.sys.impl;

import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.sys.SysRole;
import com.luxuryadmin.entity.sys.SysRolePermissionRef;
import com.luxuryadmin.entity.sys.SysUserRoleRef;
import com.luxuryadmin.mapper.sys.SysRoleMapper;
import com.luxuryadmin.mapper.sys.SysRolePermissionRefMapper;
import com.luxuryadmin.mapper.sys.SysUserRoleRefMapper;
import com.luxuryadmin.param.sys.ParamSysRole;
import com.luxuryadmin.param.sys.ParamSysRoleAdd;
import com.luxuryadmin.service.sys.SysRoleService;
import com.luxuryadmin.vo.sys.VoSysRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysRolePermissionRefMapper sysRolePermissionRefMapper;

    @Resource
    private SysUserRoleRefMapper sysUserRoleRefMapper;

    @Override
    public List<VoSysRole> querySysRoleList(ParamSysRole paramSysRole) {
        List<VoSysRole> voSysRoles = sysRoleMapper.listSysRole(paramSysRole);
        for (VoSysRole voSysRole : voSysRoles) {
            SysRolePermissionRef sysRolePermissionRef = new SysRolePermissionRef();
            sysRolePermissionRef.setFkSysRoleId(voSysRole.getId());
            List<SysRolePermissionRef> list = sysRolePermissionRefMapper.listPermByRole(sysRolePermissionRef);
            List<String> pers = list.stream().map((permissionRef) -> permissionRef.getFkSysPermissionId().toString()).collect(Collectors.toList());
            voSysRole.setPerms(pers);
        }
        return voSysRoles;
    }

    @Override
    public int addSysRole(SysRole sysRole) {
        sysRole.setInsertAdmin(1);
        int row = sysRoleMapper.saveObject(sysRole);

        return row;
    }

    @Override
    public int updateSysRole(SysRole sysRole) {
        sysRole.setUpdateTime(new Date());
        int row = sysRoleMapper.updateObject(sysRole);

        return row;
    }

    @Override
    public int deleteSysRole(SysRole sysRole) {

        int row = sysRoleMapper.deleteObject(sysRole.getId());

        return row;
    }

    @Override
    public int deleteSysRoleById(Integer id) {

        int row = sysRoleMapper.deleteObject(id);
        return row;
    }

    @Override
    public int addSysRolePerm(ParamSysRoleAdd paramSysRole) {

        List<String> perms = paramSysRole.getPerms();
        if (LocalUtils.isEmptyAndNull(paramSysRole.getPerms()) && perms.size()<=0){
            return 0;
        }
//        List<String> perms = Arrays.asList(paramSysRole.getPerms().split(","));
        int row = 0;

        List<SysRolePermissionRef> list =new ArrayList<>();
        for (String perm : perms) {
            SysRolePermissionRef permissionRef = new SysRolePermissionRef();
            permissionRef.setFkSysRoleId(Integer.parseInt(paramSysRole.getId()));
            permissionRef.setFkSysPermissionId(Integer.parseInt(perm));
            permissionRef.setInsertAdmin(paramSysRole.getInsertAdmin());
            permissionRef.setDel("0");
            permissionRef.setVersions(0);
            permissionRef.setInsertTime(new Date()) ;
            list.add(permissionRef);
            row++;
        }

        sysRolePermissionRefMapper.saveList(list);
        return row;
    }

    @Override
    public int deleteSysRolePrem(Integer roleId) {
        return sysRolePermissionRefMapper.deleteByRoleId(roleId);
    }

    @Override
    public List<SysUserRoleRef> queryUserRole(SysUserRoleRef userRoleRef) {
        return sysUserRoleRefMapper.listUserRole(userRoleRef);
    }

    @Override
    public SysRole getRoleById(Integer roleId) {
        SysRole sysRole =(SysRole)sysRoleMapper.getObjectById(roleId);
        return sysRole;
    }

}
