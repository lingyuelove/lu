package com.luxuryadmin.admin.op;

import com.luxuryadmin.common.aop.check.RequiresPerm;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.param.op.*;
import com.luxuryadmin.param.sys.ParamJobWxCensuses;
import com.luxuryadmin.service.op.OpEmployeeService;
import com.luxuryadmin.service.op.OpUnionAgentService;
import com.luxuryadmin.service.shp.ShpUserService;
import com.luxuryadmin.vo.op.VoOpEmployeeCensus;
import com.luxuryadmin.vo.op.VoOpEmployeeList;
import com.luxuryadmin.vo.op.VoOpUnionAgentList;
import com.luxuryadmin.vo.shp.VoShpUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 添加工作人员,绑定商家联盟小程序代理
 *
 * @author monkey king
 * @date 2021-09-14 19:24:07
 */
@Slf4j
@RestController
@RequestMapping(value = "/op")
@Api(tags = {"1.【运营】模块"}, description = "/op/union | 商家联盟代理 ")
public class OpUnionAgencyController extends BaseController {

    @Autowired
    private OpEmployeeService opEmployeeService;

    @Autowired
    private ShpUserService shpUserService;

    @Autowired
    private OpUnionAgentService opUnionAgentService;


    @ApiOperation(
            value = "添加员工帐号;",
            notes = "添加员工帐号;",
            httpMethod = "POST")
    @RequestMapping(value = "/addEmployeeAccount", method = RequestMethod.POST)
    public BaseResult addEmployeeAccount(@Valid ParamEmployeeAdd params, BindingResult result) {
        servicesUtil.validControllerParam(result);
        params.setCurrentUserId(getUserId());
        params.setType("1");
        opEmployeeService.addEmployeeAccount(params);
        return BaseResult.okResult("添加成功");
    }


    @ApiOperation(
            value = "根据用户手机号获取用户信息;",
            notes = "根据用户手机号获取用户信息;",
            httpMethod = "GET")
    @RequestMapping(value = "/getUserInfoByUsername", method = RequestMethod.GET)
    public BaseResult getUserInfoByUsername(@Valid ParamUsernameQuery params, BindingResult result) {
        servicesUtil.validControllerParam(result);
        VoShpUser voShpUser = shpUserService.getUserInfoByUsername(params.getUsername());
        return LocalUtils.getBaseResult(voShpUser);
    }

    @ApiOperation(
            value = "修改商家联盟分享开关",
            notes = "修改商家联盟分享开关",
            httpMethod = "GET")
    @RequestMapping(value = "/updateUnionSwitch", method = RequestMethod.GET)
    public BaseResult updateUnionSwitch(@Valid ParamUnionAgentQuery params, BindingResult result) {
        servicesUtil.validControllerParam(result);
        params.setCurrentUserId(getUserId());
        opEmployeeService.opEmployeeService(params);
        return BaseResult.okResult("修改成功");
    }


    @ApiOperation(
            value = "刪除工作人员",
            notes = "刪除工作人员",
            httpMethod = "GET")
    @RequestMapping(value = "/deleteUnionSwitch", method = RequestMethod.GET)
    public BaseResult deleteUnionSwitch(@Valid ParamIdQuery params, BindingResult result) {
        servicesUtil.validControllerParam(result);
        opEmployeeService.deleteUnionSwitch(Integer.parseInt(params.getId()));
        return BaseResult.okResult("刪除成功");
    }


    @ApiOperation(
            value = "获取工作人员列表;",
            notes = "获取工作人员列表;",
            httpMethod = "GET")
    @RequestMapping(value = "/listEmployee", method = RequestMethod.GET)
    @RequiresPerm(value = "union:employ")
    public BaseResult listEmployee() {
        List<VoOpEmployeeList> voOpBizComments = opEmployeeService.listEmployee();
        return BaseResult.okResult(voOpBizComments);
    }
    @ApiOperation(
            value = "获取工作人员统计列表;",
            notes = "获取工作人员列表;",
            httpMethod = "POST")
    @RequestMapping(value = "/listEmployeeCensus", method = RequestMethod.POST)
    @RequiresPerm(value = "census:employee")
    public BaseResult<List<VoOpEmployeeCensus>> listEmployeeCensus(@Valid ParamJobWxCensuses param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        List<VoOpEmployeeCensus> voOpBizComments = opEmployeeService.listEmployeeCensus(param);
        return BaseResult.okResult(voOpBizComments);
    }


    @ApiOperation(
            value = "添加代理帐号;",
            notes = "添加代理帐号;",
            httpMethod = "POST")
    @RequestMapping(value = "/saveUnionAgent", method = RequestMethod.POST)
    public BaseResult saveUnionAgent(@Valid ParamUnionAgentAdd params, BindingResult result) {
        servicesUtil.validControllerParam(result);
        params.setCurrentUserId(getUserId());
        opUnionAgentService.addUnionAgent(params);
        return BaseResult.okResult("添加成功");
    }

    @ApiOperation(
            value = "修改代理帐号;",
            notes = "修改代理帐号;",
            httpMethod = "POST")
    @RequestMapping(value = "/updateUnionAgent", method = RequestMethod.POST)
    public BaseResult updateUnionAgent(@Valid ParamUnionAgentUpdate params, BindingResult result) {
        servicesUtil.validControllerParam(result);
        params.setCurrentUserId(getUserId());
        opUnionAgentService.updateUnionAgent(params);
        return BaseResult.okResult("修改成功");
    }

    @ApiOperation(
            value = "删除代理帐号;",
            notes = "删除代理帐号;",
            httpMethod = "GET")
    @RequestMapping(value = "/deleteUnionAgent", method = RequestMethod.GET)
    public BaseResult deleteUnionAgent(@Valid ParamIdQuery params, BindingResult result) {
        servicesUtil.validControllerParam(result);
        opUnionAgentService.deleteUnionAgent(Integer.parseInt(params.getId()));
        return BaseResult.okResult("删除成功");
    }


    @ApiOperation(
            value = "获取代理人员列表;",
            notes = "获取代理人员列表;",
            httpMethod = "GET")
    @RequestMapping(value = "/listUnionAgent", method = RequestMethod.GET)
    @RequiresPerm(value = "union:agent")
    public BaseResult listUnionAgent(@Valid ParamUsernameAndUserIdQuery params, BindingResult result) {
        servicesUtil.validControllerParam(result);
        List<VoOpUnionAgentList> voOpBizComments = opUnionAgentService.listUnionAgent(params);
        return BaseResult.okResult(voOpBizComments);
    }

}
