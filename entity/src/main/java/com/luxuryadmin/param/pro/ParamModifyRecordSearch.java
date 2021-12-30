package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamModifyRecordSearch
 * @Author: ZhangSai
 * Date: 2021/6/4 14:08
 */
@Data
@ApiModel(value = "商品操作日志搜索", description = "商品操作日志搜索")
public class ParamModifyRecordSearch {
    @ApiModelProperty(value = "分页查询，页码：不传默认为1;", name = "pageNum", required = false)
    @Min(value = 1, message = "当前页不能小于1")
    @Max(value = 999, message = "当前页最大为999")
    private int pageNum = 1;

    /**
     * token;登录标识符
     */
    @ApiModelProperty(name = "token", required = true, value = "token;登录标识符")
    private String token;

    @ApiModelProperty(name = "bizId", required = true, value = "商品id")
    private String bizId;

    @ApiModelProperty(name = "shopId", required = false, value = "店铺id 后端自己获取")
    private Integer shopId;

    /**
     * 操作模块
     */
    @ApiModelProperty(name = "operateTimeStart", required = false, value = "开始查询时间")
    private String operateTimeStart;

    /**
     * 操作模块
     */
    @ApiModelProperty(name = "operateTimeEnd", required = false, value = "开始查询时间")
    private String operateTimeEnd;

    /**
     * 操作模块
     */
    @ApiModelProperty(name = "searchContentKey", required = false, value = "搜索关键字")
    private String searchContentKey;

    /**
     * 操作用户ID
     */
    @ApiModelProperty(name = "operateUserId", required = false, value = "操作用户ID")
    private Integer operateUserId;

    @ApiModelProperty(name = "proId", required = false, value = "商品id勿传")
    private Integer proId;


    /**
     * 当前登录用户id
     */
    @ApiModelProperty(hidden = true)
    private Integer currentUserId;
}
