package com.luxuryadmin.param.shp;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 身份参数模型
 *
 * @author monkey king
 * @Date 2020-09-04 17:46:07
 */
@ApiModel(description = "身份参数模型")
@Data
public class ParamShpUserTypeSort extends ParamToken {


    /**
     * 身份名称
     */
    @ApiModelProperty(name = "ids", required = true, value = "排序好后id用英文逗号拼接")
    @NotBlank(message = "[ids]参数错误")
    private String ids;


}
