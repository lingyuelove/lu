package com.luxuryadmin.admin.sys;

import com.luxuryadmin.admin.common.BaseAdminController;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.aop.check.RequiresPerm;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.aliyun.OSSUtil;
import com.luxuryadmin.entity.shp.ShpDetail;
import com.luxuryadmin.entity.sys.SysJobWx;
import com.luxuryadmin.param.biz.ParamShopValid;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.param.shp.ParamUploadShopValid;
import com.luxuryadmin.param.sys.ParamJobWxCensuses;
import com.luxuryadmin.param.sys.ParamSysJobWx;
import com.luxuryadmin.service.shp.ShpDetailService;
import com.luxuryadmin.service.sys.SysJobWxService;
import com.luxuryadmin.service.sys.SysUserService;
import com.luxuryadmin.vo.op.VoOpEmployeeCensus;
import com.luxuryadmin.vo.shp.VoShopValidInfo;
import com.luxuryadmin.vo.sys.VoSysJobWx;
import com.luxuryadmin.vo.sys.VoSysJobWxCensus;
import com.luxuryadmin.vo.sys.VoSysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

/**
 * @author monkey king
 * @date 2021-08-03 16:58:29
 */

@Slf4j
@RestController
@RequestMapping(value = "/user/shp", method = RequestMethod.POST)
@Api(tags = {"S001.【Sys】模块"})
public class SysJobWxController extends BaseAdminController {


    @Autowired
    private SysJobWxService sysJobWxService;

    @Autowired
    private SysUserService sysUserService;

    @ApiOperation(
            value = "获取工作微信列表",
            notes = "获取工作微信列表",
            httpMethod = "POST")
    @PostMapping("/loadJobWx")
    public BaseResult<VoSysJobWx> loadJobWx(@Valid ParamToken param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        List<VoSysJobWx> voSysJobWxList = sysJobWxService.listVoSysJobWx();
        if (!LocalUtils.isEmptyAndNull(voSysJobWxList)) {
            voSysJobWxList.forEach(voSysJobWx -> {
                voSysJobWx.setUsername(DESEncrypt.decodeUsername(voSysJobWx.getUsername()));
            });
        }
        return LocalUtils.getBaseResult(voSysJobWxList);
    }

    @ApiOperation(
            value = "获取工作微信统计列表",
            notes = "获取工作微信统计列表",
            httpMethod = "POST")
    @PostMapping("/listJobWxCensuses")
    @RequiresPerm(value = "census:jobwx")
    public BaseResult<VoSysJobWxCensus> listJobWxCensuses(@Valid ParamJobWxCensuses param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        List<VoSysJobWxCensus> sysJobWxCensuses = sysJobWxService.listJobWxCensuses(param);

        return LocalUtils.getBaseResult(sysJobWxCensuses);
    }
    @ApiOperation(
            value = "删除工作人员统计;",
            notes = "删除工作人员统计;",
            httpMethod = "POST")
    @RequestMapping(value = "/deleteEmployeeCensus", method = RequestMethod.POST)
    public BaseResult deleteJobWxCensuses(@Valid ParamJobWxCensuses param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        if (LocalUtils.isEmptyAndNull(param.getUserId())){
            return BaseResult.defaultErrorWithMsg("用户id不为空");
        }
        sysJobWxService.deleteJobWxCensuses(param.getUserId());
        return BaseResult.okResult();
    }
    @ApiOperation(
            value = "新增绑定工作微信",
            notes = "新增绑定工作微信",
            httpMethod = "POST")
    @PostMapping("/saveJobWx")
    public BaseResult saveJobWx(@Valid ParamSysJobWx param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        SysJobWx shpDetail = new SysJobWx();
        shpDetail.setUpdateTime(new Date());
        param.setState("0");
        String sysUserId = param.getSysUserId();
        if (!LocalUtils.isEmptyAndNull(sysUserId)) {
            param.setState("1");
        }
        SysJobWx sysJobWx = packSysJobWx(param);
        sysJobWxService.saveOrUpdateSysJobWx(sysJobWx);
        return BaseResult.okResult();
    }


    @ApiOperation(
            value = "绑定工作微信",
            notes = "绑定工作微信",
            httpMethod = "POST")
    @PostMapping("/updateJobWx")
    public BaseResult updateJobWx(@Valid ParamSysJobWx param, BindingResult result) {
        servicesUtil.validControllerParam(result);

        String jobWxId = param.getSysJobWxId();

        SysJobWx sysJobWx = sysJobWxService.getSysJobWxById(jobWxId);
        if (LocalUtils.isEmptyAndNull(sysJobWx)) {
            return BaseResult.okResultNoData();
        }

        param.setState("1");
        String sysUserId = param.getSysUserId();
        if (LocalUtils.isEmptyAndNull(sysUserId)) {
            param.setState("0");
            param.setSysUserId("0");
        }

        SysJobWx shpDetail = new SysJobWx();
        shpDetail.setUpdateTime(new Date());
        sysJobWx = packSysJobWx(param);
        sysJobWxService.saveOrUpdateSysJobWx(sysJobWx);
        return BaseResult.okResult();
    }


    @ApiOperation(
            value = "获取运营帐号;",
            notes = "获取运营帐号;",
            httpMethod = "GET")
    @RequestMapping(value = "/loadOpUser", method = RequestMethod.GET)
    @RequiresPerm(value = "wechat:user:list")
    public BaseResult loadOpUser(@Valid ParamToken token, BindingResult result) {
        servicesUtil.validControllerParam(result);
        List<VoSysUser> voSysUsers = sysUserService.loadOpUser();
        return LocalUtils.getBaseResult(voSysUsers);
    }

    @ApiOperation(
            value = "获取绑定的客服;",
            notes = "获取绑定的客服;",
            httpMethod = "GET")
    @RequestMapping(value = "/listBindJobWx", method = RequestMethod.GET)
    public BaseResult listBindJobWx(@Valid ParamToken token, BindingResult result) {
        servicesUtil.validControllerParam(result);
        List<VoSysJobWx> jobWxList = sysJobWxService.listBindJobWx();
        return LocalUtils.getBaseResult(jobWxList);
    }


    /**
     * 封装SysJobWx实体
     *
     * @param param
     * @return
     */
    private SysJobWx packSysJobWx(ParamSysJobWx param) {
        SysJobWx jobWx = new SysJobWx();
        String sysUserId = param.getSysUserId();
        if (!LocalUtils.isEmptyAndNull(sysUserId) ) {
            jobWx.setFkSysUserId(Integer.parseInt(sysUserId));
        }
        jobWx.setState(Integer.parseInt(param.getState()));
        jobWx.setWxAccount(param.getWxAccount());
        jobWx.setNickname(param.getNickname());
        jobWx.setPhone(param.getPhone());
        Date date = new Date();
        if ("1".equals(param.getState())) {
            jobWx.setInsertTime(date);
        }
        String jobWxId = param.getSysJobWxId();
        if (!LocalUtils.isEmptyAndNull(jobWxId)) {
            jobWx.setId(Integer.parseInt(jobWxId));
        }
        jobWx.setUpdateTime(date);
        jobWx.setInsertAdmin(getUserId());
        jobWx.setUpdateAdmin(getUserId());
        jobWx.setDel("0");
        jobWx.setRemark(param.getRemark());
        return jobWx;
    }


}
