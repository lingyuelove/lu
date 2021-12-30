package com.luxuryadmin.param.shp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 员工角色--前端参数模型
 *
 * @author monkey king
 * @date 2020-06-12 20:49:44
 */
@ApiModel(description = "员工角色--前端参数模型")
@Data
public class ParamEmployeePerm {

    /**
     * 登录标识符
     */
    @ApiModelProperty(name = "token", required = true, value = "登录标识符")
    private String token;

    /**
     * 用户id
     */
    @ApiModelProperty(name = "userId", required = true, value = "用户id")
    @Pattern(regexp = "^\\d{5,}$", message = "userId--参数错误")
    private String userId;

    /**
     * 用户类型id
     */
    @ApiModelProperty(name = "refId", required = false, value = "用户类型id")
    @Pattern(regexp = "^(-9)|\\d{5,}$", message = "userTypeId--参数错误")
    private String userTypeId;

    /**
     * 权限id
     */
    @ApiModelProperty(name = "permIds", required = false, value = "权限id")
    @Pattern(regexp = "^[0-9;]+$", message = "permIds--参数错误")
    private String permIds;

    /**
     * 对于店铺所显示的姓名
     */
    @ApiModelProperty(name = "name", required = true, value = "对于店铺所显示的姓名")
    @Length(max = 8, message = "姓名不得超过8个字符")
    @NotBlank(message = "姓名不允许为空")
    private String name;

}
