package com.luxuryadmin.service.op.impl;

import cn.hutool.core.date.DateUtil;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosAlert;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.alibaba.fastjson.JSON;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.biz.BizUnionVerify;
import com.luxuryadmin.entity.ord.OrdOrder;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.entity.shp.ShpOrderDailyCount;
import com.luxuryadmin.enums.op.*;
import com.luxuryadmin.mapper.pro.ProProductMapper;
import com.luxuryadmin.mapper.shp.ShpShopMapper;
import com.luxuryadmin.param.op.ParamOpMessageSave;
import com.luxuryadmin.param.pro.ParamProductUpload;
import com.luxuryadmin.service.op.OpMessageService;
import com.luxuryadmin.service.op.OpPushService;
import com.luxuryadmin.service.shp.ShpPermUserRefService;
import com.luxuryadmin.service.shp.ShpUserShopRefService;
import com.luxuryadmin.vo.pro.VoExpireProd;
import com.luxuryadmin.vo.pro.VoProductSimple;
import com.luxuryadmin.vo.shp.VoShpOrderDailyCountForMonth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @author: walkingPotato
 * @date: 2020-07-22 14:46
 * @email: dingxin_hz@163.com
 * @version: V1.0
 */
@Service
@Slf4j
public class OpPushServiceImpl implements OpPushService {

    @Autowired
    private JPushClient jPushClient;

    @Autowired
    private OpMessageService opMessageService;

    @Autowired
    private ShpPermUserRefService shpPermUserRefService;

    @Resource
    private ProProductMapper proProductMapper;

    @Autowired
    private ShpUserShopRefService shpUserShopRefService;

    @Resource
    private ShpShopMapper shpShopMapper;

    /**
     * 当前环境
     */
    @Value("${spring.profiles.active}")
    private String env;

    @Override
    public Boolean pushJiGuangMessage(Platform platform, List<String> aliasList, String title, String msgContent,
                                      Map<String, String> extraParam) {
        PushPayload payload = buildPushPayload(platform, aliasList, title, msgContent, extraParam);

        try {
            PushResult result = jPushClient.sendPush(payload);
            log.info("【极光推送】结果: " + result);

        } catch (APIConnectionException e) {
            log.error("【极光推送】网络连接异常，请稍后再试：", e);
            return Boolean.FALSE;
        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            log.error("【极光推送】请求失败：", e);
            log.info("【极光推送】请求失败 HTTP Status: " + e.getStatus());
            log.info("【极光推送】请求失败 Error Code: " + e.getErrorCode());
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private PushPayload buildPushPayload(Platform platform, List<String> aliasList, String title, String msgContent,
                                         Map<String, String> extraParam) {
        PushPayload.Builder pushPayLoadBuilder = PushPayload.newBuilder();
        //设置应用平台 Platform.all(),Platform.android(),Platform.ios(),Platform.android_ios()
        pushPayLoadBuilder.setPlatform(platform);

        //设置推送的用户列表
        if (CollectionUtils.isEmpty(aliasList)) {
            pushPayLoadBuilder.setAudience(Audience.all());
        } else {
            Audience.Builder audienceBuilder = Audience.newBuilder();
            audienceBuilder.addAudienceTarget(AudienceTarget.alias(aliasList));
            pushPayLoadBuilder.setAudience(audienceBuilder.build());
        }

        //设置推送环境
        //如果是生产环境，则设置ApnsProduction=true,表示为生产环境
        log.info("当前运行环境为：" + env);
        if ("pro".equals(env)) {
            pushPayLoadBuilder.setOptions(Options.newBuilder()
                    .setApnsProduction(true)
                    .build());
        }

        //设置通知内容
        IosAlert iosAlert = IosAlert.newBuilder().setTitleAndBody(title, null, msgContent).build();
        Notification notification = Notification.newBuilder()
                .addPlatformNotification(IosNotification.newBuilder()
                        .setAlert(iosAlert)
                        .setBadge(1)
                        .addExtras(extraParam)
                        .build())
                .addPlatformNotification(AndroidNotification.newBuilder()
                        .setAlert(msgContent)
                        .setTitle(title)
                        //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                        .addExtras(extraParam)
                        .build()
                )
                .build();

        pushPayLoadBuilder.setNotification(notification);

        //设置自定义消息内容
        pushPayLoadBuilder.setMessage(Message.content(msgContent));

        //设置消息参数

        return pushPayLoadBuilder.build();
    }


    /******************************* 消息推送模板 *********************************/
    /**
     * 推送修改商品价格消息
     *
     * @param shopId
     */
    @Override
    public void pushUpdateProdPriceMsg(int shopId, ParamProductUpload paramProductUpload, ProProduct oldPro) {
        try {
            DecimalFormat df = new DecimalFormat("#0");
            //是否修改成本价
            String oldInitPrice = df.format(oldPro.getInitPrice());
            Boolean isUpdateInitPrice = !oldInitPrice.equals(paramProductUpload.getInitPrice());
            //是否修改友商价
            String oldTradePrice = df.format(oldPro.getTradePrice());
            Boolean isUpdateTradePrice = !oldTradePrice.equals(paramProductUpload.getTradePrice());
            //是否修改代理价
            String oldAgencyPrice = df.format(oldPro.getAgencyPrice());
            Boolean isUpdateAgencyPrice = !oldAgencyPrice.equals(paramProductUpload.getAgencyPrice());
            //是否修改零售价
            String oldSalePrice = df.format(oldPro.getSalePrice());
            Boolean isUpdateSalePrice = !oldSalePrice.equals(paramProductUpload.getSalePrice());

            //是否更新了价格
            Boolean isUpdatePrice = isUpdateInitPrice || isUpdateTradePrice || isUpdateAgencyPrice || isUpdateSalePrice;

            //如果未更新价格，则不发送消息，直接返回
            if (!isUpdatePrice) {
                return;
            }

            String content;
            content = "【" + oldPro.getName() + "】价格发生变动，快来看看吧~";

            ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
            paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
            paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
            paramOpMessageSave.setTitle("商品价格变动通知");
            paramOpMessageSave.setSubType(EnumOpMessageSubType.UPDATE_PROD_PRICE.getCode());
            paramOpMessageSave.setContent(content);
            paramOpMessageSave.setNativePage("prodDetail");
            //拥有同行价的权限才通知;2021-12-10 01:49:49
            List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                    shopId, ConstantPermission.CHK_PRICE_TRADE);
            opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NATIVE);
            //设置参数
            Map<String, String> extraParam = new HashMap<>();
            extraParam.put("bizId", "" + paramProductUpload.getBizId());
            opMessageService.addOpMessage(paramOpMessageSave, null, shopId, pushUserIdList, extraParam);
        } catch (Exception e) {
            log.error("推送开单消息异常:" + e);
        }
    }

    @Override
    public void pushUpdateInitPriceMsg(int shopId, ParamProductUpload paramProductUpload, ProProduct oldPro) {
        try {
            DecimalFormat df = new DecimalFormat("#0");
            //是否修改成本价
            String oldInitPrice = df.format(oldPro.getInitPrice());
            Boolean isUpdateInitPrice = !oldInitPrice.equals(paramProductUpload.getInitPrice());
            //是否更新了价格
            Boolean isUpdatePrice = isUpdateInitPrice;

            //如果未更新价格，则不发送消息，直接返回
            if (!isUpdatePrice) {
                return;
            }
            BigDecimal oldPrice = new BigDecimal(oldInitPrice).divide(new BigDecimal(100));
            BigDecimal nowPrice = new BigDecimal(paramProductUpload.getInitPrice()).divide(new BigDecimal(100));
            String content;
            content = "变动前："+oldPrice+" 变动后："+nowPrice+"，【" + oldPro.getName() + "】";

            ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
            paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
            paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
            paramOpMessageSave.setTitle("商品成本变动通知");
            paramOpMessageSave.setSubType(EnumOpMessageSubType.UPDATE_PROD_INIT_PRICE.getCode());
            paramOpMessageSave.setContent(content);
            paramOpMessageSave.setNativePage("prodDetail");
            //拥有成本价的权限才通知;2021-12-10 01:49:49
            List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                    shopId, ConstantPermission.CHK_PRICE_INIT);
            opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NATIVE);
            //设置参数
            Map<String, String> extraParam = new HashMap<>();
            extraParam.put("bizId", "" + paramProductUpload.getBizId());
            opMessageService.addOpMessage(paramOpMessageSave, null, shopId, pushUserIdList, extraParam);
        } catch (Exception e) {
            log.error("推送开单消息异常:" + e);
        }
    }

    /**
     * 推送商品上架消息
     *
     * @param shopId
     * @param proBizIdList
     */
    @Override
    public void pushReleaseProductMsg(int shopId, List<String> proBizIdList) {
        try {
            if (!LocalUtils.isEmptyAndNull(proBizIdList)) {
                List<VoProductSimple> prodList = proProductMapper.listProdNameByBizIdList(proBizIdList);
                List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                        shopId, ConstantPermission.CHK_ON_SALE_PRODUCT);

                for (VoProductSimple voProductSimple : prodList) {
                    String prodName = voProductSimple.getProdName();
                    String bizId = voProductSimple.getBizId();
                    String content;
                    content = "【" + prodName + "】已经上架，快来看看吧~";

                    ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
                    paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
                    paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
                    paramOpMessageSave.setTitle("商品上架通知");
                    paramOpMessageSave.setSubType(EnumOpMessageSubType.RELEASE_PROD.getCode());
                    paramOpMessageSave.setContent(content);
                    paramOpMessageSave.setNativePage("prodDetail");


                    opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NATIVE);

                    //设置参数
                    Map<String, String> extraParam = new HashMap<>();
                    extraParam.put("bizId", "" + bizId);
                    opMessageService.addOpMessage(paramOpMessageSave, null, shopId, pushUserIdList, extraParam);
                }
            } else {
                //全店上架
                List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                        shopId, ConstantPermission.CHK_ON_SALE_PRODUCT);
                String content;
                content = "店铺商品已【全部上架】，快来看看吧~";
                ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
                paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
                paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
                paramOpMessageSave.setTitle("商品上架通知");
                paramOpMessageSave.setSubType(EnumOpMessageSubType.RELEASE_PROD.getCode());
                paramOpMessageSave.setContent(content);
                paramOpMessageSave.setNativePage("prodDetail");

                opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NATIVE);

                //设置参数
                Map<String, String> extraParam = new HashMap<>();
                extraParam.put("bizId", "");
                opMessageService.addOpMessage(paramOpMessageSave, null, shopId, pushUserIdList, extraParam);

            }
        } catch (Exception e) {
            log.error("推送开单消息异常:" + e);
        }


    }

    /**
     * 推送商品上架消息
     *
     * @param shopId
     */
    @Override
    public void pushReleaseProductMsg(int shopId) {
        pushReleaseProductMsg(shopId, null);
    }

    /**
     * 推送商品删除消息
     *
     * @param shopId
     * @param proBizIdList
     */
    @Override
    public void pushDeleteProductMsg(int shopId, List<String> proBizIdList) {

        /**
         * 发送商品删除消息
         */
        try {
            List<VoProductSimple> prodList = proProductMapper.listProdNameByBizIdList(proBizIdList);
            for (VoProductSimple voProductSimple : prodList) {
                String prodName = voProductSimple.getProdName();
                String bizId = voProductSimple.getBizId();
                String content;
                content = "【" + prodName + "】已删除~";

                ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
                paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
                paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
                paramOpMessageSave.setTitle("商品删除通知");
                paramOpMessageSave.setSubType(EnumOpMessageSubType.DELETE_PROD.getCode());
                paramOpMessageSave.setContent(content);
                paramOpMessageSave.setNativePage("prodDetail");
                //拥有回收站
                List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                        shopId, ConstantPermission.CHK_DELETE_HISTORY);
                opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NATIVE);
                //设置参数
                Map<String, String> extraParam = new HashMap<>();
                extraParam.put("bizId", "" + bizId);
                opMessageService.addOpMessage(paramOpMessageSave, null, shopId, pushUserIdList, extraParam);
            }
        } catch (Exception e) {
            log.error("推送开单消息异常:" + e);
        }
    }

    /**
     * 推送商品删除消息
     *
     * @param shopId
     * @param proBizIdList
     */
    @Override
    public void pushRedeemProductMsg(int shopId, List<String> proBizIdList) {

        /**
         * 发送质押商品赎回消息
         */
        try {
            List<VoProductSimple> prodList = proProductMapper.listProdNameByBizIdList(proBizIdList);
            for (VoProductSimple voProductSimple : prodList) {
                String prodName = voProductSimple.getProdName();
                String bizId = voProductSimple.getBizId();
                String content;
                content = "【" + prodName + "】已赎回，快来看看吧~";

                ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
                paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
                paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
                paramOpMessageSave.setTitle("质押商品赎回通知");
                paramOpMessageSave.setSubType(EnumOpMessageSubType.REDEEM_PROD.getCode());
                paramOpMessageSave.setContent(content);
                paramOpMessageSave.setNativePage("prodDetail");

                List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                        shopId, ConstantPermission.CHK_PAWN_PRODUCT);
                opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NATIVE);
                //设置参数
                Map<String, String> extraParam = new HashMap<>();
                extraParam.put("bizId", "" + bizId);
                opMessageService.addOpMessage(paramOpMessageSave, null, shopId, pushUserIdList, extraParam);
            }
        } catch (Exception e) {
            log.error("推送质押商品赎回消息异常:" + e);
        }
    }

    /**
     * 推送质押商品过期消息
     *
     * @param expireProdList
     */
    @Override
    public void pushExpireProductMsg(List<VoExpireProd> expireProdList) {

        /**
         * 发送质押商品赎回消息
         */
        try {
            for (VoExpireProd voExpireProd : expireProdList) {
                String prodName = voExpireProd.getProName();
                String bizId = voExpireProd.getBizId();
                Integer shopId = voExpireProd.getShopId();
                String content;
                content = "【" + prodName + "】已经到期，请尽快处理";

                ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
                paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
                paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
                paramOpMessageSave.setTitle("质押商品到期通知");
                paramOpMessageSave.setSubType(EnumOpMessageSubType.EXPIRE_PROD.getCode());
                paramOpMessageSave.setContent(content);
                paramOpMessageSave.setNativePage("prodDetail");

                List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                        shopId, ConstantPermission.CHK_PAWN_PRODUCT);
                opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NATIVE);
                //设置参数
                Map<String, String> extraParam = new HashMap<>();
                extraParam.put("bizId", "" + bizId);
                opMessageService.addOpMessage(paramOpMessageSave, null, shopId, pushUserIdList, extraParam);
            }
        } catch (Exception e) {
            log.error("推送质押商品赎回消息异常:" + e);
        }
    }


    /**
     * 发送商品入库消息
     */
    @Override
    public void pushUploadProductMsg(int shopId, List<String> proBizIdList) {
        try {
            List<VoProductSimple> prodList = proProductMapper.listProdNameByBizIdList(proBizIdList);
            List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                    shopId, ConstantPermission.CHK_OWN_PRODUCT);
            for (VoProductSimple voProductSimple : prodList) {
                String prodName = voProductSimple.getProdName();
                String bizId = voProductSimple.getBizId();
                String content;
                content = "【" + prodName + "】新品入库，快来看看吧~";

                ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
                paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
                paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
                paramOpMessageSave.setTitle("商品入库通知");
                paramOpMessageSave.setSubType(EnumOpMessageSubType.UPLOAD_PROD.getCode());
                paramOpMessageSave.setContent(content);
                paramOpMessageSave.setNativePage("prodDetail");


                opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NATIVE);

                //设置参数
                Map<String, String> extraParam = new HashMap<>();
                extraParam.put("bizId", "" + bizId);
                opMessageService.addOpMessage(paramOpMessageSave, null, shopId, pushUserIdList, extraParam);
            }
        } catch (Exception e) {
            log.error("推送开单消息异常:" + e);
        }
    }

    @Override
    public void pushLockProductMsg(int shopId, String prodName, String bizId, String lockId) {
        try {
            List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                    shopId, ConstantPermission.CHK_LOCK_PRODUCT);
            String content = "【" + prodName + "】商品正在锁单🔒快来看看吧~";
            ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
            paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
            paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
            paramOpMessageSave.setTitle("商品锁单通知");
            paramOpMessageSave.setSubType(EnumOpMessageSubType.LOCK_PROD.getCode());
            paramOpMessageSave.setContent(content);
            paramOpMessageSave.setNativePage("lockDetail");
            opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NATIVE);

            //设置参数
            Map<String, String> extraParam = new HashMap<>();
            extraParam.put("bizId", "" + bizId);
            extraParam.put("lockId", "" + lockId);
            opMessageService.addOpMessage(paramOpMessageSave, null, shopId, pushUserIdList, extraParam);
        } catch (Exception e) {
            log.error("推送开单消息异常:" + e);
        }
    }

    @Override
    public void recycleProductMsg(int shopId, String prodName, String bizId) {
        try {
            List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                    shopId, ConstantPermission.CHK_LOCK_PRODUCT);
            String content = "【" + prodName + "】商品正在取回，快来看看吧~";
            ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
            paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
            paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
            paramOpMessageSave.setTitle("商品取回通知");
            paramOpMessageSave.setSubType(EnumOpMessageSubType.RECYCLE_PROD.getCode());
            paramOpMessageSave.setContent(content);
            paramOpMessageSave.setNativePage("prodDetail");
            opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NATIVE);

            //设置参数
            Map<String, String> extraParam = new HashMap<>();
            extraParam.put("bizId", "" + bizId);
            opMessageService.addOpMessage(paramOpMessageSave, null, shopId, pushUserIdList, extraParam);
        } catch (Exception e) {
            log.error("推送开单消息异常:" + e);
        }
    }

    @Override
    public void pushMessageForExpiredProduct(Integer expiredNoticeId, Integer shopId, Integer expiredDay) {
        ParamOpMessageSave opMessageSave = new ParamOpMessageSave();
        opMessageSave.setType(EnumOpMessageType.SHOP.getCode());
        opMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
        opMessageSave.setTitle("到期商品提醒");
        opMessageSave.setSubType(EnumOpMessageSubType.EXPIRED_PRODUCT.getCode());
        opMessageSave.setContent("您添加的" + expiredDay + "日到期商品提醒已达到提醒日期，请及时查看~");
        opMessageSave.setNativePage("expiredNoticeList");
        List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                shopId, ConstantPermission.MSG_PRO_EXPIRE);
        opMessageService.setCommonMsgParamNative(opMessageSave, EnumOpMessageJumpType.NATIVE);
        //设置参数jump:{jumptype:1,jumpData:{id:111}}
        Map<String, String> extraParam = new HashMap<>();
        //jumpData级
        Map<String, Object> extraParamGrandson = new HashMap<>();
        extraParamGrandson.put("id", "" + expiredNoticeId);
        //jumptype级
        Map<String, Object> extraParamSon = new HashMap<>();
        extraParamSon.put("jumptype", "1");
        extraParamSon.put("jumpData", extraParamGrandson);
        //map转string
//        JSONArray jArray = new JSONArray();
//        jArray.add(extraParamSon);
//        String param = jArray.toString();
        String param = JSON.toJSONString(extraParamSon);
        extraParam.put("jump", param);
        try {
            opMessageService.addOpMessage(opMessageSave, null, shopId, pushUserIdList, extraParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessageUnionVerify(BizUnionVerify bizUnionVerify, String remark) {
        ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
        paramOpMessageSave.setType(EnumOpMessageType.SYSTEM.getCode());
        paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
        paramOpMessageSave.setTitle("审核结果通知");
        paramOpMessageSave.setSubType(EnumOpMessageSubType.UNION_VERIFY.getCode());

        paramOpMessageSave.setContent(remark);

        opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NO_JUMP);
        paramOpMessageSave.setIsPushAllUser(EnumOpMessageIsPushAllUser.YES.getCode());

        List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                bizUnionVerify.getFkShpShopId(), ConstantPermission.SHOW_UNION_UNIONSHOP);
        try {
            Long msgId = opMessageService.addOpMessage(paramOpMessageSave, null, bizUnionVerify.getFkShpShopId(), pushUserIdList, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /************************************  发送订单相关消息 *********************************/

    /**
     * 推送开单消息
     *
     * @param shopId
     * @param order
     */
    @Override
    public void pushOrderMsg(int shopId, OrdOrder order, ProProduct pro) {
        try {

            String content;
            Integer userId = order.getFkShpUserId();

            String userName = shpUserShopRefService.getNameFromShop(shopId, userId);
            Integer saleNum = order.getTotalNum();
            String prodName = pro.getName();
            content = userName + "售出" + saleNum + "件【" + prodName + "】";

            ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
            paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
            paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
            paramOpMessageSave.setTitle("商品售出通知");
            paramOpMessageSave.setSubType(EnumOpMessageSubType.SALE_PROD.getCode());
            paramOpMessageSave.setContent(content);
            paramOpMessageSave.setNativePage("orderDetail");

            opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NATIVE);

            //获取推送消息用户ID
            List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                    shopId, ConstantPermission.CHK_ALL_ORDER);
            //设置参数
            Map<String, String> extraParam = new HashMap<>();
            extraParam.put("orderId", "" + order.getNumber());
            opMessageService.addOpMessage(paramOpMessageSave, null, shopId, pushUserIdList, extraParam);
        } catch (Exception e) {
            log.error("推送开单消息异常:" + e);
        }
    }

    /**
     * 推送取消订单消息
     *
     * @param shopId
     * @param order
     */
    @Override
    public void pushCancelOrderMsg(int shopId, OrdOrder order, ProProduct pro) {
        try {

            String content;
            Integer userId = order.getFkShpUserId();

            String userName = shpUserShopRefService.getNameFromShop(shopId, userId);
            //TODO 获取商品名称
            String prodName = "商品";
            if (null != pro) {
                prodName = pro.getName();
            }

            content = userName + "退货了【" + prodName + "】";

            ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
            paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
            paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
            paramOpMessageSave.setTitle("订单退货通知");
            paramOpMessageSave.setSubType(EnumOpMessageSubType.CANCEL_ORDER.getCode());
            paramOpMessageSave.setContent(content);
            paramOpMessageSave.setNativePage("orderDetail");

            opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NATIVE);

            //获取推送消息用户ID
            List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                    shopId, ConstantPermission.CHK_ALL_ORDER);

            //设置参数
            Map<String, String> extraParam = new HashMap<>();
            extraParam.put("orderId", "" + order.getNumber());
            opMessageService.addOpMessage(paramOpMessageSave, null, shopId, pushUserIdList, extraParam);
        } catch (Exception e) {
            log.error("推送开单消息异常:" + e);
        }
    }

    /**
     * 推送更新订单消息
     *
     * @param shopId
     * @param order
     */
    @Override
    public void pushUpdateOrderMsg(int shopId, OrdOrder order, ProProduct pro, Integer userId) {
        try {

            String content;

            String userName = shpUserShopRefService.getNameFromShop(shopId, userId);
            //TODO 获取商品名称
            String prodName = "商品";
            if (null != pro) {
                prodName = pro.getName();
            }
            content = userName + "修改了【" + prodName + "】的订单信息";

            ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
            paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
            paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
            paramOpMessageSave.setTitle("订单修改通知");
            paramOpMessageSave.setSubType(EnumOpMessageSubType.UPDATE_ORDER.getCode());
            paramOpMessageSave.setContent(content);
            paramOpMessageSave.setNativePage("orderDetail");

            opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NATIVE);

            //获取推送消息用户ID
            List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                    shopId, ConstantPermission.CHK_ALL_ORDER);

            //设置参数
            Map<String, String> extraParam = new HashMap<>();
            extraParam.put("orderId", "" + order.getNumber());
            opMessageService.addOpMessage(paramOpMessageSave, null, shopId, pushUserIdList, extraParam);
        } catch (Exception e) {
            log.error("推送开单消息异常:" + e);
        }
    }

    @Override
    public void pushShpOrderDailyCount(Integer shopId, ShpOrderDailyCount shpOrderDailyCount) {
        try {
            String shopName = shpShopMapper.getVoUserShopBaseByShopId(shopId).getShopName();
            String countDateStr = DateUtil.format(shpOrderDailyCount.getCountDate(), "MM月dd日");
            String title = countDateStr + "日报";
            String subTitle = "点击查看日报详情>";


            StringBuilder content = new StringBuilder();
            content.append("【" + shopName + "】\r\n");
            content.append("一、商品\r\n");
            content.append("今日销售总量：" + shpOrderDailyCount.getOrdNum() + "件\r\n");
            content.append("今日销售总额：" + shpOrderDailyCount.getOrdAmount() + "元\r\n");
            content.append("今日净利润：" + shpOrderDailyCount.getOrdNetProfit() + "元\r\n");
            content.append("今日退货总量：" + shpOrderDailyCount.getOrdCancelNum() + "件\r\n");
            content.append("今日退货总额：" + shpOrderDailyCount.getOrdCancelAmount() + "元\r\n");

            content.append("今日入库总量：");
            content.append("自有/" + shpOrderDailyCount.getInRepoNumSelf() + "件;");
            content.append("寄卖/" + shpOrderDailyCount.getInRepoNumConsignment() + "件;");
            content.append("质押/" + shpOrderDailyCount.getInRepoNumPledge() + "件;");
            content.append("其它/" + shpOrderDailyCount.getInRepoNumOther() + "件\r\n");

            content.append("今日入库总成本：");
            content.append("自有/" + shpOrderDailyCount.getInRepoAmountSelf() + "元;");
            content.append("寄卖/" + shpOrderDailyCount.getInRepoAmountConsignment() + "元;");
            content.append("质押/" + shpOrderDailyCount.getInRepoAmountPledge() + "元;");
            content.append("其它/" + shpOrderDailyCount.getInRepoAmountOther() + "元\r\n");

            content.append("今日上架总量：" + shpOrderDailyCount.getOnshelvesProdNum() + "件\r\n");

            content.append("二、维修保养\r\n");
            content.append("今日维修保养结算总量：" + shpOrderDailyCount.getServiceSettleNum() + "件\r\n");
            content.append("今日维修保养结算销售总额：" + shpOrderDailyCount.getServiceSettleSellAmount() + "元\r\n");
            content.append("今日维修保养结算总成本：" + shpOrderDailyCount.getServiceSettleCostAmount() + "元\r\n");
            content.append("今日维修保养结算净利润：" + shpOrderDailyCount.getServiceSettleProfitAmount() + "元\r\n");

            ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
            paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
            paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
            paramOpMessageSave.setTitle(title);
            paramOpMessageSave.setSubType(EnumOpMessageSubType.DAILY_REPORT.getCode());
            paramOpMessageSave.setContent(content.toString());
            paramOpMessageSave.setNativePage("dailyCount");

            opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NO_JUMP);

            //获取推送消息用户ID
            List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                    shopId, ConstantPermission.SALE_CHECK_ALL);

            //设置参数
//        Map<String, String> extraParam = new HashMap<>();
//        extraParam.put("orderId", "" + order.getNumber());
            opMessageService.addOpMessage(paramOpMessageSave, null, shopId, pushUserIdList, null);
        } catch (Exception e) {
            log.error("推送开单消息异常:" + e);
        }
    }

    @Override
    public void pushShpOrderDailyCountForMonth(Integer shopId, VoShpOrderDailyCountForMonth orderDailyCountForMonth) {
        try {
            String shopName = shpShopMapper.getVoUserShopBaseByShopId(shopId).getShopName();
            String countDateStr = DateUtil.format(orderDailyCountForMonth.getCountDate(), "MM月dd日");
            String title = countDateStr + "月报";
            String subTitle = "点击查看月报详情>";

            StringBuilder content = new StringBuilder();
            content.append("【" + shopName + "】\r\n");
            content.append("一、商品\r\n");
            content.append("上月销售总量：" + orderDailyCountForMonth.getOrdNum() + "件\r\n");
            content.append("上月销售总额：" + orderDailyCountForMonth.getOrdAmount() + "元\r\n");
            content.append("上月净利润：" + orderDailyCountForMonth.getOrdNetProfit() + "元\r\n");
            content.append("上月退货总量：" + orderDailyCountForMonth.getOrdCancelNum() + "件\r\n");
            content.append("上月退货总额：" + orderDailyCountForMonth.getOrdCancelAmount() + "元\r\n");

            content.append("上月入库总量：");
            content.append("自有/" + orderDailyCountForMonth.getInRepoNumSelf() + "件;");
            content.append("寄卖/" + orderDailyCountForMonth.getInRepoNumConsignment() + "件;");
            content.append("质押/" + orderDailyCountForMonth.getInRepoNumPledge() + "件;");
            content.append("其它/" + orderDailyCountForMonth.getInRepoNumOther() + "件\r\n");

            content.append("上月入库总成本：");
            content.append("自有/" + orderDailyCountForMonth.getInRepoAmountSelf() + "元;");
            content.append("寄卖/" + orderDailyCountForMonth.getInRepoAmountConsignment() + "元;");
            content.append("质押/" + orderDailyCountForMonth.getInRepoAmountPledge() + "元;");
            content.append("其它/" + orderDailyCountForMonth.getInRepoAmountOther() + "元\r\n");

            content.append("上月上架总量：" + orderDailyCountForMonth.getOnshelvesProdNum() + "件\r\n");

            content.append("二、维修保养\r\n");
            content.append("上月维修保养结算总量：" + orderDailyCountForMonth.getServiceSettleNum() + "件\r\n");
            content.append("上月维修保养结算销售总额：" + orderDailyCountForMonth.getServiceSettleSellAmount() + "元\r\n");
            content.append("上月维修保养结算总成本：" + orderDailyCountForMonth.getServiceSettleCostAmount() + "元\r\n");
            content.append("上月维修保养结算净利润：" + orderDailyCountForMonth.getServiceSettleProfitAmount() + "元\r\n");


            ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
            paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
            paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
            paramOpMessageSave.setTitle(title);
            paramOpMessageSave.setSubType(EnumOpMessageSubType.DAILY_REPORT_MONTH.getCode());
            paramOpMessageSave.setContent(content.toString());
            paramOpMessageSave.setNativePage("dailyCount");

            opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NO_JUMP);

            //获取推送消息用户ID
            List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                    shopId, ConstantPermission.SALE_CHECK_ALL);

            //设置参数
//        Map<String, String> extraParam = new HashMap<>();
//        extraParam.put("orderId", "" + order.getNumber());
            opMessageService.addOpMessage(paramOpMessageSave, null, shopId, pushUserIdList, null);
        } catch (Exception e) {
            log.error("推送开单消息异常:" + e);
        }
    }


}
