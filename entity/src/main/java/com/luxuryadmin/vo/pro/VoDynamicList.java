package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="动态列表", description="动态列表")
public class VoDynamicList {


    @ApiModelProperty(value = "主键Id", name = "id;")
    private Integer id;

    @ApiModelProperty(value = "名称", name = "name")
    private String name;

    @ApiModelProperty(value = "名称", name = "name")
    private String url;

    @ApiModelProperty(value = "总数", name = "num")
    private String totalNum;
}
