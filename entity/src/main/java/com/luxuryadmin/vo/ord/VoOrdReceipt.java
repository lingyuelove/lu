package com.luxuryadmin.vo.ord;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author monkey king
 * @date 2020-01-20 23:07:50
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoOrdReceipt {

    /**
     * 电子凭证ID
     */
    private Integer id;

    /**
     * 凭证编码
     */
    private String orderBizId;

    /**
     * 甲方(店名/公司名)
     */
    private String partyA;

    /**
     * 商品名称
     */
    private String proName;

    /**
     * 商品独立编码
     */
    private String uniqueCode;

    /**
     * 销售数量
     */
    private Integer totalNum;

    /**
     * 销售总价(分)
     */
    private String totalPrice;
    /**
     * 开票时间
     */
    private String insertTime;

    /**
     * 店铺地址
     */
    private String address;

    /**
     * 乙方(客户姓名/对方公司名)
     */
    private String partyB;

    /**
     * 店铺联系电话
     */
    private String phone;

    /**
     * 售后保障
     */
    @ApiModelProperty(value = "售后保障", name = "afterSaleGuarantee", required = false)
    private String afterSaleGuarantee;

    /**
     * 票据状态: -10: 取消打印; 10:未打印; 20:已打印(线上); 21:(线下)  22:补打印;
     */
    private String state;

    /**
     * 合并后的收货地址
     */
    private String receiveAddress;

    /**
     * 合并后的收货地址
     */
    @ApiModelProperty(value = "物流信息状态 3没有物流信息 0：未发货，1：已发货顺丰 2已发货非顺丰", name = "showDeliverState")
    private String showDeliverState;
}
