package com.luxuryadmin.param.ord;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 分享电子凭证--前端接收参数模型
 *
 * @author monkey king
 * @date 2020-09-04 19:44:26
 */
@ApiModel(description = "分享电子凭证")
@Data
public class ParamShareServiceQuery {

    /**
     * 店铺编号
     */
    @ApiModelProperty(name = "shopNumber", required = true, value = "店铺编号")
    @Length(max = 9, message = "shopNumber--参数错误")
    private String shopNumber;

    /**
     * 用户编号
     */
    @ApiModelProperty(name = "userNumber", required = true, value = "用户编号")
    @Length(max = 9, message = "userNumber--参数错误")
    private String userNumber;

    /**
     * 分享批号
     */
    @ApiModelProperty(name = "shareBatch", required = true, value = "分享批号")
    @Length(max = 50, message = "shareBatch--参数错误")
    private String shareBatch;

}
