package com.luxuryadmin.apiadmin.shp;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.base.BasicParam;
import com.luxuryadmin.common.constant.*;
import com.luxuryadmin.common.exception.ControllerException;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ThreadUtils;
import com.luxuryadmin.common.utils.VersionUtils;
import com.luxuryadmin.entity.shp.ShpBindCount;
import com.luxuryadmin.entity.shp.ShpUser;
import com.luxuryadmin.entity.shp.ShpUserShopRef;
import com.luxuryadmin.enums.login.EnumOtherLoginType;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.param.login.ParamCode;
import com.luxuryadmin.param.login.ParamModifyPwd;
import com.luxuryadmin.param.shp.ParamChooseShop;
import com.luxuryadmin.param.shp.ParamUpdateUserInfo;
import com.luxuryadmin.service.biz.BizLeaguerConfigService;
import com.luxuryadmin.service.shp.*;
import com.luxuryadmin.vo.biz.VoCanSeeLeaguerPriceInfo;
import com.luxuryadmin.vo.shp.VoShopBase;
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
import java.util.*;

/**
 * @author monkey king
 * @Date 2019/12/01 4:07
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/admin", method = RequestMethod.POST)
@Api(tags = {"D001.??????????????????"}, description = "/shop/user | ??????????????????,????????????,????????????")
public class ShpShopController extends BaseController {

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
     * ????????????
     *
     * @param paramToken
     * @param result
     * @return
     */
    @ApiOperation(
            value = "????????????",
            notes = "????????????",
            httpMethod = "POST")
    @RequestMapping("/changeShop")
    public BaseResult changeShop(@Valid ParamToken paramToken, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String token = getToken();
        int userId = getUserId();

        List<VoShopBase> voShopBases = shpShopService.chooseShop(userId, token);
        if (!LocalUtils.isEmptyAndNull(voShopBases)) {
            for (VoShopBase shopBase : voShopBases) {
                shopBase.setShopHeadImgUrl(servicesUtil.formatImgUrl(shopBase.getShopHeadImgUrl()));

                //????????????: 2020-09-11 23:58:49 ???????????????;
                Integer shopId = shopBase.getShopId();
                if (shopId > 0) {
                    shpUserShopRefService.updateBossUserShopRef(shopId, userId);
                }
            }
        }
        HashMap<String, Object> objList = LocalUtils.getHashMap(voShopBases);
        return BaseResult.okResult(objList);
    }


    /**
     * ????????????--?????????????????????????????????;<br/>
     * ????????????id?????????id
     *
     * @return
     */

    @RequestRequire
    @ApiOperation(
            value = "????????????????????????",
            notes = "????????????id?????????id;<br/>" +
                    "??????????????????????????????????????????;???????????????shopId???defaultLogin????????????;",
            httpMethod = "POST")
    @RequestMapping("/myCenter")
    public BaseResult myCenter(@Valid ParamChooseShop paramChooseShop, BindingResult result) {
        servicesUtil.validControllerParam(result);
        try {
            String token = paramChooseShop.getToken();
            String chooseShopId = paramChooseShop.getShopId();
            String defaultLogin = paramChooseShop.getDefaultLogin();
            int userId = getUserId();
            //????????????,???????????????????????????;
            int shopId = getShopId();
            boolean chooseShop = !LocalUtils.isEmptyAndNull(chooseShopId);
            int minutes = 3*60;
            if (chooseShop) {
                //?????????????????????
                shopId = Integer.parseInt(chooseShopId);
                //???????????????????????????id????????????
                ShpUser shpUser = shpUserService.packShpUserForLogin(userId, shopId, defaultLogin);
                shpUserService.updateShpUser(shpUser);
            }
            //??????????????????,????????????shopId
            String newShopIdKey = RedisKeyAdminLogin.getShopIdRedisKeyByToken(token);
            redisUtil.setExMINUTES(newShopIdKey, shopId + "", minutes);
            String shopNumber = getShopNumber();
            //??????????????????
            String vipExpireTip = getVipExpire();

            //?????????id????????????????????????,????????????????????????
            if (0 != shopId && ConstantCommon.ZERO.equals(shopNumber)) {
                String shopNumberStr;
                String shopName;
                //?????????key
                Map<String, Object> shop = shpShopService.getShopNumberAndShopNameByShopId(shopId);
                if (LocalUtils.isEmptyAndNull(shop)) {
                    //???????????????????????????id????????????
                    ShpUser shpUser = shpUserService.packShpUserForLogin(userId, 0, "0");
                    shpUserService.updateShpUser(shpUser);
                    redisUtil.setExMINUTES(RedisKeyAdminLogin.getShopIdRedisKeyByToken(token), "0", minutes);
                    return BaseResult.defaultErrorWithMsg("???????????????!");
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
            VoUserShopBase voUserShopBase = shpUserService.getShpUserBaseByUserIdAndShopId(userId);
            voUserShopBase.setUserHeadImgUrl(servicesUtil.formatImgUrl(voUserShopBase.getUserHeadImgUrl()));
            voUserShopBase.setShopId(shopId);
            voUserShopBase.setUserId(userId);
            voUserShopBase.setShowCard(true);
            voUserShopBase.setIsMember(getIsMemberValue());
            voUserShopBase.setMemberState(getMemberState());
            //????????????
            voUserShopBase.setShopHeadImgUrl(servicesUtil.formatImgUrl(voUserShopBase.getShopHeadImgUrl()));
            Map<String, Object> map = LocalUtils.convertBeanToMap(voUserShopBase);
            //????????????
            map.put("uPermProUpload", hasPermToPageWithCurrentUser(ConstantPermission.MOD_UPLOAD_PRODUCT));
            //????????????
            map.put("uPermConfirmOrder", hasPermToPageWithCurrentUser(ConstantPermission.MOD_CONFIRM_ORD));
            map.put("vipExpireTip", LocalUtils.isEmptyAndNull(vipExpireTip) ? "" : vipExpireTip + " ??????");

            return BaseResult.okResult(map);
        } catch (Exception e) {
            throw new ControllerException(e.getMessage(), e);
        }
    }


    /**
     * ??????????????????
     *
     * @param paramToken paramToken
     * @return Result
     */
    @RequestMapping("/exitLogin")
    @RequestRequire
    @ApiOperation(
            value = "???????????????",
            notes = "???????????????",
            httpMethod = "POST")
    public BaseResult exitLogin(@Valid ParamToken paramToken, BindingResult result) {
        servicesUtil.validControllerParam(result);
        shpUserService.exitLogin(paramToken.getToken(), true);
        return BaseResult.okResult();
    }


}
