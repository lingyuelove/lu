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
public class ParamShpUserPermAdd extends ParamToken {


    /**
     * 身份名称
     */
    @ApiModelProperty(name = "tplName", required = true, value = "身份名称;2~20个字符长度")
    @NotBlank(message = "身份名称不允许为空")
    @Length(max = 20, message = "身份名称长度在20个字符以内")
    private String tplName;



}
