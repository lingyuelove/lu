package com.luxuryadmin.vo.fin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 日账单集合列表
 *
 * @author zhangSai
 * @date 2021/04/23 14:19:52
 */
@Data
@ApiModel(value="日账单集合列表", description="日账单集合列表")
public class VoBillDayPageByApp {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id", name = "billDayId")
    private Integer billDayId;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id", name = "shopId")
    private Integer shopId;

    /**
     * 账单id
     */
    @ApiModelProperty(value = "账单id", name = "billId")
    private Integer billId;

    /**
     * 销售利润
     */
    @ApiModelProperty(value = "销售利润", name = "profitMoney")
    private BigDecimal profitMoney;

    /**
     * 薪资支出
     */
    @ApiModelProperty(value = "薪资支出", name = "salaryMoney")
    private BigDecimal salaryMoney;

    /**
     * 其他收支
     */
    @ApiModelProperty(value = "其他收支", name = "otherMoney")
    private BigDecimal otherMoney;

    /**
     * 维修收支
     */
    @ApiModelProperty(value = "服务利润（维修收支）", name = "serviceMoney")
    private BigDecimal serviceMoney;

    /**
     * 统计时间(业务,统计前一天)
     */

    @ApiModelProperty(value = "统计时间", name = "bizTime")
    private String bizTime;


}
