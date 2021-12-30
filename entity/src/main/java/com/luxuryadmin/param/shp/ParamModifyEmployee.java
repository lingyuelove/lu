package com.luxuryadmin.param.shp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 修改员工--前端参数模型
 *
 * @author monkey king
 * @date 2019-12-26 02:15:20
 */
@ApiModel(description = "修改员工--前端参数模型")
public class ParamModifyEmployee extends ParamEmployee {

    /**
     * 用户店铺引用id
     */
    @ApiModelProperty(name = "refId", required = true, value = "用户店铺引用id")
    @Pattern(regexp = "^\\d{5,}$", message = "refId--参数错误")
    @NotBlank(message = "refId--参数错误")
    private String refId;

    /**
     * 用户id
     */
    @ApiModelProperty(name = "userId", required = true, value = " 用户id")
    @Pattern(regexp = "^\\d{5,}$", message = "userId--参数错误")
    @NotBlank(message = "userId--参数错误")
    private String userId;


    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
