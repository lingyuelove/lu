package com.luxuryadmin.vo.op;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.vo.op
 * @ClassName: VoOpBannerForUnion
 * @Author: ZhangSai
 * Date: 2021/7/21 16:22
 */
@Data
@ApiModel(value="banner --店铺联盟", description="banner --店铺联盟")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoOpBannerForUnion {
    /**
     * banner标题
     */
    @ApiModelProperty(name = "title", value = "banner标题")
    private String title;

    /**
     * banner图片地址
     */
    @ApiModelProperty(name = "imgUrl", value = "banner图片地址")
    private String imgUrl;

    /**
     * 跳转类型 noJump|不跳转 h5|跳转到h5页面 native|跳转到原生页面
     */
    @ApiModelProperty(name = "jumpType", value = "跳转类型 noJump|不跳转 h5|跳转到h5页面 " +
            "native|跳转到原生页面 externalPage|外部APP shopUnion|商家联盟",
            allowableValues="noJump,h5,native,externalPage,shopUnion")
    private String jumpType;

    /**
     * 跳转H5页面URL
     */
    @ApiModelProperty(name = "jumpH5Url", value = "跳转H5页面URL")
    private String jumpH5Url;

    /**
     * 跳转原生地址
     */
    @ApiModelProperty(name = "jumpNativePage", value = "跳转原生地址")
    private String jumpNativePage;

    private String appId;
}
