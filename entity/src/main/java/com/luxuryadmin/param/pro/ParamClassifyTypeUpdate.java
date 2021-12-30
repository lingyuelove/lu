package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamClassifyTypeUpdate
 * @Author: ZhangSai
 * Date: 2021/8/6 15:18
 */
@Data
@ApiModel(value="编辑补充信息模块实体参数", description="编辑补充信息模块实体参数")
public class ParamClassifyTypeUpdate extends ParamClassifyTypeAdd{
    @ApiModelProperty(name = "id", required = true,value = "分类名称;限长50个汉字")
    private Integer id;
}
