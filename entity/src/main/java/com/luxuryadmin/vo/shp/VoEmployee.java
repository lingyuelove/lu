package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * 员工列表VO层
 *
 * @author monkey king
 * @date 2020-01-02 16:41:39
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VoEmployee {

    /**
     * 用户店铺引用id
     */
    private Integer refId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户编号;会员号(即邀请码)
     */
    private Integer userNumber;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 用户名
     */
    private String username;

    /**
     * 头像地址
     */
    private String headImageUrl;

    /**
     * 入职时间
     */
    private String joinWorkTime;

    /**
     * 用户类型id
     */
    private Integer userTypeId;

    /**
     * 用户拥有的角色
     */
    private String role;

    /**
     * 用户身份--经营者,店长,员工等等.
     */
    private String userTypeName;

    /**
     * 状态  0：禁用   1：正常
     */
    private String state;


}
