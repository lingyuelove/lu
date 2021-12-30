package com.luxuryadmin.param.fin;


import com.luxuryadmin.common.aop.check.DateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 薪资提成明细--前端接收参数模型
 *
 * @author monkey king
 * @date 2020-09-24 03:58:48
 */
@ApiModel(description = "薪资提成明细")
@Data
public class ParamSalaryDetailQuery {

    /**
     * token;登录标识符
     */
    @ApiModelProperty(name = "token", required = true, value = "token;登录标识符")
    private String token;

    /**
     * 查询日期
     */
    @ApiModelProperty(name = "startDate", required = true, value = "查询日期;格式yyyy-MM-dd)")
    @DateTime(message = "查询日期-参数错误", format = "yyyy-MM-dd")
    private String startDate;

    /**
     * 查询日期
     */
    @ApiModelProperty(name = "endDate", required = true, value = "查询日期;格式yyyy-MM-dd)")
    @DateTime(message = "查询日期-参数错误", format = "yyyy-MM-dd")
    private String endDate;

    /**
     * 需要做薪资的用户id
     */
    @ApiModelProperty(name = "userId", required = false, value = "用户id;")
    @Pattern(regexp = "^[0-9]{5,}$", message = "员工--参数错误")
    @NotBlank(message = "员工不能为空")
    private String userId;

    /**
     * 商品属性
     */
    @ApiModelProperty(name = "proAttr", required = true,
            value = "商品属性;10:自由商品; 20:寄卖商品; 多个用英文逗号隔开")
    @Pattern(regexp = "^(10)|(20)|(10,20)$", message = "商品属性--参数错误")
    @NotBlank(message = "商品属性不能为空")
    private String proAttr;

    /**
     * shopId;此值在控制层赋值(获取登录者的shopId)
     */
    @ApiModelProperty(hidden = true)
    private Integer shopId;

    /**
     * 当前登录用户id
     */
    @ApiModelProperty(name = "localUserId", hidden = true)
    private Integer localUserId;
}
