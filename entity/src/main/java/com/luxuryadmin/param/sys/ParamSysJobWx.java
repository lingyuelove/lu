package com.luxuryadmin.param.sys;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @PackgeName: com.luxuryadmin.param.biz
 * @ClassName: ParamShopUnionAdd
 * @Author: ZhangSai
 * Date: 2021/7/16 18:38
 */
@Data
@ApiModel(description = "工作微信")
public class ParamSysJobWx extends ParamToken {

    @ApiModelProperty(value = "系统用户id", name = "sysUserId", required = true)
    @Pattern(regexp = "^[0-9]+$", message = "[sysUserId]参数错误")
    private String sysUserId;
    @ApiModelProperty(name = "state", required = true,
            value = "绑定状态 -1:已解绑 | 0:未绑定 |  1:已绑定")
    private String state;

    @ApiModelProperty(value = "表id;修改时需要传此参数", name = "sysJobWxId", required = false)
    private String sysJobWxId;

    @ApiModelProperty(value = "微信帐号", name = "wxAccount", required = false)
    private String wxAccount;

    @ApiModelProperty(value = "微信昵称", name = "nickname", required = false)
    @NotBlank(message = "[微信昵称]不允许为空!")
    private String nickname;

    @ApiModelProperty(value = "微信绑定手机号", name = "phone", required = false)
    private String phone;
    @ApiModelProperty(value = "备注", name = "remark", required = false)
    @Length(max = 250, message = "备注最多250个字符!")
    private String remark;

}
