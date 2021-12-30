package com.luxuryadmin.service.op;

import cn.jpush.api.push.model.Platform;
import com.luxuryadmin.entity.biz.BizUnionVerify;
import com.luxuryadmin.entity.ord.OrdOrder;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.entity.shp.ShpOrderDailyCount;
import com.luxuryadmin.param.pro.ParamProductUpload;
import com.luxuryadmin.vo.pro.VoExpireProd;
import com.luxuryadmin.vo.shp.VoShpOrderDailyCountForMonth;

import java.util.List;
import java.util.Map;

/**
 * 消息中心--公告
 *
 * @author monkey king
 * @date 2020-01-16 20:48:33
 */
public interface OpPushService {
    /**
     * 极光推送消息
     * @param platform 推送平台 Platform.all(),Platform.ios();
     * @param registrationIdList 推送用户【注册ID】列表
     * @param msgContent 推送内容
     * @param extraParam
     * @return
     */
    Boolean pushJiGuangMessage(Platform platform, List<String> registrationIdList,String title, String msgContent,
                               Map<String,String> extraParam);

    /******************************* 消息推送 *********************************/

    /**
     * 发送商品价格变动通知消息
     *
     * @param shopId
     * @param paramProductUpload
     */
    void pushUpdateProdPriceMsg(int shopId, ParamProductUpload paramProductUpload, ProProduct oldPro);

     void pushUpdateInitPriceMsg(int shopId, ParamProductUpload paramProductUpload, ProProduct oldPro);

    /**
     * 上架商品消息
     * @param shopId
     * @param proBizIdList
     */
    void pushReleaseProductMsg(int shopId, List<String> proBizIdList);

    /**
     * 全店上架商品消息
     * @param shopId
     */
    void pushReleaseProductMsg(int shopId);

    /**
     * 删除商品消息
     * @param shopId
     * @param proBizIdList
     */
    void pushDeleteProductMsg(int shopId, List<String> proBizIdList);

    /**
     * 赎回商品消息
     * @param shopId
     * @param proBizIdList
     */
    void pushRedeemProductMsg(int shopId, List<String> proBizIdList);

    /**
     * 到期商品消息
     * @param expireProdList
     */
    void pushExpireProductMsg(List<VoExpireProd> expireProdList);

    /**
     * 开单消息
     * @param shopId
     * @param order
     * @param pro
     */
    void pushOrderMsg(int shopId, OrdOrder order, ProProduct pro);

    /**
     * 取消订单消息
     * @param shopId
     * @param order
     * @param pro
     */
    void pushCancelOrderMsg(int shopId, OrdOrder order, ProProduct pro);

    /**
     * 修改订单消息
     * @param shopId
     * @param order
     * @param pro
     * @param userId
     */
    void pushUpdateOrderMsg(int shopId, OrdOrder order, ProProduct pro, Integer userId);

    /**
     * 店铺订单商品每日统计消息
     * @param shopId
     * @param shpOrderDailyCount
     */
    void pushShpOrderDailyCount(Integer shopId, ShpOrderDailyCount shpOrderDailyCount);

    /**
     * 店铺订单商品每日统计消息
     * @param shopId
     * @param orderDailyCountForMonth
     */
    void pushShpOrderDailyCountForMonth(Integer shopId, VoShpOrderDailyCountForMonth orderDailyCountForMonth);

    /**
     * 商品入库消息
     * @param shopId
     * @param bizIdList
     */
    void pushUploadProductMsg(int shopId, List<String> bizIdList);

    /**
     * 商品锁单消息
     * @param shopId
     * @param prodName
     * @param bizId
     * @param lockId
     */
    void pushLockProductMsg(int shopId,String prodName, String bizId,String lockId);

    /**
     * 寄卖卖商品添加取回消息
     * @param shopId
     * @param prodName
     * @param bizId
     */
    void recycleProductMsg(int shopId,String prodName, String bizId);

    /**
     * 店铺到期提醒商品
     * @param expiredNoticeId
     * @param shopId
     * @param expiredDay
     */
    void pushMessageForExpiredProduct(Integer expiredNoticeId,Integer shopId, Integer expiredDay);

    /**
     * 商家联盟审核结果通知
     */
    void sendMessageUnionVerify(BizUnionVerify bizUnionVerify, String remark);
}
