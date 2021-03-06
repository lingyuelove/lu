package com.luxuryadmin.api.biz;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.base.BasicParam;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.constant.enums.EnumCode;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.VersionUtils;
import com.luxuryadmin.entity.biz.BizLeaguer;
import com.luxuryadmin.enums.shp.EnumShpOperateLogModule;
import com.luxuryadmin.param.biz.*;
import com.luxuryadmin.param.pro.ParamClassifyTypeSearch;
import com.luxuryadmin.param.pro.ParamProductBizId;
import com.luxuryadmin.param.shp.ParamAddShpOperateLog;
import com.luxuryadmin.service.biz.BizLeaguerConfigService;
import com.luxuryadmin.service.biz.BizLeaguerService;
import com.luxuryadmin.service.biz.BizShopRecommendService;
import com.luxuryadmin.service.biz.BizShopSeeService;
import com.luxuryadmin.service.pro.ProClassifyService;
import com.luxuryadmin.service.pro.ProDetailService;
import com.luxuryadmin.service.pro.ProProductService;
import com.luxuryadmin.service.pro.ProStandardService;
import com.luxuryadmin.service.shp.ShpOperateLogService;
import com.luxuryadmin.service.shp.ShpShopService;
import com.luxuryadmin.service.shp.ShpWechatService;
import com.luxuryadmin.vo.biz.*;
import com.luxuryadmin.vo.pro.VoClassifyTypeSon;
import com.luxuryadmin.vo.pro.VoLeaguerProduct;
import com.luxuryadmin.vo.pro.VoProClassify;
import com.luxuryadmin.vo.shp.VoShpWechat;
import com.luxuryadmin.vo.shp.VoUserShopBase;
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
import java.util.*;

/**
 * @author monkey king
 * @date 2020-01-11 21:52:16
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/biz", method = RequestMethod.POST)
@Api(tags = {"G002.?????????????????? --2.6.3 --zs "}, description = "/shop/user/biz |????????????????????????")
//??????????????????????????????
//@RequiresPermissions("leaguer:check:shop")
public class BizLeaguerController extends BizLeaguerBaseController {

    @Autowired
    private BizLeaguerService bizLeaguerService;

    @Autowired
    private ProProductService proProductService;

    @Autowired
    private ProClassifyService proClassifyService;

    @Autowired
    private BizLeaguerConfigService bizLeaguerConfigService;

    @Autowired
    private ShpWechatService shpWechatService;

    @Autowired
    private ProDetailService proDetailService;

    @Autowired
    private ShpOperateLogService shpOperateLogService;

    @Autowired
    private ShpShopService shpShopService;

    @Autowired
    private BizShopRecommendService bizShopRecommendService;

    @Autowired
    private BizShopSeeService bizShopSeeService;
    @Autowired
    private ProStandardService proStandardService;

    /**
     * ???????????????
     */
    @ApiOperation(
            value = "?????????????????????;",
            notes = "?????????????????????;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "??????token"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "????????????;????????????")
    })
    @RequestRequire
    @RequestMapping("/listContact")
    public BaseResult<VoBizLeaguer> listContact(@RequestParam Map<String, String> params) {
        List<VoBizLeaguer> leaguerList = bizLeaguerService.listBizLeaguerByShopId(getShopId());
        formatVoBizLeaguer(leaguerList);
        return LocalUtils.getBaseResult(leaguerList);
    }


    /**
     * ??????(??????)????????????;
     */
    @ApiOperation(
            value = "??????(??????)????????????;",
            notes = "??????(??????)????????????;",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "??????token"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "????????????;????????????")
    })
    @RequestRequire
    @GetMapping("/getSpecificLeaguerDetail")
    public BaseResult<VoBizLeaguer> getSpecificLeaguerDetail(
            @RequestParam Map<String, String> params,
            @Valid ParamLeaguerShopId paramLeaguerShopId, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String leaguerShopId = paramLeaguerShopId.getLeaguerShopId();
        VoBizLeaguer voBizLeaguerMine = bizLeaguerService.getSpecificLeaguerDetail(getShopId(), Integer.parseInt(leaguerShopId));
        VoBizLeaguer voBizLeaguer = bizLeaguerService.getSpecificLeaguerDetail(Integer.parseInt(leaguerShopId), getShopId());
        formatVoBizLeaguer(voBizLeaguerMine);

        //?????????????????????????????????0
        Integer onSaleProductNum = 0;
        //???????????????,????????????????????????
        if (!LocalUtils.isEmptyAndNull(voBizLeaguer) && ConstantCommon.ONE.equals(voBizLeaguer.getVisible())) {
            //&& ConstantCommon.ONE.equals(""+voBizLeaguerMine.getIsWantSeeLeaguerProd())) {
            onSaleProductNum = proProductService.getOnSaleProductNumByShopId(leaguerShopId);
        }
        if (onSaleProductNum == null) {
            onSaleProductNum = 0;
        }
        voBizLeaguerMine.setOnSaleProductNum(onSaleProductNum);

        //??????????????????A???????????????B????????????????????????????????????????????????
        Integer isCanSeeSalePrice = voBizLeaguerMine.getIsCanSeeSalePrice();
        voBizLeaguerMine.setIsCanSeeSalePrice(1 - isCanSeeSalePrice);
        Integer isCanSeeTradePrice = voBizLeaguerMine.getIsCanSeeTradePrice();
        voBizLeaguerMine.setIsCanSeeTradePrice(1 - isCanSeeTradePrice);
        ParamShopSeeAdd shopSeeAdd = new ParamShopSeeAdd();
        shopSeeAdd.setShopId(getShopId());
        shopSeeAdd.setUserId(getUserId());
        shopSeeAdd.setBeSeenShopId(Integer.parseInt(leaguerShopId));
        bizShopSeeService.saveShopSee(shopSeeAdd);
        return LocalUtils.getBaseResult(voBizLeaguerMine);
    }

    /**
     * ??????(??????)????????????????????????;
     */
    @ApiOperation(
            value = "??????(??????)????????????????????????;",
            notes = "??????(??????)????????????????????????;",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "??????token")
    })
    @RequestRequire
    @GetMapping("/getCanSeeLeaguerPriceInfo")
    public BaseResult<VoCanSeeLeaguerPriceInfo> getCanSeeLeaguerPriceInfo(@RequestParam Map<String, String> params) {
        Integer shopId = getShopId();
        Integer userId = getUserId();
        VoCanSeeLeaguerPriceInfo priceInfo;
        try {
            priceInfo = bizLeaguerConfigService.getCanSeeLeaguerPriceInfo(shopId, userId);
        } catch (Exception e) {
            return BaseResult.defaultErrorWithMsg(e.getMessage());
        }
        return LocalUtils.getBaseResult(priceInfo);
    }

    /**
     * ????????????--????????????;
     */
    @ApiOperation(
            value = "????????????--???????????? --2.5.2 --zs;",
            notes = "????????????--???????????? --2.5.2 --zs;",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "??????token")
    })
    @GetMapping("/getLeaguerShop")
    @RequiresPermissions("leaguer:check:shop")
    public BaseResult<VoBizLeaguerShop> getLeaguerShop() {
        Integer shopId = getShopId();
        VoBizLeaguerShop leaguerShop = bizLeaguerConfigService.getLeaguerShop(shopId);

        return BaseResult.okResult(leaguerShop);
    }

    /**
     * ????????????--????????????;
     */
    @ApiOperation(
            value = "??????????????????--???????????? --2.5.2 --zs;",
            notes = "??????????????????--???????????? --2.5.2 --zs;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "??????token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "pageNum", value = "pageNum")
    })
    @PostMapping("/getRecommendLeaguerList")
    public BaseResult<VoLeaguerRecommendPage> getRecommendLeaguerList(@Valid ParamLeaguerRecommendBySearch leaguerRecommendBySearch) {
        try {
            VoCanSeeLeaguerPriceInfo seeLeaguerPriceInfo = bizLeaguerConfigService.getCanSeeLeaguerPriceInfo(getShopId(), getUserId());
            if (!LocalUtils.isEmptyAndNull(seeLeaguerPriceInfo) && "0".equals(seeLeaguerPriceInfo.getRecommend())) {
                return BaseResult.okResult();
            }
        } catch (Exception e) {
            throw new MyException("??????????????????--?????????????????????" + e);
        }
        Integer shopId = getShopId();
        leaguerRecommendBySearch.setShopId(shopId);
        VoLeaguerRecommendPage recommendPage = bizShopRecommendService.getRecommendLeaguerList(leaguerRecommendBySearch);
        return BaseResult.okResult(recommendPage);
    }

    /**
     * ????????????--????????????;
     */
    @ApiOperation(
            value = "????????????n?????????--???????????? --2.5.2 --zs;",
            notes = "????????????n?????????--???????????? --2.5.2 --zs;",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "??????token")
    })
    @GetMapping("/getByShowRecommend")
    public BaseResult<List<VoLeaguerRecommend>> getByShowRecommend() {
        Integer shopId = getShopId();
        try {
            VoCanSeeLeaguerPriceInfo seeLeaguerPriceInfo = bizLeaguerConfigService.getCanSeeLeaguerPriceInfo(shopId, getUserId());
            if (!LocalUtils.isEmptyAndNull(seeLeaguerPriceInfo) && "0".equals(seeLeaguerPriceInfo.getRecommend())) {
                return BaseResult.okResult();
            }
        } catch (Exception e) {
            throw new MyException("??????????????????--?????????????????????" + e);
        }
        List<VoLeaguerRecommend> leaguerRecommends = bizShopRecommendService.getByShowRecommend(shopId);
        if (leaguerRecommends != null && leaguerRecommends.size() > 0) {
            return BaseResult.okResult(leaguerRecommends);
        }
        return BaseResult.okResult();
    }

    /**
     * ??????(??????)????????????????????????;
     */
    @ApiOperation(
            value = "??????(??????)????????????????????????;",
            notes = "??????(??????)????????????????????????;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "??????token")
    })
    @RequestRequire
    @GetMapping("/updateCanSeeLeaguerPriceInfo")
    public BaseResult updateCanSeeLeaguerPriceInfo(@Valid ParamCanSeeLeaguerPriceInfo paramCanSeeLeaguerPriceInfo, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        Integer shopId = getShopId();
        Integer userId = getUserId();

        try {
            bizLeaguerConfigService.updateCanSeeLeaguerPriceInfo(shopId, userId, paramCanSeeLeaguerPriceInfo, request);
        } catch (Exception e) {
            return BaseResult.defaultErrorWithMsg(e.getMessage());
        }
        return LocalUtils.getBaseResult("????????????");
    }


    /**
     * ????????????????????????
     *
     * @param params ????????????
     * @return Result
     */
    @ApiOperation(
            value = "????????????????????????",
            notes = "????????????????????????",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "????????????;????????????")
    })
    @RequestRequire
    @RequestMapping("/updateLeaguerNote")
    public BaseResult updateLeaguerNote(@RequestParam Map<String, String> params,
                                        @Valid ParamUpdateLeaguerNote updateNote, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String note = updateNote.getNote();
        String leaguerIdStr = updateNote.getLeaguerId();
        String leaguerShopIdStr = updateNote.getLeaguerShopId();
        int leaguerId = Integer.parseInt(leaguerIdStr);
        int leaguerShopId = Integer.parseInt(leaguerShopIdStr);
        bizLeaguerService.updateLeaguerNote(leaguerId, getShopId(), leaguerShopId, note);
        return BaseResult.okResult();
    }


    /**
     * ??????????????????
     *
     * @param params ????????????
     * @return Result
     */
    @ApiOperation(
            value = "????????????????????????",
            notes = "????????????????????????",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "??????token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "int",
                    name = "leaguerId", value = "leaguerId"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "int",
                    name = "leaguerShopId", value = "????????????id"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "int",
                    name = "visible", value = "????????????????????????????????????; 0:?????????; 1:??????"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "????????????;????????????")
    })
    @RequestRequire
    @RequestMapping("/settingLeaguer")
    public BaseResult settingLeaguer(@RequestParam Map<String, String> params,
                                     @Valid ParamUpdateLeaguerVisible updateVisible, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);


        String leaguerIdStr = updateVisible.getLeaguerId();
        String leaguerShopIdStr = updateVisible.getLeaguerShopId();
        String visible = updateVisible.getVisible();
        int leaguerId = Integer.parseInt(leaguerIdStr);
        int leaguerShopId = Integer.parseInt(leaguerShopIdStr);
        BizLeaguer bizLeaguerOld = bizLeaguerService.getBizLeaguerByShopIdAndLeaguerShopId(getShopId(), leaguerShopId);

        BizLeaguer bizLeaguer = new BizLeaguer();
        bizLeaguer.setId(leaguerId);
        bizLeaguer.setFkInviterShopId(getShopId());
        bizLeaguer.setFkBeInviterShopId(leaguerShopId);
        bizLeaguer.setVisible(visible);//????????????????????????
        bizLeaguer.setTop(updateVisible.getTop());
        bizLeaguer.setUpdateTime(new Date());
        bizLeaguer.setUpdateAdmin(getUserId());
        bizLeaguer.setIsCanSeeSalePrice(1 - updateVisible.getIsCanSeeSalePrice());//?????????
        bizLeaguer.setIsCanSeeTradePrice(1 - updateVisible.getIsCanSeeTradePrice());//?????????
        bizLeaguer.setIsWantSeeLeaguerProd(updateVisible.getNotSeeGoods());//??????????????????
        bizLeaguerService.modifyVisible(bizLeaguer);

        //??????????????????????????????-??????????????????????????????
        VoUserShopBase voUserShopBaseLeaguer = shpShopService.getVoUserShopBaseByShopId(leaguerShopId);
        if (null != voUserShopBaseLeaguer) {
            String leaguerShopName = voUserShopBaseLeaguer.getShopName();
            ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
            paramAddShpOperateLog.setShopId(getShopId());
            paramAddShpOperateLog.setOperateUserId(getUserId());
            paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.LEAGUER_ALBUM.getName());
            paramAddShpOperateLog.setOperateName("????????????????????????");
            paramAddShpOperateLog.setProdId(null);
            paramAddShpOperateLog.setOrderId(null);
            paramAddShpOperateLog.setRequest(request);
            String operateLog = "???" + leaguerShopName + "-" + leaguerShopId + "???";
            constructSetSingleLeaguerPermContent(operateLog, bizLeaguerOld, bizLeaguer, paramAddShpOperateLog);

        }
        return BaseResult.okResult();
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param bizLeaguerOld
     * @param bizLeaguerNew
     * @return
     */
    private void constructSetSingleLeaguerPermContent(String operateLog, BizLeaguer bizLeaguerOld, BizLeaguer bizLeaguerNew, ParamAddShpOperateLog paramAddShpOperateLog) {
        String operateContent = operateLog;
        Integer updateFieldCount = 0;
        //???????????????????????????
        String isCanSeeMyGoodsOld = bizLeaguerOld.getVisible();
        String isCanSeeMyGoodsNew = bizLeaguerNew.getVisible();
        Boolean isUpdateCanSeeMyGoods = !isCanSeeMyGoodsOld.equals(isCanSeeMyGoodsNew);
        if (isUpdateCanSeeMyGoods) {
            updateFieldCount++;
            if (updateFieldCount != 1) {
                operateContent += ";";
            }
            if ("1".equals(isCanSeeMyGoodsNew)) {
                operateContent += "???????????????????????????";
            } else {
                operateContent += "??????????????????????????????";
            }
        }

        //?????????????????????
        Integer isWantSeeLeaguerGoodsOld = bizLeaguerOld.getIsWantSeeLeaguerProd();
        Integer isWantSeeLeaguerGoodsNew = bizLeaguerNew.getIsWantSeeLeaguerProd();
        Boolean isUpdateWantSeeLeaguerMyGoods = !isWantSeeLeaguerGoodsOld.equals(isWantSeeLeaguerGoodsNew);
        if (isUpdateWantSeeLeaguerMyGoods) {
            updateFieldCount++;
            if (updateFieldCount != 1) {
                operateContent += ";";
            }
            if (isWantSeeLeaguerGoodsNew.equals(1)) {
                operateContent += "?????????????????????";
            } else {
                operateContent += "????????????????????????";
            }
        }

        //??????????????????
        Integer isShowSalePriceOld = bizLeaguerOld.getIsCanSeeSalePrice();
        Integer isShowSalePriceNew = bizLeaguerNew.getIsCanSeeSalePrice();
        Boolean isUpdateShowSalePrice = !isShowSalePriceOld.equals(isShowSalePriceNew);
        if (isUpdateShowSalePrice) {
            updateFieldCount++;
            if (updateFieldCount != 1) {
                operateContent += ";";
            }
            if (isShowSalePriceNew.equals(1)) {
                operateContent += "??????????????????";
            } else {
                operateContent += "?????????????????????";
            }
        }

        //??????????????????/?????????
        Integer isShowTradeOld = bizLeaguerOld.getIsCanSeeTradePrice();
        Integer isShowTradePriceNew = bizLeaguerNew.getIsCanSeeTradePrice();
        Boolean isUpdateShowTradePrice = !isShowTradeOld.equals(isShowTradePriceNew);
        if (isUpdateShowTradePrice) {
            updateFieldCount++;
            if (updateFieldCount != 1) {
                operateContent += ";";
            }
            if (isShowTradePriceNew.equals(1)) {
                operateContent += "??????????????????";
            } else {
                operateContent += "?????????????????????";
            }
        }

        paramAddShpOperateLog.setOperateContent(operateContent);
        if (updateFieldCount > 0) {
            shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
        }
    }


    /**
     * ????????????(????????????)
     *
     * @param params ????????????
     * @return Result
     */
    @ApiOperation(
            value = "????????????;(????????????)",
            notes = "????????????;(????????????)",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "????????????;????????????"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "??????token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "leaguerShopId", value = "????????????id"),
    })
    @RequestRequire
    @RequestMapping("/deleteLeaguer")
    public BaseResult deleteLeaguer(@RequestParam Map<String, String> params,
                                    @Valid ParamDeleteLeaguer deleteLeaguer, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        boolean newVersion = newAndroidAndIOSVersion("2.1.0", "2.2.1");
        //???????????????,??????????????????
        if (newVersion) {
            boolean leaguerAddPerm = hasPermWithCurrentUser(ConstantPermission.MOD_LEAGUER_DELETE);
            if (!leaguerAddPerm) {
                return BaseResult.errorResult(EnumCode.ERROR_NO_SHOP_PERMISSION);
            }
        }
        String leaguerShopIdStr = deleteLeaguer.getLeaguerShopId();
        int leaguerShopId = Integer.parseInt(leaguerShopIdStr);
        bizLeaguerService.deleteBizLeaguer(getUserId(), getShopId(), leaguerShopId, request);
        return BaseResult.okResult();
    }


    /**
     * ????????????????????????
     *
     * @param productQuery ????????????
     * @return Result
     */
    @ApiOperation(
            value = "????????????????????????;",
            notes = "????????????????????????;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "????????????;????????????")
    })
    @RequestRequire
    @RequestMapping("/listLeaguerProduct")
    @RequiresPermissions("leaguer:check:shop")
    public BaseResult<List<VoLeaguerProduct>> listLeaguerProduct(
            @Valid ParamLeaguerProductQuery productQuery, BindingResult result) {
        servicesUtil.validControllerParam(result);
        formatLeaguerQueryParam(productQuery);
        List<VoLeaguerProduct> listLeaPro = bizLeaguerService.listBizLeaguerProductByShopIds(productQuery);
        return LocalUtils.getBaseResult(listLeaPro);
    }


    /**
     * ??????????????????????????????????????????
     *
     * @param params ????????????
     * @return Result
     */
    @ApiOperation(
            value = "?????????????????????????????????????????? --2.6.4??? --zs;",
            notes = "??????????????????????????????????????????--2.6.4??? --zs;",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "????????????;????????????")
    })
    @RequestRequire
    @GetMapping("/getLeaguerProductDetail")
    public BaseResult<VoLeaguerProduct> getLeaguerProductDetail(
            @RequestParam Map<String, String> params, @Valid ParamProductBizId proDetail, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String bizId = proDetail.getBizId();
        Integer leaguerShopId = proProductService.getShopIdByBizId(bizId);
        Integer shopId = getShopId();
        VoLeaguerProduct leaProDetail = proProductService.getBizLeaguerProductDetailByBizId(bizId, leaguerShopId, shopId, proDetail.getType());
        if (LocalUtils.isEmptyAndNull(leaProDetail)) {
            return BaseResult.defaultErrorWithMsg("???????????????!");
        }
        //????????????????????????????????????
        if ("union".equals(proDetail.getType()) && "1".equals(getMemberState())) {
            // =============????????????????????????????????????;?????????2021-11-05 22:18:10
            String accessLimitNumKey = "shp:shop_union:accessUser:limitNum";
            String accessLimitNumValue = redisUtil.get(accessLimitNumKey);
            String shopUnionShopTempKey = ConstantRedisKey.getShopUnionAppUserTemp(getUsername());
            String userAccessLimit = redisUtil.get(shopUnionShopTempKey);
            userAccessLimit = LocalUtils.isEmptyAndNull(userAccessLimit) ? "0: " : userAccessLimit;
            String[] split = userAccessLimit.split(":");
            //??????????????????
            int userAccessLimitInt = Integer.parseInt(split[0]);
            if (userAccessLimitInt >= Integer.parseInt(accessLimitNumValue)) {
                return BaseResult.defaultErrorWithMsg("?????????????????????????????????????????????");
            }
            //???????????????????????????????????????
            String accessProduct = split[1];
            if (!accessProduct.contains(bizId)) {
                accessProduct += "," + bizId;
                redisUtil.set(shopUnionShopTempKey, (++userAccessLimitInt) + ":" + accessProduct);
            }
            //========================End==================
        }


        String productImg = leaProDetail.getProductImg();
        if (!LocalUtils.isEmptyAndNull(productImg)) {
            String[] productImgArray = productImg.split(";");
            for (int i = 0; i < productImgArray.length; i++) {
                if (!productImgArray[i].contains("http")) {
                    productImgArray[i] = servicesUtil.formatImgUrl(productImgArray[i], false);
                }
            }
            leaProDetail.setProductImgList(productImgArray);
            leaProDetail.setProductImg(null);
        }

        String videoUrl = leaProDetail.getVideoUrl();
        leaProDetail.setVideoUrl(servicesUtil.formatImgUrl(videoUrl));


        //????????????????????????
        List<VoShpWechat> voShpWechatList = shpWechatService.listShpWechat(leaProDetail.getShopId());
        try {
            //???????????????????????????????????????????????????2.6.2???????????????
            BasicParam basicParam = getBasicParam();
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.6.2") >= 0) {
                VoShpWechat voShpWechat = shpWechatService.addByShopId(leaProDetail.getShopId(), getUserId());
                if (voShpWechat != null) {
                    voShpWechatList.add(0, voShpWechat);
                }
            }
        } catch (Exception e) {
            throw new MyException("??????????????????????????????" + e);
        }
        leaProDetail.setVoShpWechatList(voShpWechatList);
        ParamShopSeeAdd shopSeeAdd = new ParamShopSeeAdd();
        shopSeeAdd.setShopId(getShopId());
        shopSeeAdd.setUserId(getUserId());
        shopSeeAdd.setBeSeenShopId(leaguerShopId);
        bizShopSeeService.saveShopSee(shopSeeAdd);
        //??????????????????????????????????????????
        String leaguerFriendState = bizLeaguerService.getLeaguerFriendState(getShopId(), leaguerShopId);

        leaProDetail.setLeaguerFriendState(leaguerFriendState);
        ParamClassifyTypeSearch classifyTypeSearch = new ParamClassifyTypeSearch();
//            Integer productId = proProductService.getShopIdByBizId(vo.getBizId());
        classifyTypeSearch.setBizId(leaProDetail.getBizId());
        classifyTypeSearch.setShopId(leaProDetail.getShopId());
        classifyTypeSearch.setProductId(leaProDetail.getProId());
        VoClassifyTypeSon classifyTypeSon = proStandardService.getClassifyTypeList(classifyTypeSearch);
        if (!LocalUtils.isEmptyAndNull(classifyTypeSon)) {
            leaProDetail.setClassifyTypeList(classifyTypeSon.getClassifyTypeSonLists());
            if (!LocalUtils.isEmptyAndNull(classifyTypeSon.getPublicPrice()) && !"0".equals(classifyTypeSon.getPublicPrice())) {
                leaProDetail.setPublicPrice("???" + classifyTypeSon.getPublicPrice());
            }
        }
        //??????????????????????????????????????????
        if (!LocalUtils.isEmptyAndNull(proDetail.getType()) && "union".equals(proDetail.getType())) {
            //????????????????????????????????????
            String uPermShowUnionShop = ConstantPermission.SHOW_UNION_UNIONSHOP;
            leaProDetail.setUnionPerm(hasPermToPageWithCurrentUser(uPermShowUnionShop));
        }
        return LocalUtils.getBaseResult(leaProDetail);
    }


    /**
     * ???????????????????????????(????????????)
     *
     * @param params ????????????
     * @return Result
     */
    @ApiOperation(
            value = "???????????????????????????(????????????)----2.6.3???",
            notes = "???????????????????????????(????????????)----2.6.3???",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "????????????;????????????")
    })
    @RequestRequire
    @RequestMapping("/initLeaguerShop")
    public BaseResult initLeaguerShop(@RequestParam Map<String, String> params,
                                      @Valid ParamLeaguerShopId paramLeaguer, BindingResult result) {
        servicesUtil.validControllerParam(result);
        int leaguerShopId = Integer.parseInt(paramLeaguer.getLeaguerShopId());
        VoBizLeaguer voBizLeaguer = bizLeaguerService.getLeaguerShop(getShopId(), leaguerShopId);
        formatVoBizLeaguer(voBizLeaguer);
        try {
            //???????????????????????????????????????????????????2.6.2???????????????
            BasicParam basicParam = getBasicParam();
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.6.2") >= 0) {
                VoShpWechat voShpWechat = shpWechatService.addByShopId(leaguerShopId, getUserId());
                if (voShpWechat != null) {
                    List<VoShpWechat> voShpWechatList = voBizLeaguer.getVoShpWechatList();
                    voShpWechatList.add(0, voShpWechat);
                    voBizLeaguer.setVoShpWechatList(voShpWechatList);

                }
            }
        } catch (Exception e) {
            throw new MyException("??????????????????????????????" + e);
        }
        HashMap<String, Object> hashMap = LocalUtils.getHashMap(voBizLeaguer);
        List<VoProClassify> classifyList = proClassifyService.listLeaguerProClassifyByState(leaguerShopId);
        hashMap.put("classifyList", classifyList);

        return BaseResult.okResult(hashMap);
    }

    /**
     * ??????(??????)???????????????????????????
     *
     * @param params ????????????
     * @return Result
     */
    @ApiOperation(
            value = "???????????????????????????????????????",
            notes = "???????????????????????????????????????",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "????????????;????????????")
    })
    @RequestRequire
    @RequestMapping("/listSpecificLeaguerProduct")
    public BaseResult<List<VoLeaguerProduct>> listSpecificLeaguerProduct(
            @RequestParam Map<String, String> params,
            @Valid ParamSpecificLeaguerProductQuery productQuery, BindingResult result) {
        servicesUtil.validControllerParam(result);
        List<VoLeaguerProduct> listLeaPro = new ArrayList<>();
        int leaguerShopId = Integer.parseInt(productQuery.getLeaguerShopId());
        BizLeaguer leaguer = bizLeaguerService.getBizLeaguerByShopIdAndLeaguerShopId(leaguerShopId, getShopId());
        BizLeaguer leaguerMine = bizLeaguerService.getBizLeaguerByShopIdAndLeaguerShopId(getShopId(), leaguerShopId);


        if (!LocalUtils.isEmptyAndNull(productQuery.getType()) && "union".equals(productQuery.getType())) {
            formatLeaguerQueryParam(productQuery);
            listLeaPro = bizLeaguerService.listSpecificBizUnionProduct(productQuery);
        } else {
            //V2.1.0 ??????????????????????????????,?????????????????????????????????
            //V2.2.0 ????????????????????????????????????????????????????????????????????????????????????
            if (!LocalUtils.isEmptyAndNull(leaguer) && ConstantCommon.ONE.equals(leaguer.getVisible())) {
                //&& ConstantCommon.ONE.equals(""+leaguerMine.getIsWantSeeLeaguerProd())) {
                formatLeaguerQueryParam(productQuery);
                listLeaPro = bizLeaguerService.listSpecificBizLeaguerProduct(productQuery, getShopId());
            }

        }
        return LocalUtils.getBaseResult(listLeaPro);
    }


}
