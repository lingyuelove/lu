package com.luxuryadmin.admin.op;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.aop.check.RequiresPerm;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.op.OpProblem;

import com.luxuryadmin.param.op.ParamOpProblem;
import com.luxuryadmin.param.op.ParamOpProblemQuery;
import com.luxuryadmin.service.op.OpProblemService;
import com.luxuryadmin.vo.op.OpProblemType;
import com.luxuryadmin.vo.op.VoOpProblem;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Classname OpProblemController
 * @Description 帮助中心Controller
 * @Date 2020/7/09 10:00
 * @Created by sanjin
 */
@Slf4j
@RestController
@RequestMapping(value = "/op/problem")
@Api(tags = {"1【用户管理】模块"}, description = "/op/problem | 【帮助中心】 ")
public class OpProblemController extends BaseController {

    @Autowired
    private OpProblemService opProblemService;

    @RequestMapping(value = "/listAllOpProblemCategoryName", method = RequestMethod.GET)
    @ApiOperation(
            value = "查询【帮助中心】问题分类;",
            notes = "查询【帮助中心】问题分类;",
            httpMethod = "GET")
    @RequiresPerm(value = "user:op:problem")
    public BaseResult listAllOpProblemCategoryName() {
        List<OpProblemType> opProblemList = opProblemService.listAllOpProblemCategoryName();
        return LocalUtils.getBaseResult(opProblemList);
    }

    @RequestMapping(value = "/listOpProblem", method = RequestMethod.POST)
    @ApiOperation(
            value = "分页查询【帮助中心】问题;",
            notes = "分页查询【帮助中心】问题;",
            httpMethod = "POST")
    public BaseResult listOpProblem( @Valid ParamOpProblemQuery paramOpProblemQuery, BindingResult result) {
        servicesUtil.validControllerParam(result);
        PageHelper.startPage(paramOpProblemQuery.getPageNum(), paramOpProblemQuery.getPageSize());
        List<VoOpProblem> opProblemList = opProblemService.listOpProblem(paramOpProblemQuery);
        PageInfo pageInfo = new PageInfo(opProblemList);
        return LocalUtils.getBaseResult(pageInfo);
    }

    @RequestMapping(value = "/addOpProblem", method = RequestMethod.POST)
    @ApiOperation(
            value = "增加【帮助中心】问题;",
            notes = "增加【帮助中心】问题;",
            httpMethod = "POST")
    public BaseResult addOpProblem(@Valid ParamOpProblem paramOpProblem, BindingResult result){
        servicesUtil.validControllerParam(result);
        OpProblem opProblem = new OpProblem();
        BeanUtils.copyProperties(paramOpProblem,opProblem);

        Integer uid =getAdminUserId();
        opProblem.setInsertAdmin(uid);
        int id = opProblemService.addOpProblem(opProblem);
        return LocalUtils.getBaseResult(id);
    }

    @RequestMapping(value = "/getOpProblem", method = RequestMethod.GET)
    @ApiOperation(
            value = "根据ID查询【帮助中心】问题;",
            notes = "根据ID查询【帮助中心】问题;",
            httpMethod = "GET")
    public BaseResult getOpProblemInfo(@RequestParam String id){
        OpProblem info = opProblemService.getProblemById(Integer.valueOf(id));
        return LocalUtils.getBaseResult(info);
    }

    @RequestMapping(value = "/updateOpProblem", method = RequestMethod.POST)
    @ApiOperation(
            value = "修改【帮助中心】问题;",
            notes = "修改【帮助中心】问题;",
            httpMethod = "POST")
    public BaseResult updateOpProblem( @Valid ParamOpProblem paramOpProblem, BindingResult result){
        if(null == paramOpProblem.getId()){
            return BaseResult.errorResult("编辑问题ID不能为空");
        }

        servicesUtil.validControllerParam(result);
        OpProblem opProblem = new OpProblem();
        BeanUtils.copyProperties(paramOpProblem,opProblem);

        //获取修改者用户ID
        String token =getAdminToken();
        Integer uid  =getAdminUserId();
        opProblem.setUpdateAdmin(uid);

        //更新
        opProblem.setUpdateTime(new Date());
        int id = opProblemService.updateOpProblem(opProblem);
        return LocalUtils.getBaseResult(id);
    }

    @RequestMapping(value = "/delOpProblem", method = RequestMethod.POST)
    @ApiOperation(
            value = "删除【帮助中心】问题;",
            notes = "删除【帮助中心】问题;",
            httpMethod = "POST")
    public BaseResult delOpProblem(@RequestBody HashMap<String, String> map){
        String id = map.get("id");
        //获取修改者用户ID
        String token =getAdminToken();
        Integer uid  = getAdminUserId();
        int result = opProblemService.delOpProblem(Integer.valueOf(id),uid);
        return LocalUtils.getBaseResult(result);
    }

}
