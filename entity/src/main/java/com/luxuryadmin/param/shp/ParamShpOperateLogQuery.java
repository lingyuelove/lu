package com.luxuryadmin.param.shp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @Classname ParamShpServiceQuery
 * @Description 店铺服务列表查询参数
 * @Date 2020/9/18 18:11
 * @Created by sanjin145
 */
@Data
public class ParamShpOperateLogQuery {

    /**
     * token;登录标识符
     */
    @ApiModelProperty(name = "token", required = true, value = "token;登录标识符")
    private String token;

    /**
     * 当前页
     */
    @ApiModelProperty(name = "pageNum", required = true, value = "当前页;默认为1")
    @Min(value = 1, message = "当前页不能小于1")
    @Max(value = 999, message = "当前页最大为999")
    private int pageNum;

    /**
     * 店铺ID
     */
    @ApiModelProperty(name = "fkShpShopId", required = false, value = "店铺ID")
    private Integer fkShpShopId;

    /**
     * 操作用户ID
     */
    @ApiModelProperty(name = "operateUserId", required = false, value = "操作用户ID")
    private Integer operateUserId;

    /**
     * 操作模块
     */
    @ApiModelProperty(name = "module", required = false, value = "操作模块")
    private String module;

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

    @ApiModelProperty(name = "bizId", required = false, value = "商品id")
    private String bizId;
}
