package com.luxuryadmin.admin.shp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.aop.check.RequiresPerm;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.shp.ShpShop;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.param.shp.ParamBindJobWx;
import com.luxuryadmin.param.shp.ParamGetShopInfoByNumber;
import com.luxuryadmin.param.shp.ParamShpShop;
import com.luxuryadmin.param.shp.ParamUpdateShopNumber;
import com.luxuryadmin.service.pay.PayOrderService;
import com.luxuryadmin.service.shp.ShpShopNumberService;
import com.luxuryadmin.service.shp.ShpShopService;
import com.luxuryadmin.vo.pay.VoPayOrderShopForAdmin;
import com.luxuryadmin.vo.shp.VoShopInfo;
import com.luxuryadmin.vo.shp.VoShpShop;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.List;

/**
 * @author Administrator
 * @Classname ShpDetailController
 * @Description TODO
 * @Date 2020/6/24 11:50
 * @Created by Administrator
 */
@Slf4j
@RestController
@RequestMapping(value = "/shp/shop")
@Api(tags = {"2.【店铺管理】模块"}, description = "/shp/shop | 店铺列表 ")
public class ShpShopController extends BaseController {

    @Autowired
    private ShpShopService shpShopService;

    @Autowired
    private ShpShopNumberService shpShopNumberService;

    @Autowired
    private PayOrderService payOrderService;

    @RequestMapping(value = "/listShpShop", method = RequestMethod.GET)
    @ApiOperation(
            value = "分页查询店铺列表;",
            notes = "分页查询店铺列表;",
            httpMethod = "GET")
    @RequiresPerm(value = "shop:list")
    public BaseResult listShpShop(@Valid ParamShpShop paramShpShop, BindingResult result) {
        servicesUtil.validControllerParam(result);
        PageHelper.startPage(getPageNum(), paramShpShop.getPageSize());
        List<VoShpShop> voShpShops = shpShopService.queryShpShopList(paramShpShop);
        PageInfo pageInfo = new PageInfo(voShpShops);
        pageInfo.setList(voShpShops);
        return LocalUtils.getBaseResult(pageInfo);
    }

    @ApiOperation(
            value = "修改店铺编号;",
            notes = "修改店铺编号;",
            httpMethod = "GET")
    @RequestMapping(value = "/updateShopNumber", method = RequestMethod.GET)
    public BaseResult updateShopNumber(@Valid ParamUpdateShopNumber param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String newShopNumber = param.getNewShopNumber();
        String shopIdStr = param.getShopId();
        int shopId = Integer.parseInt(shopIdStr);
        Integer bossUserId = shpShopService.getBossUserIdByShopId(shopId);
        if (LocalUtils.isEmptyAndNull(bossUserId)) {
            return BaseResult.defaultErrorWithMsg("店铺不存在!");
        }
        Integer dbShopId = shpShopService.getShopIdByShopNumber(newShopNumber);
        if (!LocalUtils.isEmptyAndNull(dbShopId)) {
            return BaseResult.defaultErrorWithMsg(newShopNumber + " 店铺编号已被使用!");
        }
        shpShopNumberService.useShopNumber(shopId, newShopNumber);
        return BaseResult.okResult();
    }

    @RequestMapping(value = "/getShpShopInfo", method = RequestMethod.GET)
    @ApiOperation(
            value = "查询店铺详细信息;",
            notes = "查询店铺详细信息;",
            httpMethod = "GET")
    public BaseResult getShpShopInfo(@RequestParam String id) {
        HashMap<String, Object> info = shpShopService.getShpShopInfo(id);
        return LocalUtils.getBaseResult(info);
    }

    @RequestMapping(value = "/offShpShop", method = RequestMethod.GET)
    @ApiOperation(
            value = "禁用/启用店铺;",
            notes = "禁用/启用店铺;",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "id", value = "禁用用户的id"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "state", value = "禁用-0,启用-1"),
    })
    public BaseResult offShpShop(@RequestParam String id, String state) {
        ShpShop shpShop = shpShopService.getShpShopById(id);
        if (null == shpShop) {
            return BaseResult.defaultErrorWithMsg("【" + id + "】店铺不存在!");
        }
        shpShop.setFkShpStateCode(LocalUtils.strParseInt(state));
        shpShopService.updateShpShop(shpShop);
        return BaseResult.okResult();
    }

    @RequestMapping(value = "/bindJobWx", method = RequestMethod.GET)
    @ApiOperation(
            value = "禁用/启用店铺;",
            notes = "禁用/启用店铺;",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "id", value = "禁用用户的id"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "state", value = "禁用-0,启用-1"),
    })
    public BaseResult bindJobWx(@Valid ParamBindJobWx param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String shopId = param.getShopId();
        ShpShop shpShop = shpShopService.getShpShopById(shopId);
        if (null == shpShop) {
            return BaseResult.defaultErrorWithMsg("【" + shopId + "】店铺不存在!");
        }
        shpShop.setFkSysJobWxId(Integer.parseInt(param.getSysJobWxId()));
        shpShopService.updateShpShop(shpShop);
        return BaseResult.okResult();
    }

    @RequestMapping(value = "/getVoPayOrderShopForAdmin", method = RequestMethod.GET)
    @ApiOperation(
            value = "查询店铺会员信息;",
            notes = "查询店铺会员信息;",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "id", value = "商户的id"),
    })
    public BaseResult getVoPayOrderShopForAdmin(@RequestParam(name = "id", required = true) String id) {
        List<VoPayOrderShopForAdmin> voPayOrderShopForAdmin = payOrderService.getVoPayOrderShopForAdmin(Integer.parseInt(id));
        return LocalUtils.getBaseResult(voPayOrderShopForAdmin);
    }


    @RequestMapping(value = "/getShpShopInfoByNumber", method = RequestMethod.GET)
    @ApiOperation(
            value = "根据店铺编号查询店铺信息;",
            notes = "根据店铺编号查询店铺信息;",
            httpMethod = "GET")
    public BaseResult getShpShopInfoByNumber(@Valid ParamGetShopInfoByNumber param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        VoShopInfo info = shpShopService.getShpShopInfoByNumber(param);
        return BaseResult.okResult(info);
    }
}
