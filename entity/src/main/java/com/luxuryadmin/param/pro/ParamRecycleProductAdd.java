package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.param.ord
 * @ClassName: ParamOrderAdd
 * @Author: ZhangSai
 * Date: 2021/7/2 11:55
 */
@ApiModel(description = "添加寄卖取回订单--前端接收参数模型")
@Data
public class ParamRecycleProductAdd extends ParamToken {
    /**
     * 商品系统编码
     */
    @ApiModelProperty(value = "商品系统编码", name = "bizId", required = true)
    private String bizId;

    /**
     * 结款备注
     */
    @ApiModelProperty(value = "取回备注", name = "retrieveRemark", required = false)
    private String retrieveRemark;
    @ApiModelProperty(value = "店铺id", name = "shopId", hidden = true)
    private Integer shopId;

    @ApiModelProperty(value = "用户id", name = "userId", hidden = true)
    private Integer userId;
}
