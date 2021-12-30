package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Classname VoShpServiceType
 * @Description 店铺服务类型VO
 * @Date 2020/9/18 16:00
 * @Created by sanjin145
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoShpServiceType {

    /**
     * 类型ID
     */
    @ApiModelProperty(name = "typeId", required = false, value = "类型ID")
    private Integer typeId;

    /**
     * 类型名称
     */
    @ApiModelProperty(name = "typeName", required = false, value = "类型名称")
    private String typeName;
}
