package com.luxuryadmin.vo.stat;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoStateProdSellClassify {

    /**
     * 总注册人数
     */
    @ApiModelProperty(name = "name", required = false, value = "分类名称")
    private String name;

    /**
     * 总注册店铺数
     */
    @ApiModelProperty(name = "value", required = false, value = "分类值")
    private String value;

}
