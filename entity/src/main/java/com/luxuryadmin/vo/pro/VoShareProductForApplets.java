package com.luxuryadmin.vo.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamShareProduct
 * @Author: ZhangSai
 * Date: 2021/7/7 14:31
 */
@ApiModel(description = "分享参数类")
@Data
public class VoShareProductForApplets {

    @ApiModelProperty(value = "店铺编号", name = "shopNumber")
    private String shopNumber;

    @ApiModelProperty(value = "用户编号", name = "userNumber")
    private String userNumber;

    @ApiModelProperty(value = "分享批次", name = "shareBatch")
    private String shareBatch;

    @ApiModelProperty(value = "店铺id", name = "shopId")
    private Integer shopId;

    @ApiModelProperty(value = "商品详情id", name = "bizId")
    private String bizId;

    @ApiModelProperty(value = "分享类型 0列表 1详情", name = "type")
    private String type;

}
