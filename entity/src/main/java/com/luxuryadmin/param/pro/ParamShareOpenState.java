package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamShareOpenState
 * @Author: ZhangSai
 * Date: 2021/6/30 16:18
 */
@Data
@ApiModel(description = "编辑分享状态类")
public class ParamShareOpenState  extends ParamToken {
    @ApiModelProperty(value = "店铺id", name = "shopId", hidden = true)
    private int shopId;

    @ApiModelProperty(value = "是否开启访客功能 0 未开启 1已开启", name = "openShareUser")
    @NotBlank(message = "是否开启访客功能不为空")
    private String openShareUser;
}
