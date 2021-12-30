package com.luxuryadmin.mapper.shp;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.shp.ShpUser;
import com.luxuryadmin.param.shp.ParamShpUser;
import com.luxuryadmin.vo.shp.VoEmployee;
import com.luxuryadmin.vo.shp.VoShpUser;
import com.luxuryadmin.vo.shp.VoShpUserSalt;
import com.luxuryadmin.vo.shp.VoUserShopBase;
import com.luxuryadmin.vo.temp.TempForUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author monkey king
 */
@Mapper
public interface ShpUserMapper extends BaseMapper {

    /**
     * 根据用户名获取用户信息;
     *
     * @param username 用户名
     * @return
     */
    VoShpUserSalt getVOShpUserSaltByUsername(String username);


    /**
     * 根据用户名查找是否存在此用户;
     *
     * @param username 用户名
     * @return
     */
    int existsShpUserByUsername(String username);


    /**
     * 根据用户编号获取用户id;
     *
     * @param number 用户编码
     * @return
     */
    Integer getShpUserIdByNumber(int number);

    /**
     * 根据用户名获取用户id;
     *
     * @param username
     * @return
     */
    Integer getShpUserIdByUsername(String username);

    /**
     * 根据用户id获取用户密码;
     *
     * @param userId
     * @return
     */
    String getShpUserPasswordById(int userId);

    /**
     * 根据userId和shopId查找用户的基本信息
     *
     * @param userId ShpUser.id
     * @param shopId ShpShop.id
     * @return
     */
    VoUserShopBase getShpUserBaseByUserIdAndShopId(@Param("userId") int userId, @Param("shopId") int shopId);

    /**
     * 获取用户的基础信息,用于个人中心展示;
     *
     * @param userId ShpUser.id
     * @return
     */
    VoUserShopBase getShpUserBaseInfoByUserId(int userId);

    /**
     * 根据用户名获取用户昵称,用户编号
     *
     * @param username
     * @return
     */
    VoEmployee geVoEmployeeByUsername(String username);

    /**
     * 查询用户列表
     *
     * @param paramShpUser
     * @return
     */
    List<VoShpUser> listShpUser(ParamShpUser paramShpUser);

    /**
     * 根据条件查询员工列表
     *
     * @param paramShpUser
     * @return
     */
    List<VoShpUser> listShpUserRel(ParamShpUser paramShpUser);

    /**
     * 查询所有有效的用户ID
     *
     * @return
     */
    List<Integer> selectAllValidUserId();

    /**
     * 根据用户名列表查询用户ID
     *
     * @param usernameList
     * @return
     */
    List<Integer> selectUserIdListByUsernameList(List<String> usernameList);

    /**
     * 根据用户id获取用户昵称
     *
     * @param userId
     * @return
     */
    String getNicknameByUserId(int userId);

    /**
     * 根据用户id或者用户名(已解密的用户名)
     *
     * @param userId
     * @return
     */
    String getUsernameByUserId(int userId);

    /**
     * 获取员工的基本信息(用于薪资明细)
     *
     * @param shopId
     * @param userId
     * @return
     */
    VoEmployee getEmployeeForSalary(@Param("shopId") int shopId, @Param("userId") int userId);

    /**
     * 获取所有用户编码和用户id; 用户编码小于100万
     *
     * @return
     */
    List<ShpUser> listUserNumber();

    /**
     * 根据统计日期统计注册人数
     *
     * @param countDate
     * @return
     */
    Integer countRegPersonNumByCountDate(Date countDate);

    /**
     * 根据shopId统计注册人数
     *
     * @param shopId
     * @return
     */
    Integer countRegPersonNumByShopId(Integer shopId);


    /**
     * 根据shopId统计有效人数
     *
     * @param shopId
     * @return
     */
    Integer countValidRegPersonNumByShopId(Integer shopId);

    /**
     * 获取用户信息,用于登录,包含username,state;
     *
     * @param username
     * @return
     */
    VoShpUser getShpUserForLogin(String username);

    /**
     * 根据渠道id,获取该渠道下的所有用户id
     *
     * @param channelId
     * @return
     */
    List<Integer> listUserIdByOpChannelId(Integer channelId);

    List<TempForUser> getTempForUser();

    /**
     * 根据用户手机号获取用户信息
     *
     * @param username
     * @return
     */
    VoShpUser getUserInfoByUsername(@Param("username") String username);

    /**
     * 根据手机号获取用户信息
     *
     * @param phone
     * @return
     */
    ShpUser getUserByPhone(@Param("phone") String phone);

    VoShpUser getUserInfoById(@Param("userId") Integer userId);

    /**
     * 根据手机号判断是否为会员
     *
     * @param username
     * @return
     */
    Integer getCountByUsername(@Param("username") String username);


    /**
     * 根据小程序手机号查询奢当家手机号信息（会员）
     *
     * @param usernames
     * @return
     */
    List<String> listVipPhone(@Param("usernames") List<String> usernames);
}