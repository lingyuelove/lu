package com.luxuryadmin.param.shp;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 我邀请的人搜索类
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:59
 */
@Data
@ApiModel(value="我邀请的人搜索类", description="我邀请的人搜索类")
public class ParamCountInvite extends ParamToken {
    @ApiModelProperty(value = "month", name = "月份", required = false)
    private String month;
}
