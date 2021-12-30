package com.luxuryadmin.vo.pro;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * 锁单商品中的统计信息
 *
 * @author monkey king
 * @date 2020-06-02 15:32:36
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VoProLockTotal {

    /**
     * 锁单总数
     */
    private String countLockNum;

    /**
     * 显示文本
     */
    private String countLockNumTxt;

    /**
     * 锁单总预付定金
     */
    private String countPreMoney;

    /**
     * 锁单总预计成交
     */
    private String countPreFinishMoney;

    /**
     * 显示文本
     */
    private String countPreMoneyTxt;

    /**
     * 锁单商品的总成本
     */
    private String countInitPrice;
    /**
     * 显示文本
     */
    private String countInitPriceTxt;

    /**
     * 显示文本
     */
    private String countPreFinishMoneyTxt;

}
