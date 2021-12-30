package com.luxuryadmin.vo.sys;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author monkey king
 * @date 2021-08-03 20:48:31
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoSysJobWx {

    /**
     * SysJobWx表的id
     */
    @ApiModelProperty(name = "sysJobWxId", value = "SysJobWx表的id")
    private String sysJobWxId;

    /**
     * 绑定状态 -1:已解绑 | 0:未绑定 |  1:已绑定
     */
    @ApiModelProperty(name = "wxState", value = "绑定状态 -1:已解绑 | 0:未绑定 |  1:已绑定")
    private String wxState;

    /**
     * 微信帐号
     */
    @ApiModelProperty(name = "wxAccount", value = "微信帐号")
    private String wxAccount;

    /**
     * 微信昵称
     */
    @ApiModelProperty(name = "wxNickname", value = "微信昵称")
    private String wxNickname;

    /**
     * 微信绑定手机号
     */
    @ApiModelProperty(name = "wxPhone", value = "微信绑定手机号")
    private String wxPhone;

    private String insertTime;

    private String updateTime;

    private String insertAdmin;

    private String updateAdmin;

    /**
     * 系统帐号关联的id
     */
    @ApiModelProperty(name = "sysUserId", value = "系统帐号关联的id")
    private String sysUserId;

    /**
     * 系统帐号
     */
    @ApiModelProperty(name = "username", value = "系统帐号")
    private String username;

    /**
     * 系统帐号的昵称
     */
    @ApiModelProperty(name = "sysNickname", value = "系统帐号的昵称")
    private String sysNickname;

    private String remark;
}
