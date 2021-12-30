package com.luxuryadmin.param.sys;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

/**
 * 系统-权限菜单
 *
 * @author monkey king
 * @date 2020-05-25 17:11:30
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "系统-权限菜单")
public class ParamSysPermission extends ParamSysPermissionUpdate{
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    @ApiModelProperty(hidden = true)
    private String id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", name = "name", required = true)
    private String name;

    /**
     * 权限编码(页面跳转时的判断值)
     */
    @ApiModelProperty(value = "权限编码(页面跳转时的判断值),跳转页面时,请填写此值", name = "code", required = true)
    @Length(max = 50, message = "权限编码长度≤50个字符")
    private String code;

    /**1
     * 权限相对路径URL
     */
    @ApiModelProperty(value = "权限相对路径URL", name = "url", required = true)
    @Length(max = 250, message = "权限相对路径URL≤250个字符")
    private String url;

    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    @ApiModelProperty(value = "授权(多个用逗号分隔，如：user:list;user:create)", name = "permission", required = true)
    private String permission;



    /**
     * 权限图片地址
     */
    @ApiModelProperty(value = "权限图片地址", name = "iconUrl", required = true)
    private String iconUrl;




    /**
     * 描述
     */
    @ApiModelProperty(value = "描述", name = "remark", required = true)
    @Length(max = 250, message = "备注必须≤250个字符")
    private String remark;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String getIconUrl() {
        return iconUrl;
    }

    @Override
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
