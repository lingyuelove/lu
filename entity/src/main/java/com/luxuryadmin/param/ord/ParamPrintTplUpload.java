package com.luxuryadmin.param.ord;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.param.ord
 * @ClassName: ParamPrintTplUpload
 * @Author: ZhangSai
 * Date: 2021/9/27 11:02
 */
@Data
@ApiModel(value="订单打印模板--新增/编辑", description="订单打印模板--新增/编辑")
public class ParamPrintTplUpload extends ParamToken {
    @ApiModelProperty(value = "导出文本" , name = "context", required = true)
    private String context;

    @ApiModelProperty(value = "用户id" , name = "userId", hidden = true)
    private Integer userId;
    @ApiModelProperty(value = "店铺id" , name = "shopId", hidden = true)
    private Integer shopId;
}
