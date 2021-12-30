package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoShareSearchList
 * @Author: ZhangSai
 * Date: 2021/6/30 20:09
 */
@Data
@ApiModel(value = "小程序访客list", description = "小程序访客list")
public class VoShareSearchList {

    @ApiModelProperty(value = "商品名称", name = "showName")
    private String name;

    @ApiModelProperty(value = "商品图片", name = "smallImg")
    private String smallImg;

    @ApiModelProperty(value = "临时仓名称", name = "tempName")
    private String tempName;
}
