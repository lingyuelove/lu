package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 显示邀请店铺信息
 *
 * @author monkey king
 * @date 2021-04-02 1:01:47
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "显示邀请店铺信息", description = "显示邀请店铺信息")
public class VoInviteShop {

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", name = "username")
    private String username;

    /**
     * 显示名称
     */
    @ApiModelProperty(value = "用户名", name = "showName")
    private String showName;


    /**
     * 用户头像路径
     */
    @ApiModelProperty(value = "用户头像路径", name = "headImgUrl")
    private String headImgUrl;


    /**
     * 店铺会员状态
     */
    @ApiModelProperty(value = "店铺会员状态", name = "memberState")
    private String memberState;

    /**
     * 显示时间
     */
    @ApiModelProperty(value = "显示时间", name = "showTime")
    private String showTime;


    //=======以下字段,仅查出来做业务逻辑判断,不对外显示===========//

    /**
     * 用户注册时间
     */
    @ApiModelProperty(hidden = true)
    private String userInsertTime;

    /**
     * 店铺注册时间
     */
    @ApiModelProperty(hidden = true)
    private String shopInsertTime;

    /**
     * 店铺付费时间
     */
    @ApiModelProperty(hidden = true)
    private String shopPayStartTime;

    /**
     * 用户头像路径
     */
    @ApiModelProperty(hidden = true)
    private String userHeadImgUrl;


    /**
     * 店铺头像路径
     */
    @ApiModelProperty(hidden = true)
    private String shopHeadImgUrl;

    /**
     * 用户昵称
     */
    @ApiModelProperty(hidden = true)
    private String nickname;

    /**
     * 店铺名称
     */
    @ApiModelProperty(hidden = true)
    private String shopName;


}
