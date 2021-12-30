package com.luxuryadmin.api.shp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.base.BasicParam;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.VersionUtils;
import com.luxuryadmin.entity.shp.ShpServiceRecord;
import com.luxuryadmin.entity.shp.ShpServiceType;
import com.luxuryadmin.param.shp.ParamShpServiceQuery;
import com.luxuryadmin.param.shp.ParamShpServiceRecordAdd;
import com.luxuryadmin.param.shp.ParamShpServiceRecordFinish;
import com.luxuryadmin.service.shp.ShpServiceService;
import com.luxuryadmin.vo.shp.VoInitShopService;
import com.luxuryadmin.vo.shp.VoShpServiceRecord;
import com.luxuryadmin.vo.shp.VoShpServiceRecordDetail;
import com.luxuryadmin.vo.shp.VoShpServiceRecordForPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jacoco.agent.rt.internal_1f1cc91.core.internal.flow.IFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @author sanjin
 * @description 店铺服务Contrller
 * @date 2020-09-18 15:44
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/service", method = RequestMethod.POST)
@Api(tags = {"E007.【店铺】模块 --2.6.5"}, description = "/shop/user/service | 店铺服务")
public class ShpServiceController extends BaseController {


    @Autowired
    private ShpServiceService shpServiceService;

    /**
     * 获取新增【店铺服务】初始化数据
     *
     * @param
     * @return Result
     */
    @ApiOperation(
            value = "获取新增【店铺服务】初始化数据--2.6.5",
            notes = "获取新增【店铺服务】初始化数据;--2.6.5",
            httpMethod = "POST")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true)})
    @RequestMapping("/getInitAddShpServiceData")
    public BaseResult<VoInitShopService> addShpService() {
        Integer shopId = getShopId();
        BasicParam basicParam = getBasicParam();
        VoInitShopService voInitShopService = shpServiceService.getInitAddShpServiceData(shopId,basicParam);
        return BaseResult.okResult(voInitShopService);
    }


    /**
     * 添加店铺服务
     *
     * @param
     * @return Result
     */
    @ApiOperation(
            value = "添加店铺服务--2.6.5",
            notes = "添加店铺服务;--2.6.5",
            httpMethod = "POST")
    @RequestMapping("/addShpService")
    @RequiresPermissions("sale:service:add")
//    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true)})
    public BaseResult addShpService(@Valid ParamShpServiceRecordAdd serviceInfo, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        if (LocalUtils.isEmptyAndNull(serviceInfo.getProdImgUrls())) {
            throw new MyException("添加店铺服务时，商品图片不能为空");
        }
        Integer shopId = getShopId();
        Integer userId = getUserId();
        serviceInfo.setShopId(shopId);
        serviceInfo.setUserId(userId);
        BasicParam basicParam = getBasicParam();
        try {
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.6.5") >= 0) {
                if (LocalUtils.isEmptyAndNull(serviceInfo.getServiceShpUserId())){
                    throw new MyException("服务人员不能为空");
                }
                if (LocalUtils.isEmptyAndNull(serviceInfo.getReceiveShpUserId())){
                    throw new MyException("接单人员不能为空");
                }
            }
        } catch (Exception e) {
            throw new MyException("添加店铺服务错误"+e);
        }
        ShpServiceRecord shpServiceRecord = shpServiceService.addShpServiceRecord( serviceInfo, request);
        return BaseResult.okResult(shpServiceRecord.getId());
    }

    /**
     * 修改店铺服务
     *
     * @param
     * @return Result
     */
    @ApiOperation(
            value = "修改店铺服务--2.6.5",
            notes = "修改店铺服务;--2.6.5",
            httpMethod = "POST")
    @RequestMapping("/updateShpService")
    @RequiresPermissions("sale:service:update")
//    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true)})
    public BaseResult updateShpService(@Valid ParamShpServiceRecordAdd serviceInfo,
                                       BindingResult result,
                                       HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        if (null == serviceInfo.getId()) {
            throw new MyException("修改店铺服务时ID为空");
        }
        BasicParam basicParam = getBasicParam();
        try {
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.6.5") >= 0) {
                if (LocalUtils.isEmptyAndNull(serviceInfo.getServiceShpUserId())){
                    throw new MyException("服务人员不能为空");
                }
                if (LocalUtils.isEmptyAndNull(serviceInfo.getReceiveShpUserId())){
                    throw new MyException("接单人员不能为空");
                }
            }
        } catch (Exception e) {
            throw new MyException("添加店铺服务错误"+e);
        }
        Integer shopId = getShopId();
        Integer userId = getUserId();
        serviceInfo.setUserId(userId);
        serviceInfo.setShopId(shopId);
        BigDecimal costAmount = serviceInfo.getCostAmount();
        BigDecimal realReceiveAmount = serviceInfo.getRealReceiveAmount();

        serviceInfo.setCostAmount(null == costAmount ? new BigDecimal(0) : costAmount);
        serviceInfo.setRealReceiveAmount(null == realReceiveAmount ? new BigDecimal(0) : realReceiveAmount);

        Integer updateResult = shpServiceService.updateShpService( serviceInfo, request);

        return BaseResult.okResult(updateResult);
    }

    /**
     * 获取店铺服务列表
     *
     * @param paramShpServiceQuery
     * @return
     */
    @ApiOperation(
            value = "获取店铺服务列表--2.6.5",
            notes = "获取店铺服务列表;--2.6.5",
            httpMethod = "POST")
    @RequestMapping("/listShpService")
    @RequiresPermissions("pro:check:service")
    public BaseResult<VoShpServiceRecordForPage> listShpService(ParamShpServiceQuery paramShpServiceQuery) {
        Integer shopId = getShopId();
        paramShpServiceQuery.setFkShpShopId(shopId);
        PageHelper.startPage(paramShpServiceQuery.getPageNum(), paramShpServiceQuery.getPageSize());
        BasicParam basicParam = getBasicParam();
        VoShpServiceRecordForPage shpServiceRecordForPage = shpServiceService.listShpServiceRecord(paramShpServiceQuery,basicParam);
        //是否有查看维修保养数据汇总的权限
        boolean hasPermDataTotal = hasPermWithCurrentUser(ConstantPermission.PRO_CHECK_UPERMSERVICEPROFIT);
        if (!hasPermDataTotal){
            shpServiceRecordForPage.setServiceNum("******");
            shpServiceRecordForPage.setServiceInitPrice("******");
            shpServiceRecordForPage.setServiceProfitPrice("******");
        }
        String uPermServiceProfit = hasPermToPageWithCurrentUser(ConstantPermission.PRO_CHECK_UPERMSERVICEPROFIT);
        shpServiceRecordForPage.setUPermServiceProfit(uPermServiceProfit);
        return BaseResult.okResult(shpServiceRecordForPage);
    }

    /**
     * 根据ID获取店铺服务详情
     *
     * @param shpServiceId
     * @return
     */
    @ApiOperation(
            value = "根据ID获取店铺服务详情--2.6.5",
            notes = "根据ID获取店铺服务详情;--2.6.5",
            httpMethod = "POST")
    @RequestMapping("/getShpServiceById")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true)})
    public BaseResult<VoShpServiceRecordDetail> getShpServiceById(@RequestParam(value = "shpServiceId", required = true) Integer shpServiceId) {
        Integer shopId = getShopId();
        VoShpServiceRecordDetail recordDetail = shpServiceService.getShpServiceById(shopId, shpServiceId);
        if (LocalUtils.isEmptyAndNull(recordDetail)){
            return BaseResult.defaultOkResultWithMsg("暂无此维修服务");
        }
        String uPermUpdate = ConstantPermission.SALE_SERVICE_UPDATE;
        recordDetail.setUPermUpdate(hasPermToPageWithCurrentUser(uPermUpdate));
        return BaseResult.okResult(recordDetail);
    }

    /**
     * 完成店铺服务
     *
     * @param
     * @return Result
     */
    @ApiOperation(
            value = "完成店铺服务--2.6.5",
            notes = "完成店铺服务;--2.6.5",
            httpMethod = "POST")
    @RequestMapping("/finishShpService")
    @RequiresPermissions("sale:service:update")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true)})
    public BaseResult finishShpService(@Valid ParamShpServiceRecordFinish recordFinish,
                                       BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        Integer shopId = getShopId();
        Integer userId = getUserId();
        try {
            shpServiceService.finishShpService(shopId, userId, recordFinish, request);
        } catch (Exception e) {
            return BaseResult.defaultErrorWithMsg(e.getMessage());
        }
        return BaseResult.okResult();
    }

    /**
     * 取消店铺服务
     *
     * @param
     * @return Result
     */
    @ApiOperation(
            value = "取消店铺服务--2.6.5",
            notes = "取消店铺服务;--2.6.5",
            httpMethod = "POST")
    @RequestMapping("/cancelShpService")
    @RequiresPermissions("sale:service:update")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true)})
    public BaseResult cancelShpService(@RequestParam(value = "shpServiceId", required = true) Integer shpServiceId,
                                       HttpServletRequest request) {
        Integer shopId = getShopId();
        Integer userId = getUserId();
        try {
            shpServiceService.cancelShpService(shopId, userId, shpServiceId, request);
        } catch (Exception e) {
            return BaseResult.defaultErrorWithMsg(e.getMessage());
        }
        return BaseResult.okResult();
    }

    /**
     * 添加店铺服务类型
     *
     * @param
     * @return Result
     */
    @ApiOperation(
            value = "添加店铺服务类型",
            notes = "添加店铺服务类型;",
            httpMethod = "POST")
    @RequestMapping("/addShpServiceType")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true)})
    public BaseResult addShpServiceType(@RequestParam(value = "shpServiceTypeName", required = true) String shpServiceTypeName) {
        Integer shopId = getShopId();
        Integer userId = getUserId();
        ShpServiceType shpServiceType = shpServiceService.addShpServiceType(shopId, userId, shpServiceTypeName);
        return BaseResult.okResult(shpServiceType.getId());
    }

    /**
     * 删除店铺服务类型
     *
     * @param
     * @return Result
     */
    @ApiOperation(
            value = "删除店铺服务类型",
            notes = "删除店铺服务类型;",
            httpMethod = "POST")
    @RequestMapping("/deleteShpServiceType")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true)})
    public BaseResult deleteShpServiceType(@RequestParam(value = "shpServiceTypeId", required = true) Integer shpServiceTypeId) {
        Integer shopId = getShopId();
        Integer userId = getUserId();

        Integer result = shpServiceService.deleteShpServiceType(shopId, userId, shpServiceTypeId);
        return BaseResult.okResult(result);
    }

    /**
     * 删除店铺服务
     *
     * @param
     * @return Result
     */
    @ApiOperation(
            value = "删除店铺服务",
            notes = "删除店铺服务;",
            httpMethod = "POST")
    @RequestMapping("/deleteShpService")
    @RequiresPermissions("sale:service:delete")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true)})
    public BaseResult deleteShpService(@RequestParam(value = "shpServiceId", required = true) String shpServiceId,
                                       HttpServletRequest request) {
        Integer shopId = getShopId();
        Integer userId = getUserId();
        Integer result =null;
        try {
            BasicParam basicParam = getBasicParam();
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.6.5") >= 0) {
                List<String> idList = Arrays.asList(shpServiceId.split(","));
                if (!LocalUtils.isEmptyAndNull(idList)){
                    idList.forEach(s -> {
                        shpServiceService.deleteShpService(shopId, userId, Integer.parseInt(s), request);
                    });
                }
            }else {
                 result = shpServiceService.deleteShpService(shopId, userId, Integer.parseInt(shpServiceId), request);
            }
        } catch (Exception e) {
            throw new MyException("删除店铺服务错误"+e);
        }

        return BaseResult.okResult(result);
    }

}
