package com.luxuryadmin.entity.fin;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 工资条详情表
 *
 * @author monkey king
 * @date   2020/11/30 19:52:47
 */

@Data
public class FinSalaryDetail {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 店铺ID
     */
    private Integer fkShpShopId;

    /**
     * 用户id
     */
    private Integer fkShpUserId;

    /**
     * 工资条记录表id
     */
    private Integer fkFinSalaryId;

    /**
     * 统计的商品属性类型; 多个用逗号隔开; 10:自有商品; 20:寄卖商品
     */
    private String productAttr;

    /**
     * 销售总额(分)
     */
    private Long saleMoney;

    /**
     * 毛利润总额(分)
     */
    private Long grossProfitMoney;

    /**
     * 回收成本总额(分)
     */
    private Long recycleCostMoney;

    /**
     * TA回收产生的利润(分)
     */
    private Long recycleProfitMoney;

    /**
     * 服务利润总额(分)
     */
    private Long serviceProfitMoney;


    /**
     * 销售结果json字符串
     */
    private String saleResultJson;

    /**
     * 方案类型; 格式: 提成模块-方案,订单类型:值,订单类型2:值2;
     */
    private String schemeType;

    /**
     * 底薪(分)
     */
    private Long basicMoney;

    /**
     * 其它款项(分)
     */
    private Long elseMoney;

    /**
     * 销售总提成(分)
     */
    private Long salePushMoney;

    /**
     * 回收总提成(分)
     */
    private Long recyclePushMoney;

    /**
     * 服务总提成(分)
     */
    private Long servicePushMoney;

    /**
     * 总计(分)
     */
    private Long salaryTotalMoney;

    /**
     * 回收成本百分比(%)
     */
    private BigDecimal recycleInitPricePercent;

    /**
     * 回收单价(分)/每件
     */
    private BigDecimal recycleUnitPrice;

    /**
     * TA回收产生的利润提点(%)
     */
    private BigDecimal recycleProfitPercent;

    /**
     * 服务利润提点(%)
     */
    private BigDecimal serviceProfitPercent;

    /**
     * 工资条统计的开始时间
     */
    private Date salaryStTime;

    /**
     * 工资条统计的结束时间
     */
    private Date salaryEtTime;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 创建人用户id
     */
    private Integer insertAdmin;

    /**
     * 修改人用户id
     */
    private Integer updateAdmin;

    /**
     * 备注
     */
    private String remark;

}