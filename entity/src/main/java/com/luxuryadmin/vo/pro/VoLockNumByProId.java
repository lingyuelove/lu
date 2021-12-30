package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VoLockNumByProId {


    @ApiModelProperty(value = "商品id", name = "proId")
    private Integer proId;

    @ApiModelProperty(value = "锁单数量", name = "lockNum")
    private Integer lockNum;
}
