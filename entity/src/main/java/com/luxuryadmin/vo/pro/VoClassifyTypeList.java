package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoClassifyTypeList
 * @Author: ZhangSai
 * Date: 2021/8/3 11:22
 */
@Data
@ApiModel(value="补充信息分类显示一级实体参数", description="补充信息分类显示实体参数")
public class VoClassifyTypeList {
    @ApiModelProperty(value = "主键Id,逻辑id,软件内部关联")
    private Integer id;
    /**
     * 分类名称;限长50个汉字
     */
    @ApiModelProperty(value = "分类名称;限长50个汉字")
    private String name;

    @ApiModelProperty(value = "序号排序")
    private Integer sort;

    @ApiModelProperty(value = "上级分类code")
    private String classifyCode;

    @ApiModelProperty(value = "上级分类名称;限长50个汉字")
    private String classifyName;

    @ApiModelProperty(value = "上级id")
    private Integer pId;


}
