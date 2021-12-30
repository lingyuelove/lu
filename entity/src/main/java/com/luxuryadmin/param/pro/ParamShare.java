package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamShare
 * @Author: ZhangSai
 * Date: 2021/6/30 15:28
 */
@ApiModel(description = "分享历史搜索类")
@Data
public class ParamShare extends ParamToken {

    @ApiModelProperty(value = "分页查询，每页多少条数据：不传默认为20;", name = "pageSize", required = false)
    @Min(value = 10, message = "分页不能小于10")
    @Max(value = 100, message = "条数显示最大为100条")
    private int pageSize = 20;

    @ApiModelProperty(value = "店铺id", name = "shopId", hidden = true)
    private int shopId;

}
