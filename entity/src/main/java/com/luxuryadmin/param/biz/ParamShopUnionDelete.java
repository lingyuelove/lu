package com.luxuryadmin.param.biz;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * @PackgeName: com.luxuryadmin.param.biz
 * @ClassName: ParamShopUnionDelete
 * @Author: ZhangSai
 * Date: 2021/7/20 16:06
 */
@Data
@ApiModel(description = "商家联盟删除")
public class ParamShopUnionDelete {
    @ApiModelProperty(value = "商家联盟id 必传", name = "id", required = true)
    @Pattern(regexp = "^[0-9]{5,9}$", message = "[id]参数非法!")
    private String id;
    @ApiModelProperty(value = "当前用户id", name = "userId", hidden = true)
    private Integer userId;
    @ApiModelProperty(value = "当前用户id", name = "userId", hidden = true)
    private Integer shopId;
}
