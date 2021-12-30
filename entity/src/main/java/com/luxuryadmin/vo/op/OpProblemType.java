package com.luxuryadmin.vo.op;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.vo.op
 * @ClassName: OpProblemType
 * @Author: ZhangSai
 * Date: 2021/9/25 20:24
 */
@Data
@ApiModel(value="帮助中心类型", description="帮助中心类型")
public class OpProblemType {
    @ApiModelProperty(name = "name", value = "类型名称")
    private String name;
    @ApiModelProperty(name = "iconImg", value = "类型图标")
    private String iconImg;
}
