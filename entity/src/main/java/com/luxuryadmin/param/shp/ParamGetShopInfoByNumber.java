package com.luxuryadmin.param.shp;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**

 * @date: 2021-11-04
 * taoqimin
 */
@Data
@ApiModel(description = "根据店铺编号搜索店铺信息")
public class ParamGetShopInfoByNumber extends ParamToken {


    @ApiModelProperty(name = "number")
    @NotBlank(message = "店铺编号不能为空")
    private String number;


}
