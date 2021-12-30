package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author:     Mong
 * @Date:    2021/5/27 15:06
 * @Description: 商品二级分类视图类
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(value="商品二级分类", description="商品二级分类")
public class VoProClassifySub {

    /**
     * 二级分类id
     */
    @ApiModelProperty(value = "二级分类id", name = "id")
    private Integer id;

    /**
     * 二级分类首字母拼接
     */
    @ApiModelProperty(value = "二级分类首字母拼接", name = "code")
    private String code;

    /**
     * 二级分类名称
     */
    @ApiModelProperty(value = "二级分类名称", name = "name")
    private String name;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述", name = "description")
    private String description;

    /**
     * 图标地址
     */
    @ApiModelProperty(value = "图标地址", name = "iconUrl")
    private String iconUrl;

    /**
     * 列表显示名称
     */
    @ApiModelProperty(value = "列表显示名称", name = "showName")
    private String showName;


    /**
     * 二级分类名称首字母
     */
    @ApiModelProperty(value = "二级分类名称首字母", name = "letter")
    private String letter;

    @ApiModelProperty(value = "一级分类code", name = "classifyCode")
    private String classifyCode;

    /**
     * 类型;0:系统自带;1:用户自建
     */
    @ApiModelProperty(value = "类型;0:系统自带;1:用户自建")
    private String type;
}
