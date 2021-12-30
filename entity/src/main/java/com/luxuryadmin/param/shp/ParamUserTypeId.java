package com.luxuryadmin.param.shp;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

/**
 * 更新店铺信息--前端参数模型
 *
 * @author monkey king
 * @Date 2020-05-31 23:28:04
 */
@ApiModel(description = "更新店铺信息--前端参数模型")
public class ParamUserTypeId extends ParamToken {

    /**
     * 身份id
     */
    @ApiModelProperty(name = "userTypeId", required = true)
    @Pattern(regexp = "^\\d{5,}$", message = "userTypeId--参数错误")
    private String userTypeId;

    public String getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(String userTypeId) {
        this.userTypeId = userTypeId;
    }
}
