package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamShareDelete
 * @Author: ZhangSai
 * Date: 2021/6/30 16:02
 */
@ApiModel(description = "分享删除类")
@Data
public class ParamShareDelete extends ParamToken {
    @ApiModelProperty(value = "主键id", name = "id", required = true)
    private String id;
}
