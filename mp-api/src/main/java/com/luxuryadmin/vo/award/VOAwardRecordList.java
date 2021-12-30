package com.luxuryadmin.vo.award;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class VOAwardRecordList {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "奖励时间")
    private Date insertTime;

    @ApiModelProperty(value = "奖励天数")
    private Integer addDay;

    @ApiModelProperty(value = "描述")
    private String remark;

}
