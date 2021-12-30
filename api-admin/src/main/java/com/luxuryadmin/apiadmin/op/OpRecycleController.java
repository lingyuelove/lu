package com.luxuryadmin.apiadmin.op;

import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.param.op.ParamOpRecycleQuery;
import com.luxuryadmin.service.op.OpRecycleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 回收归集控制层
 * mong
 * @author Administrator
 */
@RestController
@Slf4j
@RequestMapping(value = "op/recycle")
@Api(tags = {"G004.回收归集 2.5.2--mong"})
public class OpRecycleController extends BaseController {

    @Autowired
    private OpRecycleService opRecycleService;


    @ApiOperation(
            value = " 添加回收归集",
            notes = " 添加回收归集",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestMapping("addRecycle")
    public BaseResult addRecycle(@Valid ParamOpRecycleQuery paramOpRecycleQuery, BindingResult result){
        servicesUtil.validControllerParam(result);
        opRecycleService.addRecycle(paramOpRecycleQuery);
        return BaseResult.okResult();
    }



}
