package com.luxuryadmin.vo.sys;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoSysContactUs {

    /**
     * 关键词
     */
    @ApiModelProperty(name = "keyWord", required = false,
            value = "关键词")
    private String keyWord;

    /**
     * 标题
     */
    @ApiModelProperty(name = "title", required = false,
            value = "标题")
    private String title;

    /**
     * 显示内容
     */
    @ApiModelProperty(name = "showContent", required = false,
            value = "显示内容")
    private String showContent;

    /**
     * 显示内容
     */
    @ApiModelProperty(name = "isCanCopy", required = false,
            value = "是否可以复制")
    private Boolean isCanCopy;

    /**
     * 显示内容
     */
    @ApiModelProperty(name = "isJumpH5Url", required = false,
            value = "是否跳转H5链接")
    private Boolean isJumpH5Url;

}
