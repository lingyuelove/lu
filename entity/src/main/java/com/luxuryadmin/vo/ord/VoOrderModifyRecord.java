package com.luxuryadmin.vo.ord;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Classname VoOrderModifyRecord
 * @Description 订单修改记录VO
 * @Date 2020/9/22 18:31
 * @Created by sanjin145
 */
@ApiModel(description = "订单修改记录--前端接收参数模型")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoOrderModifyRecord {

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题", name = "title", required = false)
    private String title;

    /**
     * 成交价格（修改前）
     */
    @ApiModelProperty(value = "成交价格（修改前）", name = "finishPriceBefore", required = false)
    private String finishPriceBefore;

    /**
     * 成交价格（修改后）
     */
    @ApiModelProperty(value = "成交价格（修改后）", name = "finishPriceAfter", required = false)
    private String finishPriceAfter;

    /**
     * 订单类型（修改前）
     */
    @ApiModelProperty(value = "订单类型（修改前）", name = "typeBefore", required = false)
    private String typeBefore;

    /**
     * 订单类型（修改后）
     */
    @ApiModelProperty(value = "订单类型（修改后）", name = "typeAfter", required = false)
    private String typeAfter;

    /**
     * 销售人员名称(修改前)
     */
    @ApiModelProperty(value = "销售人员名称(修改前)", name = "saleUserNameBefore", required = false)
    private String saleUserNameBefore;

    /**
     * 销售人员名称(修改后)
     */
    @ApiModelProperty(value = "销售人员名称(修改后)", name = "saleUserNameAfter", required = false)
    private String saleUserNameAfter;

    @ApiModelProperty(value = "更新时间", name = "updateTime", required = false)
    private Date updateTime;

    @ApiModelProperty(value = "更新人名称", name = "updateUserName", required = false)
    private String updateUserName;

}
