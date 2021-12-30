package com.luxuryadmin.service.pro;

import com.luxuryadmin.entity.pro.ProShare;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.vo.pro.*;

import java.util.List;

/**
 * 分享商品--业务逻辑层
 *
 * @author monkey king
 * @date 2020-06-10 13:53:08
 */
public interface ProShareService {


    /**
     * 根据店铺编号,用户编号和分享批号来获取分享的产品id
     *
     * @param shareProduct
     * @return
     */
    VoShareProduct getProIdsByShareBatch(ParamShareProductQuery shareProduct);

    /**
     * 查询分享出去的商品列表
     *
     * @param productQuery
     * @return
     */
    List<VoProductLoad> listShareProductByProId(ParamShareProductQuery productQuery);

    /**
     * 保存分享商品记录<br/>
     *
     * @param proShare
     * @return 返回分享批号, 如果用户有自定义则返回用户自定义的批号;反之,返回时间戳;
     */
    String saveShareProduct(ProShare proShare);

    /**
     * 分享小程序二维码 只有店铺分享 商品分享 商品详情分享
     *
     * @param save
     * @return
     */
    String saveShareProductForQRCode(ParamShareProductSave save);

    /**
     * 删除店铺商品分享记录
     *
     * @param shopId
     */
    void deleteProShareByShopId(int shopId);

    /**
     * 获取分享记录
     *
     * @param paramShare
     * @return
     */
    VoSharePage getShareByList(ParamShare paramShare);

    /**
     * 删除商品分享记录
     *
     * @param id
     */
    void deleteById(Integer id);


    /**
     * 分享商品
     *
     * @param shareProductForApplets
     * @return
     */
    VoShareProductForApplets getByShareBatch(ParamShareProductForApplets shareProductForApplets);

    /**
     * 获取分享用户记录
     *
     * @param paramShareUser
     * @return
     */
    VoShareUserPage listUnionShareUser(ParamShareUser paramShareUser);


    /**
     * 获取分享用户记录
     *
     * @param paramShareUser
     * @return
     */
    List<VoUnionAccess> listUnionShareUser(ParamUnionAccess paramShareUser);

    /**
     * 获取大于某个日期的访问人次
     *
     * @param paramShareUser
     * @return
     */
    int countAccessUserCount(ParamUnionAccess paramShareUser);

    /**
     * 获取大于某个日期的访问次数
     *
     * @param paramShareUser
     * @return
     */
    int countAccessCount(ParamUnionAccess paramShareUser);

    /**
     * 根据分享批号获取分享分类
     *
     * @param shareBatch
     * @return
     */
    List<VoProClassify> listProClassifyByShareBatch(String shareBatch);

    /**
     * 原样直接保存或者更新实体
     *
     * @param proShare
     * @return id
     */
    int saveOrUpdateObject(ProShare proShare);
}
