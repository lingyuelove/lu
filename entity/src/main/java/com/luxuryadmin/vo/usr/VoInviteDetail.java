package com.luxuryadmin.vo.usr;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 邀请人详情
 */
@Data
@ApiModel(description = "邀请人详情VO对象")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoInviteDetail {


    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号", name = "phone", required = false)
    private String phone;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", name = "username", required = false)
    private String username;

    /**
     * 邀请日期
     */
    @ApiModelProperty(value = "邀请日期", name = "inviteDate", required = false)
    private String inviteDate;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像", name = "headImgUrl", required = false)
    private String headImgUrl;

}
