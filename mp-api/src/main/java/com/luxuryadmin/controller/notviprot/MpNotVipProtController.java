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
 * banner表 controller
 *
 * @author zhangsai
 * @Date 2021-11-24 13:56:01
 */
@Slf4j
@RestController
@RequestMapping(value = "/notVipProt")
@Api(tags = "MP.不需要登录会员或者登录接口", description = "不需要登录会员或者登录接口 ")
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

    @ApiOperation(value = "获取banner信息", httpMethod = "GET")
    @GetMapping("/listBannerInfo")
    public BaseResult listBannerInfo() {
        VoShopUnionByAppShow vos = mpBannerService.listBannerInfo();
        return BaseResult.okResult(vos);
    }

    @PostMapping("/wxLogin")
    @ApiOperation(value = "微信第三方登录", httpMethod = "POST")
    public BaseResult wxLogin(@Valid ParamLogin paramLogin, BindingResult result) {
        servicesUtil.validControllerParam(result);
        VOLogin login = weiXinLoginService.wxLogin(paramLogin);
        return BaseResult.okResult(login);
    }

    @PostMapping("/getPhoneInfo")
    @ApiOperation(value = "微信第三方获取手机号(本小程序登录)", httpMethod = "POST")
    public BaseResult getPhoneInfo(@Valid ParamGetPhone param, HttpServletRequest request, BindingResult result) {
        servicesUtil.validControllerParam(result);
        VOWeixinPhoneDecryptInfo vo = weiXinLoginService.getPhoneInfo(param, request);
        return BaseResult.okResult(vo);
    }


    /**
     * 用户退出登录
     *
     * @return Result
     */
    @PostMapping("/exitLogin")
    @ApiOperation(value = "微信第三方登录", httpMethod = "POST")
    public BaseResult exitLogin() {
        mpUserService.exitLogin();
        return BaseResult.okResult();
    }


    /**
     * 加载所有联盟商品<br/>
     * 根据timestamp来决定是否失效;
     *
     * @param productQuery 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "联盟商品--店铺联盟 --2.6.1;",
            notes = "联盟商品--店铺联盟 --2.6.1;",
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
        //flag偶数时,才进行签名校验
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
                vp.setReleaseTime("更新时间：" + vp.getReleaseTime());
            }
        }
        return LocalUtils.getBaseResult(listLeaPro);
    }


    /**
     * 校验端上的签名;
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
            //md5加盐编码
            String sign = DESEncrypt.md5Hex(paramString + "union_H5");
            log.info("====签名===: " + sign);
            String paramSign = httpServletRequest.getParameter(signStr);
            if (!sign.equals(paramSign)) {
                throw new MyException(EnumCode.ERROR_SIGN);
            }
        } else {
            throw new MyException(EnumCode.ERROR_SIGN);
        }
    }


    /**
     * 获取sessionKey等相关信息
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "获取sessionKey等相关信息",
            notes = "获取sessionKey等相关信息;",
            httpMethod = "GET")
    @RequestMapping("/getSessionKey")
    public BaseResult getSessionKey(
            @Valid ParamWxJsCode param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        WxEntity wxEntity;
        try {
            //1.code先调用微信接口查该用户的openId
            wxEntity = wxService.code2Session(param.getJsCode(), wxPayProperties.getAppid(), wxPayProperties.getSecret());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return BaseResult.defaultErrorWithMsg(e.getMessage());
        }
        return BaseResult.okResult(wxEntity);
    }

    @ApiOperation(value = "支付", httpMethod = "POST")
    @PostMapping("/wxMpPay")
    public BaseResult wxMpPay(@Valid ParamPay paramPay, BindingResult result) {
        servicesUtil.validControllerParam(result);
        Map<String, Object> resMap = weiXinPayService.wxMpPay(paramPay);
        return BaseResult.okResult(resMap);
    }

    @PostMapping("/createOrder")
    @ApiOperation(value = "创建鲍杨小程序购买会员订单", httpMethod = "POST")
    public BaseResult getAwardRecord() {
        String orderNo = mpPayOrderService.createOrder();
        return BaseResult.okResult(orderNo);
    }


    @PostMapping("/saveUserInfo")
    @ApiOperation(value = "保存用户信息", httpMethod = "POST")
    public BaseResult saveUserInfo(@Valid ParamUserInfoSave param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        mpUserService.saveUserInfo(param);
        return BaseResult.okResult();
    }

    @GetMapping("/getUserInfo")
    @ApiOperation(value = "获取用户信息", httpMethod = "GET")
    public BaseResult getUserInfo() {
        VOUserInfo voUserInfo = mpUserService.getUserInfo();
        return BaseResult.okResult(voUserInfo);
    }


    //--------------------------------管理员接口----------------------------------------------------------------------------
    @GetMapping("/getVisitorRecord")
    @ApiOperation(value = "后台查询访客记录", httpMethod = "GET")
    public BaseResult getVisitorRecord(@Valid ParamVisitorRecordList param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        PageHelper.startPage(getPageNum(), getPageSize());
        VOVisitorRecord vo = mpVisitorRecordService.getVisitorRecord(param);
        return BaseResult.okResult(vo);
    }


    @GetMapping("/updateConfig")
    @ApiOperation(value = "修改配置时间", httpMethod = "GET")
    public BaseResult updateConfig(@Valid ParamAdminConfig param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        adminConfigService.updateConfig(param);
        return BaseResult.okResult();
    }

    @GetMapping("/getConfig")
    @ApiOperation(value = "获取配置时间", httpMethod = "GET")
    public BaseResult getConfig() {
        VOAdminConfig vo = adminConfigService.getConfig();
        return BaseResult.okResult(vo);
    }

    @PostMapping("/addVipUser")
    @ApiOperation(value = "添加会员用户", httpMethod = "POST")
    public BaseResult addVipUser(@Valid ParamAddVipUser param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        adminConfigService.addVipUser(param);
        return BaseResult.okResult();
    }

    @GetMapping("/listAddVipUser")
    @ApiOperation(value = "获取添加用户信息", httpMethod = "GET")
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
