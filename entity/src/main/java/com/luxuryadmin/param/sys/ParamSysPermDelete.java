package com.luxuryadmin.param.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @PackgeName: com.luxuryadmin.param.sys
 * @ClassName: ParamSysPermDelete
 * @Author: ZhangSai
 * Date: 2021/10/27 15:29
 */
@ApiModel(value = "删除权限--前端接受参数模型")
@Data
public class ParamSysPermDelete {
    @ApiModelProperty(value = "主键id;删除时,请赋值，多个逗号分隔;", name = "permissionId", required = true)
    @NotBlank(message = "主键id不允许为空")
    private String id;

    @ApiModelProperty(value = "userId;", name = "userId", hidden = true)
    private Integer userId;

}
