package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoClassifyList
 * @Author: ZhangSai
 * Date: 2021/8/5 18:36
 */
@Data
@ApiModel(value="补充信息分类显示参数", description="补充信息分类显示参数")
public class VoClassify extends VoProClassify{

//    @ApiModelProperty(value = "补充信息分类集合显示")
//    private List<VoClassifyTypeList> list;

    @ApiModelProperty(value = "补充信息分类二级集合显示")
    private List<VoClassifyTypeSonList> son;
}
