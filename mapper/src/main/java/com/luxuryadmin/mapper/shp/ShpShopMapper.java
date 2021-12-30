package com.luxuryadmin.mapper.shp;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.shp.ShpShop;
import com.luxuryadmin.param.mem.ParamMemShop;
import com.luxuryadmin.param.shp.ParamGetShopInfoByNumber;
import com.luxuryadmin.param.shp.ParamShpShop;
import com.luxuryadmin.vo.mem.VoMemShop;
import com.luxuryadmin.vo.mem.VoMemShopById;
import com.luxuryadmin.vo.shp.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Mapper
public interface ShpShopMapper extends BaseMapper {

    /**
     * 根据用户id,查找用户的店铺信息;(可能多个)
     *
     * @param userId 用户id
     * @return
     */
    List<VoShopBase> listShpShopBaseInfo(int userId);

    /**
     * 获取用户的店铺基础信息
     *
     * @param shopId
     * @return
     */
    VoUserShopBase getVoUserShopBaseByShopId(@Param("shopId") int shopId);

    /**
     * 根据店铺id获取店铺编号
     *
     * @param shopId
     * @return
     */
    Map<String, Object> getShopNumberAndShopNameByShopId(int shopId);

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
    List<VoShpShop> listShpShop(ParamShpShop paramShpShop);

    /**
     * 获取当前用户所在店铺数量
     *
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
     * 根据经营者的手机号和店铺编号查找店铺id
     *
     * @param phone
     * @param shopNumber
     * @return
     */
    Integer getShopIdByPhoneAndShopNumber(@Param("phone") String phone, @Param("shopNumber") String shopNumber);

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
     * 改变店铺状态<br/>
     * 状态名称：-90：违规禁用(一段时间) -98违规禁用(永久禁用) -99注销账号(帐号已注销)；<br/>
     * 10: 免费使用中(功能受限); <br/>
     * 20: 体验中; 21: 体验已过期; <br/>
     * 30: 付费使用中; 31: 付费已过期; <br/>
     * 40: 续时已过期;<br/>
     *
     * @param shopId
     * @param state
     * @return
     */
    int changeShopState(@Param("shopId") int shopId, @Param("state") String state);

    /**
     * 更新会员标识符
     *
     * @param shopId
     * @param yesOrNo
     * @return
     */
    int updateShopMember(@Param("shopId") int shopId, @Param("yesOrNo") String yesOrNo);

    /**
     * 根据统计日期统计注册店铺数
     *
     * @param countDate
     * @return
     */
    Integer countRegShopNumByCountDate(Date countDate);

    /**
     * 获取所有店铺, 用于初始化会员时间<br/>
     * 只获取id,is_member,insert_time字段
     *
     * @return
     */
    List<ShpShop> listShpShopForInitVip();

    /**
     * 获取所有会员时间过期的会员店铺;
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
    List<VoMemShop> listMemShop(ParamMemShop paramMemShop);

    /**
     * 后台条件查询付费单个会员店铺
     *
     * @param shopId
     * @return
     */
    VoMemShopById getVoMemShopById(@Param("shopId") Integer shopId);

    /**
     * 获取我邀请的人的店铺信息
     *
     * @param userIds
     * @return
     */
    List<VoInviteShop> listInviteShop(@Param("userIds") String userIds, @Param("time") String time);

    /**
     * @param userIds
     * @return
     */
    List<Map<String, Object>> countInviteShop(@Param("userIds") String userIds, @Param("time") String time);

    /**
     * 当前用户是否已经注册了同名的店铺
     *
     * @param userId
     * @param shopName
     * @return
     */
    Integer existsShopName(@Param("userId") int userId, @Param("shopName") String shopName);

    /**
     * 当前用户是否已经注册了同名的店铺,除了当前登录的店铺名称之后<br/>
     * 用户修改店铺信息时,防止修改成当下用户的其它同名店铺
     *
     * @param shopId
     * @param userId
     * @param shopName
     * @return
     */
    Integer existsShopNameExceptOwn(
            @Param("shopId") int shopId,
            @Param("userId") int userId,
            @Param("shopName") String shopName);

    /**
     * 根据店铺编号查询店铺信息
     *
     * @param param
     * @return
     */
    VoShopInfo getShpShopInfoByNumber(ParamGetShopInfoByNumber param);

    /**
     * 获取当天注册店铺的数量
     *
     * @param jobWxId
     * @return
     */
    Integer getRegisterShopCount(Integer jobWxId);

    /**
     * 当日购买会员店铺数
     *
     * @param jobWxId
     * @param type    1当日购买会员店铺数 2:当天月内转化会员量 3:当天百日内转化会员量 4:当天召回会员
     * @return
     */
    Integer getMemShopCount(@Param("jobWxId") Integer jobWxId, @Param("type") Integer type);

    /**
     * 在使用店铺的数量
     *
     * @param jobWxId
     * @param type    1:在使用所有店铺的数量 2:在使用非会员店铺数 3:十日内使用店铺数量 4:十日内不使用店铺数量 5:未逾期店铺 6:逾期店铺 无值：客户总量
     * @return
     */
    Integer getUseShopCount(@Param("jobWxId") Integer jobWxId, @Param("type") Integer type);

    /**
     * 根据id查询会员店铺
     *
     * @param shopId
     * @return
     */
    ShpShop getShopInfo(@Param("shopId") Integer shopId);
}