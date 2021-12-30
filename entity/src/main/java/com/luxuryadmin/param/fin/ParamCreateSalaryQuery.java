package com.luxuryadmin.param.fin;


import com.luxuryadmin.common.aop.check.DateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 薪资提成明细--前端接收参数模型
 *
 * @author monkey king
 * @date 2020-09-24 03:58:48
 */
@ApiModel(description = "薪资提成明细")
@Data
public class ParamCreateSalaryQuery extends ParamSalaryDetailQuery {

    /**
     * 底薪
     */
    @ApiModelProperty(name = "basicMoney", required = true, value = "底薪(分)")
    @Pattern(regexp = "^[0-9.]+$", message = "底薪--参数错误")
    @NotBlank(message = "底薪不能为空")
    private String basicMoney;

    /**
     * 其他款项
     */
    @ApiModelProperty(name = "elseMoney", required = true, value = "其他款项(分)")
    @Pattern(regexp = "^[-0-9.]+$", message = "其他款项--参数错误")
    @NotBlank(message = "其他款项不能为空")
    private String elseMoney;

    /**
     * 方案类型; 多个用逗号隔开; 2-1,2-2,2-3,3-1;<br/>
     * 格式: 提成模块-方案,订单类型:值,订单类型2:值2;多个用分号隔开
     */
    @ApiModelProperty(name = "schemeType", required = true,
            value = "方案类型;格式: 提成模块-方案,订单类型:值,订单类型2:值2;多个用分号隔开")
    @NotBlank(message = "方案类型不允许为空")
    private String schemeType;


    /**
     * 回收成本百分比
     */
    @ApiModelProperty(name = "recycleInitPricePercent", required = true,
            value = "回收成本百分比(%) 默认为0")
    @Pattern(regexp = "^[0-9.]*$", message = "回收成本百分比--参数错误")
    @NotBlank(message = "回收成本百分比不允许为空")
    private String recycleInitPricePercent;

    /**
     * 回收单价(分)/每件
     */
    @ApiModelProperty(name = "recycleUnitPrice", required = true,
            value = "回收单价(分)/每件 默认为0")
    @Pattern(regexp = "^[0-9.]*$", message = "回收单价--参数错误")
    @NotBlank(message = "回收单价不允许为空")
    private String recycleUnitPrice;


    /**
     * 回收利润百分比
     */
    @ApiModelProperty(name = "recycleProfitPercent", required = true,
            value = "TA回收产生的利润提点(%) 默认为0")
    @Pattern(regexp = "^[0-9.]*$", message = "回收利润百分比--参数错误")
    @NotBlank(message = "回收利润百分比不允许为空")
    private String recycleProfitPercent;


    /**
     * 服务利润百分比
     */
    @ApiModelProperty(name = "serviceProfitPercent", required = true,
            value = "服务利润提点(%) 默认为0")
    @Pattern(regexp = "^[0-9.]*$", message = "服务利润提点--参数错误")
    @NotBlank(message = "维修保养不允许为空")
    private String serviceProfitPercent;

    /**
     * 发放标识符
     */
    @ApiModelProperty(name = "salaryState", required = true,
            value = "0:未发放; 1:已发放;")
    @Pattern(regexp = "^[01]$", message = "发放标识符--参数错误")
    @NotBlank(message = "标识符不允许为空")
    private String salaryState;


    /**
     * 0:新增; 1:修改
     */
    @ApiModelProperty(name = "updateOrSave", required = true,
            value = "0:新增; 1:修改")
    @Pattern(regexp = "^[01]$", message = "修改标识符--参数错误")
    @NotBlank(message = "修改标识符不允许为空")
    private String updateOrSave;


}
