package com.luxuryadmin.param.pro;


import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @author taoqimin
 * @date 2021-11-05
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "删除动态")
@Data
public class ParamDynamicDelete extends ParamToken {


    @ApiModelProperty(name = "ids", required = true,value = "批量，id集合 1,2,3,4,5,")
    @NotBlank(message = "id不能为空")
    private String ids;

}
