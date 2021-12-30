package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 店铺用户登录时的VO
 *
 * @author monkey king
 * @date 2019-12-04 17:13:46
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VoShpUserSalt {

    /**
     * 用户id
     */
    private Integer userId;


    /**
     * 店铺id
     */
    private Integer shopId;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 店铺编号
     */
    private String shopNumber;

    /**
     * 会员号
     */
    private Integer userNumber;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码;
     */
    private String password;

    /**
     * -2:超级管理员(店长)；-1：管理员；0：普通人员；1：访客；
     */
    private String type;

    /**
     * 状态  0：禁用   1：正常
     */
    private String state;

    /**
     * 盐;长度不超过64位
     */
    private String salt;

    /**
     * 默认登录;当有多个店铺时,是否默认登录上次店铺;0:不默认登录; 1:默认登录
     */
    private String defaultLogin;
}
