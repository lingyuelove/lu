package com.luxuryadmin.api.shp;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.base.BasicParam;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.constant.enums.EnumCode;
import com.luxuryadmin.common.exception.ControllerException;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ThreadUtils;
import com.luxuryadmin.common.utils.VersionUtils;
import com.luxuryadmin.entity.pro.ProShareSeeUserAgency;
import com.luxuryadmin.entity.shp.ShpDetail;
import com.luxuryadmin.entity.shp.ShpShop;
import com.luxuryadmin.entity.shp.ShpUser;
import com.luxuryadmin.enums.login.EnumSendSmsType;
import com.luxuryadmin.enums.shp.EnumShpOperateLogModule;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.param.login.ParamImageCode;
import com.luxuryadmin.param.shp.*;
import com.luxuryadmin.service.fin.FinSalaryService;
import com.luxuryadmin.service.fin.FinShopRecordService;
import com.luxuryadmin.service.op.OpMessageService;
import com.luxuryadmin.service.op.OpSmsRecordService;
import com.luxuryadmin.service.ord.OrdOrderService;
import com.luxuryadmin.service.pro.ProShareSeeUserAgencyService;
import com.luxuryadmin.service.shp.*;
import com.luxuryadmin.vo.shp.VoShopBase;
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
import java.text.ParseException;
import java.util.*;

/**
 * @author monkey king
 * @date 2019-12-17 18:29:46
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user", method = RequestMethod.POST)
@Api(tags = {"E001.【店铺】模块"}, description = "/shop/user | 注册、修改、查看、切换店铺信息")
public class ShpShopController extends BaseController {


    @Autowired
    private ShpShopService shpShopService;

    @Autowired
    private ShpUserShopRefService shpUserShopRefService;

    @Autowired
    private ShpUserService shpUserService;

    @Autowired
    private OrdOrderService ordOrderService;

    @Autowired
    private OpMessageService opMessageService;

    @Autowired
    private FinSalaryService finSalaryService;

    @Autowired
    private ShpServiceService shpServiceService;

    @Autowired
    private ShpUserInviteService shpUserInviteService;

    @Autowired
    private FinShopRecordService finShopRecordService;

    @Autowired
    private ShpOperateLogService shpOperateLogService;

    @Autowired
    private OpSmsRecordService opSmsRecordService;

    @Autowired
    private ProShareSeeUserAgencyService proShareSeeUserAgencyService;

    /**
     * 注册店铺
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "注册店铺",
            notes = "注册店铺;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "基础参数;可不填写")

    })
    @RequestRequire
    @RequestMapping("/initRegisterShop")
    public BaseResult initRegisterShop(@RequestParam Map<String, String> params) {
        String vid = LocalUtils.getUUID();
        LocalUtils.getHashMap("vid", vid);
        String key = ConstantRedisKey.getInitRegisterShopKey(getShopId(), getUserId());
        redisUtil.setExMINUTES(key, vid, 30);
        return BaseResult.okResult(LocalUtils.getHashMap("vid", vid));
    }


    /**
     * 注册店铺
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "注册店铺",
            notes = "注册店铺;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/registerShop")
    public BaseResult registerShop(@RequestParam Map<String, String> params,
                                   @Valid ParamAddShop shopInfo, BindingResult result) {
        servicesUtil.validControllerParam(result);
        int shpId = 0;
        //2020-12-11 21:56:13上线后的版本需要校验
        String key = ConstantRedisKey.getInitRegisterShopKey(getShopId(), getUserId());
        String vid = shopInfo.getVid();
        servicesUtil.validVid(key, vid);
        ShpShop shpShop = pickShpShop(shopInfo, true);
        ShpDetail shpDetail = pickShpDetail(shopInfo, true);
        //2020-11-19 21:37:38加的逻辑, 注册店铺时, 也可添加邀请关系
        validateInviteCode(shopInfo.getInviteCode());
        boolean existsShopName = shpShopService.existsShopName(getUserId(), shpShop.getName());
        if (existsShopName) {
            return BaseResult.defaultErrorWithMsg("您已经注册过该店铺名称【" + shpShop.getName() + "】,请重新登录查看!");
        }
        shpId = shpShopService.becomeShopkeeper(shpShop, shpDetail);
        redisUtil.delete(key);
        final int userId = getUserId();
        final String phone = getUsername();
        //2021-08-30 22:59:27 判断联盟小程序的用户是否为拉新用户
        int finalShopId = shpId;
        //====注册店铺有x天免费使用商家联盟的资格;修改于2021-09-16 20:24:50===
        //=====修改于2021-11-05 22:11:26;商家联盟规则改成访问商品个数限制;此代码暂时存留
        //修改于2021-10-21 16:58:18试用商家联盟的店铺换一种存储方式;每一个注册的店铺都有独立的key
        //String shopUnionShopTempKey = ConstantRedisKey.getShopUnionShopTemp(finalShopId);
        //String shopUnionDay = redisUtil.get(ConstantRedisKey.SHOP_UNION_DAY);
        //int day = Integer.parseInt(shopUnionDay);
        //Calendar calendar = DateUtil.addDaysFromOldDate(shpShop.getInsertTime(), day);
        //redisUtil.setEx(shopUnionShopTempKey, DateUtil.format(calendar.getTime()), day);
        //=============================================================
        ThreadUtils.getInstance().executorService.execute(() -> {
            //注册的手机号去小程序用户代理列表中查询;是否有记录;如果有且当前是第一次注册店铺,则为拉新用户;
            ProShareSeeUserAgency obj = proShareSeeUserAgencyService.getObjectByPhone("1", phone);
            if (!LocalUtils.isEmptyAndNull(obj)) {
                int shopNum = shpShopService.countOwnShopCount(userId);
                if (shopNum == 1) {
                    //用户首次开店,才属于拉新用户
                    obj.setFkShpShopId(finalShopId);
                    proShareSeeUserAgencyService.updateObject(obj);
                }
            }
        });
        //邀请好友
        return BaseResult.okResult(shpId);
    }


    /**
     * 修改店铺信息
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "修改店铺信息",
            notes = "修改店铺信息",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/updateShopInfo")
    @RequiresPermissions("shop:updateShopInfo")
    public BaseResult updateShopInfo(@RequestParam Map<String, String> params,
                                     @Valid ParamUpdateShopInfo shopInfo, BindingResult result) {
        servicesUtil.validControllerParam(result);
        boolean existsShopName = shpShopService.existsShopNameExceptOwn(getShopId(), getUserId(), shopInfo.getShopName());
        if (existsShopName) {
            return BaseResult.defaultErrorWithMsg("您已拥有该名称的店铺【" + shopInfo.getShopName() + "】!");
        }
        ShpShop shpShop = pickShpShop(shopInfo, false);
        ShpDetail shpDetail = pickShpDetail(shopInfo, false);
        //2020-11-19 21:37:38加的逻辑, 注册店铺时, 也可添加邀请关系
        validateInviteCode(shopInfo.getInviteCode());
        int row = shpShopService.updateShopInfo(shpShop, shpDetail);
        if (row > 0) {
            String shopName = shopInfo.getShopName();
            if (!LocalUtils.isEmptyAndNull(shopName)) {
                String shopNameKey = ConstantRedisKey.getShopNameRedisKeyByShopId(getShopId());
                redisUtil.set(shopNameKey, shopName);
            }
        }
        return BaseResult.okResult(row);
    }

    /**
     * 我的店铺;<br/>
     * 获取个人id和店铺id
     *
     * @param params 前端参数
     * @return
     */

    @RequestRequire
    @ApiOperation(
            value = "我的店铺",
            notes = "获取店铺的基本信息",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token")
    })
    @RequestMapping("/myShop")
    public BaseResult myShop(@RequestParam Map<String, String> params) {
        try {
            int shopId = getShopId();
            VoUserShopBase voUserShopBase = shpShopService.getVoUserShopBaseByShopId(shopId);
            if (null == voUserShopBase) {
                return BaseResult.defaultErrorWithMsg("暂无店铺");
            }
            voUserShopBase.setCoverImgUrl(servicesUtil.formatImgUrl(voUserShopBase.getCoverImgUrl(), false));
            voUserShopBase.setShopHeadImgUrl(servicesUtil.formatImgUrl(voUserShopBase.getShopHeadImgUrl()));
            voUserShopBase.setMiniProgramCoverImgUrl(servicesUtil.formatImgUrl(voUserShopBase.getMiniProgramCoverImgUrl()));
            return BaseResult.okResult(voUserShopBase);
        } catch (Exception e) {
            throw new ControllerException(e.getMessage(), e);
        }
    }


    /**
     * 切换店铺
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "切换店铺",
            notes = "切换店铺",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
    })
    @RequestRequire
    @RequestMapping("/changeShop")
    public BaseResult changeShop(@RequestParam Map<String, String> params) {
        String token = getToken();
        int userId = getUserId();

        List<VoShopBase> voShopBases = shpShopService.chooseShop(userId, token);
        if (!LocalUtils.isEmptyAndNull(voShopBases)) {
            for (VoShopBase shopBase : voShopBases) {
                shopBase.setShopHeadImgUrl(servicesUtil.formatImgUrl(shopBase.getShopHeadImgUrl()));

                //临时处理: 2020-09-11 23:58:49 兼容旧数据;
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
     * 发送--一键删除验证码
     *
     * @param params 前端参数
     * @return Result
     */
    @RequestRequire
    @ApiOperation(
            value = "发送--一键删除验证码",
            notes = "发送--一键删除验证码",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", required = false,
                    name = "params", value = "基础参数;可不填写", dataType = "String")
    })
    @RequestMapping("/sendOneKeyDeleteSmsCode")
    @RequiresPermissions("shop:oneKeyDelete")
    public BaseResult sendOneKeyDeleteSmsCode(@RequestParam Map<String, String> params,
                                              @Valid ParamShopImageCode imageCode, BindingResult result) {
        servicesUtil.validControllerParam(result);
        ParamImageCode paramImageCode = new ParamImageCode();
        //给当前登录手机号发验证码
        paramImageCode.setUsername(getUsername());
        paramImageCode.setIp(getIpAddr());
        paramImageCode.setImageCode(imageCode.getImageCode());
        return shpUserService.sendSmsValidateCode(paramImageCode, EnumSendSmsType.ONE_KEY_DELETE);
    }


    /**
     * 一键删除<br/>
     * 一键删除相关信息(物理删除);<br/>
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "一键删除",
            notes = "一键删除",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/oneKeyDelete")
    @RequiresPermissions("shop:oneKeyDelete")
    public BaseResult oneKeyDelete(@RequestParam Map<String, String> params,
                                   @Valid ParamOneKeyDelete oneKeyDelete, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        String smsCode = oneKeyDelete.getSmsCode();
        int shopId = getShopId();
        int userId = getUserId();
        String st = oneKeyDelete.getStartTime() + " 00:00:00";
        String et = oneKeyDelete.getEndTime() + " 23:59:59";
        //验证码类型;
        EnumSendSmsType sendSmsType = EnumSendSmsType.ONE_KEY_DELETE;
        //端上短信验证码是否和服务器上的一致
        servicesUtil.validateServerSmsCode(getUsername(), smsCode, sendSmsType.getCode());

        //写业务逻辑
        //order;salary;message;service
        String deleteType = oneKeyDelete.getDeleteType();

        String operateContent = oneKeyDelete.getStartTime() + "至" + oneKeyDelete.getEndTime();
        String type = "删除: ";
        if (deleteType.contains("order")) {
            //删除订单
            type += "【订单】;";
            operateContent += ",订单";
            ordOrderService.deleteOrderAndReceipt(shopId, st, et);
        }
        if (deleteType.contains("salary")) {
            //删除薪资记录
            type += "【薪资】;";
            operateContent += ",薪资";
            finSalaryService.deleteFinSalary(shopId, st, et);
        }
        if (deleteType.contains("message")) {
            //删除历史消失
            type += "【店铺消息】;";
            operateContent += ",店铺消息";
            opMessageService.delOpMessageByDateRange(shopId, st, et);
        }
        if (deleteType.contains("service")) {
            //删除服务凭证
            type += "【服务凭证】;";
            operateContent += ",服务凭证";
            shpServiceService.deleteShpServiceByDateRange(shopId, userId, st, et);
        }
        if (deleteType.contains("finRecord")) {
            //店铺账单
            type += "【店铺账单】;";
            operateContent += ",店铺账单";
            finShopRecordService.deleteFinShopRecordByDateRange(shopId, st, et);
        }

        //添加【店铺操作日志】-【一键清除数据】
        ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
        paramAddShpOperateLog.setShopId(shopId);
        paramAddShpOperateLog.setOperateUserId(userId);
        paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.SHOP.getName());
        paramAddShpOperateLog.setOperateName("一键清除数据");
        paramAddShpOperateLog.setOperateContent(operateContent);
        paramAddShpOperateLog.setProdId(null);
        paramAddShpOperateLog.setOrderId(null);
        paramAddShpOperateLog.setRequest(request);

        shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
        return BaseResult.okResult(type + "成功");
    }


    /**
     * 发送--注销验证码
     *
     * @param params 前端参数
     * @return Result
     */
    @RequestRequire
    @ApiOperation(
            value = "发送--注销验证码",
            notes = "发送--注销验证码",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", required = false,
                    name = "params", value = "基础参数;可不填写", dataType = "String")
    })
    @RequestMapping("/sendDestroyShopSmsCode")
    public BaseResult sendDestroyShopSmsCode(@RequestParam Map<String, String> params,
                                             @Valid ParamShopImageCode imageCode, BindingResult result) {
        onlyBossOperate();
        servicesUtil.validControllerParam(result);
        ParamImageCode paramImageCode = new ParamImageCode();
        //给当前登录手机号发验证码
        paramImageCode.setUsername(getUsername());
        paramImageCode.setIp(getIpAddr());
        paramImageCode.setImageCode(imageCode.getImageCode());
        return shpUserService.sendSmsValidateCode(paramImageCode, EnumSendSmsType.DESTROY_SHOP);
    }


    /**
     * 解除与当前店铺的关联关系<br/>
     * 1.从shp_user_shop_ref表修改该员工的状态; 标记为已删除;
     * 2.删除用户对应该店铺的权限;
     * 3添加店铺操作日志
     *
     * @return Result
     */
    @ApiOperation(
            value = "解除与当前店铺的关联关系",
            httpMethod = "POST")
    @PostMapping(value = "/removeShop")
    public BaseResult removeShop(@Valid ParamToken paramToken, BindingResult result) {
        servicesUtil.validControllerParam(result);
        if (isBossForCurrentUser()) {
            return BaseResult.defaultErrorWithMsg("经营者不允许单独退出店铺,如需要请变更经营者后再单独退出!");
        }
        int shopId = getShopId();
        int userId = getUserId();
        String nickname = shpShopService.removeShop(shopId, userId);
        //添加【店铺操作日志】-【人员变动】
        ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
        paramAddShpOperateLog.setShopId(shopId);
        paramAddShpOperateLog.setOperateUserId(userId);
        paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.SHOP.getName());
        paramAddShpOperateLog.setOperateName("人员变动");
        paramAddShpOperateLog.setOperateContent(nickname + "(" + getUsername() + ") 员工主动退出店铺!");
        paramAddShpOperateLog.setProdId(null);
        paramAddShpOperateLog.setOrderId(null);
        paramAddShpOperateLog.setRequest(getRequest());
        shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
        return BaseResult.okResult();
    }


    /**
     * 注销店铺<br/>
     * 物理删除所有相关于此店铺的信息记录;<br/>
     * 包括商品,订单,店铺设置,清理oss的图片;
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "注销店铺",
            notes = "注销店铺",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
    })
    @RequestRequire
    @RequestMapping("/destroyShop")
    public BaseResult destroyShop(@RequestParam Map<String, String> params,
                                  @Valid ParamDestroyShop paramDestroyShop, BindingResult result) {
        onlyBossOperate();
        servicesUtil.validControllerParam(result);
        String smsCode = paramDestroyShop.getSmsCode();
        //验证码类型;
        EnumSendSmsType sendSmsType = EnumSendSmsType.DESTROY_SHOP;
        String username = getUsername();
        //端上短信验证码是否和服务器上的一致
        servicesUtil.validateServerSmsCode(username, smsCode, sendSmsType.getCode());
        int shopId = getShopId();
        Map<String, String> map = new HashMap<>(16);
        map.put("shopName", getShopName());
        shpShopService.destroyShop(shopId);
        //更新选择登录的店铺id到用户表
        ShpUser shpUser = shpUserService.packShpUserForLogin(getUserId(), 0, "0");
        shpUserService.updateShpUser(shpUser);
        shpUserService.exitLogin(getToken());
        opSmsRecordService.sendSms(username, null, EnumSendSmsType.DESTROY_SUCCESS, getIpAddr(), map);
        return BaseResult.okResult("注销成功!");
    }


    @RequestMapping(value = "/deleteMiniProgramCover", method = RequestMethod.POST)
    @ApiOperation(
            value = "删除小程序封面;",
            notes = "删除小程序封面;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "id", value = "店铺ID"),
    })
    public BaseResult deleteMiniProgramCover(@RequestParam String id) {
        ShpShop shpShop = shpShopService.getShpShopById(id);
        if (null == shpShop) {
            return BaseResult.defaultErrorWithMsg("【" + id + "】店铺不存在!");
        }
        shpShop.setMiniProgramCoverImgUrl("");
        shpShopService.updateShpShop(shpShop);
        return BaseResult.okResult();
    }


    /**
     * 店铺到期提醒;还剩x天到期提醒;<br/>
     * 端上可用长连接轮询查询;
     *
     * @return Result
     */
    @ApiOperation(
            value = "店铺到期提醒;还剩x天到期提醒;",
            notes = "店铺到期提醒;还剩x天到期提醒;如果是当天则显示剩1天<br/>" +
                    "code的状态:  ok:正常 | soon:快过期 |  expired:已过期 ",
            httpMethod = "GET")
    @GetMapping(value = "/vipExpireNotify")
    public BaseResult vipExpireNotify(@Valid ParamToken paramToken, BindingResult result) {
        servicesUtil.validControllerParam(result);

        int shopId = getShopId();
        try {
            //苹果审核帐号不拦截;
            if ("18434363494".equals(getUsername())) {
                return BaseResult.okResult();
            }
            //先取缓存数据
            String vipExpireStr = getVipExpire();
            Date vipExpire = null;
            if (!LocalUtils.isEmptyAndNull(vipExpireStr)) {
                vipExpire = DateUtil.parseShort(vipExpireStr);
            }
            if (vipExpire == null) {
                ShpShop shpShop = shpShopService.getShpShopById(shopId + "");

                if (LocalUtils.isEmptyAndNull(shpShop)) {
                    return BaseResult.defaultErrorWithMsg("店铺不存在!");
                }
                //体验会员结束时间
                Date tryEndTime = shpShop.getTryEndTime();
                //正式会员结束时间
                vipExpire = shpShop.getPayEndTime();

                if (LocalUtils.isEmptyAndNull(vipExpire)) {
                    vipExpire = tryEndTime;
                }
                String vipExpireKey = ConstantRedisKey.getVipExpireRedisKeyByShopId(shopId);
                redisUtil.set(vipExpireKey, DateUtil.formatShort(vipExpire));
            }
            return servicesUtil.checkShopExpired(getUserPayUrl(), vipExpire);

        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        return BaseResult.defaultErrorWithMsg("查询到期信息异常!");
    }

    /**
     * 变更经营者<br/>
     * 新的经营者需先加入该店铺;
     *
     * @return Result
     */
    @ApiOperation(
            value = "变更经营者",
            notes = "变更经营者",
            httpMethod = "POST")
    @PostMapping(value = "/changeShopkeeper")
    public BaseResult changeShopkeeper(@Valid ParamChangeShopkeeper param, BindingResult result) throws Exception {
        servicesUtil.validControllerParam(result);

        if (!shopIsMember()) {
            return BaseResult.defaultErrorWithMsg("该功能属于会员功能!");
        }

        if (getUserId() != getBossUserId()) {
            return BaseResult.defaultErrorWithMsg("当前登录用户不是该店铺的经营者!");
        }
        //经营者手机号
        String oldUserName = getUsername();
        String appVersion = getBasicParam().getAppVersion();
        if (VersionUtils.compareVersion(appVersion, "2.6.7") >= 0) {
            //v267(含)以上的版本,使用新的方法
            shpShopService.changeShopkeeperNew(getShopId(), oldUserName, param.getNewUsername());
        } else {
            shpShopService.changeShopkeeper(getShopId(), oldUserName, param.getNewUsername());
        }

        return BaseResult.okResult();
    }


    private void validateInviteCode(String inviteCode) {
        if (LocalUtils.isInviteCode(inviteCode)) {
            Integer inviteUserId = shpUserService.getShpUserIdByNumber(Integer.parseInt(inviteCode));
            if (null == inviteUserId) {
                throw new MyException(EnumCode.ERROR_NOT_INVITE_CODE);
            }
            shpUserInviteService.inviteUser(inviteUserId, getUserId());
        }
    }


    private ShpShop pickShpShop(ParamUpdateShopInfo shopInfo, boolean isSave) {
        int userId = getUserId();
        ShpShop shpShop = new ShpShop();

        if (isSave) {
            //新增
            Date date = new Date();
            shpShop.setContact(getUsername());
            shpShop.setFkShpUserId(userId);
            shpShop.setFkShpStateCode(10);
            shpShop.setFkShpAttributeCode(10);
            shpShop.setPayMonth(0);
            shpShop.setTotalMonth(0);
            shpShop.setInsertTime(date);
            shpShop.setInsertAdmin(userId);
            shpShop.setVersions(1);
            shpShop.setDel("0");
            //设置默认封面
            shpShop.setCoverImgUrl(ConstantCommon.DEFAULT_SHOP_COVER_IMG);
            //设置默认头像
            shpShop.setHeadImgUrl(ConstantCommon.DEFAULT_SHOP_HEAD_IMG);
            shpShop.setTryStartTime(date);
            //3个月体验会员
            Date tryEndTime = DateUtil.addMonthsFromOldDate(date, 3).getTime();
            shpShop.setTryEndTime(tryEndTime);
            //体验会员
            shpShop.setMemberState(1);
            shpShop.setIsMember("no");
        } else {
            //修改
            shpShop.setId(getShopId());
            shpShop.setUpdateTime(new Date());
        }

        String shopName = shopInfo.getShopName();
        String address = shopInfo.getAddress();
        String coverImgUrl = shopInfo.getCoverImgUrl();
        String headImgUrl = shopInfo.getShopHeadImgUrl();
        String miniProgramCoverImgUrl = shopInfo.getMiniProgramCoverImgUrl();

        if (!LocalUtils.isEmptyAndNull(shopName)) {
            shpShop.setName(shopName);
        }
        if (!LocalUtils.isEmptyAndNull(address)) {
            shpShop.setAddress(address);
        }
        if (!LocalUtils.isEmptyAndNull(coverImgUrl)) {
            shpShop.setCoverImgUrl(coverImgUrl);
        }
        if (!LocalUtils.isEmptyAndNull(headImgUrl)) {
            shpShop.setHeadImgUrl(headImgUrl);
        }
        if (!LocalUtils.isEmptyAndNull(miniProgramCoverImgUrl)) {
            shpShop.setMiniProgramCoverImgUrl(miniProgramCoverImgUrl);
        }
        return shpShop;
    }

    private ShpDetail pickShpDetail(ParamUpdateShopInfo shopInfo, boolean isSave) {
        ShpDetail shpDetail = new ShpDetail();
        String province = shopInfo.getProvince();
        String city = shopInfo.getCity();
        if (isSave) {
            //新增
            shpDetail.setShopkeeperPhone(getUsername());
            shpDetail.setInsertTime(new Date());
            shpDetail.setVersions(1);
            shpDetail.setDel("0");
        } else {
            //修改
            shpDetail.setUpdateTime(new Date());
            shpDetail.setFkShpShopId(getShopId());
        }
        if (!LocalUtils.isEmptyAndNull(province)) {
            shpDetail.setProvince(province);
        }
        if (!LocalUtils.isEmptyAndNull(city)) {
            shpDetail.setCity(city);
        }
        return shpDetail;
    }


}
