package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author monkey king
 * @Date 2020/06/10 3:28
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VoShareProduct {

    /**
     * shopId
     */
    private Integer shopId;

    /**
     * 商品id
     */
    private String proId;

    /**
     * 显示价格;如果为空,则不显示价格;如果不为空,则显示值里面对应的价格
     */
    private String showPrice;

    /**
     * 分享名称
     */
    private String shareName;

    private String classifyCode;
}
