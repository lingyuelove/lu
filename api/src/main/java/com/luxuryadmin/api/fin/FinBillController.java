package com.luxuryadmin.api.fin;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.param.fin.ParamBillDaySearch;
import com.luxuryadmin.param.fin.ParamBillSearch;
import com.luxuryadmin.param.fin.ParamBillCreate;
import com.luxuryadmin.param.fin.ParamBillUpdate;
import com.luxuryadmin.service.fin.FinBillService;
import com.luxuryadmin.vo.fin.VoBillByApp;
import com.luxuryadmin.vo.fin.VoBillDetailByApp;
import com.luxuryadmin.vo.fin.VoBillProductByApp;
import com.luxuryadmin.vo.fin.VoBillProductMoneyByApp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import javax.validation.Valid;
import java.util.List;


/**
 * 帐单表
 *
 * @author zhangSai
 * @date   2021/04/23 14:19:52
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/bill", method = RequestMethod.POST)
@Api(tags = "F004.【账单管理】模块", description = "/shop/user/bill | 账单管理")
public class FinBillController extends BaseController {
    @Autowired
    private FinBillService billService;
    /**
     * 添加账单
     *
     * @param billCreate 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "创建对账单;",
            notes = "创建对账单",
            httpMethod = "POST")
    @ApiImplicitParams({

            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token")
    })
    @RequestRequire
    @PostMapping("/addBill")
    @RequiresPermissions("statement:add")
    public BaseResult addBill(@Valid ParamBillCreate billCreate, BindingResult result) {
        servicesUtil.validControllerParam(result);
        Integer shopId =  getShopId();
        Integer userId = getUserId();
        billCreate.setUserId(userId);
        billCreate.setShopId(shopId);
        return BaseResult.okResult(billService.addBill(billCreate));
    }

    /**
     * 修改对账单
     *
     * @param billUpdate 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "修改对账单;",
            notes = "修改对账单",
            httpMethod = "POST")
    @ApiImplicitParams({

            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token")
    })
    @RequestRequire
    @PostMapping("/updateBill")
    @RequiresPermissions("statement:add")
    public BaseResult updateBill(@Valid ParamBillUpdate billUpdate, BindingResult result) {
        servicesUtil.validControllerParam(result);
        Integer shopId =  getShopId();
        Integer userId = getUserId();
        billUpdate.setUserId(userId);
        billUpdate.setShopId(shopId);
        return BaseResult.okResult(billService.updateBill(billUpdate));
    }

    /**
     * 删除对账单
     *
     * @param id 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "删除对账单;",
            notes = "删除对账单",
            httpMethod = "POST")
    @ApiImplicitParams({

            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "id", value = "主键id")
    })
    @RequestRequire
    @PostMapping("/deleteBill")
    @RequiresPermissions("statement:add")
    public BaseResult deleteBill(@RequestParam(value="id",required=true) String id) {
        ParamBillUpdate billUpdate = new ParamBillUpdate();
        Integer shopId =  getShopId();
        Integer userId = getUserId();
        billUpdate.setUserId(userId);
        billUpdate.setShopId(shopId);
        billUpdate.setBillId(Integer.parseInt(id));
        return BaseResult.okResult(billService.deleteBill(billUpdate));
    }


    /**
     * 对账单首页
     *
     * @return Result
     */
    @ApiOperation(
            value = "对账单首页;",
            notes = "对账单首页",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token"),
    })
    @GetMapping("/getBillPageByApp")
    @RequiresPermissions("statement:list")
    public BaseResult<VoBillByApp> getBillPageByApp(@Valid ParamBillSearch billSearch,String token) {
        billSearch.setUserId(getUserId());
        billSearch.setShopId(getShopId());
        VoBillByApp billPageByApp = billService.getBillPageByApp(billSearch);

        return BaseResult.okResult(billPageByApp);
    }



    /**
     * 对账单首页
     *
     * @return Result
     */
    @ApiOperation(
            value = "对账单详情;",
            notes = "对账单详情",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "billId", value = "账单id"),
    })
    @GetMapping("/getBillDetailByApp")
    @RequiresPermissions("statement:list")
    public BaseResult<VoBillDetailByApp> getBillDetailByApp(@Valid ParamBillDaySearch billDaySearch) {
        billDaySearch.setShopId(getShopId());
        VoBillDetailByApp billPageByApp = billService.getBillDetailByApp(billDaySearch);

        return BaseResult.okResult(billPageByApp);
    }

    /**
     * 创建对账单
     *
     * @return Result
     */
    @ApiOperation(
            value = "选择对账类型--创建对账单;",
            notes = "选择对账类型--创建对账单",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token"),
    })
    @GetMapping("/getBillProductMoneyByApp")
    @RequiresPermissions("statement:add")
    public BaseResult<VoBillProductByApp> getBillProductMoneyByApp() {
        Integer shopId = getShopId();
        Integer userId = getUserId();
        VoBillProductByApp billProductMoneyByApp = billService.getBillProductMoneyByApp(shopId, userId);

        return BaseResult.okResult(billProductMoneyByApp);
    }
}
