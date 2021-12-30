package com.luxuryadmin.vo.fin;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Classname VoOrdType
 * @Description 财务流水类型VO类
 * @Date 2020/10/20 16:19
 * @Created by sanjin145
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoFinShopRecordType {

    @ApiModelProperty(value = "流水类型ID", name = "id", required = false)
    private Integer id;

    @ApiModelProperty(value = "流水类型名称", name = "name", required = false)
    private String finRecordTypeName;

}
