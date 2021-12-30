package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamConveyProductQuery
 * @Author: ZhangSai
 * Date: 2021/11/23 10:38
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "商品页面查询参数实体")
@Data
public class ParamConveyProductQuery extends ParamToken {
    @ApiModelProperty(value = "商品传送表的id字段,主键id", required = true)
    @NotNull(message = "商品传送表的id字段不能为空")
    private Integer conveyId;
    /**
     * 商品名称
     */
    @ApiModelProperty(name = "proName", required = false, value = "商品名称")
    @Length(max = 50, message = "商品名称必须≤50个字符")
    private String proName;
    @ApiModelProperty(name = "uniqueCode", hidden = true, value = "独立编码")
    private String uniqueCode;
    @ApiModelProperty(value = "商品价格类型（initPrice：成本价，tradePrice：友商价，agencyPrice：代理价，salePrice：销售价）", name = "defaultPrice", required = false)
    private String defaultPrice;

    /**
     * 分类编码;all:全部
     */
    @ApiModelProperty(name = "classifyCode", required = false, value = "分类编码(传code值到服务器);")
    @Length(min = 2, max = 20, message = "分类编码长度在2~20个字符")
    private String classifyCode;

    @ApiModelProperty(name = "shopId", hidden = true, value = "店铺id")
    private Integer shopId;

    @ApiModelProperty(value = "接收状态 0待确认 1已确认", hidden = true)
    private String receiveState;

    @ApiModelProperty(name = "type",value = "寄卖传送标识 寄卖传送商品使用 type类型  send发送方 receive接收方")
//    @NotBlank(message = "寄卖传送标识不能为空")
    private String type;
}
