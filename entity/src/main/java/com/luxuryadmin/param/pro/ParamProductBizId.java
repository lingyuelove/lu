package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 需要商品业务逻辑进行操作--前端接收参数模型
 *
 * @author monkey king
 * @date 2019-12-23 13:56:43
 */
@ApiModel(description = "需要商品业务逻辑进行操作--前端接收参数模型")
@Data
public class ParamProductBizId extends ParamToken {

    /**
     * 商品业务逻辑id
     */
    @ApiModelProperty(name = "bizId", required = true, value = "商品业务逻辑id;多个用英文分号隔开")
    @NotBlank(message = "bizId--参数错误")
    private String bizId;
    @ApiModelProperty(name = "type", required = false, value = " leaguer友商店铺 union商家联盟")
    private String type;
    @ApiModelProperty(name = "deleteRemark", required = false, value = "删除备注")
    private String deleteRemark;

    @ApiModelProperty(value = "店铺id", name = "shopId", required = false)
    @Pattern(regexp = "^[0-9,]+$", message = "店铺id--参数错误")
    private String shopId;

    /**
     * pro_check的主键Id
     */
    @ApiModelProperty(value = "盘点Id", name = "checkId")
    private Integer checkId;
}
