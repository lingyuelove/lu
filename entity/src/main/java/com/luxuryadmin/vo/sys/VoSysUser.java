package com.luxuryadmin.vo.sys;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

/**
 * 系统管理-角色管理-角色
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VoSysUser {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码;
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 手机号;对称加密存储
     */
    private String phone;

    /**
     * -2:超级管理员；-1：管理员；0：普通人员；1：访客；
     */
    private Integer type;

    /**
     * 状态  0：禁用   1：正常
     */
    private String state;

    /**
     * 员工编号
     */
    private Integer number;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 添加用户_管理员id
     */
    private Integer insertAdmin;

    /**
     * 修改用户_管理员id
     */
    private Integer updateAdmin;

    /**
     * 版本号;用于更新时对比操作;
     */
    private Integer versions;

    /**
     * 是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件
     */
    private String del;

    /**
     * 备注
     */
    private String remark;

    /**
     * 权限列表
     */
    private List<String> perms;

    @ApiModelProperty(value = "账号类型Id", name = "roleId")
    private String roleId;
}
