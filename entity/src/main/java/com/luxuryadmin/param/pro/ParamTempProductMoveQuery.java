package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamTempProductMoveQuery
 * @Author: ZhangSai
 * Date: 2021/9/25 11:22
 */
@Data
@ApiModel(description = "临时仓移仓-一般商品列表查询参数实体")
public class ParamTempProductMoveQuery extends ParamToken {
    @ApiModelProperty(value = "临时仓id", name = "proTempId", required = true)
    @NotBlank(message = "临时仓ID不能为空")
    @Pattern(regexp = "^[0-9]+$", message = "临时仓id--参数错误")
    private String proTempId;

    @ApiModelProperty(value = "转入:enter 转出:remove", name = "tempType", required = true)
    @NotBlank(message = "转仓类型不能为空")
    private String tempType;
    @ApiModelProperty(value = "商品名称", name = "proName", required = false)
    private String proName;
    @ApiModelProperty(value = "移出临时仓id", name = "removeTempId", hidden = true)
    private String removeTempId;
    @ApiModelProperty(value = "移入临时仓id", name = "enterTempId", hidden = true)
    private String enterTempId;
    @ApiModelProperty(value = "店铺id", name = "shopId", hidden = true)
    private Integer shopId;

}
