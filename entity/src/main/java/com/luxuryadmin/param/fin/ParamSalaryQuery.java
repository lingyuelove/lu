package com.luxuryadmin.param.fin;


import com.luxuryadmin.common.aop.check.DateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/**
 * 薪资管理列表--前端接收参数模型
 *
 * @author monkey king
 * @date 2020-09-24 03:58:43
 */
@ApiModel(description = "薪资管理列表")
@Data
public class ParamSalaryQuery {

    /**
     * token;登录标识符
     */
    @ApiModelProperty(name = "token", required = true, value = "token;登录标识符")
    private String token;

    /**
     * 当前页
     */
    @ApiModelProperty(name = "pageNum", required = true, value = "当前页;默认为1")
    @Min(value = 1, message = "当前页不能小于1")
    @Max(value = 999, message = "当前页最大为999")
    private String pageNum;


    /**
     * 查询日期
     */
    @ApiModelProperty(name = "startDate", required = true, value = "查询日期;格式yyyy-MM-dd)")
    @DateTime(message = "查询日期-参数错误" ,format = "yyyy-MM-dd")
    private String startDate;

    /**
     * 查询日期
     */
    @ApiModelProperty(name = "endDate", required = true, value = "查询日期;格式yyyy-MM-dd)")
    @DateTime(message = "查询日期-参数错误" ,format = "yyyy-MM-dd")
    private String endDate;

    /**
     * 查询人员ID
     */
    @ApiModelProperty(name = "userId", required = false, value = "查询人员id;")
    @Pattern(regexp = "^[0-9]{5,}$", message = "查询人员--参数错误")
    private String userId;

    /**
     * shopId;此值在控制层赋值(获取登录者的shopId)
     */
    @ApiModelProperty(hidden = true)
    private Integer shopId;

}
