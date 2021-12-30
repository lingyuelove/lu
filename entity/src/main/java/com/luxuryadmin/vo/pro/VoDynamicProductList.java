package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoDynamicProductList extends VoProductLoad{


    @ApiModelProperty(value = "用户名", name = "userName")
    private String userName;

    @ApiModelProperty(value = "状态：10：正常，20已售罄，30已锁单，40已删除", name = "state")
    private String state;

    @ApiModelProperty(value = "状态描述", name = "stateDescribe")
    private String stateDescribe;

    @ApiModelProperty(value = "独立编码", name = "uniqueCode")
    private String uniqueCode;
}
