package com.luxuryadmin.param.shp;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * @author monkey king
 * @date 2021-08-03 18:13:11
 */
@Data
public class ParamUploadShopValid extends ParamToken {

    @ApiModelProperty(value = "店铺id", name = "shopId", required = true)
    @Pattern(regexp = "^[0-9]+$", message = "店铺id--参数错误")
    private String shopId;

    @ApiModelProperty(value = "图片类型", name = "license:营业执照;problem:帮助中心图片;img:其它认证图片;video:视频", required = true)
    @Pattern(regexp = "^(license)|(problem)|(img)|(video)$", message = "[type]参数错误")
    private String type;
}
