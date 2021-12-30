package com.luxuryadmin.param.op;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel(description = "Banner查询条件对象")
public class ParamOpBannerQuery {

    @ApiModelProperty(value = "分页查询，页码：不传默认为1;", name = "pageNum", required = false)
    @Min(value = 1, message = "当前页不能小于1")
    @Max(value = 999, message = "当前页最大为999")
    private int pageNum = 1;

    @ApiModelProperty(value = "分页查询，每页多少条数据：不传默认为10;", name = "pageSize", required = false)
    private int pageSize = 10;

    @ApiModelProperty(value = "Banner标题", name = "title", required = false)
    private String title;

    /**
     * banner位置 indexCarousel|首页轮播 indexPopupWindow|首页弹窗 myCarousel|我的页面轮播
     */
    @ApiModelProperty(name = "pos", required = false, value = "banner位置")
    private String pos;

    /**
     * 发布渠道 ios|苹果 android|安卓 all|所有平台
     */
    @ApiModelProperty(name = "publishPlatform", required = false, value = "发布渠道 ios|苹果 android|安卓 all|所有平台",
            allowableValues="all,ios,android")
    private String publishPlatform;

    /**
     * Banner状态 on|启用 off|停用
     */
    @ApiModelProperty(name = "state", required = false, value = "Banner状态 on|启用 off|停用",allowableValues="on,off")
    private String state;

    /**
     * 插入时间 起始
     */
    @ApiModelProperty(value = "查询条件中-创建时间范围-开始", name = "insertTimeStart", required = false)
    private Date insertTimeStart;

    /**
     * 插入时间 结束
     */
    @ApiModelProperty(value = "查询条件中-创建时间范围-结束", name = "insertTimeEnd", required = false)
    private Date insertTimeEnd;

}
