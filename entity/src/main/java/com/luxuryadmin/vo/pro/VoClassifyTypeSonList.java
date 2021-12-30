package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoClassifyTypeSonList
 * @Author: ZhangSai
 * Date: 2021/8/3 11:24
 */
@Data
@ApiModel(value="补充信息分类二级显示实体参数", description="补充信息分类二级显示实体参数")
public class VoClassifyTypeSonList {
    @ApiModelProperty(value = "主键Id,逻辑id,软件内部关联")
    private Integer id;
    /**
     * 分类名称;限长50个汉字
     */
    @ApiModelProperty(value = "分类名称;限长50个汉字")
    private String name;

    @ApiModelProperty(value = "类型;1:下拉框;2:单行输入框;3:单选框;4:复选框;5:单选标签;6:复选标签;")
    private String choseType;

    @ApiModelProperty(value = "上级分类code")
    private String classifyCode;

    @ApiModelProperty(value = "上级分类名称;限长50个汉字")
    private String classifyName;
    @ApiModelProperty(value = "默认 0 不适用 1 适用")
    private Integer state;
    @ApiModelProperty(value = "序号排序")
    private Integer sort;
    @ApiModelProperty(value = "补充信息分类三级级集合显示")
    private List<VoClassifyTypeGrandSonList> grandson;

    @ApiModelProperty(value = "分类名称;限长50个汉字")
    private String sonName;

    @ApiModelProperty(value = "上级id")
    private Integer pId;

    @ApiModelProperty(value = "分类内容字段")
    private String content;
}
