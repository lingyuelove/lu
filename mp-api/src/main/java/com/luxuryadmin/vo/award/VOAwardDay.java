package com.luxuryadmin.vo.award;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class VOAwardDay {

    @ApiModelProperty(value = "数据体")
    private List<VOAwardRecordList> voAwardRecordLists;

    @ApiModelProperty(value = "奖励总天数")
    private Integer totalAddDay;
}
