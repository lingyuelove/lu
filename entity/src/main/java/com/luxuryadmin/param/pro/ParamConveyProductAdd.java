package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamConveyProductUpdate
 * @Author: ZhangSai
 * Date: 2021/11/23 18:06
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "商品页面查询参数实体")
@Data
public class ParamConveyProductAdd  extends ParamToken {
    @ApiModelProperty(name = "proIds", required = true, value = "商品id")
    @Pattern(regexp = "^[0-9,]{5,}$", message = "商品id--参数错误")
    private String proIds;
    @ApiModelProperty(value = "商品传送表的id字段,主键id", required = true)
    @Pattern(regexp = "^[0-9]+$", message = "商品传送id--参数错误")
    private String conveyId;

    @ApiModelProperty(value = "用户id字段,主键id",hidden = true)
    private Integer userId;

    @ApiModelProperty(value = "店铺id字段,主键id",hidden = true)
    private Integer shopId;
    @ApiModelProperty(value = "商品id,主键id",hidden = true)
    private List<String> proIdList;
}
