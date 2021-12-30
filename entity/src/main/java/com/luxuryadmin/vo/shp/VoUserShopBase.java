package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.enums.shp.EnumShpUserType;

/**
 * 个人信息
 *
 * @author monkey king
 * @date 2019-12-13 14:45:34
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoUserShopBase {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 店铺id
     */
    private Integer shopId;

    /**
     * 用户编号;会员号(即邀请码)
     */
    private Integer userNumber;

    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * 店铺创建者;
     */
    private String shopkeeper;

    /**
     * 店铺创建者手机号
     */
    private String shopPhone;

    /**
     * 店铺省份
     */
    private String shopProvince;

    /**
     * 店铺城市
     */
    private String shopCity;

    /**
     * 店铺地址
     */
    private String shopAddress;

    /**
     * 店铺编号
     */
    private String shopNumber;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 用户名
     */
    private String username;

    /**
     * user_shop_ref表中的用户对应店铺的用户名
     */
    private String userShopName;

    /**
     * 封面照片地址
     */
    private String coverImgUrl;

    /**
     * 用户头像地址
     */
    private String userHeadImgUrl;

    /**
     * 店铺头像地址
     */
    private String shopHeadImgUrl;


    /**
     * -2:超级管理员(店长)；-1：管理员；0：普通人员；1：访客；
     */
    private String type;


    /**
     * 认证状态: 0:未认证; 1:已认证
     */
    private String validateState;

    /**
     * 是否展示卡片
     */
    private Boolean showCard;

    /**
     * 是否是会员 yes|是会员 no|不是会员
     */
    private String isMember;

    /**
     * 微信小程序封面图片地址
     */
    private String miniProgramCoverImgUrl;

    /**
     * 该用户邀请人的昵称
     */
    private String inviteCode;

    /**
     * boss: 0:非经营者; 1:经营者
     */
    private String boss;

    /**
     * 是否有【查看友商信息】权限
     */
    private Boolean leaguerInfoPerm;

    /**
     * 会员状态: 0:非会员(会员已过期); 1:体验会员;2:正式会员;3:靓号会员
     */
    private String memberState;

    /**
     * 绑定微信登录; 0:未绑定 | 1:已绑定
     */
    private String bindWeChat;

    /**
     * 绑定苹果登录; 0:未绑定 | 1:已绑定
     */
    private String bindApple;

    public String getBindApple() {
        return bindApple;
    }

    public void setBindApple(String bindApple) {
        this.bindApple = bindApple;
    }

    public String getBindWeChat() {
        return bindWeChat;
    }

    public void setBindWeChat(String bindWeChat) {
        this.bindWeChat = bindWeChat;
    }

    public String getMemberState() {
        return memberState;
    }

    public void setMemberState(String memberState) {
        this.memberState = memberState;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(Integer userNumber) {
        this.userNumber = userNumber;
    }

    public String getShopNumber() {
        return shopNumber;
    }

    public void setShopNumber(String shopNumber) {
        this.shopNumber = shopNumber;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return decodePhone(phone);
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return decodePhone(username);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return EnumShpUserType.getShpUserTypeName(type);
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShopkeeper() {
        return shopkeeper;
    }

    public void setShopkeeper(String shopkeeper) {
        this.shopkeeper = shopkeeper;
    }

    public String getShopPhone() {
        return decodePhone(shopPhone);
    }

    public void setShopPhone(String shopPhone) {
        this.shopPhone = shopPhone;
    }

    public String getShopProvince() {
        return shopProvince;
    }

    public void setShopProvince(String shopProvince) {
        this.shopProvince = shopProvince;
    }

    public String getShopCity() {
        return shopCity;
    }

    public void setShopCity(String shopCity) {
        this.shopCity = shopCity;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public String getValidateState() {
        return validateState;
    }

    public void setValidateState(String validateState) {
        this.validateState = validateState;
    }

    private String decodePhone(String encodePhone) {
        //符合解密的长度条件
        boolean isTrue = !LocalUtils.isEmptyAndNull(encodePhone) && encodePhone.length() >= 16;
        return isTrue ? DESEncrypt.decodeUsername(encodePhone) : encodePhone;
    }

    public String getUserHeadImgUrl() {
        return userHeadImgUrl;
    }

    public void setUserHeadImgUrl(String userHeadImgUrl) {
        this.userHeadImgUrl = userHeadImgUrl;
    }

    public String getShopHeadImgUrl() {
        return shopHeadImgUrl;
    }

    public void setShopHeadImgUrl(String shopHeadImgUrl) {
        this.shopHeadImgUrl = shopHeadImgUrl;
    }

    public String getUserShopName() {
        return userShopName;
    }

    public void setUserShopName(String userShopName) {
        this.userShopName = userShopName;
    }

    public Boolean getShowCard() {
        return showCard;
    }

    public void setShowCard(Boolean showCard) {
        this.showCard = showCard;
    }

    public String getIsMember() {
        return isMember;
    }

    public void setIsMember(String isMember) {
        this.isMember = isMember;
    }

    public String getMiniProgramCoverImgUrl() {
        return miniProgramCoverImgUrl;
    }

    public void setMiniProgramCoverImgUrl(String miniProgramCoverImgUrl) {
        this.miniProgramCoverImgUrl = miniProgramCoverImgUrl;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getBoss() {
        return boss;
    }

    public void setBoss(String boss) {
        this.boss = boss;
    }

    public Boolean getLeaguerInfoPerm() {
        return leaguerInfoPerm;
    }

    public void setLeaguerInfoPerm(Boolean leaguerInfoPerm) {
        this.leaguerInfoPerm = leaguerInfoPerm;
    }
}
