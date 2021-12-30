package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamShareProductForApplets
 * @Author: ZhangSai
 * Date: 2021/7/9 11:24
 */
@ApiModel(description = "根据分享批次获取分享信息")
@Data
public class ParamShareProductForApplets {
    @ApiModelProperty(value = "分享批次", name = "shareBatch", required = true)
    private String shareBatch;
    @ApiModelProperty(value = "分享类型 0列表 1详情", name = "type", required = true)
    private String type;
}
