package com.luxuryadmin.param.sys;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "系统管理-角色管理")
@Data
public class ParamSysRoleAdd{



    /**
     * 主键Id,逻辑id,软件内部关联
     */
    @ApiModelProperty(hidden = false)
    @Pattern(regexp = "^[0-9,]+$", message = "id--参数错误")
    private String id;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称，新增必填", name = "roleName", required = true)
    @NotBlank(message = "角色名称在新增时不允许为空")
    private String roleName;

    /**
     * 是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件
     */
    @ApiModelProperty(hidden = true)
    private String del = "0";

    /**
     * 权限集合 数组
     */
    @ApiModelProperty(value = "权限集合 数组", name = "perms", required = true)
    private List<String> perms;

    private Integer insertAdmin;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", name = "remark", required = false)
    @Length(max = 250, message = "备注必须≤250个字符")
    private String remark;




}
