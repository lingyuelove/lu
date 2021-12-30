package com.luxuryadmin.admin.biz;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.aop.check.RequiresPerm;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.param.biz.ParamCanSeeLeaguerPriceInfo;
import com.luxuryadmin.param.biz.ParamRecommendAdminBySearch;
import com.luxuryadmin.param.biz.ParamShopRecommendAdd;
import com.luxuryadmin.service.biz.BizShopRecommendService;
import com.luxuryadmin.vo.biz.VoLeaguerRecommend;
import com.luxuryadmin.vo.biz.VoRecommendAdminPage;
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
import java.util.List;


/**
 *添加友商推荐 controller
 *@author zhangsai
 *@Date 2021-05-27 11:42:44
 */
@Slf4j
@RestController
@RequestMapping(value = "/biz/recommend", method = RequestMethod.POST)
@Api(tags = {"8.【友商】模块 --2.5.2 --zs"}, description = "/biz/recommend |【友商】模块相关")
public class BizShopRecommendController extends BaseController {

    @Autowired
    private BizShopRecommendService bizShopRecommendService;

    /**
     * 友商相册--店铺信息;
     */
    @ApiOperation(
            value = "推荐友商 --集合显示 --2.5.2 --zs;",
            notes = "推荐友商 --集合显示 --2.5.2 --zs;",
            httpMethod = "POST")
    @RequestRequire
    @PostMapping("/getRecommendAdminPage")
    @RequiresPerm(value = "shop:recommend")
    public BaseResult<VoRecommendAdminPage> getRecommendAdminPage(ParamRecommendAdminBySearch recommendAdminBySearch) {
        VoRecommendAdminPage recommendAdminPage= bizShopRecommendService.getRecommendAdminPage(recommendAdminBySearch);
        return LocalUtils.getBaseResult(recommendAdminPage);
    }
    /**
     * 推荐友商  --新增;
     */
    @ApiOperation(
            value = "推荐友商  --新增;",
            notes = "推荐友商  --新增;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token")
    })
    @RequestRequire
    @PostMapping("/addShopRecommend")
    public BaseResult addShopRecommend(@Valid ParamShopRecommendAdd shopRecommendAdd, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        bizShopRecommendService.addShopRecommend(shopRecommendAdd);
        return LocalUtils.getBaseResult("更新成功");
    }

    @ApiOperation(
            value = "推荐友商  --删除;",
            notes = "推荐友商  --删除;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "id", value = "主键id")
    })
    @RequestRequire
    @PostMapping("/deleteShopRecommend")
    public BaseResult deleteShopRecommend(@RequestParam(value="id",required=true)String id) {

        try {
            bizShopRecommendService.deleteShopRecommend(Integer.parseInt(id));
        } catch (Exception e) {
            return BaseResult.defaultErrorWithMsg(e.getMessage());
        }


        return LocalUtils.getBaseResult("删除成功");
    }

}
