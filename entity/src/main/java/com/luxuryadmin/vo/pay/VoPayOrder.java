package com.luxuryadmin.vo.pay;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.luxuryadmin.common.utils.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 订单卡片显示
 *
 * @author monkey king
 * @date 2019-12-25 21:13:26
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VoPayOrder {

    /**
     * 逻辑id
     */
    private Integer id;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 实际支付金额
     */
    private String realMoney;

    /**
     * 平台交易订单编号
     */
    private String orderNo;

    /**
     * 第三方生成的订单号
     */
    private String transactionId;

    /**
     * 交易时间
     */
    private String finishTime;

    /**
     * 支付渠道
     */
    private String payChannel;

    /**
     * '-99:删除支付订单;<br/>
     * 10:待支付,11:主动取消支付,12:超时自动取消; <br/>
     * 20:支付失败; 30:退款中,31:退款成功,32:退款失败; 33:已支付;<br/>
     * 40:支付成功<br/>
     */
    private Integer state;

}
