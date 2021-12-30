package com.luxuryadmin.vo.usr;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoUserInviteData {
    /**
     * 邀请H5链接地址
     */
    @ApiModelProperty(name = "h5Url", required = false, value = "邀请H5链接地址",example="邀请H5链接地址")
    private String h5Url;

    /**
     * 用户邀请码
     */
    @ApiModelProperty(name = "inviteNum", required = false, value = "用户邀请码",example="用户邀请码")
    private String inviteNum;
}
