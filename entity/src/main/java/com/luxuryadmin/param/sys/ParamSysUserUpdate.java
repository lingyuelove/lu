package com.luxuryadmin.param.sys;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 系统管理-账号管理
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "系统管理-账号管理")
@Data
public class ParamSysUserUpdate {



    /**
     * 主键Id,逻辑id,软件内部关联
     */
    @ApiModelProperty(value = "用户id", name = "id", required = true)
    private Integer id;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", name = "username", required = false)
    private String username;

    /**
     * 密码;
     */
    @ApiModelProperty(value = "密码", name = "password", required = false)
    private String password;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称", name = "nickname", required = false)
    private String nickname;


    /**
     * 手机号;对称加密存储
     */
    @ApiModelProperty(value = "手机号", name = "phone", required = false)
    private String phone;

    /**
     * -2:超级管理员；-1：管理员；0：普通人员；1：访客；
     */
    @ApiModelProperty(value = "账号类型:-2:超级管理员；-1：管理员；0：普通人员；1：访客", name = "type", required = false)
    private Integer type;

    @ApiModelProperty(value = "账号类型Id", name = "roleId", required = true)
    @NotBlank(message = "账号类型不为空")
    @Pattern(regexp = "^[0-9,]+$", message = "账号类型Id--参数错误")
    private String roleId;
    /**
     * 状态  0：禁用   1：正常
     */
    @ApiModelProperty(value = "状态 0：禁用 1：正常;创建时默认禁用", name = "state", required = false)
    private String state = "0";

    /**
     * 员工编号
     */
    private Integer number;

    /**
     * 是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件
     */
    private String del = "0";



    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", name = "remark", required = false)
    private String remark;



}
