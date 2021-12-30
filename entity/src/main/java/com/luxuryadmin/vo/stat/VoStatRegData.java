package com.luxuryadmin.vo.stat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class VoStatRegData {

    /**
     * 日期范围列表
     */
    @ApiModelProperty(name = "dateRangeList", required = false, value = "日期范围列表")
    private List<String> dateRangeList;

    /**
     * 注册人数列表
     */
    @ApiModelProperty(name = "regPersonNumList", required = false, value = "注册人数列表")
    private List<Integer> regPersonNumList;

    /**
     * 注册店铺列表
     */
    @ApiModelProperty(name = "regShopNumList", required = false, value = "注册店铺列表")
    private List<Integer> regShopNumList;

}
