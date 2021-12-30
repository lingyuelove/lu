package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VoProPrintTpl {

    /**
     * 主键ID
     */
    @ApiModelProperty(name = "id", required = false, value = "主键ID")
    private Integer id;

    /**
     * 标题
     */
    @ApiModelProperty(name = "title", required = false, value = "标题")
    private String title;

    /**
     * 模板内容
     */
    @ApiModelProperty(name = "content", required = false, value = "模板内容")
    private String content;

    @ApiModelProperty(name = "state", required = false, value = "0普通模板 1快速模板")
    private String state;
}
