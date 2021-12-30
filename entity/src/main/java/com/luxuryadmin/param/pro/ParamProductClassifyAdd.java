package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamProductClassifyList
 * @Author: ZhangSai
 * Date: 2021/8/6 14:37
 */
@Data
@ApiModel(value="新增商品补充信息分类实体参数", description="新增商品补充信息分类实体参数")
public class ParamProductClassifyAdd {
    @ApiModelProperty(value = "商品补充信息子类集合显示")
    private List<ParamProductClassifySunAddList> productClassifySunAddLists;
    @ApiModelProperty(value = "店铺id")
    private Integer shopId;
    @ApiModelProperty(value = "商品id")
    private Integer productId;
    @ApiModelProperty(value = "用户id")
    private Integer userId;
}
