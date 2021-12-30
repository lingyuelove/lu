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
 * <p>
 * 员工权限2.0
 *
 * @author monkey king
 * @date 2021-12-02 02:32:06
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "权限实体--前端接受参数模型")
@Data
public class ParamShpPermIndexUpdate extends ParamToken {


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
     * 敏感权限:  0:否; 1:是
     */
    @ApiModelProperty(value = " 敏感权限:  0:否; 1:是", name = "isPrivate", required = true)
    @Pattern(regexp = "^[01]$", message = "[isPrivate]格式错误!")
    @NotBlank(message = "[isPrivate]不允许为空")
    private String isPrivate;


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
     * ios线上版本号;权限版本号小于等于端上版本号都显示,向下兼容
     */
    @ApiModelProperty(value = "ios线上版本号;权限版本号小于等于端上版本号都显示,向下兼容", name = "iosVersion")
    @Pattern(regexp = "^[0-9]+$", message = "[iosVersion]格式错误")
    @NotBlank(message = "ios版本号不允许为空")
    private String iosVersion;


    /**
     * android线上版本号;权限版本号小于等于端上版本号都显示,向下兼容
     */
    @ApiModelProperty(value = "android线上版本号;权限版本号小于等于端上版本号都显示,向下兼容", name = "androidVersion")
    @Pattern(regexp = "^[0-9]+$", message = "[androidVersion]格式错误")
    @NotBlank(message = "android版本号不允许为空")
    private String androidVersion;

    /**
     * 指定店铺id可见;多个用逗号隔开;
     */
    @ApiModelProperty(value = "指定店铺id可见;多个用逗号隔开;", name = "onlyShopId")
    private String onlyShopId;

    /**
     * 颜色编码; 16进制; FFFFFF
     */
    @ApiModelProperty(value = "颜色编码; 16进制; FFFFFF", name = "color")
    private String color;



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


    /**
     * 描述
     */
    @ApiModelProperty(value = "描述", name = "remark")
    @Length(max = 250, message = "备注必须≤250个字符")
    private String remark;

}
