package com.luxuryadmin.api.fin;

import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.entity.fin.FinShopRecord;
import com.luxuryadmin.enums.fin.EnumFinShopRecordType;
import com.luxuryadmin.param.fin.ParamFinShopRecordAdd;
import com.luxuryadmin.param.fin.ParamFinShopRecordQuery;
import com.luxuryadmin.param.fin.ParamFinShopRecordUpdate;
import com.luxuryadmin.param.fin.ParamFundRecordAdd;
import com.luxuryadmin.service.fin.FinFundRecordService;
import com.luxuryadmin.service.fin.FinShopRecordService;
import com.luxuryadmin.service.fin.FinShopRecordTypeService;
import com.luxuryadmin.vo.fin.VoFinShopRecord;
import com.luxuryadmin.vo.fin.VoFinShopRecordDetail;
import com.luxuryadmin.vo.fin.VoFinShopRecordHomePageTop;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 店铺财务流水模块Controller
 *
 * @author sanjin145
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/fin/record", method = RequestMethod.POST)
@Api(tags = {"F003.【财务】模块"}, description = "/shop/user/fin/record |店铺财务流水")
public class FinShopRecordController extends BaseController {

    @Autowired
    private FinShopRecordService finShopRecordService;

    @Autowired
    private FinShopRecordTypeService finShopRecordTypeService;

    @Autowired
    private FinFundRecordService fundRecordService;

    /**
     * 根据店铺ID查询【财务流水类型】列表
     *
     * @return Result
     */
    @ApiOperation(
            value = "查询所有【财务流水类型】列表,不根据支出收入类型区分;",
            notes = "查询所有【财务流水类型】列表,不根据支出收入类型区分;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token")
    })
    @PostMapping("/listAllRecordTypeWithoutInoutType")
    public BaseResult<List<String>> listAllRecordTypeWithoutInoutType() {
        Integer shopId = getShopId();
        List<String> voOrdTypeList = finShopRecordTypeService.listAllRecordTypeWithoutInoutType(shopId);
        return BaseResult.okResult(voOrdTypeList);
    }

    /**
     * 查询【财务流水】页面顶部汇总信息
     *
     * @return Result
     */
    @ApiOperation(
            value = "查询【财务流水】页面顶部汇总信息;",
            notes = "查询【财务流水】页面顶部汇总信息",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token")
    })
    @PostMapping("/getFinShopRecordHomePageTop")
    @RequiresPermissions("shop:bill:add")
    public BaseResult<VoFinShopRecordHomePageTop> getFinShopRecordHomePageTop(
            @Valid ParamFinShopRecordQuery paramFinShopRecordQuery, BindingResult result) {
        servicesUtil.validControllerParam(result);
        Integer shopId = getShopId();
        VoFinShopRecordHomePageTop voTop = null;
        String all = ConstantPermission.SHOP_CHECK_SHOPBILL;
        boolean hasAll = hasPermWithCurrentUser(all);
        //沒有查看全部的权限,只能查看自己;
        if (!hasAll) {
            paramFinShopRecordQuery.setUserId(getUserId() + "");
        }
        try {
            voTop = finShopRecordService.getFinShopRecordHomePageTop(shopId, paramFinShopRecordQuery);
        } catch (Exception e) {
            throw new MyException(e.getMessage());
        }
        return BaseResult.okResult(voTop);
    }

    /**
     * 根据店铺ID查询【财务流水】列表
     *
     * @return Result
     */
    @ApiOperation(
            value = "分页查询【财务流水】列表;",
            notes = "分页查询【财务流水】列表",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token")
    })
    @PostMapping("/listFinShopRecord")
    public BaseResult<List<List<VoFinShopRecord>>> listFinShopRecord(
            @Valid ParamFinShopRecordQuery paramFinShopRecordQuery, BindingResult result) {
        servicesUtil.validControllerParam(result);
        Integer shopId = getShopId();
        PageHelper.startPage(getPageNum(), PAGE_SIZE_10);
        List<List<VoFinShopRecord>> voRecordList;
        try {
            String all = ConstantPermission.SHOP_CHECK_SHOPBILL;
            boolean hasAll = hasPermWithCurrentUser(all);
            //沒有查看全部的权限,只能查看自己;
            if (!hasAll) {
                paramFinShopRecordQuery.setUserId(getUserId() + "");
            }
            voRecordList = finShopRecordService.listFinShopRecordByShopId(shopId, paramFinShopRecordQuery);
        } catch (Exception e) {
            throw new MyException(e.getMessage());
        }
        return BaseResult.okResult(voRecordList);
    }

    /**
     * 添加财务流水
     *
     * @param paramFinShopRecordAdd 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "添加【财务流水】;",
            notes = "添加【财务流水】",
            httpMethod = "POST")
    @ApiImplicitParams({

            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token")
    })
    @RequestRequire
    @PostMapping("/addFinShopRecord")
    @RequiresPermissions("shop:bill:add")
    public BaseResult addFinShopRecord(@Valid ParamFinShopRecordAdd paramFinShopRecordAdd, BindingResult result) {
        servicesUtil.validControllerParam(result);
        Integer shopId = getShopId();
        Integer userId = getUserId();
        FinShopRecord shopRecordType = finShopRecordService.addFinShopRecord(shopId, userId, paramFinShopRecordAdd, EnumFinShopRecordType.MANUAL, null, null);


        return BaseResult.okResult(shopRecordType.getId());
    }

    @ApiOperation(
            value = "记一笔编辑功能",
            notes = "记一笔编辑功能",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token")
    })
    @RequestRequire
    @PostMapping("/updateFinShopRecord")
    public BaseResult<FinShopRecord> updateFinShopRecord(@Valid ParamFinShopRecordUpdate paramFinShopRecordUpdate, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        Integer shopId = getShopId();
        Integer userId = getUserId();
        paramFinShopRecordUpdate.setShopId(shopId);
        paramFinShopRecordUpdate.setUserId(userId);
        FinShopRecord finShopRecord = finShopRecordService.updateFinShopRecord(paramFinShopRecordUpdate, request);
        if (finShopRecord == null) {
            throw new MyException("此记一笔为空");
        }

        return BaseResult.okResult(finShopRecord);
    }

    /**
     * 删除财务流水
     *
     * @param
     * @return Result
     */
    @ApiOperation(
            value = "删除【财务流水】",
            notes = "删除【财务流水】;",
            httpMethod = "POST")
    @RequestMapping("/deleteFinShopRecord")
    @RequiresPermissions("shop:bill:delete")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true)})
    public BaseResult deleteFinShopRecord(@RequestParam(value = "finShopRecordId", required = true) String finShopRecordId,
                                          HttpServletRequest request) {
        Integer shopId = getShopId();
        Integer userId = getUserId();
        Integer result = finShopRecordService.deleteFinShopRecord(shopId, userId, Integer.parseInt(finShopRecordId), request);
        return BaseResult.okResult(result);
    }

    /**
     * 获取【财务流水】详情
     *
     * @param
     * @return Result
     */
    @ApiOperation(
            value = "获取【财务流水】详情",
            notes = "获取【财务流水】详情;",
            httpMethod = "POST")
    @RequestMapping("/getFinShopRecordDetailById")
    @RequiresPermissions("shop:check:shopBill")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true)})
    public BaseResult<VoFinShopRecordDetail> getFinShopRecordDetailById(@RequestParam(value = "finShopRecordId", required = true) String finShopRecordId) {
        Integer shopId = getShopId();
        VoFinShopRecordDetail voDetail = finShopRecordService.getFinShopRecordDetailById(shopId, Integer.parseInt(finShopRecordId));
        return BaseResult.okResult(voDetail);
    }
}
