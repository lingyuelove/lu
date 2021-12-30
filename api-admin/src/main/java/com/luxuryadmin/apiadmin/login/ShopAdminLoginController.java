package com.luxuryadmin.apiadmin.login;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.base.BasicParam;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.constant.RedisKeyAdminLogin;
import com.luxuryadmin.common.encrypt.PBKDF2Util;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.ImageCode;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.common.utils.ThreadUtils;
import com.luxuryadmin.entity.shp.ShpUserToken;
import com.luxuryadmin.enums.login.EnumSendSmsType;
import com.luxuryadmin.param.login.ParamImageCode;
import com.luxuryadmin.param.login.ParamLoginPwd;
import com.luxuryadmin.param.login.ParamLoginSms;
import com.luxuryadmin.param.login.ParamQRCodeLoginQuery;
import com.luxuryadmin.service.shp.ShpUserService;
import com.luxuryadmin.service.shp.ShpUserTokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
@Api(tags = "A001.【登录注册】模块", description = "/login |登录发短信验证码接口 ")
public class ShopAdminLoginController extends BaseController {

    @Autowired
    private ShpUserService shpUserService;

    @Autowired
    private ShpUserTokenService shpUserTokenService;

    @Autowired
    protected RedisUtil redisUtil;

    public static final String not_pass = "NOT_PASS";

    public static final Long out_time = 300L;

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
    @RequestMapping("/adminPwdLogin")
    public BaseResult adminPwdLogin(@Valid ParamLoginPwd loginPwd, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String username = loginPwd.getUsername();
        String password = loginPwd.getPassword();
        BaseResult baseResult = shpUserService.shpPwdLogin(username, password, true);
        if (ConstantCommon.OK.equals(baseResult.getCode())) {
            HashMap<String, Object> data = (HashMap<String, Object>) baseResult.getData();
            Object token = data.get("token");
            String shopIdValue = redisUtil.get(RedisKeyAdminLogin.getShopIdRedisKeyByToken(token.toString()));
            String memberKey = ConstantRedisKey.getShopMemberRedisKeyByShopId(Integer.parseInt(shopIdValue));
            String isMember = redisUtil.get(memberKey);
            if (!ConstantCommon.YES.equals(isMember)) {
                throw new MyException("PC端体验已结束,目前只支持正式会员使用哦,请到APP端开通会员~");
            }
        }
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
    @RequestMapping("/adminSmsLogin")
    public BaseResult adminSmsLogin(@Valid ParamLoginSms loginSms, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String username = loginSms.getUsername();
        String smsCode = loginSms.getSmsCode();
        //验证码类型;
        EnumSendSmsType sendSmsType = EnumSendSmsType.LOGIN;
        //redis的短信验证码
        String smsCodeRedisKey = ConstantRedisKey.getYzmSmsCodeKey(sendSmsType.getCode(), username);
        String smsCodeRedisValue = redisUtil.get(smsCodeRedisKey);
        //端上短信验证码是否和服务器上的一致
        servicesUtil.validateServerSmsCode(username, smsCode, sendSmsType.getCode());
        //进行登录
        BaseResult baseResult = shpUserService.shpUsernameLogin(username, true);
        if (ConstantCommon.OK.equals(baseResult.getCode())) {
            //清除验证码redisKey和验证码剩余时间redisKey
            cleanSmsCodeRedisKey(username, smsCodeRedisKey);
        }
        saveIp(baseResult, getBasicParam());
        return baseResult;
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
     * 获取登录二维码
     *
     * @return Result
     */
    @ApiOperation(
            value = "获取登录二维码数据",
            notes = "获取登录二维码数据",
            httpMethod = "GET")
    @GetMapping("/getLoginQRCode")
    public BaseResult getLoginQRCode() {
        String redisKey = shpUserService.getLoginQRCode();
        return BaseResult.okResult(redisKey);
    }


    /**
     * 轮训登录
     *
     * @return Result
     */
    @ApiOperation(
            value = "轮训登录",
            notes = "轮训登录",
            httpMethod = "POST")
    @GetMapping("/circulationLogin")
    public BaseResult circulationLogin(@Valid ParamQRCodeLoginQuery param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        BaseResult baseResult = shpUserService.circulationLogin(param);
        if (baseResult.getData() != null) {
            saveIp(baseResult, getBasicParam());
        }
        return baseResult;
    }
}
