package com.luxuryadmin.apiadmin.fin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.fin.FinShopRecord;
import com.luxuryadmin.enums.fin.EnumFinShopRecordType;
import com.luxuryadmin.param.fin.ParamFinShopRecordAdd;
import com.luxuryadmin.param.fin.ParamFinShopRecordId;
import com.luxuryadmin.param.fin.ParamFinShopRecordQuery;
import com.luxuryadmin.param.fin.ParamFinShopRecordUpdate;
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
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public BaseResult listFinShopRecord(
            @Valid ParamFinShopRecordQuery paramFinShopRecordQuery, BindingResult result) {
        servicesUtil.validControllerParam(result);
        Integer shopId = getShopId();
        VoFinShopRecordHomePageTop voTop;
        PageInfo pageInfo;
        try {
            String all = ConstantPermission.SHOP_CHECK_SHOPBILL;
            boolean hasAll = hasPermWithCurrentUser(all);
            //沒有查看全部的权限,只能查看自己;
            if (!hasAll) {
                paramFinShopRecordQuery.setUserId(getUserId() + "");
            }
            voTop = finShopRecordService.getFinShopRecordHomePageTop(shopId, paramFinShopRecordQuery);
            PageHelper.startPage(getPageNum(), PAGE_SIZE_10);
            pageInfo = finShopRecordService.listFinShopRecordByShopIdForAdmin(shopId, paramFinShopRecordQuery);

        } catch (Exception e) {
            throw new MyException(e.getMessage());
        }
        HashMap<String, Object> hashMap = LocalUtils.getHashMap(pageInfo.getList());
        Map<String, Object> map2 = LocalUtils.convertBeanToMap(voTop);
        hashMap.putAll(map2);
        hashMap.put("totalSize", pageInfo.getTotal());
        return BaseResult.okResult(hashMap);
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
    public BaseResult<VoFinShopRecordDetail> getFinShopRecordDetailById(@Valid ParamFinShopRecordId param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        Integer shopId = getShopId();
        int fsrId = Integer.parseInt(param.getFinShopRecordId());
        VoFinShopRecordDetail voDetail = finShopRecordService.getFinShopRecordDetailById(shopId, fsrId);
        return BaseResult.okResult(voDetail);
    }
}
