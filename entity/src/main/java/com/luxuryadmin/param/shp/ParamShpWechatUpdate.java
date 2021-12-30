package com.luxuryadmin.param.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.param.shp
 * @ClassName: ParamShpWechatUpdate
 * @Author: ZhangSai
 * Date: 2021/8/30 18:09
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="友商店铺联系方式的显示", description="友商店铺联系方式的显示")
public class ParamShpWechatUpdate extends ParamShpWechatAdd{
    @ApiModelProperty(name = "id", required = true, value = "主键id;")
    private Integer id;
}
