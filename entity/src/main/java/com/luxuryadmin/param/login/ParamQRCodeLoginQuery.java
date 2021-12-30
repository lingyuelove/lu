package com.luxuryadmin.param.login;


import com.luxuryadmin.param.common.ParamBasic;
import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 二维码登录
 *
 * @author taoqimin
 * @date 2021-10-10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "二维码登录")
public class ParamQRCodeLoginQuery extends ParamBasic {


    @ApiModelProperty(value = "二维码识别值", name = "codeKey", required = true)
    @NotBlank(message = "codeKey识别内容不能为空")
    private String codeKey;

    @ApiModelProperty(value = "刷新页面时传此值", name = "unload")
    private String unload;
}
