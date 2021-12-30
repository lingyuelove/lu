package com.luxuryadmin.param.stat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 数据统计-注册数据查询参数
 */
@Data
public class ParamRegDataQuery {

    /**
     * 开始日期
     */
    @ApiModelProperty(name = "regStartTime", required = false, value = "注册开始日期;格式yyyy-MM-dd)")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private String regStartDate;

    /**
     * 结束日期
     */
    @ApiModelProperty(name = "regEndTime", required = false, value = "注册结束日期;格式yyyy-MM-dd)")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private String regEndDate;


}
