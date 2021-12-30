package com.luxuryadmin.param.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Administrator
 * @Classname ParamShpUser
 * @Description TODO
 * @Date 2021-05-02 08:14:42
 * @Created by Administrator
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "变更经营者")
@Data
public class ParamChangeShopkeeper extends ParamToken {



    /**
     * 新经营者用户名
     */
    @ApiModelProperty(value = "新经营者用户名", name = "newUsername", required = true)
    @NotBlank(message = "[newUsername]参数错误")
    private String newUsername;

}

