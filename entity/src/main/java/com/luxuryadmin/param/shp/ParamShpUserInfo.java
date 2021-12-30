package com.luxuryadmin.param.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 * @Classname ParamShpUser
 * @Description TODO
 * @Date 2020/6/22 17:25
 * @Created by Administrator
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "店铺管理-员工列表-编辑新建员工")
public class ParamShpUserInfo {

    /**
     * 登录标识符
     */
    @ApiModelProperty(value = "登录标识符", name = "token", required = true)
    private String token;

    /**
     * 主键Id,逻辑id,软件内部关联
     */
    @ApiModelProperty(value = "用户编号", name = "id", required = false)
    @Pattern(regexp = "^[0-9,]+$", message = "id--参数错误")
    private Integer id;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", name = "username", required = false)
    private String username;

    /**
     * 身份
     */
    @ApiModelProperty(value = "身份", name = "type", required = false)
    private String type;

    /**
     * 用户的角色组
     */
    @ApiModelProperty(value = "用户的角色组", name = "roles", required = true)
    private List<String> roles;

    /**
     * 修改人
     */
    private String updateAdmin;


    public String getUpdateAdmin() {
        return updateAdmin;
    }

    public void setUpdateAdmin(String updateAdmin) {
        this.updateAdmin = updateAdmin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
