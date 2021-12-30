package com.luxuryadmin.param.fin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 添加记账流水前端参数
 */
@Data
@ApiModel(description = "员工角色--前端参数模型")
public class ParamFinShopRecordAdd {

    /**
     * 流水类型 支出|收入
     */
    @ApiModelProperty(name = "inoutType", required = true, value = "流水类型 in|收入 out|支出",
            allowableValues = "in,out")
    @NotNull(message="流水类型不能为空")
    private String inoutType;

    /**
     * 流水变动金额
     */
    @ApiModelProperty(name = "changeAmount", required = true, value = "流水变动金额 字符串类型")
    @NotNull(message="流水类型不能为空")
    private String changeAmount;

    /**
     * 流水类别
     */
    @ApiModelProperty(name = "type", required = true, value = "流水类别，中文，如【商品销售】，不包含括号")
    @NotNull(message="流水类别不能为空")
    private String type;

    /**
     * 流水发生日期
     */
    @ApiModelProperty(name = "happenTime", required = true, value = "流水发生日期")
    @NotNull(message="流水发生日期不能为空")
    private String happenTime;

    /**
     * 流水发生日期
     */
    @ApiModelProperty(name = "note", required = false, value = "备注")
    private String note;

    /**
     * 流水详情图片地址
     */
    @ApiModelProperty(value = "流水详情图片地址", name = "imgUrlDetail", required = false)
    private String imgUrlDetail;

}
