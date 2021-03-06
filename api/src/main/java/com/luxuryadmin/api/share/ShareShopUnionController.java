package com.luxuryadmin.api.share;

import com.luxuryadmin.api.biz.BizLeaguerBaseController;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.enums.EnumCode;
import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ThreadUtils;
import com.luxuryadmin.common.utils.aliyun.OSSUtil;
import com.luxuryadmin.common.utils.weixxin.mpsdk.WxEntity;
import com.luxuryadmin.common.utils.weixxin.mpsdk.WxService;
import com.luxuryadmin.entity.org.OrgOrganization;
import com.luxuryadmin.entity.shp.ShpShop;
import com.luxuryadmin.param.api.ParamWxData;
import com.luxuryadmin.param.api.ParamWxJsCode;
import com.luxuryadmin.param.api.ParamWxUserPhone;
import com.luxuryadmin.param.biz.*;
import com.luxuryadmin.param.common.ParamBasic;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.param.ord.ParamShareReceiptQuery;
import com.luxuryadmin.param.ord.ParamShareServiceQuery;
import com.luxuryadmin.param.oss.ParamImg;
import com.luxuryadmin.param.pro.ParamProductBizId;
import com.luxuryadmin.param.pro.ParamShareSeeUserAdd;
import com.luxuryadmin.param.pro.ParamUnionBizId;
import com.luxuryadmin.service.biz.BizShopUnionService;
import com.luxuryadmin.service.ord.OrdShareReceiptService;
import com.luxuryadmin.service.pro.ProShareSeeUserAgencyService;
import com.luxuryadmin.service.pro.ProShareSeeUserService;
import com.luxuryadmin.service.shp.ShpServiceService;
import com.luxuryadmin.service.shp.ShpShopService;
import com.luxuryadmin.service.shp.ShpWechatService;
import com.luxuryadmin.vo.biz.VoShopUnionByAppShow;
import com.luxuryadmin.vo.biz.VoShopUnionForAdminPage;
import com.luxuryadmin.vo.ord.VoShareReceipt;
import com.luxuryadmin.vo.pro.VoLeaguerProduct;
import com.luxuryadmin.vo.pro.VoProShareSeeUserAgency;
import com.luxuryadmin.vo.shp.VoShpServiceRecordDetail;
import com.luxuryadmin.vo.shp.VoShpWechat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author sanjin145
 * @date 2020-09-22
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/share", method = RequestMethod.GET)
@Api(tags = {"G002.1.???????????????????????? --2.6.1"}, description = "/shop/share | ??????????????????,???????????????")
public class ShareShopUnionController extends BizLeaguerBaseController {

    @Autowired
    private BizShopUnionService bizShopUnionService;

    @Autowired
    private ShpWechatService shpWechatService;

    @Autowired
    private ShpShopService shpShopService;

    @Autowired
    private WxService wxService;

    @Autowired
    private ProShareSeeUserService proShareSeeUserService;

    @Autowired
    private ProShareSeeUserAgencyService proShareSeeUserAgencyService;


    @ApiOperation(
            value = "???????????? --?????? --2.6.1 --zs;",
            notes = "???????????? --?????? --2.6.1 --zs;",
            httpMethod = "POST")
    @PostMapping("/addShopUnion")
    public BaseResult addShopUnion(@Valid ParamShopUnionAdd shopUnionAdd, BindingResult result) {
        servicesUtil.validControllerParam(result);
        bizShopUnionService.addShopUnion(shopUnionAdd);
        return BaseResult.okResult();
    }

    @ApiOperation(
            value = "???????????? --???????????? --2.6.1 --zs;",
            notes = "???????????? --???????????? --2.6.1 --zs;",
            httpMethod = "POST")
    @PostMapping("/getShopUnionForAdminPage")
    public BaseResult<VoShopUnionForAdminPage> getShopUnionForAdminPage(@Valid ParamShopUnionForAdminBySearch shopUnionForAdminBySearch, BindingResult result) {
        servicesUtil.validControllerParam(result);
        VoShopUnionForAdminPage recommendAdminPage = bizShopUnionService.getShopUnionForAdminPage(shopUnionForAdminBySearch);
        return BaseResult.okResult(recommendAdminPage);
    }


    /**
     * ????????????--????????????;
     */
    @ApiOperation(
            value = "????????????--???????????? --2.6.1 --zs;",
            notes = "????????????--???????????? --2.6.1 --zs;",
            httpMethod = "POST")
    @PostMapping("/initShopUnionH5")
    public BaseResult<VoShopUnionByAppShow> initShopUnionH5(@Valid ParamBasic basic, BindingResult result) {
        servicesUtil.validControllerParam(result);
        VoShopUnionByAppShow shopUnionByAppShow = bizShopUnionService.getShopUnionByAppShow(10000);
        shopUnionByAppShow.setProductTotalNum(null);
        shopUnionByAppShow.setProductTotalPrice(null);
        return BaseResult.okResult(shopUnionByAppShow);
    }

    /**
     * ????????????????????????<br/>
     * ??????timestamp?????????????????????;
     *
     * @param productQuery ????????????
     * @return Result
     */
    @ApiOperation(
            value = "????????????--???????????? --2.6.1;",
            notes = "????????????--???????????? --2.6.1;",
            httpMethod = "POST")
    @PostMapping("/loadShopUnionProductH5")
    public BaseResult<List<VoLeaguerProduct>> loadShopUnionProductH5(
            @Valid ParamLeaguerProductQuery productQuery, BindingResult result) throws Exception {
        servicesUtil.validControllerParam(result);
        String expireFlag = redisUtil.get("shp:shop_union:expire:flag");

        //flag?????????,?????????????????????
        if (Integer.parseInt(expireFlag) % 2 == 0) {
            validateSign(getRequest());
        }
        //?????????2021-11-05 21:27:50; ?????????????????????,?????????????????????30???????????????;?????????????????????;
        //String timestamp = productQuery.getTimestamp();
        //try {
        //    //v262???????????????1?????????????????????;?????????????????????????????????
        //    Date expireDate = new Date(Long.parseLong(timestamp));
        //    Date now = new Date();
        //    String flag = productQuery.getFlag();
        //    if (now.after(expireDate) || !expireFlag.equals(flag)) {
        //        //??????????????????;
        //        return BaseResult.errorResult(EnumCode.ERROR_MP_EXPIRED);
        //    }
        //} catch (Exception e) {
        //    return BaseResult.defaultErrorWithMsg("[tp]????????????");
        //}
        formatLeaguerQueryParam(productQuery);
        List<VoLeaguerProduct> listLeaPro = bizShopUnionService.listUnionProductNoPage(productQuery);
        if (!LocalUtils.isEmptyAndNull(listLeaPro)) {
            DecimalFormat df = new DecimalFormat(",##0.##");
            for (VoLeaguerProduct vp : listLeaPro) {
                new BigDecimal(vp.getTradePrice());
                vp.setTradePrice(df.format(LocalUtils.calcNumber(vp.getTradePrice(), "*", 0.01)));
                vp.setReleaseTime("???????????????" + vp.getReleaseTime());
            }
        }
        return LocalUtils.getBaseResult(listLeaPro);
    }


    /**
     * ?????????????????????;
     *
     * @param httpServletRequest
     */
    private void validateSign(HttpServletRequest httpServletRequest) {
        Map<String, String[]> map = httpServletRequest.getParameterMap();
        List<String> keyList = new ArrayList<String>(map.keySet());
        String signStr = "sign";
        if (keyList.contains(signStr)) {
            keyList.remove(signStr);
            Collections.sort(keyList);
            String paramString = "";
            for (String key : keyList) {
                paramString += (key + "=" + httpServletRequest.getParameter(key) + "&");
            }
            paramString = paramString.substring(0, paramString.length() - 1);
            log.info(paramString);
            //md5????????????
            String sign = DESEncrypt.md5Hex(paramString + "union_H5");
            log.info("====??????===: " + sign);
            String paramSign = httpServletRequest.getParameter(signStr);
            if (!sign.equals(paramSign)) {
                throw new MyException(EnumCode.ERROR_SIGN);
            }
        } else {
            throw new MyException(EnumCode.ERROR_SIGN);
        }
    }


    /**
     * ????????????????????????<br/>
     * ???????????????????????????2021-11-05 22:02:30;
     * ????????????bizId????????????;????????????30???????????????;????????????;
     *
     * @return Result
     */
    @ApiOperation(
            value = "????????????????????????;",
            notes = "????????????????????????;",
            httpMethod = "GET")
    @RequestRequire
    @GetMapping("/getUnionProductDetail")
    public BaseResult<VoLeaguerProduct> getUnionProductDetail(@Valid ParamUnionBizId unionBizId, BindingResult result) throws Exception {
        servicesUtil.validControllerParam(result);
        String bizId = unionBizId.getBizId();
        Integer leaguerShopId = Integer.parseInt(unionBizId.getShopId());
        VoLeaguerProduct leaProDetail = proProductService.getUnionProductDetailByBizId(bizId, leaguerShopId);
        if (LocalUtils.isEmptyAndNull(leaProDetail)) {
            return BaseResult.okResultNoData();
        }
        // =============????????????????????????????????????;?????????2021-11-05 22:18:10
        String expireMsg = redisUtil.get("shp:shop_union:expire:msg");
        String userPhone = unionBizId.getUserPhone();
        String accessLimitNumKey = "shp:shop_union:accessUser:limitNum";
        String accessLimitNumValue = redisUtil.get(accessLimitNumKey);
        String userPhoneKey = "shp:shop_union:accessUser:" + userPhone;
        String userAccessLimit = redisUtil.get(userPhoneKey);
        userAccessLimit = LocalUtils.isEmptyAndNull(userAccessLimit) ? "0: " : userAccessLimit;
        String[] split = userAccessLimit.split(":");
        //??????????????????
        int userAccessLimitInt = Integer.parseInt(split[0]);
        if (LocalUtils.isEmptyAndNull(userPhone)
                || (userAccessLimitInt >= Integer.parseInt(accessLimitNumValue))) {
            EnumCode.ERROR_MP_LIMIT.setMessage(expireMsg);
            return BaseResult.errorResult(EnumCode.ERROR_MP_LIMIT);
        }
        //???????????????????????????????????????
        String accessProduct = split[1];
        if (!accessProduct.contains(bizId)) {
            accessProduct += "," + bizId;
            redisUtil.set(userPhoneKey, (++userAccessLimitInt) + ":" + accessProduct);
        }
        //========================End==================


        new BigDecimal(leaProDetail.getTradePrice());
        DecimalFormat df = new DecimalFormat(",##0.##");
        leaProDetail.setTradePrice(df.format(LocalUtils.calcNumber(leaProDetail.getTradePrice(), "*", 0.01)));
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
        //??????????????????????????????,???????????????????????????????????????2021-11-05 22:02:11
        //if (LocalUtils.isEmptyAndNull(voShpWechatList)) {
        //    ShpShop shpShop = shpShopService.getShpShopById(unionBizId.getShopId());
        //    voShpWechatList = new ArrayList<>();
        //    VoShpWechat sw = new VoShpWechat();
        //    sw.setContactPersonName("?????????");
        //    sw.setContactPersonWechat(shpShop.getContact());
        //    voShpWechatList.add(sw);
        //}
        leaProDetail.setVoShpWechatList(voShpWechatList);
        return LocalUtils.getBaseResult(leaProDetail);
    }


    /**
     * ??????sessionKey???????????????
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "??????sessionKey???????????????",
            notes = "??????sessionKey???????????????;",
            httpMethod = "GET")
    @RequestMapping("/getSessionKey")
    public BaseResult getSessionKey(
            @Valid ParamWxJsCode param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        WxEntity wxEntity;
        try {
            String appId = "wx6a22abceb44f08e5";
            String appSecret = "0e424bf1f031bbc44e49a92a591de42f";
            //1.code????????????????????????????????????openId
            wxEntity = wxService.code2Session(param.getJsCode(), appId, appSecret);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return BaseResult.defaultErrorWithMsg(e.getMessage());
        }
        return BaseResult.okResult(wxEntity);
    }


    /**
     * ??????????????????????????????
     *
     * @return Result
     */
    @ApiOperation(
            value = "??????????????????????????????",
            notes = "??????????????????????????????",
            httpMethod = "POST")
    @PostMapping("/saveMpAccessUser")
    public BaseResult saveMpAccessUser(
            @Valid ParamShareSeeUserAdd param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String timestamp = param.getTimestamp();
        String userId = param.getUserId();
        servicesUtil.validControllerParam(result);
        param.setIp(getIpAddr());
        String openId = param.getOpenId();
        param.setOpenId(openId);
        param.setUserId(userId);
        param.setShareBatch(timestamp);
        proShareSeeUserService.addShareSeeUserForUnion(param);
        return BaseResult.okResult();
    }


    /**
     * ?????????????????????????????????
     *
     * @return Result
     */
    @ApiOperation(
            value = "?????????????????????????????????",
            notes = "?????????????????????????????????",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "seeUserId", value = "?????????????????????id")
    })
    @PostMapping("/saveMpAccessUserPhone")
    public BaseResult saveMpAccessUserPhone(
            @Valid ParamWxUserPhone param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        servicesUtil.validControllerParam(result);
        String iv = param.getIv();
        String encryptedData = param.getEncryptedData();
        String sessionKey = param.getSessionKey();
        String userPhone = "";
        try {
            //1.code????????????????????????????????????openId
            userPhone = wxService.getUserPhone(iv, encryptedData, sessionKey);
            log.info("========???????????????: {}", userPhone);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return BaseResult.defaultErrorWithMsg("???????????????????????????: " + e.getMessage());
        }
        return LocalUtils.getBaseResult(userPhone);
    }


    /**
     * ?????????????????????????????????
     *
     * @return Result
     */
    @ApiOperation(
            value = "?????????????????????????????????",
            notes = "?????????????????????????????????",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "flagId", value = "?????????????????????id")
    })
    @GetMapping("/loadMpAccessAgency")
    public BaseResult loadMpAccessAgency() {
        String flagId = getParameter("flagId");
        List<VoProShareSeeUserAgency> list = proShareSeeUserAgencyService.listVoProShareSeeUserAgency(flagId);
        return LocalUtils.getBaseResult(list);
    }

}