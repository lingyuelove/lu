package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamConveyDelete
 * @Author: ZhangSai
 * Date: 2021/11/22 18:17
 */

@Data
@ApiModel(value="商品传送表删除实体参数", description="商品传送表删除实体参数")
public class ParamConveyDelete extends ParamToken {
    @ApiModelProperty(name = "id", required = true,value = "批量，id集合 1,2,3,4,5,")
    @NotBlank(message = "id不能为空")
    private String id;
    @ApiModelProperty(name = "type", required = true,value = "type刪除方类型 send发送方 receive接收方")
    @NotBlank(message = "type不能为空")
    private String type;
}
