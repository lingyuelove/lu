package com.luxuryadmin.vo.biz;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.vo.biz
 * @ClassName: VoUnionVerify
 * @Author: ZhangSai
 * Date: 2021/11/3 18:25
 */
@Data
@ApiModel(description = "商家联盟认证信息")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoUnionVerify {
    @ApiModelProperty(value = "状态 0 未审核 1已通过 2未通过/去认证")
    private String state;
}
