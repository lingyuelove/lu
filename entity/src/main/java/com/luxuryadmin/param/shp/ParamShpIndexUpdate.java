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
public class ParamShpIndexUpdate extends ParamToken {

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
     * 权限id
     */
    @ApiModelProperty(value = "权限id", name = "permId")
    @Pattern(regexp = "^[0-9]+$", message = "权限id格式错误")
    private String permId;

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
    @ApiModelProperty(value = "名称", name = "name")
    @NotBlank(message = "名称不允许为空")
    private String name;


    /**
     * 排序
     */
    @ApiModelProperty(value = "排序(如果用户不填,默认0)", name = "sort")
    @Pattern(regexp = "^[0-9]+$", message = "排序参数格式错误")
    private String sort;


}
