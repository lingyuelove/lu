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
@Api(tags = {"B002.??????????????????2.5.5--zs"}, description = "/shop/user/pro |??????????????????????????????")
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
     * ???????????????????????????
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "???????????????????????????",
            notes = "???????????????????????????;????????????;????????????;????????????;",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "????????????;????????????"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "??????token"),
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
        //????????????
        hashMap.put("attributeList", voProAttributeList);
        //????????????
        hashMap.put("classifyList", voProClassifyList);
        //????????????
        hashMap.put("proSourceList", voProSourceList);
        ////????????????
        //hashMap.put("proQualityList", voProQualityList);
        //????????????
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
        //??????????????????
        String uPermDynamic = ConstantPermission.DYNAMIC_LIST;
        hashMap.put("uPermDynamic", hasPermToPageWithCurrentUser(uPermDynamic));
        //????????????????????????
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
        //?????????????????? ????????????????????????????????????
//        hashMap.put("publicUrl", "https://www.luxuryadmin.com/h5/price-query.html?selectPublic=1");
        hashMap.put("publicUrl", publicProductUtil.getPublicUrl());
        return BaseResult.okResult(hashMap);
    }


    @ApiOperation(value = "??????????????????---2.5.2---mong",
            notes = "??????????????????????????????????????????????????????",
            httpMethod = "POST"
    )

    @RequestMapping("listProClassifySub")
    public BaseResult listProClassifySub(@Valid ParamProClassifySubQuery paramProClassifySubQuery, BindingResult result) {
        servicesUtil.validControllerParam(result);
        paramProClassifySubQuery.setShopId(getShopId());
        //???????????????????????????????????????????????????????????????????????????

        List<VoProClassifySub> classifySubs = proClassifySubService.listAllProClassifySub(paramProClassifySubQuery);
        return BaseResult.okResult(classifySubs);
    }


    /**
     * ????????????
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "????????????--2.6.2",
            notes = "1.????????????,??????,??????;<br/>" +
                    "2.????????????,????????????,????????????,??????,??????(????????????);<br/>" +
                    "??????: ???????????????,?????????????????????;<br/>" +
                    "??????????????????,?????????,??????????????????;<br/>" +
                    "?????????????????????????????????,??????????????????;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "????????????;????????????")
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
                    //2.6.6????????????????????????----?????????????????????
                    ParamDynamicProductAdd dynamicProduct = new ParamDynamicProductAdd();
                    dynamicProduct.setUserId(userId);
                    dynamicProduct.setShopId(shopId);
                    dynamicProduct.setDynamicId(paramProductUpload.getDynamicId());
                    dynamicProduct.setProId(proId + "");
                    proDynamicProductService.saveDynamicProduct(dynamicProduct);
                }
            }
        } catch (Exception e) {
            throw new MyException("????????????????????????" + e);
        }
        //????????????
        //ordOrderService.refreshSalaryForUser(shopId, pro.getRecycleAdmin(), userId, pro.getInsertTime());

        //??????????????????-??????????????????
        ParamFinShopRecordAdd paramFinShopRecordAdd = new ParamFinShopRecordAdd();
        paramFinShopRecordAdd.setInoutType(EnumFinShopRecordInoutType.OUT.getCode());
        BigDecimal initPrice = pro.getInitPrice();
        initPrice = null == initPrice ? new BigDecimal(0.00) : initPrice;
        paramFinShopRecordAdd.setChangeAmount(initPrice.divide(new BigDecimal(100.00)).toString());
        paramFinShopRecordAdd.setType("????????????");
        paramFinShopRecordAdd.setHappenTime(DateUtil.format(new Date()));
        String note = "???" + pro.getName() + "????????????????????????";
        paramFinShopRecordAdd.setNote(note);
        String imgUrl = pro.getSmallImg();
        finShopRecordService.addFinShopRecord(shopId, getUserId(), paramFinShopRecordAdd, EnumFinShopRecordType.SYSTEM, imgUrl, pro.getBizId());


        //????????????????????????
        //??????????????????????????????-??????????????????/??????????????????
        List<String> bizIdList = new ArrayList<>();
        bizIdList.add(pro.getBizId());
        ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
        paramAddShpOperateLog.setShopId(shopId);
        paramAddShpOperateLog.setOperateUserId(userId);
        paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.PROD.getName());

        String prodName = pro.getName();
        if ("20".equals(paramProductUpload.getState())) {
            //?????????????????????????????????????????????????????????
            opPushService.pushReleaseProductMsg(shopId, bizIdList);

            paramAddShpOperateLog.setOperateName("????????????");
            paramAddShpOperateLog.setOperateContent(prodName);
            paramAddShpOperateLog.setProdId(proId);
            paramAddShpOperateLog.setOrderId(null);
            paramAddShpOperateLog.setRequest(request);
            shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
        } else if ("10".equals(paramProductUpload.getState())) {
            //???????????????
            opPushService.pushUploadProductMsg(shopId, bizIdList);

            paramAddShpOperateLog.setOperateName("????????????");
            paramAddShpOperateLog.setOperateContent(prodName);
            paramAddShpOperateLog.setProdId(proId);
            paramAddShpOperateLog.setOrderId(null);
            paramAddShpOperateLog.setRequest(request);
            shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
        }
        //??????????????????????????????
        ParamFundRecordAdd paramFundRecordAddPro = new ParamFundRecordAdd();
        paramFundRecordAddPro.setFkShpShopId(shopId);
        paramFundRecordAddPro.setUserId(getUserId());
        paramFundRecordAddPro.setMoney(paramProductUpload.getInitPrice());
        paramFundRecordAddPro.setInitPrice("0");
        paramFundRecordAddPro.setState(paramFinShopRecordAdd.getInoutType());
        paramFundRecordAddPro.setFundType("10");
        paramFundRecordAddPro.setCount(paramProductUpload.getTotalNum());
        paramFundRecordAddPro.setFinClassifyName("????????????");
        paramFundRecordAddPro.setAttributeCode(pro.getFkProAttributeCode());
        fundRecordService.addProductFundRecord(paramFundRecordAddPro);


        //????????????;10:???????????????;20:????????????
        String state = paramProductUpload.getState();

        state = "10".equals(state) ? "???????????????" : "????????????";
        String beforeValue = "?????????:" + formatPrice(paramProductUpload.getInitPrice()) + ",?????????:" + formatPrice(paramProductUpload.getTradePrice()) + ",?????????:" + formatPrice(paramProductUpload.getAgencyPrice()) + ",?????????:" + formatPrice(paramProductUpload.getSalePrice()) + ",????????????:" + servicesUtil.getAttributeCn(paramProductUpload.getAttribute(), true);
        addProModifyRecord(proId, "??????", state, prodName, beforeValue, paramProductUpload.getPlatform());
        addProModifyRecord(proId, "??????", "????????????", "0", paramProductUpload.getTotalNum(), paramProductUpload.getPlatform());
        return BaseResult.okResult(proId);
    }

    /**
     * ????????????id;????????????;
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "????????????id;????????????;--2.6.2",
            notes = "????????????id;????????????;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "????????????;????????????"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "bizId", value = "???????????????id")
    })
    @RequestMapping(value = "/updateProduct")
    @RequiresPermissions("pro:updateProInfo")
    public BaseResult updateProduct(@RequestParam Map<String, String> params,
                                    @Valid ParamProductUpload paramProductUpload, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        checkReleaseParam(paramProductUpload);
        int shopId = getShopId();
        int userId = getUserId();
        //????????????????????????
        proConveyProductService.checkProduct(paramProductUpload, shopId, userId);
        ProProduct pro = proProductService.pickProProduct(paramProductUpload, false, shopId, userId);
        int state = pro.getFkProStateCode();
        if (state >= EnumProState.SALE_40.getCode() || state < EnumProState.STAND_BY_10.getCode()) {
            return BaseResult.defaultErrorWithMsg("???????????????????????????????????????????????????????????????");
        }
        String bizId = pro.getBizId();
        //??????????????????,?????????????????????,????????????????????????????????????;
        ProProduct oldPro = getProProductByShopIdBizId(shopId, bizId);
        Integer totalNum1 = oldPro.getTotalNum();
        Integer totalNum2 = Integer.parseInt(paramProductUpload.getTotalNum());
        ProDetail oldDetail = proDetailService.getProDetailByProId(oldPro.getId());

        //??????????????????????????????????????????0???????????????1??????????????????????????????-1???????????????????????????
        BigDecimal finishPriceOld = oldPro.getInitPrice();
        BigDecimal finishPriceNew = pro.getInitPrice();
        int compareResult = 0;
        BigDecimal changePrice = null;
        if (null != finishPriceNew) {
            changePrice = finishPriceNew.subtract(finishPriceOld);
            compareResult = finishPriceNew.compareTo(finishPriceOld);
        }

        //?????????????????????????????????
        boolean updateInitPrice = servicesUtil.hasPermission(shopId, getUserId(), "pro:updateInitPrice");
        //????????????????????????
        BigDecimal initPrice = pro.getInitPrice();
        if (!LocalUtils.isEmptyAndNull(initPrice) && oldPro.getInitPrice().intValue() != initPrice.intValue()) {
            if (!updateInitPrice) {
                return BaseResult.defaultErrorWithMsg("??????????????????????????????!");
            }
        }
        Boolean flag = false;
        try {
            //???????????????????????????2.5.2???????????????
            BasicParam basicParam = getBasicParam();
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.5.2") >= 0) {
                if (oldPro.getTotalNum().intValue() > pro.getTotalNum().intValue()) {
                    flag = true;
                }
            }
        } catch (Exception e) {
            throw new MyException("???????????????????????????" + e);
        }
        if (flag) {
            //????????????????????????????????????????????????
            proTempProductService.selectProductNum(oldPro.getId(), pro.getTotalNum());
        }
        ProDetail proDetail = proProductService.pickProDetail(paramProductUpload, false);
        int rows = proProductService.updateProProduct(pro, proDetail, paramProductUpload.getProductClassifyAddLists());
        //???????????????, ??????????????????.2.5.2???????????? ?????????????????????????????????
        if (oldPro.getTotalNum().intValue() != pro.getTotalNum().intValue()) {
            proProductService.clearProLockNumByBizId(shopId, bizId);
            proProductService.getProRedisNum(shopId, bizId);
        }
        try {
            //???????????????????????????2.6.6???????????????
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
            throw new MyException("????????????????????????" + e);
        }

        //??????????????????-??????????????????-???????????????????????????
        //??????0????????????????????????????????????????????????
        if (compareResult != 0) {
            ParamFinShopRecordAdd paramFinShopRecordAdd = new ParamFinShopRecordAdd();
            //?????????????????????????????????????????????
            if (compareResult == 1) {
                paramFinShopRecordAdd.setInoutType(EnumFinShopRecordInoutType.IN.getCode());
            }
            //?????????????????????
            else if (compareResult == -1) {
                paramFinShopRecordAdd.setInoutType(EnumFinShopRecordInoutType.OUT.getCode());
            }

            if (null != changePrice) {
                paramFinShopRecordAdd.setChangeAmount(changePrice.divide(new BigDecimal(100.00)).toString());
            }
            paramFinShopRecordAdd.setType("????????????");
            paramFinShopRecordAdd.setHappenTime(DateUtil.format(new Date()));
            String note = "???" + pro.getName() + "????????????????????????";
            paramFinShopRecordAdd.setNote(note);
            String imgUrl = pro.getSmallImg();
            finShopRecordService.addFinShopRecord(shopId, getUserId(), paramFinShopRecordAdd, EnumFinShopRecordType.SYSTEM, imgUrl, oldPro.getBizId());


        }
        Integer total = oldPro.getTotalNum() - pro.getTotalNum();
        //??????????????????????????????
        //????????????????????????????????????????????????
        Boolean flag1 = !oldPro.getFkProAttributeCode().equals(paramProductUpload.getAttribute()) && !"20".equals(oldPro.getFkProAttributeCode()) && "20".equals(paramProductUpload.getAttribute());
        //????????????????????????????????????????????????
        Boolean flag2 = !oldPro.getFkProAttributeCode().equals(paramProductUpload.getAttribute()) && "20".equals(oldPro.getFkProAttributeCode()) && !"20".equals(paramProductUpload.getAttribute());
        if (compareResult != 0 || total != 0 || flag1 || flag2) {
            //??????????????????????????????
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
            paramFundRecordAddPro.setFinClassifyName("????????????");
            paramFundRecordAddPro.setAttributeCode(pro.getFkProAttributeCode());
            Integer totalNum = pro.getTotalNum();
            if (pro.getTotalNum() == null) {
                totalNum = oldPro.getTotalNum();
            }
            if (flag2) {
                //??????????????????
                paramFundRecordAddPro.setMoney(initPrice.toString());
                paramFundRecordAddPro.setCount(pro.getTotalNum().toString());
                paramFundRecordAddPro.setState(EnumFinShopRecordInoutType.OUT.getCode());
                fundRecordService.addProductFundRecord(paramFundRecordAddPro);
            } else if (flag1) {
                //??????????????????
                paramFundRecordAddPro.setMoney(oldPro.getInitPrice().toString());
                paramFundRecordAddPro.setCount(oldPro.getTotalNum().toString());
                paramFundRecordAddPro.setState(EnumFinShopRecordInoutType.IN.getCode());
                paramFundRecordAddPro.setAttributeCode("remove");
                fundRecordService.addProductFundRecord(paramFundRecordAddPro);
            } else {
                fundRecordService.addFundRecordForUpdateProduct(paramFundRecordAddPro, totalNum.toString());
            }

        }

        //????????????
        //????????????????????????
        // ordOrderService.refreshSalaryForUser(shopId, pro.getRecycleAdmin(), userId, pro.getInsertTime());
        //????????????????????????
        //ordOrderService.refreshSalaryForUser(shopId, oldPro.getRecycleAdmin(), userId, pro.getInsertTime());

        //??????????????????????????????
        opPushService.pushUpdateProdPriceMsg(shopId, paramProductUpload, oldPro);
        //???????????????????????????????????? ??????????????????????????? ??????????????????????????????????????????
        if ("10".equals(oldPro.getFkProAttributeCode()) && "10".equals(paramProductUpload.getAttribute())) {
            opPushService.pushUpdateInitPriceMsg(shopId, paramProductUpload, oldPro);
        }
        //?????????????????????????????????????????????????????????
        if ("20".equals(paramProductUpload.getState()) && LocalUtils.isBetween(oldPro.getFkProStateCode(), 10, 19)) {
            List<String> bizIdList = new ArrayList<>();
            bizIdList.add(pro.getBizId());
            opPushService.pushReleaseProductMsg(shopId, bizIdList);
        }

        //????????????????????????
        String source = paramProductUpload.getPlatform();
        equalsMoreProduct(oldPro, oldDetail, paramProductUpload, source);

        String state1 = paramProductUpload.getState();
        if ("11".equals(state1)) {
            addProModifyRecord(oldPro.getId(), "??????", "???????????????", oldPro.getName(), oldPro.getName(), source);
        } else if ("20".equals(state1)) {
            addProModifyRecord(oldPro.getId(), "??????", "???????????????", oldPro.getName(), oldPro.getName(), source);
        }

        //??????????????????????????????-??????????????????
        return saveUpdateProdOperateLog(paramProductUpload, request, shopId, userId, pro, oldPro, rows);
    }


    /**
     * ?????????????????????????????????2
     *
     * @return Result
     */
    @ApiOperation(
            value = "??????????????????????????????2.5.5;",
            notes = "??????????????????????????????2.5.5;",
            httpMethod = "POST")
    @RequestMapping("/recycleProductAdd")
    public BaseResult recycleProductAdd(@Valid ParamRecycleProductAdd recycleProductAdd, BindingResult result) {
        servicesUtil.validControllerParam(result);
        recycleProductAdd.setUserId(getUserId());
        recycleProductAdd.setShopId(getShopId());
        proProductService.recycleProductAdd(recycleProductAdd, "0");
        ProProduct oldPro = getProProductByShopIdBizId(getShopId(), recycleProductAdd.getBizId());
        addProModifyRecord(oldPro.getId(), "??????", "??????????????????", null, recycleProductAdd.getRetrieveRemark(), recycleProductAdd.getPlatform());
        //???????????????????????????
        opPushService.recycleProductMsg(getShopId(), oldPro.getName(), recycleProductAdd.getBizId());
        return BaseResult.okResult();
    }

    /**
     * ?????????????????????????????????2
     *
     * @return Result
     */
    @ApiOperation(
            value = "????????????--??????????????????2.5.5;",
            notes = "????????????--??????????????????2.5.5;",
            httpMethod = "POST")
    @RequestMapping("/recycleProductUpdate")
    public BaseResult recycleProductUpdate(@Valid ParamRecycleProductAdd recycleProductAdd, BindingResult result) {
        servicesUtil.validControllerParam(result);
        ProProduct oldPro = getProProductByShopIdBizId(getShopId(), recycleProductAdd.getBizId());
        recycleProductAdd.setUserId(getUserId());
        recycleProductAdd.setShopId(getShopId());
        proProductService.recycleProductAdd(recycleProductAdd, "1");

        addProModifyRecord(oldPro.getId(), "??????", "??????????????????", oldPro.getRetrieveRemark(), recycleProductAdd.getRetrieveRemark(), recycleProductAdd.getPlatform());
        return BaseResult.okResult();
    }

    /**
     * ????????????id;????????????;
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "????????????id;??????????????????/??????2.5.2;",
            notes = "????????????id;??????????????????/??????2.5.2;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "bizId", value = "???????????????id"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token???????????????"),
    })
    @RequestMapping(value = "/updateProductPrice")
    @RequiresPermissions("pro:updateProInfo")
    public BaseResult updateProProductPrice(@Valid ParamProductUploadPrice productUploadPrice, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        int shopId = getShopId();
        int userId = getUserId();
        ProProduct oldPro = proProductService.getProProductByShopIdBizId(shopId, productUploadPrice.getBizId());
        if (oldPro == null) {
            return BaseResult.defaultErrorWithMsg("???????????????");
        }
        int state = oldPro.getFkProStateCode();
        if (state >= EnumProState.SALE_40.getCode() || state < EnumProState.STAND_BY_10.getCode()) {
            return BaseResult.defaultErrorWithMsg("???????????????????????????????????????????????????????????????");
        }
        String bizId = oldPro.getBizId();
        //?????????????????????????????????
        boolean updateInitPrice = servicesUtil.hasPermission(shopId, getUserId(), "pro:updateInitPrice");
        //????????????????????????
        BigDecimal initPrice = new BigDecimal(productUploadPrice.getInitPrice());
        if (!LocalUtils.isEmptyAndNull(initPrice) && oldPro.getInitPrice().intValue() != initPrice.intValue()) {
            if (!updateInitPrice) {
                return BaseResult.defaultErrorWithMsg("??????????????????????????????!");
            }
        }
        Boolean flag = false;
        try {
            //???????????????????????????2.5.2???????????????
            BasicParam basicParam = getBasicParam();
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.5.2") >= 0) {
                if (!LocalUtils.isEmptyAndNull(productUploadPrice.getTotalNum()) && oldPro.getTotalNum() > Integer.parseInt(productUploadPrice.getTotalNum())) {
                    flag = true;
                }
            }
        } catch (Exception e) {
            throw new MyException("???????????????????????????" + e);
        }
        if (flag) {
            //????????????????????????????????????????????????
            proTempProductService.selectProductNum(oldPro.getId(), Integer.parseInt(productUploadPrice.getTotalNum()));
        }
        int rows = proProductService.updateProProductPrice(productUploadPrice, getShopId(), getUserId());
        // //???????????????, ??????????????????.2.5.2???????????? ?????????????????????????????????
        if (Integer.parseInt(productUploadPrice.getTotalNum()) != oldPro.getTotalNum().intValue()) {
            proProductService.clearProLockNumByBizId(shopId, bizId);
            proProductService.getProRedisNum(shopId, bizId);
        }
        ParamProductUpload paramProductUpload = new ParamProductUpload();
        BeanUtils.copyProperties(productUploadPrice, paramProductUpload);
        //??????????????????????????????
        opPushService.pushUpdateProdPriceMsg(shopId, paramProductUpload, oldPro);
        //???????????????????????????????????? ??????????????????????????? ??????????????????????????????????????????
        if (EnumProAttribute.OWN.getCode().equals(oldPro.getFkProAttributeCode())) {
            opPushService.pushUpdateInitPriceMsg(shopId, paramProductUpload, oldPro);
        }
        //?????????4????????????????????????
        equalsPriceAndNum(oldPro, paramProductUpload, productUploadPrice.getPlatform(), productUploadPrice.getChangeInitPriceRemark());


        //??????????????????????????????-??????????????????
        return saveUpdateProdOperateLog(paramProductUpload, request, shopId, userId, oldPro, oldPro, rows);
    }

    /**
     * ????????????????????????????????????
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
        //????????????
        if (!oldPro.getRecycleAdmin().equals(recycleAdminUserId)) {
            updateFieldCount++;
            operateContent += "????????????????????????" + recycleAdminUserName + "???";
        }
        //?????????
        BigDecimal initPriceOld = oldPro.getInitPrice();
        if (paramProductUpload.getInitPrice() != null) {
            BigDecimal initPriceNew = new BigDecimal(paramProductUpload.getInitPrice());
            if (initPriceOld.compareTo(initPriceNew) != 0) {
                updateFieldCount++;
                try {
                    String oldInitPrice = LocalUtils.calcNumber(oldPro.getInitPrice(), "/", 100, 0).toString();
                    operateContent += "??????????????????" + oldInitPrice + "???????????????" + initPriceNew.divide(new BigDecimal(100.00)).toString() + "???";
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

        //?????????
        BigDecimal tradePriceOld = oldPro.getTradePrice();
        if (paramProductUpload.getTradePrice() != null) {
            BigDecimal tradePriceNew = new BigDecimal(paramProductUpload.getTradePrice());
            if (tradePriceOld.compareTo(tradePriceNew) != 0) {
                updateFieldCount++;
                if (updateFieldCount != 1) {
                    operateContent += ",";
                }
                operateContent += "?????????????????????" + tradePriceNew.divide(new BigDecimal(100.00)).toString() + "???";
            }
        }

        //?????????
        BigDecimal agencyPriceOld = oldPro.getAgencyPrice();
        if (paramProductUpload.getAgencyPrice() != null) {
            BigDecimal agencyPriceNew = new BigDecimal(paramProductUpload.getAgencyPrice());
            if (agencyPriceOld.compareTo(agencyPriceNew) != 0) {
                updateFieldCount++;
                if (updateFieldCount != 1) {
                    operateContent += ",";
                }
                operateContent += "?????????????????????" + agencyPriceNew.divide(new BigDecimal(100.00)).toString() + "???";
            }
        }

        //?????????
        BigDecimal salePriceOld = oldPro.getSalePrice();
        if (paramProductUpload.getSalePrice() != null) {
            BigDecimal salePriceNew = new BigDecimal(paramProductUpload.getSalePrice());
            if (salePriceOld.compareTo(salePriceNew) != 0) {
                updateFieldCount++;
                if (updateFieldCount != 1) {
                    operateContent += ",";
                }
                operateContent += "?????????????????????" + salePriceNew.divide(new BigDecimal(100.00)).toString() + "???";
            }
        }

        //??????
        Integer totalNumOld = oldPro.getTotalNum();
        Integer totalNumNew = Integer.parseInt(paramProductUpload.getTotalNum());
        if (!totalNumOld.equals(totalNumNew)) {
            updateFieldCount++;
            if (updateFieldCount != 1) {
                operateContent += ",";
            }
            operateContent += "??????????????????" + paramProductUpload.getTotalNum() + "???";
        }

        //????????????
        String name = oldPro.getName();
        if (paramProductUpload.getName() != null) {
            String nameNew = paramProductUpload.getName();
            if (!name.equals(nameNew)) {
                updateFieldCount++;
                if (updateFieldCount != 1) {
                    operateContent += ",";
                }
                operateContent += "???????????????" + name + "???????????????????????????" + nameNew + "???";
            }
        }

        //????????????
        String description = oldPro.getDescription();
        if (paramProductUpload.getDescription() != null) {
            String descriptionNew = paramProductUpload.getDescription();
            if (!description.equals(descriptionNew)) {
                updateFieldCount++;
                if (updateFieldCount != 1) {
                    operateContent += ",";
                }
                operateContent += "???????????????" + description + "???????????????????????????" + descriptionNew + "???";
            }
        }

        //????????????
        String targetUser = oldPro.getTargetUser();
        if (paramProductUpload.getTargetUser() != null) {
            String targetUserNew = paramProductUpload.getTargetUser();
            if (!targetUser.equals(targetUserNew)) {
                updateFieldCount++;
                if (updateFieldCount != 1) {
                    operateContent += ",";
                }
                operateContent += "?????????????????????" + targetUser + "?????????????????????????????????" + targetUserNew + "???";
            }
        }

        operateContent += ")";

        ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
        paramAddShpOperateLog.setShopId(shopId);
        paramAddShpOperateLog.setOperateUserId(userId);
        paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.PROD.getName());
        paramAddShpOperateLog.setOperateName("????????????");
        paramAddShpOperateLog.setOperateContent(operateContent);
        paramAddShpOperateLog.setProdId(oldPro.getId());
        paramAddShpOperateLog.setOrderId(null);
        paramAddShpOperateLog.setRequest(request);

        //?????????????????????????????????
        if (updateFieldCount > 0) {
            shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
        }
        return BaseResult.okResult(rows);
    }

    /**
     * ??????????????????
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "??????????????????",
            notes = "1.??????????????????;<br/>",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "????????????;????????????"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "??????token")
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
            throw new MyException("????????????????????????????????????");
        }
        proRedeem.setFinishPrice(new BigDecimal(paramProductRedeem.getRedeemPrice()).multiply(new BigDecimal(100.00)));
        int proId = proProductService.redeemProduct(proRedeem, userId);

        //??????????????????-????????????????????????
        ParamFinShopRecordAdd paramFinShopRecordAdd = new ParamFinShopRecordAdd();
        paramFinShopRecordAdd.setInoutType(EnumFinShopRecordInoutType.OUT.getCode());
        paramFinShopRecordAdd.setChangeAmount(paramProductRedeem.getRedeemPrice());
        paramFinShopRecordAdd.setType("????????????");
        paramFinShopRecordAdd.setHappenTime(DateUtil.format(new Date()));
        String note = "???" + proRedeem.getName() + "????????????????????????";
        paramFinShopRecordAdd.setNote(note);
        String imgUrl = proRedeem.getSmallImg();
        finShopRecordService.addFinShopRecord(shopId, getUserId(), paramFinShopRecordAdd, EnumFinShopRecordType.SYSTEM, imgUrl, proRedeem.getBizId());
        //????????????????????????????????????
        ParamFundRecordAdd paramFundRecordAddPro = new ParamFundRecordAdd();
        paramFundRecordAddPro.setFkShpShopId(shopId);
        paramFundRecordAddPro.setUserId(getUserId());
        paramFundRecordAddPro.setMoney(paramProductRedeem.getRedeemPrice());
        paramFundRecordAddPro.setInitPrice(proRedeem.getInitPrice().toString());
        paramFundRecordAddPro.setState(EnumFinShopRecordInoutType.IN.getCode());
        paramFundRecordAddPro.setFundType("60");
        paramFundRecordAddPro.setCount("1");
        paramFundRecordAddPro.setFinClassifyName("????????????????????????");
        paramFundRecordAddPro.setAttributeCode("30");
        fundRecordService.addProductFundRecord(paramFundRecordAddPro);

        //????????????????????????
        addProModifyRecord(proRedeem.getId(), "??????", proRedeem.getName(), proRedeem.getName(), paramProductRedeem.getPlatform());

        //??????????????????
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
        //????????????????????????,???????????????,??????,????????????,??????,?????????????????????;
        if (20 == state) {
            String note = "?????????,?????????";
            if (LocalUtils.isEmptyAndNull(description)) {
                throw new MyException(note + "??????????????????");
            }
            if (LocalUtils.isEmptyAndNull(attribute)) {
                throw new MyException(note + "??????????????????");
            }
            if (LocalUtils.isEmptyAndNull(classify)) {
                throw new MyException(note + "??????????????????");
            }
            //2020-09-04 sanjin145 ??????????????????????????????????????????
//            if (LocalUtils.isEmptyAndNull(repairCard)) {
//                throw new MyException(note + "??????????????????");
//            }
        }
        //????????????????????????
        if (LocalUtils.isEmptyAndNull(voPro.getUniqueCode())) {
            voPro.setUniqueCode("");
        }
        //??????????????????
        if (LocalUtils.isEmptyAndNull(voPro.getClassifySub())) {
            voPro.setClassifySub("");
        }
        //??????????????????
        if (LocalUtils.isEmptyAndNull(voPro.getSubSeriesName())) {
            voPro.setSubSeriesName("");
        }
        //??????????????????
        if (LocalUtils.isEmptyAndNull(voPro.getSeriesModelName())) {
            voPro.setSeriesModelName("");
        }
        //???????????????????????????
        String repairCardTime = voPro.getRepairCardTime();
        try {
            BasicParam basicParam = getBasicParam();
            String platform = basicParam.getPlatform();
            String appVersion = basicParam.getAppVersion();

            //???????????????????????????????????????????????????
            Boolean isOldVersion = Boolean.FALSE;
            if (StringUtil.isNotBlank(appVersion)) {
                if ("android".equals(platform)) {
                    isOldVersion = VersionUtils.compareVersion(appVersion, "2.1.0") < 0;
                } else if ("ios".equals(platform)) {
                    isOldVersion = VersionUtils.compareVersion(appVersion, "2.2.1") < 0;
                }
            }
            if (isOldVersion) {
                repairCardTime = StringUtil.remove(repairCardTime, "???");
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
