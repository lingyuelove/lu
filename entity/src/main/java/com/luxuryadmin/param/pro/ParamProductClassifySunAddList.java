package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamProductClassifySunAddList
 * @Author: ZhangSai
 * Date: 2021/8/3 15:04
 */
@Data
@ApiModel(value="新增商品补充信息子分类实体参数", description="新增商品补充信息子分类实体参数")
public class ParamProductClassifySunAddList {

    /**
     * 补充信息类型名称
     */
    @ApiModelProperty(value = "补充信息类型内容名称")
    private Integer id;

    @ApiModelProperty(value = "补充信息类型内容,分号隔开")
    private String content;
}
