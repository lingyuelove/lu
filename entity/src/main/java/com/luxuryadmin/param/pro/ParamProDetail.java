package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @author qwy
 * @Classname ParamProduct
 * @Description TODO
 * @Date 2020/6/28 13:59
 * @Created by Administrator
 */
@ApiModel(description = "商品管理-一般商品列表查询参数实体")
@Data
public class ParamProDetail {

    @ApiModelProperty(value = "登录标识符", name = "token", required = true)
    private String token;

    @ApiModelProperty(value = "商品业务id", name = "bizId", required = true)
    @NotBlank(message = "商品不存在--bizId")
    private String bizId ;

    @ApiModelProperty(value = "临时仓商品详情标识(从临时仓过来的商品详情需要带上此参数)", name = "tp", required = false)
    private String tp ;

    @ApiModelProperty(value = "临时仓id", name = "tid", required = false)
    @Pattern(regexp = "^[0-9]+$", message = "临时仓id--参数错误")
    private String tid;

    @ApiModelProperty(value = "寄卖转移id", name = "conveyId", required = false)
    @Pattern(regexp = "^[0-9]+$", message = "寄卖转移id--参数错误")
    private String conveyId;
}
