package com.luxuryadmin.param.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.luxuryadmin.common.aop.check.DateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 注销店铺
 * @author Administrator
 */
@ApiModel(description = "注销店铺")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParamDestroyShop {

    /**
     * 登录标识符
     */
    @ApiModelProperty(value = "登录标识符", name = "token", required = true)
    private String token;

    /**
     * 短信验证码
     */
    @ApiModelProperty(value = "短信验证码", name = "smsCode", required = true)
    private String smsCode;

}
