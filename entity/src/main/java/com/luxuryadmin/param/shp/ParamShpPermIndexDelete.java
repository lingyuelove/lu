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
public class ParamShpPermIndexDelete extends ParamToken {


    /**
     * 主键Id,逻辑id,软件内部关联
     */
    @ApiModelProperty(value = "业务逻辑id;更新时,请赋值;", name = "id", required = true)
    @Pattern(regexp = "^[0-9,]+$", message = "id--参数错误")
    private String id;


}
