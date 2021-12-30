package com.luxuryadmin.param.biz;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @PackgeName: com.luxuryadmin.param.biz
 * @ClassName: ParamLeaguerRecommendBySearch
 * @Author: ZhangSai
 * Date: 2021/5/27 18:36
 */
@Data
@ApiModel(value="推荐友商Page显示搜索类", description="推荐友商Page显示搜索类")
public class ParamLeaguerRecommendBySearch extends ParamToken {


    @ApiModelProperty(value = "分页查询，每页多少条数据：不传默认为20;", name = "pageSize", required = false)
    @Min(value = 10, message = "分页不能小于10")
    @Max(value = 100, message = "条数显示最大为100条")
    private int pageSize = 20;

    @ApiModelProperty(value = "店铺id后台获取当前用户shopId前段不用传;", name = "shopId")
    private Integer shopId;
}
