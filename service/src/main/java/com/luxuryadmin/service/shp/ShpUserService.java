package com.luxuryadmin.service.shp;

import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.entity.shp.ShpUser;
import com.luxuryadmin.entity.shp.ShpUserDetail;
import com.luxuryadmin.enums.login.EnumSendSmsType;
import com.luxuryadmin.param.login.*;
import com.luxuryadmin.param.shp.ParamShpUser;
import com.luxuryadmin.vo.shp.*;
import com.luxuryadmin.vo.usr.VoInviteList;

import java.util.HashMap;
import java.util.List;

/**
 * 店铺管理员-用户登录,注册,忘记密码业务;
 *
 * @author monkey king
 * @Date 2019/12/01 3:39
 */
public interface ShpUserService {

    /**
     * 获取用户信息,用于登录,包含username,state;
     *
     * @param username
     * @return
     */
    VoShpUser getShpUserForLogin(String username);

    /**
     * 根据用户名查找店铺用户
     *
     * @param username 用户名
     * @return
     */
    VoShpUserSalt getVOShpUserSaltByUsername(String username);

    /**
     * 根据用户编号获取用户id
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
     * 店铺短信登录方法-app登录
     *
     * @param username 用户名
     * @return
     */
    BaseResult shpUsernameLogin(String username);

    /**
     * 店铺短信登录方法--可选择app登录或者后台登录
     *
     * @param username     用户名
     * @param isAdminLogin 是否后台登录; true:后台登录 | false:app登录
     * @return
     */
    BaseResult shpUsernameLogin(String username, boolean isAdminLogin);

    /**
     * 店铺用户帐号密码登录方法--app登录
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    BaseResult shpPwdLogin(String username, String password);

    /**
     * 店铺用户帐号密码登录方法--可选择app登录或者后台登录
     *
     * @param username     用户名
     * @param password     密码
     * @param isAdminLogin 是否后台登录; true:后台登录 | false:app登录
     * @return
     */
    BaseResult shpPwdLogin(String username, String password, boolean isAdminLogin);


    /**
     * 发送短信验证码
     *
     * @param paramImageCode
     * @param sendSmsType
     * @return
     */
    BaseResult sendSmsValidateCode(ParamImageCode paramImageCode, EnumSendSmsType sendSmsType);

    /**
     * 注册店铺用户<br>
     * 1.判断用户名是否存在;<br/>
     * 2.判断短信验证码是否正确;<br/>
     * 3.判断邀请码(如果填邀请码)是否存在;<br/>
     * 4.都通过之后;注册用户;然后清除smsCode在redis中的值;
     *
     * @param username   用户名
     * @param inviteCode 邀请码
     * @param remark     备注
     * @param nickName   昵称
     * @return
     */
    ShpUser registerShpUser(String username, String inviteCode, String remark, String nickName);

    /**
     * 注册店铺用户<br>
     * 1.判断用户名是否存在;<br/>
     * 2.判断短信验证码是否正确;<br/>
     * 3.判断邀请码(如果填邀请码)是否存在;<br/>
     * 4.都通过之后;注册用户;然后清除smsCode在redis中的值;
     *
     * @param username   用户名
     * @param inviteCode 邀请码
     * @return
     */
    BaseResult registerShpUserAndLogin(String username, String inviteCode, String nickName);


    /**
     * 修改ShpUser的密码
     *
     * @param paramNewPwd 设置新密码参数
     * @param sendSmsType 枚举类; register:注册接口设置密码(需要登录) | resetPassword:重置密码(不需要登录)
     * @return
     */
    BaseResult updateShpUserPassword(ParamNewPwd paramNewPwd, EnumSendSmsType sendSmsType);

    /**
     * 修改用户密码
     *
     * @param userId
     * @param newPassword
     */
    void updateShpUserPassword(int userId, String newPassword);


    /**
     * 根据用户名查找是否存在此用户;
     *
     * @param username 用户名
     * @return {@link Boolean} true:存在 || false:不存在
     */
    boolean existsShpUserByUsername(String username);

    /**
     * 添加ShpUser记录并返回该记录的id;
     *
     * @param shpUser 要新增的{@link ShpUser}实体
     * @return {@link ShpUser}.id
     */
    int saveShpUserReturnId(ShpUser shpUser);


    /**
     * 根据userId和shopId查找用户的基本信息
     *
     * @param userId ShpUser.id
     * @return
     */
    VoUserShopBase getShpUserBaseByUserIdAndShopId(int userId);

    /**
     * 封装一个ShpUser用于登录成功时使用
     *
     * @param userId       用户id
     * @param shopId       店铺id
     * @param defaultLogin 默认是否自动登录; 0:不自动登录; 1:自动登录;
     * @return
     */
    ShpUser packShpUserForLogin(int userId, int shopId, String defaultLogin);


    /**
     * 退出登录<br/>
     * 清除redis中的token
     *
     * @param token 用户token
     */
    void exitLogin(String token);


    /**
     * 退出登录<br/>
     * 清除redis中的token
     *
     * @param token        用户token
     * @param isAdminLogin 是否后台登录; true:后台登录 | false:app登录
     */
    void exitLogin(String token, boolean isAdminLogin);

    /**
     * 更新实体
     *
     * @param shpUser ShpUser实体
     */
    void updateShpUser(ShpUser shpUser);


    /**
     * 根据用户名获取用户昵称,用户编号
     *
     * @param username
     * @return
     */
    VoEmployee geVoEmployeeByUsername(String username);

    /**
     * 分页查询用户列表
     *
     * @param paramShpUser
     * @return
     */
    List<VoShpUser> queryShpUserList(ParamShpUser paramShpUser);

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
    ShpUser getObjectById(String id);

    /**
     * 根据id获取用户的身份信息
     *
     * @param id
     * @return
     */
    ShpUserDetail selectDetailById(String id);

    /**
     * 根据id获取用户详情
     *
     * @param id
     * @param shopId
     * @return
     */
    VoShpUserInfo selectInfoById(int shopId, String id);

    /**
     * 根绝条件查询员工列表
     *
     * @param paramShpUser
     * @return
     */
    List<VoShpUser> queryShpUserRelList(ParamShpUser paramShpUser);

    /**
     * 注册用户,并随机生成密码;发送短信告知用户
     *
     * @param shopName
     * @param username
     * @param ip
     * @return
     */
    int registerShpUserWithRandomPwd(String shopName, String username, String ip);

    /**
     * 更新店铺用户【极光】【Registration ID】
     *
     * @param shpUserId
     * @param jiGuangRegId
     * @return
     */
    Boolean updateShpUserJiGuangRegId(Integer shpUserId, String jiGuangRegId) throws Exception;

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
     * 修改有用户密码;
     *
     * @param modifyPwd
     */
    void modifyPassword(ParamModifyPwd modifyPwd);


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
    VoEmployee getEmployeeForSalary(int shopId, int userId);

    /**
     * 分页查询邀请列表
     *
     * @param userId
     * @param pageNo
     * @return
     */
    VoInviteList getInviteList(int userId, Integer pageNo);


    /**
     * 升级用户邀请码的值到百万以上
     *
     * @return
     */
    void updateUserNumberForMillion();

    /**
     * 根据店铺ID获取在职员工数量
     *
     * @param shopId
     * @return
     */
    Integer getValidShpUserNumByShopId(Integer shopId);

    /**
     * 根据渠道id,获取该渠道下的所有用户id
     *
     * @param channelId
     * @return
     */
    List<Integer> listUserIdByOpChannelId(Integer channelId);

    /**
     * 根据用户手机号获取用户信息
     * @param username
     * @return
     */
    VoShpUser getUserInfoByUsername(String username);

    /**
     * 一键登录接口
     * @param paramVerify
     */
    BaseResult getVerifyInfo(ParamVerify paramVerify);

    /**
     * 轮训登录
     * @param param
     * @return
     */
    BaseResult circulationLogin(ParamQRCodeLoginQuery param);

    /**
     * 获取登录二维码数据
     * @return
     */
    String getLoginQRCode();
}
