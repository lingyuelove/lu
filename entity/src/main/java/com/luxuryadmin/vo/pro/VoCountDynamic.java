package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoCountDynamic {


    @ApiModelProperty(value = "主键Id", name = "id;")
    private Integer id;

    @ApiModelProperty(value = "动态列表id", name = "fkProDynamicId")
    private Integer fkProDynamicId;

    @ApiModelProperty(value = "总数", name = "num")
    private Integer totalNum;
}
