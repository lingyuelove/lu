package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoProductForDelete
 * @Author: ZhangSai
 * Date: 2021/7/1 13:36
 */
@Data
@ApiModel(value="删除列表类", description="机构仓app端显示大类")
public class VoProductOrOrderForDelete {
    /**
     * 订单ID
     */
    @ApiModelProperty(name = "orderId", value = "订单id")
    private Integer orderId;

    @ApiModelProperty(name = "orderNumber", value = "订单编号")
    private String orderNumber;
    /**
     * 商品唯一标识符,业务id,此id对外开放
     */
    @ApiModelProperty(name = "bizId", value = "商品唯一标识符,业务id")
    private String bizId;

    /**
     * 成本价(分)
     */
    @ApiModelProperty(name = "initPrice", value = "成本价(分)")
    private BigDecimal initPrice;

    /**
     * 友商价(分)
     */
    @ApiModelProperty(name = "tradePrice", value = "友商价(分)")
    private BigDecimal tradePrice;

    /**
     * 代理价(分)
     */
    @ApiModelProperty(name = "agencyPrice", value = "代理价(分)")
    private BigDecimal agencyPrice;

    /**
     * 零售价(分)
     */
    @ApiModelProperty(name = "salePrice", value = "零售价(分)")
    private BigDecimal salePrice;

    /**
     * 最终成交价(分)
     */
    @ApiModelProperty(name = "finishPrice", value = "最终成交价(分)")
    private BigDecimal finishPrice;

    /**
     * 缩略图地址
     */
    @ApiModelProperty(name = "smallImg", value = "缩略图地址")
    private String smallImg;

    /**
     * 名称
     */
    @ApiModelProperty(name = "name", value = "商品名称")
    private String name;

    /**
     * 删除时间
     */
    @ApiModelProperty(name = "deleteTime", value = "删除时间")
    private String deleteTime;

    @ApiModelProperty(name = "deleteAdminName", value = "删除人名字")
    private String deleteAdminName;
    @ApiModelProperty(name = "deleteAdmin", value = "删除人",hidden = true)
    private Integer deleteAdmin;
    /**
     * 订单状态;中文;
     */
    @ApiModelProperty(name = "state", value = "订单状态")
    private String state;

    /**
     * 订单状态;中文;
     */
    @ApiModelProperty(name = "stateCn", value = "订单状态;中文")
    private String stateCn;

    /**
     * 结款状态  -1:未填写| 0:未结款 | 1:已结款
     */
    @ApiModelProperty(name = "entrustState", value = "结款状态")
    private String entrustState;

    /**
     * 结款状态  -1:未填写| 0:未结款 | 1:已结款
     */
    @ApiModelProperty(name = "entrustStateName", value = "结款状态名称")
    private String entrustStateName;
    /**
     * 该商品总库存;
     */
    @ApiModelProperty(name = "totalNum", value = "该商品总库存")
    private int totalNum;

    /**
     * 订单类型
     */
    @ApiModelProperty(name = "orderType", value = "订单类型")
    private String orderType;

    @ApiModelProperty(name = "updateDeleteState", value = "更新状态 0：不可编辑 1：可编辑")
    private String updateDeleteState;

    /**
     * 商品属性;中文
     */
    @ApiModelProperty(name = "attributeShortCn", value = "商品属性;中文")
    private String attributeCn;

    /**
     * 商品属性;短名称
     */
    @ApiModelProperty(name = "attributeShortCn", value = "商品属性;短名称")
    private String attributeShortCn;

    /**
     * 适用人群
     */
    @ApiModelProperty(name = "targetUser", value = "适用人群")
    private String targetUser;
}
