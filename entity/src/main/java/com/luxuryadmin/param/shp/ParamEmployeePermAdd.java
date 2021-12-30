package com.luxuryadmin.param.shp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 添加员工(授权角色)--前端参数模型
 *
 * @author monkey king
 * @date 2020-06-12 20:49:44
 */
@ApiModel(description = "员工角色--前端参数模型")
@Data
public class ParamEmployeePermAdd extends ParamEmployeePerm {

    /**
     * 帐号(手机号码)
     */
    @ApiModelProperty(value = "帐号(手机号码)", name = "username", required = true)
    @Pattern(regexp = "^[1][3456789][0-9]{9}$", message = "手机号格式错误")
    @NotBlank(message = "帐号不能为空")
    private String username;

    /**
     * 添加类型
     */
    @ApiModelProperty(name = "addType", required = false, value = "添加类型")
    @Pattern(regexp = "^1$", message = "[addType]参数错误")
    private String addType;

    /**
     * 创建薪资0:不创建 | 1:创建
     */
    @ApiModelProperty(name = "createSalary", value = "创建薪资0:不创建 | 1:创建")
    @Pattern(regexp = "[01]$", message = "[createSalary]参数错误")
    private String createSalary;

    /**
     * 用户id
     */
    @ApiModelProperty(hidden = true)
    private String userId;


}
