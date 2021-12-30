package com.luxuryadmin.param.ord;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Administrator
 */
@Data
public class ParamOrderUploadEntrust extends ParamToken {
    @ApiModelProperty(name = "orderId", required = true, value = "订单id")
    private String orderBizId;

    /**
     * 结款凭证
     */
    @ApiModelProperty(value = "结款凭证", name = "entrustImg", required = false)
    private String entrustImg;

    /**
     * 结款备注
     */
    @ApiModelProperty(value = "结款备注", name = "entrustRemark", required = false)
    @Length(max = 50, message = "结款备注必须≤50个字符")
    private String entrustRemark;

    @ApiModelProperty(hidden = true)
    private Integer userId;
}
