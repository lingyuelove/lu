package com.luxuryadmin.param.invite;

import com.luxuryadmin.param.common.ParamBasic;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class ParamUserInviteList extends ParamBasic {

    @ApiModelProperty(value = "邀请用户类型，10：全部，20：vip，30：体验，40：已过期", required = true, name = "inviteType")
    @NotNull(message = "inviteType不能为空")
    private Integer inviteType;

    @ApiModelProperty(hidden = true)
    private Integer userId;
}
