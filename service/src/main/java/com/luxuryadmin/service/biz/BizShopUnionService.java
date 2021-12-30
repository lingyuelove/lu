package com.luxuryadmin.service.biz;


import com.luxuryadmin.entity.biz.BizShopUnion;
import com.luxuryadmin.param.biz.ParamLeaguerProductQuery;
import com.luxuryadmin.param.biz.ParamShopUnionAdd;
import com.luxuryadmin.param.biz.ParamShopUnionDelete;
import com.luxuryadmin.param.biz.ParamShopUnionForAdminBySearch;
import com.luxuryadmin.param.pro.ParamProProduct;
import com.luxuryadmin.vo.biz.VoShopUnionByAppShow;
import com.luxuryadmin.vo.biz.VoShopUnionForAdminList;
import com.luxuryadmin.vo.biz.VoShopUnionForAdminPage;
import com.luxuryadmin.vo.pro.VoLeaguerProduct;
import com.luxuryadmin.vo.pro.VoProductLoadForUnionPage;
import com.luxuryadmin.vo.shp.VoShpUser;

import java.util.List;

/**
 * biz_shop_union service
 *
 * @author zhangsai
 * @Date 2021-07-16 17:58:54
 */
public interface BizShopUnionService {
    /**
     * 新增商家联盟
     *
     * @param shopUnionAdd
     */
    void addShopUnion(ParamShopUnionAdd shopUnionAdd);

    /**
     * 移除商家联盟
     *
     * @param id
     */
    void removeUnionShop(Integer id);

    /**
     * 前段移除商家联盟
     *
     * @param shopUnionDelete
     */
    void removeUnionShopByApp(ParamShopUnionDelete shopUnionDelete);

    /**
     * 获取后台集合显示
     *
     * @param shopUnionForAdminBySearch
     * @return
     */
    VoShopUnionForAdminPage getShopUnionForAdminPage(ParamShopUnionForAdminBySearch shopUnionForAdminBySearch);
    /**
     * 获取后台集合显示(导出)
     *
     * @param shopUnionForAdminBySearch
     * @return
     */
    List<VoShopUnionForAdminList> listShopUnionForAdmin(ParamShopUnionForAdminBySearch shopUnionForAdminBySearch);
    /**
     * app端店铺详情的显示
     *
     * @param shopId
     * @return
     */
    VoShopUnionByAppShow getShopUnionByAppShow(Integer shopId);

    /**
     * 商家联盟商品集合显示
     *
     * @param productQuery
     * @return
     */
    List<VoLeaguerProduct> getUnionProductByAppShow(ParamLeaguerProductQuery productQuery);

    /**
     * 商品列表的集合显示
     *
     * @param proProduct
     * @return
     */
    VoProductLoadForUnionPage getProductLoadForUnionPage(ParamProProduct proProduct);


    /**
     * 获取商家联盟;根据状态;
     *
     * @param state -10 已退出 10已加入
     * @return
     */
    List<BizShopUnion> listBizShopUnionByState(String state);

    /**
     * 刷新商家联盟缓存名单;包含买家,卖家
     */
    void refreshBizShopUnionForRedis();


    /**
     * 直接查询商家联盟的所有商品;<br/>
     * 针对排序做了性能优化, 故不使用mysql分页, 使用java的业务逻辑分页;
     * 调用此方法, 不需要结合分页控件进行分页;
     *
     * @param productQuery
     * @return
     */
    List<VoLeaguerProduct> listUnionProductNoPage(ParamLeaguerProductQuery productQuery);

    /**
     * 获取商家联盟小程序分享用户组
     *
     * @return
     */
    List<VoShpUser> listUnionMpShareUser();

    /**
     *  从商家联盟下架该商品
     * @param ids
     */
    void backOfUnion(String ids);

    /**
     *  从商家联盟上架该商品
     * @param ids
     */
    void upOfUnion(String ids);

}
