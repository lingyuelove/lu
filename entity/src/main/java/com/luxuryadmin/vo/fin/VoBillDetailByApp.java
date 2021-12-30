package com.luxuryadmin.vo.fin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 帐单详情
 *
 * @author zhangSai
 * @date 2021/04/23 14:19:52
 */
@Data
@ApiModel(value = "帐单详情", description = "帐单详情")
public class VoBillDetailByApp {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id", name = "billId")
    private Integer billId;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id", name = "shopId")
    private Integer shopId;

    /**
     * 总资金
     */
    @ApiModelProperty(value = "总资产", name = "totalMoney")
    private BigDecimal totalAllMoney;

    /**
     * 应有资金
     */
    @ApiModelProperty(value = "应有资金", name = "surplusMoney")
    private BigDecimal surplusAllMoney;

    /**
     * 商品成本
     */
    @ApiModelProperty(value = "商品成本", name = "changeAmount")
    private BigDecimal productAllInitPrice;

    @ApiModelProperty(value = "日账单集合列表", name = "billDayPageByApps")
    private List<VoBillDayPageByApp> billDayPageByApps;

    @ApiModelProperty(value = "分页参数", name = "pageNum")
    private Integer pageNum;

    @ApiModelProperty(value = "分页参数", name = "pageSize")
    private Integer pageSize;

    @ApiModelProperty(value = "是否存在下一页", name = "hasNextPage")
    private Boolean  hasNextPage;

}
