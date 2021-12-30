package com.luxuryadmin.param.pro;


import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author taoqimin
 * @date 2021-11-05
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "新增动态")
@Data
public class ParamDynamicSave extends ParamToken {

    @ApiModelProperty(name = "name", required = true, value = "名称")
    @NotBlank(message = "名称不能为空")
    private String name;

    @ApiModelProperty(hidden = true)
    private Integer shopId;

    @ApiModelProperty(hidden = true)
    private Integer userId;
}
