package com.luxuryadmin.vo.ord;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 临时仓订单集合显示
 *
 * @author zhangSai
 * @date   2021/04/21 13:56:16
 */
@Data
public class VoOrderForTemp {
    /**
     * 主键Id
     */
    @ApiModelProperty(value = "订单id", name = "id")
    private Integer id;

    /**
     * 订单编号(针对店铺的唯一订单编码)
     */
    @ApiModelProperty(value = "订单编号(针对店铺的唯一订单编码)", name = "number")
    private String number;

    /**
     * 最终成交价(分)
     */
    @ApiModelProperty(value = "最终成交价(分)", name = "finishPrice")
    private BigDecimal finishPrice;

    /**
     * 订单总数量;
     */
    @ApiModelProperty(value = "订单总数量", name = "totalNum")
    private Integer totalNum;

    /**
     * 商品卖出时间
     */
    @ApiModelProperty(value = "商品卖出时间", name = "saleTime")
    private String saleTime;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称", name = "productName")
    private String productName;

    /**
     * 缩略图
     */
    @ApiModelProperty(value = "缩略图", name = "smallImg")
    private String smallImg;

    /**
     * 状态  -90 已删除(不计入统计); -20:已退款; -10:已取消开单;  10：开单中  11: 预定中  20：已售出;
     */
    @ApiModelProperty(value = "订单状态 20:已退款; -10:已取消开单; 10：开单中  11: 预定中  20：已售出;", name = "state")
    private String state;

    /**
     * 商品适用人群
     */
    @ApiModelProperty(value = "商品适用人群", name = "targetUser")
    private String targetUser;

    /**
     * 商品属性;短名称 例：寄
     */
    @ApiModelProperty(value = "商品属性短名称 例：寄", name = "attributeShortCn")
    private String attributeShortCn;

    /**
     * 等同于attributeUs;兼容线上2.4.2;
     */
    @ApiModelProperty(value = "商品类型：自有 其他 质押 寄卖", name = "attributeCodeCn")
    private String attributeCodeCn;

    /**
     * 销售员昵称
     */
    @ApiModelProperty(value = "销售员昵称", name = "saleNickname")
    private String saleNickname;
}
