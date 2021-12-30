package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author taoqimin
 * @date 2021-09-16 23:00:23
 */
@Data
public class ParamSendProDeliver extends ParamToken {

    @ApiModelProperty(name = "deliverId", required = true, value = "发货id")
    @NotNull(message = "发货id不能为空")
    private String deliverId;

    @ApiModelProperty(name = "deliverType", required = true, value = "发货方式")
    @NotBlank(message = "发货方式不能为空")
    private String deliverType;

    @ApiModelProperty(name = "deliverImgs", required = false, value = "发货凭证图")
    private String deliverImgs;

    @ApiModelProperty(name = "logisticsNumber", required = false,value = "物流单号")
    @Length(max = 15, message = "物流单号长度过长")
    private String logisticsNumber;
    @ApiModelProperty(name = "checkPhoneNo", required = false,value = "物流单号所需手机号后四位")
    private String checkPhoneNo;
    @ApiModelProperty(hidden = true)
    private Integer currentUserId;


}
