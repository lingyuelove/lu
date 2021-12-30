package com.luxuryadmin.vo.invite;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VOUserInviteList {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "userId")
    private Integer userId;

    @ApiModelProperty(value = "微信名称")
    private String wxNickname;

    @ApiModelProperty(value = "微信头像")
    private String headImgUrl;

    @ApiModelProperty(value = "0:非会员; 1:体验会员;2:正式会员;3:靓号会员")
    private Integer memberState;

}
