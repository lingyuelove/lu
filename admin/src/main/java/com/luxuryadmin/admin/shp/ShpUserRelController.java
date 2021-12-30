package com.luxuryadmin.admin.shp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.aop.check.RequiresPerm;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.shp.ShpUser;
import com.luxuryadmin.entity.shp.ShpUserDetail;
import com.luxuryadmin.entity.shp.ShpUserShopRef;

import com.luxuryadmin.param.shp.ParamShpUser;
import com.luxuryadmin.param.shp.ParamShpUserInfo;
import com.luxuryadmin.service.shp.ShpUserRoleRefService;
import com.luxuryadmin.service.shp.ShpUserService;
import com.luxuryadmin.service.shp.ShpUserShopRefService;
import com.luxuryadmin.vo.shp.VoShpUser;
import com.luxuryadmin.vo.shp.VoShpUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @Classname ShpUserController
 * @Description TODO
 * @Date 2020/6/22 15:28
 * @Created by Administrator
 */
@Slf4j
@RestController
@RequestMapping(value = "/shp")
@Api(tags = {"2.【店铺管理】模块"}, description = "/shp/user | 员工列表 ")
public class ShpUserRelController extends BaseController {

    @Autowired
    private ShpUserService shpUserService;

    @Autowired
    private ShpUserRoleRefService shpUserRoleRefService;

    @Autowired
    private ShpUserShopRefService shpUserShopRefService;

    @RequestMapping(value = "/listShpUserRel", method = RequestMethod.GET)
    @ApiOperation(
            value = "分页查询员工列表;",
            notes = "分页查询员工列表;",
            httpMethod = "GET")
    @RequiresPerm(value = "shop:user")
    public BaseResult listShpUserRel(@Valid ParamShpUser paramShpUser, BindingResult result) {
        servicesUtil.validControllerParam(result);
        PageHelper.startPage(paramShpUser.getPageNum(), paramShpUser.getPageSize());
        List<VoShpUser> voShpUsers = shpUserService.queryShpUserRelList(paramShpUser);
        PageInfo pageInfo = new PageInfo(voShpUsers);
        return LocalUtils.getBaseResult(pageInfo);
    }


    @RequestMapping(value = "/offShpUserRel", method = RequestMethod.GET)
    @ApiOperation(
            value = "禁用/启用用户;",
            notes = "禁用/启用用户;",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "id", value = "禁用用户的id"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "state", value = "禁用-0,启用-1"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "shopId", value = "店铺id"),
    })
    public BaseResult offShpUser(@RequestParam String id,String state,String shopId) {
        ShpUser shpUser = shpUserService.getObjectById(id);
        if(null==shpUser){
            return BaseResult.defaultErrorWithMsg("【" + id + "】用户不存在存在!");
        }
        ShpUserShopRef shpUserShopRef = new ShpUserShopRef();
        shpUserShopRef.setFkShpUserId(LocalUtils.strParseInt(id));
        shpUserShopRef.setFkShpShopId(LocalUtils.strParseInt(shopId));
        shpUserShopRef.setState(state);
        shpUserShopRefService.updateUserShopRefState(shpUserShopRef);
        //shpUserService.updateShpUser(shpUser);
        return BaseResult.okResult();
    }

    @RequestMapping(value = "/getShpUserDetailRel", method = RequestMethod.GET)
    @ApiOperation(
            value = "获取用户身份信息;",
            notes = "获取用户身份信息;",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "id", value = "获取用户身份信息的id"),
    })
    public BaseResult<ShpUserDetail> getShpUserDetail(@RequestParam String id) {
        ShpUserDetail shpUserDetail = shpUserService.selectDetailById(id);
        return LocalUtils.getBaseResult(shpUserDetail);
    }

    @RequestMapping(value = "/getShpUserInfoRel", method = RequestMethod.GET)
    @ApiOperation(
            value = "获取用户详情信息;",
            notes = "获取用户详情信息;",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "id", value = "获取用户详情信息的id"),
    })
    public BaseResult getShpUserInfo(@RequestParam String id) {

        int shopId = getShopId();
        VoShpUserInfo shpUserInfo = shpUserService.selectInfoById(shopId,id);
        return LocalUtils.getBaseResult(shpUserInfo);
    }

    @RequestMapping(value = "/updateShpUserInfoRel", method = RequestMethod.POST)
    @ApiOperation(
            value = "修改用户详情信息;",
            notes = "修改用户详情信息;",
            httpMethod = "POST")
    public BaseResult updateShpUserInfo(@Valid ParamShpUserInfo paramShpUserInfo, BindingResult result){
        ShpUser shpUser = new ShpUser();
        BeanUtils.copyProperties(paramShpUserInfo,shpUser);
        shpUserService.updateShpUser(shpUser);
        shpUserRoleRefService.deleteByUserId(paramShpUserInfo.getId());
        shpUserRoleRefService.saveUserRoleRefs(paramShpUserInfo);
        return BaseResult.okResult();
    }

}
