package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品打印模板添加-参数模型
 */
@Data
public class ParamProPrintTplAdd {
    /**
     * 标题
     */
    @ApiModelProperty(name = "id", required = false, value = "主键ID")
    private Integer id;

    /**
     * 标题
     */
    @ApiModelProperty(name = "title", required = true, value = "标题")
    private String title;

    /**
     * 模板内容
     */
    @ApiModelProperty(name = "content", required = true, value = "模板内容")
    private String content;

    @ApiModelProperty(name = "state", required = false, value = "0普通模板 1快速模板")
    private String state;
}
