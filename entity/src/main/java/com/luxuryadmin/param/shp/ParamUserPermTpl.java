package com.luxuryadmin.param.shp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 系统模板权限--前端接受参数模型
 *
 * @author monkey king
 * @date 2020-09-09 01:01:52
 */
@ApiModel(value = "系统模板权限--前端接受参数模型")
public class ParamUserPermTpl {


    /**
     * 登录标识符
     */
    @ApiModelProperty(value = "登录标识符", name = "token", required = true)
    private String token;

    /**
     * 主键Id,逻辑id,软件内部关联
     */
    @ApiModelProperty(value = "业务逻辑id;更新时,请赋值;", name = "id", required = true)
    @NotBlank(message = "模板名称不允许为空")
    private String tplName;

    /**
     * 权限子节点
     */
    @ApiModelProperty(value = "权限子节点,最后一级权限的id;多个id,用英文分号隔开", name = "permIds", required = true)
    @Pattern(regexp = "^[0-9;]{5,}$", message = "权限子节点格式错误")
    @NotBlank(message = "权限子节点不允许为空")
    private String permIds;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序", name = "sort", required = false)
    @Pattern(regexp = "^[0-9]+$", message = "排序格式错误")
    @NotBlank(message = "排序不允许为空")
    private String sort;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTplName() {
        return tplName;
    }

    public void setTplName(String tplName) {
        this.tplName = tplName;
    }

    public String getPermIds() {
        return permIds;
    }

    public void setPermIds(String permIds) {
        this.permIds = permIds;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
