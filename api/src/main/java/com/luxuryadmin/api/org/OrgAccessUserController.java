package com.luxuryadmin.api.org;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.org.OrgAccessUser;
import com.luxuryadmin.param.org.*;
import com.luxuryadmin.service.org.OrgAccessUserService;
import com.luxuryadmin.service.org.OrgOrganizationService;
import com.luxuryadmin.vo.org.VoAccessUserByApp;
import com.luxuryadmin.vo.org.VoAccessUserPageByApp;
import com.luxuryadmin.vo.org.VoOrganizationByApp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.regex.Pattern;


/**
 * 机构仓
 *
 * @author zhangSai
 * @date 2021/04/21 13:56:16
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/org/access", method = RequestMethod.POST)
@Api(tags = "C0041.【机构临时仓】模块", description = "/shop/user/org/access | 机构临时仓")
public class OrgAccessUserController extends BaseController {

    @Autowired
    private OrgAccessUserService accessUserService;

    /**
     * 添加手机白名单
     *
     * @param accessUserAdd 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "添加参展人员--机构版临时仓;",
            notes = "添加参展人员--机构版临时仓",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token")
    })
    @RequestRequire
    @PostMapping("/addAccessUser")
    public BaseResult addAccessUser(@Valid ParamAccessUserAdd accessUserAdd, BindingResult bindingResult) {
        servicesUtil.validControllerParam(bindingResult);
        OrgAccessUser accessUser = accessUserService.getAccessUser(accessUserAdd.getPhone(),accessUserAdd.getOrganizationId(),"10");
        if (accessUser != null) {
            BaseResult result = new BaseResult();
            result.setCode("error");
            if (!accessUser.getAccessType().equals(accessUserAdd.getAccessType())) {
                if ("10".equals(accessUser.getAccessType())) {
                    result.setMsg("此手机号已在白名单添加");
                } else {
                    result.setMsg("此手机号已在黑名单添加");
                }
            } else {
                result.setMsg("此手机号已添加");
            }
            return result;
        }
        Integer shopId = getShopId();
        Integer userId = getUserId();
        accessUserAdd.setUserId(userId);
        accessUserAdd.setShopId(shopId);
        Integer id = accessUserService.addAccessUser(accessUserAdd);
        return BaseResult.okResult(id);
    }

    /**
     * 删除机构版临时仓
     *
     * @param id 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "删除添加参展人员--机构版临时仓;",
            notes = "删除添加参展人员--机构版临时仓",
            httpMethod = "POST")
    @ApiImplicitParams({

            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "id", value = "主键id"),
    })
    @RequestRequire
    @PostMapping("/deleteAccessUser")
    public BaseResult deleteAccessUser(@RequestParam(value = "id", required = true) String id) {


        //清除缓存
        String phone = accessUserService.getAccessUserPhoneById(Integer.parseInt(id));
        //phoneRedisKey
        String keyPhone = "_online:access_user:phone:" + phone;
        String unionId = redisUtil.get(keyPhone);
        //unionIdRedisKey
        String keyUid = "_online:access_user:unionId:" + unionId;
        redisUtil.delete(keyPhone);
        redisUtil.delete(keyUid);
        accessUserService.deleteAccessUser(Integer.parseInt(id));
        return BaseResult.okResult();
    }

    /**
     * 机构仓app端集合显示
     *
     * @return Result
     */
    @ApiOperation(
            value = "参展人员集合显示--机构版临时仓app端;",
            notes = "参展人员集合显示--机构版临时仓app端",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token"),
    })
    @GetMapping("/getAccessUserByApp")
    public BaseResult<VoAccessUserByApp> getAccessUserByApp(@Valid ParamAccessUserSearch accessUserSearch, BindingResult bindingResult) {
        servicesUtil.validControllerParam(bindingResult);

        VoAccessUserByApp accessUserByApp = accessUserService.getAccessUserByApp(accessUserSearch);

        return BaseResult.okResult(accessUserByApp);
    }

    /**
     * 机构仓app端集合显示
     *
     * @return Result
     */
    @ApiOperation(
            value = "参展人员搜索--机构版临时仓app端;",
            notes = "参展人员搜索--机构版临时仓app端",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "phone", value = "手机号"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "organizationId", value = "机构id"),
    })
    @GetMapping("/getAccessUserList")
    public BaseResult<List<VoAccessUserPageByApp>> getAccessUserList(@RequestParam(value = "phone", required = true) String phone, @RequestParam(value = "organizationId", required = true) String organizationId) {

        List<VoAccessUserPageByApp> accessUser = accessUserService.getAccessUserList(phone, Integer.parseInt(organizationId));


        return BaseResult.okResult(accessUser);
    }
}