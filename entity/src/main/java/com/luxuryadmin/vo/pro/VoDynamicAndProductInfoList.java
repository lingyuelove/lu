package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoDynamicAndProductInfoList {


    private Integer proId;

    private Integer dynamicId;

    private String name;

}
