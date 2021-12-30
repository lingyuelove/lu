package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 过期商品列表
 *
 * @author ZhangSai
 * @date   2021/05/11 10:45:48
 */
@Data
@ApiModel(value="过期商品列表", description="过期商品列表")
public class VoExpiredProductByList extends VoProductLoad {


    /**
     * 商品属性表的code
     */
    @ApiModelProperty(value = "商品属性表的code", name = "attributeCode")
    private String attributeCode;


    @ApiModelProperty(value = "过期天数", name = "productExpiredDay")
    private Integer productExpiredDay;
}
