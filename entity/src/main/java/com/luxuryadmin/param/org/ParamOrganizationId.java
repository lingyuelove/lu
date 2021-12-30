package com.luxuryadmin.param.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author monkey king
 * @date 2021-05-14 19:32:22
 */
@Data
@ApiModel(value="机构id", description="机构id")
public class ParamOrganizationId {


    /**
     * 机构id
     */
    @ApiModelProperty(value = "机构id", name = "organizationId", required = true)
    @NotNull(message = "[orgId]参数错误")
    private Integer organizationId;


}
