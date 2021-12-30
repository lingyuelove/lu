package com.luxuryadmin.param.shp;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 更新权限实体--前端接受参数模型
 *
 * @author monkey king
 * @date 2019-12-30 16:42:25
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "权限实体--前端接受参数模型")
@Data
public class ParamShpPermissionUpdate extends ParamToken {


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
     * 是否在app首页的全部功能里显示:  0:不显示; 1:显示
     */
    @ApiModelProperty(value = "是否在app首页的全部功能里显示:  0:不显示; 1:显示", name = "display", required = true)
    @Pattern(regexp = "^[01]$", message = "是否显示参数格式错误")
    @NotBlank(message = "是否在app首页的全部功能里显示不允许为空")
    private String display;


    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", name = "name")
    private String name;


    /**
     * 排序
     */
    @ApiModelProperty(value = "排序(如果用户不填,默认0)", name = "sort")
    @Pattern(regexp = "^[0-9]+$", message = "排序参数格式错误")
    private String sort;

    /**
     * 权限编码(页面跳转时的判断值)
     */
    @ApiModelProperty(value = "权限编码(页面跳转时的判断值),跳转页面时,请填写此值", name = "code")
    @Length(max = 50, message = "权限编码长度≤50个字符")
    private String code;

    /**
     * 1
     * 权限相对路径URL
     */
    @ApiModelProperty(value = "权限相对路径URL", name = "url")
    @Length(max = 250, message = "权限相对路径URL≤250个字符")
    private String url;

    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    @ApiModelProperty(value = "授权(多个用逗号分隔，如：user:list;user:create)", name = "permission")
    private String permission;


    /**
     * 权限图片地址
     */
    @ApiModelProperty(value = "权限图片地址", name = "iconUrl")
    private String iconUrl;


    /**
     * 描述
     */
    @ApiModelProperty(value = "描述", name = "remark")
    @Length(max = 250, message = "备注必须≤250个字符")
    private String remark;


    /**
     * 版本
     */
    @ApiModelProperty(value = "版本", name = "versions", required = true)
    @Pattern(regexp = "^[0-9]+$", message = "版本号格式错误")
    @NotBlank(message = "版本号不允许为空")
    private String versions;

    /**
     * 新模块标识符
     */
    @ApiModelProperty(value = "新模块标识符", name = "newState")
    private String newState;


    /**
     * 付费模块模块标识符
     */
    @ApiModelProperty(value = "新模块标识符", name = "costState")
    private String costState;


    /**
     * h5链接
     */
    @ApiModelProperty(value = "h5链接", name = "httpUrl")
    private String httpUrl;

}
