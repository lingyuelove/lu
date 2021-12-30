package com.luxuryadmin.param.login;


import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 扫码登录内容
 *
 * @author taoqimin
 * @date 2021-10-10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "扫码登录内容")
public class ParamQRCodeQuery extends ParamToken {


    @ApiModelProperty(value = "二维码识别值", name = "codeKey", required = true)
    @NotBlank(message = "codeKey识别内容不能为空")
    private String codeKey;

}
