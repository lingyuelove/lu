package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Classname VoProductAnalyzeDetail
 * @Description TODO
 * @Date 2020/9/25 13:46
 * @Created by sanjin145
 */
@Data
public class VoProductAnalyzeDetail {

    /**
     * 商品类别名称
     */
    @ApiModelProperty(name = "prodTypeName", required = false, value = "商品类别名称")
    private String prodTypeName;

    /**
     * 统计数据-自有商品
     */
    @ApiModelProperty(name = "countDataSelf", required = false, value = "统计数据-自有商品")
    private String countDataSelf;

    /**
     * 统计数据-寄卖商品
     */
    @ApiModelProperty(name = "countDataConsignment", required = false, value = "统计数据-寄卖商品")
    private String countDataConsignment;

    /**
     * 统计数据-其它商品
     */
    @ApiModelProperty(name = "countDataOther", required = false, value = "统计数据-其它商品")
    private String countDataOther;

    /**
     * 统计数据-所有商品，合计
     */
    @ApiModelProperty(name = "countDataAll", required = false, value = "统计数据-所有商品，合计")
    private String countDataAll;

}
