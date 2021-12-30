package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品所在临时仓
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:59
 */
@Data
@ApiModel(value="商品所在临时仓", description="商品所在临时仓")
public class VoTempForPro {

    @ApiModelProperty(value = "主键Id临时仓编号", name = "tempId;")
    private String tempId;

    /**
     * 临时仓名称
     */
    @ApiModelProperty(value = "临时仓名称", name = "tempName")
    private String tempName;
}
