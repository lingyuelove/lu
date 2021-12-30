package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoClassifyTypeSon
 * @Author: ZhangSai
 * Date: 2021/9/17 11:10
 */
@Data
@ApiModel(value="补充信息分类实体参数", description="补充信息分类实体参数")
public class VoClassifyTypeSon {
    @ApiModelProperty(value = "补充信息分类二级显示实体参数")
    private List<VoClassifyTypeSonList> classifyTypeSonLists;

    @ApiModelProperty(value = "公价")
    private String publicPrice;
}
