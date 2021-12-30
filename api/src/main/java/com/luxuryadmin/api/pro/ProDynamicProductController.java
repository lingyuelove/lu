package com.luxuryadmin.api.pro;

import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.enums.pro.EnumDynamicInitialize;
import com.luxuryadmin.enums.pro.EnumDynamicProductState;
import com.luxuryadmin.param.pro.ParamDynamicProductAdd;
import com.luxuryadmin.param.pro.ParamDynamicProductDelete;
import com.luxuryadmin.param.pro.ParamDynamicProductQuery;
import com.luxuryadmin.service.pro.ProDynamicProductService;
import com.luxuryadmin.service.util.ProProductBaseController;
import com.luxuryadmin.vo.pro.VoDynamicProductList;
import com.luxuryadmin.vo.pro.VoDynamicProductShow;
import com.luxuryadmin.vo.pro.VoProRedisNum;
import com.luxuryadmin.vo.pro.VoProductLoad;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 动态列表商品信息 controller
 *
 * @author zhangsai
 * @Date 2021-11-05 11:45:33
 */
@Api(tags = {"C003.1【商品位置】模块 --2.6.6"}, description = "商品位置商品信息")
@RestController
@RequestMapping(value = "/shop/user/pro/dynamicProduct")
public class ProDynamicProductController extends ProProductBaseController {


    @Autowired
    private ProDynamicProductService proDynamicProductService;

    @ApiOperation(
            value = "获取商品位置列表",
            httpMethod = "POST")
    @RequestRequire
    @PostMapping("/listDynamicProduct")
    public BaseResult<VoDynamicProductShow> listDynamicProduct(@Valid ParamDynamicProductQuery param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        int shopId = getShopId();
        PageHelper.startPage(getPageNum(), getPageSize());
        List<VoDynamicProductList> dynamicProducts = proDynamicProductService.listDynamicProduct(param);
        Map<Integer, VoDynamicProductList> voProDeliverByPageMapById = dynamicProducts.stream().collect(Collectors.toMap(VoDynamicProductList::getDynamicProductId, Function.identity()));
        List<VoProductLoad> voProductLoads = new ArrayList<>();
        dynamicProducts.stream().forEach(vp -> {
            VoProductLoad voProductLoad = new VoProductLoad();
            BeanUtils.copyProperties(vp, voProductLoad);
            voProductLoads.add(voProductLoad);
        });
        formatVoProductLoad(null, voProductLoads, null);
        List<VoDynamicProductList> resuList = new ArrayList<>();
        voProductLoads.stream().forEach(vp -> {
            VoDynamicProductList voDynamicProductList = new VoDynamicProductList();
            BeanUtils.copyProperties(vp, voDynamicProductList);
            VoDynamicProductList voDynamicProductList1 = voProDeliverByPageMapById.get(vp.getDynamicProductId());
            voDynamicProductList.setShowTime("添加时间：" + voDynamicProductList1.getShowTime());
            voDynamicProductList.setUserName(voDynamicProductList1.getUserName());
            voDynamicProductList.setState(voDynamicProductList1.getState());

            if (voDynamicProductList1.getState().equals(EnumDynamicProductState.SOLD_OUT.getCode().toString())){
                voDynamicProductList.setStateDescribe(EnumDynamicProductState.SOLD_OUT.getMsg());
            }else if (voDynamicProductList1.getState().equals(EnumDynamicProductState.ALREADY_DELETE.getCode().toString())){
                voDynamicProductList.setStateDescribe(EnumDynamicProductState.ALREADY_DELETE.getMsg());
            }else if (voDynamicProductList1.getState().equals(EnumDynamicProductState.ALREADY_LOCK.getCode().toString())){
                VoProRedisNum proRedisNum = proProductService.getProRedisNum(shopId, vp.getBizId());
                voDynamicProductList.setLockState(proRedisNum.getLeftNum() == 0 ? "1" : "0");
            }
            voDynamicProductList.setRemark(StringUtil.isNotBlank(voDynamicProductList1.getRemark())?voDynamicProductList1.getRemark():null);
            voDynamicProductList.setUniqueCode(voDynamicProductList1.getUniqueCode());
            resuList.add(voDynamicProductList);
        });
        VoDynamicProductShow dynamicProductShow =new VoDynamicProductShow();
        String editPerm = ConstantPermission.MOD_UPDATE_INFO;
        String uPermDynamicDel = ConstantPermission.DYNAMIC_DELETE;

        dynamicProductShow.setUPermEdit(hasPermToPageWithCurrentUser(editPerm));
        dynamicProductShow.setUPermDynamicDel(hasPermToPageWithCurrentUser(uPermDynamicDel));
        dynamicProductShow.setDynamicProducts(resuList);
        return BaseResult.okResult(dynamicProductShow);
    }


    @ApiOperation(
            value = "添加商品位置",
            httpMethod = "POST")
    @RequestRequire
    @PostMapping("/saveDynamicProduct")
    public BaseResult saveDynamicProduct(@Valid ParamDynamicProductAdd param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        int shopId = getShopId();
        int userId = getUserId();
        param.setShopId(shopId);
        param.setUserId(userId);
        proDynamicProductService.saveDynamicProduct(param);
        return BaseResult.okResult();
    }

    @ApiOperation(
            value = "删除商品位置",
            httpMethod = "POST")
    @RequestRequire
    @PostMapping("/deleteDynamicProduct")
    public BaseResult deleteDynamicProduct(@Valid ParamDynamicProductDelete param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        proDynamicProductService.deleteDynamicProduct(param);
        return BaseResult.okResult();
    }
}
