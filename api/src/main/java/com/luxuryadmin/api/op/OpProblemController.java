package com.luxuryadmin.api.op;

import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.op.OpProblem;
import com.luxuryadmin.param.op.ParamOpProblemQuery;
import com.luxuryadmin.service.op.OpProblemService;
import com.luxuryadmin.vo.op.OpProblemType;
import com.luxuryadmin.vo.op.VoOpProblem;
import com.luxuryadmin.vo.op.VoOpProblemCategory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * @author Administrator
 * @Classname OpProblemController
 * @Description 帮助中心Controller
 * @Date 2020/7/09 10:00
 * @Created by sanjin
 */
@Slf4j
@RestController
@RequestMapping(value = "/op/problem")
@Api(tags = {"1.【用户管理】模块"}, description = "/op/problem | 【帮助中心】H5页面 ")
public class OpProblemController extends BaseController {

    @Autowired
    private OpProblemService opProblemService;


    @RequestMapping(value = "/selectEnableOpProblem"
            , method = RequestMethod.GET)
    @ApiOperation(
            value = "查询【帮助中心】启用问题;",
            notes = "查询【帮助中心】启用问题;",
            httpMethod = "GET")
    public BaseResult<List<VoOpProblem>> selectEnableOpProblem(ParamOpProblemQuery paramOpProblemQuery, BindingResult result) {
        servicesUtil.validControllerParam(result);
        List<VoOpProblem> opProblemList = opProblemService.selectEnableOpProblem(paramOpProblemQuery);
        return LocalUtils.getBaseResult(opProblemList);
    }
    @RequestMapping(value = "/listAllOpProblemCategoryName", method = RequestMethod.POST)
    @ApiOperation(
            value = "查询所有【帮助中心】问题分类;",
            notes = "查询所有【帮助中心】问题分类;",
            httpMethod = "POST")
    public BaseResult< List<OpProblemType>> listAllOpProblemCategoryName() {
        List<OpProblemType> opProblemList = opProblemService.listAllOpProblemCategoryName();
        return LocalUtils.getBaseResult(opProblemList);
    }

    @RequestMapping(value = "/loadEnableOpProblemDetail", method = RequestMethod.POST)
    @ApiOperation(
            value = "根据ID查询【帮助中心】问题详情;",
            notes = "根据ID查询【帮助中心】问题详情;",
            httpMethod = "POST")
    public BaseResult loadEnableOpProblemDetail(@RequestBody HashMap<String, String> map) {
        String id = map.get("id");
        OpProblem info = opProblemService.getProblemById(Integer.valueOf(id));
        if (LocalUtils.isEmptyAndNull(info)){
            return LocalUtils.getBaseResult(info);
        }
        info.setPlayNum(info.getPlayNum()+1);
        opProblemService.updateOpProblem(info);
        String videoUrl =servicesUtil.formatImgUrl(info.getVideoUrl());
        info.setVideoUrl(videoUrl);
        return LocalUtils.getBaseResult(info);
    }

}
