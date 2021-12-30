package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamProductClassifyAddList
 * @Author: ZhangSai
 * Date: 2021/8/3 15:00
 */
@Data
@ApiModel(value="新增商品补充信息分类实体参数", description="新增商品补充信息分类实体参数")
public class ParamProductClassifyAddList {
    /**
     * 商品补充信息分类id
     */
//    @ApiModelProperty(value = "商品补充信息分类id")
//    private Integer classifyTypeId;

    @ApiModelProperty(value = "商品补充信息子类集合显示")
    private List<ParamProductClassifySunAddList> productClassifySunAddLists;
}
