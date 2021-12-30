package com.luxuryadmin.param.biz;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * @PackgeName: com.luxuryadmin.param.biz
 * @ClassName: ParamShopSeeAdd
 * @Author: ZhangSai
 * Date: 2021/5/28 14:04
 */
@Data
@ApiModel(description = "友商店铺查看新增次数")
public class ParamShopSeeAdd {
    /**
     * shopId
     */
    @ApiModelProperty(value = "店铺id后端获取 前端不传", name = "shopId", required = true)
    @Pattern(regexp = "^[0-9]{5,9}$", message = "[shopId]参数非法!")
    private Integer shopId;

    @ApiModelProperty(value = "用户id后端获取 前端不传", name = "userId", required = true)
    @Pattern(regexp = "^[0-9]{5,9}$", message = "[userId]参数非法!")
    private Integer userId;

    @ApiModelProperty(value = "被查看的店铺id", name = "beSeenShopId", required = true)
    @Pattern(regexp = "^[0-9]{5,9}$", message = "[beSeenShopId]参数非法!")
    private Integer beSeenShopId;
}
