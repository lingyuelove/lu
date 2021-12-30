package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 商品编辑价格数量类
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:59
 */
@Data
@ApiModel(value="商品编辑价格数量类", description="商品编辑价格数量类")
public class ParamProductUploadPrice extends ParamToken {
    /**
     * 商品业务逻辑id
     */
    @ApiModelProperty(value = "商品业务逻辑id;更新商品时,请赋值;", name = "bizId", required = true)
    private String bizId;

    /**
     * 成本价
     */
    @ApiModelProperty(value = "成本价;不填默认为0", name = "initPrice", required = true)
    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "成本价-格式错误")
    @Length(max = 15,message = "价格超出范围!")
    private String initPrice;

    /**
     * 友商价
     */
    @ApiModelProperty(value = "友商价;不填默认为0", name = "tradePrice", required = true)
    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "友商价-格式错误")
    @Length(max = 15,message = "价格超出范围!")
    private String tradePrice;

    /**
     * 代理价
     */
    @ApiModelProperty(value = "代理价;不填默认为0", name = "agencyPrice", required = true)
    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "代理价-格式错误")
    @Length(max = 15,message = "价格超出范围!")
    private String agencyPrice;

    /**
     * 销售价(卖客价)
     */
    @ApiModelProperty(value = "卖客售价(卖客价);不填默认为0", name = "salePrice", required = true)
    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "销售价-格式错误")
    @Length(max = 15,message = "价格超出范围!")
    private String salePrice;

    /**
     * 商品库存
     */
    @ApiModelProperty(value = "商品库存;不填默认为1", name = "totalNum", required = true)
    @Min(value =1,message = "库存不能小于1")
    @Max(value =999,message = "库存数量最大为999")
    @NotBlank(message = "商品库存不允许为空")
    private String totalNum;
    /**
     *  成本价备注
     */
    @ApiModelProperty(value = "成本价备注", name = "changeInitPriceRemark", required = false)
    @Length(max = 250, message = "成本价备注必须≤250个字符")
    private String changeInitPriceRemark;

}
