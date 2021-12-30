package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value="盘点更新参数", description="盘点更新参数")
public class ParamCheckUpdateForApi {

    /**
     * 主键Id
     */
    @ApiModelProperty(value = "主键Id", name = "id", required = true)
    @NotNull(message = "id不为空")
    private Integer id;

    /**
     * 盘点状态 10:进行中 | 20:取消 | 30:完成
     */
    @ApiModelProperty(value = "盘点状态 10:进行中 | 20:取消 | 30:完成", name = "checkState", required = true)
    @NotBlank(message = "盘点状态不为空")
    private String checkState;
}
