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
     * ????????????
     */
    @Value("${spring.profiles.active}")
    private String env;

    @Override
    public Boolean pushJiGuangMessage(Platform platform, List<String> aliasList, String title, String msgContent,
                                      Map<String, String> extraParam) {
        PushPayload payload = buildPushPayload(platform, aliasList, title, msgContent, extraParam);

        try {
            PushResult result = jPushClient.sendPush(payload);
            log.info("????????????????????????: " + result);

        } catch (APIConnectionException e) {
            log.error("?????????????????????????????????????????????????????????", e);
            return Boolean.FALSE;
        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            log.error("?????????????????????????????????", e);
            log.info("?????????????????????????????? HTTP Status: " + e.getStatus());
            log.info("?????????????????????????????? Error Code: " + e.getErrorCode());
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private PushPayload buildPushPayload(Platform platform, List<String> aliasList, String title, String msgContent,
                                         Map<String, String> extraParam) {
        PushPayload.Builder pushPayLoadBuilder = PushPayload.newBuilder();
        //?????????????????? Platform.all(),Platform.android(),Platform.ios(),Platform.android_ios()
        pushPayLoadBuilder.setPlatform(platform);

        //???????????????????????????
        if (CollectionUtils.isEmpty(aliasList)) {
            pushPayLoadBuilder.setAudience(Audience.all());
        } else {
            Audience.Builder audienceBuilder = Audience.newBuilder();
            audienceBuilder.addAudienceTarget(AudienceTarget.alias(aliasList));
            pushPayLoadBuilder.setAudience(audienceBuilder.build());
        }

        //??????????????????
        //?????????????????????????????????ApnsProduction=true,?????????????????????
        log.info("????????????????????????" + env);
        if ("pro".equals(env)) {
            pushPayLoadBuilder.setOptions(Options.newBuilder()
                    .setApnsProduction(true)
                    .build());
        }

        //??????????????????
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
                        //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????key??????????????????????????????value???
                        .addExtras(extraParam)
                        .build()
                )
                .build();

        pushPayLoadBuilder.setNotification(notification);

        //???????????????????????????
        pushPayLoadBuilder.setMessage(Message.content(msgContent));

        //??????????????????

        return pushPayLoadBuilder.build();
    }


    /******************************* ?????????????????? *********************************/
    /**
     * ??????????????????????????????
     *
     * @param shopId
     */
    @Override
    public void pushUpdateProdPriceMsg(int shopId, ParamProductUpload paramProductUpload, ProProduct oldPro) {
        try {
            DecimalFormat df = new DecimalFormat("#0");
            //?????????????????????
            String oldInitPrice = df.format(oldPro.getInitPrice());
            Boolean isUpdateInitPrice = !oldInitPrice.equals(paramProductUpload.getInitPrice());
            //?????????????????????
            String oldTradePrice = df.format(oldPro.getTradePrice());
            Boolean isUpdateTradePrice = !oldTradePrice.equals(paramProductUpload.getTradePrice());
            //?????????????????????
            String oldAgencyPrice = df.format(oldPro.getAgencyPrice());
            Boolean isUpdateAgencyPrice = !oldAgencyPrice.equals(paramProductUpload.getAgencyPrice());
            //?????????????????????
            String oldSalePrice = df.format(oldPro.getSalePrice());
            Boolean isUpdateSalePrice = !oldSalePrice.equals(paramProductUpload.getSalePrice());

            //?????????????????????
            Boolean isUpdatePrice = isUpdateInitPrice || isUpdateTradePrice || isUpdateAgencyPrice || isUpdateSalePrice;

            //?????????????????????????????????????????????????????????
            if (!isUpdatePrice) {
                return;
            }

            String content;
            content = "???" + oldPro.getName() + "???????????????????????????????????????~";

            ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
            paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
            paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
            paramOpMessageSave.setTitle("????????????????????????");
            paramOpMessageSave.setSubType(EnumOpMessageSubType.UPDATE_PROD_PRICE.getCode());
            paramOpMessageSave.setContent(content);
            paramOpMessageSave.setNativePage("prodDetail");
            //?????????????????????????????????;2021-12-10 01:49:49
            List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                    shopId, ConstantPermission.CHK_PRICE_TRADE);
            opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NATIVE);
            //????????????
            Map<String, String> extraParam = new HashMap<>();
            extraParam.put("bizId", "" + paramProductUpload.getBizId());
            opMessageService.addOpMessage(paramOpMessageSave, null, shopId, pushUserIdList, extraParam);
        } catch (Exception e) {
            log.error("????????????????????????:" + e);
        }
    }

    @Override
    public void pushUpdateInitPriceMsg(int shopId, ParamProductUpload paramProductUpload, ProProduct oldPro) {
        try {
            DecimalFormat df = new DecimalFormat("#0");
            //?????????????????????
            String oldInitPrice = df.format(oldPro.getInitPrice());
            Boolean isUpdateInitPrice = !oldInitPrice.equals(paramProductUpload.getInitPrice());
            //?????????????????????
            Boolean isUpdatePrice = isUpdateInitPrice;

            //?????????????????????????????????????????????????????????
            if (!isUpdatePrice) {
                return;
            }
            BigDecimal oldPrice = new BigDecimal(oldInitPrice).divide(new BigDecimal(100));
            BigDecimal nowPrice = new BigDecimal(paramProductUpload.getInitPrice()).divide(new BigDecimal(100));
            String content;
            content = "????????????"+oldPrice+" ????????????"+nowPrice+"??????" + oldPro.getName() + "???";

            ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
            paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
            paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
            paramOpMessageSave.setTitle("????????????????????????");
            paramOpMessageSave.setSubType(EnumOpMessageSubType.UPDATE_PROD_INIT_PRICE.getCode());
            paramOpMessageSave.setContent(content);
            paramOpMessageSave.setNativePage("prodDetail");
            //?????????????????????????????????;2021-12-10 01:49:49
            List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                    shopId, ConstantPermission.CHK_PRICE_INIT);
            opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NATIVE);
            //????????????
            Map<String, String> extraParam = new HashMap<>();
            extraParam.put("bizId", "" + paramProductUpload.getBizId());
            opMessageService.addOpMessage(paramOpMessageSave, null, shopId, pushUserIdList, extraParam);
        } catch (Exception e) {
            log.error("????????????????????????:" + e);
        }
    }

    /**
     * ????????????????????????
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
                    content = "???" + prodName + "?????????????????????????????????~";

                    ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
                    paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
                    paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
                    paramOpMessageSave.setTitle("??????????????????");
                    paramOpMessageSave.setSubType(EnumOpMessageSubType.RELEASE_PROD.getCode());
                    paramOpMessageSave.setContent(content);
                    paramOpMessageSave.setNativePage("prodDetail");


                    opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NATIVE);

                    //????????????
                    Map<String, String> extraParam = new HashMap<>();
                    extraParam.put("bizId", "" + bizId);
                    opMessageService.addOpMessage(paramOpMessageSave, null, shopId, pushUserIdList, extraParam);
                }
            } else {
                //????????????
                List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                        shopId, ConstantPermission.CHK_ON_SALE_PRODUCT);
                String content;
                content = "???????????????????????????????????????????????????~";
                ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
                paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
                paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
                paramOpMessageSave.setTitle("??????????????????");
                paramOpMessageSave.setSubType(EnumOpMessageSubType.RELEASE_PROD.getCode());
                paramOpMessageSave.setContent(content);
                paramOpMessageSave.setNativePage("prodDetail");

                opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NATIVE);

                //????????????
                Map<String, String> extraParam = new HashMap<>();
                extraParam.put("bizId", "");
                opMessageService.addOpMessage(paramOpMessageSave, null, shopId, pushUserIdList, extraParam);

            }
        } catch (Exception e) {
            log.error("????????????????????????:" + e);
        }


    }

    /**
     * ????????????????????????
     *
     * @param shopId
     */
    @Override
    public void pushReleaseProductMsg(int shopId) {
        pushReleaseProductMsg(shopId, null);
    }

    /**
     * ????????????????????????
     *
     * @param shopId
     * @param proBizIdList
     */
    @Override
    public void pushDeleteProductMsg(int shopId, List<String> proBizIdList) {

        /**
         * ????????????????????????
         */
        try {
            List<VoProductSimple> prodList = proProductMapper.listProdNameByBizIdList(proBizIdList);
            for (VoProductSimple voProductSimple : prodList) {
                String prodName = voProductSimple.getProdName();
                String bizId = voProductSimple.getBizId();
                String content;
                content = "???" + prodName + "????????????~";

                ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
                paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
                paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
                paramOpMessageSave.setTitle("??????????????????");
                paramOpMessageSave.setSubType(EnumOpMessageSubType.DELETE_PROD.getCode());
                paramOpMessageSave.setContent(content);
                paramOpMessageSave.setNativePage("prodDetail");
                //???????????????
                List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                        shopId, ConstantPermission.CHK_DELETE_HISTORY);
                opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NATIVE);
                //????????????
                Map<String, String> extraParam = new HashMap<>();
                extraParam.put("bizId", "" + bizId);
                opMessageService.addOpMessage(paramOpMessageSave, null, shopId, pushUserIdList, extraParam);
            }
        } catch (Exception e) {
            log.error("????????????????????????:" + e);
        }
    }

    /**
     * ????????????????????????
     *
     * @param shopId
     * @param proBizIdList
     */
    @Override
    public void pushRedeemProductMsg(int shopId, List<String> proBizIdList) {

        /**
         * ??????????????????????????????
         */
        try {
            List<VoProductSimple> prodList = proProductMapper.listProdNameByBizIdList(proBizIdList);
            for (VoProductSimple voProductSimple : prodList) {
                String prodName = voProductSimple.getProdName();
                String bizId = voProductSimple.getBizId();
                String content;
                content = "???" + prodName + "??????????????????????????????~";

                ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
                paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
                paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
                paramOpMessageSave.setTitle("????????????????????????");
                paramOpMessageSave.setSubType(EnumOpMessageSubType.REDEEM_PROD.getCode());
                paramOpMessageSave.setContent(content);
                paramOpMessageSave.setNativePage("prodDetail");

                List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                        shopId, ConstantPermission.CHK_PAWN_PRODUCT);
                opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NATIVE);
                //????????????
                Map<String, String> extraParam = new HashMap<>();
                extraParam.put("bizId", "" + bizId);
                opMessageService.addOpMessage(paramOpMessageSave, null, shopId, pushUserIdList, extraParam);
            }
        } catch (Exception e) {
            log.error("????????????????????????????????????:" + e);
        }
    }

    /**
     * ??????????????????????????????
     *
     * @param expireProdList
     */
    @Override
    public void pushExpireProductMsg(List<VoExpireProd> expireProdList) {

        /**
         * ??????????????????????????????
         */
        try {
            for (VoExpireProd voExpireProd : expireProdList) {
                String prodName = voExpireProd.getProName();
                String bizId = voExpireProd.getBizId();
                Integer shopId = voExpireProd.getShopId();
                String content;
                content = "???" + prodName + "?????????????????????????????????";

                ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
                paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
                paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
                paramOpMessageSave.setTitle("????????????????????????");
                paramOpMessageSave.setSubType(EnumOpMessageSubType.EXPIRE_PROD.getCode());
                paramOpMessageSave.setContent(content);
                paramOpMessageSave.setNativePage("prodDetail");

                List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                        shopId, ConstantPermission.CHK_PAWN_PRODUCT);
                opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NATIVE);
                //????????????
                Map<String, String> extraParam = new HashMap<>();
                extraParam.put("bizId", "" + bizId);
                opMessageService.addOpMessage(paramOpMessageSave, null, shopId, pushUserIdList, extraParam);
            }
        } catch (Exception e) {
            log.error("????????????????????????????????????:" + e);
        }
    }


    /**
     * ????????????????????????
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
                content = "???" + prodName + "?????????????????????????????????~";

                ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
                paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
                paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
                paramOpMessageSave.setTitle("??????????????????");
                paramOpMessageSave.setSubType(EnumOpMessageSubType.UPLOAD_PROD.getCode());
                paramOpMessageSave.setContent(content);
                paramOpMessageSave.setNativePage("prodDetail");


                opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NATIVE);

                //????????????
                Map<String, String> extraParam = new HashMap<>();
                extraParam.put("bizId", "" + bizId);
                opMessageService.addOpMessage(paramOpMessageSave, null, shopId, pushUserIdList, extraParam);
            }
        } catch (Exception e) {
            log.error("????????????????????????:" + e);
        }
    }

    @Override
    public void pushLockProductMsg(int shopId, String prodName, String bizId, String lockId) {
        try {
            List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                    shopId, ConstantPermission.CHK_LOCK_PRODUCT);
            String content = "???" + prodName + "????????????????????????????????????????~";
            ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
            paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
            paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
            paramOpMessageSave.setTitle("??????????????????");
            paramOpMessageSave.setSubType(EnumOpMessageSubType.LOCK_PROD.getCode());
            paramOpMessageSave.setContent(content);
            paramOpMessageSave.setNativePage("lockDetail");
            opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NATIVE);

            //????????????
            Map<String, String> extraParam = new HashMap<>();
            extraParam.put("bizId", "" + bizId);
            extraParam.put("lockId", "" + lockId);
            opMessageService.addOpMessage(paramOpMessageSave, null, shopId, pushUserIdList, extraParam);
        } catch (Exception e) {
            log.error("????????????????????????:" + e);
        }
    }

    @Override
    public void recycleProductMsg(int shopId, String prodName, String bizId) {
        try {
            List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                    shopId, ConstantPermission.CHK_LOCK_PRODUCT);
            String content = "???" + prodName + "???????????????????????????????????????~";
            ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
            paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
            paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
            paramOpMessageSave.setTitle("??????????????????");
            paramOpMessageSave.setSubType(EnumOpMessageSubType.RECYCLE_PROD.getCode());
            paramOpMessageSave.setContent(content);
            paramOpMessageSave.setNativePage("prodDetail");
            opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NATIVE);

            //????????????
            Map<String, String> extraParam = new HashMap<>();
            extraParam.put("bizId", "" + bizId);
            opMessageService.addOpMessage(paramOpMessageSave, null, shopId, pushUserIdList, extraParam);
        } catch (Exception e) {
            log.error("????????????????????????:" + e);
        }
    }

    @Override
    public void pushMessageForExpiredProduct(Integer expiredNoticeId, Integer shopId, Integer expiredDay) {
        ParamOpMessageSave opMessageSave = new ParamOpMessageSave();
        opMessageSave.setType(EnumOpMessageType.SHOP.getCode());
        opMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
        opMessageSave.setTitle("??????????????????");
        opMessageSave.setSubType(EnumOpMessageSubType.EXPIRED_PRODUCT.getCode());
        opMessageSave.setContent("????????????" + expiredDay + "????????????????????????????????????????????????????????????~");
        opMessageSave.setNativePage("expiredNoticeList");
        List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                shopId, ConstantPermission.MSG_PRO_EXPIRE);
        opMessageService.setCommonMsgParamNative(opMessageSave, EnumOpMessageJumpType.NATIVE);
        //????????????jump:{jumptype:1,jumpData:{id:111}}
        Map<String, String> extraParam = new HashMap<>();
        //jumpData???
        Map<String, Object> extraParamGrandson = new HashMap<>();
        extraParamGrandson.put("id", "" + expiredNoticeId);
        //jumptype???
        Map<String, Object> extraParamSon = new HashMap<>();
        extraParamSon.put("jumptype", "1");
        extraParamSon.put("jumpData", extraParamGrandson);
        //map???string
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
        paramOpMessageSave.setTitle("??????????????????");
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

    /************************************  ???????????????????????? *********************************/

    /**
     * ??????????????????
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
            content = userName + "??????" + saleNum + "??????" + prodName + "???";

            ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
            paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
            paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
            paramOpMessageSave.setTitle("??????????????????");
            paramOpMessageSave.setSubType(EnumOpMessageSubType.SALE_PROD.getCode());
            paramOpMessageSave.setContent(content);
            paramOpMessageSave.setNativePage("orderDetail");

            opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NATIVE);

            //????????????????????????ID
            List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                    shopId, ConstantPermission.CHK_ALL_ORDER);
            //????????????
            Map<String, String> extraParam = new HashMap<>();
            extraParam.put("orderId", "" + order.getNumber());
            opMessageService.addOpMessage(paramOpMessageSave, null, shopId, pushUserIdList, extraParam);
        } catch (Exception e) {
            log.error("????????????????????????:" + e);
        }
    }

    /**
     * ????????????????????????
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
            //TODO ??????????????????
            String prodName = "??????";
            if (null != pro) {
                prodName = pro.getName();
            }

            content = userName + "????????????" + prodName + "???";

            ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
            paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
            paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
            paramOpMessageSave.setTitle("??????????????????");
            paramOpMessageSave.setSubType(EnumOpMessageSubType.CANCEL_ORDER.getCode());
            paramOpMessageSave.setContent(content);
            paramOpMessageSave.setNativePage("orderDetail");

            opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NATIVE);

            //????????????????????????ID
            List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                    shopId, ConstantPermission.CHK_ALL_ORDER);

            //????????????
            Map<String, String> extraParam = new HashMap<>();
            extraParam.put("orderId", "" + order.getNumber());
            opMessageService.addOpMessage(paramOpMessageSave, null, shopId, pushUserIdList, extraParam);
        } catch (Exception e) {
            log.error("????????????????????????:" + e);
        }
    }

    /**
     * ????????????????????????
     *
     * @param shopId
     * @param order
     */
    @Override
    public void pushUpdateOrderMsg(int shopId, OrdOrder order, ProProduct pro, Integer userId) {
        try {

            String content;

            String userName = shpUserShopRefService.getNameFromShop(shopId, userId);
            //TODO ??????????????????
            String prodName = "??????";
            if (null != pro) {
                prodName = pro.getName();
            }
            content = userName + "????????????" + prodName + "??????????????????";

            ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
            paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
            paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
            paramOpMessageSave.setTitle("??????????????????");
            paramOpMessageSave.setSubType(EnumOpMessageSubType.UPDATE_ORDER.getCode());
            paramOpMessageSave.setContent(content);
            paramOpMessageSave.setNativePage("orderDetail");

            opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NATIVE);

            //????????????????????????ID
            List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                    shopId, ConstantPermission.CHK_ALL_ORDER);

            //????????????
            Map<String, String> extraParam = new HashMap<>();
            extraParam.put("orderId", "" + order.getNumber());
            opMessageService.addOpMessage(paramOpMessageSave, null, shopId, pushUserIdList, extraParam);
        } catch (Exception e) {
            log.error("????????????????????????:" + e);
        }
    }

    @Override
    public void pushShpOrderDailyCount(Integer shopId, ShpOrderDailyCount shpOrderDailyCount) {
        try {
            String shopName = shpShopMapper.getVoUserShopBaseByShopId(shopId).getShopName();
            String countDateStr = DateUtil.format(shpOrderDailyCount.getCountDate(), "MM???dd???");
            String title = countDateStr + "??????";
            String subTitle = "????????????????????????>";


            StringBuilder content = new StringBuilder();
            content.append("???" + shopName + "???\r\n");
            content.append("????????????\r\n");
            content.append("?????????????????????" + shpOrderDailyCount.getOrdNum() + "???\r\n");
            content.append("?????????????????????" + shpOrderDailyCount.getOrdAmount() + "???\r\n");
            content.append("??????????????????" + shpOrderDailyCount.getOrdNetProfit() + "???\r\n");
            content.append("?????????????????????" + shpOrderDailyCount.getOrdCancelNum() + "???\r\n");
            content.append("?????????????????????" + shpOrderDailyCount.getOrdCancelAmount() + "???\r\n");

            content.append("?????????????????????");
            content.append("??????/" + shpOrderDailyCount.getInRepoNumSelf() + "???;");
            content.append("??????/" + shpOrderDailyCount.getInRepoNumConsignment() + "???;");
            content.append("??????/" + shpOrderDailyCount.getInRepoNumPledge() + "???;");
            content.append("??????/" + shpOrderDailyCount.getInRepoNumOther() + "???\r\n");

            content.append("????????????????????????");
            content.append("??????/" + shpOrderDailyCount.getInRepoAmountSelf() + "???;");
            content.append("??????/" + shpOrderDailyCount.getInRepoAmountConsignment() + "???;");
            content.append("??????/" + shpOrderDailyCount.getInRepoAmountPledge() + "???;");
            content.append("??????/" + shpOrderDailyCount.getInRepoAmountOther() + "???\r\n");

            content.append("?????????????????????" + shpOrderDailyCount.getOnshelvesProdNum() + "???\r\n");

            content.append("??????????????????\r\n");
            content.append("?????????????????????????????????" + shpOrderDailyCount.getServiceSettleNum() + "???\r\n");
            content.append("???????????????????????????????????????" + shpOrderDailyCount.getServiceSettleSellAmount() + "???\r\n");
            content.append("????????????????????????????????????" + shpOrderDailyCount.getServiceSettleCostAmount() + "???\r\n");
            content.append("????????????????????????????????????" + shpOrderDailyCount.getServiceSettleProfitAmount() + "???\r\n");

            ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
            paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
            paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
            paramOpMessageSave.setTitle(title);
            paramOpMessageSave.setSubType(EnumOpMessageSubType.DAILY_REPORT.getCode());
            paramOpMessageSave.setContent(content.toString());
            paramOpMessageSave.setNativePage("dailyCount");

            opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NO_JUMP);

            //????????????????????????ID
            List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                    shopId, ConstantPermission.SALE_CHECK_ALL);

            //????????????
//        Map<String, String> extraParam = new HashMap<>();
//        extraParam.put("orderId", "" + order.getNumber());
            opMessageService.addOpMessage(paramOpMessageSave, null, shopId, pushUserIdList, null);
        } catch (Exception e) {
            log.error("????????????????????????:" + e);
        }
    }

    @Override
    public void pushShpOrderDailyCountForMonth(Integer shopId, VoShpOrderDailyCountForMonth orderDailyCountForMonth) {
        try {
            String shopName = shpShopMapper.getVoUserShopBaseByShopId(shopId).getShopName();
            String countDateStr = DateUtil.format(orderDailyCountForMonth.getCountDate(), "MM???dd???");
            String title = countDateStr + "??????";
            String subTitle = "????????????????????????>";

            StringBuilder content = new StringBuilder();
            content.append("???" + shopName + "???\r\n");
            content.append("????????????\r\n");
            content.append("?????????????????????" + orderDailyCountForMonth.getOrdNum() + "???\r\n");
            content.append("?????????????????????" + orderDailyCountForMonth.getOrdAmount() + "???\r\n");
            content.append("??????????????????" + orderDailyCountForMonth.getOrdNetProfit() + "???\r\n");
            content.append("?????????????????????" + orderDailyCountForMonth.getOrdCancelNum() + "???\r\n");
            content.append("?????????????????????" + orderDailyCountForMonth.getOrdCancelAmount() + "???\r\n");

            content.append("?????????????????????");
            content.append("??????/" + orderDailyCountForMonth.getInRepoNumSelf() + "???;");
            content.append("??????/" + orderDailyCountForMonth.getInRepoNumConsignment() + "???;");
            content.append("??????/" + orderDailyCountForMonth.getInRepoNumPledge() + "???;");
            content.append("??????/" + orderDailyCountForMonth.getInRepoNumOther() + "???\r\n");

            content.append("????????????????????????");
            content.append("??????/" + orderDailyCountForMonth.getInRepoAmountSelf() + "???;");
            content.append("??????/" + orderDailyCountForMonth.getInRepoAmountConsignment() + "???;");
            content.append("??????/" + orderDailyCountForMonth.getInRepoAmountPledge() + "???;");
            content.append("??????/" + orderDailyCountForMonth.getInRepoAmountOther() + "???\r\n");

            content.append("?????????????????????" + orderDailyCountForMonth.getOnshelvesProdNum() + "???\r\n");

            content.append("??????????????????\r\n");
            content.append("?????????????????????????????????" + orderDailyCountForMonth.getServiceSettleNum() + "???\r\n");
            content.append("???????????????????????????????????????" + orderDailyCountForMonth.getServiceSettleSellAmount() + "???\r\n");
            content.append("????????????????????????????????????" + orderDailyCountForMonth.getServiceSettleCostAmount() + "???\r\n");
            content.append("????????????????????????????????????" + orderDailyCountForMonth.getServiceSettleProfitAmount() + "???\r\n");


            ParamOpMessageSave paramOpMessageSave = new ParamOpMessageSave();
            paramOpMessageSave.setType(EnumOpMessageType.SHOP.getCode());
            paramOpMessageSave.setPushPlatform(EnumOpMessagePushPlatform.ALL.getCode());
            paramOpMessageSave.setTitle(title);
            paramOpMessageSave.setSubType(EnumOpMessageSubType.DAILY_REPORT_MONTH.getCode());
            paramOpMessageSave.setContent(content.toString());
            paramOpMessageSave.setNativePage("dailyCount");

            opMessageService.setCommonMsgParamNative(paramOpMessageSave, EnumOpMessageJumpType.NO_JUMP);

            //????????????????????????ID
            List<Integer> pushUserIdList = shpPermUserRefService.listShopUserIdWithPermission(
                    shopId, ConstantPermission.SALE_CHECK_ALL);

            //????????????
//        Map<String, String> extraParam = new HashMap<>();
//        extraParam.put("orderId", "" + order.getNumber());
            opMessageService.addOpMessage(paramOpMessageSave, null, shopId, pushUserIdList, null);
        } catch (Exception e) {
            log.error("????????????????????????:" + e);
        }
    }


}
