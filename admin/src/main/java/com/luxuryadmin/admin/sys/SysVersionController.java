package com.luxuryadmin.admin.sys;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.aop.check.RequiresPerm;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.op.OpPlatform;
import com.luxuryadmin.entity.sys.SysVersion;

import com.luxuryadmin.param.sys.ParamSysVersion;
import com.luxuryadmin.param.sys.ParamSysVersionAdd;
import com.luxuryadmin.service.sys.SysVersionService;
import com.luxuryadmin.vo.sys.VoSysVersion;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/sys/version")
@Api(tags = {"5.【系统管理】模块"}, description = "/sys/Version | 平台后台系统模块 ")
public class SysVersionController extends BaseController {

    @Autowired
    private SysVersionService sysVersionService;

    @RequestMapping(value = "/listAllOpPlatform", method = RequestMethod.GET)
    @ApiOperation(
            value = "分页查询APP列表;",
            notes = "分页查询APP列表;",
            httpMethod = "GET")
    public BaseResult listAllOpPlatform() {
        List<OpPlatform> opPlatformList = sysVersionService.queryAllOpPlatform();
        return LocalUtils.getBaseResult(opPlatformList);
    }

    @RequestMapping(value = "/listSysVersion", method = RequestMethod.GET)
    @ApiOperation(
            value = "分页查询版本列表;",
            notes = "分页查询版本列表;",
            httpMethod = "GET")
    @RequiresPerm(value = "system:version")
    public BaseResult listSysVersion(@Valid ParamSysVersion paramSysVersion, BindingResult result) {
        servicesUtil.validControllerParam(result);
        PageHelper.startPage(paramSysVersion.getPageNum(), paramSysVersion.getPageSize());
        List<VoSysVersion> voSysVersions = sysVersionService.querySysVersionList(paramSysVersion);
        PageInfo pageInfo = new PageInfo(voSysVersions);
        return LocalUtils.getBaseResult(pageInfo);
    }

    @RequestMapping(value = "/addSysVersion", method = RequestMethod.POST)
    @ApiOperation(
            value = "增加版本;",
            notes = "增加版本;",
            httpMethod = "POST")
    public BaseResult addSysVersion( @Valid ParamSysVersionAdd paramSysVersion, BindingResult result){
        servicesUtil.validControllerParam(result);
        SysVersion sysVersion = new SysVersion();
        BeanUtils.copyProperties(paramSysVersion,sysVersion);
        Integer uid = getAdminUserId();
        sysVersion.setInsertAdmin(uid);
        int id = 0;
        try {
            id = sysVersionService.addSysVersion(sysVersion);
        }catch (Exception e){
            if(e instanceof SQLIntegrityConstraintViolationException){
                return BaseResult.defaultErrorWithMsg("版本号已存在");
            }
        }
        return LocalUtils.getBaseResult(id);
    }

    @RequestMapping(value = "/updateSysVersion", method = RequestMethod.POST)
    @ApiOperation(
            value = "修改版本;",
            notes = "修改版本-根据id修改;",
            httpMethod = "POST")
    public BaseResult updateSysVersion( @Valid ParamSysVersionAdd paramSysVersionAdd, BindingResult result){
        servicesUtil.validControllerParam(result);
        SysVersion sysVersion = new SysVersion();
        BeanUtils.copyProperties(paramSysVersionAdd,sysVersion);
        sysVersion.setId(paramSysVersionAdd.getId());

        Integer uid = getAdminUserId();
        sysVersion.setUpdateAdmin(uid);
        int id = sysVersionService.updateSysVersion(sysVersion);
        return LocalUtils.getBaseResult(id);
    }

    @RequestMapping(value = "/deleteSysVersion", method = RequestMethod.POST)
    @ApiOperation(
            value = "删除版本;",
            notes = "删除版本-根据id删除;",
            httpMethod = "POST")
    public BaseResult deleteSysVersion(Long id){
        Integer uid = getAdminUserId();;
        SysVersion sysVersionDel = new SysVersion();
        sysVersionDel.setId(id);
        sysVersionDel.setDel(ConstantCommon.DEL_ON);
        sysVersionDel.setUpdateAdmin(uid);
        sysVersionService.deleteSysVersion(sysVersionDel);
        return LocalUtils.getBaseResult(id);
    }

}
