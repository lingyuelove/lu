package com.luxuryadmin.param.biz;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: tqm
 * Date: 2021-11-04
 */
@ApiModel(description = "审核")
@Data
public class ParamAudit extends ParamToken {


    @ApiModelProperty(value = "id", name = "id")
    @NotNull(message = "id不能为空")
    private Integer id;


    @ApiModelProperty(value = "状态 1已通过 2未通过", name = "state")
    @NotBlank(message = "状态不能为空")
    private String state;

    @ApiModelProperty(value = "失败原因", name = "failRemark")
    private String failRemark;
}
