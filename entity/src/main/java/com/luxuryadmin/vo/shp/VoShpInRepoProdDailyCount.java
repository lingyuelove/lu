package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Classname VoShpInRepoProdDailyCount
 * @Description 店铺入库商品每日统计VO
 * @Date 2020/9/15 15:20
 * @Created by sanjin145
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoShpInRepoProdDailyCount {

    /**
     * 入库数量
     */
    private Integer inRepoNum;

    /**
     * 入库总额
     */
    private BigDecimal inRepoAmount;

    public VoShpInRepoProdDailyCount() {
        this.inRepoNum = 0;
        this.inRepoAmount = new BigDecimal(0.00);
    }
}
