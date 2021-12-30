package com.luxuryadmin.admin.sys;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.aop.check.RequiresPerm;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.common.utils.ExcelUtils;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.sys.SysUser;

import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.param.sys.*;
import com.luxuryadmin.service.sys.SysUserService;
import com.luxuryadmin.vo.sys.VoSysRole;
import com.luxuryadmin.vo.sys.VoSysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/sys/user")
@Api(tags = {"5.【系统管理】模块"}, description = "/sys/user | 平台后台系统模块 ")
public class SysUserController extends BaseController {

    @Autowired
    private SysUserService sysUserService;

    @RequestMapping(value = "/listSysUser", method = RequestMethod.GET)
    @ApiOperation(
            value = "分页查询账号列表;",
            notes = "分页查询账号列表;",
            httpMethod = "GET")
    @RequiresPerm(value = "system:user")
    public BaseResult listSysUser(@Valid ParamSysUser paramSysUser, BindingResult result) {
        servicesUtil.validControllerParam(result);
        PageHelper.startPage(paramSysUser.getPageNum(), paramSysUser.getPageSize());
        List<VoSysUser> voSysUsers = sysUserService.querySysUserList(paramSysUser);
        PageInfo pageInfo = new PageInfo(voSysUsers);
        return LocalUtils.getBaseResult(pageInfo);
    }

    @RequestMapping(value = "/addSysUser", method = RequestMethod.POST)
    @ApiOperation(
            value = "增加账号;",
            notes = "增加账号;",
            httpMethod = "POST")
    public BaseResult addSysUser( @Valid ParamSysUserAdd paramSysUser, BindingResult result){
        servicesUtil.validControllerParam(result);
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(paramSysUser,sysUser);

        sysUser.setInsertAdmin(getAdminUserId());
        BCryptPasswordEncoder encode = new BCryptPasswordEncoder();
        sysUser.setPassword(encode.encode(sysUser.getPassword()));
        int userId = sysUserService.addSysUser(sysUser);
        paramSysUser.setId(userId);
        paramSysUser.setInsertAdmin(getAdminUserId());
        paramSysUser.setRoleId(paramSysUser.getRoleId());
        int id = sysUserService.addSysUserRole(paramSysUser);
        return LocalUtils.getBaseResult(id);
    }

    @RequestMapping(value = "/updateSysUser", method = RequestMethod.POST)
    @ApiOperation(
            value = "修改账号;",
            notes = "修改账号-根据id修改;",
            httpMethod = "POST")
    public BaseResult updateSysUser( @Valid ParamSysUserUpdate paramSysUser, BindingResult result){
        servicesUtil.validControllerParam(result);
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(paramSysUser,sysUser);
        sysUser.setId(paramSysUser.getId());
        Integer adminUserId =getAdminUserId();
        sysUser.setUpdateAdmin(adminUserId);
        if(null!=sysUser.getUsername()){
            sysUser.setUsername(DESEncrypt.encodeUsername(sysUser.getUsername()));
        }
        if(null!=sysUser.getPhone()){
            sysUser.setPhone(DESEncrypt.encodeUsername(sysUser.getPhone()));
        }
        if(null!= sysUser.getPassword()){
            BCryptPasswordEncoder encode = new BCryptPasswordEncoder();
            sysUser.setPassword(encode.encode(sysUser.getPassword()));
        }
          sysUserService.updateSysUser(sysUser);
        if(null!=paramSysUser.getRoleId()){
            sysUserService.deleteSysUserPrem(sysUser);
            ParamSysUserAdd paramSysUserAdd = new ParamSysUserAdd();
            BeanUtils.copyProperties(paramSysUser,paramSysUserAdd);
            paramSysUserAdd.setInsertAdmin(adminUserId);
            sysUserService.addSysUserRole(paramSysUserAdd);
        }
        return LocalUtils.getBaseResult(0);
    }

    @RequestMapping(value = "/deleteSysUser", method = RequestMethod.POST)
    @ApiOperation(
            value = "删除账号;",
            notes = "删除账号-根据id删除;",
            httpMethod = "POST")
    public BaseResult deleteSysUser(@Valid ParamSysPermDelete sysPermDelete, BindingResult result){
        servicesUtil.validControllerParam(result);


        sysUserService.deleteSysUser( sysPermDelete);

        return LocalUtils.getBaseResult(0);
    }

    @RequestMapping(value = "/exportExcelUser", method = RequestMethod.GET)
    @ApiOperation(
            value = "用户表格导出;",
            notes = "用户表格导出;",
            httpMethod = "GET")
    public void downExcel(@Valid ParamSysUser paramSysUser, HttpServletResponse response){
        //PageHelper.startPage(paramSysUser.getPageNum(), paramSysUser.getPageSize());
        List<VoSysUser> voSysUsers = sysUserService.querySysUserList(paramSysUser);
        List<SysUser> sysUsers = new ArrayList<>();
        for (VoSysUser voSysUser : voSysUsers) {
            SysUser sysUser = new SysUser();
            BeanUtils.copyProperties(voSysUser,sysUser);
            sysUsers.add(sysUser);
        }
        List<SysUser> rows = CollUtil.newArrayList(sysUsers);
        try {
            ExcelUtils.downExcel(response,voSysUsers,"用户列表.xlsx");
        } catch (IOException e) {
            log.error("导出失败！",e);
        }
    }

    @RequestMapping(value = "/updateUserPass", method = RequestMethod.POST)
    @ApiOperation(
            value = "用户修改密码;",
            notes = "用户修改密码;",
            httpMethod = "POST")
    public BaseResult downExcel(@Valid ParamSysUserUpdatePass paramSysUserUpdatePass, BindingResult result){
        servicesUtil.validControllerParam(result);
        SysUser sysUser = new SysUser();
        sysUser = sysUserService.selectById((paramSysUserUpdatePass.getId()));
        if(null==sysUser){
            return BaseResult.errorResult("没有找到对应的用户！");
        }

        BCryptPasswordEncoder encode = new BCryptPasswordEncoder();
        if(DESEncrypt.decodeUsername(paramSysUserUpdatePass.getUsername()).equals(sysUser.getUsername())
            &&encode.encode(paramSysUserUpdatePass.getPasswordOld()).equals(sysUser.getPassword())){

//            Integer uid = LocalUtils.strParseInt(redisUtil.get(SpringSecurityUtil.getUserName()));
            sysUser.setUpdateAdmin(getAdminUserId());

            sysUser.setPassword(encode.encode(paramSysUserUpdatePass.getPasswordNew()));
            int id = sysUserService.updateSysUser(sysUser);
            return LocalUtils.getBaseResult("密码修改成功！");
        }else {
            return LocalUtils.getBaseResult("密码验证错误！");
        }
    }


}
