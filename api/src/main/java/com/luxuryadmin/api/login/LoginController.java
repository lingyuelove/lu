package com.luxuryadmin.api.login;

import com.alibaba.fastjson.JSONObject;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.base.BasicParam;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.constant.ConstantWeChat;
import com.luxuryadmin.common.constant.enums.EnumCode;
import com.luxuryadmin.common.encrypt.PBKDF2Util;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.*;
import com.luxuryadmin.entity.shp.ShpBindCount;
import com.luxuryadmin.entity.shp.ShpUserToken;
import com.luxuryadmin.enums.login.EnumOtherLoginType;
import com.luxuryadmin.enums.login.EnumSendSmsType;
import com.luxuryadmin.param.login.*;
import com.luxuryadmin.service.shp.ShpBindCountService;
import com.luxuryadmin.service.shp.ShpUserService;
import com.luxuryadmin.service.shp.ShpUserTokenService;
import com.luxuryadmin.service.shp.impl.ShpUserServiceImpl;
import com.luxuryadmin.vo.shp.VoShpUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

/**
 * 登录注册Controller
 *
 * @author monkey king
 * @Date 2019/12/01 4:07
 */
@Slf4j
@RestController
@RequestMapping(value = "/login", method = RequestMethod.POST)
@Api(tags = "A001.【登录注册】模块", description = "/login |登录注册发短信验证码接口 ")
public class LoginController extends BaseController {

    @Autowired
    private ShpUserService shpUserService;

    @Autowired
    private ShpBindCountService shpBindCountService;

    @Autowired
    private ShpUserTokenService shpUserTokenService;

    /**
     * 注册成功后还未设置密码
     */
    private static final String NO_SET_PASSWORD = "register_no_set_password_";

    /**
     * 店铺用户登录接口--帐号密码登录
     *
     * @return Result
     */
    @RequestRequire
    @ApiOperation(
            value = "1.用户帐号密码登录接口",
            notes = "用户登录接口--帐号密码登录;<br/>登录成功返回token;",
            httpMethod = "POST")
    @RequestMapping("/pwdLogin")
    public BaseResult pwdLogin(@Valid ParamLoginPwd loginPwd, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String username = loginPwd.getUsername();
        String password = loginPwd.getPassword();
        BaseResult baseResult = shpUserService.shpPwdLogin(username, password);

        saveIp(baseResult, getBasicParam());
        return baseResult;
    }

    /**
     * 店铺用户登录接口--短信验证码登录
     *
     * @return Result
     */
    @RequestRequire
    @ApiOperation(
            value = "2.短信验证码登录接口",
            notes = "用户登录接口--短信验证码登录;<br/>登录成功返回token;",
            httpMethod = "POST")
    @RequestMapping("/smsLogin")
    public BaseResult smsLogin(@Valid ParamLoginSms loginSms, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String username = loginSms.getUsername();
        String smsCode = loginSms.getSmsCode();

//        //苹果审核用手机号，验证码传入的是密码，调用密码登录。
//        if ("18434363494".equals(username)) {
//            log.error("*********** 【苹果审核用手机号验证码登录】1: *****************");
//            BaseResult baseResult = shpUserService.shpLogin(username, smsCode);
//            if (ConstantCommon.OK.equals(baseResult.getCode())) {
//                //清除验证码redisKey和验证码剩余时间redisKey
//                updateAppVersion(baseResult, params);
//            }
//            return baseResult;
//        }
        //验证码类型;
        EnumSendSmsType sendSmsType = EnumSendSmsType.LOGIN;
        //redis的短信验证码
        String smsCodeRedisKey = ConstantRedisKey.getYzmSmsCodeKey(sendSmsType.getCode(), username);
        String smsCodeRedisValue = redisUtil.get(smsCodeRedisKey);
        //端上短信验证码是否和服务器上的一致
        servicesUtil.validateServerSmsCode(username, smsCode, sendSmsType.getCode());
        //进行登录
        BaseResult baseResult = shpUserService.shpUsernameLogin(username);
        if (ConstantCommon.OK.equals(baseResult.getCode())) {
            //清除验证码redisKey和验证码剩余时间redisKey
            cleanSmsCodeRedisKey(username, smsCodeRedisKey);
        }
        saveIp(baseResult, getBasicParam());
        return baseResult;
    }

    /**
     * 第三方登录
     *
     * @return Result
     */
    @RequestRequire
    @ApiOperation(
            value = "第三方登录",
            notes = "第三方登录",
            httpMethod = "POST")
    @RequestMapping("/otherLogin")
    public BaseResult otherLogin(@Valid ParamCode paramCode, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String bindType = paramCode.getBindType();
        BaseResult baseResult;
        switch (bindType) {
            case "0":
                //苹果id登录
                baseResult = appleLogin(paramCode);
                break;
            case "1":
                //微信登录
                baseResult = weChatLogin(paramCode);
                break;
            default:
                baseResult = BaseResult.defaultErrorWithMsg("授权方式错误!");
                break;
        }
        return baseResult;

    }


    /**
     * 微信登录
     *
     * @return
     */
    private BaseResult weChatLogin(ParamCode paramCode) {
        String errMsg = "微信登录授权失败";
        try {
            String code = paramCode.getCode();
            String appId = ConstantWeChat.APP_ID_FOR_APP;
            String appSecret = ConstantWeChat.APP_SECRET_FOR_APP;
            //1.code先调用微信接口查该用户的openId
            ShpBindCount shpBindCount = shpBindCountService.bindWeChatLogin(appId, appSecret, code);
            if (null != shpBindCount) {
                /*2.openId去数据库查,如果存在(更新相关信息),则直接登录返回token,
                 如果不存在(保存相关信息), 则绑定手机号,然后直接登录,返回token*/
                String openId = shpBindCount.getOpenId();
                ShpBindCount sbc = shpBindCountService.getBindCountByOpenId(EnumOtherLoginType.WE_CHAT, openId);
                if (null != sbc) {
                    //LocalUtils.copyProperties(shpBindCount, sbc);
                    //shpBindCountService.saveOrUpdateBindCount(sbc);
                    //状态: -1: 解绑; 0:未绑定; 1:已绑定
                    String state = sbc.getState();
                    if (ConstantCommon.ONE.equals(state)) {
                        //生成登录token 进行登录
                        String username = sbc.getUsername();
                        BaseResult baseResult = shpUserService.shpUsernameLogin(username);
                        saveIp(baseResult, getBasicParam());
                        return baseResult;
                    }
                }
                shpBindCount.setType("wx");
                JSONObject json = new JSONObject(LocalUtils.convertBeanToMap(shpBindCount));
                //openId放在redis里面30分钟有效绑定时间
                String key = ConstantRedisKey.getBindOtherLoginKey(openId);
                redisUtil.setExMINUTES(key, json.toJSONString(), 30);
                //未绑定过微信 需要重新绑定,返回openId进行标记
                return BaseResult.okResult(LocalUtils.getHashMap("openId", openId));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errMsg = e.getMessage();
        }
        return BaseResult.errorResult(errMsg);
    }

    /**
     * 苹果登录
     *
     * @return
     */
    private BaseResult appleLogin(ParamCode paramCode) {
        String errMsg = "苹果授权失败: ";
        try {
            //苹果授权唯一标识码(相当于微信的openId)
            String code = paramCode.getCode();
                /*2.openId去数据库查,如果存在(更新相关信息),则直接登录返回token,
                 如果不存在(保存相关信息), 则绑定手机号,然后直接登录,返回token*/
            ShpBindCount sbc = shpBindCountService.getBindCountByOpenId(EnumOtherLoginType.APPLE, code);
            if (null != sbc) {
                //状态: -1: 解绑; 0:未绑定; 1:已绑定
                String state = sbc.getState();
                if (ConstantCommon.ONE.equals(state)) {
                    //已绑定,直接苹果登录 生成登录token 进行登录
                    String username = sbc.getUsername();
                    BaseResult baseResult = shpUserService.shpUsernameLogin(username);
                    saveIp(baseResult, getBasicParam());
                    return baseResult;
                }
            }
            sbc = new ShpBindCount();
            sbc.setOpenId(code);
            sbc.setType("apple");
            JSONObject json = new JSONObject(LocalUtils.convertBeanToMap(sbc));
            //openId放在redis里面30分钟有效绑定时间
            String key = ConstantRedisKey.getBindOtherLoginKey(code);
            redisUtil.setExMINUTES(key, json.toJSONString(), 30);
            //未绑定过苹果登录 需要重新绑定,返回openId进行标记
            return BaseResult.okResult(LocalUtils.getHashMap("openId", code));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errMsg += e.getMessage();
        }
        return BaseResult.errorResult(errMsg);
    }


    /**
     * 登录成功之后都需要调用此接口 保存ip等相关设备信息
     *
     * @param baseResult
     * @param params     通过继承BaseController的getBasicParam()方法获取;
     */
    private void saveIp(BaseResult baseResult, BasicParam params) {
        if (ConstantCommon.OK.equals(baseResult.getCode())) {
            //清除验证码redisKey和验证码剩余时间redisKey
            updateAppVersion(baseResult, params);
        }
    }

    private void updateAppVersion(BaseResult baseResult, BasicParam params) {
        HashMap<String, Object> data = (HashMap<String, Object>) baseResult.getData();
        Object userIdStr = data.get("userId");
        Object token = data.get("token");
        int userId = Integer.parseInt(userIdStr.toString());
        ThreadUtils.getInstance().executorService.execute(() -> {
            //ShpUser shpUser = new ShpUser();
            //shpUser.setId(userId);
            //shpUser.setAppVersion(appVersion);
            //shpUser.setUpdateTime(new Date());
            //shpUserService.updateShpUser(shpUser);
            ShpUserToken shpToken = new ShpUserToken();
            shpToken.setFkShpUserId(userId);
            shpToken.setToken(token.toString());
            shpToken.setLoginIp(params.getIp());
            shpToken.setDeviceId(params.getDeviceId());
            shpToken.setPhoneSystem(params.getPhoneSystem());
            shpToken.setPhoneType(params.getPhoneType());
            String platform = params.getPlatform();
            shpToken.setPlatform(LocalUtils.isEmptyAndNull(platform) ? "" : platform.toLowerCase());
            shpToken.setAppVersion(params.getAppVersion());
            shpToken.setState("1");
            shpToken.setInsertTime(new Date());
            shpToken.setUpdateTime(shpToken.getInsertTime());
            shpToken.setInsertAdmin(userId);
            shpToken.setUpdateAdmin(0);
            shpToken.setVersions(1);
            shpToken.setDel("0");
            shpUserTokenService.saveShpUserToken(shpToken);
        });
    }

    /**
     * 获取图形验证码
     *
     * @param response
     * @throws IOException
     */
    @ApiOperation(
            value = "3-1.获取图形验证码",
            notes = "获取图形验证码;返回图片输出流<br/>",
            httpMethod = "GET")
    @ApiImplicitParam(paramType = "query", required = true, name = "username", value = "用户名", dataType = "String")
    @GetMapping("/imageCode.png")
    public void getImageCode(HttpServletResponse response, String username) {
        try {
            String imageCodeKey = ConstantRedisKey.getImageCode(username);
            String imageCode = PBKDF2Util.getSalt(4);
            BufferedImage bi = ImageCode.createImageCode(imageCode);
            ImageIO.write(bi, "PNG", response.getOutputStream());
            //图形验证码5分钟过期
            redisUtil.setExMINUTES(imageCodeKey, imageCode, 5);
            log.debug("====用户获取验证码====用户:" + username + " : " + imageCode);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 注册店铺用户-ShpUser;<br/>
     * 1.判断用户名是否存在;<br/>
     * 2.判断短信验证码是否正确;<br/>
     * 3.判断邀请码(如果填邀请码)是否存在;<br/>
     * 4.都通过之后;注册用户;然后清除smsCode在redis中的值;
     *
     * @return Result
     */
    @RequestRequire
    @ApiOperation(
            value = "3-3.用户注册接口",
            notes = "用户注册接口--短信验证码登录;<br/>注册成功返回token;",
            httpMethod = "POST")
    @RequestMapping("/register")
    public BaseResult register(@Valid ParamRegister register, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String username = register.getUsername();
        String inviteCode = register.getInviteCode();
        String smsCode = register.getSmsCode();
        String nickName = register.getNickname();
        //redis的短信验证码
        String smsCodeRedisKey = ConstantRedisKey.getYzmSmsCodeKey(EnumSendSmsType.REGISTER.getCode(), username);
        String smsCodeRedisValue = redisUtil.get(smsCodeRedisKey);
        //端上短信验证码是否和服务器上的一致
        servicesUtil.validateServerSmsCode(username, smsCode, EnumSendSmsType.REGISTER.getCode());
        BaseResult baseResult = shpUserService.registerShpUserAndLogin(username, inviteCode, nickName);
        if (ConstantCommon.OK.equals(baseResult.getCode())) {
            //注册成功之后;清除验证码redisKey和验证码剩余时间redisKey
            cleanSmsCodeRedisKey(username, smsCodeRedisKey);
            redisUtil.setExMINUTES(NO_SET_PASSWORD + username, getIpAddr(), 5);
        }
        saveIp(baseResult, getBasicParam());
        return baseResult;
    }

    /**
     * 发送--注册验证码
     *
     * @return Result
     */
    @RequestRequire
    @ApiOperation(
            value = "3-2.发送【注册】短信验证码",
            notes = "发送【注册】短信验证码;<br/>如果需要校验图形验证码,则需把imageCode参数传上来;",
            httpMethod = "POST")
    @RequestMapping("/sendRegisterSmsCode")
    public BaseResult sendRegisterSmsCode(@Valid ParamImageCode imageCode, BindingResult result) {
        servicesUtil.validControllerParam(result);
        imageCode.setIp(getIpAddr());
        return shpUserService.sendSmsValidateCode(imageCode, EnumSendSmsType.REGISTER);
    }


    /**
     * 发送--登录验证码
     *
     * @return Result
     */
    @RequestRequire
    @ApiOperation(
            value = "4.发送登录验证码",
            notes = "用户注册接口--短信验证码登录;<br/>注册成功返回token;",
            httpMethod = "POST")
    @RequestMapping("/sendLoginSmsCode")
    public BaseResult sendLoginSmsCode(@Valid ParamImageCode imageCode, BindingResult result) {
        servicesUtil.validControllerParam(result);
        imageCode.setIp(getIpAddr());
        return shpUserService.sendSmsValidateCode(imageCode, EnumSendSmsType.LOGIN);
    }

    /**
     * 发送--重置密码验证码
     *
     * @return Result
     */
    @RequestRequire
    @ApiOperation(
            value = "5.发送【重置】短信验证码",
            notes = "发送【重置】短信验证码;<br/>",
            httpMethod = "POST")
    @RequestMapping("/sendResetPwdSmsCode")
    public BaseResult sendResetPwdSmsCode(@Valid ParamImageCode imageCode, BindingResult result) {
        servicesUtil.validControllerParam(result);
        imageCode.setIp(getIpAddr());
        return shpUserService.sendSmsValidateCode(imageCode, EnumSendSmsType.RESET_PASSWORD);
    }

    /**
     * 发送--【绑定微信公众号帐号】短信验证码
     *
     * @return Result
     */
    @RequestRequire
    @ApiOperation(
            value = "5.发送【绑定微信公众号帐号】短信验证码",
            notes = "发送【绑定微信公众号帐号】短信验证码;<br/>",
            httpMethod = "POST")
    @RequestMapping("/sendBindCountSmsCode")
    public BaseResult sendBindCountSmsCode(@Valid ParamImageCode imageCode, BindingResult result) {
        servicesUtil.validControllerParam(result);
        if (LocalUtils.isEmptyAndNull(imageCode.getImageCode())) {
            return BaseResult.defaultErrorWithMsg("请输入图形验证码!");
        }
        imageCode.setIp(getIpAddr());
        return shpUserService.sendSmsValidateCode(imageCode, EnumSendSmsType.BIND_COUNT);
    }


    /**
     * 帐号绑定微信openId
     *
     * @return Result
     */
    @RequestRequire
    @ApiOperation(
            value = "帐号绑定微信openId",
            notes = "帐号绑定微信openId",
            httpMethod = "POST")
    @RequestMapping("/bindWeChatCount")
    public BaseResult bindWeChatCount(@Valid ParamBindCount bindCount, BindingResult result) {
        servicesUtil.validControllerParam(result);
        //验证码类型;
        EnumSendSmsType sendSmsType = EnumSendSmsType.BIND_COUNT;
        String username = bindCount.getUsername();
        String smsCode = bindCount.getSmsCode();
        String code = bindCount.getCode();
        //端上短信验证码是否和服务器上的一致
        servicesUtil.validateServerSmsCode(username, smsCode, sendSmsType.getCode());
        Integer userId = shpUserService.getShpUserIdByUsername(username);
        if (null == userId) {
            return BaseResult.defaultErrorWithMsg("用户不存在!");
        }
        boolean exists = shpBindCountService.existsBindCount(EnumOtherLoginType.WE_CHAT, username);
        if (exists) {
            return BaseResult.defaultErrorWithMsg("该用户已绑定公众号!");
        }
        //通过code获取openId;再把openId存起来;
        String openId = "";
        shpBindCountService.saveBindCount(userId, username, openId, EnumOtherLoginType.WE_CHAT);
        return BaseResult.defaultOkResultWithMsg("绑定成功!");
    }


    /**
     * 【微信授权登录】--发送短信验证码<br/>
     * 1.判断该用户是否已注册帐号;
     * 2.如果已注册帐号,则直接发送验证码,验证通过之后即可绑定
     * 3.如果未注册帐号,则返回标识符,代表该帐号未注册,放开邀请码的输入框,然后再走之前的注册流程.
     *
     * @return Result
     */
    @RequestRequire
    @ApiOperation(
            value = "发送【微信授权登录】短信验证码",
            notes = "发送【微信授权登录】短信验证码;<br/>",
            httpMethod = "POST")

    @RequestMapping("/sendOtherLoginSmsCode")
    public BaseResult sendOtherLoginSmsCode(@Valid ParamBindOpenId imageCode, BindingResult result) {
        servicesUtil.validControllerParam(result);
        imageCode.setIp(getIpAddr());
        String openId = imageCode.getOpenId();
        //是否存在已注册用户
        VoShpUser shpUser = shpUserService.getShpUserForLogin(imageCode.getUsername());
        EnumSendSmsType sendSmsType = EnumSendSmsType.BIND_COUNT;
        if (null == shpUser) {
            //该用户未注册,发送注册验证码
            sendSmsType = EnumSendSmsType.REGISTER;
        } else {
            //已注册用户检查一下是否被绑定
            String openIdKey = ConstantRedisKey.getBindOtherLoginKey(openId);
            JSONObject sbcJson = JSONObject.parseObject(redisUtil.get(openIdKey));
            String typeJson = sbcJson.getString("type");
            EnumOtherLoginType type = EnumOtherLoginType.WE_CHAT;
            if ("apple".equals(typeJson)) {
                type = EnumOtherLoginType.APPLE;
            }
            String username = imageCode.getUsername();
            boolean exists = shpBindCountService.existsBindCount(type, username);
            if (exists) {
                return BaseResult.defaultErrorWithMsg("该用户已绑定【" + type.getDescription() + "】帐号!");
            }
        }
        //绑定验证码的短信类型
        String key = ConstantRedisKey.getBindOtherLoginKey(openId) + "_sms_type";
        redisUtil.setExMINUTES(key, sendSmsType.getCode(), 5);
        BaseResult baseResult = shpUserService.sendSmsValidateCode(imageCode, sendSmsType);
        if (ConstantCommon.OK.equals(baseResult.getCode())) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("smsType", sendSmsType.getCode());
            baseResult.setData(data);
        }
        return baseResult;
    }


    /**
     * 绑定第三方登录
     *
     * @return Result
     */
    @RequestRequire
    @ApiOperation(
            value = "绑定第三方登录",
            notes = "绑定第三方登录",
            httpMethod = "POST")
    @RequestMapping("/bindOtherApp")
    public BaseResult bindOtherApp(@Valid ParamBindWxApp bindCount, BindingResult result) throws Exception {
        servicesUtil.validControllerParam(result);
        //验证码类型;
        boolean isRegister = false;
        EnumSendSmsType sendSmsType = EnumSendSmsType.BIND_COUNT;
        String username = bindCount.getUsername();
        String smsCode = bindCount.getSmsCode();
        String openId = bindCount.getOpenId();
        String inviteCode = bindCount.getInviteCode();
        String key = ConstantRedisKey.getBindOtherLoginKey(openId) + "_sms_type";
        String openIdKey = ConstantRedisKey.getBindOtherLoginKey(openId);

        if (LocalUtils.isEmptyAndNull(openIdKey)) {
            return BaseResult.defaultErrorWithMsg("openId失效,请重新获授权!");
        }

        String smsType = redisUtil.get(key);
        if (LocalUtils.isEmptyAndNull(smsType)) {
            return BaseResult.defaultErrorWithMsg("验证码已失效,请重新获取验证码!");
        }
        //注册
        if (EnumSendSmsType.REGISTER.getCode().equals(smsType)) {
            sendSmsType = EnumSendSmsType.REGISTER;
            isRegister = true;
        }
        //端上短信验证码是否和服务器上的一致
        servicesUtil.validateServerSmsCode(username, smsCode, sendSmsType.getCode());
        BaseResult baseResult;
        EnumOtherLoginType enumType = EnumOtherLoginType.WE_CHAT;

        JSONObject sbcJson = JSONObject.parseObject(redisUtil.get(openIdKey));
        String type = sbcJson.getString("type");
        String accessToken = sbcJson.getString("accessToken");
        String refreshToken = sbcJson.getString("refreshToken");
        if ("apple".equals(type)) {
            enumType = EnumOtherLoginType.APPLE;
        }
        Integer userId;
        ShpBindCount sbc = shpBindCountService.getBindCountByOpenId(enumType, openId);
        if (sbc != null && "1".equals(sbc.getState())) {
            //已经绑定
            return BaseResult.defaultErrorWithMsg("该" + enumType.getDescription() + "帐号已被其它帐号绑定,请更换!");
        }
        if (isRegister) {
            //新注册用户,调用注册接口
            baseResult = shpUserService.registerShpUserAndLogin(username, inviteCode, bindCount.getNickname());
        } else {
            //已存在用户, 直接绑定, 成功之后 直接登录
            userId = shpUserService.getShpUserIdByUsername(username);
            if (null == userId) {
                return BaseResult.defaultErrorWithMsg("用户不存在!");
            }

            //生成登录token 进行登录
            baseResult = shpUserService.shpUsernameLogin(username);
        }

        if (ConstantCommon.OK.equals(baseResult.getCode())) {
            HashMap<String, Object> data = (HashMap<String, Object>) baseResult.getData();
            Object userIdStr = data.get("userId");
            userId = Integer.parseInt(userIdStr.toString());
            shpBindCountService.updateUserinfoFromWeChat(
                    openId, accessToken, refreshToken, sbc, enumType, userId);
            saveIp(baseResult, getBasicParam());
            cleanSmsCodeRedisKey(username, key);
            redisUtil.delete(openIdKey);
        }
        return baseResult;
    }


    /**
     * 注册成功后-设置登录密码;<br/>
     * 1.注册成功后,会有一个redis的值,设置完密码再进行清除;<br/>
     * 2.清除该key值之前,如果该值为null,则不允许通过该接口进行设置密码;
     *
     * @return Result
     */
    @RequestRequire
    @ApiOperation(
            value = "6.注册成功后-设置登录密码",
            notes = "注册成功后-设置登录密码;如果跳过此步骤,则只能重置密码,进行设置密码;<br/>",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", required = true,
                    name = "token", value = "token", dataType = "String")
    })
    @RequestMapping("/setLoginPassword")
    public BaseResult setLoginPassword(@Valid ParamNewPwd newPwd, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String username = newPwd.getUsername();
        String ip = getIpAddr();
        String noSetPasswordKey = NO_SET_PASSWORD + username;
        String redisIp = redisUtil.get(noSetPasswordKey);
        if (LocalUtils.isEmptyAndNull(redisIp)) {
            return BaseResult.defaultErrorWithMsg("登录失效,请重新登录!");
        }
        if (!redisIp.equals(ip)) {
            return BaseResult.defaultErrorWithMsg("网络发生变化,请重新登录!");
        }
        newPwd.setTokenUserId(getUserId());
        BaseResult baseResult = shpUserService.updateShpUserPassword(newPwd, EnumSendSmsType.REGISTER);
        redisUtil.delete(noSetPasswordKey);
        return baseResult;
    }


    /**
     * 重置登录密码;
     *
     * @return Result
     */
    @RequestRequire
    @ApiOperation(
            value = "7.【重置】登录密码",
            notes = "【重置】登录密码;<br/>",
            httpMethod = "POST")
    @RequestMapping("/resetLoginPassword")
    public BaseResult resetLoginPassword(@Valid ParamNewPwdSmsCode newPwdSmsCode, BindingResult result) {
        servicesUtil.validControllerParam(result);
        //验证码类型;
        EnumSendSmsType sendSmsType = EnumSendSmsType.RESET_PASSWORD;
        String username = newPwdSmsCode.getUsername();
        String smsCode = newPwdSmsCode.getSmsCode();
        //redis的短信验证码
        String smsCodeRedisKey = ConstantRedisKey.getYzmSmsCodeKey(sendSmsType.getCode(), username);
        String smsCodeRedisValue = redisUtil.get(smsCodeRedisKey);
        //端上短信验证码是否和服务器上的一致
        servicesUtil.validateServerSmsCode(username, smsCode, sendSmsType.getCode());
        BaseResult baseResult = shpUserService.updateShpUserPassword(newPwdSmsCode, sendSmsType);
        if (ConstantCommon.OK.equals(baseResult.getCode())) {
            //清除验证码redisKey和验证码剩余时间redisKey
            cleanSmsCodeRedisKey(username, smsCodeRedisKey);
        }
        return baseResult;
    }

    /**
     * 清除验证码的redisKey;<br/>
     * 清除验证码剩余时间的redisKey;
     *
     * @param username
     * @param smsCodeRedisKey
     */
    private void cleanSmsCodeRedisKey(String username, String smsCodeRedisKey) {
        //清除注册相关在redis中的缓存
        String yzmLeftTimeKey = ConstantRedisKey.getSendYzmLeftTime(username);
        redisUtil.delete(smsCodeRedisKey);
        redisUtil.delete(yzmLeftTimeKey);
    }


    /**
     * 店铺用户登录接口--一键登录
     *
     * @return Result
     */
    @RequestRequire
    @ApiOperation(
            value = "一键登录接口",
            httpMethod = "POST")
    @PostMapping("/getVerifyInfo")
    public BaseResult getVerifyInfo(@Valid ParamVerify paramVerify, BindingResult result) {
        servicesUtil.validControllerParam(result);
        paramVerify.setNoSetPassword(NO_SET_PASSWORD);
        paramVerify.setIpAddress(getIpAddr());
        paramVerify.setGetBasicParam(getBasicParam());
        BaseResult verifyInfo = shpUserService.getVerifyInfo(paramVerify);
        saveIp(verifyInfo, getBasicParam());
        return verifyInfo;

    }


    @Autowired
    protected RedisUtil redisUtil;

    /**
     * 验证扫码登录识别二维码内容
     *
     * @return Result
     */
    @RequestRequire
    @ApiOperation(
            value = "验证扫码登录识别二维码内容",
            httpMethod = "POST")
    @PostMapping("/verifyQRCode")
    public BaseResult verifyQRCode(@Valid ParamQRCodeQuery param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        //String[] split = param.getCodeKey().split(":");
        String isBoolean = redisUtil.get(param.getCodeKey());
        if (StringUtil.isBlank(isBoolean)) {
            return BaseResult.errorResult(EnumCode.ERROR_EXPIRED);
        }
        redisUtil.setExSECONDS(param.getCodeKey(), ShpUserServiceImpl.ok_qrcode_scan, ShpUserServiceImpl.out_time);
        return BaseResult.okResult();
    }

    /**
     * 扫码确认登录
     *
     * @return Result
     */
    @RequestRequire
    @ApiOperation(
            value = "扫码确认登录",
            httpMethod = "POST")
    @PostMapping("/okQRCodeLogin")
    public BaseResult okQRCodeLogin(@Valid ParamQRCodeQuery param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        //String[] split = param.getCodeKey().split(":");
        String isBoolean = redisUtil.get(param.getCodeKey());
        if (StringUtil.isBlank(isBoolean)) {
            return BaseResult.errorResult(EnumCode.ERROR_EXPIRED);
        }
        if (isBoolean.equals(ShpUserServiceImpl.unload)) {
            redisUtil.delete(param.getCodeKey());
            return BaseResult.errorResult(EnumCode.ERROR_EXPIRED);
        }
        if (!shopIsMember()) {
            throw new MyException("PC端体验已结束,目前只支持正式会员使用哦,请到APP端开通会员~");
        }
        Integer userId = getUserId();
        redisUtil.setExSECONDS(param.getCodeKey(), userId.toString(), ShpUserServiceImpl.out_time);
        return BaseResult.okResult();
    }
}
