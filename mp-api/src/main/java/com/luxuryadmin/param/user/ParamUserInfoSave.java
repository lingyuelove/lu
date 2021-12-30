package com.luxuryadmin.param.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class ParamUserInfoSave {

    /**
     * id
     */
    @ApiModelProperty(value = "userId")
    @NotNull(message = "用户id不能为空")
    private Integer userId;
    /**
     * 微信名
     */
    @ApiModelProperty(value = "微信名")
    private String wxNickname;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", required = true, name = "username")
    @NotBlank(message = "用户名不能为空")
    private String username;
    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickname;
    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String headImgUrl;
    /**
     * open_id
     */
    @ApiModelProperty(value = "", required = true, name = "openId")
    @NotBlank(message = "openId不能为空")
    private String openId;
    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    private String province;
    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private String city;
    /**
     * 区
     */
    @ApiModelProperty(value = "区")
    private String district;
}
