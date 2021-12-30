package com.luxuryadmin.api.op;

import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.enums.op.EnumOpMessageSubType;
import com.luxuryadmin.enums.op.EnumOpMessageType;
import com.luxuryadmin.enums.sys.EnumSysConfigNew;
import com.luxuryadmin.param.op.ParamOpMessageAdd;
import com.luxuryadmin.param.op.ParamOpMessageSave;
import com.luxuryadmin.service.op.OpMessageService;
import com.luxuryadmin.service.shp.ShpUserPermissionRefService;
import com.luxuryadmin.service.shp.ShpUserService;
import com.luxuryadmin.service.sys.SysConfigNewService;
import com.luxuryadmin.vo.op.VoMessageSubTypeList;
import com.luxuryadmin.vo.op.VoOpMessageDetail;
import com.luxuryadmin.vo.op.VoOpMessageUnreadCount;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sanjin
 * @Classname OpProblemController
 * @Description 帮助中心Controller
 * @Date 2020/7/13 15:00
 * @Created by sanjin
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/op/message")
@Api(tags = {"1.【消息中心】模块"}, description = "/shop/user/op/message | 【消息中心】 ")
public class OpMessageController extends BaseController {

    @Autowired
    private OpMessageService opMessageService;

    @Autowired
    private ShpUserService shpUserService;

    @Autowired
    private SysConfigNewService sysConfigNewService;

    @Autowired
    private ShpUserPermissionRefService shpUserPermissionRefService;

    @RequestMapping(value = "/testAddOpMessage", method = RequestMethod.POST)
    @ApiOperation(
            value = "测试新增【消息】;",
            notes = "测试新增 【消息】;</br>此接口无法新增友商消息类型",
            httpMethod = "POST",
            position = 1)
    public BaseResult testAddOpMessage(ParamOpMessageAdd testAddParam) {
        try {
            //testBatchAddOpMessage(testAddParam);
        } catch (Exception e) {
            log.error("" + e);
            return BaseResult.errorResult(e.getMessage());
        }
        return BaseResult.okResult(Boolean.TRUE);

    }

    @RequestMapping(value = "/loadOpMessageUnReadCount", method = RequestMethod.POST)
    @ApiOperation(
            value = "根据店铺ID获取【消息】未读数量;",
            notes = "根据店铺ID获取【消息】未读数量",
            httpMethod = "POST",
            position = 2)
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true)})
    public BaseResult loadOpMessageUnReadCount() {
        Integer shopId = getShopId();
        Integer userId = getUserId();

        VoOpMessageUnreadCount voOpMessageUnreadCount;
        try {
            voOpMessageUnreadCount = opMessageService.loadOpMessageUnReadCountByShopId(shopId, userId);
            String intervalSeconds = sysConfigNewService.getSysConfigByMasterKeyAndSubKey(EnumSysConfigNew.MSG_POLL_INTERVAL_SECONDS);
            voOpMessageUnreadCount.setPollInterval(Integer.valueOf(intervalSeconds));
            //如果没有友商权限, 则友商消息设置为0;
            if (!hasPermWithCurrentUser(ConstantPermission.MOD_LEAGUER_ADD)) {
                Integer totalNum = voOpMessageUnreadCount.getTotalUnReadCount();
                Integer leaguerMsgNum = voOpMessageUnreadCount.getFriendBusinessMessageUnreadCount();
                //减去友商数量
                BigDecimal num = LocalUtils.calcNumber(totalNum, "-", leaguerMsgNum);
                voOpMessageUnreadCount.setTotalUnReadCount(num.intValue());
                voOpMessageUnreadCount.setFriendBusinessMessageUnreadCount(0);
            }
        } catch (Exception e) {
            log.error("" + e);
            return BaseResult.errorResult(e.getMessage());
        }
        return BaseResult.okResult(voOpMessageUnreadCount);
    }

    @RequestMapping(value = "/loadOpMessageOtherList", method = RequestMethod.POST)
    @ApiOperation(
            value = "根据店铺ID获取非【店铺消息|友商消息|系统消】的消息列表，展示在APP消息页面下面;",
            notes = "根据店铺ID获取【消息】列表;不包含type在【店铺消息|友商消息|系统消息】里的消息<br/>" +
                    "1.【店主】可以查看店铺内的所有【其它消息】<br/>" +
                    "2. 店铺的【其他用户】只能查看自己对应【用户ID】的【其它消息】",
            httpMethod = "POST",
            position = 3)
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true)})
    public BaseResult<List<VoOpMessageDetail>> loadOpMessageOtherList() {
        Integer shopId = getShopId();
        Integer userId = getUserId();

        List<VoOpMessageDetail> opMessageList = new ArrayList<>();
        try {
            PageHelper.startPage(getPageNum(), PAGE_SIZE_20);
            opMessageList = opMessageService.loadOpMessageOtherListByShopIdAndUserId(shopId, userId);
        } catch (Exception e) {
            log.error("" + e);
        }
        return BaseResult.okResult(opMessageList);
    }

    @RequestMapping(value = "/loadSubTypeCnNameList", method = RequestMethod.POST)
    @ApiOperation(
            value = "获取店铺消息子类型;",
            notes = "获取店铺消息子类型<br/>",
            httpMethod = "POST",
            position = 3)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true),
    })
    public BaseResult<VoMessageSubTypeList> loadSubTypeCnNameList() {
        VoMessageSubTypeList voMessageSubTypeCnName = opMessageService.loadSubTypeCnNameList();
        BaseResult baseResult = BaseResult.okResult(voMessageSubTypeCnName);
        return baseResult;
    }


    @RequestMapping(value = "/loadOpMessageListByType", method = RequestMethod.POST)
    @ApiOperation(
            value = "根据类型获取消息列表;",
            notes = "根据类型获取消息列表<br/>",
            httpMethod = "POST",
            position = 3)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "type", value = "消息类型", allowableValues = "shop,system", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "subType", value = "店铺消息子类型", required = false)
    })
    public BaseResult<List<VoOpMessageDetail>> loadOpMessageListByType(String type, String subType) {
        Integer shopId = getShopId();
        Integer userId = getUserId();

        if (StringUtil.isBlank(type) && !StringUtil.isBlank(subType)) {
            throw new MyException("只有店铺类型需要子类型");
        }

        List<VoOpMessageDetail> opMessageList = new ArrayList<>();
        try {
            PageHelper.startPage(getPageNum(), PAGE_SIZE_20);
            opMessageList = opMessageService.loadOpMessageOtherListByType(shopId, userId, type, subType);
        } catch (Exception e) {
            log.error("" + e);
        }
        BaseResult baseResult = BaseResult.okResult(opMessageList);
        return baseResult;
    }


    @RequestMapping(value = "/updateOpMessageReadStateByRefId", method = RequestMethod.POST)
    @ApiOperation(
            value = "根据ID修改【消息】状态为已读;",
            notes = "根据ID修改【消息】状态为已读;",
            httpMethod = "POST",
            position = 4)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "id", value = "列表接口返回的消息ID", allowableValues = "shop,system", required = true)
    })
    public BaseResult updateOpMessageReadStateByRefId(String id) {
        if (null == id) {
            return BaseResult.errorResult("消息关联ID为空");
        }
        Integer shopId = getShopId();
        Integer userId = getUserId();
        if (shopId.equals(0) || userId.equals(0)) {
            return BaseResult.errorResult("全部已读所有【消息】没有获取到Token对应的【店铺ID】或【用户ID】");
        }

        Boolean result;
        try {
            result = opMessageService.updateOpMessageReadStateByRefId(Long.parseLong(id));
        } catch (Exception e) {
            log.error("" + e);
            return BaseResult.errorResult(e.getMessage());
        }
        return BaseResult.okResult(result);
    }

    @RequestMapping(value = "/updateAllUnReadOpMessage", method = RequestMethod.POST)
    @ApiOperation(
            value = "全部已读所有【消息】,不包含type在【友商消息】里的消息，但包含【店铺消息|系统消息|其他消息】;",
            notes = "全部已读所有【消息】,不包含type在【友商消息】里的消息，但包含【店铺消息|系统消息|其他消息】",
            httpMethod = "POST",
            position = 5)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true),
    })
    public BaseResult updateAllUnReadOpMessage() {
        Integer shopId = getShopId();
        Integer userId = getUserId();

        Boolean result;
        try {
            result = opMessageService.updateAllUnReadOpMessageByShopIdAndUserId(shopId, userId);
        } catch (Exception e) {
            log.error("" + e);
            return BaseResult.errorResult(e.getMessage());
        }
        return BaseResult.okResult(result);
    }

    @RequestMapping(value = "/delOpMessageRefById", method = RequestMethod.POST)
    @ApiOperation(
            value = "根据ID删除【消息】;",
            notes = "根据ID删除【消息】;",
            httpMethod = "POST",
            position = 6)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "id", value = "列表接口返回的消息ID", allowableValues = "shop,system", required = true)
    })
    public BaseResult delOpMessageRefById(String id) {
        if (StringUtil.isBlank(id)) {
            return BaseResult.errorResult("删除【消息】ID为空");
        }

        Boolean result;
        try {
            result = opMessageService.delOpMessageRefById(Long.parseLong(id));
        } catch (Exception e) {
            log.error("" + e);
            return BaseResult.errorResult(e.getMessage());
        }
        return BaseResult.okResult(result);
    }

    /*************************************************   私有方法   *************************************************/
    private void testBatchAddOpMessage(ParamOpMessageAdd testAddParam) throws Exception {
        //插入消息模板
        ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
        paramOpMessageSave.setTitle(testAddParam.getTitle());
        paramOpMessageSave.setContent(testAddParam.getContent());
        paramOpMessageSave.setClickH5Url("http://www.baidu.com");
        paramOpMessageSave.setType(testAddParam.getMsgType());

        String subType = EnumOpMessageSubType.ADD_SHOP_USER_FRIEND_SUCCESS.getCode();
        paramOpMessageSave.setSubType(subType);

        /**
         * 获取【消息类型】和【消息子类型】
         */
        EnumOpMessageType enumType = EnumOpMessageType.getEnumByCode(testAddParam.getMsgType());
        if (null == enumType) {
            throw new Exception("传入无效的【消息类型】");
        }

        /**
         * 获取新增的【消息ID】
         */
        Long msgId = opMessageService.addOpMessage(paramOpMessageSave, null, null, null, null);
        testSingleShopAddOpMessage(testAddParam.getShopId(), testAddParam.getPhone(), msgId);
    }

    private void testSingleShopAddOpMessage(Integer shopId, String phone, Long msgId) throws Exception {
        Integer userId = shpUserService.getShpUserIdByUsername(phone);
        if (null == userId) {
            throw new Exception("数据库中没有对应店铺用户手机号=" + phone + "的用户");
        }

        List<Integer> userIdList = new ArrayList<>();
        userIdList.add(userId);

        opMessageService.addOpMessageShopUserRef(shopId, userIdList, msgId, EnumOpMessageType.SYSTEM.getCode(), null);
    }

}
