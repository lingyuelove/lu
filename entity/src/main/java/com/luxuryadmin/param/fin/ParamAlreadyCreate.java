package com.luxuryadmin.param.fin;


import com.luxuryadmin.common.aop.check.DateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
public class ParamAlreadyCreate {

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


}
