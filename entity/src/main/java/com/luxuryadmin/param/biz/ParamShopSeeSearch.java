package com.luxuryadmin.param.biz;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @PackgeName: com.luxuryadmin.param.biz
 * @ClassName: ParamShopSeeSearch
 * @Author: ZhangSai
 * Date: 2021/5/28 14:48
 */
@Data
@ApiModel(description = "友商店铺查看次数搜索类")
public class ParamShopSeeSearch {
    @ApiModelProperty(value = "店铺id", name = "shopId")
    private Integer shopId;

    @ApiModelProperty(value = "用户id", name = "userId")
    private Integer userId;

    @ApiModelProperty(value = "被查看的店铺id", name = "beSeenShopId")
    private Integer beSeenShopId;

    @ApiModelProperty(value = "创建时间")
    private Date insertTime;
}
