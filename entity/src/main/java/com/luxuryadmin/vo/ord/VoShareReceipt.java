package com.luxuryadmin.vo.ord;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author monkey king
 * @Date 2020/06/10 3:28
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VoShareReceipt {

    /**
     * 店铺id
     */

    private Integer shopId;

    /**
     * 店铺编号
     */
    private String shopNumber;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 是否显示店铺商品;0:不显示; 1:显示
     */
    private String showProduct;

    @ApiModelProperty(name = "showType", required = false, value = "展示类型 all 全部 context:只展示内容 默认all")
    private String showType;

    @ApiModelProperty(name = "showProduct", required = false,
            value = "是否显示分享物流信息;0:不显示; 1:显示")
    private String showDeliver;
}
