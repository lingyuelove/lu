package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoClassifyTypeGrandSonList
 * @Author: ZhangSai
 * Date: 2021/8/3 11:25
 */
@Data
@ApiModel(value="补充信息分类三级显示实体参数", description="补充信息分类三级显示实体参数")
public class VoClassifyTypeGrandSonList {

    @ApiModelProperty(value = "主键Id,逻辑id,软件内部关联")
    private Integer id;
    /**
     * 分类名称;限长50个汉字
     */
    @ApiModelProperty(value = "分类名称;限长50个汉字")
    private String name;

    @ApiModelProperty(value = "序号排序")
    private Integer sort;

    @ApiModelProperty(value = "上级id")
    private Integer pId;
}
