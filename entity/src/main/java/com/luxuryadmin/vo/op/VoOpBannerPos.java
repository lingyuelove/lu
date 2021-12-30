package com.luxuryadmin.vo.op;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Banner位置VO类
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoOpBannerPos {

    /**
     * 位置编号
     */
    @ApiModelProperty(name = "posCode", required = false, value = "位置编号")
    private String posCode;

    /**
     * 位置展示名称
     */
    @ApiModelProperty(name = "posShowName", required = false, value = "位置展示名称")
    private String posShowName;

}

