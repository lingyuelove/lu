package com.luxuryadmin.apiadmin.pro;

import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.base.BasicParam;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.common.utils.VersionUtils;
import com.luxuryadmin.common.utils.aliyun.OSSUtil;
import com.luxuryadmin.entity.pro.ProDetail;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.enums.fin.EnumFinShopRecordInoutType;
import com.luxuryadmin.enums.fin.EnumFinShopRecordType;
import com.luxuryadmin.enums.pro.EnumProAttribute;
import com.luxuryadmin.enums.pro.EnumProState;
import com.luxuryadmin.enums.shp.EnumShpOperateLogModule;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.param.fin.ParamFinShopRecordAdd;
import com.luxuryadmin.param.fin.ParamFundRecordAdd;
import com.luxuryadmin.param.pro.ParamProductRedeem;
import com.luxuryadmin.param.pro.ParamProductUpload;
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
import com.luxuryadmin.vo.pro.VoProAccessory;
import com.luxuryadmin.vo.pro.VoProAttribute;
import com.luxuryadmin.vo.pro.VoProClassify;
import com.luxuryadmin.vo.pro.VoProSource;
import com.luxuryadmin.vo.shp.VoEmployee;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author monkey king
 * @date 2020-05-26 19:19:22
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/admin/pro", method = RequestMethod.POST)
@Api(tags = {"B002.??????????????????"}, description = "/shop/user/pro |??????????????????????????????")
public class ProProductUploadController extends ProProductBaseController {

    @Autowired
    private ProProductService proProductService;
    @Autowired
    private ProDetailService proDetailService;
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
    public BaseResult initUpload(@RequestParam Map<String, String> params) {
        int shopId = getShopId();
        List<VoProAttribute> voProAttributeList = proAttributeService.listProAttributeByShopId(shopId);
        List<VoProClassify> voProClassifyList = proClassifyService.listProClassifyByState(shopId, null);
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
        return BaseResult.okResult(hashMap);
    }


    /**
     * ????????????
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "????????????",
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
        int proId = proProductService.saveProProductReturnId(pro, proDetail,paramProductUpload.getProductClassifyAddLists());
        redisUtil.delete(key);

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

        //??????????????????????????????
        ParamFundRecordAdd paramFundRecordAdd =new ParamFundRecordAdd();
        paramFundRecordAdd.setFkShpShopId(shopId);
        paramFundRecordAdd.setUserId(getUserId());
        paramFundRecordAdd.setMoney( paramProductUpload.getInitPrice());
        paramFundRecordAdd.setInitPrice("0");
        paramFundRecordAdd.setState(paramFinShopRecordAdd.getInoutType());
        paramFundRecordAdd.setFundType("10");
        paramFundRecordAdd.setCount(paramProductUpload.getTotalNum());
        paramFundRecordAdd.setFinClassifyName("????????????");
        paramFundRecordAdd.setAttributeCode(pro.getFkProAttributeCode());
        fundRecordService.addProductFundRecord(paramFundRecordAdd);

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

        //????????????;10:???????????????;20:????????????
        String state = paramProductUpload.getState();
        state = "10".equals(state) ? "???????????????" : "????????????";
        addProModifyRecord(proId, "??????", state, prodName, prodName,"pc");
        addProModifyRecord(proId, "??????", "????????????", "0", paramProductUpload.getTotalNum(),"pc");
        return BaseResult.okResult(proId);
    }

    /**
     * ????????????id;????????????;
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "????????????id;????????????;",
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
        ProProduct pro = proProductService.pickProProduct(paramProductUpload, false, shopId, userId);
        int state = pro.getFkProStateCode();
        if (state >= EnumProState.SALE_40.getCode() || state < EnumProState.STAND_BY_10.getCode()) {
            return BaseResult.defaultErrorWithMsg("???????????????????????????????????????????????????????????????");
        }
        String bizId = pro.getBizId();
        ProProduct oldPro = getProProductByShopIdBizId(shopId, bizId);
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

        ProDetail proDetail = proProductService.pickProDetail(paramProductUpload, false);

        int rows = proProductService.updateProProduct(pro, proDetail,paramProductUpload.getProductClassifyAddLists());
        //???????????????, ??????????????????.
        if (oldPro.getTotalNum().intValue() != pro.getTotalNum().intValue()) {
            proProductService.clearProLockNumByBizId(shopId, bizId);
            proProductService.getProRedisNum(shopId, bizId);
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
            //??????????????????????????????
            ParamFundRecordAdd paramFundRecordAdd =new ParamFundRecordAdd();
            paramFundRecordAdd.setFkShpShopId(shopId);
            paramFundRecordAdd.setUserId(getUserId());
            paramFundRecordAdd.setMoney( pro.getInitPrice().toString());
            paramFundRecordAdd.setInitPrice("0");
            paramFundRecordAdd.setState(paramFinShopRecordAdd.getInoutType());
            paramFundRecordAdd.setFundType("10");
            paramFundRecordAdd.setCount(pro.getTotalNum().toString());
            paramFundRecordAdd.setFinClassifyName("????????????");
            paramFundRecordAdd.setAttributeCode(pro.getFkProAttributeCode());
            fundRecordService.addFundRecordForUpdateProduct(paramFundRecordAdd,paramProductUpload.getTotalNum());

        }

        //????????????
        //????????????????????????
        // ordOrderService.refreshSalaryForUser(shopId, pro.getRecycleAdmin(), userId, pro.getInsertTime());
        //????????????????????????
        //ordOrderService.refreshSalaryForUser(shopId, oldPro.getRecycleAdmin(), userId, pro.getInsertTime());

        //??????????????????????????????
        opPushService.pushUpdateProdPriceMsg(shopId, paramProductUpload, oldPro);
        //???????????????????????????????????? ??????????????????????????? ??????????????????????????????????????????
        if(EnumProAttribute.OWN.getCode().equals(oldPro.getFkProAttributeCode()) && EnumProAttribute.OWN.getCode().equals(paramProductUpload.getAttributeCode())){
            opPushService.pushUpdateInitPriceMsg(shopId, paramProductUpload, oldPro);
        }


        //?????????????????????????????????????????????????????????
        if ("20".equals(paramProductUpload.getState()) && LocalUtils.isBetween(oldPro.getFkProStateCode(), 10, 19)) {
            List<String> bizIdList = new ArrayList<>();
            bizIdList.add(pro.getBizId());
            opPushService.pushReleaseProductMsg(shopId, bizIdList);
        }
        //????????????????????????
        String source ="pc";
        equalsMoreProduct(oldPro, oldDetail, paramProductUpload,source);

        String state1 = paramProductUpload.getState();
        if ("11".equals(state1)) {
            addProModifyRecord(oldPro.getId(), "??????", "???????????????", oldPro.getName(), oldPro.getName(),source);
        } else if ("20".equals(state1)) {
            addProModifyRecord(oldPro.getId(), "??????", "???????????????", oldPro.getName(), oldPro.getName(),source);
        }

        //??????????????????????????????-??????????????????
        return saveUpdateProdOperateLog(paramProductUpload, request, shopId, userId, pro, oldPro, rows);
    }

    /**
     * ??????????????????
     *
     * @param paramToken
     * @return
     * @throws Exception
     */
    @ApiOperation(
            value = "??????????????????;",
            notes = "??????????????????;",
            httpMethod = "POST")
    @PostMapping("/uploadImg")
    public BaseResult uploadImg(@Valid ParamToken paramToken, BindingResult result, HttpServletRequest request) throws Exception {
        servicesUtil.validControllerParam(result);
        StringBuffer dirName = new StringBuffer();
        dirName.append("product/picture/").append(getShopId());
        dirName.append("/").append(DateUtil.format(new Date(), "YYYYMMdd"));
        dirName.append("/").append(getUserId()).append("/");
        String filePath = OSSUtil.uploadBaseMethod(request, dirName.toString(), 10);
        return BaseResult.okResult(filePath);
    }


    /**
     * ????????????
     *
     * @param paramToken
     * @return
     * @throws Exception
     */
    @ApiOperation(
            value = "????????????;",
            notes = "????????????;",
            httpMethod = "POST")
    @PostMapping("/uploadVideo")
    public BaseResult uploadVideo(@Valid ParamToken paramToken, BindingResult result,
                                  @RequestParam("file") MultipartFile file) throws Exception {
        servicesUtil.validControllerParam(result);
        if (file.isEmpty()) {
            return BaseResult.errorResult("???????????????!");
        }
        long fileSize = file.getSize();
        if (fileSize > 300 * 1024 * 1024) {
            return BaseResult.errorResult("??????????????????????????????300M.");
        }
        String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        if (!"mp4".equalsIgnoreCase(fileType)) {
            return BaseResult.errorResult("?????????mp4??????!");
        }
        StringBuffer dirName = new StringBuffer();
        dirName.append("product/video/").append(getShopId());
        dirName.append("/").append(DateUtil.format(new Date(), "YYYYMMdd"));
        dirName.append("/").append(getUserId()).append("/");
        String fileName = System.currentTimeMillis() + "." + fileType;
        String relativePath = "/" + dirName + fileName;

        String serverPath = OSSUtil.uploadBytesFile(dirName.toString() + fileName, file.getBytes());

        return BaseResult.okResult(relativePath);
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
        Integer recycleAdminUserId = pro.getRecycleAdmin();
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
                operateContent += "?????????????????????" + initPriceNew.divide(new BigDecimal(100.00)).toString() + "???";
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
        operateContent += ")";

        ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
        paramAddShpOperateLog.setShopId(shopId);
        paramAddShpOperateLog.setOperateUserId(userId);
        paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.PROD.getName());
        paramAddShpOperateLog.setOperateName("????????????");
        paramAddShpOperateLog.setOperateContent(operateContent);
        paramAddShpOperateLog.setProdId(null);
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
    @RequiresPermissions("pro:redeemProduct")
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
        ParamFundRecordAdd paramFundRecordAdd =new ParamFundRecordAdd();
        paramFundRecordAdd.setFkShpShopId(shopId);
        paramFundRecordAdd.setUserId(getUserId());
        paramFundRecordAdd.setMoney( paramProductRedeem.getRedeemPrice());
        paramFundRecordAdd.setInitPrice(proRedeem.getInitPrice().toString());
        paramFundRecordAdd.setState(paramFinShopRecordAdd.getInoutType());
        paramFundRecordAdd.setFundType("60");
        paramFundRecordAdd.setCount("1");
        paramFundRecordAdd.setFinClassifyName("????????????????????????");
        paramFundRecordAdd.setAttributeCode("30");
        fundRecordService.addProductFundRecord(paramFundRecordAdd);
        //????????????????????????
        addProModifyRecord(proRedeem.getId(), "??????", proRedeem.getName(), proRedeem.getName(),"pc");
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


}
