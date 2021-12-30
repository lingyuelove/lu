package com.luxuryadmin.mapper.biz;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.biz.BizLeaguer;
import com.luxuryadmin.vo.biz.VoBizLeaguer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 */
@Mapper
public interface BizLeaguerMapper extends BaseMapper {


    /**
     * 精确查找店铺;根据店长手机号或者店铺编号
     *
     * @param shopId
     * @param shopNumber
     * @return
     */
    List<VoBizLeaguer> searchShop(@Param("shopId") int shopId, @Param("shopNumber") String shopNumber);

    /**
     * 查找友商店铺的基本信息
     *
     * @param myShopId
     * @param leaguerShopId
     * @return
     */
    VoBizLeaguer getLeaguerShop(@Param("myShopId") int myShopId, @Param("leaguerShopId") int leaguerShopId);

    /**
     * 获取对该友商不可见的其它友商的shopId; visible='0'
     *
     * @param shopId (当前登录的shopId)
     * @return
     */
    List<Integer> listBizLeaguerNoVisible(int shopId);

    /**
     * 获取友商的店铺id
     *
     * @param shopId
     * @param isTop  是否只看星标友商商品
     * @return
     */
    List<Integer> listBizLeaguerShopIdByShopId(@Param("shopId") int shopId, @Param("isTop") String isTop);

    /**
     * 友商通讯录; BizLeaguer.state='20'
     *
     * @param shopId
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
     * 删除友商
     *
     * @param shopId
     * @param leaguerShopId
     * @return
     */
    int deleteBizLeaguer(@Param("shopId") int shopId, @Param("leaguerShopId") int leaguerShopId);


    /**
     * 修改友商备注
     *
     * @param leaguerId
     * @param shopId
     * @param leaguerShopId
     * @param note
     * @return
     */
    int updateLeaguerNote(
            @Param("leaguerId") int leaguerId, @Param("shopId") int shopId,
            @Param("leaguerShopId") int leaguerShopId, @Param("note") String note);


    /**
     * 查找友商记录;状态等于20
     *
     * @param shopId
     * @param leaguerShopId
     * @return
     */
    BizLeaguer getBizLeaguerByShopIdAndLeaguerShopId(
            @Param("shopId") int shopId, @Param("leaguerShopId") int leaguerShopId);

    /**
     * 获取友商店铺的详细信息
     *
     * @param myShopId
     * @param leaguerShopId
     * @return
     */
    VoBizLeaguer getSpecificLeaguerDetail(
            @Param("myShopId") int myShopId, @Param("leaguerShopId") int leaguerShopId);


    /**
     * 查询不想看到友商商品的友商店铺ID列表
     *
     * @param shopId
     * @return
     */
    List<Integer> listBizLeaguerNoWantSee(Integer shopId);

    /**
     * 修改店铺【友商总权限】时，修改对应所有的友商【权限】
     *
     * @param shopId
     * @param isUpdateSalePrice
     * @param isCanSeeSalePrice
     * @param isUpdateTradePrice
     * @param isCanSeeTradePrice
     */
    Integer updateAllBizLeaguerRecord(@Param("shopId") Integer shopId,
                                      @Param("isUpdateSalePrice") Integer isUpdateSalePrice,
                                      @Param("isCanSeeSalePrice") Integer isCanSeeSalePrice,
                                      @Param("isUpdateTradePrice") Integer isUpdateTradePrice,
                                      @Param("isCanSeeTradePrice") Integer isCanSeeTradePrice);

    /**
     * 根据shopId查询友商数量
     * @param shopId
     * @return
     */
    Integer countLeaguerNumByShopId(Integer shopId);

    /**
     * 友商店铺根据状态搜索
     * @param myShopId
     * @param leaguerShopId
     * @param state
     * @return
     */
    VoBizLeaguer  getLeaguerDetailForState(
            @Param("myShopId") int myShopId, @Param("leaguerShopId") int leaguerShopId);

    void deleteLeaguerForShop(int shopId);

    /**
     * 查看此友商是否是好友友商是否
     * @param myShopId
     * @param leaguerShopId
     * @return
     */
    String getLeaguerFriendState( @Param("myShopId")int myShopId,@Param("leaguerShopId") int leaguerShopId);
}