package com.luxuryadmin.service.shp;

import com.luxuryadmin.entity.shp.ShpDetail;
import com.luxuryadmin.entity.shp.ShpShop;
import com.luxuryadmin.param.mem.ParamMemShop;
import com.luxuryadmin.param.shp.ParamGetShopInfoByNumber;
import com.luxuryadmin.param.shp.ParamShpShop;
import com.luxuryadmin.vo.mem.VoMemShop;
import com.luxuryadmin.vo.mem.VoMemShopById;
import com.luxuryadmin.vo.shp.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 店铺基本信息;包含开通会员信息;
 *
 * @author monkey king
 * @date 2019-12-14 02:19:19
 */
public interface ShpShopService {


    /**
     * 成为店主(开店)
     *
     * @param shpShop
     * @param shpDetail
     * @return
     */
    int becomeShopkeeper(ShpShop shpShop, ShpDetail shpDetail);

    /**
     * 获取用户的店铺基础信息
     *
     * @param shopId
     * @return
     */
    VoUserShopBase getVoUserShopBaseByShopId(int shopId);

    /**
     * 选择店铺
     *
     * @param userId
     * @param token
     * @return
     */
    List<VoShopBase> chooseShop(int userId, String token);

    /**
     * 根据店铺id获取店铺编号
     *
     * @param shopId
     * @return
     */
    Map<String, Object> getShopNumberAndShopNameByShopId(int shopId);

    /**
     * 更新店铺信息
     *
     * @param shpShop
     * @param shpDetail
     * @return
     */
    int updateShopInfo(ShpShop shpShop, ShpDetail shpDetail);

    /**
     * 获取订单凭证所需的店铺信息;
     *
     * @param shopId
     * @return
     */
    VoUserShopBase getShopInfoToOrdReceipt(int shopId);

    /**
     * 根据店铺编号获取分享店铺的信息
     *
     * @param shopNumber
     * @return
     */
    VoUserShopBase getShareShopInfoByShopNumber(String shopNumber);


    /**
     * 根据店铺编号获取店铺id
     *
     * @param shopNumber
     * @return
     */
    Integer getShopIdByShopNumber(String shopNumber);

    /**
     * 根据店铺编号获取店铺id
     *
     * @param phone
     * @param shopNumber
     * @return
     */
    Integer getShopIdByPhoneAndShopNumber(String phone, String shopNumber);

    /**
     * 获取所有店铺的id和店主的userId
     *
     * @return
     */
    List<VoUserShopBase> listAllShopIdAndUserId();

    /**
     * 条件查询店铺列表
     *
     * @param paramShpShop
     * @return
     */
    List<VoShpShop> queryShpShopList(ParamShpShop paramShpShop);

    /**
     * 根据id查询店铺详情
     *
     * @param id
     * @return
     */
    HashMap<String, Object> getShpShopInfo(String id);

    /**
     * 根据id查询店铺
     *
     * @param id
     * @return
     */
    ShpShop getShpShopById(String id);

    /**
     * 修改店铺
     *
     * @param shpShop
     */
    void updateShpShop(ShpShop shpShop);


    /**
     * 根据经营者的手机号获取店铺编号
     *
     * @param phone
     * @return
     */
    List<Map<String, String>> getShopNumberByPhone(String phone);

    /**
     * 根据[店铺ID]获取[经营者用户ID]
     *
     * @param shopId
     * @return
     */
    Integer getBossUserIdByShopId(int shopId);

    /**
     * 查询所有的店铺ID
     *
     * @return
     */
    List<Integer> queryShpShopIdList();

    /**
     * 注销店铺
     *
     * @param shopId
     */
    void destroyShop(int shopId);


    /**
     * 根据店铺编号
     *
     * @param shopId
     * @param yesOrNo
     * @return
     */
    int updateShopMember(int shopId, String yesOrNo);

    /**
     * 获取所有店铺, 用于初始化会员时间<br/>
     * 只获取id,is_member,insert_time字段
     *
     * @return
     */
    List<ShpShop> listShpShopForInitVip();

    /**
     * 获取所有会员过期的店铺;
     *
     * @param expireTime
     * @return
     */
    List<ShpShop> listVipExpireShop(String expireTime);

    /**
     * 条件查询付费会员店铺列表
     *
     * @param paramMemShop
     * @return
     */
    List<VoMemShop> queryMemShopList(ParamMemShop paramMemShop);

    /**
     * 后台付费会员店铺单个查询
     *
     * @param shopId
     * @return
     */
    VoMemShopById getForAdminVoMemShopById(Integer shopId);

    /**
     * 店铺会员更新
     *
     * @param fkShpShopId
     * @param payMonth
     * @return
     */
    ShpShop updateShpShopMember(Integer fkShpShopId, Integer payMonth);


    /**
     * 获取我邀请的人的店铺信息
     *
     * @param userIdArray
     * @return
     */
    List<VoInviteShop> listInviteShop(Object[] userIdArray, String time);

    /**
     * 统计我邀请的人的店铺数量
     *
     * @param userIdArray
     * @return
     */
    List<Map<String, Object>> countInviteShop(Object[] userIdArray, String time);

    /**
     * 当前用户是否已经注册了同名的店铺
     *
     * @param userId
     * @param shopName
     * @return
     */
    boolean existsShopName(int userId, String shopName);

    /**
     * 当前用户是否已经注册了同名的店铺,除了当前登录的店铺名称之后<br/>
     * 用户修改店铺信息时,防止修改成当下用户的其它同名店铺
     *
     * @param shopId
     * @param userId
     * @param shopName
     * @return
     */
    boolean existsShopNameExceptOwn(int shopId, int userId, String shopName);

    /**
     * 延长体验会员时长
     *
     * @param shopId
     * @param newTryEndTime 新的体验结束时间
     */
    void extendTryEndTime(int shopId, Date newTryEndTime);

    /**
     * 变更经营者
     *
     * @param shopId
     * @param oldUsername 旧经营者帐号
     * @param newUsername 新经营者帐号
     */
    void changeShopkeeper(int shopId, String oldUsername, String newUsername);

    /**
     * 变更经营者
     *
     * @param shopId
     * @param oldUsername 旧经营者帐号
     * @param newUsername 新经营者帐号
     */
    void changeShopkeeperNew(int shopId, String oldUsername, String newUsername) throws Exception;


    /**
     * 解除与当前店铺的关联关系<br/>
     * 1.从shp_user_shop_ref表修改该员工的状态; 标记为已删除;
     * 2.删除用户对应该店铺的权限;
     * 3添加店铺操作日志
     *
     * @param shopId
     * @param userId
     * @return 该员工在店铺的昵称
     */
    String removeShop(int shopId, int userId);

    /**
     * 获取当前用户所在店铺数量
     * @param userId
     * @return
     */
    Integer getShopCount(int userId);

    /**
     * 统计属于自己店铺的总数(自己是经营者)
     *
     * @param userId
     * @return
     */
    int countOwnShopCount(int userId);

    /**
     * 根据店铺编号查询店铺信息
     * @param param
     * @return
     */
    VoShopInfo getShpShopInfoByNumber(ParamGetShopInfoByNumber param);
}
