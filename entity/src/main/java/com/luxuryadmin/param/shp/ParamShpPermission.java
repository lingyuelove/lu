package com.luxuryadmin.param.shp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**权限实体--前端接受参数模型
 * @author monkey king
 * @date 2019-12-30 16:42:25
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "权限实体--前端接受参数模型")
@Data
public class ParamShpPermission extends ParamShpPermissionUpdate {

    /**
     * 主键Id,逻辑id,软件内部关联
     */
    @ApiModelProperty(hidden = true)
    private String id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", name = "name", required = true)
    @NotBlank(message = "名称不允许为空")
    private String name;

}
