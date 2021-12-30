package com.luxuryadmin.api.data;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.VersionUtils;
import com.luxuryadmin.entity.ord.OrdType;
import com.luxuryadmin.enums.pro.EnumProAttribute;
import com.luxuryadmin.param.data.ParamDataRecycleQuery;
import com.luxuryadmin.param.data.ParamDataSaleRankQuery;
import com.luxuryadmin.service.pro.ProAttributeService;
import com.luxuryadmin.service.pro.ProClassifyService;
import com.luxuryadmin.service.pro.ProDynamicService;
import com.luxuryadmin.service.pro.ProProductService;
import com.luxuryadmin.service.shp.ShpUserShopRefService;
import com.luxuryadmin.service.util.ProProductBaseController;
import com.luxuryadmin.vo.data.VoRecycleClassifyHome;
import com.luxuryadmin.vo.data.VoRecycleProductList;
import com.luxuryadmin.vo.data.VoRecycleProductShow;
import com.luxuryadmin.vo.data.VoRecycleUserList;
import com.luxuryadmin.vo.org.VoOrganizationTempPageByApp;
import com.luxuryadmin.vo.pro.*;
import com.luxuryadmin.vo.shp.VoEmployee;
import com.luxuryadmin.vo.shp.VoShpAfterSaleGuarantee;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @PackgeName: com.luxuryadmin.api.pro
 * @ClassName: ProRecycleUserController
 * @Author: ZhangSai
 * Date: 2021/6/11 10:22
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/data/recycle", method = RequestMethod.POST)
@Api(tags = {"1.1【数据统计】模块"}, description = "/shop/user/data/recycle 【回收分析】--2.5.4")
public class ProRecycleUserProductController  extends ProProductBaseController {
    @Autowired
    protected ProProductService proProductService;
    @Autowired
    private ShpUserShopRefService shpUserShopRefService;

    @Autowired
    private ProClassifyService proClassifyService;
    @Autowired
    private ProDynamicService proDynamicService;
    @Autowired
    private ProAttributeService proAttributeService;
    @ApiOperation(
            value = "回收列表-- 回收的商品列表 --分页查询",
            notes = "回收列表-- 回收的商品列表 --分页查询",
            httpMethod = "POST")
    @RequestMapping(value = "/getRecycleProductList", method = RequestMethod.POST)
    public BaseResult<VoRecycleProductShow> getRecycleProductList(ParamDataRecycleQuery dataRecycleQuery, BindingResult result) {
        dataRecycleQuery.setShopId(getShopId());
        servicesUtil.validControllerParam(result);
        if (LocalUtils.isEmptyAndNull(dataRecycleQuery.getPageNum())){
            dataRecycleQuery.setPageNum("1");
        }
        setDefaultQueryDateRange(dataRecycleQuery);
        Page page = PageHelper.startPage(Integer.parseInt(dataRecycleQuery.getPageNum()),PAGE_SIZE_10);
        //商品列表是否有成本价权限
        boolean hasPermInitPrice = hasPermWithCurrentUser(ConstantPermission.CHK_PRICE_INIT);
        List<VoRecycleProductList> recycleProductLists =  proProductService.getRecycleProductList(dataRecycleQuery,hasPermInitPrice);
        //设置返回参数
        VoRecycleProductShow recycleProductShow =new VoRecycleProductShow();

        recycleProductShow.setTotalNumName("总库存(件)");
        recycleProductShow.setTotalPriceName("总成本(元)");
        if (LocalUtils.isEmptyAndNull(recycleProductLists)){
            if (!hasPermInitPrice){
                recycleProductShow.setTotalPrice("*****");
            }else {
                recycleProductShow.setTotalPrice("0");
            }
            recycleProductShow.setTotalNum("0");
            return  BaseResult.okResult(recycleProductShow);
        }
        String userPerms = servicesUtil.getUserPerms(getShopId(), getUserId());
        recycleProductLists.forEach(voRecycleProductList -> {
            formatVoProductLoad(dataRecycleQuery.getAppVersion(), voRecycleProductList, true, userPerms, "normal");

        });
        int shopId = getShopId();
        List<Integer> proIds = recycleProductLists.stream().map(VoProductLoad::getProId).collect(Collectors.toList());
        List<VoDynamicAndProductInfoList> voDynamicAndProductInfoLists = proDynamicService.listInfoByProId(proIds, shopId);
        if (voDynamicAndProductInfoLists != null && voDynamicAndProductInfoLists.size() > 0) {
            Map<Integer, VoDynamicAndProductInfoList> dynamicInfoMap = voDynamicAndProductInfoLists.stream().collect(Collectors.toMap(VoDynamicAndProductInfoList::getProId, Function.identity()));
            recycleProductLists.stream().forEach(pp -> {
                if (dynamicInfoMap.get(pp.getProId()) != null) {
                    pp.setDynamicName("商品位置：" + dynamicInfoMap.get(pp.getProId()).getName());
                    pp.setDynamicId(dynamicInfoMap.get(pp.getProId()).getDynamicId().toString());
                } else {
                    pp.setDynamicName("商品位置：无");
                }
            });
        } else {
            recycleProductLists.stream().forEach(pp -> {
                pp.setDynamicName("商品位置：无");
            });
        }
        recycleProductShow.setList(recycleProductLists);
        Map<String,Object> totalMap = proProductService.getRecycleClassifyCount(dataRecycleQuery);
        BigDecimal totalNum = new BigDecimal(0);
        BigDecimal totalPrice = new BigDecimal(0);
        if (!LocalUtils.isEmptyAndNull(totalMap)) {
            totalNum = (BigDecimal) totalMap.get("totalNum");
        }
        DecimalFormat df = new DecimalFormat(",##0.##");
        try {
            totalPrice = LocalUtils.calcNumber(totalMap.get("totalPrice"), "/", 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!hasPermInitPrice){
            recycleProductShow.setTotalPrice("*****");
        }else {
            recycleProductShow.setTotalPrice(df.format(totalPrice));
        }
        recycleProductShow.setTotalNum(df.format(totalNum));


        return  BaseResult.okResult(recycleProductShow);
    }

    @ApiOperation(
            value = "回收分析 --回收排行榜 --分页查询",
            notes = "回收分析 --回收排行榜 --分页查询",
            httpMethod = "POST")
    @RequestMapping(value = "/getRecycleUserList", method = RequestMethod.POST)
    public BaseResult getRecycleUserList(ParamDataRecycleQuery dataRecycleQuery, BindingResult result) {
        dataRecycleQuery.setShopId(getShopId());
        servicesUtil.validControllerParam(result);
        if (LocalUtils.isEmptyAndNull(dataRecycleQuery.getPageNum())){
            dataRecycleQuery.setPageNum("1");
        }
        if (dataRecycleQuery.getRecycleTimeStart() == null){
            dataRecycleQuery.setRecycleTimeStart(dataRecycleQuery.getFinishTimeStart());
        }
        setDefaultQueryDateRange(dataRecycleQuery);
        Page page = PageHelper.startPage(Integer.parseInt(dataRecycleQuery.getPageNum()),PAGE_SIZE_50);
        List<VoRecycleUserList> recycleUserLists =  proProductService.getRecycleUserList(dataRecycleQuery,page);
        HashMap<String, Object> hashMap =new HashMap<>();
        try {
            //2.6.7之前直接返回
            if (VersionUtils.compareVersion(dataRecycleQuery.getAppVersion(), "2.6.7") < 0) {
                return  BaseResult.okResult(recycleUserLists);
            }
            //2.6.7之后添加数量和价格返回
            Map<String,Object> totalMap = proProductService.getRecycleClassifyCount(dataRecycleQuery);
            BigDecimal totalNum = new BigDecimal(0);
            BigDecimal totalPrice ;
            if (!LocalUtils.isEmptyAndNull(totalMap)) {
                totalNum = (BigDecimal) totalMap.get("totalNum");

            }
            totalPrice = LocalUtils.calcNumber(totalMap.get("totalPrice"), "/", 100);
            DecimalFormat df = new DecimalFormat(",##0.##");
            hashMap.put("totalNum", df.format(totalNum));
            boolean hasPermInitPrice = hasPermWithCurrentUser(ConstantPermission.CHK_PRICE_INIT);
            if (!hasPermInitPrice){
                hashMap.put("totalPrice", "*****");
            }else {
                hashMap.put("totalPrice", df.format(totalPrice));
            }

        } catch (Exception e) {
            log.info("回收分析版本控制错误为"+e);
        }
        //2.6.7之后返回价格和数量
        //回收人员
        hashMap.put("recycleUserLists", recycleUserLists);

        return  BaseResult.okResult(hashMap);
    }

    @ApiOperation(
            value = "回收分析 --产品属性分析",
            notes = "回收分析 --产品属性分析",
            httpMethod = "POST")
    @RequestMapping(value = "/getRecycleClassifyHome", method = RequestMethod.POST)
    public BaseResult<List<VoRecycleClassifyHome>> getRecycleClassifyHome(ParamDataRecycleQuery dataRecycleQuery, BindingResult result) {
        dataRecycleQuery.setShopId(getShopId());
        servicesUtil.validControllerParam(result);
        setDefaultQueryDateRange(dataRecycleQuery);
        List<VoRecycleClassifyHome> recycleClassifyHomes =  proProductService.getRecycleClassifyHome(dataRecycleQuery);
        return  BaseResult.okResult(recycleClassifyHomes);
    }

    /**
     * 初始化开单页面;-订单类型;-销售人员列表
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "初始化回收列表页面;",
            notes = "初始化回收列表页面;初始化回收列表页面;<br/>" +
                    "初始化回收列表页面",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
    })
    @RequestRequire
    @PostMapping("/initRecycleParam")
    public BaseResult initRecycleParam(@RequestParam Map<String, String> params) {
        int shopId = getShopId();
        List<VoEmployee> voEmployeeList = shpUserShopRefService.getSalesmanByShopId(shopId);
        List<VoProClassify> voProClassifyList = proClassifyService.listProClassifyByState(shopId, "1");
        List<VoProAttribute> voProAttributeList = proAttributeService.listProAttributeByShopId(shopId);
        HashMap<String, Object> hashMap =new HashMap<>();
        //员工列表VO层
        hashMap.put("salesmanList", voEmployeeList);
        //商品属性
        hashMap.put("attributeList", voProAttributeList);
        //商品分类
        hashMap.put("classifyList", voProClassifyList);
//        //添加库存和数量
//        hashMap.put("totalNum", 1);
//        hashMap.put("totalPrice", 1);
        return BaseResult.okResult(hashMap);
    }
    /**
     * 设置默认查询时间范围
     * @param dataRecycleQuery
     */
    private void setDefaultQueryDateRange(ParamDataRecycleQuery dataRecycleQuery) {
        Boolean flag = null == dataRecycleQuery.getRecycleTimeStart() || null == dataRecycleQuery.getFinishTimeStart();
        if(flag && null == dataRecycleQuery.getRecycleTimeEnd()){
            Date startDate = DateUtil.getFirstDayOfCurrentMonth();
            Date endDate = DateUtil.getLastDayOfCurrentMonth();

            dataRecycleQuery.setRecycleTimeStart(startDate);
            dataRecycleQuery.setRecycleTimeEnd(endDate);
        }
        //如果有分类参数
        String classifyCode = dataRecycleQuery.getClassifyCode();
        if (!LocalUtils.isEmptyAndNull(classifyCode)) {
            String[] classifyCodeArray = LocalUtils.splitString(classifyCode, ";");
            StringBuffer newCode = new StringBuffer();
            for (String code : classifyCodeArray) {
                if (code.length() >= 2) {
                    code = code.substring(0, 2);
                }
                newCode.append(code).append("|");
            }
            if (newCode.toString().endsWith("|")) {
                newCode = new StringBuffer(newCode.substring(0, newCode.length() - 1));
            }
            dataRecycleQuery.setClassifyCode(newCode.toString());
        }
        dataRecycleQuery.setAttributeCode(LocalUtils.formatParamForSqlInQuery(dataRecycleQuery.getAttributeCode()));
        dataRecycleQuery.setRecycleAdmin(LocalUtils.formatParamForSqlInQuery(dataRecycleQuery.getRecycleAdmin()));
        if (!LocalUtils.isEmptyAndNull(dataRecycleQuery.getProName())){
            dataRecycleQuery.setUniqueCode(dataRecycleQuery.getProName());
        }
    }

}
