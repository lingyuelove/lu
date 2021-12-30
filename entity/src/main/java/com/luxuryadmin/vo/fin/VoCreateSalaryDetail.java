package com.luxuryadmin.vo.fin;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

/**
 * 薪资详情表的基础参数
 *
 * @author monkey king
 * @date 2020-09-23 23:59:09
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoCreateSalaryDetail {


    /**
     * 10:自有商品; 20:寄卖商品
     */
    private String proAttr;

    /**
     * 底薪(分)
     */
    private String basicMoney;

    /**
     * 其他款项(分)
     */
    private String elseMoney;

    /**
     * 回收成本百分比
     */
    private String recycleInitPricePercent;

    /**
     * 回收单价(分)/每件
     */
    private String recycleUnitPrice;

    /**
     * 回收利润提点
     */
    private String recycleProfitPercent;

    /**
     * 服务利润提点
     */
    private String serviceProfitPercent;

    /**
     * 方案类型; 格式: 提成模块-方案,订单类型:值,订单类型2:值2;
     */
    private String schemeType;

    /**
     * 0:未发放; 1:已发放;
     */
    private String salaryState;

    /**
     * 已发放薪资的结果集
     */
    private String saleResultJson;


}
