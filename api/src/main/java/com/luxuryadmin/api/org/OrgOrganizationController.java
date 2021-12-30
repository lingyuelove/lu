package com.luxuryadmin.api.org;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.entity.org.OrgOrganization;
import com.luxuryadmin.param.org.ParamOrganizationAdd;
import com.luxuryadmin.param.org.ParamOrganizationSearch;
import com.luxuryadmin.param.org.ParamOrganizationUpdate;
import com.luxuryadmin.service.org.OrgOrganizationService;
import com.luxuryadmin.vo.org.VoOrganizationByApp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Date;

/**
 * 机构仓
 *
 * @author zhangSai
 * @date   2021/04/21 13:56:16
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/org", method = RequestMethod.POST)
@Api(tags = "C0041.【机构临时仓】模块", description = "/shop/user/org | 机构临时仓管理")
public class OrgOrganizationController extends BaseController {

    @Autowired
    private OrgOrganizationService organizationService;

    /**
     * 添加机构仓
     *
     * @param organizationAdd 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "机构版临时仓创建;",
            notes = "机构版临时仓创建",
            httpMethod = "POST")
    @ApiImplicitParams({

            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token")
    })
    @RequestRequire
    @PostMapping("/addOrganization")
    @RequiresPermissions("onlineOrgTemp:list")
    public BaseResult addOrganization(@Valid ParamOrganizationAdd organizationAdd, BindingResult result) {
        servicesUtil.validControllerParam(result);
        Integer shopId =  getShopId();
        Integer userId = getUserId();
        organizationAdd.setInsertAdmin(userId);
        organizationAdd.setShopId(shopId);
        return BaseResult.okResult(organizationService.addOrganization(organizationAdd));
    }

    /**
     * 修改机构仓
     *
     * @param organizationUpdate 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "机构版临时仓修改;",
            notes = "机构版临时仓修改",
            httpMethod = "POST")
    @ApiImplicitParams({

            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token")
    })
    @RequestRequire
    @PostMapping("/updateOrganization")
    @RequiresPermissions("onlineOrgTemp:list")
    public BaseResult updateOrganization(@Valid ParamOrganizationUpdate organizationUpdate, BindingResult result) {
        servicesUtil.validControllerParam(result);
        Integer userId = getUserId();
        organizationUpdate.setUpdateAdmin(userId);
        organizationUpdate.setShopId(getShopId());
        return BaseResult.okResult(organizationService.updateOrganization(organizationUpdate));
    }


    /**
     * 修改机构仓
     * @param id
     * @param state
     * @return
     */
    @ApiOperation(
            value = "机构版临时仓修改白名单;",
            notes = "机构版临时仓修改白名单",
            httpMethod = "POST")
    @ApiImplicitParams({

            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "id", value = "主键id"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "state", value = "展会状态 10 不开启限制 | 20 开启限制")
    })
    @RequestRequire
    @PostMapping("/updateState")
    @RequiresPermissions("onlineOrgTemp:list")
    public BaseResult updateById(@RequestParam(value="id",required=true) String id,@RequestParam(value="state",required=true) String state) {
        Integer userId = getUserId();
        OrgOrganization organization = new OrgOrganization();
        organization.setId(Integer.parseInt(id));
        organization.setUpdateAdmin(userId);
        organization.setUpdateTime(new Date());
        organization.setState(state);
        Integer result = organizationService.updateById(organization);
        return BaseResult.okResult(state);
    }


    /**
     * 删除
     *
     * @param id 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "机构版临时仓删除;",
            notes = "机构版临时仓删除",
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
    @PostMapping("/deleteOrganization")
    @RequiresPermissions("onlineOrgTemp:delete")
    public BaseResult delete(@RequestParam(value="id",required=true) String id) {

        organizationService.delete(Integer.parseInt(id));
        return BaseResult.okResult(Integer.parseInt(id));
    }

    /**
     * 机构仓app端集合显示
     *
     * @return Result
     */
    @ApiOperation(
            value = "机构版临时仓app端集合显示;",
            notes = "机构版临时仓app端集合显示",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token"),
    })
    @GetMapping("/getOrganizationPageByApp")
    @RequiresPermissions("onlineOrgTemp:list")
    public BaseResult<VoOrganizationByApp> getOrganizationTempPageByApp(@Valid ParamOrganizationSearch organizationSearch,String token) {
        organizationSearch.setShopId(getShopId());
        organizationSearch.setUserId(getUserId());
        VoOrganizationByApp organizationPageByApp = organizationService.getOrganizationPageByApp(organizationSearch);

        return BaseResult.okResult(organizationPageByApp);
    }
}