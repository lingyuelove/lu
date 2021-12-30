package com.luxuryadmin.service.biz;

import com.luxuryadmin.entity.biz.BizLeaguer;
import com.luxuryadmin.param.biz.ParamLeaguerProductQuery;
import com.luxuryadmin.param.biz.ParamSpecificLeaguerProductQuery;
import com.luxuryadmin.vo.biz.VoBizLeaguer;
import com.luxuryadmin.vo.biz.VoCanSeeLeaguerPriceInfo;
import com.luxuryadmin.vo.pro.VoLeaguerProduct;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 友商首页--业务逻辑层
 *
 * @author monkey king
 * @date 2020-01-11 20:10:06
 */
public interface BizLeaguerService {



    /**
     * 精确查找店铺;根据店长手机号或者店铺编号
     *
     * @param shopNumber
     * @param shopId
     * @return
     */
    List<VoBizLeaguer> searchShop(int shopId, String shopNumber);

    /**
     * 查找友商店铺的基本信息
     *
     * @param myShopId
     * @param leaguerShopId
     * @return
     */
    VoBizLeaguer getLeaguerShop(int myShopId, int leaguerShopId);


    /**
     * 添加友商记录;
     *
     * @param bizLeaguer
     * @return
     */
    int saveBizLeaguer(BizLeaguer bizLeaguer);

    /**
     * 获取(所有)友商的商品;<br/>
     * 1.获取友商;(除非友商对其设置不允许查看)
     *
     * @param paramQuery
     * @return
     */
    List<VoLeaguerProduct> listBizLeaguerProductByShopIds(ParamLeaguerProductQuery paramQuery);

    /**
     * 获取(具体)友商的商品;<br/>
     * @param shopId
     * @param paramQuery
     * @return
     */
    List<VoLeaguerProduct> listSpecificBizLeaguerProduct(ParamSpecificLeaguerProductQuery paramQuery,Integer shopId);

    /**
     * 获取(具体)友商的商品;<br/>
     * @param shopId
     * @param paramQuery
     * @return
     */
    List<VoLeaguerProduct> listSpecificBizUnionProduct(ParamSpecificLeaguerProductQuery paramQuery);

    Boolean isCanSeeSalePrice(Integer leaguerShopIdA, Integer leaguerShopIdB);

    Boolean isCanSeeTradePrice(Integer leaguerShopIdA, Integer leaguerShopIdB);

    /**
     * 商家联盟是否可以查看友商价
     * @param leaguerShopId
     * @param userId
     * @return
     */
    VoCanSeeLeaguerPriceInfo isCanSeePriceForUnion(Integer leaguerShopId, Integer userId) ;
    /**
     * 友商通讯录<br/>
     * 查找该店铺的友商成员; BizLeaguer.state='20'
     *
     * @param shopId 登录帐号的店铺id
     * @return
     */
    List<VoBizLeaguer> listBizLeaguerByShopId(int shopId);

    /**
     * 是否允许友商查看店铺商品
     *
     * @param bizLeaguer
     * @return 受影响行数
     */
    int modifyVisible(BizLeaguer bizLeaguer);

    /**
     * 删除友商;(互相删除,即对方列表也会移除自己)
     *
     * @param userId
     * @param shopId
     * @param leaguerShopId
     * @return 受影响行数; 返回2 表示删除正常; 成对删除
     */
    void deleteBizLeaguer(int userId, int shopId, int leaguerShopId, HttpServletRequest request);

    /**
     * 修改友商备注
     *
     * @param leaguerId
     * @param shopId
     * @param leaguerShopId
     * @param note
     * @return
     */
    int updateLeaguerNote(int leaguerId, int shopId, int leaguerShopId, String note);

    /**
     * 查找友商记录;状态等于20
     *
     * @param shopId
     * @param leaguerShopId
     * @return
     */
    BizLeaguer getBizLeaguerByShopIdAndLeaguerShopId(int shopId, int leaguerShopId);

    /**
     * 获取友商店铺的详细信息
     *
     * @param myShopId
     * @param leaguerShopId
     * @return
     */
    VoBizLeaguer getSpecificLeaguerDetail(int myShopId, int leaguerShopId);

    /**
     * 删除店铺友商
     * @param shopId
     */
    void deleteLeaguerForShop(int shopId);

    /**
     * 查看此友商是否是好友友商是否
     * @param myShopId
     * @param leaguerShopId
     * @return
     */
    String getLeaguerFriendState(int myShopId, int leaguerShopId);
}
