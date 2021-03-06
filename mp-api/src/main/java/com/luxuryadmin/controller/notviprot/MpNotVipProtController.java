package com.luxuryadmin.controller.notviprot;

import com.github.pagehelper.PageHelper;
import com.luxuryadmin.biz.BizLeaguerBaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.enums.EnumCode;
import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.common.utils.weixxin.mpsdk.WxEntity;
import com.luxuryadmin.common.utils.weixxin.mpsdk.WxService;
import com.luxuryadmin.config.WxPayProperties;
import com.luxuryadmin.enums.EnumIsMember;
import com.luxuryadmin.enums.EnumMemberState;
import com.luxuryadmin.param.adminconfig.ParamAddVipUser;
import com.luxuryadmin.param.adminconfig.ParamAdminConfig;
import com.luxuryadmin.param.api.ParamWxJsCode;
import com.luxuryadmin.param.biz.ParamLeaguerProductQuery;
import com.luxuryadmin.param.common.ParamBasic;
import com.luxuryadmin.param.pay.ParamPay;
import com.luxuryadmin.param.user.ParamUserInfoSave;
import com.luxuryadmin.param.visitor.ParamVisitorRecordList;
import com.luxuryadmin.param.weixin.ParamGetPhone;
import com.luxuryadmin.param.weixin.ParamLogin;
import com.luxuryadmin.service.*;
import com.luxuryadmin.service.biz.BizShopUnionService;
import com.luxuryadmin.util.MessageUtil;
import com.luxuryadmin.vo.adminconfig.VOAdminConfig;
import com.luxuryadmin.vo.biz.VoShopUnionByAppShow;
import com.luxuryadmin.vo.pro.VoLeaguerProduct;
import com.luxuryadmin.vo.user.VOAddVipUserList;
import com.luxuryadmin.vo.user.VOUserInfo;
import com.luxuryadmin.vo.visitor.VOVisitorRecord;
import com.luxuryadmin.vo.wx.VOLogin;
import com.luxuryadmin.vo.wx.VOWeixinPhoneDecryptInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * banner??? controller
 *
 * @author zhangsai
 * @Date 2021-11-24 13:56:01
 */
@Slf4j
@RestController
@RequestMapping(value = "/notVipProt")
@Api(tags = "MP.???????????????????????????????????????", description = "??????????????????????????????????????? ")
public class MpNotVipProtController extends BizLeaguerBaseController {

    @Autowired
    private AdminConfigService adminConfigService;

    @Autowired
    private MpPayOrderService mpPayOrderService;

    @Autowired
    private MpBannerService mpBannerService;

    @Autowired
    private WeiXinLoginService weiXinLoginService;

    @Autowired
    private BizShopUnionService bizShopUnionService;

    @Autowired
    private WxService wxService;

    @Autowired
    private WxPayProperties wxPayProperties;

    @Autowired
    private WeiXinPayService weiXinPayService;

    @Autowired
    private MpUserService mpUserService;

    @Autowired
    private BasicsService basicsService;

    @Autowired
    private MpVisitorRecordService mpVisitorRecordService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private TokenService tokenService;

    @ApiOperation(value = "??????banner??????", httpMethod = "GET")
    @GetMapping("/listBannerInfo")
    public BaseResult listBannerInfo() {
        VoShopUnionByAppShow vos = mpBannerService.listBannerInfo();
        return BaseResult.okResult(vos);
    }

    @PostMapping("/wxLogin")
    @ApiOperation(value = "?????????????????????", httpMethod = "POST")
    public BaseResult wxLogin(@Valid ParamLogin paramLogin, BindingResult result) {
        servicesUtil.validControllerParam(result);
        VOLogin login = weiXinLoginService.wxLogin(paramLogin);
        return BaseResult.okResult(login);
    }

    @PostMapping("/getPhoneInfo")
    @ApiOperation(value = "??????????????????????????????(??????????????????)", httpMethod = "POST")
    public BaseResult getPhoneInfo(@Valid ParamGetPhone param, HttpServletRequest request, BindingResult result) {
        servicesUtil.validControllerParam(result);
        VOWeixinPhoneDecryptInfo vo = weiXinLoginService.getPhoneInfo(param, request);
        return BaseResult.okResult(vo);
    }


    /**
     * ??????????????????
     *
     * @return Result
     */
    @PostMapping("/exitLogin")
    @ApiOperation(value = "?????????????????????", httpMethod = "POST")
    public BaseResult exitLogin() {
        mpUserService.exitLogin();
        return BaseResult.okResult();
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
        if (StringUtil.isNotBlank(productQuery.getProName())) {
            Integer userId = basicsService.getUserId();
            if (userId == null) {
                throw new MyException(EnumCode.ERROR_TOKEN_DISABLE);
            }
            VOUserInfo userInfo = mpUserService.getUserInfo();
            if (EnumMemberState.mistake_vip.getCode().equals(userInfo.getMemberState())) {
                throw new MyException(EnumCode.VIP_PAST);
            }
        }
        servicesUtil.validControllerParam(result);
        String expireFlag = redisUtil.get("shp:shop_union:expire:flag");
        //flag?????????,?????????????????????
        if (Integer.parseInt(expireFlag) % 2 == 0) {
            validateSign(getRequest());
        }
        VOUserInfo userInfo = mpUserService.getUserInfo();
        formatLeaguerQueryParam(productQuery);
        List<VoLeaguerProduct> listLeaPro = bizShopUnionService.listUnionProductNoPage(productQuery);
        if (!LocalUtils.isEmptyAndNull(listLeaPro)) {
            DecimalFormat df = new DecimalFormat(",##0.##");
            for (VoLeaguerProduct vp : listLeaPro) {
                new BigDecimal(vp.getTradePrice());
                if (userInfo == null || EnumIsMember.NO.getCode().equals(userInfo.getIsMember())) {
                    vp.setShopName(null);
                }
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
            //1.code????????????????????????????????????openId
            wxEntity = wxService.code2Session(param.getJsCode(), wxPayProperties.getAppid(), wxPayProperties.getSecret());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return BaseResult.defaultErrorWithMsg(e.getMessage());
        }
        return BaseResult.okResult(wxEntity);
    }

    @ApiOperation(value = "??????", httpMethod = "POST")
    @PostMapping("/wxMpPay")
    public BaseResult wxMpPay(@Valid ParamPay paramPay, BindingResult result) {
        servicesUtil.validControllerParam(result);
        Map<String, Object> resMap = weiXinPayService.wxMpPay(paramPay);
        return BaseResult.okResult(resMap);
    }

    @PostMapping("/createOrder")
    @ApiOperation(value = "???????????????????????????????????????", httpMethod = "POST")
    public BaseResult getAwardRecord() {
        String orderNo = mpPayOrderService.createOrder();
        return BaseResult.okResult(orderNo);
    }


    @PostMapping("/saveUserInfo")
    @ApiOperation(value = "??????????????????", httpMethod = "POST")
    public BaseResult saveUserInfo(@Valid ParamUserInfoSave param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        mpUserService.saveUserInfo(param);
        return BaseResult.okResult();
    }

    @GetMapping("/getUserInfo")
    @ApiOperation(value = "??????????????????", httpMethod = "GET")
    public BaseResult getUserInfo() {
        VOUserInfo voUserInfo = mpUserService.getUserInfo();
        return BaseResult.okResult(voUserInfo);
    }


    //--------------------------------???????????????----------------------------------------------------------------------------
    @GetMapping("/getVisitorRecord")
    @ApiOperation(value = "????????????????????????", httpMethod = "GET")
    public BaseResult getVisitorRecord(@Valid ParamVisitorRecordList param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        PageHelper.startPage(getPageNum(), getPageSize());
        VOVisitorRecord vo = mpVisitorRecordService.getVisitorRecord(param);
        return BaseResult.okResult(vo);
    }


    @GetMapping("/updateConfig")
    @ApiOperation(value = "??????????????????", httpMethod = "GET")
    public BaseResult updateConfig(@Valid ParamAdminConfig param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        adminConfigService.updateConfig(param);
        return BaseResult.okResult();
    }

    @GetMapping("/getConfig")
    @ApiOperation(value = "??????????????????", httpMethod = "GET")
    public BaseResult getConfig() {
        VOAdminConfig vo = adminConfigService.getConfig();
        return BaseResult.okResult(vo);
    }

    @PostMapping("/addVipUser")
    @ApiOperation(value = "??????????????????", httpMethod = "POST")
    public BaseResult addVipUser(@Valid ParamAddVipUser param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        adminConfigService.addVipUser(param);
        return BaseResult.okResult();
    }

    @GetMapping("/listAddVipUser")
    @ApiOperation(value = "????????????????????????", httpMethod = "GET")
    public BaseResult listAddVipUser(@Valid ParamBasic param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        PageHelper.startPage(getPageNum(), getPageSize());
        List<VOAddVipUserList> vo = mpUserService.listAddVipUser(param);
        return LocalUtils.getBaseResult(vo);
    }

    @RequestMapping("/testWXService")
    @Transactional(rollbackFor = Exception.class)
    public void testWXService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean isGet = request.getMethod().toLowerCase().equals("get");
        serviceService.wxMpService(isGet,request,response);

    }

}
