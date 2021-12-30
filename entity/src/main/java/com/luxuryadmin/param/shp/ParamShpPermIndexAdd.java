package com.luxuryadmin.param.shp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 权限实体--前端接受参数模型
 * 员工权限2.0
 *
 * @author monkey king
 * @date 2021-12-02 02:32:06
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "权限实体--前端接受参数模型")
@Data
public class ParamShpPermIndexAdd extends ParamShpPermIndexUpdate {

    /**
     * 主键Id,逻辑id,软件内部关联
     */
    @ApiModelProperty(hidden = true)
    private String id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", name = "name", required = true)
    @NotBlank(message = "名称不允许为空")
    private String name;

}
