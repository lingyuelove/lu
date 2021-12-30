package com.luxuryadmin.api.pro;

import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.pro.ProCheck;
import com.luxuryadmin.entity.pro.ProCheckProduct;
import com.luxuryadmin.param.pro.ParamCheckProductListForApiBySearch;
import com.luxuryadmin.param.pro.ParamCheckProductUpdateStateForApi;
import com.luxuryadmin.service.pro.ProCheckProductService;
import com.luxuryadmin.service.pro.ProCheckService;
import com.luxuryadmin.service.pro.ProTempProductService;
import com.luxuryadmin.service.pro.ProTempService;
import com.luxuryadmin.service.util.ProProductBaseController;
import com.luxuryadmin.vo.pro.VoCheckProductDetailByApi;
import com.luxuryadmin.vo.pro.VoCheckProductListForApi;
import com.luxuryadmin.vo.pro.VoProductLoad;
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
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/shop/user/pro/check")
@Api(tags = {"C006.【盘点】模块 "}, description = "/shop/user/pro/check |盘点】模块相关--2.6.4❤")
@RequiresPermissions("checkProduct:list")
public class ProCheckProductController  extends ProProductBaseController {

    @Autowired
    private ProCheckProductService checkProductService;

    @Autowired
    private ProCheckService checkService;
    @Autowired
    private ProTempService proTempService;
    @Autowired
    private ProTempProductService proTempProductService;
    /**
     * 修改盘点商品的状态
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "修改盘点",
            notes = "修改盘点",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token")
    })
    @PostMapping(value = "/updateCheckProduct")
    public BaseResult updateCheckProduct(@Valid ParamCheckProductUpdateStateForApi checkProductUpdateStateForApi, BindingResult result) {
        servicesUtil.validControllerParam(result);
        ProCheckProduct checkProduct = checkProductService.getById(checkProductUpdateStateForApi.getId());
        if (checkProduct == null ){
            return BaseResult.errorResult("此盘点商品不存在");
        }
        ProCheck check = checkService.getById(checkProduct.getFkProCheckId());
        if (check == null ){
            return BaseResult.errorResult("此盘点不存在");
        }
        if (!"10".equals(check.getCheckState())){
            return BaseResult.errorResult("此批次的盘点状态已修改，不可再进行盘点");
        }
        Integer userId = getUserId();
        checkProductUpdateStateForApi.setUserId(userId);
        checkProductService.updateCheckProduct(checkProductUpdateStateForApi);
        return BaseResult.okResult(checkProductService.getCheckProductCount(checkProduct.getFkProCheckId()));
    }

    /**
     * 商户盘点集合显示
     * @param checkProductListForApiBySearch
     * @return
     */
    @ApiOperation(
            value = "获取商户盘点商品集合--2.6.4❤",
            notes = "获取商户盘点商品集合--2.6.4❤",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token登录标识符"),
    })

    @RequestRequire
    @GetMapping("/getCheckProductListForApi")
    public BaseResult<Map<String,Object>> getCheckProductListForApi(@Valid ParamCheckProductListForApiBySearch checkProductListForApiBySearch) {

        checkProductListForApiBySearch.setShopId(getShopId());
        Map<String,Object> objectMap = checkProductService.getCheckProductListForApi(checkProductListForApiBySearch);
        return BaseResult.okResult(objectMap);
    }


    @ApiOperation(
            value = "获取商户盘点数量",
            notes = "获取商户盘点数量",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token登录标识符"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "fkProCheckId", value = "盘点id"),
    })
    @RequestRequire
    @GetMapping("/getCheckProductCount")
    public BaseResult<Map<String,Integer>> getCheckProductCount(@RequestParam(name="fkProCheckId",required=true)String fkProCheckId) {

        return BaseResult.okResult(checkProductService.getCheckProductCount(Integer.parseInt(fkProCheckId)));
    }


    @ApiOperation(
            value = "扫码获取商户信息",
            notes = "扫码获取商户信息",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token登录标识符"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "bizId", value = "商品bizId"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "fkProCheckId", value = "盘点id"),
    })
    @RequestRequire
    @GetMapping("/getCheckProductDetail")
    public BaseResult<VoCheckProductListForApi> getCheckProductForApi(@RequestParam(name="bizId",required=true) String bizId,@RequestParam(name="fkProCheckId",required=true)String fkProCheckId) {
        VoCheckProductListForApi voCheckProductListForApi = null;
        //判断是否为条形码扫码
        if (bizId.contains("#")){
            bizId = bizId.replace("#","");
            VoProductLoad  voProductLoad = proProductService.getProductDetailByShopIdId(getShopId(), bizId);
            if (!LocalUtils.isEmptyAndNull(voProductLoad)){
                voCheckProductListForApi = checkProductService.getCheckProductForApi(voProductLoad.getBizId(), Integer.parseInt(fkProCheckId));
            }
        }else {
             voCheckProductListForApi = checkProductService.getCheckProductForApi(bizId, Integer.parseInt(fkProCheckId));
        }
        if (LocalUtils.isEmptyAndNull(voCheckProductListForApi)){
            return  BaseResult.defaultErrorWithMsg("该商品信息不在此盘点内");
        }
        //临时仓商品状态 是否已售罄
        if (!LocalUtils.isEmptyAndNull(voCheckProductListForApi.getTempId())){
            String tempProState =proTempProductService.getTempProState(getShopId(),voCheckProductListForApi.getTempId(), voCheckProductListForApi.getFkProProductId());
            voCheckProductListForApi.setTempProState(tempProState);
            if ("1".equals(tempProState)){
                voCheckProductListForApi.setTempProStateName("已售出");
            }
            if ("2".equals(tempProState)){
                voCheckProductListForApi.setTempProStateName("已售罄");
            }
        }else {
            String tempName = proTempService.getTempName(getShopId(), voCheckProductListForApi.getFkProProductId());
            voCheckProductListForApi.setTempName(tempName);
        }

        return BaseResult.okResult(voCheckProductListForApi);
    }
    /**
     * 商户盘点商品单个显示
     * @param id
     * @return
     */
    @ApiOperation(
            value = "商户盘点商品单个显示",
            notes = "商户盘点商品单个显示",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token登录标识符"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "id", value = "主键id"),
    })
    @RequestRequire
    @GetMapping("/getCheckProductDetailByApi")
    public BaseResult<VoCheckProductDetailByApi> getCheckProductDetailByApi(@RequestParam(name="id",required=true) String id) {

        VoCheckProductDetailByApi voCheckProductDetailByApi = checkProductService.getCheckProductDetailByApi(Integer.parseInt(id));
        if (voCheckProductDetailByApi == null){
            return BaseResult.okResult();
        }
        //商品状态
        voCheckProductDetailByApi.setStateCn(servicesUtil.getStateCn(voCheckProductDetailByApi.getStateUs()));
        //添加上架下架权限判断权限判断
        String uPermRelease = "0";
        String  uPermBackOff = "0";
        voCheckProductDetailByApi.setUPermRelease(uPermRelease);
        voCheckProductDetailByApi.setUPermBackOff(uPermBackOff);

        //未盘点商品直接返回
        if ("no".equals(voCheckProductDetailByApi.getCheckState())){
            return BaseResult.okResult(voCheckProductDetailByApi);
        }
        //盘点进行中商品直接返回
        ProCheck check = checkService.getById(voCheckProductDetailByApi.getCheckId());
        if (!"10".equals(check.getCheckState())){

            return BaseResult.okResult(voCheckProductDetailByApi);
        }
        //盘点进行中不在时间范围内
        if (check.getStartTime() != null && check.getEndTime() != null && check.getEndTime().before(new Date())){
            return BaseResult.okResult(voCheckProductDetailByApi);
        }
        //判断盘点上下架的状态
        Boolean flag = "0".equals(voCheckProductDetailByApi.getCheckType()) && "已上架".equals(voCheckProductDetailByApi.getStateCn()) ;
        Boolean flag2 =  "1".equals(voCheckProductDetailByApi.getCheckType()) && "未上架".equals(voCheckProductDetailByApi.getStateCn()) ;
            String releasePerm = ConstantPermission.MOD_RELEASE_PRODUCT;
        if (flag){
            //商品下架
            voCheckProductDetailByApi.setUPermBackOff(hasPermToPageWithCurrentUser(releasePerm));
        }else if (flag2){
            //商品上架
            voCheckProductDetailByApi.setUPermRelease(hasPermToPageWithCurrentUser(releasePerm));
        }


        return BaseResult.okResult(voCheckProductDetailByApi);
    }
}
