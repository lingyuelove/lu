package com.luxuryadmin.vo.stat;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 运营数据统计-销售数据
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoStatSaleProdData {

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

}
