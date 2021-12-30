package com.luxuryadmin.param.shp;

import com.luxuryadmin.param.common.ParamBasic;
import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 多店铺选择登录--前端参数模型
 *
 * @author monkey king
 * @Date 2019-12-26 02:15:20
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "多店铺选择登录--前端参数模型")
@Data
public class ParamChooseShop  extends ParamToken {

    /**
     * 店铺ID(选填)
     */
    @ApiModelProperty(name = "shopId", required = false, value = "店铺ID(选填);")
    @Pattern(regexp = "^\\d{4,7}$", message = "shopId--参数错误")
    private String shopId;

    /**
     * 自动登录(选填,不填则默认为0) 0:不自动登录; 1:自动登录;"
     */
    @ApiModelProperty(name = "defaultLogin", required = false,
            value = "自动登录(选填,不填则不更新) 0:不自动登录; 1:自动登录;")
    @Pattern(regexp = "^(0)|(1)$", message = "defaultLogin--参数错误")
    private String defaultLogin;

}
