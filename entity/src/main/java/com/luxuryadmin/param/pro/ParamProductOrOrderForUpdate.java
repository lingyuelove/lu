package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamProductOrOrderForUpdate
 * @Author: ZhangSai
 * Date: 2021/7/5 16:21
 */
@ApiModel(description = "删除模块--编辑订单/商品备注")
@Data
public class ParamProductOrOrderForUpdate extends ParamProductOrOrderForDelete{

    /**
     * 删除备注
     */
    @ApiModelProperty(name = "deleteRemark", value = "删除备注")
    private String deleteRemark;
}
