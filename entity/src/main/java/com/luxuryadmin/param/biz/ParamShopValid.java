package com.luxuryadmin.param.biz;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * @PackgeName: com.luxuryadmin.param.biz
 * @ClassName: ParamShopUnionAdd
 * @Author: ZhangSai
 * Date: 2021/7/16 18:38
 */
@Data
@ApiModel(description = "商家联盟认证信息")
public class ParamShopValid extends ParamToken {

    @ApiModelProperty(value = "营业执照路径", name = "licenseImgUrl", required = false)
    private String licenseImgUrl;

    @ApiModelProperty(value = "图片地址, 多个用分号隔开;", name = "shopId", required = false)
    private String validImgUrl;

    @ApiModelProperty(value = "视频地址, 多个用分号隔开;", name = "userId", required = false)
    private String validVideoUrl;

    @ApiModelProperty(value = "店铺id", name = "shopId", required = true)
    @Pattern(regexp = "^[0-9]+$", message = "店铺id--参数错误")
    private String shopId;
}
