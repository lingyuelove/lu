package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value="盘点商品更新参数", description="盘点商品更新参数")
public class ParamCheckProductUpdateStateForApi {

    @ApiModelProperty(value = "主键Id", name = "id", required = true)
    @NotNull(message = "id不为空")
    private Integer id;

    /**
     * 盘点类型 缺失:0  存在:1
     */
    @ApiModelProperty(value = "盘点类型 缺失:0  存在:1", name = "checkType", required = true)
    @NotBlank(message = "盘点类型不为空")
    private String checkType;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", name = "remark", required = false)
    private String remark;


    private Integer userId;
}
