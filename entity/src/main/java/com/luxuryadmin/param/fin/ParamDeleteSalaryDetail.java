package com.luxuryadmin.param.fin;


import com.luxuryadmin.common.aop.check.DateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 删除薪资明细
 *
 * @author monkey king
 * @date 2020-09-24 03:58:43
 */
@ApiModel(description = "删除薪资明细")
@Data
public class ParamDeleteSalaryDetail {

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
     * 查询人员ID
     */
    @ApiModelProperty(name = "userId", required = true, value = "查询人员id;")
    @Pattern(regexp = "^[0-9]{5,}$", message = "查询人员--参数错误")
    @NotBlank(message = "用户不允许为空")
    private String userId;

}
