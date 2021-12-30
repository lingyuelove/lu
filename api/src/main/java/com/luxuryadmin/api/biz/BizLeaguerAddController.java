package com.luxuryadmin.api.biz;

import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.constant.enums.EnumCode;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.biz.BizLeaguer;
import com.luxuryadmin.entity.biz.BizLeaguerAdd;
import com.luxuryadmin.enums.op.EnumOpMessageJumpType;
import com.luxuryadmin.enums.op.EnumOpMessagePushPlatform;
import com.luxuryadmin.enums.op.EnumOpMessageSubType;
import com.luxuryadmin.enums.op.EnumOpMessageType;
import com.luxuryadmin.enums.shp.EnumShpOperateLogModule;
import com.luxuryadmin.param.biz.ParamAddLeaguer;
import com.luxuryadmin.param.biz.ParamBecomeLeaguer;
import com.luxuryadmin.param.biz.ParamLeaguerShopId;
import com.luxuryadmin.param.biz.ParamShopNumberToken;
import com.luxuryadmin.param.op.ParamOpMessageSave;
import com.luxuryadmin.param.shp.ParamAddShpOperateLog;
import com.luxuryadmin.service.biz.BizLeaguerAddService;
import com.luxuryadmin.service.biz.BizLeaguerService;
import com.luxuryadmin.service.op.OpMessageService;
import com.luxuryadmin.service.shp.*;
import com.luxuryadmin.vo.biz.VoBizLeaguer;
import com.luxuryadmin.vo.biz.VoBizLeaguerAdd;
import com.luxuryadmin.vo.shp.VoUserShopBase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 友商模块
 *
 * @author monkey king
 * @date 2020-01-11 21:52:16
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/biz", method = RequestMethod.POST)
@Api(tags = {"G001.【友商】模块 --2.6.3❤ --zs"}, description = "/shop/user/biz |添加友商好友 --2.6.3❤ --zs")
@RequiresPermissions("leaguer:check:shop")
public class BizLeaguerAddController extends BizLeaguerBaseController {

    @Autowired
    private BizLeaguerAddService bizLeaguerAddService;

    @Autowired
    private BizLeaguerService bizLeaguerService;

    @Autowired
    private ShpShopService shpShopService;

    @Autowired
    private OpMessageService opMessageService;

    @Autowired
    private ShpUserShopRefService shpUserShopRefService;

    //@Autowired
    //private ShpUserPermissionRefService shpUserPermissionRefService;

    @Autowired
    private ShpPermUserRefService shpPermUserRefService;

    @Autowired
    private ShpOperateLogService shpOperateLogService;

    /**
     * 获取友商申请添加好友记录
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "获取友商申请添加好友记录 --2.6.3❤ --zs",
            notes = "获取友商申请添加好友记录 --2.6.3❤ --zs",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "pageNum", value = "当前页"),
    })
    @RequestRequire
    @RequestMapping("/listLeaguerAddRecord")
    public BaseResult<List<VoBizLeaguerAdd>> listLeaguerAddRecord(@RequestParam Map<String, String> params) {

        boolean newVersion = newAndroidAndIOSVersion("2.1.0", "2.2.1");
        //新版本权限,临时做此判断
        if (newVersion) {
            boolean leaguerAddPerm = hasPermWithCurrentUser(ConstantPermission.MOD_LEAGUER_ADD);
            if (!leaguerAddPerm) {
                return BaseResult.errorResult(EnumCode.ERROR_NO_SHOP_PERMISSION);
            }
        }
        PageHelper.startPage(getPageNum(), PAGE_SIZE_10);
        List<VoBizLeaguerAdd> leaguerAddList = bizLeaguerAddService.listBizLeaguerAddByShopId(getShopId());
        if (!LocalUtils.isEmptyAndNull(leaguerAddList)) {
            for (VoBizLeaguerAdd leaguerAdd : leaguerAddList) {
                String headImageUrl = leaguerAdd.getHeadImgUrl();
                leaguerAdd.setHeadImgUrl(servicesUtil.formatImgUrl(headImageUrl));
            }
        }
        return LocalUtils.getBaseResult(leaguerAddList);
    }

    /**
     * 搜索友商(店铺)
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "搜索友商(店铺)",
            notes = "搜索友商(店铺)",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/searchLeaguerShop")
    public BaseResult<VoBizLeaguer> searchLeaguerShop(@RequestParam Map<String, String> params,
                                                      @Valid ParamShopNumberToken paramVo, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String resultStr = "你搜索的是自己的店铺哦~";
        String shopNumber = paramVo.getShopNumber();
        String myShopNumber = getShopNumber();
        if (shopNumber.equals(myShopNumber)) {
            return BaseResult.defaultErrorWithMsg(resultStr);
        }
        List<VoBizLeaguer> voBizLeaguerList = bizLeaguerService.searchShop(getShopId(), shopNumber);
        if (!LocalUtils.isEmptyAndNull(voBizLeaguerList)) {
            if (voBizLeaguerList.size() == 1) {
                //如果该店铺是自己;
                if (getShopId() == voBizLeaguerList.get(0).getShopId()) {
                    return BaseResult.defaultErrorWithMsg(resultStr);
                }
            }
            for (VoBizLeaguer voBizLeaguer : voBizLeaguerList) {
                formatVoBizLeaguer(voBizLeaguer);
                if (!LocalUtils.isEmptyAndNull(voBizLeaguer)) {
                    BizLeaguer bizLeaguer = bizLeaguerService.getBizLeaguerByShopIdAndLeaguerShopId(getShopId(), voBizLeaguer.getShopId());
                    if (LocalUtils.isEmptyAndNull(bizLeaguer)) {
                        //不是友商,以下信息不能查看
                        voBizLeaguer.setShopkeeperNickname(null);
                        voBizLeaguer.setPhone(null);
                        voBizLeaguer.setVisible(null);
                        voBizLeaguer.setTop(null);
                    }
                }
            }
        }
        return LocalUtils.getBaseResult(voBizLeaguerList);
    }

    /**
     * 添加友商好友
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "添加友商好友 --2.6.3❤ --zs",
            notes = "添加友商好友 --2.6.3❤ --zs",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/requestAddLeaguer")
    public BaseResult requestAddLeaguer(@RequestParam Map<String, String> params,
                                        @Valid ParamAddLeaguer paramAddLeaguer, BindingResult result,HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        boolean newVersion = newAndroidAndIOSVersion("2.1.0", "2.2.1");
        //新版本权限,临时做此判断
        if (newVersion) {
            boolean leaguerAddPerm = hasPermWithCurrentUser(ConstantPermission.MOD_LEAGUER_ADD);
            if (!leaguerAddPerm) {
                return BaseResult.errorResult(EnumCode.ERROR_NO_SHOP_PERMISSION);
            }
        }
        String leaguerShopIdStr = paramAddLeaguer.getLeaguerShopId();
        String requestMsg = paramAddLeaguer.getRequestMsg();
        int leaguerShopId = Integer.parseInt(leaguerShopIdStr);
        int myShopId = getShopId();
        if (leaguerShopId == myShopId) {
            return BaseResult.defaultErrorWithMsg("不能添加自己为友商!");
        }
        paramAddLeaguer.setMyShopId(myShopId);
        paramAddLeaguer.setUserId(getUserId());
        bizLeaguerAddService.saveBizLeaguerAdd( paramAddLeaguer);

        //推送友商消息
        pushRequestAddLeaguerMsg(leaguerShopId, myShopId);

        //添加【店铺操作日志】-【申请添加友商】
        VoUserShopBase voUserShopBaseLeaguer = shpShopService.getVoUserShopBaseByShopId(leaguerShopId);
        if(null!=voUserShopBaseLeaguer) {
            ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
            paramAddShpOperateLog.setShopId(myShopId);
            paramAddShpOperateLog.setOperateUserId(getUserId());
            paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.LEAGUER_ALBUM.getName());
            paramAddShpOperateLog.setOperateName("申请添加友商");
            paramAddShpOperateLog.setOperateContent("【" + voUserShopBaseLeaguer.getShopName() + "-" + leaguerShopId + "】");
            paramAddShpOperateLog.setProdId(null);
            paramAddShpOperateLog.setOrderId(null);
            paramAddShpOperateLog.setRequest(request);
            shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
        }
        return BaseResult.okResult();
    }

    /**
     * 发送添加友商消息
     *
     * @param leaguerShopId
     * @param myShopId
     */
    private void pushRequestAddLeaguerMsg(Integer leaguerShopId, Integer myShopId) {
        try {

            String content;

            String shopName = shpShopService.getShpShopById("" + myShopId).getName();
            content = shopName + "请求添加为友商~";

            ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
            paramOpMessageSave.setType(EnumOpMessageType.FRIENDBUSINESS.getCode());
            paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
            paramOpMessageSave.setTitle("友商添加申请");
            paramOpMessageSave.setSubType(EnumOpMessageSubType.APPLY_ADD_FRIEND_SERVICE.getCode());
            paramOpMessageSave.setContent(content);

            opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NO_JUMP);

            String perm = ConstantPermission.CHK_ALL_ORDER;

            boolean newVersion = newAndroidAndIOSVersion("2.1.0", "2.2.1");
            if (newVersion) {
                //新版本用添加友商的权限来控制通知
                perm = ConstantPermission.MOD_LEAGUER_ADD;
            }

            //获取[添加友商]消息用户ID
            List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                    leaguerShopId, perm);
            opMessageService.addOpMessage(paramOpMessageSave, null, leaguerShopId, pushUserIdList, null);
        } catch (Exception e) {
            log.error("推送添加友商消息异常:" + e);
        }
    }

    /**
     * 操作申请添加友商记录
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "通过友商申请,成为友商",
            notes = "通过友商申请,成为友商",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/becomeLeaguer")
    public BaseResult becomeLeaguer(@RequestParam Map<String, String> params,
                                    @Valid ParamBecomeLeaguer paramAddRecord, BindingResult result,HttpServletRequest request) {
        servicesUtil.validControllerParam(result);

        boolean newVersion = newAndroidAndIOSVersion("2.1.0", "2.2.1");
        //新版本权限,临时做此判断
        if (newVersion) {
            boolean leaguerAddPerm = hasPermWithCurrentUser(ConstantPermission.MOD_LEAGUER_ADD);
            if (!leaguerAddPerm) {
                return BaseResult.errorResult(EnumCode.ERROR_NO_SHOP_PERMISSION);
            }
        }

        String leaguerShopIdStr = paramAddRecord.getLeaguerShopId();
        String note = paramAddRecord.getNote();
        String visible = paramAddRecord.getVisible();
        int leaguerShopId = Integer.parseInt(leaguerShopIdStr);
        int shopId = getShopId();
        //查找是否已经成为友商;
        BizLeaguer bizLeaguer = bizLeaguerService.getBizLeaguerByShopIdAndLeaguerShopId(shopId, leaguerShopId);
        if (!LocalUtils.isEmptyAndNull(bizLeaguer)) {
            throw new MyException("该店铺已经是你的友商,不能重复添加!");
        }
        //因为是'自己'操作别人添加自己的记录,所以,'自己'属于被动;因此本店铺的shopId属于被动添加shopId
        BizLeaguerAdd bizLeaguerAdd = bizLeaguerAddService.getLeaguerAddByShopIdAndLeaguerShopId(leaguerShopId, shopId);
        if (LocalUtils.isEmptyAndNull(bizLeaguerAdd)) {
            return BaseResult.defaultErrorWithMsg("找不到添加友商记录");
        }
        String dbState = bizLeaguerAdd.getState();
        if ("20".equals(dbState)) {
            return BaseResult.defaultErrorWithMsg("该店铺已经是你的友商,不能重复添加!");
        }
        bizLeaguerAdd.setInsertAdmin(getUserId());
        bizLeaguerAddService.updateBizLeaguerAdd(bizLeaguerAdd, "20", note, visible);

        pushOperateLeaguerAddRecord(leaguerShopId, shopId);

        //添加【店铺操作日志】-【添加友商成功】
        VoUserShopBase voUserShopBaseLeaguer = shpShopService.getVoUserShopBaseByShopId(leaguerShopId);
        if(null!=voUserShopBaseLeaguer) {
            ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
            paramAddShpOperateLog.setShopId(shopId);
            paramAddShpOperateLog.setOperateUserId(getUserId());
            paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.LEAGUER_ALBUM.getName());
            paramAddShpOperateLog.setOperateName("同意添加友商");
            paramAddShpOperateLog.setOperateContent("【" + voUserShopBaseLeaguer.getShopName() + "-" + leaguerShopId + "】");
            paramAddShpOperateLog.setProdId(null);
            paramAddShpOperateLog.setOrderId(null);
            paramAddShpOperateLog.setRequest(request);
            shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
        }
        return BaseResult.okResult();
    }


    /**
     * 删除友商申请记录
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "删除友商申请记录",
            notes = "删除友商申请记录",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/deleteLeaguerAddRecord")
    public BaseResult deleteLeaguerAddRecord(@RequestParam Map<String, String> params,
                                             @Valid ParamLeaguerShopId paramDeleteRecord, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);

        boolean newVersion = newAndroidAndIOSVersion("2.1.0", "2.2.1");
        //新版本权限,临时做此判断
        if (newVersion) {
            boolean leaguerAddPerm = hasPermWithCurrentUser(ConstantPermission.MOD_LEAGUER_DELETE);
            if (!leaguerAddPerm) {
                return BaseResult.errorResult(EnumCode.ERROR_NO_SHOP_PERMISSION);
            }
        }

        String leaguerShopIdStr = paramDeleteRecord.getLeaguerShopId();
        int leaguerShopId = Integer.parseInt(leaguerShopIdStr);
        int shopId = getShopId();
        //因为是'自己'操作别人添加自己的记录,所以,'自己'属于被动;因此本店铺的shopId属于被动添加shopId
        BizLeaguerAdd bizLeaguerAdd = bizLeaguerAddService.getLeaguerAddByShopIdAndLeaguerShopId(leaguerShopId, shopId);
        if (LocalUtils.isEmptyAndNull(bizLeaguerAdd)) {
            return BaseResult.defaultErrorWithMsg("找不到申请友商记录");
        }
        bizLeaguerAdd.setInsertAdmin(getUserId());
        bizLeaguerAddService.updateBizLeaguerAdd(bizLeaguerAdd, "-10", null, null);

        return BaseResult.okResult();
    }

    /**
     * 发送添加友商成功消息
     *
     * @param leaguerShopId
     * @param myShopId
     */
    private void pushOperateLeaguerAddRecord(Integer leaguerShopId, Integer myShopId) {
        try {

            String content;

            String shopName = shpShopService.getShpShopById("" + myShopId).getName();
            content = shopName + "已同意添加您为友商，快去他的店铺看看吧~";

            ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
            paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
            paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
            paramOpMessageSave.setTitle("添加友商成功");
            paramOpMessageSave.setSubType(EnumOpMessageSubType.ADD_SHOP_USER_FRIEND_SUCCESS.getCode());
            paramOpMessageSave.setContent(content);
            opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NO_JUMP);

            //获取[添加友商]消息用户ID
            List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                    leaguerShopId, ConstantPermission.MOD_LEAGUER_ADD);
            opMessageService.addOpMessage(paramOpMessageSave, null, leaguerShopId, pushUserIdList, null);
        } catch (Exception e) {
            log.error("推送添加友商消息异常:" + e);
        }
    }

}
