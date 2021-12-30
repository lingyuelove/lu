package com.luxuryadmin.api.org;


import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.org.OrgOrganizationTemp;
import com.luxuryadmin.param.org.ParamOrganizationTempAdd;
import com.luxuryadmin.param.org.ParamOrganizationTempSearch;
import com.luxuryadmin.param.org.ParamOrganizationTempUpdate;
import com.luxuryadmin.param.pro.ParamTempProductOrgPageBySearch;
import com.luxuryadmin.service.org.OrgOrganizationTempService;
import com.luxuryadmin.service.org.OrgTempSeatService;
import com.luxuryadmin.service.pro.ProTempProductService;
import com.luxuryadmin.service.pro.ProTempService;
import com.luxuryadmin.vo.org.VoOrganizationTempByApp;
import com.luxuryadmin.vo.pro.VoTempForOrg;
import com.luxuryadmin.vo.pro.VoTempProductOrgByApp;
import com.luxuryadmin.vo.pro.VoTempProductOrgDetailByApp;
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

/**
 * 机构临时仓
 *
 * @author zhangSai
 * @date 2021/04/21 13:57:27
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/org", method = RequestMethod.POST)
@Api(tags = "C0041.【机构临时仓】模块", description = "/shop/user/org | 机构临时仓管理")
public class OrgOrganizationTempController extends BaseController {

    @Autowired
    private OrgOrganizationTempService organizationTempService;

    @Autowired
    private OrgTempSeatService tempSeatService;

    @Autowired
    private ProTempService tempService;

    @Autowired
    private ProTempProductService proTempProductService;

    /**
     * 添加机构临时仓
     *
     * @param organizationTempAdd 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "提取【机构临时仓】;",
            notes = "提取【机构临时仓】",
            httpMethod = "POST")
    @RequestRequire
    @PostMapping("/addOrganizationTemp")
    @RequiresPermissions("onlineOrgTemp:list")
    public BaseResult addOrganizationTemp(@Valid ParamOrganizationTempAdd organizationTempAdd, BindingResult result) {
        servicesUtil.validControllerParam(result);
        OrgOrganizationTemp organizationTemp = organizationTempService.getByTempId(organizationTempAdd.getTempId(), organizationTempAdd.getOrganizationId());

        if (organizationTemp != null) {
            throw new MyException("此临时仓已加入");
        }
        Integer shopId = getShopId();
        Integer userId = getUserId();
        organizationTempAdd.setInsertAdmin(userId);
        organizationTempAdd.setShopId(shopId);

        return BaseResult.okResult(organizationTempService.addOrganizationTemp(organizationTempAdd));
    }

    /**
     * 修改机构临时仓
     *
     * @param organizationTempUpdate 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "修改【机构临时仓详情】;",
            notes = "修改【机构临时仓详情】",
            httpMethod = "POST")
    @RequestRequire
    @PostMapping("/updateOrganizationTemp")
    @RequiresPermissions("onlineOrgTemp:list")
    public BaseResult updateOrganizationTemp(@Valid ParamOrganizationTempUpdate organizationTempUpdate, BindingResult result) {
        servicesUtil.validControllerParam(result);
        Integer userId = getUserId();
        organizationTempUpdate.setUpdateAdmin(userId);
        return BaseResult.okResult(organizationTempService.updateOrganizationTemp(organizationTempUpdate));
    }

    /**
     * 删除
     *
     * @param id 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "删除【机构临时仓详情】;",
            notes = "删除【机构临时仓详情】",
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
    @PostMapping("/delete")
    @RequiresPermissions("onlineOrgTemp:delete")
    public BaseResult updateOrganizationTemp(@RequestParam(value = "id", required = true) String id) {

        organizationTempService.delete(Integer.parseInt(id));
        return BaseResult.okResult(Integer.parseInt(id));
    }


    /**
     * 机构临时仓app端集合显示
     *
     * @return Result
     */
    @ApiOperation(
            value = "机构临时仓详情app端集合显示;",
            notes = "机构临时仓详情app端集合显示",
            httpMethod = "GET")
    @GetMapping("/getOrganizationTempPageByApp")
    @RequiresPermissions("onlineOrgTemp:list")
    public BaseResult<VoOrganizationTempByApp> getOrganizationTempPageByApp(@Valid ParamOrganizationTempSearch organizationTempSearch, BindingResult result) {
        servicesUtil.validControllerParam(result);
        if (LocalUtils.isEmptyAndNull(organizationTempSearch.getPageNum())){
            organizationTempSearch.setPageNum("1");
        }
        organizationTempSearch.setShopId(getShopId());
        organizationTempSearch.setUserId(getUserId());
        VoOrganizationTempByApp voOrganizationTempByApp = organizationTempService.getOrganizationTempPageByApp(organizationTempSearch);

        return BaseResult.okResult(voOrganizationTempByApp);
    }


    /**
     * 所有商品页面--机构临时仓
     *
     * @return Result
     */
    @ApiOperation(
            value = "所有商品页面--机构临时仓;",
            notes = "所有商品页面--机构临时仓",
            httpMethod = "GET")
    @GetMapping("/getTempProductOrgByApp")
    @RequiresPermissions("onlineOrgTemp:list")
    public BaseResult<VoTempProductOrgByApp> getTempProductOrgByApp(@Valid ParamTempProductOrgPageBySearch tempProductOrgPageBySearch, BindingResult result) {
        servicesUtil.validControllerParam(result);
        if (LocalUtils.isEmptyAndNull(tempProductOrgPageBySearch.getPageNum())){
            tempProductOrgPageBySearch.setPageNum("1");
        }
        VoTempProductOrgByApp voTempProductOrgByApp = proTempProductService.getTempProductOrgByApp(tempProductOrgPageBySearch);

        return BaseResult.okResult(voTempProductOrgByApp);
    }


    /**
     * 机构临时仓--机构临时仓
     *
     * @return Result
     */
    @ApiOperation(
            value = "机构临时仓--机构临时仓;",
            notes = "机构临时仓--机构临时仓",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "tempId", value = "临时仓id"),
    })
    @GetMapping("/getTempProductOrgForShopByApp")
    @RequiresPermissions("onlineOrgTemp:list")
    public BaseResult<VoTempProductOrgByApp> getTempProductOrgForShopByApp(@Valid ParamTempProductOrgPageBySearch tempProductOrgPageBySearch) {

        if (LocalUtils.isEmptyAndNull(tempProductOrgPageBySearch.getPageNum())){
            tempProductOrgPageBySearch.setPageNum("1");
        }
        VoTempProductOrgByApp voTempProductOrgForShopByApp = proTempProductService.getTempProductOrgForShopByApp(tempProductOrgPageBySearch);

        return BaseResult.okResult(voTempProductOrgForShopByApp);
    }


    /**
     * 机构临时仓--机构临时仓
     *
     * @return Result
     */
    @ApiOperation(
            value = "商品详情--机构临时仓;",
            notes = "商品详情--机构临时仓",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "tempProductId", value = "临时仓商品id"),
    })
    @GetMapping("/getTempProductOrgDetail")
    public BaseResult<VoTempProductOrgDetailByApp> getTempProductOrgDetail(@RequestParam(value = "tempProductId", required = true) Integer tempProductId) {

        VoTempProductOrgDetailByApp tempProductOrgDetailByApp = proTempProductService.getTempProductOrgDetail(tempProductId);

        return BaseResult.okResult(tempProductOrgDetailByApp);
    }

    /**
     * 获取店铺临时仓详情
     *
     * @return Result
     */
    @ApiOperation(
            value = "机构版临时仓提取--临时仓信息;",
            notes = "机构版临时仓提取--临时仓信息",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "tempId", value = "临时仓编号"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "organizationId", value = "机构临时仓编号"),
    })
    @GetMapping("/getVoTempForOrg")
    @RequiresPermissions("onlineOrgTemp:list")
    public BaseResult<VoTempForOrg> getVoTempForOrg(@RequestParam(value = "tempId", required = true) String tempId, @RequestParam(value = "organizationId", required = true) String organizationId) {
        Integer shopId = getShopId();
        OrgOrganizationTemp organizationTemp = organizationTempService.getByTempId(Integer.parseInt(tempId), Integer.parseInt(organizationId));
        if (organizationTemp != null) {
            return BaseResult.defaultErrorWithMsg("此临时仓已加入");
        }
        VoTempForOrg tempForOrg = tempService.getVoTempForOrg(Integer.parseInt(tempId), shopId);
        return BaseResult.okResult(tempForOrg);
    }


}