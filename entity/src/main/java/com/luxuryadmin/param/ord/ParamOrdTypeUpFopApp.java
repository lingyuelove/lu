package com.luxuryadmin.param.ord;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.servlet.http.HttpServletRequest;

/**
 * @PackgeName: com.luxuryadmin.param.ord
 * @ClassName: ParamOrdTypeSaveOrUpFopApp
 * @Author: ZhangSai
 * Date: 2021/7/27 16:00
 */
@ApiModel(description = "订单管理-订单列表参数实体")
@Data
public class ParamOrdTypeUpFopApp extends ParamToken {
    @ApiModelProperty(value = "主键id", name = "id", required = true)
    private  Integer id;
    @ApiModelProperty(value = "店铺id", name = "shopId", hidden = true)
    private  Integer shopId;
    @ApiModelProperty(value = "用户id", name = "userId", hidden = true)
    private Integer userId;
    @ApiModelProperty(value = "订单类型名称", name = "ordTypeName", required = true)
    @Length(max = 6, message = "shareBatch--参数错误")
    private String ordTypeName;
    @ApiModelProperty(value = "订单类型名称原名称", name = "ordTypeNameOld", hidden = true )
    private String ordTypeNameOld;
    @ApiModelProperty(hidden = true)
    private HttpServletRequest request;
}
