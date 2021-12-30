package com.luxuryadmin.api.op;

import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.param.op.ParamAppBannerQuery;
import com.luxuryadmin.service.op.OpBannerService;
import com.luxuryadmin.vo.op.VoOpBanner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Administrator
 * @Classname OpBannerController
 * @Description 帮助中心Controller
 * @Date 2020/8/28 15:15
 * @Created by sanjin
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/op/banner")
@Api(tags = {"1.【Banner管理】模块"}, description = "/shop/user/op/banner | 【Banner】 ")
public class OpBannerController extends BaseController {

    @Autowired
    private OpBannerService opBannerService;
    @RequestMapping(value = "/listOpBannerByPos", method = RequestMethod.POST)
    @ApiOperation(
            value = "查询所有【帮助中心】启用问题;",
            notes = "查询所有【帮助中心】启用问题;",
            httpMethod = "POST")
    public BaseResult<List<VoOpBanner>> listOpBannerByPath(ParamAppBannerQuery paramAppBannerQuery) {
        List<VoOpBanner> voOpBannerList = opBannerService.listOpBannerByPath(paramAppBannerQuery);
        return LocalUtils.getBaseResult(voOpBannerList);
    }

}
