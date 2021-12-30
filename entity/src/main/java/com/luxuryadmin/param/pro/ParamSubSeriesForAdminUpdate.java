package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamSubSeriesForAdminUpdate
 * @Author: ZhangSai
 * Date: 2021/8/17 18:12
 */
@ApiModel(description = "品牌系列更新类")
@Data
public class ParamSubSeriesForAdminUpdate extends ParamSubSeriesForAdminAdd{
    /**
     * 临时仓商品的id
     */
    @ApiModelProperty(name = "id", required = true,
            value = "id")
    @NotBlank(message = "id不允许为空")
    private String id;
}
