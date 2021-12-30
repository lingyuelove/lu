package com.luxuryadmin.param.biz;

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
@ApiModel(description = "商家联盟新增")
public class ParamShopUnionAdd {
    @ApiModelProperty(value = "店铺id 必传", name = "shopId", required = true)
    @Pattern(regexp = "^[0-9]{5,9}$", message = "[shopId]参数非法!")
    private String shopNumber;

    @ApiModelProperty(value = "添加类型 10 买家 20 卖家", name = "type", required = true)
    private String type;
    @ApiModelProperty(value = "当前用户id", name = "userId", hidden = true)
    private Integer userId;
}
