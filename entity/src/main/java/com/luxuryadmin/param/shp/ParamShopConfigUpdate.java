package com.luxuryadmin.param.shp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.param.shp
 * @ClassName: ParamShopConfigUpdate
 * @Author: ZhangSai
 * Date: 2021/7/1 11:25
 */
@ApiModel(description = "店铺配置修改类")
@Data
public class ParamShopConfigUpdate {

    @ApiModelProperty(value = "店铺id")
    private Integer shopId;

    @ApiModelProperty(value = "是否开启小程序访客功能 0未开启 1已开启")
    private String openShareUser;
}
