package com.luxuryadmin.param.pay;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.servlet.tags.Param;

import javax.validation.constraints.NotBlank;

/**
 * 创建微信支付订单
 *
 * @author sanjin
 * @Date 2020-08-21
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "取消开单--前端接收参数模型")
@Data
public class ParamWxCreateOrder extends ParamToken {

    /**
     * 示例值：iOS, Android, Wap
     */
    @ApiModelProperty(name = "sign", required = true, value = "sign不允许为空")
    @NotBlank(message = "[sign]参数错误")
    private String sign;

    /**
     * 随机字符串
     */
    @ApiModelProperty(name = "randomStr", required = true, value = "随机字符串")
    private String randomStr;

}
