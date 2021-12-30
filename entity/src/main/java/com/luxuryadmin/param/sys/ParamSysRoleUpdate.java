package com.luxuryadmin.param.sys;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "系统管理-角色管理")
@Data
public class ParamSysRoleUpdate {

    /**
     * 主键Id,逻辑id,软件内部关联
     */
    @ApiModelProperty(value = "角色编号,修改或者删除必填", name = "id", required = true)
    @Pattern(regexp = "^[0-9,]+$", message = "id--参数错误")
    private String id;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称", name = "roleName", required = false)
    private String roleName;

    /**
     * 权限集合 数组
     */
    @ApiModelProperty(value = "权限集合 数组", name = "perms", required = true)
    private List<String> perms;



    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", name = "remark", required = false)
    private String remark;



}
