package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamConveyUpdate
 * @Author: ZhangSai
 * Date: 2021/11/24 10:47
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "商品传送页面提取参数")
@Data
public class ParamConveyUpdate extends ParamToken {
    @ApiModelProperty(value = "编号", name = "number", required = true)
    @Length(max = 8, message = "编号必须≤8个字符")
    private String number;
    @ApiModelProperty(name = "shopId", hidden = true,value = "店铺id")
    private Integer shopId;
    @ApiModelProperty(name = "userId", hidden = true,value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "接收状态（确认接收时使用）  1已确认")
    private String receiveState;
}
