package com.luxuryadmin.param.op;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.servlet.tags.Param;

import javax.validation.constraints.NotNull;

/**
 * @author monkey king
 * @date 2021-09-14 20:04:59
 */
@Data
@ApiModel(description = "Banner管理")
public class ParamOpEmployeeAccount extends ParamToken {

    /**
     * 用户名
     */
    @ApiModelProperty(name = "username", required = true, value = "用户名")
    @NotNull(message = "用户名不允许为空")
    private String username;


    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;



}
