package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author taoqimin
 * @date 2021-11-04
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VoShopInfo {

    @ApiModelProperty(value = "店铺id", name = "id")
    private Integer id;

    @ApiModelProperty(value = "店铺名称", name = "name")
    private String name;

    @ApiModelProperty(value = "手机号", name = "contact")
    private String contact;

    @ApiModelProperty(value = "省", name = "province")
    private String province;

    @ApiModelProperty(value = "市", name = "city")
    private String city;

    @ApiModelProperty(value = "地址", name = "address")
    private String address;
}
