package com.luxuryadmin.admin.ord;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.aop.check.RequiresPerm;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.param.ord.ParamOrdOrder;
import com.luxuryadmin.service.ord.OrdOrderService;
import com.luxuryadmin.vo.ord.VoOrder;
import com.luxuryadmin.vo.pro.VoProduct;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @Classname OrdOrderController
 * @Description TODO
 * @Date 2020/6/30 9:45
 * @Created by Administrator
 */
@Slf4j
@RestController
@RequestMapping(value = "/ord")
@Api(tags = {"4.【订单管理】模块"}, description = "/ord/order | 订单列表 ")
public class OrdOrderController extends BaseController {

    @Autowired
    private OrdOrderService ordOrderService;

    @RequestMapping(value = "/listOrdOrder", method = RequestMethod.GET)
    @ApiOperation(
            value = "分页查询订单列表;",
            notes = "分页查询订单列表;",
            httpMethod = "GET")
    @RequiresPerm(value = "order:list")
    public BaseResult listOrdOrder(@Valid ParamOrdOrder paramOrdOrder, BindingResult result) {
        servicesUtil.validControllerParam(result);
        PageHelper.startPage(paramOrdOrder.getPageNum(), paramOrdOrder.getPageSize());
        List<VoOrder> voOrders = ordOrderService.queryOrdOrderList(paramOrdOrder);
        PageInfo pageInfo = new PageInfo(voOrders);
        return LocalUtils.getBaseResult(pageInfo);
    }


    @RequestMapping(value = "/getOrdOrderInfo", method = RequestMethod.GET)
    @ApiOperation(
            value = "查询订单详细信息;",
            notes = "查询订单详细信息;",
            httpMethod = "GET")
    public BaseResult getOrdOrderInfo(@RequestParam String id){
        VoOrder info = ordOrderService.getOrdOrderInfo(id);
        return LocalUtils.getBaseResult(info);
    }
}
