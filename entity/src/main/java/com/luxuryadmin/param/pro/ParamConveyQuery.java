package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamConveyQuery
 * @Author: ZhangSai
 * Date: 2021/11/22 18:26
 */

@ApiModel(description = "商品传送页面查询参数实体")
@Data
@EqualsAndHashCode(callSuper = true)
public class ParamConveyQuery extends ParamToken {
    @ApiModelProperty(name = "shopId", hidden = true,value = "店铺id")
    private Integer shopId;

    @ApiModelProperty(name = "type", required = true,value = "type类型  send发送方 receive接收方")
    @NotBlank(message = "type不能为空")
    private String type;
}
