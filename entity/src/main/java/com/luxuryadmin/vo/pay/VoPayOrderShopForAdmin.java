package com.luxuryadmin.vo.pay;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(value="账单说明", description="账单说明")
public class VoPayOrderShopForAdmin {
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称", name = "goodsName")
    private String goodsName;


    /**
     * 创建类型; 0:用户自动创建,1:后台管理员手动创建;2活动
     */
    @ApiModelProperty(value = "创建类型; 0:用户自动创建,1:后台管理员手动创建;2活动", name = "createType")
    private String createType;


    /**
     * 备注
     */
    @ApiModelProperty(value = "续费月数", name = "remark")
    private String remark;

    /**
     * 交易结束时间
     */
    @ApiModelProperty(value = "交易结束时间", name = "finishTime")
    private Date finishTime;

}
