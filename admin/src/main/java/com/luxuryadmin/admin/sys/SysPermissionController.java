package com.luxuryadmin.admin.sys;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.aop.check.RequiresPerm;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.sys.SysPermission;

import com.luxuryadmin.param.sys.ParamSysPermDelete;
import com.luxuryadmin.param.sys.ParamSysPermissionUpdate;
import com.luxuryadmin.service.sys.SysPermissionService;
import com.luxuryadmin.param.sys.ParamSysPermission;
import com.luxuryadmin.vo.shp.VoShpPermission;
import com.luxuryadmin.vo.sys.VoSysPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * @author monkey king
 * @Date 2019/12/01 4:07
 */
@Slf4j
@RestController
@RequestMapping(value = "/sys")
@Api(tags = {"5.【系统管理】模块","5.【系统管理】模块"}, description = "/sys/admin | 平台后台系统模块 ")
public class SysPermissionController extends BaseController {

    @Autowired
    private SysPermissionService sysPermissionService;


    /**
     * 加载平台权限列表
     *
     * @param request 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "加载平台权限列表",
            notes = "加载平台权限列表",
            httpMethod = "POST")
    @RequestMapping("/loadSysPermission")
    @RequiresPerm(value = "system:sys")
    public BaseResult loadSysPermission(HttpServletRequest request) {
        String token =request.getHeader("Authorization");
        String userName = servicesUtil.getShpUserNameByToken(token);

        //查询当前id所拥有的权限
//        List<VoSysPermission> permissions = sysPermissionService.queryByUser(userName);
//        if (!LocalUtils.isEmptyAndNull(permissions)){
//            return LocalUtils.getBaseResult(permissions);
//        }
        //2.6.5之后显示
        List<VoSysPermission> voSysPermissions = sysPermissionService.groupBySysPermission();
        return LocalUtils.getBaseResult(voSysPermissions);
    }
    @ApiOperation(
            value = "加载用户平台权限列表",
            notes = "加载用户平台权限列表",
            httpMethod = "POST")
    @RequestMapping("/queryByUser")
    public BaseResult queryByUser( HttpServletRequest request) {
        String token =request.getHeader("Authorization");
        String userName = servicesUtil.getShpUserNameByToken(token);

        //查询当前id所拥有的权限
        List<VoSysPermission> permissions = sysPermissionService.queryByUser(userName);
        if (!LocalUtils.isEmptyAndNull(permissions)){
            return LocalUtils.getBaseResult(permissions);
        }

        return LocalUtils.getBaseResult(userName);
    }
    /**
     * 添加平台端权限
     *
     * @return Result
     */
    @ApiOperation(
            value = "添加平台端权限",
            notes = "添加平台端权限",
            httpMethod = "POST")
    @RequestMapping("/addSysPermission")
    public BaseResult addSysPermission( @Valid ParamSysPermission permission, BindingResult result) {
        servicesUtil.validControllerParam(result);
        SysPermission perm = pickSysPermission(permission, true);

        perm.setId(0);
        existsPermission(perm);
        perm.setId(null);
//        Integer uid = LocalUtils.strParseInt(redisUtil.get(SpringSecurityUtil.getUserName()));
        Integer uid  = getAdminUserId();;
        perm.setInsertAdmin(uid);
        return BaseResult.okResult(sysPermissionService.saveSysPermission(perm));
    }

    /**
     * 更新平台端权限
     *
     * @return Result
     */
    @ApiOperation(
            value = "更新平台端权限",
            notes = "更新平台端权限",
            httpMethod = "POST")
    @RequestMapping("/updateSysPermission")
    public BaseResult updateSysPermission(@Valid ParamSysPermissionUpdate permission, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String id = permission.getId();
        SysPermission permissionDb = sysPermissionService.getSysPermissionById(Integer.parseInt(id));
        if (LocalUtils.isEmptyAndNull(permissionDb)) {
            return BaseResult.defaultErrorWithMsg("该权限不存在!id = " + id);
        }
        String permName = permission.getName();

        SysPermission perm = pickSysPermission(permission, false);
        Integer uid  = getAdminUserId();;
        perm.setUpdateAdmin(uid);
        existsPermission(perm);
        sysPermissionService.updateSysPermission(perm);
        return BaseResult.okResult(perm);
    }

    public void existsPermission( SysPermission sysPermission){
        boolean exists = sysPermissionService.existsSysPermissionName(sysPermission.getParentId(), sysPermission.getName(),sysPermission.getId());
        if (exists) {
//            return BaseResult.defaultErrorWithMsg();
            throw new MyException("【" + sysPermission.getName() + "】已存在该模块!");
        }
        String code = sysPermission.getCode();
        if (!LocalUtils.isEmptyAndNull(code) && 0==sysPermission.getType()) {
            boolean existsCode = sysPermissionService.existsSysPermissionCode(code,sysPermission.getId());
            if (existsCode) {
//                return BaseResult.defaultErrorWithMsg("code值: 【" + code + "】已存在!");
                throw new MyException("code值: 【" + code + "】已存在!");
            }
        }
        String permission = sysPermission.getPermission();
        if (!LocalUtils.isEmptyAndNull(permission)) {
            boolean existsCode = sysPermissionService.existsSysPermission(permission,sysPermission.getId());
            if (existsCode) {
//                return BaseResult.defaultErrorWithMsg("code值: 【" + code + "】已存在!");
                throw new MyException("permission权限值: 【" + permission + "】已存在!");
            }
        }
    }
    /**
     * 封装SysPermission实体</br>
     * 判断权限类型与父节点id
     *
     * @param perm
     * @return
     */
    private SysPermission pickSysPermission(ParamSysPermissionUpdate perm, boolean isSave) {
        SysPermission sysPermission = new SysPermission();

        int parenId = Integer.parseInt(perm.getParentId());
        int type = Integer.parseInt(perm.getType());
        if (type > 0 && parenId <= 0) {
            //不是根目录菜单,则必须要有父节点
            throw new MyException("不是根目录菜单,则必须要有父节点!");
        }
        Date date = new Date();
        if (isSave) {
            //新增
            sysPermission.setInsertTime(date);
            sysPermission.setInsertAdmin(getAdminUserId());
        } else {
            sysPermission.setId(Integer.parseInt(perm.getId()));
            sysPermission.setUpdateTime(date);
            sysPermission.setUpdateAdmin(getAdminUserId());
        }
        String sort = perm.getSort();
        sysPermission.setParentId(parenId);
        sysPermission.setType(type);
        sysPermission.setName(perm.getName());
        sysPermission.setCode(perm.getCode());
        sysPermission.setUrl(perm.getUrl());
        sysPermission.setPermission(perm.getPermission());
        sysPermission.setIconUrl(perm.getIconUrl());
        sysPermission.setSort(LocalUtils.isEmptyAndNull(sort) ? 0 : Integer.parseInt(sort));
        sysPermission.setRemark(perm.getRemark());
        return sysPermission;
    }

    /**
     * 新增或更新权限;
     * 新增 id 不传 ，更新必传
     * @return Result
     */
    @RequestMapping(value = "/saveOrUpdateSysPermission", method = RequestMethod.POST)
    @ApiOperation(
            value = "新增或更新权限;",
            notes = "新增或更新权限;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
    })
    public BaseResult saveOrUpdateSysPermission(@Valid ParamSysPermission param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        SysPermission sysPer = new SysPermission();
        sysPer.setId(LocalUtils.strParseInt(param.getId()));
        sysPer.setParentId(LocalUtils.strParseInt(param.getParentId()));
        sysPer.setName(param.getName());
        sysPer.setUrl(param.getUrl());
        sysPer.setPermission(param.getPermission());
        sysPer.setType(LocalUtils.strParseInt(param.getType()));
        sysPer.setIconUrl(param.getIconUrl());
        sysPer.setSort(LocalUtils.strParseInt(param.getSort()));
        sysPer.setInsertTime(new Date());
        sysPer.setUpdateTime(sysPer.getInsertTime());
        sysPer.setInsertAdmin(getAdminUserId());
        sysPer.setUpdateAdmin(getAdminUserId());
        return BaseResult.okResult(sysPermissionService.saveOrUpdateSysPermission(sysPer));
    }


    /**
     * 刪除app端权限
     *
     * @return Result
     */
    @ApiOperation(
            value = "刪除平台端权限",
            notes = "刪除平台端权限",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
    })
    @RequestRequire
    @RequestMapping("/deleteSysPermission")
    public BaseResult deleteSyspPermission(@Valid ParamSysPermDelete sysPermDelete, BindingResult result) {
        servicesUtil.validControllerParam(result);
        sysPermDelete.setUserId(getAdminUserId());
        return BaseResult.okResult(sysPermissionService.deleteSysPerm(sysPermDelete));
    }
}
