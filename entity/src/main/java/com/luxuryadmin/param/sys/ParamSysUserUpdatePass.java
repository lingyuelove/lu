package com.luxuryadmin.param.sys;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 系统管理-账号管理
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "系统管理-账号管理")
public class ParamSysUserUpdatePass {

    /**
     * 主键Id,逻辑id,软件内部关联
     */
    @ApiModelProperty(value = "用户编号", name = "id", required = true)
    private Integer id;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", name = "username", required = true)
    private String username;

    /**
     * 密码;
     */
    @ApiModelProperty(value = "旧密码", name = "passwordOld", required = true)
    private String passwordOld;

    /**
     * 密码;
     */
    @ApiModelProperty(value = "新密码", name = "passwordNew", required = true)
    private String passwordNew;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordOld() {
        return passwordOld;
    }

    public void setPasswordOld(String passwordOld) {
        this.passwordOld = passwordOld;
    }

    public String getPasswordNew() {
        return passwordNew;
    }

    public void setPasswordNew(String passwordNew) {
        this.passwordNew = passwordNew;
    }
}
