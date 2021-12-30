package com.luxuryadmin.param.oss;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.param.oss
 * @ClassName: ParamImgToken
 * @Author: ZhangSai
 * Date: 2021/11/5 17:02
 */
@Data
public class ParamImg extends ParamToken{
    @ApiModelProperty(value = "所属类型", required = true, name = "type")
    private String type;
    @ApiModelProperty(value = "所属子类型", required = true, name = "sonType")
    private String sonType;
}
