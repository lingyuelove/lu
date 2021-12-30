package com.luxuryadmin.admin.mem;

import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.aop.check.RequiresPerm;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.ExcelUtils;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.sys.SysUser;
import com.luxuryadmin.param.mem.ParamMemShop;
import com.luxuryadmin.param.mem.ParamMemShopOrder;
import com.luxuryadmin.param.pay.ParamAddPayOrderForAdmin;
import com.luxuryadmin.param.sys.ParamSysUser;
import com.luxuryadmin.service.pay.PayOrderService;
import com.luxuryadmin.vo.mem.VoMemShop;
import com.luxuryadmin.vo.mem.VoMemShopOrder;
import com.luxuryadmin.vo.sys.VoSysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/mem/shop/order")
@Api(tags = {"7.【会员管理】模块"}, description = "/mem/shop/order | 商铺会员订单列表 ")
public class MemShopOrderController extends BaseController {

    @Autowired
    private PayOrderService payOrderService;

    @RequestMapping(value = "/listMemShopOrder", method = RequestMethod.GET)
    @ApiOperation(
            value = "分页查询商铺会员订单列表;",
            notes = "分页查询商铺会员订单列表;",
            httpMethod = "GET")
    @RequiresPerm(value = "member:shop:order")
    public BaseResult listOrdOrder(@Valid ParamMemShopOrder paramMemShopOrder, BindingResult result) {
        servicesUtil.validControllerParam(result);
        if (LocalUtils.isEmptyAndNull(paramMemShopOrder.getPageNum())){
            paramMemShopOrder.setPageNum(1);
        }
        if (LocalUtils.isEmptyAndNull(paramMemShopOrder.getPageSize())){
            paramMemShopOrder.setPageSize(20);
        }
        PageHelper.startPage(paramMemShopOrder.getPageNum(), paramMemShopOrder.getPageSize());
        List<VoMemShopOrder> voMemShopOrders = payOrderService.pageVoMemShopOrder(paramMemShopOrder);
        PageInfo pageInfo = new PageInfo(voMemShopOrders);
        return LocalUtils.getBaseResult(pageInfo);
    }


    @RequestMapping(value = "/exportExcelMemShopOrder", method = RequestMethod.GET)
    @ApiOperation(
            value = "商铺会员订单导出;",
            notes = "商铺会员订单导出;",
            httpMethod = "GET")
    public void downExcel(@Valid ParamMemShopOrder paramMemShopOrder, HttpServletResponse response){
        PageHelper.startPage(1, 50000);
        List<VoMemShopOrder> voMemShopOrders = payOrderService.pageVoMemShopOrder(paramMemShopOrder);
        List<VoMemShopOrder> voMemShopOrderList = new ArrayList<>();
        for (VoMemShopOrder voMemShopOrder : voMemShopOrders) {
            VoMemShopOrder voMemShopOrder1 = new VoMemShopOrder();
            BeanUtils.copyProperties(voMemShopOrder,voMemShopOrder1);
            if (!LocalUtils.isEmptyAndNull( voMemShopOrder.getState())){
                String stateCn = voMemShopOrder.getState().toString();

                if ("40".equals(stateCn)) {
                    stateCn = "已支付";
                } else if ("10".equals(stateCn)) {
                    stateCn = "待支付";
                }
                voMemShopOrder1.setStateCn(stateCn);
            }

            voMemShopOrderList.add(voMemShopOrder1);
        }
        List<VoMemShopOrder> voMemShopOrderList1 = CollUtil.newArrayList(voMemShopOrderList);
        try {
            ExcelUtils.downExcel(response,voMemShopOrderList1,"商铺会员订单.xlsx");
        } catch (IOException e) {
            log.error("导出失败！",e);
        }
    }



    @PostMapping("/addPayOrderForAdmin")
    @ApiOperation(
            value = "新增商户会员订单;",
            notes = "新增商户会员订单;",
            httpMethod = "POST")
    public BaseResult listOrdOrder(@Valid ParamAddPayOrderForAdmin paramAddPayOrderForAdmin, BindingResult result) {
        servicesUtil.validControllerParam(result);

        return  payOrderService.addPayOrderForAdmin(paramAddPayOrderForAdmin);
    }
}
