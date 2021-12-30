package com.luxuryadmin.api.share;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.org.OrgAccessUser;
import com.luxuryadmin.entity.org.OrgOrganization;
import com.luxuryadmin.param.org.ParamOrganizationId;
import com.luxuryadmin.param.org.ParamOrganizationTempSearch;
import com.luxuryadmin.param.pro.ParamTempProductOrgPageBySearch;
import com.luxuryadmin.param.pro.ParamTempProductOrgPageForWebBySearch;
import com.luxuryadmin.service.org.OrgAccessUserService;
import com.luxuryadmin.service.org.OrgOrganizationService;
import com.luxuryadmin.service.org.OrgOrganizationTempService;
import com.luxuryadmin.service.org.OrgTempSeatService;
import com.luxuryadmin.service.pro.ProTempProductService;
import com.luxuryadmin.vo.org.VoOrganizationTempByApplets;
import com.luxuryadmin.vo.org.VoOrganizationTempPageByApplets;
import com.luxuryadmin.vo.org.VoTempSeatList;
import com.luxuryadmin.vo.pro.VoTempProductOrgByApplets;
import com.luxuryadmin.vo.pro.VoTempProductOrgDetailByApp;
import com.luxuryadmin.vo.shp.VoUserShopBase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/org/share", method = RequestMethod.GET)
@Api(tags = {"Z004.【机构临时仓分享】模块"}, description = "/org/share | 外部分享,不需要登录")
public class ShareOrgController extends BaseController {

    @Autowired
    private OrgOrganizationTempService organizationTempService;

    @Autowired
    private ProTempProductService proTempProductService;

    @Autowired
    private OrgAccessUserService accessUserService;

    @Autowired
    private OrgOrganizationService organizationService;

    @Autowired
    private OrgTempSeatService tempSeatService;

    /**
     * 机构临时仓小程序集合显示
     *
     * @return Result
     */
    @ApiOperation(
            value = "小程序首页--机构临时仓详情;",
            notes = "小程序首页--机构临时仓详情",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "phone", value = "phone"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "searchName", value = "搜索名称"),

    })
    @GetMapping("/getOrganizationTempPageByApplets")
    public BaseResult<VoOrganizationTempByApplets> getOrganizationTempPageByApplets(@Valid ParamOrganizationTempSearch organizationTempSearch) {
        OrgOrganization organization = organizationService.getOrganization(organizationTempSearch.getOrganizationId());

        if (LocalUtils.isEmptyAndNull(organization)) {
            return BaseResult.defaultErrorWithMsg("暂无此展会");
        }
        if (organization != null && "20".equals(organization.getState())) {
            OrgAccessUser accessUser = accessUserService.getAccessUser(organizationTempSearch.getPhone(), organizationTempSearch.getOrganizationId(), "10");
            if (LocalUtils.isEmptyAndNull(accessUser)) {
                return BaseResult.defaultErrorWithMsg("此手机号暂无资格");
            }
        }
        if (LocalUtils.isEmptyAndNull(organizationTempSearch.getPageNum())){
            organizationTempSearch.setPageNum("1");
        }
        VoOrganizationTempByApplets voOrganizationTempByApplets = organizationTempService.getOrganizationTempPageByApplets(organizationTempSearch);

        return BaseResult.okResult(voOrganizationTempByApplets);
    }

    /**
     * 机构临时仓排序位置集合显示
     *
     * @return Result
     */
    @ApiOperation(
            value = "展会位置分组集合显示;",
            notes = "展会位置分组置集合显示",
            httpMethod = "GET")
    @GetMapping("/getShopTempSeat")
    public BaseResult<List<VoTempSeatList>> getShopTempSeat(@Valid ParamOrganizationId orgId, BindingResult result) {
        servicesUtil.validControllerParam(result);
        OrgOrganization organization = organizationService.getOrganization(orgId.getOrganizationId());
        if (organization == null) {
            return BaseResult.defaultErrorWithMsg("此展会不存在");
        }
        Integer shopId = organization.getFkShpShopId();
        List<VoTempSeatList> tempSeatLists = tempSeatService.getShopTempSeat(shopId);
        return BaseResult.okResult(tempSeatLists);
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

    @GetMapping("/getTempProductOrgByApplets")
    public BaseResult<VoTempProductOrgByApplets> getTempProductOrgByApplets(@Valid ParamTempProductOrgPageForWebBySearch tempProductOrgPageBySearch) {
        if (LocalUtils.isEmptyAndNull(tempProductOrgPageBySearch.getPageNum())){
            tempProductOrgPageBySearch.setPageNum("1");
        }
        ParamTempProductOrgPageBySearch paramTempProductOrgPageBySearch = new ParamTempProductOrgPageBySearch();
        BeanUtils.copyProperties(tempProductOrgPageBySearch,paramTempProductOrgPageBySearch);
        VoTempProductOrgByApplets voTempProductOrgByApp = proTempProductService.getTempProductOrgByApplets(paramTempProductOrgPageBySearch);

        return BaseResult.okResult(voTempProductOrgByApp);
    }

    /**
     * 所有商品页面--机构临时仓
     *
     * @return Result
     */
    @ApiOperation(
            value = "店铺商品页面--机构临时仓;",
            notes = "店铺商品页面--机构临时仓",
            httpMethod = "GET")
    @ApiImplicitParams({

            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "tempId", value = "临时仓id"),
    })
    @GetMapping("/getShopProductOrgByApplets")
    public BaseResult<VoTempProductOrgByApplets> getShopProductOrgByApplets(@Valid ParamTempProductOrgPageForWebBySearch tempProductOrgPageBySearch) {
        if (LocalUtils.isEmptyAndNull(tempProductOrgPageBySearch.getPageNum())){
            tempProductOrgPageBySearch.setPageNum("1");
        }
        ParamTempProductOrgPageBySearch paramTempProductOrgPageBySearch = new ParamTempProductOrgPageBySearch();
        BeanUtils.copyProperties(tempProductOrgPageBySearch,paramTempProductOrgPageBySearch);
        VoTempProductOrgByApplets voTempProductOrgByApp = proTempProductService.getTempProductOrgByApplets(paramTempProductOrgPageBySearch);

        return BaseResult.okResult(voTempProductOrgByApp);
    }

    @ApiOperation(
            value = "商品详情--机构临时仓;",
            notes = "商品详情--机构临时仓",
            httpMethod = "GET")
    @ApiImplicitParams({

            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "tempProductId", value = "临时仓商品id"),
    })
    @GetMapping("/getTempProductOrgDetail")
    public BaseResult<VoTempProductOrgDetailByApp> getTempProductOrgDetail(@RequestParam(value = "tempProductId", required = true) Integer tempProductId) {

        VoTempProductOrgDetailByApp tempProductOrgDetailByApp = proTempProductService.getTempProductOrgDetail(tempProductId);

        return BaseResult.okResult(tempProductOrgDetailByApp);
    }


}
