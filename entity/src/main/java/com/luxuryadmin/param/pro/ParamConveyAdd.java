package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamConveyAdd
 * @Author: ZhangSai
 * Date: 2021/11/22 15:47
 */

@Data
@ApiModel(value="商品传送表新增实体参数", description="商品传送表新增实体参数")
public class ParamConveyAdd extends ParamToken {

    @Length(max = 10, message = "商品传送名称长度不能超过10个字符")
    @ApiModelProperty(value = "商品传送名称", name = "name", required = true)
    private String name;
    @ApiModelProperty(value = "店铺id", name = "shopId", hidden = true)
    private Integer shopId;
    @ApiModelProperty(value = "用户id", name = "userId", hidden = true)
    private Integer userId;
    @ApiModelProperty(value = "新增vid", name = "vid", required = true)
    private String vid;
}
