package com.luxuryadmin.entity.stat;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 运营统计-销售分类表
 *
 * @author sanjin145
 * @date   2020/12/15 16:18:43
 */
@Data
public class StatProdSaleClassify {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 销售类别code
     */
    private String proClassifyCode;

    /**
     * 销售类别名称
     */
    private String proClassifyName;

    /**
     * 总销售量
     */
    private Integer totalSaleNum;

    /**
     * 总销售额
     */
    private BigDecimal totalSaleAmount;

    /**
     * 创建时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;
}