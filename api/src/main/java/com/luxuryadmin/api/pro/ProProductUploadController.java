package com.luxuryadmin.api.pro;

import com.luxuryadmin.api.util.PublicProductUtil;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.base.BasicParam;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.common.utils.VersionUtils;
import com.luxuryadmin.entity.pro.ProDetail;
import com.luxuryadmin.entity.pro.ProDynamic;
import com.luxuryadmin.entity.pro.ProDynamicProduct;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.enums.fin.EnumFinShopRecordInoutType;
import com.luxuryadmin.enums.fin.EnumFinShopRecordType;
import com.luxuryadmin.enums.pro.EnumDynamicProductState;
import com.luxuryadmin.enums.pro.EnumProAttribute;
import com.luxuryadmin.enums.pro.EnumProState;
import com.luxuryadmin.enums.shp.EnumShpOperateLogModule;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.param.fin.ParamFinShopRecordAdd;
import com.luxuryadmin.param.fin.ParamFundRecordAdd;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.param.shp.ParamAddShpOperateLog;
import com.luxuryadmin.service.fin.FinFundRecordService;
import com.luxuryadmin.service.fin.FinShopRecordService;
import com.luxuryadmin.service.op.OpPushService;
import com.luxuryadmin.service.ord.OrdOrderService;
import com.luxuryadmin.service.pro.*;
import com.luxuryadmin.service.shp.ProQualityService;
import com.luxuryadmin.service.shp.ShpOperateLogService;
import com.luxuryadmin.service.shp.ShpUserService;
import com.luxuryadmin.service.shp.ShpUserShopRefService;
import com.luxuryadmin.service.util.ProProductBaseController;
import com.luxuryadmin.vo.pro.*;
import com.luxuryadmin.vo.shp.VoEmployee;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author monkey king
 * @date 2020-05-26 19:19:22
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/pro", method = RequestMethod.POST)
@Api(tags = {"B002.【进销】模块2.5.5--zs"}, description = "/shop/user/pro |用户【进销】模块相关")
public class ProProductUploadController extends ProProductBaseController {

    @Autowired
    private ProProductService proProductService;

    @Autowired
    private ProDetailService proDetailService;

    @Autowired
    private ProTempProductService proTempProductService;

    @Autowired
    private ProAttributeService proAttributeService;

    @Autowired
    private ProClassifyService proClassifyService;

    @Autowired
    private ProSourceService proSourceService;

    @Autowired
    private ProQualityService proQualityService;

    @Autowired
    private ProAccessoryService proAccessoryService;

    @Autowired
    private ShpUserShopRefService shpUserShopRefService;

    @Autowired
    private OpPushService opPushService;

    @Autowired
    private FinShopRecordService finShopRecordService;

    @Autowired
    private OrdOrderService ordOrderService;

    @Autowired
    private ShpOperateLogService shpOperateLogService;

    @Autowired
    private ShpUserService shpUserService;

    @Autowired
    private FinFundRecordService fundRecordService;
    @Autowired
    PublicProductUtil publicProductUtil;
    @Autowired
    private ProClassifySubService proClassifySubService;
    @Autowired
    private ProClassifyTypeService proClassifyTypeService;

    @Autowired
    private ProDynamicProductService proDynamicProductService;

    @Autowired
    private ProConveyProductService proConveyProductService;

    /**
     * 初始化上传商品页面
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "初始化上传商品页面",
            notes = "初始化上传商品页面;加载分类;加载属性;加载标签;",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
    })
    @GetMapping(value = "/initUpload")
    @RequiresPermissions(value = {"pro:uploadProduct", "pro:updateProInfo"}, logical = Logical.OR)
    public BaseResult initUpload(@Valid ParamProduct paramProduct, BindingResult result) {
        servicesUtil.validControllerParam(result);
        int shopId = getShopId();
        List<VoProAttribute> voProAttributeList = proAttributeService.listProAttributeByShopId(shopId);
        List<VoProClassify> voProClassifyList = proClassifyService.listProClassifyByState(shopId, "1");
        List<VoProSource> voProSourceList = proSourceService.listProSource(shopId);
        //List<VoProQuality> voProQualityList = proQualityService.listProQuality();
        List<VoProAccessory> voProAccessoryList = proAccessoryService.listProAccessoryByShopId(shopId);
        List<VoEmployee> voEmployeeList = shpUserShopRefService.getSalesmanByShopId(shopId);
        HashMap<String, Object> hashMap = new HashMap<>(16);
        //商品属性
        hashMap.put("attributeList", voProAttributeList);
        //商品分类
        hashMap.put("classifyList", voProClassifyList);
        //商品来源
        hashMap.put("proSourceList", voProSourceList);
        ////商品成色
        //hashMap.put("proQualityList", voProQualityList);
        //附件情况
        hashMap.put("proAccessoryList", voProAccessoryList);
        hashMap.put("recyclemanList", voEmployeeList);
        String vid = LocalUtils.getUUID();
        hashMap.put("vid", vid);
        String key = ConstantRedisKey.getInitUploadProductKey(shopId, getUserId());
        redisUtil.setExMINUTES(key, vid, 60);

        String ownPerm = ConstantPermission.CHK_OWN_PRODUCT;
        String entrustPerm = ConstantPermission.CHK_ENTRUST_PRODUCT;
        String pawnPerm = ConstantPermission.CHK_PAWN_PRODUCT;
        String otherPerm = ConstantPermission.CHK_OTHER_PRODUCT;
        String releasePerm = ConstantPermission.MOD_RELEASE_PRODUCT;
        String uPermOwn = hasPermToPageWithCurrentUser(ownPerm);
        String uPermEntrust = hasPermToPageWithCurrentUser(entrustPerm);
        String uPermPawn = hasPermToPageWithCurrentUser(pawnPerm);
        String uPermOther = hasPermToPageWithCurrentUser(otherPerm);
        hashMap.put("uPermOwn", uPermOwn);
        hashMap.put("uPermEntrust", uPermEntrust);
        hashMap.put("uPermPawn", uPermPawn);
        hashMap.put("uPermOther", uPermOther);
        hashMap.put("uPermRelease", hasPermToPageWithCurrentUser(releasePerm));
        hashMap.put("uPermBackoff", hasPermToPageWithCurrentUser(releasePerm));
        //获取动态权限
        String uPermDynamic = ConstantPermission.DYNAMIC_LIST;
        hashMap.put("uPermDynamic", hasPermToPageWithCurrentUser(uPermDynamic));
        //补充信息附件情况
        ParamClassifyTypeSearch classifyTypeSearch = new ParamClassifyTypeSearch();
        classifyTypeSearch.setShopId(getShopId());
        String bizId = paramProduct.getBizId();
//        String type =null;
//        Integer productId = 0;
//        if (!LocalUtils.isEmptyAndNull(bizId)){
//            ProProduct product = proProductService.getProProductForDeleteByShopIdBizId(shopId,bizId);
//            if (product != null){
//                type =product.getFkProClassifyCode();
//                productId = product.getId();
//            }
//        }
//        if (!LocalUtils.isEmptyAndNull(type) && type.equals("WB")){
//
//            classifyTypeSearch.setProductId(productId);
//        }
        classifyTypeSearch.setState("yes");
        classifyTypeSearch.setBizId(bizId);
        List<VoClassify> hashMapClassify = proClassifyTypeService.getClassifyTypeForClassify(classifyTypeSearch);
//        List<VoClassifyTypeSonList> classifyTypeListForWB = proClassifyTypeService.getClassifyTypeList(classifyTypeSearch);
//        hashMap.put("classifyTypeListForWB", classifyTypeListForWB);
//        classifyTypeSearch.setClassifyCode("XB");
//        classifyTypeSearch.setProductId(null);
//        if (!LocalUtils.isEmptyAndNull(type) && type.equals("XB")){
//            classifyTypeSearch.setProductId(productId);
//        }
//        List<VoClassifyTypeSonList> classifyTypeListForXB = proClassifyTypeService.getClassifyTypeList(classifyTypeSearch);
//        hashMap.put("classifyTypeListForXB", classifyTypeListForXB);
        hashMap.put("classifyTypeList", hashMapClassify);
        //公价查询连接 选取公价图的时候添加参数
//        hashMap.put("publicUrl", "https://www.luxuryadmin.com/h5/price-query.html?selectPublic=1");
        hashMap.put("publicUrl", publicProductUtil.getPublicUrl());
        return BaseResult.okResult(hashMap);
    }


    @ApiOperation(value = "加载二级分类---2.5.2---mong",
            notes = "加载二级分类；根据商铺及一级分类加载",
            httpMethod = "POST"
    )

    @RequestMapping("listProClassifySub")
    public BaseResult listProClassifySub(@Valid ParamProClassifySubQuery paramProClassifySubQuery, BindingResult result) {
        servicesUtil.validControllerParam(result);
        paramProClassifySubQuery.setShopId(getShopId());
        //查询店铺和一级分类下所有二级分类，按首字母进行匹配

        List<VoProClassifySub> classifySubs = proClassifySubService.listAllProClassifySub(paramProClassifySubQuery);
        return BaseResult.okResult(classifySubs);
    }


    /**
     * 上传商品
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "上传商品--2.6.2",
            notes = "1.商品图片,属性,库存;<br/>" +
                    "2.商品描述,商品名称,适用人群,分类,保卡(保卡年份);<br/>" +
                    "注意: 满足第一点,方可保存到仓库;<br/>" +
                    "同时满足第一,第二点,方可立即上架;<br/>" +
                    "在没有满足上架的条件时,隐藏上架按钮;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestMapping(value = "/uploadProduct")
    @RequiresPermissions("pro:uploadProduct")
    public BaseResult uploadProduct(@RequestParam Map<String, String> params,
                                    @Valid ParamProductUpload paramProductUpload, BindingResult result, HttpServletRequest request) {

        int shopId = getShopId();
        int userId = getUserId();
        servicesUtil.validControllerParam(result);
        checkReleaseParam(paramProductUpload);
        String vid = paramProductUpload.getVid();
        String key = ConstantRedisKey.getInitUploadProductKey(getShopId(), getUserId());
        servicesUtil.validVid(key, vid);
        ProProduct pro = proProductService.pickProProduct(paramProductUpload, true, shopId, userId);
        ProDetail proDetail = proProductService.pickProDetail(paramProductUpload, true);
        int proId = proProductService.saveProProductReturnId(pro, proDetail, paramProductUpload.getProductClassifyAddLists());
        redisUtil.delete(key);
        try {
            if (VersionUtils.compareVersion(paramProductUpload.getAppVersion(), "2.6.6") >= 0) {
                if (StringUtil.isNotBlank(paramProductUpload.getAttribute()) && !paramProductUpload.getAttribute().equals("30")
                        && StringUtil.isNotBlank(paramProductUpload.getDynamicId())) {
                    //2.6.6版本新增商品位置----质押商品不添加
                    ParamDynamicProductAdd dynamicProduct = new ParamDynamicProductAdd();
                    dynamicProduct.setUserId(userId);
                    dynamicProduct.setShopId(shopId);
                    dynamicProduct.setDynamicId(paramProductUpload.getDynamicId());
                    dynamicProduct.setProId(proId + "");
                    proDynamicProductService.saveDynamicProduct(dynamicProduct);
                }
            }
        } catch (Exception e) {
            throw new MyException("获取商品位置错误" + e);
        }
        //刷新薪资
        //ordOrderService.refreshSalaryForUser(shopId, pro.getRecycleAdmin(), userId, pro.getInsertTime());

        //添加账单流水-【商品入库】
        ParamFinShopRecordAdd paramFinShopRecordAdd = new ParamFinShopRecordAdd();
        paramFinShopRecordAdd.setInoutType(EnumFinShopRecordInoutType.OUT.getCode());
        BigDecimal initPrice = pro.getInitPrice();
        initPrice = null == initPrice ? new BigDecimal(0.00) : initPrice;
        paramFinShopRecordAdd.setChangeAmount(initPrice.divide(new BigDecimal(100.00)).toString());
        paramFinShopRecordAdd.setType("商品入库");
        paramFinShopRecordAdd.setHappenTime(DateUtil.format(new Date()));
        String note = "【" + pro.getName() + "】商品入库成本价";
        paramFinShopRecordAdd.setNote(note);
        String imgUrl = pro.getSmallImg();
        finShopRecordService.addFinShopRecord(shopId, getUserId(), paramFinShopRecordAdd, EnumFinShopRecordType.SYSTEM, imgUrl, pro.getBizId());


        //添加商品入库消息
        //添加【店铺操作日志】-【商品入库】/【商品上架】
        List<String> bizIdList = new ArrayList<>();
        bizIdList.add(pro.getBizId());
        ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
        paramAddShpOperateLog.setShopId(shopId);
        paramAddShpOperateLog.setOperateUserId(userId);
        paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.PROD.getName());

        String prodName = pro.getName();
        if ("20".equals(paramProductUpload.getState())) {
            //如果状态为【保存并上架】，发送上架消息
            opPushService.pushReleaseProductMsg(shopId, bizIdList);

            paramAddShpOperateLog.setOperateName("商品上架");
            paramAddShpOperateLog.setOperateContent(prodName);
            paramAddShpOperateLog.setProdId(proId);
            paramAddShpOperateLog.setOrderId(null);
            paramAddShpOperateLog.setRequest(request);
            shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
        } else if ("10".equals(paramProductUpload.getState())) {
            //保存到仓库
            opPushService.pushUploadProductMsg(shopId, bizIdList);

            paramAddShpOperateLog.setOperateName("商品入库");
            paramAddShpOperateLog.setOperateContent(prodName);
            paramAddShpOperateLog.setProdId(proId);
            paramAddShpOperateLog.setOrderId(null);
            paramAddShpOperateLog.setRequest(request);
            shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
        }
        //新增商品添加账单记录
        ParamFundRecordAdd paramFundRecordAddPro = new ParamFundRecordAdd();
        paramFundRecordAddPro.setFkShpShopId(shopId);
        paramFundRecordAddPro.setUserId(getUserId());
        paramFundRecordAddPro.setMoney(paramProductUpload.getInitPrice());
        paramFundRecordAddPro.setInitPrice("0");
        paramFundRecordAddPro.setState(paramFinShopRecordAdd.getInoutType());
        paramFundRecordAddPro.setFundType("10");
        paramFundRecordAddPro.setCount(paramProductUpload.getTotalNum());
        paramFundRecordAddPro.setFinClassifyName("入库记录");
        paramFundRecordAddPro.setAttributeCode(pro.getFkProAttributeCode());
        fundRecordService.addProductFundRecord(paramFundRecordAddPro);


        //商品状态;10:保存到库存;20:立即上架
        String state = paramProductUpload.getState();

        state = "10".equals(state) ? "保存到仓库" : "立即上架";
        String beforeValue = "成本价:" + formatPrice(paramProductUpload.getInitPrice()) + ",友商价:" + formatPrice(paramProductUpload.getTradePrice()) + ",代理价:" + formatPrice(paramProductUpload.getAgencyPrice()) + ",销售价:" + formatPrice(paramProductUpload.getSalePrice()) + ",商品属性:" + servicesUtil.getAttributeCn(paramProductUpload.getAttribute(), true);
        addProModifyRecord(proId, "入库", state, prodName, beforeValue, paramProductUpload.getPlatform());
        addProModifyRecord(proId, "入库", "入库数量", "0", paramProductUpload.getTotalNum(), paramProductUpload.getPlatform());
        return BaseResult.okResult(proId);
    }

    /**
     * 根据业务id;更新商品;
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "根据业务id;更新商品;--2.6.2",
            notes = "根据业务id;更新商品;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "bizId", value = "商品的业务id")
    })
    @RequestMapping(value = "/updateProduct")
    @RequiresPermissions("pro:updateProInfo")
    public BaseResult updateProduct(@RequestParam Map<String, String> params,
                                    @Valid ParamProductUpload paramProductUpload, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        checkReleaseParam(paramProductUpload);
        int shopId = getShopId();
        int userId = getUserId();
        //寄卖传送检查商品
        proConveyProductService.checkProduct(paramProductUpload, shopId, userId);
        ProProduct pro = proProductService.pickProProduct(paramProductUpload, false, shopId, userId);
        int state = pro.getFkProStateCode();
        if (state >= EnumProState.SALE_40.getCode() || state < EnumProState.STAND_BY_10.getCode()) {
            return BaseResult.defaultErrorWithMsg("【未上架】和【已上架】状态的商品才能修改！");
        }
        String bizId = pro.getBizId();
        //修改商品之前,先查询的旧数据,为了添加操作记录而做对比;
        ProProduct oldPro = getProProductByShopIdBizId(shopId, bizId);
        Integer totalNum1 = oldPro.getTotalNum();
        Integer totalNum2 = Integer.parseInt(paramProductUpload.getTotalNum());
        ProDetail oldDetail = proDetailService.getProDetailByProId(oldPro.getId());

        //比较新老【商品成本价】大小，0表示相等，1表示修改后的价格大，-1表示修改后的价格小
        BigDecimal finishPriceOld = oldPro.getInitPrice();
        BigDecimal finishPriceNew = pro.getInitPrice();
        int compareResult = 0;
        BigDecimal changePrice = null;
        if (null != finishPriceNew) {
            changePrice = finishPriceNew.subtract(finishPriceOld);
            compareResult = finishPriceNew.compareTo(finishPriceOld);
        }

        //更改商品成本价需要权限
        boolean updateInitPrice = servicesUtil.hasPermission(shopId, getUserId(), "pro:updateInitPrice");
        //更改了商品成本价
        BigDecimal initPrice = pro.getInitPrice();
        if (!LocalUtils.isEmptyAndNull(initPrice) && oldPro.getInitPrice().intValue() != initPrice.intValue()) {
            if (!updateInitPrice) {
                return BaseResult.defaultErrorWithMsg("无权限【修改成本价】!");
            }
        }
        Boolean flag = false;
        try {
            //判断版本控制是否在2.5.2或以上版本
            BasicParam basicParam = getBasicParam();
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.5.2") >= 0) {
                if (oldPro.getTotalNum().intValue() > pro.getTotalNum().intValue()) {
                    flag = true;
                }
            }
        } catch (Exception e) {
            throw new MyException("临时仓商品详情错误" + e);
        }
        if (flag) {
            //判断该商品数量是否大于临时仓数量
            proTempProductService.selectProductNum(oldPro.getId(), pro.getTotalNum());
        }
        ProDetail proDetail = proProductService.pickProDetail(paramProductUpload, false);
        int rows = proProductService.updateProProduct(pro, proDetail, paramProductUpload.getProductClassifyAddLists());
        //更改了库存, 清空锁单记录.2.5.2新增逻辑 修改库存临时仓的不修改
        if (oldPro.getTotalNum().intValue() != pro.getTotalNum().intValue()) {
            proProductService.clearProLockNumByBizId(shopId, bizId);
            proProductService.getProRedisNum(shopId, bizId);
        }
        try {
            //判断版本控制是否在2.6.6或以上版本
            BasicParam basicParam = getBasicParam();
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.6.6") >= 0) {

                if (paramProductUpload.getAttribute().equals("30") && !oldPro.getFkProAttributeCode().equals("30")) {
                    proDynamicProductService.deleteDynamicProductByProId(rows, shopId);
                } else {
                    ProDynamicProduct proDynamicProduct = proDynamicProductService.getDynamicProductInfoByProId(oldPro.getId(), shopId);
                    if (StringUtil.isNotBlank(paramProductUpload.getDynamicId())) {
                        if (totalNum1.equals(totalNum2)) {
                            if (proDynamicProduct != null) {
                                proDynamicProduct.setFkProDynamicId(Integer.parseInt(paramProductUpload.getDynamicId()));
                                proDynamicProductService.update(proDynamicProduct);
                            } else {
                                ParamDynamicProductAdd paramDynamicProductAdd = new ParamDynamicProductAdd();
                                paramDynamicProductAdd.setProId(oldPro.getId() + "");
                                paramDynamicProductAdd.setDynamicId(paramProductUpload.getDynamicId());
                                paramDynamicProductAdd.setShopId(shopId);
                                paramDynamicProductAdd.setUserId(userId);
                                proDynamicProductService.saveDynamicProduct(paramDynamicProductAdd);
                            }
                        } else {
                            if (proDynamicProduct != null) {
                                proDynamicProduct.setState(EnumDynamicProductState.NORMAL.getCode());
                                proDynamicProduct.setFkProDynamicId(Integer.parseInt(paramProductUpload.getDynamicId()));
                                proDynamicProductService.update(proDynamicProduct);
                            } else {
                                ParamDynamicProductAdd paramDynamicProductAdd = new ParamDynamicProductAdd();
                                paramDynamicProductAdd.setProId(oldPro.getId() + "");
                                paramDynamicProductAdd.setDynamicId(paramProductUpload.getDynamicId());
                                paramDynamicProductAdd.setShopId(shopId);
                                paramDynamicProductAdd.setUserId(userId);
                                proDynamicProductService.saveDynamicProduct(paramDynamicProductAdd);
                            }
                        }
                    } else {
                        if (proDynamicProduct != null) {
                            proDynamicProductService.deleteDynamicProductByProId(oldPro.getId(), shopId);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new MyException("商品修改动态错误" + e);
        }

        //添加账单流水-【价差调整】-【修改商品成本价】
        //等于0表示价格没有修改，不生成记账流水
        if (compareResult != 0) {
            ParamFinShopRecordAdd paramFinShopRecordAdd = new ParamFinShopRecordAdd();
            //修改后的价格大，说明收入增加了
            if (compareResult == 1) {
                paramFinShopRecordAdd.setInoutType(EnumFinShopRecordInoutType.IN.getCode());
            }
            //修改后的价格小
            else if (compareResult == -1) {
                paramFinShopRecordAdd.setInoutType(EnumFinShopRecordInoutType.OUT.getCode());
            }

            if (null != changePrice) {
                paramFinShopRecordAdd.setChangeAmount(changePrice.divide(new BigDecimal(100.00)).toString());
            }
            paramFinShopRecordAdd.setType("价差调整");
            paramFinShopRecordAdd.setHappenTime(DateUtil.format(new Date()));
            String note = "【" + pro.getName() + "】商品成本价修改";
            paramFinShopRecordAdd.setNote(note);
            String imgUrl = pro.getSmallImg();
            finShopRecordService.addFinShopRecord(shopId, getUserId(), paramFinShopRecordAdd, EnumFinShopRecordType.SYSTEM, imgUrl, oldPro.getBizId());


        }
        Integer total = oldPro.getTotalNum() - pro.getTotalNum();
        //查询商品品类是否更改
        //从另外三种类型改为寄卖商品的商品
        Boolean flag1 = !oldPro.getFkProAttributeCode().equals(paramProductUpload.getAttribute()) && !"20".equals(oldPro.getFkProAttributeCode()) && "20".equals(paramProductUpload.getAttribute());
        //从寄卖商品改为另外三种类型的商品
        Boolean flag2 = !oldPro.getFkProAttributeCode().equals(paramProductUpload.getAttribute()) && "20".equals(oldPro.getFkProAttributeCode()) && !"20".equals(paramProductUpload.getAttribute());
        if (compareResult != 0 || total != 0 || flag1 || flag2) {
            //新增商品修改账单记录
            ParamFundRecordAdd paramFundRecordAddPro = new ParamFundRecordAdd();
            paramFundRecordAddPro.setFkShpShopId(shopId);
            paramFundRecordAddPro.setUserId(getUserId());
            paramFundRecordAddPro.setMoney(oldPro.getInitPrice().toString());

            if (initPrice == null) {
                initPrice = new BigDecimal(0);
            }
            paramFundRecordAddPro.setInitPrice(initPrice.toString());

//            paramFundRecordAddPro.setState(paramFinShopRecordAdd.getInoutType());
            paramFundRecordAddPro.setFundType("10");
            paramFundRecordAddPro.setCount(oldPro.getTotalNum().toString());
            paramFundRecordAddPro.setFinClassifyName("入库记录");
            paramFundRecordAddPro.setAttributeCode(pro.getFkProAttributeCode());
            Integer totalNum = pro.getTotalNum();
            if (pro.getTotalNum() == null) {
                totalNum = oldPro.getTotalNum();
            }
            if (flag2) {
                //新增商品成本
                paramFundRecordAddPro.setMoney(initPrice.toString());
                paramFundRecordAddPro.setCount(pro.getTotalNum().toString());
                paramFundRecordAddPro.setState(EnumFinShopRecordInoutType.OUT.getCode());
                fundRecordService.addProductFundRecord(paramFundRecordAddPro);
            } else if (flag1) {
                //移除商品成本
                paramFundRecordAddPro.setMoney(oldPro.getInitPrice().toString());
                paramFundRecordAddPro.setCount(oldPro.getTotalNum().toString());
                paramFundRecordAddPro.setState(EnumFinShopRecordInoutType.IN.getCode());
                paramFundRecordAddPro.setAttributeCode("remove");
                fundRecordService.addProductFundRecord(paramFundRecordAddPro);
            } else {
                fundRecordService.addFundRecordForUpdateProduct(paramFundRecordAddPro, totalNum.toString());
            }

        }

        //刷新薪资
        //修改后的回收人员
        // ordOrderService.refreshSalaryForUser(shopId, pro.getRecycleAdmin(), userId, pro.getInsertTime());
        //修改前的回收人员
        //ordOrderService.refreshSalaryForUser(shopId, oldPro.getRecycleAdmin(), userId, pro.getInsertTime());

        //发送修改商品价格消息
        opPushService.pushUpdateProdPriceMsg(shopId, paramProductUpload, oldPro);
        //发送商品成本价格变动消息 只有原先是自有商品 修改后也是自有商品才进行通知
        if ("10".equals(oldPro.getFkProAttributeCode()) && "10".equals(paramProductUpload.getAttribute())) {
            opPushService.pushUpdateInitPriceMsg(shopId, paramProductUpload, oldPro);
        }
        //商品状态从下架变成了上架，发送上架消息
        if ("20".equals(paramProductUpload.getState()) && LocalUtils.isBetween(oldPro.getFkProStateCode(), 10, 19)) {
            List<String> bizIdList = new ArrayList<>();
            bizIdList.add(pro.getBizId());
            opPushService.pushReleaseProductMsg(shopId, bizIdList);
        }

        //添加商品操作记录
        String source = paramProductUpload.getPlatform();
        equalsMoreProduct(oldPro, oldDetail, paramProductUpload, source);

        String state1 = paramProductUpload.getState();
        if ("11".equals(state1)) {
            addProModifyRecord(oldPro.getId(), "下架", "保存并下架", oldPro.getName(), oldPro.getName(), source);
        } else if ("20".equals(state1)) {
            addProModifyRecord(oldPro.getId(), "上架", "保存并上架", oldPro.getName(), oldPro.getName(), source);
        }

        //添加【店铺操作日志】-【修改商品】
        return saveUpdateProdOperateLog(paramProductUpload, request, shopId, userId, pro, oldPro, rows);
    }


    /**
     * 寄卖卖商品添加取回接口2
     *
     * @return Result
     */
    @ApiOperation(
            value = "寄卖商品添加取回接口2.5.5;",
            notes = "寄卖商品添加取回接口2.5.5;",
            httpMethod = "POST")
    @RequestMapping("/recycleProductAdd")
    public BaseResult recycleProductAdd(@Valid ParamRecycleProductAdd recycleProductAdd, BindingResult result) {
        servicesUtil.validControllerParam(result);
        recycleProductAdd.setUserId(getUserId());
        recycleProductAdd.setShopId(getShopId());
        proProductService.recycleProductAdd(recycleProductAdd, "0");
        ProProduct oldPro = getProProductByShopIdBizId(getShopId(), recycleProductAdd.getBizId());
        addProModifyRecord(oldPro.getId(), "取回", "寄卖商品取回", null, recycleProductAdd.getRetrieveRemark(), recycleProductAdd.getPlatform());
        //寄卖卖商品添加取回
        opPushService.recycleProductMsg(getShopId(), oldPro.getName(), recycleProductAdd.getBizId());
        return BaseResult.okResult();
    }

    /**
     * 寄卖卖商品添加取回接口2
     *
     * @return Result
     */
    @ApiOperation(
            value = "寄卖商品--编辑取回原因2.5.5;",
            notes = "寄卖商品--编辑取回原因2.5.5;",
            httpMethod = "POST")
    @RequestMapping("/recycleProductUpdate")
    public BaseResult recycleProductUpdate(@Valid ParamRecycleProductAdd recycleProductAdd, BindingResult result) {
        servicesUtil.validControllerParam(result);
        ProProduct oldPro = getProProductByShopIdBizId(getShopId(), recycleProductAdd.getBizId());
        recycleProductAdd.setUserId(getUserId());
        recycleProductAdd.setShopId(getShopId());
        proProductService.recycleProductAdd(recycleProductAdd, "1");

        addProModifyRecord(oldPro.getId(), "取回", "编辑取回原因", oldPro.getRetrieveRemark(), recycleProductAdd.getRetrieveRemark(), recycleProductAdd.getPlatform());
        return BaseResult.okResult();
    }

    /**
     * 根据业务id;更新商品;
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "根据业务id;更新商品价格/数量2.5.2;",
            notes = "根据业务id;更新商品价格/数量2.5.2;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "bizId", value = "商品的业务id"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token登录标识符"),
    })
    @RequestMapping(value = "/updateProductPrice")
    @RequiresPermissions("pro:updateProInfo")
    public BaseResult updateProProductPrice(@Valid ParamProductUploadPrice productUploadPrice, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        int shopId = getShopId();
        int userId = getUserId();
        ProProduct oldPro = proProductService.getProProductByShopIdBizId(shopId, productUploadPrice.getBizId());
        if (oldPro == null) {
            return BaseResult.defaultErrorWithMsg("暂无该商品");
        }
        int state = oldPro.getFkProStateCode();
        if (state >= EnumProState.SALE_40.getCode() || state < EnumProState.STAND_BY_10.getCode()) {
            return BaseResult.defaultErrorWithMsg("【未上架】和【已上架】状态的商品才能修改！");
        }
        String bizId = oldPro.getBizId();
        //更改商品成本价需要权限
        boolean updateInitPrice = servicesUtil.hasPermission(shopId, getUserId(), "pro:updateInitPrice");
        //更改了商品成本价
        BigDecimal initPrice = new BigDecimal(productUploadPrice.getInitPrice());
        if (!LocalUtils.isEmptyAndNull(initPrice) && oldPro.getInitPrice().intValue() != initPrice.intValue()) {
            if (!updateInitPrice) {
                return BaseResult.defaultErrorWithMsg("无权限【修改成本价】!");
            }
        }
        Boolean flag = false;
        try {
            //判断版本控制是否在2.5.2或以上版本
            BasicParam basicParam = getBasicParam();
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.5.2") >= 0) {
                if (!LocalUtils.isEmptyAndNull(productUploadPrice.getTotalNum()) && oldPro.getTotalNum() > Integer.parseInt(productUploadPrice.getTotalNum())) {
                    flag = true;
                }
            }
        } catch (Exception e) {
            throw new MyException("临时仓商品详情错误" + e);
        }
        if (flag) {
            //判断该商品数量是否大于临时仓数量
            proTempProductService.selectProductNum(oldPro.getId(), Integer.parseInt(productUploadPrice.getTotalNum()));
        }
        int rows = proProductService.updateProProductPrice(productUploadPrice, getShopId(), getUserId());
        // //更改了库存, 清空锁单记录.2.5.2新增逻辑 修改库存临时仓的不修改
        if (Integer.parseInt(productUploadPrice.getTotalNum()) != oldPro.getTotalNum().intValue()) {
            proProductService.clearProLockNumByBizId(shopId, bizId);
            proProductService.getProRedisNum(shopId, bizId);
        }
        ParamProductUpload paramProductUpload = new ParamProductUpload();
        BeanUtils.copyProperties(productUploadPrice, paramProductUpload);
        //发送修改商品价格消息
        opPushService.pushUpdateProdPriceMsg(shopId, paramProductUpload, oldPro);
        //发送商品成本价格变动消息 只有原先是自有商品 修改后也是自有商品才进行通知
        if (EnumProAttribute.OWN.getCode().equals(oldPro.getFkProAttributeCode())) {
            opPushService.pushUpdateInitPriceMsg(shopId, paramProductUpload, oldPro);
        }
        //数量和4个价格的操作记录
        equalsPriceAndNum(oldPro, paramProductUpload, productUploadPrice.getPlatform(), productUploadPrice.getChangeInitPriceRemark());


        //添加【店铺操作日志】-【修改商品】
        return saveUpdateProdOperateLog(paramProductUpload, request, shopId, userId, oldPro, oldPro, rows);
    }

    /**
     * 保存【修改商品】操作日志
     *
     * @param paramProductUpload
     * @param request
     * @param shopId
     * @param userId
     * @param pro
     * @param oldPro
     * @param rows
     * @return
     */
    private BaseResult saveUpdateProdOperateLog(@Valid ParamProductUpload paramProductUpload, HttpServletRequest request, int shopId, int userId, ProProduct pro, ProProduct oldPro, int rows) {
        Integer recycleAdminUserId = LocalUtils.isEmptyAndNull(pro.getRecycleAdmin()) ? 0 : pro.getRecycleAdmin();
        String recycleAdminUserName = shpUserShopRefService.getNameFromShop(shopId, recycleAdminUserId);
        Integer updateFieldCount = 0;
        String operateContent = pro.getName() + "(";
        //回收人员
        if (!oldPro.getRecycleAdmin().equals(recycleAdminUserId)) {
            updateFieldCount++;
            operateContent += "修改回收人员为【" + recycleAdminUserName + "】";
        }
        //成本价
        BigDecimal initPriceOld = oldPro.getInitPrice();
        if (paramProductUpload.getInitPrice() != null) {
            BigDecimal initPriceNew = new BigDecimal(paramProductUpload.getInitPrice());
            if (initPriceOld.compareTo(initPriceNew) != 0) {
                updateFieldCount++;
                try {
                    String oldInitPrice = LocalUtils.calcNumber(oldPro.getInitPrice(), "/", 100, 0).toString();
                    operateContent += "把旧成本价【" + oldInitPrice + "】修改为【" + initPriceNew.divide(new BigDecimal(100.00)).toString() + "】";
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

        //同行价
        BigDecimal tradePriceOld = oldPro.getTradePrice();
        if (paramProductUpload.getTradePrice() != null) {
            BigDecimal tradePriceNew = new BigDecimal(paramProductUpload.getTradePrice());
            if (tradePriceOld.compareTo(tradePriceNew) != 0) {
                updateFieldCount++;
                if (updateFieldCount != 1) {
                    operateContent += ",";
                }
                operateContent += "修改同行价为【" + tradePriceNew.divide(new BigDecimal(100.00)).toString() + "】";
            }
        }

        //代理价
        BigDecimal agencyPriceOld = oldPro.getAgencyPrice();
        if (paramProductUpload.getAgencyPrice() != null) {
            BigDecimal agencyPriceNew = new BigDecimal(paramProductUpload.getAgencyPrice());
            if (agencyPriceOld.compareTo(agencyPriceNew) != 0) {
                updateFieldCount++;
                if (updateFieldCount != 1) {
                    operateContent += ",";
                }
                operateContent += "修改代理价为【" + agencyPriceNew.divide(new BigDecimal(100.00)).toString() + "】";
            }
        }

        //销售价
        BigDecimal salePriceOld = oldPro.getSalePrice();
        if (paramProductUpload.getSalePrice() != null) {
            BigDecimal salePriceNew = new BigDecimal(paramProductUpload.getSalePrice());
            if (salePriceOld.compareTo(salePriceNew) != 0) {
                updateFieldCount++;
                if (updateFieldCount != 1) {
                    operateContent += ",";
                }
                operateContent += "修改销售价为【" + salePriceNew.divide(new BigDecimal(100.00)).toString() + "】";
            }
        }

        //库存
        Integer totalNumOld = oldPro.getTotalNum();
        Integer totalNumNew = Integer.parseInt(paramProductUpload.getTotalNum());
        if (!totalNumOld.equals(totalNumNew)) {
            updateFieldCount++;
            if (updateFieldCount != 1) {
                operateContent += ",";
            }
            operateContent += "修改库存为【" + paramProductUpload.getTotalNum() + "】";
        }

        //商品名称
        String name = oldPro.getName();
        if (paramProductUpload.getName() != null) {
            String nameNew = paramProductUpload.getName();
            if (!name.equals(nameNew)) {
                updateFieldCount++;
                if (updateFieldCount != 1) {
                    operateContent += ",";
                }
                operateContent += "原商品名称" + name + "，修改商品名称为【" + nameNew + "】";
            }
        }

        //商品描述
        String description = oldPro.getDescription();
        if (paramProductUpload.getDescription() != null) {
            String descriptionNew = paramProductUpload.getDescription();
            if (!description.equals(descriptionNew)) {
                updateFieldCount++;
                if (updateFieldCount != 1) {
                    operateContent += ",";
                }
                operateContent += "原商品描述" + description + "，修改商品描述为【" + descriptionNew + "】";
            }
        }

        //适用人群
        String targetUser = oldPro.getTargetUser();
        if (paramProductUpload.getTargetUser() != null) {
            String targetUserNew = paramProductUpload.getTargetUser();
            if (!targetUser.equals(targetUserNew)) {
                updateFieldCount++;
                if (updateFieldCount != 1) {
                    operateContent += ",";
                }
                operateContent += "原商品适用人群" + targetUser + "，修改商品适用人群为【" + targetUserNew + "】";
            }
        }

        operateContent += ")";

        ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
        paramAddShpOperateLog.setShopId(shopId);
        paramAddShpOperateLog.setOperateUserId(userId);
        paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.PROD.getName());
        paramAddShpOperateLog.setOperateName("修改商品");
        paramAddShpOperateLog.setOperateContent(operateContent);
        paramAddShpOperateLog.setProdId(oldPro.getId());
        paramAddShpOperateLog.setOrderId(null);
        paramAddShpOperateLog.setRequest(request);

        //有更新字段，才添加日志
        if (updateFieldCount > 0) {
            shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
        }
        return BaseResult.okResult(rows);
    }

    /**
     * 赎回质押商品
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "赎回质押商品",
            notes = "1.赎回质押商品;<br/>",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token")
    })
    @RequestMapping(value = "/redeemProduct")
    @RequiresPermissions(value = {"pro:redeemProduct", "ord:confirmOrd"}, logical = Logical.OR)
    public BaseResult redeemProduct(@RequestParam Map<String, String> params,
                                    @Valid ParamProductRedeem paramProductRedeem, BindingResult result) {
        int shopId = getShopId();
        int userId = getUserId();
        servicesUtil.validControllerParam(result);

        ProProduct proRedeem = proProductService.getProProductByShopIdBizId(shopId, paramProductRedeem.getProdId());
        Integer proState = proRedeem.getFkProStateCode();
        if (EnumProState.SALE_44.getCode().equals(proState)) {
            throw new MyException("已赎回商品，不可重复赎回");
        }
        proRedeem.setFinishPrice(new BigDecimal(paramProductRedeem.getRedeemPrice()).multiply(new BigDecimal(100.00)));
        int proId = proProductService.redeemProduct(proRedeem, userId);

        //添加账单流水-【质押商品赎回】
        ParamFinShopRecordAdd paramFinShopRecordAdd = new ParamFinShopRecordAdd();
        paramFinShopRecordAdd.setInoutType(EnumFinShopRecordInoutType.OUT.getCode());
        paramFinShopRecordAdd.setChangeAmount(paramProductRedeem.getRedeemPrice());
        paramFinShopRecordAdd.setType("商品赎回");
        paramFinShopRecordAdd.setHappenTime(DateUtil.format(new Date()));
        String note = "【" + proRedeem.getName() + "】质押商品赎回价";
        paramFinShopRecordAdd.setNote(note);
        String imgUrl = proRedeem.getSmallImg();
        finShopRecordService.addFinShopRecord(shopId, getUserId(), paramFinShopRecordAdd, EnumFinShopRecordType.SYSTEM, imgUrl, proRedeem.getBizId());
        //质押商品赎回添加账单记录
        ParamFundRecordAdd paramFundRecordAddPro = new ParamFundRecordAdd();
        paramFundRecordAddPro.setFkShpShopId(shopId);
        paramFundRecordAddPro.setUserId(getUserId());
        paramFundRecordAddPro.setMoney(paramProductRedeem.getRedeemPrice());
        paramFundRecordAddPro.setInitPrice(proRedeem.getInitPrice().toString());
        paramFundRecordAddPro.setState(EnumFinShopRecordInoutType.IN.getCode());
        paramFundRecordAddPro.setFundType("60");
        paramFundRecordAddPro.setCount("1");
        paramFundRecordAddPro.setFinClassifyName("质押商品账单记录");
        paramFundRecordAddPro.setAttributeCode("30");
        fundRecordService.addProductFundRecord(paramFundRecordAddPro);

        //添加商品操作记录
        addProModifyRecord(proRedeem.getId(), "赎回", proRedeem.getName(), proRedeem.getName(), paramProductRedeem.getPlatform());

        //添加通知消息
        List<String> proBizIdList = new ArrayList<>();
        proBizIdList.add(paramProductRedeem.getProdId());
        opPushService.pushRedeemProductMsg(shopId, proBizIdList);
        return BaseResult.okResult(proId);
    }

    private void checkReleaseParam(ParamProductUpload voPro) {
        int state = Integer.parseInt(voPro.getState());
        String description = voPro.getDescription();
        String attribute = voPro.getAttribute();
        String classify = voPro.getClassify();
        String repairCard = voPro.getRepairCard();
        //如果商品立即上架,则商品描述,名称,适用人群,分类,保卡都为必填项;
        if (20 == state) {
            String note = "上架时,请填写";
            if (LocalUtils.isEmptyAndNull(description)) {
                throw new MyException(note + "【商品描述】");
            }
            if (LocalUtils.isEmptyAndNull(attribute)) {
                throw new MyException(note + "【商品属性】");
            }
            if (LocalUtils.isEmptyAndNull(classify)) {
                throw new MyException(note + "【商品分类】");
            }
            //2020-09-04 sanjin145 取消【商品上架】保卡字段校验
//            if (LocalUtils.isEmptyAndNull(repairCard)) {
//                throw new MyException(note + "【商品保卡】");
//            }
        }
        //独立编码是否为空
        if (LocalUtils.isEmptyAndNull(voPro.getUniqueCode())) {
            voPro.setUniqueCode("");
        }
        //品牌是否为空
        if (LocalUtils.isEmptyAndNull(voPro.getClassifySub())) {
            voPro.setClassifySub("");
        }
        //系列是否为空
        if (LocalUtils.isEmptyAndNull(voPro.getSubSeriesName())) {
            voPro.setSubSeriesName("");
        }
        //型号是否为空
        if (LocalUtils.isEmptyAndNull(voPro.getSeriesModelName())) {
            voPro.setSeriesModelName("");
        }
        //处理保卡时间中的年
        String repairCardTime = voPro.getRepairCardTime();
        try {
            BasicParam basicParam = getBasicParam();
            String platform = basicParam.getPlatform();
            String appVersion = basicParam.getAppVersion();

            //判断版本号，低于指定版本号，则处理
            Boolean isOldVersion = Boolean.FALSE;
            if (StringUtil.isNotBlank(appVersion)) {
                if ("android".equals(platform)) {
                    isOldVersion = VersionUtils.compareVersion(appVersion, "2.1.0") < 0;
                } else if ("ios".equals(platform)) {
                    isOldVersion = VersionUtils.compareVersion(appVersion, "2.2.1") < 0;
                }
            }
            if (isOldVersion) {
                repairCardTime = StringUtil.remove(repairCardTime, "年");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        voPro.setRepairCardTime(repairCardTime);
    }


    public static void main(String[] args) throws Exception {
        System.out.println(LocalUtils.formatPrice(LocalUtils.calcNumber(10000.5, "*", 0.01)));
    }
}
