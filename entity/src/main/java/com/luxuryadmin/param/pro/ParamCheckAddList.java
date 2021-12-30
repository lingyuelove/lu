package com.luxuryadmin.param.pro;


import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value="新增盘点参数", description="新增盘点参数")
public class ParamCheckAddList extends ParamToken {



    /**
     * 商品属性表的code
     */
    @ApiModelProperty(value = "商品属性表的code集合，用逗号分隔", name = "fkProAttributeCodes", required = false)
//    @NotBlank(message = "商品属性表的code集合，用逗号分隔不为空")
    private String fkProAttributeCodes;

    /**
     * 销售价(分)最低
     */
    @ApiModelProperty(value = "销售价(分)最低", name = "salePriceStart", required = false)
    private BigDecimal salePriceStart;

    /**
     * 销售价(分)最高
     */
    @ApiModelProperty(value = "销售价(分)最高", name = "salePriceEnd", required = false)
    private BigDecimal salePriceEnd;

    /**
     * 盘点名称
     */
    @ApiModelProperty(value = "盘点名称", name = "checkName", required = false)
    private String checkName;

    /**
     * 商品分类名称; 和分类列表对应; 默认0:无分类;
     */
    @ApiModelProperty(value = "商品分类名称集合，用逗号分隔", name = "fkProClassifyCodes", required = false)
    private String fkProClassifyCodes;

    /**
     * 盘点开始时间
     */
    @ApiModelProperty(value = "盘点开始时间", name = "startTime", required = false)
    private String startTime;

    /**
     * 盘点结束时间
     */
    @ApiModelProperty(value = "盘点结束时间", name = "endTime", required = false)
    private String endTime;
    @ApiModelProperty(value = "盘点类型 temp:临时仓； warehouse:仓库", name = "type", required = false)
//    @NotBlank(message = "盘点类型不为空")
    private String type;

    @ApiModelProperty(value = "临时仓id", name = "proTempId", required = false)
    @Pattern(regexp = "^[0-9]+$", message = "临时仓id--参数错误")
    private String tempId;
}
