package com.luxuryadmin.vo.visitor;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class VOVisitorRecord {


    @ApiModelProperty(value = "访问人次")
    private Integer visitorPersonNum;

    @ApiModelProperty(value = "访问次数")
    private Integer visitorNum;

    private PageInfo objList;
}
