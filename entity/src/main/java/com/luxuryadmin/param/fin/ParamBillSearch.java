package com.luxuryadmin.param.fin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 新增帐单类
 *
 * @author zhangSai
 * @date   2021/04/23 14:19:52
 */
@Data
@ApiModel(value="帐单搜索类", description="帐单搜索类")
public class ParamBillSearch {
    @ApiModelProperty(value = "分页查询，页码：不传默认为1;", name = "pageNum", required = false)
    @Min(value = 1, message = "当前页不能小于1")
    @Max(value = 999, message = "当前页最大为999")
    private int pageNum = 1;

    @ApiModelProperty(value = "分页查询，每页多少条数据：不传默认为20;", name = "pageSize", required = false)
    @Min(value = 10, message = "分页不能小于10")
    @Max(value = 100, message = "条数显示最大为100条")
    private int pageSize = 20;

    @ApiModelProperty(value = "店铺id", name = "shopId", required = false)
    private Integer shopId ;
    @ApiModelProperty(value = "用户id", name = "userId")
    private Integer userId ;
    /**
     * 对账状态 10：进行中 -99 已删除
     */
    @ApiModelProperty(value = "对账状态 10：进行中 -99 已删除;", name = "state", required = true)
    private String state;
}
