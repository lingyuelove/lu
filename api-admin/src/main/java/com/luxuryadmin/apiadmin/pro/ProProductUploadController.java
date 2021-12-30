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
@Api(tags = {"B002.【进销】模块"}, description = "/shop/user/pro |用户【进销】模块相关")
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
    public BaseResult initUpload(@RequestParam Map<String, String> params) {
        int shopId = getShopId();
        List<VoProAttribute> voProAttributeList = proAttributeService.listProAttributeByShopId(shopId);
        List<VoProClassify> voProClassifyList = proClassifyService.listProClassifyByState(shopId, null);
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
        return BaseResult.okResult(hashMap);
    }


    /**
     * 上传商品
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "上传商品",
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
        int proId = proProductService.saveProProductReturnId(pro, proDetail,paramProductUpload.getProductClassifyAddLists());
        redisUtil.delete(key);

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

        //新增商品添加账单记录
        ParamFundRecordAdd paramFundRecordAdd =new ParamFundRecordAdd();
        paramFundRecordAdd.setFkShpShopId(shopId);
        paramFundRecordAdd.setUserId(getUserId());
        paramFundRecordAdd.setMoney( paramProductUpload.getInitPrice());
        paramFundRecordAdd.setInitPrice("0");
        paramFundRecordAdd.setState(paramFinShopRecordAdd.getInoutType());
        paramFundRecordAdd.setFundType("10");
        paramFundRecordAdd.setCount(paramProductUpload.getTotalNum());
        paramFundRecordAdd.setFinClassifyName("入库记录");
        paramFundRecordAdd.setAttributeCode(pro.getFkProAttributeCode());
        fundRecordService.addProductFundRecord(paramFundRecordAdd);

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

        //商品状态;10:保存到库存;20:立即上架
        String state = paramProductUpload.getState();
        state = "10".equals(state) ? "保存到仓库" : "立即上架";
        addProModifyRecord(proId, "入库", state, prodName, prodName,"pc");
        addProModifyRecord(proId, "入库", "入库数量", "0", paramProductUpload.getTotalNum(),"pc");
        return BaseResult.okResult(proId);
    }

    /**
     * 根据业务id;更新商品;
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "根据业务id;更新商品;",
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
        ProProduct pro = proProductService.pickProProduct(paramProductUpload, false, shopId, userId);
        int state = pro.getFkProStateCode();
        if (state >= EnumProState.SALE_40.getCode() || state < EnumProState.STAND_BY_10.getCode()) {
            return BaseResult.defaultErrorWithMsg("【未上架】和【已上架】状态的商品才能修改！");
        }
        String bizId = pro.getBizId();
        ProProduct oldPro = getProProductByShopIdBizId(shopId, bizId);
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

        ProDetail proDetail = proProductService.pickProDetail(paramProductUpload, false);

        int rows = proProductService.updateProProduct(pro, proDetail,paramProductUpload.getProductClassifyAddLists());
        //更改了库存, 清空锁单记录.
        if (oldPro.getTotalNum().intValue() != pro.getTotalNum().intValue()) {
            proProductService.clearProLockNumByBizId(shopId, bizId);
            proProductService.getProRedisNum(shopId, bizId);
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
            //商品修改添加账单记录
            ParamFundRecordAdd paramFundRecordAdd =new ParamFundRecordAdd();
            paramFundRecordAdd.setFkShpShopId(shopId);
            paramFundRecordAdd.setUserId(getUserId());
            paramFundRecordAdd.setMoney( pro.getInitPrice().toString());
            paramFundRecordAdd.setInitPrice("0");
            paramFundRecordAdd.setState(paramFinShopRecordAdd.getInoutType());
            paramFundRecordAdd.setFundType("10");
            paramFundRecordAdd.setCount(pro.getTotalNum().toString());
            paramFundRecordAdd.setFinClassifyName("修改订单");
            paramFundRecordAdd.setAttributeCode(pro.getFkProAttributeCode());
            fundRecordService.addFundRecordForUpdateProduct(paramFundRecordAdd,paramProductUpload.getTotalNum());

        }

        //刷新薪资
        //修改后的回收人员
        // ordOrderService.refreshSalaryForUser(shopId, pro.getRecycleAdmin(), userId, pro.getInsertTime());
        //修改前的回收人员
        //ordOrderService.refreshSalaryForUser(shopId, oldPro.getRecycleAdmin(), userId, pro.getInsertTime());

        //发送修改商品价格消息
        opPushService.pushUpdateProdPriceMsg(shopId, paramProductUpload, oldPro);
        //发送商品成本价格变动消息 只有原先是自有商品 修改后也是自有商品才进行通知
        if(EnumProAttribute.OWN.getCode().equals(oldPro.getFkProAttributeCode()) && EnumProAttribute.OWN.getCode().equals(paramProductUpload.getAttributeCode())){
            opPushService.pushUpdateInitPriceMsg(shopId, paramProductUpload, oldPro);
        }


        //商品状态从下架变成了上架，发送上架消息
        if ("20".equals(paramProductUpload.getState()) && LocalUtils.isBetween(oldPro.getFkProStateCode(), 10, 19)) {
            List<String> bizIdList = new ArrayList<>();
            bizIdList.add(pro.getBizId());
            opPushService.pushReleaseProductMsg(shopId, bizIdList);
        }
        //添加商品操作记录
        String source ="pc";
        equalsMoreProduct(oldPro, oldDetail, paramProductUpload,source);

        String state1 = paramProductUpload.getState();
        if ("11".equals(state1)) {
            addProModifyRecord(oldPro.getId(), "下架", "保存并下架", oldPro.getName(), oldPro.getName(),source);
        } else if ("20".equals(state1)) {
            addProModifyRecord(oldPro.getId(), "上架", "保存并上架", oldPro.getName(), oldPro.getName(),source);
        }

        //添加【店铺操作日志】-【修改商品】
        return saveUpdateProdOperateLog(paramProductUpload, request, shopId, userId, pro, oldPro, rows);
    }

    /**
     * 商品图片上传
     *
     * @param paramToken
     * @return
     * @throws Exception
     */
    @ApiOperation(
            value = "商品图片上传;",
            notes = "商品图片上传;",
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
     * 视频上传
     *
     * @param paramToken
     * @return
     * @throws Exception
     */
    @ApiOperation(
            value = "视频上传;",
            notes = "视频上传;",
            httpMethod = "POST")
    @PostMapping("/uploadVideo")
    public BaseResult uploadVideo(@Valid ParamToken paramToken, BindingResult result,
                                  @RequestParam("file") MultipartFile file) throws Exception {
        servicesUtil.validControllerParam(result);
        if (file.isEmpty()) {
            return BaseResult.errorResult("请上传文件!");
        }
        long fileSize = file.getSize();
        if (fileSize > 300 * 1024 * 1024) {
            return BaseResult.errorResult("上传文件大小超出限制300M.");
        }
        String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        if (!"mp4".equalsIgnoreCase(fileType)) {
            return BaseResult.errorResult("只支持mp4格式!");
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
        Integer recycleAdminUserId = pro.getRecycleAdmin();
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
                operateContent += "修改成本价为【" + initPriceNew.divide(new BigDecimal(100.00)).toString() + "】";
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
        operateContent += ")";

        ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
        paramAddShpOperateLog.setShopId(shopId);
        paramAddShpOperateLog.setOperateUserId(userId);
        paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.PROD.getName());
        paramAddShpOperateLog.setOperateName("修改商品");
        paramAddShpOperateLog.setOperateContent(operateContent);
        paramAddShpOperateLog.setProdId(null);
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
    @RequiresPermissions("pro:redeemProduct")
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
        ParamFundRecordAdd paramFundRecordAdd =new ParamFundRecordAdd();
        paramFundRecordAdd.setFkShpShopId(shopId);
        paramFundRecordAdd.setUserId(getUserId());
        paramFundRecordAdd.setMoney( paramProductRedeem.getRedeemPrice());
        paramFundRecordAdd.setInitPrice(proRedeem.getInitPrice().toString());
        paramFundRecordAdd.setState(paramFinShopRecordAdd.getInoutType());
        paramFundRecordAdd.setFundType("60");
        paramFundRecordAdd.setCount("1");
        paramFundRecordAdd.setFinClassifyName("质押商品账单记录");
        paramFundRecordAdd.setAttributeCode("30");
        fundRecordService.addProductFundRecord(paramFundRecordAdd);
        //添加商品操作记录
        addProModifyRecord(proRedeem.getId(), "赎回", proRedeem.getName(), proRedeem.getName(),"pc");
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


}
