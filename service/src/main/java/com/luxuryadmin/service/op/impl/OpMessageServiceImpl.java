package com.luxuryadmin.service.op.impl;

import cn.jpush.api.push.model.Platform;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.ExcelUtils;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.entity.op.OpMessage;
import com.luxuryadmin.entity.op.OpMessageShopUserRef;
import com.luxuryadmin.enums.op.*;
import com.luxuryadmin.mapper.biz.BizLeaguerAddMapper;
import com.luxuryadmin.mapper.op.OpMessageMapper;
import com.luxuryadmin.mapper.op.OpMessageShopUserRefMapper;
import com.luxuryadmin.mapper.shp.ShpShopMapper;
import com.luxuryadmin.param.op.ParamOpMessageQuery;
import com.luxuryadmin.param.op.ParamOpMessageSave;
import com.luxuryadmin.service.op.OpMessageService;
import com.luxuryadmin.service.op.OpPushService;
import com.luxuryadmin.service.shp.ShpUserService;
import com.luxuryadmin.vo.op.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 用户管理--消息中心Service实现类
 *
 * @author sanjin
 * @date 2020-07-13 16:04:06
 */
@Service
@Slf4j
public class OpMessageServiceImpl implements OpMessageService {

    @Resource
    private OpMessageMapper opMessageMapper;

    @Resource
    private BizLeaguerAddMapper bizLeaguerAddMapper;

    @Resource
    private ShpUserService shpUserService;

    @Resource
    private OpMessageShopUserRefMapper opMessageShopUserRefMapper;

    @Resource
    private OpPushService opPushService;

    @Resource
    private ShpShopMapper shpShopMapper;

    @Autowired
    protected RedisUtil redisUtil;

    /**
     * 新增消息方法
     *
     * @param paramOpMessageSave 消息主体内容参数
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addOpMessage(ParamOpMessageSave paramOpMessageSave, Integer uid, Integer shopId, List<Integer> userIdList, Map<String, String> extraParam) throws Exception {
        /**
         * step1.校验参数
         */
        checkOpMessageParam(paramOpMessageSave, userIdList);

        /**
         * step2.构造新增消息主体批次对象opMessage
         */
        OpMessage opMessage = new OpMessage();
        BeanUtils.copyProperties(paramOpMessageSave, opMessage);

        //设置图标
        String titleImgUrl = getTitleImgUrlByMessage(opMessage, shopId);
        opMessage.setTitleImgUrl(titleImgUrl);
        ;

        //默认未点击状态
        opMessage.setInsertTime(new Date());
        opMessage.setUpdateTime(new Date());
        opMessageMapper.insertSelective(opMessage);

        /**
         * step3.如果【发送类型sendType】是【立即发送】
         *       调用立即发送方法rightSend
         */
        Long messageId = opMessage.getId();
        String sendType = opMessage.getSendType();
        if (EnumOpMessageSendType.RIGHT_NOW.getCode().equals(sendType)) {
            rightSend(messageId, uid, userIdList, shopId, extraParam);
        }

        return messageId;
    }

    /**
     * 根据消息类型，获取消息图标
     *
     * @param opMessage
     * @return
     */
    private String getTitleImgUrlByMessage(OpMessage opMessage, Integer shopId) {
//        String titleImgUrl = "";
//        String type = opMessage.getType();
//        switch (type){
//            case "system":
//                titleImgUrl = ConstantCommon.ossDomain + "/push/msgImg/msgSystemImg.png";
//                break;
//            case "other":
//                titleImgUrl = ConstantCommon.ossDomain + "/push/msgImg/msgOtherImg.png";
//                break;
//            case "shop":
//                titleImgUrl = ConstantCommon.ossDomain + "/push/msgImg/msgShopImg.png";
////                ShpShop shpShop =  (ShpShop)shpShopMapper.getObjectById(shopId);
////                String coverImgUrl = shpShop.getCoverImgUrl();
////                if(StringUtil.isBlank(coverImgUrl) || "默认封面".equals(coverImgUrl)){
////
////                }else{
////                    titleImgUrl = ConstantCommon.ossDomain + coverImgUrl;
////                }
//                break;
//            default:
//                titleImgUrl="";
//                break;
//        }

        String titleImgUrl = getTitleImgUrlByMessage(opMessage.getSubType());
        return titleImgUrl;
    }

    private String getTitleImgUrlByMessage(String subType) {
        /*String urlImg = "";
        for (EnumOpMessageSubType enums : EnumOpMessageSubType.values()) {
            if (enums.getCode().equals(subType)) {
                urlImg = ConstantCommon.ossDomain + "/push/msgImg/" + subType + ".png";
            }
        }
        return urlImg;*/
        String titleImgUrl = "";
        switch (subType) {
            //商品售出通知
            case "saleProd":
                titleImgUrl = ConstantCommon.ossDomain + "/push/msgImg/saleProd.png";
                break;
            //商品上架通知
            case "releaseProd":
                titleImgUrl = ConstantCommon.ossDomain + "/push/msgImg/releaseProd.png";
                break;
            //删除商品通知
            case "deleteProd":
                titleImgUrl = ConstantCommon.ossDomain + "/push/msgImg/deleteProd.png";
                break;
            //修改商品
            case "updateProdPrice":
                titleImgUrl = ConstantCommon.ossDomain + "/push/msgImg/updateProdPrice.png";
                break;
            //修改商品成本价
            case "updateProdInitPrice":
                titleImgUrl = ConstantCommon.ossDomain + "/push/msgImg/updateProdInitPrice.png";
                break;
            //质押商品赎回
            case "redeemProd":
                titleImgUrl = ConstantCommon.ossDomain + "/push/msgImg/redeemProd.png";
                break;
            //质押商品到期
            case "expireProd":
                titleImgUrl = ConstantCommon.ossDomain + "/push/msgImg/expireProd.png";
                break;
            //商品入库通知
            case "uploadProd":
                titleImgUrl = ConstantCommon.ossDomain + "/push/msgImg/uploadProd.png";
                break;
            //寄卖取回
            case "recycleProd":
                titleImgUrl = ConstantCommon.ossDomain + "/push/msgImg/recycleProd.png";
                break;
            //商品锁单
            case "lockProd":
                titleImgUrl = ConstantCommon.ossDomain + "/push/msgImg/lockProd.png";
                break;
            //订单退货
            case "cancelOrder":
                titleImgUrl = ConstantCommon.ossDomain + "/push/msgImg/cancelOrder.png";
                break;
            //更新订单
            case "updateOrder":
                titleImgUrl = ConstantCommon.ossDomain + "/push/msgImg/updateOrder.png";
                break;
            //每日日报
            case "dailyReport":
                titleImgUrl = ConstantCommon.ossDomain + "/push/msgImg/dailyReport.png";
                break;
            //每月月报
            case "dailyReportForMonth":
                titleImgUrl = ConstantCommon.ossDomain + "/push/msgImg/dailyReportForMonth.png";
                break;
            //到期商品提醒
            case "expiredProduct":
                titleImgUrl = ConstantCommon.ossDomain + "/push/msgImg/expiredProduct.png";
                break;
            //商家联盟审核结果通知
            case "unionVerify":
                titleImgUrl = ConstantCommon.ossDomain + "/push/msgImg/unionVerify.png";
                break;
            default:
                titleImgUrl = "";
                break;
        }
        return titleImgUrl;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int rightSend(Long msgId, Integer uid, List<Integer> userIdList, Integer shopId, Map<String, String> extraParam)
            throws Exception {

        /**
         * step1.获取消息发送对象
         */
        OpMessage opMessage = opMessageMapper.selectByPrimaryKey(msgId);
        String msgType = opMessage.getType();
        String pushPlatform = opMessage.getPushPlatform();
        String msgContent = opMessage.getContent();
        String title = opMessage.getTitle();


        /**
         * step2.调用消息添加接口
         */
        if (CollectionUtils.isEmpty(userIdList)) {
            userIdList = constructUserIdListByMessage(opMessage);
        }
        if (CollectionUtils.isEmpty(userIdList)) {
            throw new Exception("推送用户列表为空");
        }
        addOpMessageShopUserRef(shopId, userIdList, msgId, msgType, extraParam);

        /**
         * step3.调用通知推送接口
         */

        Platform platform = getPushPlatform(pushPlatform);
        List<String> userIdStrList = new ArrayList<>();
        for (Integer userId : userIdList) {
            userIdStrList.add(userId + "");
        }
        opPushService.pushJiGuangMessage(platform, userIdStrList, title, msgContent, extraParam);

        /**
         * step4.修改消息发送状态
         */
        opMessage.setPushState(EnumOpMessagePushState.HAVE_PUSH.getCode());
        opMessage.setSendTime(new Date());
        opMessage.setUpdateAdmin(uid);
        opMessage.setUpdateTime(new Date());
        return opMessageMapper.updateByPrimaryKeySelective(opMessage);
    }

    /**
     * 自动定时发送
     */
    @Override
    public void autoTimerPush() {

        /**
         * 查询所有的【预发送时间】<= 【当前时间】的，并且【推送状态】为未推送的。
         */
        List<Long> pushMsgIdList = opMessageMapper.selectAllUnPushPreSendMsgId();
        log.info("**************************** 一共查询到{}条待发送的消息 ***************************", pushMsgIdList.size());
        for (Long msgId : pushMsgIdList) {
            try {
                rightSend(msgId, null, null, null, null);
            } catch (Exception e) {
                log.error("" + e);
            }
        }
    }

    /**
     * 根据所有子类型列表
     *
     * @return
     */
    @Override
    public VoMessageSubTypeList loadSubTypeCnNameList() {
        VoMessageSubTypeList voList = new VoMessageSubTypeList();

        //切换栏展示子类型列表
        List<VoMessageSubType> switchTabSubTypeList = new ArrayList<>();
        voList.setSwitchTabSubTypeList(switchTabSubTypeList);

        //切换拦子类型1-入库
        VoMessageSubType subTypeInRepo = new VoMessageSubType();
        subTypeInRepo.setCode(EnumOpMessageSubType.UPLOAD_PROD.getCode());
        subTypeInRepo.setCnName("入库");
        switchTabSubTypeList.add(subTypeInRepo);

        //切换拦子类型2-售出
        VoMessageSubType subTypeSellOut = new VoMessageSubType();
        subTypeSellOut.setCode(EnumOpMessageSubType.SALE_PROD.getCode());
        subTypeSellOut.setCnName("售出");
        switchTabSubTypeList.add(subTypeSellOut);

        //切换拦子类型3-退货
        VoMessageSubType subTypeReturnProd = new VoMessageSubType();
        subTypeReturnProd.setCode(EnumOpMessageSubType.CANCEL_ORDER.getCode());
        subTypeReturnProd.setCnName("退货");
        switchTabSubTypeList.add(subTypeReturnProd);

        //切换拦子类型4-日报
        VoMessageSubType subTypeDailyReport = new VoMessageSubType();
        subTypeDailyReport.setCode(EnumOpMessageSubType.DAILY_REPORT.getCode());
        subTypeDailyReport.setCnName("日报");
        switchTabSubTypeList.add(subTypeDailyReport);

        //所有子类型列表
        List<VoMessageSubType> allSubTypeList = EnumOpMessageSubType.getAllShopMsgSubTypeCnName();
        voList.setAllSubTypeList(allSubTypeList);

        return voList;
    }

    private Platform getPushPlatform(String pushPlatform) {
        if (StringUtil.isBlank(pushPlatform) || EnumOpMessagePushPlatform.ALL.equals(pushPlatform)) {
            return Platform.all();
        } else if (EnumOpMessagePushPlatform.IOS.equals(pushPlatform)) {
            return Platform.ios();
        } else if (EnumOpMessagePushPlatform.ANDROID.equals(pushPlatform)) {
            return Platform.android();
        }
        return Platform.all();
    }

    /**
     * 根据消息构造用户列表
     *
     * @param opMessage
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    private List<Integer> constructUserIdListByMessage(OpMessage opMessage) throws Exception {
        List<Integer> userIdList;
        String isPushAllUser = opMessage.getIsPushAllUser();
        /**
         * 如果推送全部用户，则查询所有【shp_user】表里的用户ID
         *
         * 如果不是推送全部用户，则根据上传的Excel文件获取【用户ID】列表
         */
        if (EnumOpMessageIsPushAllUser.YES.getCode().equals(isPushAllUser)) {
            userIdList = shpUserService.selectAllValidUserId();
        } else if (EnumOpMessageIsPushAllUser.NO.getCode().equals(isPushAllUser)) {
            String excelFileUrl = opMessage.getPushUserExcelUrl();
            userIdList = parseExcelUserList(excelFileUrl);
        } else {
            throw new Exception("无效的是否全部推送类型");
        }

        return userIdList;
    }

    /**
     * 根据文件解析用户ID列表
     *
     * @param excelFileUrl
     * @return
     */
    private List<Integer> parseExcelUserList(String excelFileUrl) throws Exception {
        List<Integer> userIdList = new ArrayList<>();
        List<String> phoneList = ExcelUtils.parseExcelByUrl(excelFileUrl, String.class);
        if (CollectionUtils.isEmpty(phoneList)) {
            throw new Exception("上传的文件列表为空");
        }

        List<String> usernameList = new ArrayList<>();
        for (String phone : phoneList) {
            usernameList.add(DESEncrypt.encodeUsername(phone));
        }

        userIdList = shpUserService.selectUserIdListByUsernameList(usernameList);
        return userIdList;
    }

    /**
     * 新增消息【店铺-店员】关联记录
     *
     * @param fkShpShopId       店铺ID，【消息类型】为【系统消息】时【店铺ID】可为空，其它条件下必填。
     * @param userIdList        用户ID
     * @param messageId         消息ID
     * @param enumOpMessageType 消息类型
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addOpMessageShopUserRef(Integer fkShpShopId, List<Integer> userIdList, Long messageId,
                                           String enumOpMessageType, Map<String, String> extraParam) throws Exception {
        String errorMsg = "";

        /**
         * step1.参数校验
         */
        //如果消息类型不是【系统消息】，参数【店铺ID】【fkShpShopId】不能为空
        if (null == fkShpShopId && !EnumOpMessageType.SYSTEM.getCode().equals(enumOpMessageType) && !EnumOpMessageType.OTHER.getCode().equals(enumOpMessageType)) {
            errorMsg = "【非系统消息】,参数【店铺ID】【fkShpShopId】不能为空";
            log.error(errorMsg);
            throw new Exception(errorMsg);
        }

        /**
         * step2.循环批量插入【消息店铺用户关系表】，每次插入100条记录
         */
        Integer batchSize = 500;
        Integer userListCount = userIdList.size();
        Integer batchCount = userListCount % batchSize == 0 ? userListCount / batchSize : userListCount / batchSize + 1;
        Map<String, Object> param = new HashMap<>();
        log.info("userListCount={},batchCount={} ", userListCount, batchCount);
        for (int loopCount = 0; loopCount < batchCount; loopCount++) {
            //截取列表开始下标
            Integer startIndex = loopCount * batchSize;
            //截取列表结束下标
            Integer endIndex = (loopCount + 1) * batchSize;

            //如果是最后一个批次，截取列表结束下标 = 用户ID列表数量-1
            endIndex = loopCount == batchCount - 1 ? userListCount : endIndex;
            List<Integer> subUserIdList = userIdList.subList(startIndex, endIndex);

            //当前批次插入对象数量
            Integer currentBatchSize = endIndex - startIndex;

            log.info("startIndex={},endIndex={},currentBatchSize={}", startIndex, endIndex, currentBatchSize);
            //开始循环插入店铺用户关联表
            if (null != subUserIdList) {
                List<OpMessageShopUserRef> shopUserRefList = new ArrayList<>();
                for (Integer userId : subUserIdList) {
                    OpMessageShopUserRef shopUserRef = new OpMessageShopUserRef();
                    shopUserRef.setMessageId(messageId);
                    shopUserRef.setFkShpShopId(fkShpShopId);
                    shopUserRef.setFkShpUserId(userId);
                    shopUserRef.setClickState(EnumOpMessageClickState.NOT_CLICK.getCode());
                    shopUserRef.setInsertTime(new Date());
                    shopUserRef.setUpdateTime(new Date());
                    shopUserRef.setDel(ConstantCommon.DEL_OFF);
                    shopUserRef.setExtraParam(JSONObject.toJSONString(extraParam));
                    shopUserRefList.add(shopUserRef);
                }
                Integer succesInsertCount = opMessageShopUserRefMapper.batchInsertMessageShopUserRef(shopUserRefList);
                log.info("店铺ID={}第{}批次，成功插入{}条消息", fkShpShopId, loopCount + 1, succesInsertCount);
            } else {
                log.info("没有获取到subUserIdList");
            }
        }

        /**
         * step3.返回结果
         */
        return true;
    }

    @Override
    public Boolean updateOpMessageReadStateByRefId(Long id) throws Exception {
        /**
         * step1.参数校验
         */
        String errorMsg = "";
        log.error(errorMsg);
        if (null == id) {
            errorMsg = "更新消息状态为已读业务参数为空，请检查【id】是否有值";
            throw new Exception(errorMsg);
        }

        OpMessageShopUserRef shopUserRef = opMessageShopUserRefMapper.selectByPrimaryKey(id);
        if (null == shopUserRef) {
            errorMsg = "数据库未获取到id=" + id + "的待更新状态消息记录";
            throw new Exception(errorMsg);
        }

        /**
         * step2. 更新消息状态为已读
         */
        shopUserRef.setClickState(EnumOpMessageClickState.HAVE_CLICK.getCode());
        shopUserRef.setUpdateTime(new Date());

        /**
         * step3.返回结果
         */
        return opMessageShopUserRefMapper.updateByPrimaryKeySelective(shopUserRef) == 1;
    }

    /**
     * 全部已读所有【消息】,不包含type在【友商消息】里的消息，但包含【店铺消息|系统消息|其他消息】
     * 1.如果【店铺ID】为空，说明用户当前没有店铺，只更新【系统消息】
     * 2.如果【店铺ID】不为空，则【系统消息】【除系统消息外的其它消息】都进行更新。
     *
     * @param shopId 店铺ID
     * @param userId 用户ID
     * @return
     */
    @Override
    public Boolean updateAllUnReadOpMessageByShopIdAndUserId(Integer shopId, Integer userId) throws Exception {
        /**
         * step1.参数校验
         */
        String errorMsg = "";
        log.error(errorMsg);
        if (null == userId) {
            errorMsg = "全部已读所有【消息】业务参数为空，请检查【userId】是否有值";
            throw new Exception(errorMsg);
        }

        /**
         * step2.根据【店铺ID shopId】值是否为空进行不同的更新操作
         */
        //更新系统消息
        Boolean result1 = opMessageShopUserRefMapper.updateAllUnReadSystemOpMessageByUserId(userId) >= 0;

        //如果【店铺ID】不为空，更新其它消息
        Boolean result2 = Boolean.FALSE;
        if (null != shopId) {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("shopId", shopId);
            result2 = opMessageShopUserRefMapper.updateAllUnReadOtherOpMessageByUserIdAndShopId(params) >= 0;
        }

        /**
         * step3.返回结果
         */
        return result1 & result2;
    }

    @Override
    public List<VoOpMessageDetail> loadOpMessageOtherListByType(Integer shopId, Integer userId, String type, String subType) throws Exception {
        /**
         * step1.参数校验
         */
        String errorMsg = "";
        if (null == userId || StringUtil.isBlank(type)) {
            errorMsg = "根据类型获取消息列表【用户ID】为空";
            log.error(errorMsg);
            throw new Exception(errorMsg);
        }

        /**
         * step3.构造查询参数
         */
        Map<String, Object> params = new HashMap<>();
        params.put("fkShpUserId", userId);
        params.put("type", type);
        params.put("subType", subType);

        //构造查询条件
        //如果不是系统消息，走原流程，直接根据type类型字段查询
        //如果是系统消息，要查询系统消息和其它消息（即为不是店铺消息和友商消息的消息）
        List<VoOpMessageDetail> list = new ArrayList<>();
        if (type.equals(EnumOpMessageType.SHOP.getCode())) {
            if (null == shopId) {
                errorMsg = "根据类型获取【店铺】消息列表【店铺ID】为空";
                log.error(errorMsg);
                throw new Exception(errorMsg);
            } else {
                params.put("fkShpShopId", shopId);
                list = opMessageMapper.selectOpMessageListByType(params);
            }
        } else if (type.equals(EnumOpMessageType.SYSTEM.getCode())) {
            params.put("fkShpShopId", null);
            list = opMessageMapper.selectOpMessageOtherListByShopIdAndUserId(params);
        } else {
            errorMsg = "无效的消息列表类型，只能为【shop|system】中的一种";
            log.error(errorMsg);
            throw new Exception(errorMsg);
        }

        /**
         * step4.返回结果
         */
        //更新图片URL
//        if(!CollectionUtils.isEmpty(list)){
//            for (VoOpMessageDetail voOpMessageDetail : list) {
//                String titleImgUrl = voOpMessageDetail.getTitleImgUrl();
//                voOpMessageDetail.setTitleImgUrl(titleImgUrl+"?timestamp="+System.currentTimeMillis());
//            }
//        }
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(opMessageDetail -> {
                if (!LocalUtils.isEmptyAndNull(opMessageDetail.getNativePage()) && "expiredNoticeList".equals(opMessageDetail.getNativePage())) {
                    String nativePage = opMessageDetail.getExtraParam();
                    HashMap hashMap = JSON.parseObject(nativePage, HashMap.class);
                    String result = hashMap.get("jump").toString();
                    HashMap jumpDate = JSON.parseObject(result, HashMap.class);
                    String jumpDateResult = jumpDate.get("jumpData").toString();
                    HashMap jumpDateResultMap = JSON.parseObject(jumpDateResult, HashMap.class);
                    String resultId = jumpDateResultMap.get("id").toString();
                    Map<String, String> stringMap = new HashMap<>();
                    stringMap.put("expiredNoticeId", resultId);
                    String param = JSON.toJSONString(stringMap);
                    opMessageDetail.setExtraParam(param);
                }


            });
        }
        return list;
    }

    @Override
    public List<VoOpMessage> listOpMessageForCms(ParamOpMessageQuery paramOpMessageQuery) {
        List<VoOpMessage> list = opMessageMapper.selectOpVoMessageForCms(paramOpMessageQuery);
        return list;
    }


    /**
     * 不包含type在【店铺消息|友商消息|系统消息】里的其它消息
     * 1.【店主】可以查看店铺内的所有【其它消息】
     * 2. 店铺的【其他用户】只能查看自己对应【用户ID】的【其它消息】
     *
     * @param fkShpShopId
     * @param fkUserId
     * @return
     * @throws Exception
     */
    @Override
    public List<VoOpMessageDetail> loadOpMessageOtherListByShopIdAndUserId(Integer fkShpShopId, Integer fkUserId) throws Exception {
        /**
         * step1.参数校验
         */
        String errorMsg = "";
        log.error(errorMsg);
        if (null == fkUserId) {
            errorMsg = "【用户ID】为空";
            throw new Exception(errorMsg);
        }

        /**
         * step3.构造查询参数
         */
        Map<String, Object> params = new HashMap<>();
        params.put("fkShpShopId", fkShpShopId);
        params.put("fkShpUserId", fkUserId);

        /**
         * step4.返回结果
         */
        return opMessageMapper.selectOpMessageOtherListByShopIdAndUserId(params);
    }

    @Override
    public VoOpMessageUnreadCount loadOpMessageUnReadCountByShopId(Integer fkShpShopId, Integer fkShpUserId) throws Exception {
        /**
         * step1.参数校验
         */
        String errorMsg = "";
        log.error(errorMsg);
        if (null == fkShpUserId) {
            errorMsg = "获取【消息】未读数量【用户ID】为空";
            throw new Exception(errorMsg);
        }

        /**
         * step2.查询未读数量
         */
        VoOpMessageUnreadCount vo = new VoOpMessageUnreadCount();
        Map<String, Object> param = new HashMap<>();
        param.put("fkShpShopId", fkShpShopId);
        param.put("fkShpUserId", fkShpUserId);

        /**
         * 2-1 获取【店铺消息】
         */
        param.put("type", EnumOpMessageType.SHOP.getCode());
        Integer shopMessageUnreadCount = 0;
        if (null != fkShpShopId) {
            shopMessageUnreadCount = opMessageShopUserRefMapper.selectUnreadCountByShopIdAndType(param);
        }
        vo.setShopMessageUnreadCount(shopMessageUnreadCount);

        /**
         * 2-2 获取【友商消息】
         * 友商消息未读数量取申请记录状态，不采用消息的【已读|未读】状态
         *【biz_leaguer_add.state】 10:已发请求(待确认); 12:已发请求(已过期);
         */
        Integer friendBusinessMessageUnreadCount = bizLeaguerAddMapper.selectUnHandleCountByShopId(fkShpShopId);
        vo.setFriendBusinessMessageUnreadCount(friendBusinessMessageUnreadCount);

        /**
         * 2-3 获取【系统消息]
         *【店铺ID】为NULL
         */
        param.put("type", EnumOpMessageType.SYSTEM.getCode());
        param.put("fkShpShopId", null);
        Integer systemMessageUnreadCount = opMessageShopUserRefMapper.selectUnreadCountByShopIdAndType(param);
        vo.setSystemMessageUnreadCount(systemMessageUnreadCount);

        /**
         * 2-4 获取【其它消息】未读数量
         */
        param.put("fkShpShopId", fkShpShopId);
        param.put("fkShpUserId", fkShpUserId);
        Integer otherUnReadCount = 0;
//      Integer otherUnReadCount = opMessageMapper.selectOpMessageOtherListUnReadCountByShopIdAndUserId(param);
//      vo.setOtherMessageUnreadCount(otherUnReadCount);

        Integer totalUnReadCount = shopMessageUnreadCount + friendBusinessMessageUnreadCount + systemMessageUnreadCount + otherUnReadCount;
        vo.setTotalUnReadCount(totalUnReadCount);

        /**
         * step3.返回结果
         */
        return vo;
    }

    @Override
    public Boolean delOpMessageRefById(Long id) throws Exception {
        /**
         * step1.参数校验
         */
        String errorMsg = "";
        log.error(errorMsg);
        if (null == id) {
            errorMsg = "删除【消息】ID为空";
            throw new Exception(errorMsg);
        }

        /**
         * step2. 更新消息状态为已读
         */
        OpMessageShopUserRef shopUserRef = opMessageShopUserRefMapper.selectByPrimaryKey(id);
        if (null == shopUserRef) {
            errorMsg = "数据库未获取到id=" + id + "的待删除消息记录";
            throw new Exception(errorMsg);
        }

        shopUserRef.setDel(ConstantCommon.DEL_ON);
        shopUserRef.setUpdateTime(new Date());

        /**
         * step3.返回结果
         */
        return opMessageShopUserRefMapper.updateByPrimaryKeySelective(shopUserRef) == 1;
    }


    /***********************************  删除/更新操作  *************************************/
    @Override
    public int delOpMessage(Long msgId, Integer uid) {
        OpMessage opMessage = opMessageMapper.selectByPrimaryKey(msgId);
        opMessage.setDel(ConstantCommon.DEL_ON);
        opMessage.setUpdateAdmin(uid);
        opMessage.setUpdateTime(new Date());
        return opMessageMapper.updateByPrimaryKeySelective(opMessage);
    }

    @Override
    public int delOpMessageByDateRange(Integer shopId, String startTime, String endTime) {
        if (null == shopId) {
            throw new MyException("店铺ID为空，不允许删除");
        }
        //删除指定时间范围内的店铺消息
        opMessageShopUserRefMapper.delOpMessageByDateRange(shopId, startTime, endTime);
        return 0;
    }

    /**
     * 更新用户消息
     *
     * @param paramOpMessageSave
     * @param uid
     * @return
     */
    @Override
    public int updateOpMessage(ParamOpMessageSave paramOpMessageSave, Integer uid) throws Exception {
        //复制属性
        OpMessage opMessage = new OpMessage();
        BeanUtils.copyProperties(paramOpMessageSave, opMessage);
        checkOpMessageParam(paramOpMessageSave, null);

        //如果状态为已发送，则不允许修改
        Long msgId = paramOpMessageSave.getId();
        OpMessage opMessageDb = opMessageMapper.selectByPrimaryKey(msgId);
        if (null == opMessageDb) {
            throw new Exception("对应ID=" + msgId + "的消息不存在");
        }
        if (EnumOpMessagePushState.HAVE_PUSH.equals(opMessageDb.getPushState())) {
            throw new Exception("已发送的消息不可编辑");
        }

        //设置修改者ID
        uid = null == uid ? -1 : uid;
        opMessage.setUpdateAdmin(uid);

        //设置修改时间
        opMessage.setUpdateTime(new Date());

        //更新数据库
        return opMessageMapper.updateByPrimaryKeySelective(opMessage);
    }

    @Override
    public VoOpMessage loadOpMessageByIdForCms(Long msgId) {
        return opMessageMapper.selectOpMessageByIdForCms(msgId);
    }

    /**
     * 设置【跳转APP原生页面】消息默认参数
     *
     * @param paramOpMessageSave
     */
    @Override
    public void setCommonMsgParamNative(ParamOpMessageSave paramOpMessageSave, EnumOpMessageJumpType enumOpMessageJumpType) {
        paramOpMessageSave.setSendType(EnumOpMessageSendType.RIGHT_NOW.getCode());
        paramOpMessageSave.setPreSendTime(null);
        paramOpMessageSave.setJumpType(enumOpMessageJumpType.getCode());
        paramOpMessageSave.setClickH5Url("");
        paramOpMessageSave.setIsPushAllUser(EnumOpMessageIsPushAllUser.NO.getCode());
        paramOpMessageSave.setPushState(null);
        paramOpMessageSave.setCreateSource(EnumOpMessageCreateSource.CODE.getCode());
        paramOpMessageSave.setPushUserExcelUrl("");
    }


    /***********************************  前台参数校验  *************************************/
    private void checkOpMessageParam(ParamOpMessageSave paramOpMessageSave, List<Integer> userIdList) throws Exception {
        //消息类型
        String type = paramOpMessageSave.getType();
        //推送平台
        String pushPlatform = paramOpMessageSave.getPushPlatform();
        //消息标题
        String title = paramOpMessageSave.getTitle();
        //消息内容
        String content = paramOpMessageSave.getContent();
        //发送方式
        String sendType = paramOpMessageSave.getSendType();
        //跳转类型
        String jumpType = paramOpMessageSave.getJumpType();
        //是否推送全部用户用户
        String isPushAllUser = paramOpMessageSave.getIsPushAllUser();

        //部分用户推送列表Excel地址
        String pushUserExcelUrl = paramOpMessageSave.getPushUserExcelUrl();


        /**
         * step1.参数校验
         * 1-1 基本参数校验
         */
        String errorMsg;
        if (StringUtil.isAnyBlank(title, content, type, pushPlatform, sendType, jumpType, isPushAllUser)) {
            errorMsg = "前台必要业务参数为空，请检查【pushPlatform|type|title|content|sendType|jumpType|isPushAllUser】是否有值";
            log.error(errorMsg);
            throw new Exception(errorMsg);
        }

        /**
         * 1-2 如果子类型为空，赋予默认值
         */
        if (StringUtil.isBlank(paramOpMessageSave.getSubType())) {
            paramOpMessageSave.setSubType("default");
        }

        /**
         * 1-3 如果【发送方式】为【定时发送】，【预定发送时间】不能为空。
         */
        if (EnumOpMessageSendType.TIMER.getCode().equals(paramOpMessageSave.getSendType())) {
            if (null == paramOpMessageSave.getPreSendTime()) {
                errorMsg = "【定时发送】方式【预发送时间】不能为空";
                log.error(errorMsg);
                throw new Exception(errorMsg);
            }
        }

        /**
         * 1-4
         *    （1）如果跳转类型是【h5页面】，【H5跳转地址】不能为空
         *    （2）如果跳转类型是【原生页面】，【页面类型】不能为空
         */
        if (paramOpMessageSave.getJumpType().equals(EnumOpMessageJumpType.H5.getCode())) {
            if (StringUtil.isBlank(paramOpMessageSave.getClickH5Url())) {
                errorMsg = "跳转类型是【h5页面】，【H5跳转地址】不能为空";
                log.error(errorMsg);
                throw new Exception(errorMsg);
            }
        } else if (paramOpMessageSave.getJumpType().equals(EnumOpMessageJumpType.NATIVE.getCode())) {
            if (StringUtil.isBlank(paramOpMessageSave.getNativePage())) {
                errorMsg = "跳转类型是【原生页面】，【页面类型】不能为空";
                log.error(errorMsg);
                throw new Exception(errorMsg);
            }
        }

        /**
         * 1-5
         *    如果是推送部分用户，并且userIdList为空【部分用户推送列表Excel地址】不能为空
         */
        if (CollectionUtils.isEmpty(userIdList) &&
                paramOpMessageSave.getIsPushAllUser().equals(EnumOpMessageIsPushAllUser.NO.getCode())) {
            if (StringUtil.isBlank(pushUserExcelUrl)) {
                errorMsg = "推送部分用户，【部分用户推送列表Excel地址】不能为空";
                log.error(errorMsg);
                throw new Exception(errorMsg);
            }
        }
    }


}
