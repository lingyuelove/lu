package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 此分享不能进行权限验证 不继承token类
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamDeliverShare
 * @Author: ZhangSai
 * Date: 2021/12/2 11:14
 */
@Data
@ApiModel(value="物流详情查询参数", description="物流详情查询参数")
public class ParamDeliverShare {
    @ApiModelProperty(value = "物流单号", name = "logisticsNumber", required = true)
    @NotBlank(message = "物流单号不为空")
    private String logisticsNumber;
    @ApiModelProperty(value = "手机后四位", name = "phone", required = true)
    @NotBlank(message = "手机号不为空")
    private String phone;

    @ApiModelProperty(value = "商品显示id", name = "bizId", required = true)
    @NotBlank(message = "商品参数不为空")
    private String bizId;
}
