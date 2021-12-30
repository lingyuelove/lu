package com.luxuryadmin.param.shp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 身份参数模型
 *
 * @author monkey king
 * @Date 2020-09-04 17:46:07
 */
@ApiModel(description = "身份参数模型")
public class ParamShpUserPermUpdate {


    /**
     * 登录标识符
     */
    @ApiModelProperty(name = "token", required = true, value = "登录标识符")
    private String token;

    /**
     * 身份id
     */
    @ApiModelProperty(name = "userTypeId", required = true, value = "身份id,更新时,请赋值")
    @Pattern(regexp = "^\\d{5,}$", message = "userTypeId--参数错误")
    private String userTypeId;

    /**
     * 权限id,多个用分号隔开;只需要传最后一层菜单的id
     */
    @ApiModelProperty(name = "permIds", required = false, value = "权限id,多个用分号隔开;只需要传最后一层菜单的id")
    private String permIds;

    public String getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(String userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPermIds() {
        return permIds;
    }

    public void setPermIds(String permIds) {
        this.permIds = permIds;
    }
}
