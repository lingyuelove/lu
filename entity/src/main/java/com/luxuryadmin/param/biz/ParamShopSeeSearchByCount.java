package com.luxuryadmin.param.biz;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @PackgeName: com.luxuryadmin.param.biz
 * @ClassName: ParamShopSeeSearchByCount
 * @Author: ZhangSai
 * Date: 2021/5/31 17:40
 */
@Data
@ApiModel(description = "友商店铺查看次数搜索类")
public class ParamShopSeeSearchByCount {
    @ApiModelProperty(value = "店铺id", name = "shopId")
    private Integer shopId;
    @ApiModelProperty(value = "创建时间开始")
    private Date insertTime;


}
