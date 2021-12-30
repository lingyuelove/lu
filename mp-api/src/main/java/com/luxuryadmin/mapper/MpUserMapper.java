package com.luxuryadmin.mapper;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.MpUser;
import com.luxuryadmin.vo.user.VOAddVipUserList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


/**
 * 小程序用户表 dao
 *
 * @author zhangsai
 * @Date 2021-11-24 13:56:01
 */
@Mapper
public interface MpUserMapper extends BaseMapper<MpUser> {


    /**
     * 根据openid查询用户信息，可能有多个
     *
     * @param openId
     * @return
     */
    MpUser getUserInfoByOpenId(@Param("openId") String openId);

    /**
     * 根据用户名获取用户信息o
     *
     * @param username
     * @return
     */
    MpUser getUserInfoByUsername(@Param("username") String username);

    /**
     * 根据id获取用户信息
     *
     * @param userId
     * @return
     */
    MpUser getUserInfo(@Param("userId") Integer userId);

    /**
     * 修改体验期会员
     *
     * @return
     */
    void updatePastUserInfo(@Param("date") Date date);


    /**
     * 修改正式会员信息
     *
     * @param date
     * @return
     */
    void updateVipUserInfo(@Param("date") Date date);

    List<String> getUserName(@Param("date") Date date);

    List<MpUser> getVipUserName();

    /**
     * 将小程序会员转为奢当家会员
     *
     * @param yesSdjVipPhones
     */
    void updateSdjVipByMpVipUsername(@Param("yesSdjVipPhones") List<String> yesSdjVipPhones);

    /**
     * 将奢当家会员过期
     *
     * @param noSdjVipPhones
     */
    void updateSdjVipPast(@Param("noSdjVipPhones") List<String> noSdjVipPhones);

    /**
     * 获取添加用户信息
     *
     * @return
     */
    List<VOAddVipUserList> listAddVipUser();
}
