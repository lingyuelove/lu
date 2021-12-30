package com.luxuryadmin.param.biz;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.param.biz
 * @ClassName: ParamShopRecommendAdd
 * @Author: ZhangSai
 * Date: 2021/5/27 21:31
 */
@Data
@ApiModel(description = "友商推荐新增")
public class ParamShopRecommendAdd {
    @ApiModelProperty(value = "店铺id", name = "shopId", required = true)
//    @Pattern(regexp = "^[0-9]{5,9}$", message = "店铺参数非法!")
//    @NotNull(message = "[shopId]不允许为空!")
    private List<String> shopId;
}
