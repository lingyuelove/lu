package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamClassifyTypeSunAdd
 * @Author: ZhangSai
 * Date: 2021/8/6 11:37
 */
@Data
@ApiModel(value="新增补充信息子模块实体参数", description="新增补充信息子模块实体参数")
public class ParamClassifyTypeSunAdd{
    @ApiModelProperty(name = "name", required = false,value = "分类名称;限长50个汉字")
    private String name;
    @ApiModelProperty(name = "id", required = false,value = "主键id")
    private Integer id;
}
