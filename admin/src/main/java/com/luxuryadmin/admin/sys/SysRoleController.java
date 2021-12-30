package com.luxuryadmin.admin.sys;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.aop.check.RequiresPerm;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.sys.SysRole;
import com.luxuryadmin.entity.sys.SysUserRoleRef;

import com.luxuryadmin.param.sys.ParamSysPermDelete;
import com.luxuryadmin.param.sys.ParamSysRole;
import com.luxuryadmin.param.sys.ParamSysRoleAdd;
import com.luxuryadmin.param.sys.ParamSysRoleUpdate;
import com.luxuryadmin.service.sys.SysRoleService;
import com.luxuryadmin.vo.sys.VoSysRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/sys/role")
@Api(tags = {"5.【系统管理】模块","5.【系统管理】模块"}, description = "/sys/role | 平台后台系统模块 角色管理 ")
public class SysRoleController extends BaseController {

    @Autowired
    private SysRoleService sysRoleService;

    @RequestMapping(value = "/listSysRole", method = RequestMethod.GET)
    @ApiOperation(
            value = "分页查询角色列表;",
            notes = "分页查询角色列表;",
            httpMethod = "GET")
    @RequiresPerm(value = "system:role")
    public BaseResult querySysRoleList(@Valid ParamSysRole paramSysRole, BindingResult result){
        servicesUtil.validControllerParam(result);
        PageHelper.startPage(paramSysRole.getPageNum(), paramSysRole.getPageSize());
        List<VoSysRole> voSysRoles = sysRoleService.querySysRoleList(paramSysRole);
        PageInfo pageInfo = new PageInfo(voSysRoles);
        return LocalUtils.getBaseResult(pageInfo);
    }

    @RequestMapping(value = "/addSysRole", method = RequestMethod.POST)
    @ApiOperation(
            value = "增加角色;",
            notes = "增加角色;",
            httpMethod = "POST")
    public BaseResult addSysRole( @Valid ParamSysRoleAdd paramSysRole, BindingResult result){
        servicesUtil.validControllerParam(result);
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(paramSysRole,sysRole);
        Integer adminUserId =getAdminUserId();
        sysRole.setInsertAdmin(getAdminUserId());
        int id = sysRoleService.addSysRole(sysRole);
        paramSysRole.setId(sysRole.getId().toString());
        paramSysRole.setInsertAdmin(adminUserId);
        sysRoleService.addSysRolePerm(paramSysRole);
        return LocalUtils.getBaseResult(id);
    }

    @RequestMapping(value = "/updateSysRole", method = RequestMethod.POST)
    @ApiOperation(
            value = "修改角色;",
            notes = "修改角色-根据id修改;",
            httpMethod = "POST")
    public BaseResult updateSysRole( @Valid ParamSysRoleUpdate paramSysRole, HttpServletRequest request, BindingResult result){
        servicesUtil.validControllerParam(result);
        SysRole role =sysRoleService.getRoleById(Integer.parseInt(paramSysRole.getId()));
        if (role.getType() == 0){
            throw new MyException("系统设置，不可修改");
        }
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(paramSysRole,sysRole);
        sysRole.setId(LocalUtils.strParseInt(paramSysRole.getId()));

        sysRole.setUpdateAdmin(getAdminUserId());
        sysRoleService.updateSysRole(sysRole);
        sysRoleService.deleteSysRolePrem(LocalUtils.strParseInt(paramSysRole.getId()));
        ParamSysRoleAdd paramSysRoleAdd = new ParamSysRoleAdd();
        BeanUtils.copyProperties(paramSysRole,paramSysRoleAdd);
        paramSysRoleAdd.setInsertAdmin(getAdminUserId());
         sysRoleService.addSysRolePerm(paramSysRoleAdd);
        return LocalUtils.getBaseResult(0);
    }

    @RequestMapping(value = "/deleteSysRole", method = RequestMethod.POST)
    @ApiOperation(
            value = "删除角色;",
            notes = "删除角色-根据id删除;",
            httpMethod = "POST")
    public BaseResult deleteSysRole(@Valid ParamSysPermDelete sysPermDelete, BindingResult result){
        servicesUtil.validControllerParam(result);
        SysRole role =sysRoleService.getRoleById(Integer.parseInt(sysPermDelete.getId()));
        if (role.getType() == 0){
            throw new MyException("系统设置，不可删除");
        }
        //判断这个角色有没有被引用
        SysUserRoleRef userRoleRef = new SysUserRoleRef();
        userRoleRef.setFkSysRoleId(LocalUtils.strParseInt(sysPermDelete.getId()));
        userRoleRef.setDel(ConstantCommon.DEL_OFF);
        List<SysUserRoleRef> list = sysRoleService.queryUserRole(userRoleRef);
        if(null==list||list.size()>0){
            return BaseResult.errorResult("该角色正在被使用，无法删除！");
        }
        sysRoleService.deleteSysRoleById(Integer.parseInt(sysPermDelete.getId()));
        sysRoleService.deleteSysRolePrem(Integer.parseInt(sysPermDelete.getId()));
//        List<String> idList = Arrays.asList(sysPermDelete.getId().split(","));
//        idList.forEach(s -> {
//
//        });

        return LocalUtils.getBaseResult(0);
    }

}
