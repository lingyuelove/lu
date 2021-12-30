package com.luxuryadmin.param.common;

/**
 * @author monkey king
 * @date 2020-07-29 17:20:40
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 上传网络路径
 *
 * @author monkey king
 * @Date 2020-07-29 17:21:03
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "上传网络路径")
@Data
public class UploadUrlParam extends ParamToken {

    /**
     * token
     */
    @ApiModelProperty(value = "uploadString", name = "uploadString", required = true)
    @Length(min = 30, max = 50, message = "[uploadString]格式错误")
    @NotBlank(message = "uploadString不允许为空")
    private String uploadString;

}
