package com.luxuryadmin.api.ord;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.entity.ord.OrdType;
import com.luxuryadmin.param.ord.ParamOrdTypeUpFopApp;
import com.luxuryadmin.service.ord.OrdTypeService;
import com.luxuryadmin.vo.ord.VoOrdType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单类型模块Controller
 * sanjin145
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/ord/type", method = RequestMethod.POST)
@Api(tags = {"C003.【订单】模块 --2.6.2"}, description = "/shop/user/ord/type |用户【订单类型】模块相关")
public class OrdTypeController extends BaseController {

    @Autowired
    private OrdTypeService ordTypeService;

    /**
     * 根据店铺ID查询【订单类型】列表
     *
     * @return Result
     */
    @ApiOperation(
            value = "查询【订单类型】列表;",
            notes = "查询【订单类型】列表",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token")
    })
    @PostMapping("/listOrdType")
    public BaseResult<List<VoOrdType>> listOrdType() {
        Integer shopId =  getShopId();
        List<OrdType> ordTypeList = ordTypeService.listOrdTypeByShopId(shopId);
        List<VoOrdType> voOrdTypeList = formatOrdTypeList(ordTypeList);
        return BaseResult.okResult(voOrdTypeList);
    }

    /**
     * 将订单类型DO转换为VO
     * @param ordTypeList
     * @return
     */
    private List<VoOrdType> formatOrdTypeList(List<OrdType> ordTypeList) {
        List<VoOrdType> voOrdTypeList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(ordTypeList)){
            for (OrdType ordType : ordTypeList) {
                VoOrdType voOrdType = new VoOrdType();
                BeanUtils.copyProperties(ordType,voOrdType);
                voOrdTypeList.add(voOrdType);
            }
        }
        return voOrdTypeList;
    }


    /**
     * 添加订单类型
     *
     * @param ordTypeName 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "添加【订单类型】;",
            notes = "添加【订单类型】",
            httpMethod = "POST")
    @ApiImplicitParams({

            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token")
    })
    @RequestRequire
    @PostMapping("/addOrdType")
    public BaseResult addOrdType(@RequestParam(value="ordTypeName",required=true) String  ordTypeName, HttpServletRequest request) {
        Integer shopId =  getShopId();
        Integer userId = getUserId();
        OrdType ordType;
        try {
            ordType = ordTypeService.addOrdType(shopId, userId, ordTypeName,request);
        }catch (Exception e){
            return BaseResult.defaultErrorWithMsg(e.getMessage());
        }
        return BaseResult.okResult(ordType.getId());
    }
    /**
     * 添加订单类型
     *
     * @param ordTypeUpFopApp 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "编辑【订单类型】--2.6.2;",
            notes = "编辑【订单类型】--2.6.2",
            httpMethod = "POST")
    @PostMapping("/updateOrderType")
    public BaseResult updateOrderType(@Valid ParamOrdTypeUpFopApp ordTypeUpFopApp, HttpServletRequest request,BindingResult result) {
        servicesUtil.validControllerParam(result);
        Integer shopId =  getShopId();
        Integer userId = getUserId();
        try {
            ordTypeUpFopApp.setUserId(userId);
            ordTypeUpFopApp.setShopId(shopId);
            ordTypeUpFopApp.setRequest(request);
             ordTypeService.updateOrderType(ordTypeUpFopApp);
        }catch (Exception e){
            return BaseResult.defaultErrorWithMsg(e.getMessage());
        }
        return BaseResult.okResult();
    }
    /**
     * 删除订单类型
     *
     * @param
     * @return Result
     */
    @ApiOperation(
            value = "删除【订单类型】",
            notes = "删除【订单类型】;",
            httpMethod = "POST")
    @RequestMapping("/deleteOrdType")
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true) })
    public BaseResult deleteOrdType(@RequestParam(value="ordTypeId",required=true) String  ordTypeId,HttpServletRequest request) {
        Integer shopId =  getShopId();
        Integer userId = getUserId();
        Integer result = ordTypeService.deleteOrdType(shopId,userId,Integer.parseInt(ordTypeId),request);
        return BaseResult.okResult(result);
    }
}
