package com.luxuryadmin.param.ord;

import com.luxuryadmin.param.common.ParamBasic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.param.ord
 * @ClassName: ParamPrintTplUploadForAdmin
 * @Author: ZhangSai
 * Date: 2021/9/28 13:58
 */
@Data
@ApiModel(value="订单打印模板--新增/编辑", description="订单打印模板--新增/编辑")
public class ParamPrintTplUploadForAdmin  {
    @ApiModelProperty(value = "导出文本" , name = "context", required = true)
    private String context;
}
