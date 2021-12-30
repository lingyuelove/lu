package com.luxuryadmin.api.shp;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.base.BasicParam;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.constant.ConstantWeChat;
import com.luxuryadmin.common.exception.ControllerException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ThreadUtils;
import com.luxuryadmin.entity.shp.ShpBindCount;
import com.luxuryadmin.entity.shp.ShpUser;
import com.luxuryadmin.entity.shp.ShpUserShopRef;
import com.luxuryadmin.enums.login.EnumOtherLoginType;
import com.luxuryadmin.param.login.ParamCode;
import com.luxuryadmin.param.login.ParamModifyPwd;
import com.luxuryadmin.param.shp.ParamChooseShop;
import com.luxuryadmin.param.shp.ParamUpdateUserInfo;
import com.luxuryadmin.service.biz.BizLeaguerConfigService;
import com.luxuryadmin.service.shp.*;
import com.luxuryadmin.vo.biz.VoCanSeeLeaguerPriceInfo;
import com.luxuryadmin.vo.shp.VoUserShopBase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author monkey king
 * @Date 2019/12/01 4:07
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user", method = RequestMethod.POST)
@Api(tags = {"D001.【我的】模块"}, description = "/shop/user | 修改个人信息,我的页面,退出登录")
public class ShpUserController extends BaseController {

    @Autowired
    private ShpUserService shpUserService;

    @Autowired
    private ShpShopService shpShopService;

    @Autowired
    private ShpUserPermissionRefService shpUserPermissionRefService;

    @Autowired
    private ShpUserShopRefService shpUserShopRefService;

    @Autowired
    private ShpUserInviteService shpUserInviteService;

    @Autowired
    private BizLeaguerConfigService bizLeaguerConfigService;

    @Autowired
    private ShpBindCountService shpBindCountService;


    /**
     * 我的模块--登录成功之后获取的接口;<br/>
     * 获取个人id和店铺id
     *
     * @param params 前端参数
     * @return
     */

    @RequestRequire
    @ApiOperation(
            value = "【我的】模块页面",
            notes = "获取个人id和店铺id;<br/>" +
                    "如果登录接口返回多个店铺信息;则需要传送shopId和defaultLogin两个参数;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestMapping("/myCenter")
    public BaseResult myCenter(@RequestParam Map<String, String> params,
                               @Valid ParamChooseShop paramChooseShop, BindingResult result) {
        servicesUtil.validControllerParam(result);
        try {
            String token = paramChooseShop.getToken();
            String chooseShopId = paramChooseShop.getShopId();
            String defaultLogin = paramChooseShop.getDefaultLogin();
            int userId = getUserId();
            //直接登录,获取默认的店铺信息;
            int shopId = getShopId();
            boolean chooseShop = !LocalUtils.isEmptyAndNull(chooseShopId);
            int day = 30;
            String miniProgramCoverImgUrl;
            if (chooseShop) {
                //登录选择的店铺
                shopId = Integer.parseInt(chooseShopId);
                //更新选择登录的店铺id到用户表
                ShpUser shpUser = shpUserService.packShpUserForLogin(userId, shopId, defaultLogin);
                shpUserService.updateShpUser(shpUser);
            }
            //重新选择店铺,重新更新shopId
            String newShopIdKey = ConstantRedisKey.getShopIdRedisKeyByToken(token);
            redisUtil.setEx(newShopIdKey, shopId + "", day);
            String shopNumber = getShopNumber();
            //会员到期时间
            String vipExpireTip = getVipExpire();

            //有店铺id却找不到店铺编码,则重新查询数据库
            if (0 != shopId && ConstantCommon.ZERO.equals(shopNumber)) {
                String shopNumberStr;
                String shopName;
                //存储新key
                Map<String, Object> shop = shpShopService.getShopNumberAndShopNameByShopId(shopId);
                if (LocalUtils.isEmptyAndNull(shop)) {
                    //更新选择登录的店铺id到用户表
                    ShpUser shpUser = shpUserService.packShpUserForLogin(userId, 0, "0");
                    shpUserService.updateShpUser(shpUser);
                    redisUtil.setEx(ConstantRedisKey.getShopIdRedisKeyByToken(token), "0", day);
                    return BaseResult.defaultErrorWithMsg("店铺不存在!");
                }
                shopNumberStr = shop.get("shopNumber") + "";
                shopName = shop.get("shopName") + "";
                String isMember = shop.get("isMember") + "";
                String memberState = shop.get("memberState") + "";
                String tryEndTime = shop.get("tryEndTime") + "";
                String payEndTime = shop.get("payEndTime") + "";
                vipExpireTip = LocalUtils.isEmptyAndNull(payEndTime) ? tryEndTime : payEndTime;

                vipExpireTip = DateUtil.formatShort(DateUtil.parse(vipExpireTip));


                String newShopNumberKey = ConstantRedisKey.getShopNumberRedisKeyByShopId(shopId);
                String newShopNameKey = ConstantRedisKey.getShopNameRedisKeyByShopId(shopId);
                redisUtil.set(newShopNumberKey, shopNumberStr);
                redisUtil.set(newShopNameKey, shopName);
                Integer bossUserId = shpShopService.getBossUserIdByShopId(shopId);
                String newBossUserIdKey = ConstantRedisKey.getBossUserIdRedisKeyByShopId(shopId);
                redisUtil.set(newBossUserIdKey, bossUserId + "");
                redisUtil.set(ConstantRedisKey.getShopMemberRedisKeyByShopId(shopId), isMember);
                redisUtil.set(ConstantRedisKey.getMemberStateRedisKeyByShopId(shopId), memberState);
            }


            Integer parentInviteUserId = shpUserInviteService.getParentInviteCode(userId);

            VoUserShopBase voUserShopBase = shpUserService.getShpUserBaseByUserIdAndShopId(userId);
            voUserShopBase.setUserHeadImgUrl(servicesUtil.formatImgUrl(voUserShopBase.getUserHeadImgUrl()));
            voUserShopBase.setShopId(shopId);
            voUserShopBase.setUserId(userId);
            voUserShopBase.setShowCard(true);
            voUserShopBase.setIsMember(getIsMemberValue());
            voUserShopBase.setMemberState(getMemberState());

            String boss = "0";
            if (getBossUserId() == userId) {
                boss = "1";
            }
            voUserShopBase.setBoss(boss);
            if (parentInviteUserId != null) {
                String inviteUserNickname = shpUserService.getNicknameByUserId(parentInviteUserId);
                voUserShopBase.setInviteCode(inviteUserNickname);
            }

            //店铺头像
            voUserShopBase.setShopHeadImgUrl(servicesUtil.formatImgUrl(voUserShopBase.getShopHeadImgUrl()));

            //设置小程序封面
            miniProgramCoverImgUrl = voUserShopBase.getMiniProgramCoverImgUrl();
            miniProgramCoverImgUrl = LocalUtils.isEmptyAndNull(miniProgramCoverImgUrl) ? "" : servicesUtil.formatImgUrl(miniProgramCoverImgUrl);
            voUserShopBase.setMiniProgramCoverImgUrl(miniProgramCoverImgUrl);

            //设置【查看友商信息】权限
            Boolean leaguerInfoPerm = hasPermWithCurrentUser(ConstantPermission.LEAGUER_CHECK_INFO);
            voUserShopBase.setLeaguerInfoPerm(leaguerInfoPerm);

            //苹果审核用，不显示卡片
            if ("18434363494".equals(getUsername())) {
                voUserShopBase.setShowCard(false);
            }
            final int newShopId = shopId;
            shpUserShopRefService.updateBossUserShopRef(shopId, userId);
            int bossUserId = getBossUserId();
            //旧版权限替换成新版权限;下个版本之后, 版本号大于v2.1.1, 可以去除此段代码;
            //以下代码注释于2021-10-22 19:47:02;过渡2个版本之后,可删除此段代码;
            //ThreadUtils.getInstance().executorService.execute(() -> {
            //    //经营者不需要迁移权限
            //    if (userId != bossUserId) {
            //        shpUserPermissionRefService.initOldUserPermission(newShopId, userId);
            //    }
            //});

            Map<String, Object> map = LocalUtils.convertBeanToMap(voUserShopBase);
            ArrayList<Integer> userIds = new ArrayList<>();
            //初步内置只有此id有权限显示导入图片功能
            userIds.add(10000);
            userIds.add(10001);
            if (userIds.contains(getUserId())) {
                map.put("toolName", "导入图片");
                map.put("toolUrl", "https://www.luxuryadmin.com/h5/importImg.html");
            }
            //注册店铺库权限
            map.put("uPermRegisterShop", hasPermToPageWithCurrentUser(ConstantPermission.MOD_REGISTER_SHOP));
            //入库权限
            map.put("uPermProUpload", hasPermToPageWithCurrentUser(ConstantPermission.MOD_UPLOAD_PRODUCT));
            //开单权限
            map.put("uPermConfirmOrder", hasPermToPageWithCurrentUser(ConstantPermission.MOD_CONFIRM_ORD));
            //友商添加权限(和友商消息权限一致,同时拥有与否)
            map.put("uPermLeaguerAdd", hasPermToPageWithCurrentUser(ConstantPermission.MOD_LEAGUER_ADD));
            String onlyShowTopLeaguer = "0";
            if (shopId != 0) {
                //是否只看星标友商
                VoCanSeeLeaguerPriceInfo priceInfo = bizLeaguerConfigService.getCanSeeLeaguerPriceInfo(shopId, userId);
                onlyShowTopLeaguer = priceInfo.getOnlyShowTopLeaguer();
            }
            map.put("onlyShowTopLeaguer ", onlyShowTopLeaguer);
            map.put("vipExpireTip", LocalUtils.isEmptyAndNull(vipExpireTip) ? "" : vipExpireTip + " 到期");
            //小程序封面
            String mpCoverImgKey = ConstantRedisKey.MP_COVER_IMG;
            String mpCoverImg = redisUtil.get(mpCoverImgKey);
            map.put("mpCoverImg", mpCoverImg);

            //支付入口,appVersion:2.4.0及以上版本号才有支付入口
            BasicParam basicParam = getBasicParam();
            String paySwitch = redisUtil.get(ConstantRedisKey.PAY_SWITCH);
            boolean isShowPay = shopId != 0;
            //ios单独支付开关配置;为了苹果审核期间不开放支付入口
            if (!"on".equals(paySwitch) && "ios".equalsIgnoreCase(basicParam.getPlatform())) {
                isShowPay = false;
            }
            if (isShowPay) {
                HashMap<String, String> payMap = new HashMap<>(16);
                payMap.put("kthyUrl", getUserPayUrl());
                payMap.put("kthyIcon", "https://file.luxuryadmin.com/default/kaitonghuiyuan.png");
                payMap.put("kthyText", "开通会员");
                map.put("kthy", payMap);
            }
            Integer shopCount = shpShopService.getShopCount(getUserId());
            map.put("shopCount", shopCount);

            //h5的域名
            String h5DomainKey = ConstantRedisKey.H5DOMAIN;
            String h5Domain = redisUtil.get(h5DomainKey);
            map.put("h5Domain", h5Domain);
            return BaseResult.okResult(map);
        } catch (Exception e) {
            throw new ControllerException(e.getMessage(), e);
        }
    }


    /**
     * 修改个人信息
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "修改个人信息",
            notes = "修改个人信息",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/updateUserInfo")
    public BaseResult updateUserInfo(@RequestParam Map<String, String> params,
                                     @Valid ParamUpdateUserInfo user, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String nickname = user.getNickname();
        String userHeadImgUrl = user.getUserHeadImgUrl();
        boolean emptyNickname = LocalUtils.isEmptyAndNull(nickname);
        boolean emptyHeadImgUer = LocalUtils.isEmptyAndNull(userHeadImgUrl);
        boolean allNull = emptyNickname && emptyHeadImgUer;
        if (allNull) {
            return BaseResult.defaultOkResultWithMsg("更新内容不允许为空!");
        }
        ShpUser shpUser = new ShpUser();
        if (!emptyNickname) {
            shpUser.setNickname(nickname);
            ShpUserShopRef userShopRef = shpUserShopRefService.getShpUserShopRefByUserId(getShopId(), getUserId());
            if (userShopRef != null) {
                userShopRef.setName(nickname);
                userShopRef.setUpdateTime(new Date());
                shpUserShopRefService.updateUserShopRef(userShopRef);
            }
        }
        if (!emptyHeadImgUer) {
            shpUser.setHeadImgUrl(userHeadImgUrl);
        }
        shpUser.setId(getUserId());
        shpUserService.updateShpUser(shpUser);
        return BaseResult.defaultOkResultWithMsg("更新成功");
    }

    /**
     * 修改登录密码;
     *
     * @param params 前端参数
     * @return Result
     */
    @RequestRequire
    @ApiOperation(
            value = "8.【修改】登录密码",
            notes = "【修改】登录密码;<br/>",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", required = false,
                    name = "params", value = "基础参数;可不填写", dataType = "String")
    })
    @RequestMapping("/modifyPassword")
    public BaseResult modifyPassword(@RequestParam Map<String, String> params,
                                     @Valid ParamModifyPwd modifyPwd, BindingResult result) {
        servicesUtil.validControllerParam(result);
        modifyPwd.setUsername(getUsername());
        modifyPwd.setUserId(getUserId());
        //校验原密码是否正确.如果原密码正确则更新新密码;
        shpUserService.modifyPassword(modifyPwd);
        //修改密码成功之后需要重新登录
        shpUserService.exitLogin(modifyPwd.getToken());
        return BaseResult.okResult("密码修改成功,请重新登录!");
    }

    /**
     * 用户退出登录
     *
     * @param token token
     * @return Result
     */
    @RequestMapping("/exitLogin")
    @RequestRequire
    @ApiOperation(
            value = "用户退出登",
            notes = "用户退出登",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
    })
    public BaseResult exitLogin(@RequestParam String token) {
        shpUserService.exitLogin(token);
        return BaseResult.okResult();
    }


    /**
     * 已登录状态下绑定微信登录
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "已登录状态下绑定微信登录",
            notes = "已登录状态下绑定微信登录",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/bindWeChatLogin")
    public BaseResult bindWeChatLogin(@RequestParam Map<String, String> params,
                                      @Valid ParamCode paramCode, BindingResult result) {
        servicesUtil.validControllerParam(result);
        int userId = getUserId();
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
                EnumOtherLoginType type = EnumOtherLoginType.WE_CHAT;
                ShpBindCount sbc = shpBindCountService.getBindCountByOpenId(type, openId);
                if (sbc != null && "1".equals(sbc.getState())) {
                    //已经绑定
                    return BaseResult.defaultErrorWithMsg("该【" + type.getDescription() + "】帐号已被其它帐号绑定,请更换!");
                }
                boolean exists = shpBindCountService.existsBindCount(type, getUsername());
                if (exists) {
                    return BaseResult.defaultErrorWithMsg("该用户已绑定【" + type.getDescription() + "】帐号!");
                }
                String accessToken = shpBindCount.getAccessToken();
                String refreshToken = shpBindCount.getRefreshToken();
                shpBindCountService.updateUserinfoFromWeChat(
                        openId, accessToken, refreshToken, sbc, type, userId);
                return BaseResult.okResult("绑定成功");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errMsg += e.getMessage();
        }
        return BaseResult.errorResult(errMsg);
    }
}
