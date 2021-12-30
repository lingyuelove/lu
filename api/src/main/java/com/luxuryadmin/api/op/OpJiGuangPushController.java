package com.luxuryadmin.api.op;

import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.service.shp.ShpUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sanjin
 * @Classname OpProblemController
 * @Description 帮助中心Controller
 * @Date 2020/7/13 15:00
 * @Created by sanjin
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/op/push")
@Api(tags = {"1.【推送】模块"}, description = "/shop/user/op/push | 【推送】 ")
public class OpJiGuangPushController extends BaseController {

    @Autowired
    private ShpUserService shpUserService;

    @RequestMapping(value = "/updateShpUserJiGuangRegId", method = RequestMethod.POST)
    @ApiOperation(
            value = "更新店铺用户极光Registration ID;",
            notes = "更新店铺用户极光Registration ID;",
            httpMethod = "POST"
            )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true),
            @ApiImplicitParam(paramType = "query",dataType = "String", name = "jiGuangRegId", value = "极光【Registration ID】",required = true)
    })
    public BaseResult updateShpUserJiGuangRegId(String jiGuangRegId) {
        Integer shpUserId = getUserId();
        //Integer shpUserId = 10065;
        try {
            shpUserService.updateShpUserJiGuangRegId(shpUserId,jiGuangRegId);
        } catch (Exception e) {
            log.error("" + e);
            return BaseResult.errorResult(e.getMessage());
        }
        return BaseResult.okResult(Boolean.TRUE);

    }


}
