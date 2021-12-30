package com.luxuryadmin.api.shp;

import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.shp.ShpAfterSaleGuarantee;
import com.luxuryadmin.param.shp.ParamAddShpAfterSaleGuarantee;
import com.luxuryadmin.service.shp.ShpAfterSaleGuaranteeService;
import com.luxuryadmin.vo.shp.VoShpAfterSaleGuarantee;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author sanjin
 * @date 2020-08-20 15:01
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/guarantee", method = RequestMethod.POST)
@Api(tags = {"E005.【店铺】模块"}, description = "/shop/user/guarantee | 店铺售后保障")
public class ShpAfterSaleGuaranteeController extends BaseController {


    @Autowired
    private ShpAfterSaleGuaranteeService shpAfterSaleGuaranteeService;


    /**
     * 添加店铺售后保障
     *
     * @param
     * @return Result
     */
    @ApiOperation(
            value = "添加店铺售后保障",
            notes = "添加店铺售后保障;",
            httpMethod = "POST")
    @RequestMapping("/addAfterSaleGuarantee")
    public BaseResult addAfterSaleGuarantee(@Valid ParamAddShpAfterSaleGuarantee guaranteeInfo, BindingResult result) {
        servicesUtil.validControllerParam(result);
        Integer shopId =  getShopId();
        Integer userId = getUserId();
        ShpAfterSaleGuarantee shpAfterSaleGuarantee= shpAfterSaleGuaranteeService.addAfterSaleGuarantee(shopId,userId,guaranteeInfo);
        return BaseResult.okResult(shpAfterSaleGuarantee.getId());
    }

    /**
     * 删除店铺售后保障
     *
     * @param
     * @return Result
     */
    @ApiOperation(
            value = "删除店铺售后保障",
            notes = "删除店铺售后保障;",
            httpMethod = "POST")
    @RequestMapping("/deleteAfterSaleGuarantee")
    public BaseResult deleteAfterSaleGuarantee(Integer  guaranteeId) {
        Integer shopId =  getShopId();
        Integer userId = getUserId();
        try {
            shpAfterSaleGuaranteeService.deleteAfterSaleGuarantee(shopId, userId, guaranteeId);
        }catch (Exception e){
            return BaseResult.defaultErrorWithMsg(e.getMessage());
        }
        return BaseResult.okResult();
    }

    /**
     * 删除店铺售后保障
     *
     * @param
     * @return Result
     */
    @ApiOperation(
            value = "获取店铺售后保障列表",
            notes = "获取店铺售后保障列表;",
            httpMethod = "POST")
    @RequestMapping("/listAfterSaleGuarantee")
    public BaseResult listAfterSaleGuarantee() {
        Integer shopId =  getShopId();
        List<VoShpAfterSaleGuarantee> list = shpAfterSaleGuaranteeService.listAfterSaleGuarantee(shopId);
        return BaseResult.okResult(list);
    }

}
