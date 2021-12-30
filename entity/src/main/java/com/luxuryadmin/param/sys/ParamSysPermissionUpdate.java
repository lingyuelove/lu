package com.luxuryadmin.param.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**更新权限实体--前端接受参数模型
 * @author monkey king
 * @date 2019-12-30 16:42:25
 */
@ApiModel(value = "权限实体--前端接受参数模型")
public class ParamSysPermissionUpdate {


    /**
     * 登录标识符
     */
    @ApiModelProperty(value = "登录标识符", name = "token", required = true)
    private String token;

    /**
     * 主键Id,逻辑id,软件内部关联
     */
    @ApiModelProperty(value = "业务逻辑id;更新时,请赋值;", name = "id", required = true)
    @Pattern(regexp = "^[0-9,]+$", message = "id--参数错误")
    private String id;

    /**
     * 父节点id
     */
    @ApiModelProperty(value = "父节点id", name = "parentId", required = true)
    @Pattern(regexp = "^[0-9]+$", message = "父节点id格式错误")
    @NotBlank(message = "父节点id不允许为空")
    private String parentId;

    /**
     * 类型 0：模块   1：页面   2：功能
     */
    @ApiModelProperty(value = "类型 0：模块   1：页面   2：功能", name = "type", required = true)
    @Pattern(regexp = "^[012]$", message = "类型参数格式错误")
    @NotBlank(message = "类型不允许为空")
    private String type;


    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", name = "name", required = false)
    private String name;


    /**
     * 排序
     */
    @ApiModelProperty(value = "排序(如果用户不填,默认0)", name = "sort", required = false)
    @Pattern(regexp = "^[0-9]+$", message = "排序参数格式错误")
    private String sort;

    /**
     * 权限编码(页面跳转时的判断值)
     */
    @ApiModelProperty(value = "权限编码(页面跳转时的判断值),跳转页面时,请填写此值", name = "code", required = false)
    @Length(max = 50, message = "权限编码长度≤50个字符")
    private String code;

    /**1
     * 权限相对路径URL
     */
    @ApiModelProperty(value = "权限相对路径URL", name = "url", required = false)
    @Length(max = 250, message = "权限相对路径URL≤250个字符")
    private String url;

    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    @ApiModelProperty(value = "授权(多个用逗号分隔，如：user:list;user:create)", name = "permission", required = false)
    private String permission;



    /**
     * 权限图片地址
     */
    @ApiModelProperty(value = "权限图片地址", name = "iconUrl", required = false)
    private String iconUrl;




    /**
     * 描述
     */
    @ApiModelProperty(value = "描述", name = "remark", required = false)
    @Length(max = 250, message = "备注必须≤250个字符")
    private String remark;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
