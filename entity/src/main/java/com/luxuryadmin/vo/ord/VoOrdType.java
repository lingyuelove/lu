package com.luxuryadmin.vo.ord;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Classname VoOrdType
 * @Description TODO
 * @Date 2020/9/24 15:37
 * @Created by sanjin145
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoOrdType {

    @ApiModelProperty(value = "订单类型ID", name = "id", required = false)
    private Integer id;

    @ApiModelProperty(value = "订单类型名称", name = "name", required = false)
    private String name;

    @ApiModelProperty(value = "订单类型排序", name = "sort", required = false)
    private Integer sort;

}
